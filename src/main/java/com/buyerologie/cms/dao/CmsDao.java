package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CmsDao {
    List<Integer> selectSameModuleIds(@Param(value = "moduleId") int moduleId);

    List<Integer> selectByModuleIdListAndRecordId(@Param(value = "moduleIdList") String moduleIdList,
                                                  @Param(value = "recordId") int recordId);
}
