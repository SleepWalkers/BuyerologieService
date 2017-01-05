package com.buyerologie.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyerologie.cms.model.CmsPage;

public interface CmsPageDao {

    void insert(CmsPage cmsPage);

    void updateById(CmsPage cmsPage);

    void deleteById(int id);

    CmsPage selectById(int id);

    CmsPage selectByName(@Param("name") String name);

    int countAll();

    List<CmsPage> selectAll();

}