package ru.itmo.backend.uitest.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class DetectivePage extends BasePage {
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[1]")
    private WebElement cardList;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[2]")
    private WebElement createCrimeCard;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[4]/div/button[3]")
    private WebElement criminals;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr/td[2]/table/tbody/tr/td")
    private WebElement selectVision;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[2]/td[2]/button")
    private WebElement victimName;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[3]/td[2]/button")
    private WebElement criminalName;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[4]/td[2]/button")
    private WebElement crimeTime;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[5]/td[2]/input")
    private WebElement placeOfCrime;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[6]/td[2]/input")
    private WebElement weapon;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/table/tbody/tr[7]/td[2]/select")
    private WebElement crimeType;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[3]/div/div/button")
    private WebElement submitButton;

    public DetectivePage() {
        PageFactory.initElements(driver, this);
    }

    public void clickCardList() {
        cardList.click();
    }
    public void clickCreateCrimeCard() {
        createCrimeCard.click();
    }
    public void clickCriminals() {
        criminals.click();
    }
    public void clickVision() {
        selectVision.click();
    }
    public void setVictimName() {
        victimName.click();
    }
    public void setCriminalName() {
        criminalName.click();
    }
    public void setCrimeTime() {
        crimeTime.click();
    }
    public void setPlaceOfCrime(String address) {
        placeOfCrime.clear();
        placeOfCrime.sendKeys(address);
    }
    public void setWeapon(String Weapon) {
        weapon.clear();
        weapon.sendKeys(Weapon);
    }
    public void selectCrimeType(boolean intentional) {
        Select select = new Select(crimeType);
        if (intentional) {
            select.selectByVisibleText("INTENTIONAL");
        }
        else {
            select.selectByVisibleText("UNINTENTIONAL");
        }
    }
    public void clickSubmit() {
        submitButton.click();
    }

    public void createCard(String address, String weapon, boolean intentional) throws InterruptedException {
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        clickCreateCrimeCard();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement modalContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div/div[3]")));
        clickVision();
        Thread.sleep(500);
        setCriminalName();
        setVictimName();
        setCrimeTime();
        setPlaceOfCrime(address);
        setWeapon(weapon);
        selectCrimeType(intentional);
        Thread.sleep(2000);
        clickSubmit();
    }
    public void checkCard() throws InterruptedException {
        clickCardList();
        Thread.sleep(2000);
    }

}
