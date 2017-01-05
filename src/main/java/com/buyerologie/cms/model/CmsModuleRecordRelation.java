package com.buyerologie.cms.model;

public class CmsModuleRecordRelation {
    private int moduleId;

    private int recordId;

    public CmsModuleRecordRelation() {
    }

    public CmsModuleRecordRelation(int moduleId, int recordId) {
        this.moduleId = moduleId;
        this.recordId = recordId;
    }

    public CmsModuleRecordRelation(CmsModuleRecordRelation cmsModuleRecordRelation) {
        this.moduleId = cmsModuleRecordRelation.moduleId;
        this.recordId = cmsModuleRecordRelation.recordId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}