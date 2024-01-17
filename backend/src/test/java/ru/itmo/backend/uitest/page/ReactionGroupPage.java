package ru.itmo.backend.uitest.page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;

import java.time.Duration;

public class ReactionGroupPage extends BasePage {
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/button[1]")
    private WebElement ammunition;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/button[2]")
    private WebElement reactionGroup;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/button[3]")
    private WebElement transport;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/button[4]")
    private WebElement criminalInfo;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/header/button[1]")
    private WebElement addNewResource;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div/form/table/tbody/tr[1]/td[2]/input")
    private WebElement resourceName;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div/form/table/tbody/tr[2]/td[2]/input")
    private WebElement resourceAmount;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div/form/table/tbody/tr[3]/td[2]/input")
    private WebElement maxResourceAmount;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div/form/table/tbody/tr[4]/td[2]/select")
    private WebElement resourceType;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div/form/button")
    private WebElement submitButtonAmmo;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[1]/div/div/div/div")
    private WebElement ammoModalContent;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/header/button[1]")
    private WebElement registerNewMan;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/header/button[2]")
    private WebElement showWorkingMen;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/div[1]/div/div/form/table/tbody/tr[1]/td[2]/input")
    private WebElement memberName;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/div[1]/div/div/form/table/tbody/tr[2]/td[2]/input")
    private WebElement telegramId;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/div[1]/div/div/form/button")
    private WebElement memberSubmit;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[2]/div/div[1]/div/div")
    private WebElement memberModalContent;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/header/button")
    private WebElement newTransport;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/div/div[2]/div/div/form/table/tbody/tr[1]/td[2]/input")
    private WebElement transportBrand;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/div/div[2]/div/div/form/table/tbody/tr[2]/td[2]/input")
    private WebElement transportModel;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/div/div[2]/div/div/form/table/tbody/tr[3]/td[2]/input")
    private WebElement maxFuel;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/div/div[2]/div/div/form/button")
    private WebElement transportSubmit;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[3]/div/div/div[2]/div/div")
    private WebElement transportModalContent;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[2]/div/table/tbody/tr[1]/td[4]/button")
    private WebElement appointGroup;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[1]/div")
    private WebElement appointModalContent;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[1]/div/form/button")
    private WebElement appointMember;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[1]/div/form/table/tbody/tr[1]/td[2]")
    private WebElement chooseMember;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[2]/div/table/tbody/tr[1]/td[4]")
    private WebElement groupStatus;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[1]/table/tbody/tr[7]/td[2]/select")
    private WebElement selectStatus;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/div[4]/div/div[2]/div/table/tbody/tr[1]/td[1]")
    private WebElement criminalName;

    public ReactionGroupPage() {
        PageFactory.initElements(driver, this);
    }

    public void purchaseAmmo(String name, int amount, int maxAmount, String type) {
        ammunition.click();
        addNewResource.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOf(ammoModalContent));
        resourceName.sendKeys(name);
        resourceAmount.sendKeys(Integer.toString(amount));
        maxResourceAmount.sendKeys(Integer.toString(maxAmount));
        resourceType.sendKeys(type);
        submitButtonAmmo.click();
    }

    public void setNewTransport(String brand, String model, int maximum) {
        transport.click();
        newTransport.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOf(transportModalContent));
        transportModel.sendKeys(model);
        transportBrand.sendKeys(brand);
        maxFuel.sendKeys(Integer.toString(maximum));
        transportSubmit.click();
    }
    public void setRegisterNewMan(String name, int telegram) {
        reactionGroup.click();
        registerNewMan.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOf(memberModalContent));
        memberName.sendKeys(name);
        telegramId.sendKeys(Integer.toString(telegram));
        memberSubmit.click();
    }
    public void setAppointMember() {
        criminalInfo.click();
        appointGroup.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOf(appointModalContent));
        chooseMember.click();
        appointMember.click();
    }
    public void arrestCriminal(String status) {
        criminalName.click();
        Select select = new Select(selectStatus);
        select.selectByVisibleText(status);
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }



}
