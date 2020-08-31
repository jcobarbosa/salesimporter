package br.com.jcobarbosa.salesimporter;

import br.com.jcobarbosa.salesimporter.config.SalesFileProperties;
import br.com.jcobarbosa.salesimporter.repository.SaleImportingFileRepository;
import br.com.jcobarbosa.salesimporter.service.SalesImportingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalesImportingServiceTest {

    @InjectMocks
    private SalesImportingServiceImpl salesImportingService;

    @Mock
    private SalesFileProperties salesFileProperties;

    @Mock
    private SaleImportingFileRepository saleImportingFileRepository;

    @Before
    public void setUp() {
        mockProperties();
    }

    @Test
    public void whenAnEmptyFileIsProvided() {
        File file = getFile("empty");
        try {
            String result = salesImportingService.process("empty.dat", getBufferedReader(file));
            assertNull(result);
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneInvalidCustomerEntryWasProvidedNoCustomersShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("customer");
        writeToFile(file, "002");

        try {
            String result = salesImportingService.process("customer.dat", getBufferedReader(file));
            assertNotNull(result);
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneValidCustomerEntryWasProvidedOneCustomerShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("customer");
        writeToFile(file, "002ç1çTestçBusiness Area");

        try {
            String result = salesImportingService.process("customer.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de clientes no arquivo de entrada: 1"));
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 0"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneInvalidSellerEntryWasProvidedNoSellersShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("seller");
        writeToFile(file, "001");

        try {
            String result = salesImportingService.process("seller.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 0"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneSellerWithInvalidSalaryEntryWasProvidedNoSellersShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("seller");
        writeToFile(file, "001ç12345çSellerTestçu");

        try {
            String result = salesImportingService.process("seller.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 1"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneValidSellerEntryWasProvidedOneSellerShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("seller");
        writeToFile(file, "001ç1çTestç1.2");

        try {
            String result = salesImportingService.process("seller.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de clientes no arquivo de entrada: 0"));
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 1"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneInvalidSaleEntryWasProvidedNoSalesShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");

        File file = getFile("sale");
        writeToFile(file, "003");

        try {
            String result = salesImportingService.process("sale.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("ID da venda mais cara: null"));
            assertTrue(result.contains("O pior vendedor: null"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyOneValidSaleEntryWasProvidedOneSaleShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");
        when(salesFileProperties.getSaleLineSeparator()).thenReturn(",");
        when(salesFileProperties.getSaleItemSeparator()).thenReturn("-");

        File file = getFile("sale");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("001ç12345çTestç1500\n");
        stringBuilder.append("003ç1ç[1-1-1]çTest");
        writeToFile(file, stringBuilder.toString());

        try {
            String result = salesImportingService.process("sale.dat", getBufferedReader(file));
            assertNotNull(result);
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyValidSalesEntriesWereProvidedTestSellerTheWorstSellerShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");
        when(salesFileProperties.getSaleLineSeparator()).thenReturn(",");
        when(salesFileProperties.getSaleItemSeparator()).thenReturn("-");

        File file = getFile("sale");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("001ç12345çTestç1500\n");
        stringBuilder.append("001ç67890çOtherç1600\n");
        stringBuilder.append("003ç1ç[1-1-1]çTest\n");
        stringBuilder.append("003ç2ç[1-1-1]çOther\n");
        stringBuilder.append("003ç3ç[1-2-2]çOther");
        writeToFile(file, stringBuilder.toString());

        try {
            String result = salesImportingService.process("sale.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 2"));
            assertTrue(result.contains("ID da venda mais cara: 3"));
            assertTrue(result.contains("O pior vendedor: Test"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    @Test
    public void whenOnlyValidSalesWithOtherSalesDataInvalidEntriesWereProvidedOtherSellerTheWorstSellerShouldBePresentOnReport() {
        when(salesFileProperties.getMainLineSeparator()).thenReturn("ç");
        when(salesFileProperties.getSaleLineSeparator()).thenReturn(",");
        when(salesFileProperties.getSaleItemSeparator()).thenReturn("-");

        File file = getFile("sale");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("001ç12345çTestç1500\n");
        stringBuilder.append("001ç67890çOtherç1600\n");
        stringBuilder.append("003ç1ç[1-1-1]çTest\n");
        stringBuilder.append("003ç2ç[1-x-1]çOther\n");
        stringBuilder.append("003ç3ç[1-x-2]çOther");
        writeToFile(file, stringBuilder.toString());

        try {
            String result = salesImportingService.process("sale.dat", getBufferedReader(file));
            assertNotNull(result);
            assertTrue(result.contains("Quantidade de vendedor no arquivo de entrada: 2"));
            assertTrue(result.contains("ID da venda mais cara: 1"));
            assertTrue(result.contains("O pior vendedor: Other"));
        } catch(Exception e) {
            Assert.fail("Error not expected.");
        }
    }

    private void writeToFile(File file, String content) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String fileName) {
        try {
            return File.createTempFile(fileName, ".dat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private BufferedReader getBufferedReader(File file) {
        try {
            return  new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mockProperties() {
        when(salesFileProperties.getOutDirectory()).thenReturn("");
        when(salesFileProperties.getRecordTypeSeller()).thenReturn("001");
        when(salesFileProperties.getRecordTypeCustomer()).thenReturn("002");
        when(salesFileProperties.getRecordTypeSale()).thenReturn("003");
    }
}
