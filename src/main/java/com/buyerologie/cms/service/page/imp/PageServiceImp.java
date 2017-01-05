package com.buyerologie.cms.service.page.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.cms.dao.CmsModuleDao;
import com.buyerologie.cms.dao.CmsPageDao;
import com.buyerologie.cms.dao.CmsPageModuleRelationDao;
import com.buyerologie.cms.model.CmsPage;
import com.buyerologie.cms.service.module.ModuleService;
import com.buyerologie.cms.service.page.PageService;
import com.buyerologie.cms.service.vo.PageVO;

@Service(value = "pageService")
public class PageServiceImp implements PageService {

    @Resource
    private CmsPageDao               cmsPageDao;

    @Resource
    private CmsModuleDao             cmsModuleDao;

    @Resource
    private CmsPageModuleRelationDao cmsPageModuleRelationDao;

    @Resource
    private ModuleService            moduleService;

    @Override
    public PageVO getPage(int pageId) {
        CmsPage page = cmsPageDao.selectById(pageId);
        return getPage(page);
    }

    @Override
    public PageVO getPage(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return getPage(cmsPageDao.selectByName(name));
    }

    private PageVO getPage(CmsPage page) {
        PageVO pageVO = null;

        if (page != null) {
            pageVO = new PageVO(page);
        }

        return pageVO;
    }

    @Override
    public List<PageVO> getPageVOs() {
        List<CmsPage> pages = cmsPageDao.selectAll();

        List<PageVO> pageVOs = null;

        if (pages != null && pages.size() > 0) {
            pageVOs = new ArrayList<PageVO>();

            for (CmsPage page : pages)
                pageVOs.add(getPage(page));

        }

        return pageVOs;
    }

    @Override
    public int countPage() {
        return cmsPageDao.countAll();
    }

    @Override
    public void publishPage(String pageName, String pageEnName, String title, String description,
                            String keywords) {

        if (StringUtils.isNotBlank(pageName) && StringUtils.isNotBlank(pageEnName)) {
            CmsPage page = new CmsPage();
            page.setName(pageName);
            page.setEnName(pageEnName);
            page.setTitle(title);
            page.setDescription(description);
            page.setKeywords(keywords);
            cmsPageDao.insert(page);
        }
    }

    @Override
    public void editPage(int pageId, String pageName, String pageEnName, String title,
                         String description, String keywords) {

        if (StringUtils.isNotBlank(pageName) && StringUtils.isNotBlank(pageEnName)) {

            CmsPage page = cmsPageDao.selectById(pageId);

            if (page != null) {
                page.setName(pageName);
                page.setEnName(pageEnName);
                page.setTitle(title);
                page.setDescription(description);
                page.setKeywords(keywords);
                cmsPageDao.updateById(page);
            }
        }
    }

    @Override
    public void deletePage(int pageId) {
        cmsPageDao.deleteById(pageId);
    }

}