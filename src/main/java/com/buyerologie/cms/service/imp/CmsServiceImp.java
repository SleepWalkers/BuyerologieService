package com.buyerologie.cms.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.cms.dao.CmsDao;
import com.buyerologie.cms.dao.CmsModuleRecordRelationDao;
import com.buyerologie.cms.dao.CmsPageModuleRelationDao;
import com.buyerologie.cms.model.CmsModule;
import com.buyerologie.cms.model.CmsModuleRecordRelation;
import com.buyerologie.cms.model.CmsPageModuleRelation;
import com.buyerologie.cms.service.CmsService;
import com.buyerologie.cms.service.module.ModuleService;
import com.buyerologie.cms.service.page.PageService;
import com.buyerologie.cms.service.record.RecordService;
import com.buyerologie.cms.service.vo.ModuleVO;
import com.buyerologie.cms.service.vo.PageVO;
import com.buyerologie.cms.service.vo.RecordVO;
import com.buyerologie.utils.StringUtil;

@Service(value = "cmsService")
public class CmsServiceImp implements CmsService {

    @Resource
    private PageService                pageService;

    @Resource
    private ModuleService              moduleService;

    @Resource
    private RecordService              recordService;

    @Resource
    private CmsDao                     cmsDao;

    @Resource
    private CmsModuleRecordRelationDao cmsModuleRecordRelationDao;

    @Resource
    private CmsPageModuleRelationDao   cmsPageModuleRelationDao;

    private List<Integer> getSameModuleSameRecordId(int moduleId, int recordId) {
        List<Integer> moduleIds = cmsDao.selectSameModuleIds(moduleId);

        if (moduleIds == null || moduleIds.isEmpty()) {
            return null;
        }
        String moduleIdsStr = StringUtil.buildIntListToString(moduleIds, ",");
        if (StringUtils.isBlank(moduleIdsStr)) {
            return null;
        }
        List<Integer> recordIds = cmsDao.selectByModuleIdListAndRecordId(moduleIdsStr, recordId);
        return recordIds;
    }

    @Override
    public int countModules(int pageId) {
        return moduleService.countModule(pageId);
    }

    @Override
    public void deleteRecordType(int recordId) {
        if (recordId <= 0) {
            return;
        }

        List<CmsModuleRecordRelation> r_ModuleRecords = cmsModuleRecordRelationDao
            .selectByRecordIdList(recordId + "");

        if (r_ModuleRecords == null || r_ModuleRecords.isEmpty()) {
            return;
        }

        List<Integer> recordIds = getSameModuleSameRecordId(r_ModuleRecords.get(0).getModuleId(),
            recordId);

        if (recordIds == null || recordIds.isEmpty()) {
            return;
        }

        for (int theRecordId : recordIds) {
            cmsModuleRecordRelationDao.deleteByRecordId(theRecordId);
            recordService.deleteRecord(theRecordId);
        }
    }

    @Override
    public void publishRecordType(int moduleId, int[] recordIdArr, String[] recordNameArr) {
        if (moduleId <= 0 || recordIdArr == null || recordNameArr == null
            || recordIdArr.length != recordNameArr.length) {
            return;
        }

        ModuleVO moduleVO = moduleService.getModuleVO(moduleId);
        if (moduleVO == null) {
            return;
        }

        for (int i = 0; i < recordIdArr.length; i++) {
            int recordId = recordIdArr[i];
            if (recordId > 0) {
                RecordVO recordVO = recordService.get(recordIdArr[i]);
                if (recordVO == null) {
                    continue;
                }
                if (recordVO.getName().equals(recordNameArr[i])) {
                    continue;
                }

                List<Integer> recordIds = getSameModuleSameRecordId(moduleId, recordId);
                if (recordIds == null || recordIds.isEmpty()) {
                    continue;
                }

                String recordName = recordNameArr[i];
                for (int theRecordId : recordIds) {
                    recordService.editRecordType(theRecordId, recordName);
                }
            } else {
                List<Integer> moduleIds = cmsDao.selectSameModuleIds(moduleId);
                if (moduleIds == null || moduleIds.isEmpty()) {
                    continue;
                }
                String recordName = recordNameArr[i];
                for (int theModuleId : moduleIds) {
                    int newRecordId = recordService.addType(recordName);
                    CmsModuleRecordRelation r_ModuleRecord = new CmsModuleRecordRelation();

                    r_ModuleRecord.setModuleId(theModuleId);
                    r_ModuleRecord.setRecordId(newRecordId);
                    cmsModuleRecordRelationDao.insert(r_ModuleRecord);
                }
            }
        }
    }

