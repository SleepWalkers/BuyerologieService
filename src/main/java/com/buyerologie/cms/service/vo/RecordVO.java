package com.buyerologie.cms.service.vo;

import org.apache.commons.lang.StringUtils;

import com.buyerologie.cms.model.CmsRecord;

public class RecordVO extends CmsRecord {

    private Object recordData;

    private int    moduleId;

    public enum RecordDataType {
        /** 单品 */
        PRODUCT("product"),
        /** 品牌 */
        BRAND("brand"),
        /** 话题 */
        TOPIC("topic"),
        /**  用户*/
        USER("user"),

        PIC_TAG("picTag"),
        /**  二手*/
        SECOND_HAND("secondHand"),
        /**  白菜*/
        CABBAGE("cabbage"),
        /** 香评 */
        COMMENT("comment"),
        /**  字符串*/
        STRING("string"),
        /**  图片地址*/
        IMAGE("image"),
        /**  url*/
        URL("url");

        private String value;

        public static RecordDataType getRecordDataType(String string) {
            RecordDataType recordDataType = null;
            if (StringUtils.isNotBlank(string)) {
                if (string.equals(PRODUCT.toString()))
                    recordDataType = PRODUCT;
                else if (string.equals(BRAND.toString()))
                    recordDataType = BRAND;
                else if (string.equals(TOPIC.toString()))
                    recordDataType = TOPIC;
                else if (string.equals(USER.toString()))
                    recordDataType = USER;
                else if (string.equals(PIC_TAG.toString()))
                    recordDataType = PIC_TAG;
                else if (string.equals(SECOND_HAND.toString()))
                    recordDataType = SECOND_HAND;
                else if (string.equals(CABBAGE.toString()))
                    recordDataType = CABBAGE;
                else if (string.equals(COMMENT.toString()))
                    recordDataType = COMMENT;
                else if (string.equals(STRING.toString()))
                    recordDataType = STRING;
                else if (string.equals(IMAGE.toString()))
                    recordDataType = IMAGE;
                else if (string.equals(URL.toString()))
                    recordDataType = URL;
            }

            return recordDataType;

        }

        private RecordDataType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getValue();
        }

    }

    public RecordVO(CmsRecord record) {
        super(record);
    }

    public RecordVO() {
    }

    public Object getRecordData() {
        return recordData;
    }

    public void setRecordData(Object recordData) {
        this.recordData = recordData;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

}