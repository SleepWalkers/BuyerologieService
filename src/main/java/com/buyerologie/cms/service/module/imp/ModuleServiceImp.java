package com.buyerologie.cms.service.module.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.cms.dao.CmsModuleDao;
import com.buyerologie.cms.dao.CmsPageDao;
import com.buyerologie.cms.dao.CmsPageModuleRelationDao;
import com.buyerologie.cms.dao.CmsRecordDao;
import com.buyerologie.cms.model.CmsModule;
import com.buyerologie.cms.model.CmsPageModuleRelation;
import com.buyerologie.cms.service.module.ModuleService;
import com.buyerologie.cms.service.record.RecordService;
import com.buyerologie.cms.service.vo.ModuleVO;
import com.buyerologie.cms.service.vo.RecordVO;
import com.buyerologie.utils.PageUtil;

@Service(value = "moduleService")
public class ModuleServiceImp implements ModuleService {

    @Resource
    private CmsModuleDao             cmsModuleDao;

    @Resource
    private CmsRecordDao             cmsRecordDao;

    @Resource
    private CmsPageDao               cmsPageDao;

    @Resource
    private CmsPageModuleRelationDao cmsPageModuleRelationDao;

    @Resource
    private RecordService            recordService;

    @Override
    public int publishModule(int pageId, String moduleName) {

        if (pageId <= 0 || StringUtils.isBlank(moduleName)) {
            return -1;
        }

        CmsModule module = new CmsModule();
        module.setName(moduleName);
        cmsModuleDao.insert(module);

        return module.getId();
    }

    @Override
    public ModuleVO getModuleVO(int moduleId) {
        CmsModule module = cmsModuleDao.selectById(moduleId);
        if (module == null) {
            return null;
        }
        ModuleVO moduleVO = new ModuleVO(module);

        List<CmsPageModuleRelation> cmsPageModuleRelations = cmsPageModuleRelationDao
            .selectByModuleId(moduleVO.getId());
        if (cmsPageModuleRelations != null && !cmsPageModuleRelations.isEmpty()) {
            moduleVO.setPageId(cmsPageModuleRelations.get(0).getPageId());
        }
        return moduleVO;
    }

    @Override
    public List<ModuleVO> getModuleVOs(List<CmsModule> modules) {
        List<ModuleVO> moduleVOs = null;

        if (modules != null && modules.size() > 0) {

            moduleVOs = new ArrayList<ModuleVO>();

            List<Integer> moduleIds = new ArrayList<Integer>();
            for (CmsModule module : modules) {
                ModuleVO moduleVO = new ModuleVO(module);

                moduleIds.add(module.getId());

                moduleVOs.add(moduleVO);
            }

            List<RecordVO> recordVOs = recordService.getRecordVOs(moduleIds);

            List<String> recordNames = null;

            if (recordVOs != null && recordVOs.size() > 0) {
                for (int i = 0; i < moduleVOs.size(); i++) {

                    recordNames = new ArrayList<String>();

                    for (RecordVO recordVO : recordVOs) {
                        if (recordVO.getModuleId() == moduleVOs.get(i).getId()
                            && StringUtils.isNotBlank(recordVO.getName())) {

                            recordNames.add(recordVO.getName());

                            if (moduleVOs.get(i).getRecordMap() != null)
                                moduleVOs.get(i).getRecordMap().put(recordVO.getName(), recordVO);
                            else {

                                Map<String, RecordVO> recordMap = new HashMap<String, RecordVO>();
                                recordMap.put(recordVO.getName(), recordVO);
                                moduleVOs.get(i).setRecordMap(recordMap);
                            }
                        }
                    }
                    moduleVOs.get(i).setRecordNames(recordNames);

                    recordNames = null;
                }
            }
        }
        return moduleVOs;
    }

    @Override
    public int countModule(int pageId) {
        return cmsModuleDao.countModule(pageId > 0 ? pageId : null, null);
    }

    @Override
    public void deleteModule(int moduleId) {
        if (moduleId <= 0) {
            return;
        }
        cmsModuleDao.deleteById(moduleId);
    }

    @Override
    public List<ModuleVO> getModuleVOs(int pageId) {
        List<CmsModule> modules = cmsModuleDao.selectModule(pageId, null, null, null, null);
        return getModuleVOs(modules);
    }

    @Override
    public List<ModuleVO> getModuleVOs(int pageId, int currentPage, int pageSize) {
        int start = PageUtil.getStart(currentPage, pageSize);
        int limit = PageUtil.getLimit(currentPage, pageSize);
        List<CmsModule> modules = cmsModuleDao.selectModule(pageId, null, null, start, limit);
        return getModuleVOs(modules);
    }

    @Override
    public List<ModuleVO> getModuleVOs(int pageId, String moduleName, int currentPage, int pageSize) {

        int start = PageUtil.getStart(currentPage, pageSize);
        int limit = PageUtil.getLimit(currentPage, pageSize);

        List<CmsModule> modules = cmsModuleDao.selectModule(pageId, moduleName, null, start, limit);

        return getModuleVOs(modules);
    }

    @Override
    public List<CmsModule> getModuleTypes(int pageId) {
        if (pageId < 0) {
            return null;
        }

        List<String> moduleNames = cmsModuleDao.selectDistinctModuleName(pageId);
        List<CmsModule> modules = cmsModuleDao.selectModules(pageId);

        if (moduleNames == null || moduleNames.isEmpty() || modules == null || modules.isEmpty()) {
            return null;
        }

        List<CmsModule> moduleTypes = new ArrayList<CmsModule>();
        for (String moduleName : moduleNames) {
            for (CmsModule module : modules) {
                if (moduleName.equals(module.getName())) {
                    moduleTypes.add(module);
                    break;
                }
            }
        }
        return moduleTypes;
    }

    @Override
    public void editModule(int moduleId, String moduleName) {
        if (moduleId <= 0 || StringUtils.isBlank(moduleName)) {
            return;
        }
        CmsModule module = cmsModuleDao.selectById(moduleId);
        if (module == null) {
            return;
        }
        module.setName(moduleName);
        cmsModuleDao.updateById(module);
    }

}
