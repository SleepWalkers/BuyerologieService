package com.buyerologie.topic.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.topic.CommentService;
import com.buyerologie.topic.TopicContentService;
import com.buyerologie.topic.exception.TopicContentNullException;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.vo.ListComment;
import com.buyerologie.user.UserService;

@Service("commentService")
public class CommentServiceImp implements CommentService {

    @Resource
    private UserService         userService;

    @Resource
    private TopicContentService topicContentService;

    @Override
    public List<ListComment> getListComments(int topicId, int page, int pageSize) {
        if (topicId <= 0) {
            return null;
        }

        List<TopicContent> topicContents = topicContentService.getSubContents(topicId, page,
            pageSize);
        if (topicContents == null || topicContents.isEmpty()) {
            return null;
        }
        return buildListComment(topicContents);
    }

    @Override
    public int countComment(int newsId) {
        if (newsId <= 0) {
            return 0;
        }
        return topicContentService.countContentNum(newsId);
    }

    @Override
    public void publicComment(int topicId, int creatorId, String comment) throws TopicException {
        if (StringUtils.isBlank(comment)) {
            throw new TopicContentNullException();
        }
        TopicContent topicContent = new TopicContent();
        topicContent.setContent(comment);
        topicContent.setTitleId(topicId);
        topicContent.setParentId(topicId);
        topicContent.setCreatorId(creatorId);
        topicContentService.publishContent(topicContent);
    }

    private List<ListComment> buildListComment(List<TopicContent> topicContents) {
        if (topicContents == null || topicContents.isEmpty()) {
            return null;
        }
        List<ListComment> comments = new ArrayList<>();

        for (TopicContent topicContent : topicContents) {
            comments.add(new ListComment(userService.getUser(topicContent.getCreatorId()),
                topicContent));
        }
        return comments;
    }
}
