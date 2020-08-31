package br.com.jcobarbosa.salesimporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sales.file")
public class SalesFileProperties {

    private String inDirectory;
    private String outDirectory;
    private String mainLineSeparator;
    private String saleLineSeparator;
    private String saleItemSeparator;
    private String recordTypeSeller;
    private String recordTypeCustomer;
    private String recordTypeSale;

    public String getInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(String inDirectory) {
        this.inDirectory = inDirectory;
    }

    public String getOutDirectory() {
        return outDirectory;
    }

    public void setOutDirectory(String outDirectory) {
        this.outDirectory = outDirectory;
    }

    public String getMainLineSeparator() {
        return mainLineSeparator;
    }

    public void setMainLineSeparator(String mainLineSeparator) {
        this.mainLineSeparator = mainLineSeparator;
    }

    public String getSaleLineSeparator() {
        return saleLineSeparator;
    }

    public void setSaleLineSeparator(String saleLineSeparator) {
        this.saleLineSeparator = saleLineSeparator;
    }

    public String getSaleItemSeparator() {
        return saleItemSeparator;
    }

    public void setSaleItemSeparator(String saleItemSeparator) {
        this.saleItemSeparator = saleItemSeparator;
    }

    public String getRecordTypeSeller() {
        return recordTypeSeller;
    }

    public void setRecordTypeSeller(String recordTypeSeller) {
        this.recordTypeSeller = recordTypeSeller;
    }

    public String getRecordTypeCustomer() {
        return recordTypeCustomer;
    }

    public void setRecordTypeCustomer(String recordTypeCustomer) {
        this.recordTypeCustomer = recordTypeCustomer;
    }

    public String getRecordTypeSale() {
        return recordTypeSale;
    }

    public void setRecordTypeSale(String recordTypeSale) {
        this.recordTypeSale = recordTypeSale;
    }
}