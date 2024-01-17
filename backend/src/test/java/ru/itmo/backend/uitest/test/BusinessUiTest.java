package ru.itmo.backend.uitest.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.springframework.test.context.jdbc.Sql;
import ru.itmo.backend.uitest.confProperties.ConfProperties;
import ru.itmo.backend.uitest.page.*;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessUiTest extends BaseSeleniumTest{
    public static LoginPage loginPage;
    public static RegisterPage registerPage;
    public static DetectivePage detectivePage;
    public static AuditorPage auditorPage;
    public static TemplePage templePage;
    public static ReactionGroupPage reactionGroupPage;


    @BeforeEach
    public void setupPages() {
        loginPage = new LoginPage();
        registerPage = new RegisterPage();
        templePage = new TemplePage();
        detectivePage = new DetectivePage();
//        auditorPage = new AuditorPage();
//        reactionGroupPage = new ReactionGroupPage();
    }

    @Test
    @Sql(value = {"/create-visions-selenium.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Order(1)
    public void Investigation() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        registerPage.registerNewUser("Willy", "Wolles",
                "technic", "technic",
                "email@yandex.ru", 433915408,
                false, true, false, false);
        loginPage.auth("technic", "technic");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        templePage.newPreCog("alice", 20);
        Thread.sleep(1000);
        templePage.watchVision(false);
        templePage.clickLogOut();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        registerPage.registerNewUser("John", "Enderton",
                "detective", "detective",
                "email@gmail.com", 433915408,
                true, false,false,false);
        loginPage.auth("detective","detective");
        Thread.sleep(1000);
        detectivePage.createCard("Washington", "Axe", true);
        detectivePage.checkCard();
        Thread.sleep(3000);
    }

    @Test
    @Order(2)
    public void PreCogCommission() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.auth("technic", "technic");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Thread.sleep(1000);
        templePage.newPreCog("alice", 20);
        Thread.sleep(1000);
        templePage.watchVision(true);
        Thread.sleep(2000);
        templePage.clickLogOut();
    }

}
