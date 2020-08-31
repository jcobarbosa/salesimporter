package br.com.jcobarbosa.salesimporter.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SaleImportingEntry {

    private String recordType;
    private String record;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
