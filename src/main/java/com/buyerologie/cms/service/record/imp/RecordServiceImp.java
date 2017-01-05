package com.buyerologie.cms.service.record.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.buyerologie.cms.dao.CmsModuleDao;
import com.buyerologie.cms.dao.CmsModuleRecordRelationDao;
import com.buyerologie.cms.dao.CmsRecordDao;
import com.buyerologie.cms.model.CmsModuleRecordRelation;
import com.buyerologie.cms.model.CmsRecord;
import com.buyerologie.cms.service.module.ModuleService;
import com.buyerologie.cms.service.record.RecordService;
import com.buyerologie.cms.service.vo.RecordVO;
import com.buyerologie.utils.StringUtil;

@Service(value = "recordService")
public class RecordServiceImp implements RecordService {

    @Resource
    private CmsRecordDao               cmsRecordDao;

    @Resource
    private CmsModuleDao               cmsModuleDao;

    @Resource
    private CmsModuleRecordRelationDao cmsModuleRecordRelationDao;

    @Resource
    private ModuleService              moduleService;

    @Override
    public int addType(String recordName) {
        if (StringUtils.isBlank(recordName)) {
            return -1;
        }
        CmsRecord record = new CmsRecord();
        record.setName(recordName);
        cmsRecordDao.insert(record);
        return record.getId();
    }

    @Override
    public RecordVO get(int recordId) {
        if (recordId <= 0) {
            return null;
        }
        CmsRecord record = cmsRecordDao.selectById(recordId);
        if (record == null) {
            return null;
        }
        return new RecordVO(record);
    }

    @Override
    public List<RecordVO> getRecordVOs(int moduleId) {
        List<Integer> moduleIdList = new ArrayList<Integer>();
        moduleIdList.add(moduleId);
        return getRecordVOs(moduleIdList);
    }

    @Override
    public List<RecordVO> getRecordVOs(List<Integer> moduleIdList) {

        String idList = StringUtil.buildIntListToString(moduleIdList, ",");

        List<RecordVO> recordVOs = null;
        if (StringUtils.isNotBlank(idList)) {
            recordVOs = new ArrayList<RecordVO>();

            List<CmsRecord> records = cmsRecordDao.selectByModuleId(idList);

            if (records != null && records.size() > 0) {

                List<Integer> recordIdList = new ArrayList<Integer>();

                for (CmsRecord record : records) {
                    RecordVO recordVO = new RecordVO(record);

                    recordIdList.add(record.getId());

                    recordVOs.add(recordVO);
                }

                String recordIds = StringUtil.buildIntListToString(recordIdList, ",");

                if (StringUtils.isNotBlank(recordIds)) {

                    List<CmsModuleRecordRelation> r_ModuleRecords = cmsModuleRecordRelationDao
                        .selectByRecordIdList(recordIds);

                    if (r_ModuleRecords != null && r_ModuleRecords.size() > 0)
                        for (int i = 0; i < recordVOs.size(); i++)
                            for (CmsModuleRecordRelation r_ModuleRecord : r_ModuleRecords)
                                if (r_ModuleRecord.getRecordId() == recordVOs.get(i).getId()) {
                                    recordVOs.get(i).setModuleId(r_ModuleRecord.getModuleId());
                                    break;
                                }

                }
            }
        }
        return recordVOs;
    }

    @Override
    public void editRecord(int recordId, String value) {
        if (recordId <= 0 || value == null) {
            return;
        }
        CmsRecord record = cmsRecordDao.selectById(recordId);
        if (record == null || record.getValue().equals(value)) {
            return;
        }
        record.setValue(value.trim());
        cmsRecordDao.updateById(record);
    }

    @Override
    public void editRecordType(int recordId, String recordName) {
        if (recordId <= 0 || StringUtils.isBlank(recordName)) {
            return;
        }
        CmsRecord record = cmsRecordDao.selectById(recordId);
        if (record == null) {
            return;
        }
        record.setName(recordName);
        cmsRecordDao.updateById(record);
    }

    @Override
    public void deleteRecordType(String recordName) {
        if (StringUtils.isNotBlank(recordName)) {
            List<CmsRecord> records = cmsRecordDao.selectByName(recordName);

            cmsRecordDao.deleteByName(recordName);

            for (CmsRecord record : records)
                cmsModuleRecordRelationDao.deleteByRecordId(record.getId());
        }
    }

    @Override
    public List<CmsRecord> getRecords(int moduleId, int recordId) {
        if (moduleId <= 0 || recordId <= 0) {
            return null;
        }

        List<CmsRecord> records = cmsRecordDao.selectByModuleIdAndRecordId(moduleId, recordId);
        if (records == null || records.isEmpty()) {
            return null;
        }
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setValue(records.get(i).getValue().trim());
        }
        return records;
    }

    @Override
    public void deleteRecord(int recordId) {
        if (recordId <= 0) {
            return;
        }
        cmsRecordDao.deleteById(recordId);
    }

    @Override
    public int createRecord(String recordName, String value) {
        if (StringUtils.isBlank(recordName)) {
            return -1;
        }
        CmsRecord record = new CmsRecord();
        record.setName(recordName);
        record.setValue(value.trim());
        cmsRecordDao.insert(record);
        return record.getId();
    }

}