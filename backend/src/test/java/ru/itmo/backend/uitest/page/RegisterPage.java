package ru.itmo.backend.uitest.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Set;

public class RegisterPage extends BasePage{
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[1]/input")
    private WebElement firstNameField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[2]/input")
    private WebElement lastNameField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[3]/input")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[4]/input")
    private WebElement emailField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[5]/input")
    private WebElement passField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[1]/div[6]/input")
    private WebElement confirmPassField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[2]/input")
    private WebElement telegramIdField;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[3]/label[1]/input")
    private WebElement detectiveCheckBox;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[3]/label[2]/input")
    private WebElement technicCheckBox;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[3]/label[3]/input")
    private WebElement auditorCheckBox;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[1]/div[3]/label[4]/input")
    private WebElement reactionGroupCheckBox;
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/div/form/div/div[2]/button")
    private WebElement submitButton;

    public RegisterPage() {
        PageFactory.initElements(driver, this);
    }

    public void registerNewUser(String firstName, String lastName,
                                String login, String password,
                                String email, int telegramId,
                                boolean isDetective, boolean isTechnic,
                                boolean isAuditor, boolean isReactionGroup) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        loginField.sendKeys(login);
        emailField.sendKeys(email);
        passField.sendKeys(password);
        confirmPassField.sendKeys(password);
        telegramIdField.sendKeys(Integer.toString(telegramId));
        if (isAuditor) { auditorCheckBox.click(); }
        if (isDetective) { detectiveCheckBox.click(); }
        if (isTechnic) { technicCheckBox.click(); }
        if (isReactionGroup) { reactionGroupCheckBox.click(); }
        submitButton.click();

    }
}
