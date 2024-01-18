package ru.itmo.backend.uitest.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage{
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/div/form/div/div[1]/input")
    
    private WebElement loginField;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/div/form/div/div[2]/input")
    private WebElement passField;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/div/form/div/div[3]/button")
    private WebElement submitButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div[2]/div/div/form/div/div[4]/label")
    private WebElement registerButton;

    public LoginPage() {
        PageFactory.initElements(driver, this);
    }

    public void inputLogin(String login) {
        loginField.clear();
        loginField.sendKeys(login);
    }

    public void inputPass(String pass) {
        passField.clear();
        passField.sendKeys(pass);
    }

    public void clickSubmitBtn() {
        submitButton.click();
    }

    public void auth (String login, String pass) {
        inputLogin(login);
        inputPass(pass);
        clickSubmitBtn();
    }

    public void clickRegisterBtn() {
        registerButton.click();
    }
}
