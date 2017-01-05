package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.cms.model.CmsModuleRecordRelation;

public interface CmsModuleRecordRelationDao {

    void insert(CmsModuleRecordRelation cmsModuleRecordRelation);

    void updateById(CmsModuleRecordRelation cmsModuleRecordRelation);

    void deleteById(int id);

    CmsModuleRecordRelation selectById(int id);

    List<CmsModuleRecordRelation> selectByRecordIdList(@Param(value = "recordIdList") String recordIdList);

    List<CmsModuleRecordRelation> selectByModuleId(@Param(value = "moduleId") int moduleId);

    void deleteByModuleId(@Param(value = "moduleId") int moduleId);

    void deleteByRecordId(@Param(value = "recordId") int recordId);

}