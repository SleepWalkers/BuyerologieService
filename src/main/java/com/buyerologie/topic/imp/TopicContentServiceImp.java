package com.buyerologie.topic.imp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.topic.TopicContentService;
import com.buyerologie.topic.TopicTitleService;
import com.buyerologie.topic.dao.TopicContentDao;
import com.buyerologie.topic.exception.TopicContentNullException;
import com.buyerologie.topic.exception.TopicCreatorNullException;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.exception.TopicTitleNullException;
import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.user.UserService;
import com.buyerologie.utils.PageUtil;
import com.buyerologie.utils.StringUtil;

@Service("topicContentService")
public class TopicContentServiceImp implements TopicContentService {

    @Resource
    private TopicContentDao   topicContentDao;

    @Resource
    private UserService       userService;

    @Resource
    private TopicTitleService topicTitleService;

    @Override
    public TopicContent getContent(int contentId) {
        if (contentId <= contentId) {
            return null;
        }
        return topicContentDao.selectById(contentId);
    }

    @Override
    public TopicContent getMainContent(int titleId) {
        if (titleId <= 0) {
            return null;
        }

        return topicContentDao.selectMainContent(titleId);
    }

    @Override
    public List<TopicContent> getSubContents(int titleId, int page, int pageSize) {
        if (titleId <= 0) {
            return null;
        }
        return topicContentDao.selectByTitleId(titleId, PageUtil.getStart(page, pageSize),
            PageUtil.getLimit(page, pageSize));
    }

    @Override
    public int countContentNum(int titleId) {
        if (titleId <= 0) {
            return 0;
        }
        return topicContentDao.countByTitleId(titleId);
    }

    @Override
    public void publishContent(TopicContent topicContent) throws TopicException {
        if (topicContent == null) {
            return;
        }
        validate(topicContent);
        topicContentDao.insert(topicContent);
    }

    private void validate(TopicContent topicContent) throws TopicException {

        if (userService.getUser(topicContent.getCreatorId()) == null) {
            throw new TopicCreatorNullException();
        }

        if (topicTitleService.get(topicContent.getTitleId()) == null) {
            throw new TopicTitleNullException();
        }
    }

    @Override
    public void editContent(TopicContent topicContent) throws TopicException {
        if (topicContent == null) {
            return;
        }
        TopicContent originalTopicContent = topicContentDao.selectById(topicContent.getId());
        if (originalTopicContent == null) {
            throw new TopicContentNullException();
        }
        validate(originalTopicContent);
        originalTopicContent.setContent(topicContent.getContent());
        originalTopicContent.setVideoId(topicContent.getVideoId());
        topicContentDao.updateById(originalTopicContent);
    }

    @Override
    public void deleteContent(int contentId) {
        if (contentId <= 0) {
            return;
        }
        topicContentDao.deleteById(contentId);
    }

    @Override
    public void deleteAllContent(int titleId) {
        if (titleId <= 0) {
            return;
        }
        topicContentDao.deleteByTitleId(titleId);
    }

    @Override
    public int getTopicId(String videoId) {
        if (StringUtils.isBlank(videoId)) {
            return 0;
        }
        Integer topicId = topicContentDao.selectByVideoId(videoId);
        return topicId == null ? 0 : topicId;
    }

    @Override
    public List<TopicContent> getVideoContents(List<Integer> titleIds) {
        if (titleIds == null || titleIds.isEmpty()) {
            return null;
        }
        List<TopicContent> videoContents = topicContentDao.selectVideoContentByTitleId(StringUtil
            .buildIntListToString(titleIds, ","));

        return videoContents;
    }

}
