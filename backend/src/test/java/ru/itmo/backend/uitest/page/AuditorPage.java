package ru.itmo.backend.uitest.page;

import org.openqa.selenium.support.PageFactory;

public class AuditorPage extends BasePage {

    public AuditorPage() {
        PageFactory.initElements(driver, this);
    }
}
