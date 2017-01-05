package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.cms.model.CmsPageModuleRelation;

public interface CmsPageModuleRelationDao {

    void insert(CmsPageModuleRelation cmsPageModuleRelation);

    void deleteByPageId(@Param(value = "pageId") int pageId);

    void deleteByModuleId(@Param(value = "moduleId") int moduleId);

    List<CmsPageModuleRelation> selectByPageId(@Param(value = "pageId") int pageId);

    List<CmsPageModuleRelation> selectByModuleId(@Param(value = "moduleId") int moduleId);

}