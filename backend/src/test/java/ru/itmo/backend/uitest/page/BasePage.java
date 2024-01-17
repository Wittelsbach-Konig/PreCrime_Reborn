package ru.itmo.backend.uitest.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

abstract public class BasePage {
    protected static WebDriver driver;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/button")
    private WebElement burgerMenu;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/ul/li[1]")
    private WebElement burgerDetective;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/ul/li[2]")
    private WebElement burgerAuditor;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/ul/li[3]")
    private WebElement burgerReactionGroup;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/ul/li[4]")
    private WebElement burgerTechnic;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[1]/header/button")
    private WebElement logOut;

    public static void setDriver(WebDriver webDriver) { driver = webDriver; }

    public void clickBurgerMenu() {
        burgerMenu.click();
    }
    public void chooseDetective() {
        clickBurgerMenu();
        burgerDetective.click();
    }
    public void chooseAuditor() {
        clickBurgerMenu();
        burgerAuditor.click();
    }
    public void chooseReactionGroup() {
        clickBurgerMenu();
        burgerReactionGroup.click();
    }
    public void chooseTechnic() {
        clickBurgerMenu();
        burgerTechnic.click();
    }
    public void clickLogOut() {
        logOut.click();
    }
}
