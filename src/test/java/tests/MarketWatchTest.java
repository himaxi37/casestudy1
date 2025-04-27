package tests;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pages.MarketWatchPage;

import org.openqa.selenium.edge.EdgeDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.WebElement;

public class MarketWatchTest {

    WebDriver driver;
    MarketWatchPage marketWatchPage;

    @BeforeMethod
    public void setUp() {
        driver = new EdgeDriver();
        driver.get("https://www.bseindia.com/");
        
        // Initialize page objects
        marketWatchPage = new MarketWatchPage(driver);
    }

    @Test
    public void captureInstruments() throws IOException {
    	
        // Navigate to Market Watch
        marketWatchPage.goToMarketWatch();
        
        // Capture instrument data
        List<WebElement> rows = marketWatchPage.getInstrumentRows();
        
        // Create an Excel file to store captured data
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("MarketWatchData");
        
        // Add headers to Excel
        sheet.createRow(0).createCell(0).setCellValue("Instrument");
        sheet.createRow(0).createCell(1).setCellValue("Underlying");
        sheet.createRow(0).createCell(2).setCellValue("Notional Turnover");
        // Add rows to Excel file
        int rowNum = 1;
        for (WebElement row : rows) {
            String instrument = row.findElement(By.xpath("td[1]")).getText();
            String underlying = row.findElement(By.xpath("td[2]")).getText();
            String notionalTurnover = row.findElement(By.xpath("td[10]")).getText();
            
            // Write data to Excel
            sheet.createRow(rowNum).createCell(0).setCellValue(instrument);
            sheet.createRow(rowNum).createCell(2).setCellValue(underlying);
            sheet.createRow(rowNum).createCell(3).setCellValue(notionalTurnover);
            
            rowNum++;
        }
        
        // Write Excel file to disk
        FileOutputStream fileOut = new FileOutputStream("MarketWatchData.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        
        // Additional logic for grouping by type and underlying, and finding highest turnover can be added here
        
    }


    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
