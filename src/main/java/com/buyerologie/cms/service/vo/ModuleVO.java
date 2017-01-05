package com.buyerologie.cms.service.vo;

import java.util.List;
import java.util.Map;

import com.buyerologie.cms.model.CmsModule;

public class ModuleVO extends CmsModule {

    private int                   pageId;

    private List<String>          recordNames;

    private Map<String, RecordVO> recordMap;

    public ModuleVO(CmsModule module) {
        super(module);
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public Map<String, RecordVO> getRecordMap() {
        return recordMap;
    }

    public void setRecordMap(Map<String, RecordVO> recordMap) {
        this.recordMap = recordMap;
    }

    public List<String> getRecordNames() {
        return recordNames;
    }

    public void setRecordNames(List<String> recordNames) {
        this.recordNames = recordNames;
    }
}
