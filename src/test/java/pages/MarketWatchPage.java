package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class MarketWatchPage {

    private WebDriver driver;
    private By marketWatchButton = By.linkText("ETF Market Watch"); // Modify with actual element
    private By instrumentTable = By.xpath("//div[@class='largetable']"); // Modify with actual table id

    public MarketWatchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void goToMarketWatch() {
        WebElement marketWatchLink = driver.findElement(marketWatchButton);
        marketWatchLink.click();
    }

    public List<WebElement> getInstrumentRows() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(instrumentTable));
        return driver.findElements(By.xpath("//table[@id='ContentPlaceHolder1_GridView1']//tr"));
    }
}
