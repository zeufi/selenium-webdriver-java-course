package base;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.HomePage;
import utils.CookieManager;
import utils.EventReporter;
import utils.WindowManager;

import java.io.File;
import java.io.IOException;

public class BaseTests {

    private EventFiringWebDriver driver;
    protected HomePage homePage;

    @BeforeClass
    @Parameters("browser")
    public void setUp(String browser){
        String driverExtention = "";
        if(System.getenv("RUNNER_OS") != null) {
            driverExtention = "-linux";
        }
        if(browser.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver", "resources/geckodriver" + driverExtention);
            driver = new EventFiringWebDriver(new FirefoxDriver(getFirefoxOptions()));
        }
        else if(browser.equalsIgnoreCase("chrome")){
            System.setProperty("webdriver.chrome.driver", "resources/chromedriver" + driverExtention);
            driver = new EventFiringWebDriver(new ChromeDriver(getChromeOptions()));
        }
        driver.register(new EventReporter());
    }

    @BeforeMethod
    public void goHome(){
        driver.get("https://the-internet.herokuapp.com/");
        homePage = new HomePage(driver);
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    @AfterMethod
    public void recordFailure(ITestResult result){
        if(ITestResult.FAILURE == result.getStatus())
        {
            TakesScreenshot camera = (TakesScreenshot)driver;
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            try{
                Files.move(screenshot, new File("resources/screenshots/" + result.getName() + ".png"));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public WindowManager getWindowManager(){
        return new WindowManager(driver);
    }
    private FirefoxOptions getFirefoxOptions(){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        // Default headless mode off, set to true based on env var
        boolean headless = Boolean.parseBoolean(System.getenv("HEADLESS_FIREFOX"));
        options.setHeadless(headless);
        return options;
    }
    private ChromeOptions getChromeOptions(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        // Default headless mode off, set to true based on env var
        boolean headless = Boolean.parseBoolean(System.getenv("HEADLESS_CHROME"));
        options.setHeadless(headless);
        return options;
    }

    public CookieManager getCookieManager(){
        return new CookieManager(driver);
    }
}
