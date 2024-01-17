package ru.itmo.backend.uitest.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.itmo.backend.uitest.annotations.SeleniumTest;
import ru.itmo.backend.uitest.confProperties.ConfProperties;
import ru.itmo.backend.uitest.page.BasePage;

import java.util.concurrent.TimeUnit;

@SeleniumTest
abstract public class BaseSeleniumTest {
    protected WebDriver driver;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setup() {
        // Директория home/username
        String homeDir = System.getProperty("user.home");
        // Директория с браузером. Поменять на свою в conf.properties
        String chromeBinaryLocation = homeDir + ConfProperties.getProperty("chrome");
        // Директория с драйвером хрома. Поменять на свою в conf.properties
        String chromedriverLocation = homeDir + ConfProperties.getProperty("chromedriver");
        System.setProperty("webdriver.chrome.driver", chromedriverLocation);
        ChromeOptions options = getChromeOptions(chromeBinaryLocation);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        BasePage.setDriver(driver);
    }

    private static ChromeOptions getChromeOptions(String chromeBinaryLocation) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("enable-automation");
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-gpu");
        options.setBinary(chromeBinaryLocation);
        return options;
    }

    @AfterEach
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
