package qa.mobile.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;

public class AppiumServer {

    private AppiumDriverLocalService service;

    /**
     * Starts the Appium server on the specified port.
     *
     * @param port The port on which the Appium server should run.
     */
    public void startServer(int port) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingPort(port)
                .withIPAddress("127.0.0.1")
                .withArgument(() -> "--base-path", "/wd/hub");

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        if (service.isRunning()) {
            System.out.println("Appium server started on port: " + port);
        } else {
            System.err.println("Failed to start Appium server on port: " + port);
        }
    }

    /**
     * Stops the Appium server running on the specified port.
     */
    public void stopServer(int port) {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped on port: " + port);
        } else {
            System.err.println("No running server found on port: " + port);
        }
    }
}
