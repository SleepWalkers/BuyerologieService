package com.buyerologie.cms.model;

public class CmsPageModuleRelation {
    private int pageId;

    private int moduleId;

    public CmsPageModuleRelation() {
    }

    public CmsPageModuleRelation(int pageId, int moduleId) {
        this.pageId = pageId;
        this.moduleId = moduleId;
    }

    public CmsPageModuleRelation(CmsPageModuleRelation cmsPageModuleRelation) {
        this.moduleId = cmsPageModuleRelation.moduleId;
        this.pageId = cmsPageModuleRelation.pageId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
}