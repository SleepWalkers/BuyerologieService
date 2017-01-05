package com.buyerologie.topic.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buyerologie.common.exception.PageNotFoundException;
import com.buyerologie.topic.CommentService;
import com.buyerologie.topic.NewsService;
import com.buyerologie.topic.TopicContentService;
import com.buyerologie.topic.TopicTitleService;
import com.buyerologie.topic.dao.ImageDao;
import com.buyerologie.topic.dao.TopicImageRelationDao;
import com.buyerologie.topic.enums.ImageType;
import com.buyerologie.topic.enums.TopicCate;
import com.buyerologie.topic.exception.TopicException;
import com.buyerologie.topic.model.Image;
import com.buyerologie.topic.model.TopicContent;
import com.buyerologie.topic.model.TopicImageRelation;
import com.buyerologie.topic.model.TopicTitle;
import com.buyerologie.topic.vo.DetailedNews;
import com.buyerologie.topic.vo.ListNews;
import com.buyerologie.user.UserService;
import com.buyerologie.user.model.User;

@Service("newsService")
public class NewsServiceImp implements NewsService {

    @Resource
    private ImageDao              imageDao;

    @Resource
    private TopicImageRelationDao topicImageRelationDao;

    @Resource
    private UserService           userService;

    @Resource
    private CommentService        commentService;

    @Resource
    private TopicTitleService     topicTitleService;

    @Resource
    private TopicContentService   topicContentService;

    private static final int      DEFAULT_DETAILED_NEWS_PAGE      = 1;

    private static final int      DEFAULT_DETAILED_NEWS_PAGE_SIZE = 3;

    @Override
    public DetailedNews getNews(int id) throws PageNotFoundException {
        if (id <= 0) {
            throw new PageNotFoundException();
        }
        TopicTitle topicTitle = topicTitleService.get(id);
        if (topicTitle == null) {
            throw new PageNotFoundException();
        }
        User creator = userService.getUser(topicTitle.getCreatorId());
        if (creator == null) {
            return null;
        }

        TopicContent topicContent = topicContentService.getMainContent(id);
        if (topicContent == null) {
            throw new PageNotFoundException();
        }
        DetailedNews detailedNews = new DetailedNews(creator, topicTitle, topicContent);

        detailedNews.setComments(commentService.getListComments(id, DEFAULT_DETAILED_NEWS_PAGE,
            DEFAULT_DETAILED_NEWS_PAGE_SIZE));
        return detailedNews;
    }

    @Override
    public List<ListNews> getListNewses(int page, int pageSize) {

        List<TopicTitle> topicTitles = topicTitleService.getTitles(1, page, pageSize);
        if (topicTitles == null || topicTitles.isEmpty()) {
            return null;
        }

        List<ListNews> listNewses = new ArrayList<>();
        for (TopicTitle topicTitle : topicTitles) {
            listNewses
                .add(new ListNews(topicTitle, topicTitleService.getImages(topicTitle.getId())));
        }

        return listNewses;
    }

    @Override
    @Transactional
    public void createNews(int creatorId, String title, String content) throws TopicException {
        TopicTitle topicTitle = new TopicTitle();
        topicTitle.setCreatorId(creatorId);
        topicTitle.setTitle(title);
        topicTitle.setCateId(TopicCate.NEWS.getCateId());
        topicTitleService.createTitle(topicTitle);

        TopicContent topicContent = new TopicContent();
        topicContent.setTitleId(topicTitle.getId());
        topicContent.setParentId(0);
        topicContent.setCreatorId(creatorId);
        topicContent.setContent(content);

        topicContentService.publishContent(topicContent);
    }

    @Override
    public void editNews(int id, String title, String content) throws TopicException {
        TopicTitle topicTitle = topicTitleService.get(id);
        if (topicTitle == null) {
            return;
        }
        topicTitle.setTitle(title);
        topicTitleService.editTitle(topicTitle);

        TopicContent topicContent = topicContentService.getMainContent(id);
        if (topicContent == null) {
            return;
        }
        topicContent.setContent(content);
        topicContentService.editContent(topicContent);
    }

    @Override
    public void deleteNews(int newsId) {
        topicTitleService.deleteTitle(newsId);
        topicContentService.deleteAllContent(newsId);
    }

    @Override
    public List<ListNews> getListNews(List<Integer> newsIds) {
        return getListNews(newsIds, ImageType.LIST);
    }

    @Override
    public List<ListNews> getListNews(List<Integer> newsIds, ImageType imageType) {
        if (newsIds == null || newsIds.isEmpty()) {
            return null;
        }

        List<TopicTitle> topicTitles = topicTitleService.getTitles(newsIds);
        if (topicTitles == null || topicTitles.isEmpty()) {
            return null;
        }

        List<ListNews> listNewses = new ArrayList<>();
        for (TopicTitle topicTitle : topicTitles) {
            listNewses.add(new ListNews(topicTitle, topicTitleService.getImages(topicTitle.getId(),
                imageType)));
        }
        return listNewses;
    }

    @Override
    public int count() {
        return topicTitleService.countTitleNum(TopicCate.NEWS.getCateId());
    }

    @Override
    public List<Image> getImages(int id, ImageType imageType) {
        if (id <= 0 || imageType == null) {
            return null;
        }
        return imageDao.selectByTopicId(id, imageType.getType());
    }

    @Override
    public void addNewsImage(int id, String imagePath, ImageType imageType) {
        if (imageType == null) {
            return;
        }

        Image image = new Image();
        image.setImagePath(imagePath);
        imageDao.insert(image);

        topicImageRelationDao
            .insert(new TopicImageRelation(id, image.getId(), imageType.getType()));
    }

    @Override
    public void editImage(int imageId, String imagePath) {
        Image image = imageDao.selectById(imageId);
        if (image == null) {
            return;
        }
        image.setImagePath(imagePath);
        imageDao.updateById(image);
    }

    @Override
    public void delteNewsImage(int id, int imageId) {
        topicImageRelationDao.delete(imageId, id);
    }

}
