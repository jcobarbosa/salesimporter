package br.com.jcobarbosa.salesimporter.service;

import br.com.jcobarbosa.salesimporter.collaborator.ImportingReportProducer;
import br.com.jcobarbosa.salesimporter.config.SalesFileProperties;
import br.com.jcobarbosa.salesimporter.model.*;
import br.com.jcobarbosa.salesimporter.repository.SaleImportingFileRepository;
import br.com.jcobarbosa.salesimporter.util.ImporterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.jcobarbosa.salesimporter.util.ImporterUtil.*;

@Service
public class SalesImportingServiceImpl implements SalesImportingService {

    private static final Logger logger = LoggerFactory.getLogger(SalesImportingServiceImpl.class);

    private SalesFileProperties salesFileProperties;
    private SaleImportingFileRepository saleImportingFileRepository;

    public SalesImportingServiceImpl(SalesFileProperties salesFileProperties,
                                     SaleImportingFileRepository saleImportingFileRepository) {

        this.salesFileProperties = salesFileProperties;
        this.saleImportingFileRepository = saleImportingFileRepository;
    }

    /**
     *
     * @param fileName
     * @param reader
     * @return String - importing results collaborator
     */
    @Override
    public String process(String fileName, BufferedReader reader) {
        String result = null;
        logger.info("Working on sales from file [" + fileName + "]...");

        SaleImportingFile file = storeImportingData(fileName, reader);

        if (file != null && !file.getEntries().isEmpty()) {
            processSellers(file);
            processCustomer(file);
            processSale(file);

            saleImportingFileRepository.save(file);

            result = generateReport(fileName, file);
        } else {
            logger.error("File [" + fileName + "] is null and no action was taken.");
        }

        return result;
    }

    private String generateReport(String fileName, SaleImportingFile fileSaved) {
        String result = ImportingReportProducer.build(fileSaved, fileName, salesFileProperties.getOutDirectory());
        logger.info("Sales imported! For details, check the collaborator below:");
        logger.info("\n\n######## Importing result collaborator [" + fileName + "]:\n" + result);
        return result;
    }

    private void processSale(SaleImportingFile file) {
        try {
            Map<String, Seller> mapSeller = file.getSellers().stream().collect(Collectors.toMap(seller -> seller.getName(), seller -> seller));

            file.getEntries().stream()
                    .filter(entry -> entry.getRecordType().equals(salesFileProperties.getRecordTypeSale()))
                    .forEach(entry -> {
                        String[] content = entry.getRecord().trim().split(salesFileProperties.getMainLineSeparator());

                        Sale sale = new Sale();
                        sale.setSaleId(getLongFromPosition(content, 1));
                        sale.setSeller(mapSeller.get(ImporterUtil.getStringFromPosition(content, 3)));
                        if (content != null && content.length >= 3) {
                            sale.getItems().addAll(getItems(content[2]));
                        }

                        file.getSales().add(sale);
                    });

        } catch (Exception e) {
            logger.error("Couldn't read sales. " + e.getMessage());
        }
    }

    private List<SaleItem> getItems(String items) {
        List<SaleItem> list = new ArrayList<>();
        if (items != null && !items.trim().isEmpty()) {

            String[] itemsSanitized = items.replace("[", "").replace("]", "")
                    .split(salesFileProperties.getSaleLineSeparator());

            if (itemsSanitized != null && itemsSanitized.length > 0) {
                for (String saleItemLine : itemsSanitized) {
                    String[] content = saleItemLine.split(salesFileProperties.getSaleItemSeparator());

                    if (content != null && content.length > 1) {
                        SaleItem item = new SaleItem();
                        item.setItemId(getLongFromPosition(content, 0));
                        item.setQuantity(getBigDecimalFromPosition(content, 1));
                        item.setPrice(getBigDecimalFromPosition(content, 2));

                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    private void processCustomer(SaleImportingFile file) {
        file.getEntries().stream()
                .filter(entry -> entry.getRecordType().equals(salesFileProperties.getRecordTypeCustomer()))
                .forEach(entry -> {
                    String[] content = entry.getRecord().trim().split(salesFileProperties.getMainLineSeparator());

                    if (content != null && content.length > 1) {
                        Customer customer = new Customer();
                        customer.setCnpj(getStringFromPosition(content, 1));
                        customer.setName(getStringFromPosition(content, 2));
                        customer.setBusinessArea(getStringFromPosition(content, 3));

                        file.getCustomers().add(customer);
                    }
                });
    }

    private void processSellers(SaleImportingFile file) {
        file.getEntries().stream()
                .filter(entry -> entry.getRecordType().equals(salesFileProperties.getRecordTypeSeller()))
                .forEach(entry -> {
                    String[] content = entry.getRecord().trim().split(salesFileProperties.getMainLineSeparator());

                    if (content != null && content.length > 1) {
                        Seller seller = new Seller();
                        seller.setCpf(getStringFromPosition(content, 1));
                        seller.setName(getStringFromPosition(content, 2));
                        seller.setSalary(getBigDecimalFromPosition(content, 3));

                        file.getSellers().add(seller);
                    }
                });
    }

    private SaleImportingFile storeImportingData(String fileName, BufferedReader reader) {
        SaleImportingFile saleImportingFile;
        try {
            if (reader.ready()) {
                saleImportingFile = new SaleImportingFile(fileName);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line != null && !line.trim().isEmpty()) {
                        saleImportingFile.getEntries().add(createSaleImportingEntry(line));
                    }
                }
                reader.close();

                return saleImportingFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SaleImportingEntry createSaleImportingEntry(String line) {
        String[] content = line.split(salesFileProperties.getMainLineSeparator());

        SaleImportingEntry entry = new SaleImportingEntry();
        entry.setRecordType(getStringFromPosition(content, 0));
        entry.setRecord(line);

        return entry;
    }

}
