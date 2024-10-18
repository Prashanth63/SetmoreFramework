package qa.mobile;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.mobile.utils.AppiumServer;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class Login {
    public AppiumDriver driver;
public WebDriverWait wait;
    private AppiumServer appiumServer = new AppiumServer();
    private int appiumPort;

    @Parameters({"deviceName", "platformVersion", "appiumPort", "username", "password", "udid", "systemPort"})
    @BeforeClass
    public void setup(String deviceName, String platformVersion,
                      String appiumPort, String username, String password, String udid, String systemPort)
            throws MalformedURLException, IOException {

        this.appiumPort = Integer.parseInt(appiumPort);
        // Start the Appium server on the given port
        appiumServer.startServer(this.appiumPort);
        int systemPortValue = Integer.parseInt(systemPort);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", platformVersion);
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("udid", udid);
        caps.setCapability("systemPort",systemPort);
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.adaptavant.setmore");
        caps.setCapability("appActivity", "com.anywhere.container.presentation.ui.MainActivity");
        caps.setCapability("noReset", false);
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("dontStopAppOnReset", true);
        caps.setCapability("autoGrantPermissions", true);
        URL url = new URL("http://127.0.0.1:" + this.appiumPort + "/wd/hub");
        driver = new AndroidDriver(url, caps);
    }

    @Test
    @Parameters({"username", "password"})
    public void loginWithValidCredentials(String username, String password) throws InterruptedException {
        WebElement continueButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(5)"));
        continueButton.click();
        Thread.sleep(1000);
        WebElement emailField = driver.findElement(AppiumBy.accessibilityId("email_txt"));
        WebElement passwordField = driver.findElement(AppiumBy.accessibilityId("password_txt"));
        WebElement loginButton = driver.findElement(AppiumBy.accessibilityId("login_btn"));
        emailField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Verification
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@text='Settings']")));
            System.out.println("Logged in successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            wait =new WebDriverWait(driver,Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.androidUIAutomator("new UiSelector().text(\"Cancel\")"))).click();
        }
        catch (Exception e){
            System.out.println("Widget popup not appeared");
        }

    }
    @Test(priority = 2)
    public void logOut() throws InterruptedException {
        Thread.sleep(1000);
        WebElement settings = driver.findElement(AppiumBy.accessibilityId("Settings"));
        settings.click();
        scrollIntoView("Log out");
        Thread.sleep(1000);
        WebElement logout = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Log out\")"));
        logout.click();
        Thread.sleep(1000);
        WebElement yesLogout = driver.findElement(AppiumBy.accessibilityId("logout_confirm_btn"));
        yesLogout.click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@text='Continue with Email']")));
            System.out.println("Logged out successfully");
        } catch (Exception e) {
            System.out.println("Logout failed");
        }

    }

    public void scrollIntoView(String elementText) {
        WebElement scrollElement = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                        ".scrollIntoView(new UiSelector().text(\"" + elementText + "\"))"));
    }
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    public void stopAppiumServer() {
        appiumServer.stopServer(appiumPort);  // Stop server only on the specific port
        System.out.println("Appium server stopped on port: " + appiumPort);
    }
    public void clickOn(WebElement element){


    }
}