    @Override
    public PageVO getPageVO(String name) {
        PageVO pageVO = pageService.getPage(name);
        if (pageVO != null) {
            buildPageVO(pageVO);
        }
        return pageVO;
    }

    @Override
    public PageVO getPageVO(int pageId) {
        PageVO pageVO = pageService.getPage(pageId);
        if (pageVO != null) {
            buildPageVO(pageVO);
        }
        return pageVO;
    }

    private void buildPageVO(PageVO pageVO) {
        List<ModuleVO> moduleVOs = getModuleVOs(pageVO.getId());

        List<CmsModule> modules = moduleService.getModuleTypes(pageVO.getId());

        if (modules != null && !modules.isEmpty()) {
            List<String> moduleNames = new ArrayList<String>();
            for (CmsModule module : modules) {
                moduleNames.add(module.getName());
            }
            pageVO.setModuleNames(moduleNames);
        }
        if (moduleVOs != null && moduleVOs.size() > 0) {
            Map<String, List<ModuleVO>> moduleMap = new HashMap<String, List<ModuleVO>>();

            for (ModuleVO moduleVO : moduleVOs)
                if (moduleMap.containsKey(moduleVO.getName()))
                    moduleMap.get(moduleVO.getName()).add(moduleVO);
                else {
                    List<ModuleVO> newModuleVOs = new ArrayList<ModuleVO>();
                    newModuleVOs.add(moduleVO);
                    moduleMap.put(moduleVO.getName(), newModuleVOs);
                }

            pageVO.setModuleMap(moduleMap);
        }
    }

    @Override
    public List<ModuleVO> get(int pageId, int currentPage, int pageSize) {
        List<ModuleVO> moduleVOs = moduleService.getModuleVOs(pageId, currentPage, pageSize);
        for (ModuleVO moduleVO : moduleVOs) {
            moduleVO.setPageId(pageId);
        }
        return moduleVOs;
    }

    @Override
    public List<ModuleVO> get(int pageId, String moduleName, int currentPage, int pageSize) {
        List<ModuleVO> moduleVOs = moduleService.getModuleVOs(pageId, moduleName, currentPage,
            pageSize);
        for (ModuleVO moduleVO : moduleVOs) {
            moduleVO.setPageId(pageId);
        }
        return moduleVOs;
    }

    private List<ModuleVO> getModuleVOs(int pageId) {
        List<ModuleVO> moduleVOs = moduleService.getModuleVOs(pageId);

        if (moduleVOs != null && moduleVOs.size() > 0) {
            for (int i = 0; i < moduleVOs.size(); i++) {
                if (moduleVOs.get(i).getRecordNames() != null
                    && moduleVOs.get(i).getRecordMap() != null) {
                    for (String recordName : moduleVOs.get(i).getRecordNames()) {
                        if (moduleVOs.get(i) != null && moduleVOs.get(i).getRecordMap() != null
                            && moduleVOs.get(i).getRecordMap().containsKey(recordName)) {
                            moduleVOs
                                .get(i)
                                .getRecordMap()
                                .get(recordName)
                                .setRecordData(
                                    moduleVOs.get(i).getRecordMap().get(recordName).getValue());
                        }
                    }
                }
            }
        }
        return moduleVOs;
    }

    @Override
    public void createModuleType(int pageId, String moduleName) {
        if (pageId <= 0 || StringUtils.isBlank(moduleName)) {
            return;
        }

        PageVO pageVO = pageService.getPage(pageId);
        if (pageVO == null) {
            return;
        }

        int moduleId = moduleService.publishModule(pageId, moduleName);
        if (moduleId <= 0) {
            return;
        }

        CmsPageModuleRelation r_PageModule = new CmsPageModuleRelation();
        r_PageModule.setModuleId(moduleId);
        r_PageModule.setPageId(pageId);

        cmsPageModuleRelationDao.insert(r_PageModule);
    }

