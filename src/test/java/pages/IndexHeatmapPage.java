package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class IndexHeatmapPage {
    WebDriver driver;

    public IndexHeatmapPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locator for all index cards in Index Heatmap
    private By indexCards = By.xpath("//div[contains(@class, 'boxred ng-scope')]");

    public List<WebElement> getAllIndexCards() {
        return driver.findElements(indexCards);
    }
}
