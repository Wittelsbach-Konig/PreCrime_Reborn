package ru.itmo.backend.uitest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TemplePage extends BasePage{
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[2]")
    private WebElement checkPreCogs;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[1]")
    private WebElement checkVisions;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/header/button")
    private WebElement newPreCogButton;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/header/div/div/div/form/table/tbody/tr[1]/td[2]/input")
    private WebElement preCogName;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/header/div/div/div/form/table/tbody/tr[2]/td[2]/input")
    private WebElement preCogAge;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/header/div/div/div/form/button")
    private WebElement submitButton;
    @FindBy(xpath = "//*[@id=\"psychicDropdown\"]")
    private WebElement physicsDropDown;
    @FindBy(xpath = "//*[@id=\"serotoninLevel\"]")
    private WebElement serotoninLevel;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/div[2]/div/div[1]/button")
    private WebElement serotoninEnter;
    @FindBy(xpath = "//*[@id=\"dopamineLevel\"]")
    private WebElement dopamineLevel;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/div[2]/div/div[2]/button")
    private WebElement dopamineEnter;
    @FindBy(xpath = "//*[@id=\"stressLevel\"]")
    private WebElement stressLevel;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/div/div[2]/div/div[3]/button")
    private WebElement depressantEnter;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/td[1]/div/div/table/tbody/tr/td[1]")
    private WebElement visionButton;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/td[1]/div/div/table/tbody/tr/td[4]/td[1]/button/img")
    private WebElement acceptButton;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/td[1]/div/div/table/tbody/tr/td[4]/td[2]/button/img")
    private WebElement deleteButton;


    public TemplePage() {
        PageFactory.initElements(driver, this);
    }

    public void clickCheckVisions() {
        checkVisions.click();
    }
    public void clickCheckPreCogs() {
        checkPreCogs.click();
    }
    public void selectPreCog(String name) {
        Select select = new Select(physicsDropDown);
        select.selectByVisibleText(name);
    }
    public void newPreCog(String name, int age) {
        clickCheckPreCogs();
        newPreCogButton.click();
        preCogName.sendKeys(name);
        preCogAge.sendKeys(Integer.toString(age));
        submitButton.click();
        selectPreCog(name);
    }

    private void setLevel(WebElement substanceLevel, WebElement substanceEnter, int level) {
        substanceLevel.sendKeys(Integer.toString(level));
        substanceEnter.click();
    }

    public void setSerotonin(int level) {
        setLevel(serotoninLevel, serotoninEnter, level);
    }
    public void setDopamine(int level) {
        setLevel(dopamineLevel, dopamineEnter, level);
    }
    public void setDepressant(int level) {
        setLevel(stressLevel, depressantEnter, level);
    }

    private int getSubstanceLevel(WebElement substance) {
        return Integer.parseInt(substance.getText());
    }
    public int getStressLevel() {
        return getSubstanceLevel(stressLevel);
    }
    public int getDopamineLevel() {
        return getSubstanceLevel(dopamineLevel);
    }
    public int getSerotoninLevel() {
        return getSubstanceLevel(serotoninLevel);
    }

    public void healPreCog () {
        
    }

    public void clickVision() {
        visionButton.click();
    }
    public void setAcceptButton() {
        acceptButton.click();
    }
    public void setDeleteButton() {
        deleteButton.click();
    }
    public void watchVision(boolean echo) throws InterruptedException {
        clickCheckVisions();
        Thread.sleep(1500);
        clickVision();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
        WebElement videoContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("video-container")));
        if (!echo) {setAcceptButton();}
        else {setDeleteButton();}
        Thread.sleep(1000);
    }

}
