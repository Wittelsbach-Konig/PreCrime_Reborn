package ru.itmo.backend.uitest.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuditorPage extends BasePage {

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[1]")
    private WebElement cardList;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div/table/tbody/tr[1]/td[3]")
    private WebElement doubleClick;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div[2]/div/div")
    private WebElement auditorModalContent;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div[2]/div/div/div/button")
    private WebElement reportMistake;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div[2]/div/div")
    private WebElement auditor2ModalContent;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div[2]/div/div/div/table/tbody/tr/td[2]/textarea")
    private WebElement textMessage;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div/div/div/div[2]/div/div/div/button")
    private WebElement submitMessage;

    public AuditorPage() {
        PageFactory.initElements(driver, this);
    }

    public void setReportMistake(String message) throws InterruptedException {
        Thread.sleep(2000);
        cardList.click();
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofMillis(2000));
        WebElement modalContent3 = wait3.until(ExpectedConditions.visibilityOf(doubleClick));
        Actions action = new Actions(driver);
        action.doubleClick(doubleClick).perform();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOf(auditorModalContent));
        reportMistake.click();
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofMillis(2000));
        WebElement modalContent1 = wait1.until(ExpectedConditions.visibilityOf(auditor2ModalContent));
        textMessage.sendKeys(message);
        submitMessage.click();
    }

}
