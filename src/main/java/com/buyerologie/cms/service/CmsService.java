package com.buyerologie.cms.service;

import java.util.List;

import com.buyerologie.cms.service.vo.ModuleVO;
import com.buyerologie.cms.service.vo.PageVO;

public interface CmsService {

    /**
     * 根据pageId获得pageVO 其中包括该页面的所有模块和记录的信息
     * @param pageId
     * @return
     */
    public PageVO getPageVO(int pageId);

    public PageVO getPageVO(String name);

    void createModuleType(int pageId, String moduleName);

    void editModuleType(int moduleId, String moduleName);

    void deleteModuleType(int moduleId);

    void deleteModule(int moduleId);

    void createRecords(int sampleModuleId, String[] recordNameArr, String[] recordValueArr);

    void modifyRecords(int[] recordIdArr, String[] recordValueArr);

    void deletePage(int pageId);

    int countModules(int pageId);

    /**
     * 分页获取某个页面中的模块
     * @param pageId               页面ID
     * @param currentPage       当前页面
     * @param pageSize            页面大小
     * @return
     */
    List<ModuleVO> get(int pageId, int currentPage, int pageSize);

    /**
     * 分页获取某个页面中的某个类型的模块
     * @param pageId
     * @param moduleName
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<ModuleVO> get(int pageId, String moduleName, int currentPage, int pageSize);

    /**
     * 新增/修改记录类型
     * @param pageId
     * @param moduleName
     * @param recordName
     * @param recordDataType
     */
    public void publishRecordType(int moduleId, int[] recordIdArr, String[] recordNameArr);

    /**
     * 编辑记录
     * @param recordId
     * @param recordName
     * @param value
     * @param recordDataType
     */
    public void editRecord(int recordId, String value);

    /**
     * 删除记录类型
     * @param recordName
     * @param recordDataType
     */
    public void deleteRecordType(int recordId);

}
