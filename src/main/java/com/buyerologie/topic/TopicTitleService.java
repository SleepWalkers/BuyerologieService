package com.buyerologie.topic;

import java.util.List;

import com.buyerologie.topic.enums.ImageType;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.exception.TopicPublishException;
import com.buyerologie.topic.model.Image;
import com.buyerologie.topic.model.TopicTitle;

public interface TopicTitleService {

    /**
     * 根据id获得title
     * @param titleId
     * @return
     */
    TopicTitle get(int id);

    List<Image> getImages(int titleId);

    List<Image> getImages(int titleId, ImageType imageType);

    /**
     * 分页获取title的列表
     * @return
     */
    List<TopicTitle> getTitles(int cateId, int page, int pageSize);

    List<TopicTitle> getTitles(List<Integer> topicIds);

    /**
     * 统计title数量
     * @return
     */
    int countTitleNum(int cateId);

    /**
     * 发表title
     * @throws TopicPublishException 
     */
    void createTitle(TopicTitle topicTitle) throws TopicException;

    /**
     * 编辑title
     * @throws TopicException 
     */
    void editTitle(TopicTitle topicTitle) throws TopicException;

    void deleteTitle(int titleId);
}
