package com.buyerologie.topic.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.common.exception.PageNotFoundException;
import com.buyerologie.topic.CommentService;
import com.buyerologie.topic.CourseService;
import com.buyerologie.topic.TopicContentService;
import com.buyerologie.topic.TopicTitleService;
import com.buyerologie.topic.enums.TopicCate;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.topic.vo.Course;
import com.buyerologie.user.UserService;
import com.buyerologie.user.model.User;

@Service("courseService")
public class CourseServiceImp implements CourseService {

    @Resource
    private UserService         userService;

    @Resource
    private CommentService      commentService;

    @Resource
    private TopicTitleService   topicTitleService;

    @Resource
    private TopicContentService topicContentService;

    private static final int    DEFAULT_DETAILED_NEWS_PAGE      = 1;

    private static final int    DEFAULT_DETAILED_NEWS_PAGE_SIZE = 3;

    @Override
    public List<Course> getCourses(int page, int pageSize) {
        List<TopicTitle> topicTitles = topicTitleService.getTitles(TopicCate.COURSE.getCateId(),
            page, pageSize);
        if (topicTitles == null || topicTitles.isEmpty()) {
            return null;
        }
        List<Course> courses = new ArrayList<>();
        for (TopicTitle topicTitle : topicTitles) {
            TopicContent topicContent = topicContentService.getMainContent(topicTitle.getId());
            if (topicContent == null) {
                continue;
            }
            courses.add(new Course(null, topicTitle, topicContent));
        }
        return courses;
    }

    @Override
    public Course getCourse(int courseId) throws PageNotFoundException {
        if (courseId <= 0) {
            throw new PageNotFoundException();
        }
        TopicTitle topicTitle = topicTitleService.get(courseId);
        if (topicTitle == null) {
            throw new PageNotFoundException();
        }
        User creator = userService.getUser(topicTitle.getCreatorId());
        if (creator == null) {
            return null;
        }

        TopicContent topicContent = topicContentService.getMainContent(courseId);
        if (topicContent == null) {
            throw new PageNotFoundException();
        }
        Course course = new Course(creator, topicTitle, topicContent);

        course.setComments(commentService.getListComments(courseId, DEFAULT_DETAILED_NEWS_PAGE,
            DEFAULT_DETAILED_NEWS_PAGE_SIZE));
        return course;
    }

    @Override
    public int count() {
        return topicTitleService.countTitleNum(TopicCate.COURSE.getCateId());
    }

    @Override
    public void createCourse(int creatorId, boolean isFree, String title, String videoId,
                             String content) throws TopicException {
        TopicTitle topicTitle = new TopicTitle();
        topicTitle.setCateId(TopicCate.COURSE.getCateId());
        topicTitle.setCreatorId(creatorId);
        topicTitle.setTitle(title);
        topicTitle.setIsFree(isFree);
        topicTitleService.createTitle(topicTitle);

        TopicContent topicContent = new TopicContent();
        topicContent.setContent(content);
        topicContent.setCreatorId(creatorId);
        topicContent.setTitleId(topicTitle.getId());
        topicContent.setParentId(0);
        topicContent.setVideoId(videoId);
        topicContentService.publishContent(topicContent);
    }

    @Override
    public void editCourse(int id, boolean isFree, String title, String videoId, String content)
                                                                                                throws TopicException {

        TopicTitle topicTitle = topicTitleService.get(id);
        if (topicTitle == null) {
            return;
        }
        topicTitle.setTitle(title);
        topicTitle.setIsFree(isFree);
        topicTitleService.editTitle(topicTitle);

        TopicContent topicContent = topicContentService.getMainContent(id);
        if (topicContent == null) {
            return;
        }
        topicContent.setContent(content);
        topicContent.setVideoId(videoId);
        topicContentService.editContent(topicContent);
    }

    @Override
    public void deleteCourse(int courseId) {
        topicTitleService.deleteTitle(courseId);
        topicContentService.deleteAllContent(courseId);
    }

    @Override
    public int getCourseId(String videoId) {
        if (StringUtils.isBlank(videoId)) {
            return 0;
        }

        return topicContentService.getTopicId(videoId);
    }

    @Override
    public List<String> getCourseVideoIds(List<Integer> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return null;
        }
        List<TopicContent> videoContents = topicContentService.getVideoContents(courseIds);
        if (videoContents == null || videoContents.isEmpty()) {
            return null;
        }
        List<String> videoIds = new ArrayList<>();
        for (TopicContent videoContent : videoContents) {
            videoIds.add(videoContent.getVideoId());
        }
        return videoIds;
    }

}
