package br.com.jcobarbosa.salesimporter.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class SaleImportingFile {

    @Id
    private ObjectId id;
    private String name;
    private List<SaleImportingEntry> entries = new ArrayList<>();
    private List<Seller> sellers = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    public SaleImportingFile() {
    }

    public SaleImportingFile(String fileName) {
        this.name = fileName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SaleImportingEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<SaleImportingEntry> entries) {
        this.entries = entries;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
