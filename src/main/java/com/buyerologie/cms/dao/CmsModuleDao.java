package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.cms.model.CmsModule;

public interface CmsModuleDao {

    void insert(CmsModule cmsModule);

    void updateById(CmsModule cmsModule);

    void deleteById(int id);

    CmsModule selectById(int id);

    List<CmsModule> selectSameModules(@Param(value = "moduleId") int moduleId);

    List<CmsModule> selectModule(@Param(value = "pageId") Integer pageId,
                                 @Param(value = "name") String moduleName,
                                 @Param(value = "orderBy") String orderBy,
                                 @Param(value = "start") Integer start,
                                 @Param(value = "limit") Integer limit);

    List<CmsModule> selectModules(@Param(value = "pageId") int pageId);

    List<String> selectDistinctModuleName(@Param(value = "pageId") int pageId);

    int countModule(@Param(value = "pageId") Integer pageId,
                    @Param(value = "name") String moduleName);
}