package com.buyerologie.topic;

import java.util.List;

import com.buyerologie.common.exception.PageNotFoundException;
import com.buyerologie.topic.enums.ImageType;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.model.Image;
import com.buyerologie.topic.vo.DetailedNews;
import com.buyerologie.topic.vo.ListNews;

public interface NewsService {

    DetailedNews getNews(int id) throws PageNotFoundException;

    List<ListNews> getListNewses(int page, int pageSize);

    List<ListNews> getListNews(List<Integer> newsIds);

    List<ListNews> getListNews(List<Integer> newsIds, ImageType imageType);

    List<Image> getImages(int id, ImageType imageType);

    void addNewsImage(int id, String imagePath, ImageType imageType);

    void editImage(int imageId, String imagePath);

    void delteNewsImage(int id, int imageId);

    int count();

    void createNews(int creatorId, String title, String content) throws TopicException;

    void editNews(int id, String title, String content) throws TopicException;

    void deleteNews(int newsId);
}
