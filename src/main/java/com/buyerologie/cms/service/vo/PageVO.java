package com.buyerologie.cms.service.vo;

import java.util.List;
import java.util.Map;

import com.buyerologie.cms.model.CmsPage;

public class PageVO extends CmsPage {

    private List<String>                moduleNames;

    private Map<String, List<ModuleVO>> moduleMap;

    public PageVO() {
    }

    public PageVO(CmsPage page) {
        super(page);
    }

    public List<String> getModuleNames() {
        return moduleNames;
    }

    public void setModuleNames(List<String> moduleNames) {
        this.moduleNames = moduleNames;
    }

    public Map<String, List<ModuleVO>> getModuleMap() {
        return moduleMap;
    }

    public void setModuleMap(Map<String, List<ModuleVO>> moduleMap) {
        this.moduleMap = moduleMap;
    }

}