    @Override
    public void editModuleType(int moduleId, String moduleName) {
        if (moduleId <= 0 || StringUtils.isBlank(moduleName)) {
            return;
        }
        ModuleVO moduleVO = moduleService.getModuleVO(moduleId);
        if (moduleVO == null || moduleVO.getName().equals(moduleName)) {
            return;
        }
        List<Integer> moduleIds = cmsDao.selectSameModuleIds(moduleId);

        if (moduleIds == null || moduleIds.isEmpty()) {
            return;
        }

        for (int theModuleId : moduleIds) {
            moduleService.editModule(theModuleId, moduleName);
        }
    }

    @Override
    public void deleteModuleType(int moduleId) {
        if (moduleId <= 0) {
            return;
        }

        List<Integer> moduleIds = cmsDao.selectSameModuleIds(moduleId);

        if (moduleIds == null || moduleIds.isEmpty()) {
            return;
        }
        for (int theModuleId : moduleIds) {
            deleteModule(theModuleId);
        }
    }

    @Override
    public void deleteModule(int moduleId) {
        if (moduleId <= 0) {
            return;
        }

        List<CmsModuleRecordRelation> r_ModuleRecords = cmsModuleRecordRelationDao
            .selectByModuleId(moduleId);

        if (r_ModuleRecords != null && r_ModuleRecords.size() > 0) {
            for (CmsModuleRecordRelation r_ModuleRecord : r_ModuleRecords) {
                recordService.deleteRecord(r_ModuleRecord.getRecordId());
            }
        }
        cmsModuleRecordRelationDao.deleteByModuleId(moduleId);

        cmsPageModuleRelationDao.deleteByModuleId(moduleId);

        moduleService.deleteModule(moduleId);
    }

    @Override
    public void createRecords(int sampleModuleId, String[] recordNameArr, String[] recordValueArr) {
        if (sampleModuleId <= 0 || recordNameArr == null || recordValueArr == null
            || recordNameArr.length != recordValueArr.length) {
            return;
        }

        ModuleVO sampleModuleVO = moduleService.getModuleVO(sampleModuleId);

        List<RecordVO> sampleRecordVOs = recordService.getRecordVOs(sampleModuleId);

        if (sampleModuleVO == null || sampleRecordVOs == null || sampleRecordVOs.isEmpty()) {
            return;
        }

        int sampleRecordSize = sampleRecordVOs.size();
        int newRecordLength = recordNameArr.length;
        int times = newRecordLength / sampleRecordSize;

        for (int i = 0; i < times; i++) {
            int moduleId = moduleService.publishModule(sampleModuleVO.getPageId(),
                sampleModuleVO.getName());
            for (int j = 0; j < sampleRecordSize; j++) {
                int arrIndex = sampleRecordSize * i + j;
                int recordId = recordService.createRecord(recordNameArr[arrIndex],
                    recordValueArr[arrIndex]);
                if (recordId > 0) {
                    cmsModuleRecordRelationDao.insert(new CmsModuleRecordRelation(moduleId,
                        recordId));
                }
            }
            cmsPageModuleRelationDao.insert(new CmsPageModuleRelation(sampleModuleVO.getPageId(),
                moduleId));
        }
    }

    @Override
    public void modifyRecords(int[] recordIdArr, String[] recordValueArr) {
        if (recordIdArr == null || recordValueArr == null
            || recordIdArr.length != recordValueArr.length) {
            return;
        }

        for (int i = 0; i < recordIdArr.length; i++) {
            recordService.editRecord(recordIdArr[i], recordValueArr[i]);
        }
    }

    @Override
    public void deletePage(int pageId) {

        List<CmsPageModuleRelation> cmsPageModuleRelations = cmsPageModuleRelationDao
            .selectByPageId(pageId);

        if (cmsPageModuleRelations != null && cmsPageModuleRelations.size() > 0) {
            for (CmsPageModuleRelation r_PageModule : cmsPageModuleRelations) {
                deleteModule(r_PageModule.getModuleId());
            }
        }
        cmsPageModuleRelationDao.deleteByPageId(pageId);

        pageService.deletePage(pageId);
    }

    @Override
    public void editRecord(int recordId, String value) {
        recordService.editRecord(recordId, value);
    }

}
