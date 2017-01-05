package com.buyerologie.cms.service.vo;

import java.io.Serializable;

public class ModuleTypeVO implements Serializable {

    /**  */
    private static final long serialVersionUID = 8172229944177769383L;
    private String            moduleTypeName;

    public String getModuleTypeName() {
        return moduleTypeName;
    }

    public void setModuleTypeName(String moduleTypeName) {
        this.moduleTypeName = moduleTypeName;
    }

}
