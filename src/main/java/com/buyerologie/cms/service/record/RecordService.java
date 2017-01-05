package com.buyerologie.cms.service.record;

import java.util.List;

import com.buyerologie.cms.model.CmsRecord;
import com.buyerologie.cms.service.vo.RecordVO;

public interface RecordService {

    /**
     * 获得模块的所有记录
     * @param moduleId
     * @return
     */
    public List<RecordVO> getRecordVOs(int moduleId);

    /**
     * 获得moduleIdList的所有的模块的记录
     * @param moduleIdList
     * @return
     */
    public List<RecordVO> getRecordVOs(List<Integer> moduleIdList);

    int createRecord(String recordName, String value);

    public RecordVO get(int recordId);

    /**
     * 获得所有和moduleId代表的模块，相同的模块中，recordId所代表的record相同的Record
     * @param moduleId
     * @param recordId
     * @return
     */
    public List<CmsRecord> getRecords(int moduleId, int recordId);

    public int addType(String recordName);

    public void editRecord(int recordId, String value);

    public void editRecordType(int recordId, String recordName);

    /**
     * 删除记录
     * @param recordName
     * @param recordDataType
     */
    public void deleteRecordType(String recordName);

    void deleteRecord(int recordId);
}
