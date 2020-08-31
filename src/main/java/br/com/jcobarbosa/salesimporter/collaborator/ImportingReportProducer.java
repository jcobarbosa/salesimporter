package br.com.jcobarbosa.salesimporter.collaborator;

import br.com.jcobarbosa.salesimporter.model.Sale;
import br.com.jcobarbosa.salesimporter.model.SaleImportingFile;
import br.com.jcobarbosa.salesimporter.model.Seller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.Optional.ofNullable;

public class ImportingReportProducer {

    private static final Logger logger = LoggerFactory.getLogger(ImportingReportProducer.class);

    public static String build(SaleImportingFile file, String fileName, String outputDirectory) {
        String fileReportName = fileName.replace(".dat", ".done.dat");

        try {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Quantidade de clientes no arquivo de entrada: " + file.getCustomers().size() + "\n");
            stringBuilder.append("Quantidade de vendedor no arquivo de entrada: " + file.getSellers().size() + "\n");
            stringBuilder.append("ID da venda mais cara: " + getHighestSale(file.getSales()) + "\n");
            stringBuilder.append("O pior vendedor: " + getWorstSeller(file) + "\n");

            FileWriter fileWriter = new FileWriter(outputDirectory + "\\" + fileReportName);
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();

            return stringBuilder.toString();
        } catch (Exception e) {
            logger.error("Report generating failed! " + e.getMessage());
        }

        return null;
    }

    private static String getWorstSeller(SaleImportingFile file) {
        String result = null;

        Map<String, BigDecimal> sellerSummary = getSalesSummaryBySellerName(file);

        if (!sellerSummary.entrySet().isEmpty()) {
            List<Map.Entry<String, BigDecimal>> resultSorted = sellerSummary.entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(Collectors.toList());

            if (resultSorted != null && !resultSorted.isEmpty()) {
                result = resultSorted.get(0).getKey();
            }
        }

        return result;
    }

    private static HashMap<String, BigDecimal> getSalesSummaryBySellerName(SaleImportingFile file) {
        HashMap<String, BigDecimal> sellerSummary = new HashMap<>();

        if (file.getSales() != null && !file.getSales().isEmpty()) {

            file.getSales().forEach(sale -> {
                String sellerName = ofNullable(sale.getSeller()).map(Seller::getName).orElse(null);
                sellerSummary.put(sellerName, sellerSummary.getOrDefault(sellerName, BigDecimal.ZERO).add(sale.getTotal()));
            });

            return sellerSummary;
        }

        return sellerSummary;
    }

    private static String getHighestSale(List<Sale> sales) {
        if (sales != null && !sales.isEmpty()) {
            Sale sale = Collections.max(sales, Comparator.comparing(o -> o.getTotal()));
            return String.valueOf(ofNullable(sale).map(Sale::getSaleId).orElse(null));
        }

        return null;
    }
}
