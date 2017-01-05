package com.buyerologie.topic.imp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.topic.TopicTitleService;
import com.buyerologie.topic.dao.ImageDao;
import com.buyerologie.topic.dao.TopicImageRelationDao;
import com.buyerologie.topic.dao.TopicTitleDao;
import com.buyerologie.topic.enums.ImageType;
import com.buyerologie.topic.exception.TopicCreatorNullException;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.exception.TopicTitleBlankException;
import com.buyerologie.topic.exception.TopicTitleNullException;
import com.buyerologie.topic.model.Image;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.user.UserService;
import com.buyerologie.utils.PageUtil;
import com.buyerologie.utils.StringUtil;

@Service("topicTitleService")
public class TopicTitleServiceImp implements TopicTitleService {

    @Resource
    private ImageDao              imageDao;

    @Resource
    private TopicTitleDao         topicTitleDao;

    @Resource
    private TopicImageRelationDao topicImageRelationDao;

    @Resource
    private UserService           userService;

    @Override
    public TopicTitle get(int id) {
        if (id <= 0) {
            return null;
        }
        return topicTitleDao.selectById(id);
    }

    @Override
    public List<Image> getImages(int titleId) {
        return getImages(titleId, ImageType.LIST);
    }

    @Override
    public List<Image> getImages(int titleId, ImageType imageType) {
        if (titleId <= 0) {
            return null;
        }
        return imageDao.selectByTopicId(titleId, imageType.getType());
    }

    @Override
    public List<TopicTitle> getTitles(int cateId, int page, int pageSize) {
        if (cateId <= 0) {
            return null;
        }
        return topicTitleDao.selectByCateId(cateId, PageUtil.getStart(page, pageSize),
            PageUtil.getLimit(page, pageSize));
    }

    @Override
    public int countTitleNum(int cateId) {
        if (cateId <= 0) {
            return 0;
        }
        return topicTitleDao.countByCateId(cateId);
    }

    @Override
    public void createTitle(TopicTitle topicTitle) throws TopicException {
        if (topicTitle == null) {
            return;
        }
        validate(topicTitle);
        topicTitleDao.insert(topicTitle);
    }

    private void validate(TopicTitle topicTitle) throws TopicException {
        if (StringUtils.isBlank(topicTitle.getTitle())) {
            throw new TopicTitleBlankException();
        }
        if (userService.getUser(topicTitle.getCreatorId()) == null) {
            throw new TopicCreatorNullException();
        }
    }

    @Override
    public void editTitle(TopicTitle topicTitle) throws TopicException {
        if (topicTitle == null) {
            return;
        }
        TopicTitle topicTitleOriginal = topicTitleDao.selectById(topicTitle.getId());
        if (topicTitleOriginal == null) {
            throw new TopicTitleNullException();
        }
        validate(topicTitle);
        topicTitleOriginal.setCateId(topicTitle.getCateId());
        topicTitleOriginal.setCreatorId(topicTitle.getCreatorId());
        topicTitleOriginal.setTitle(topicTitle.getTitle());
        topicTitleDao.updateById(topicTitleOriginal);
    }

    @Override
    public void deleteTitle(int titleId) {
        if (titleId <= 0) {
            return;
        }
        topicTitleDao.deleteById(titleId);
    }

    @Override
    public List<TopicTitle> getTitles(List<Integer> topicIds) {
        if (topicIds == null || topicIds.isEmpty()) {
            return null;
        }
        String topicIdsStr = StringUtil.buildIntListToString(topicIds, ",");
        return topicTitleDao.selectByTopicIds(topicIdsStr);
    }

}
