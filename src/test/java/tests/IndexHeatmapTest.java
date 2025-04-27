package tests;

import pages.IndexHeatmapPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;

public class IndexHeatmapTest {
    WebDriver driver;
    IndexHeatmapPage heatmapPage;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "/msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://bseindia.com/markets.html");

        heatmapPage = new IndexHeatmapPage(driver);
    }

    @Test
    public void analyzeIndexHeatmap() {
        List<WebElement> cards = heatmapPage.getAllIndexCards();

        Map<String, Double> gainers = new HashMap<>();
        Map<String, Double> losers = new HashMap<>();

        for (WebElement card : cards) {
            String cardText = card.getText();
            String[] lines = cardText.split("\n");

            if (lines.length >= 3) {
                String indexName = lines[0].trim();
                String changeText = lines[2].trim(); // example: +262.06 +0.63%

                String pointsChange = changeText.split(" ")[0].replace(",", "");

                double change = 0;
                try {
                    change = Double.parseDouble(pointsChange);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing change for index: " + indexName);
                }

                if (change > 0) {
                    gainers.put(indexName, change);
                } else {
                    losers.put(indexName, change);
                }
            }
        }

        // Highest Gainer
        String highestGainer = Collections.max(gainers.entrySet(), Map.Entry.comparingByValue()).getKey();
        double highestGainValue = gainers.get(highestGainer);

        // Highest Loser
        String highestLoser = Collections.min(losers.entrySet(), Map.Entry.comparingByValue()).getKey();
        double highestLossValue = losers.get(highestLoser);

        // Indexes gained more than 50 points
        List<String> gainedMoreThan50 = new ArrayList<>();
        for (Map.Entry<String, Double> entry : gainers.entrySet()) {
            if (entry.getValue() > 50) {
                gainedMoreThan50.add(entry.getKey() + " (" + entry.getValue() + " points)");
            }
        }

        // Printing results
        System.out.println("Indexes that Gained:");
        gainers.forEach((k, v) -> System.out.println(k + " : " + v + " points"));

        System.out.println("\nIndexes that Lost:");
        losers.forEach((k, v) -> System.out.println(k + " : " + v + " points"));

        System.out.println("\nHighest Gainer: " + highestGainer + " (" + highestGainValue + " points)");
        System.out.println("Highest Loser: " + highestLoser + " (" + highestLossValue + " points)");

        System.out.println("\nIndexes gaining more than 50 points:");
        gainedMoreThan50.forEach(System.out::println);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
