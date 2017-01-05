package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.cms.model.CmsRecord;

public interface CmsRecordDao {

    void insert(CmsRecord cmsRecord);

    void updateById(CmsRecord cmsRecord);

    void deleteById(int id);

    CmsRecord selectById(int id);

    List<CmsRecord> selectByModuleIdAndRecordId(@Param(value = "moduleId") int moduleId,
                                                @Param(value = "recordId") int recordId);

    List<CmsRecord> selectByModuleId(@Param(value = "moduleIdList") String moduleIdList);

    List<CmsRecord> selectByName(@Param(value = "name") String recordName);

    void deleteByName(@Param(value = "name") String recordName);

}