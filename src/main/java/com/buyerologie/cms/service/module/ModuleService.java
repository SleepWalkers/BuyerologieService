package com.buyerologie.cms.service.module;

import java.util.List;

import com.buyerologie.cms.model.CmsModule;
import com.buyerologie.cms.service.vo.ModuleVO;

public interface ModuleService {

    /**
     * 获得模块
     * @param moduleId
     * @return
     */
    public ModuleVO getModuleVO(int moduleId);

    /**
     * 获得某个页面下的所有模块
     * @param pageId
     * @return
     */
    public List<ModuleVO> getModuleVOs(int pageId);

    /**
     * 分页获得某个页面下的所有模块
     * @param pageId        
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<ModuleVO> getModuleVOs(int pageId, int currentPage, int pageSize);

    /**
     * 分页获得某个页面下的某个类型的模块
     * @param pageId
     * @param moduleName
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<ModuleVO> getModuleVOs(int pageId, String moduleName, int currentPage, int pageSize);

    /**
     * 获得某个页面中的模块类型
     * @param pageId
     * @return
     */
    public List<CmsModule> getModuleTypes(int pageId);

    /**
     * 将modules转化为vo
     * @param modules
     * @return
     */
    public List<ModuleVO> getModuleVOs(List<CmsModule> modules);

    /**
     * 发布模块 返回生成的模块id
     * @param pageId
     * @param moduleName
     * @return
     */
    public int publishModule(int pageId, String moduleName);

    public void editModule(int moduleId, String moduleName);

    /**
     * 删除模块
     * @param moduleId
     */
    public void deleteModule(int moduleId);

    /**
     * 统计某个页面下的模块个数
     * @param pageId
     * @return
     */
    public int countModule(int pageId);

}
