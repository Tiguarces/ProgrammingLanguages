package com.Tiguarces.ProgrammingLanguages.scraping.browser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrowserConstants {
    public BrowserConstants(@Value("${custom.chromium.binary}") final String binary,
                            @Value("${custom.chromium.driver}") final String driver) {

        this.BINARY = binary;
        this.DRIVER_BINARY = driver;
        this.DRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
        this.ARGUMENTS = List.of(
                "--no-sandbox",
                "--window-size=1920,1080",
                "--start-maximized",
                "--disable-dev-shm-usage",
                "--remote-allow-origins=*",
                "--blink-settings=imagesEnabled=false",
                "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36"
        );
    }

    public final String BINARY;
    public final String DRIVER_BINARY;
    public final String DRIVER_SYSTEM_PROPERTY;
    public final List<String> ARGUMENTS;
}
