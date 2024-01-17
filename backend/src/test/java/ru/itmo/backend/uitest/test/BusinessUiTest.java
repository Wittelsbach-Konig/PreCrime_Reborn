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
        auditorPage = new AuditorPage();
        reactionGroupPage = new ReactionGroupPage();
    }

    @Test
    @Sql(value = {"/create-visions-selenium.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Order(1)
    public void Investigation() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        Thread.sleep(1000);
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
        Thread.sleep(1000);
        templePage.clickLogOut();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        Thread.sleep(1000);
        registerPage.registerNewUser("John", "Enderton",
                "detective", "detective",
                "email@gmail.com", 433915408,
                true, false,false,false);
        loginPage.auth("detective","detective");
        Thread.sleep(1000);
        detectivePage.createCard("Washington", "Axe", true);
        Thread.sleep(1000);
        detectivePage.checkCard();

        Thread.sleep(3000);
        detectivePage.clickLogOut();
    }

    @Test
    @Order(2)
    public void PreCogCommission() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.auth("technic", "technic");
        Thread.sleep(1000);

        templePage.clickCheckPreCogs();
        Thread.sleep(1000);
        templePage.selectPreCog("alice");
        int prevSerotonin = templePage.getSerotoninLevel();
        int prevDopamine = templePage.getDopamineLevel();
        int prevStress = templePage.getStressLevel();
        Thread.sleep(1000);
        templePage.healPreCog("alice");
        Thread.sleep(1000);

        Assertions.assertNotEquals(templePage.getSerotoninLevel(), prevSerotonin);
        Assertions.assertNotEquals(templePage.getDopamineLevel(), prevDopamine);
        Assertions.assertNotEquals(templePage.getStressLevel(), prevStress);

        templePage.newPreCog("bob", 60);
        Thread.sleep(1500);
        templePage.clickRetirePreCog("bob");
        Assertions.assertEquals(templePage.getPreCogStatus("bob"), "No");
        Thread.sleep(1000);
        templePage.clickLogOut();
    }

    @Test
    @Order(3)
    public void ReactionGroupAppointment() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        Thread.sleep(1000);
        registerPage.registerNewUser("Coba", "Ivanov",
                "reactiongroup", "reactiongroup",
                "email@rambler.ru", 433915408,
                false, false,false,true);
        Thread.sleep(1000);
        loginPage.auth("reactiongroup", "reactiongroup");
        Thread.sleep(1000);
        reactionGroupPage.purchaseAmmo("M4", 10, 100, "WEAPON");
        Thread.sleep(1000);
        reactionGroupPage.setNewTransport("Kia", "Solaris", 160);
        Thread.sleep(1000);
        reactionGroupPage.setRegisterNewMan("Patrick", 433915408);
        Thread.sleep(1000);
        reactionGroupPage.setAppointMember();
        Thread.sleep(1500);
        reactionGroupPage.arrestCriminal("CAUGHT");
        Thread.sleep(1000);
        reactionGroupPage.clickLogOut();


    }

    @Test
    @Order(4)
    public void AuditorHinting() throws InterruptedException {
        driver.get(ConfProperties.getProperty("mainpage"));

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage.clickRegisterBtn();
        Thread.sleep(1000);
        registerPage.registerNewUser("Vasiliy", "Shemyaka",
                "auditor", "auditor",
                "email@gmail.ru", 433915408,
                false, false,true,false);
        Thread.sleep(1000);

        loginPage.auth("auditor", "auditor");
        Thread.sleep(1000);
        auditorPage.clickLogOut();

    }

}
