package com.Tiguarces.ProgrammingLanguages.scraping.browser;

import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.OutputStream;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.*;
import static com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserConstants.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.util.CollectionUtils.*;

@Slf4j
public final class BrowserClient implements Closeable {
    private final ChromeDriver browserClient;
    private final ChromeDriverService browserService;
    private Document websiteDocument;

    public BrowserClient() {
        log.info(BROWSER_LOGS.get(1));
        System.setProperty(DRIVER_SYSTEM_PROPERTY, DRIVER_BINARY);

        browserService = ChromeDriverService.createDefaultService();
        browserService.sendOutputTo(OutputStream.nullOutputStream());

        ChromeOptions options = new ChromeOptions();
                      options.addArguments(ARGUMENTS);
                      options.setBinary(BINARY);

        browserClient = new ChromeDriver(options);
        browserClient.manage()
                     .timeouts()
                     .pageLoadTimeout(Duration.of(45, SECONDS));

        log.info(BROWSER_LOGS.get(2));
    }

    public void loadPage(final String url) {
        log.info(BROWSER_LOGS.get(3) + url);

        browserClient.get(url);
        websiteDocument = Jsoup.parse(browserClient.getPageSource());
    }

    private static final String WIKIPEDIA_DESCRIPTION_SELECTOR = ConfigurationLoader.wikipediaTask.description();

    public List<Element> findElements(final String selector) {
        return websiteDocument.select(Objects.requireNonNull(selector));
    }

    public String selectElementAndGetValue(final Element element, final String selector) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(selector);

        var elements = element.select(selector);
        return (!isEmpty(elements))
                 ? elements.text() : null ;
    }

    public String findElement(final String selector) {
        Elements foundElement = websiteDocument.select(selector);

        if(!foundElement.isEmpty()) {
            Element currentElement = foundElement.get(0);

            if(selector.contains(SRC_ATTR_SELECTOR)) {
                return currentElement.attr(SRC_ATTR);
            }

            if(selector.contains(HREF_ATTR_SELECTOR)) {
                return currentElement.attr(HREF_ATTR);
            }

            if(selector.equals(WIKIPEDIA_DESCRIPTION_SELECTOR)) {
                Elements children = foundElement.get(0).nextElementSiblings();
                StringBuilder descriptionBuilder = new StringBuilder();
                String tagName;

                for (Element child : children) {
                    currentElement = child;
                    descriptionBuilder.append(currentElement.text())
                                      .append(NEW_HTML_LINE);

                    if(((tagName = currentElement.tagName()).equals(UL_TAG)) || tagName.equals(OL_TAG)) {
                        var listOfLi = currentElement.select(LI_TAG);
                        int liAmount = listOfLi.size();

                        descriptionBuilder.append(UL_HTML_OPEN_TAG);

                        for (int i = 0; i < liAmount; i++) {
                            if(i == 0 && (i != liAmount - 1)) {
                                descriptionBuilder.append(NEW_HTML_LINE);
                            }
                            descriptionBuilder.append(LI_HTML_OPEN_TAG)
                                              .append((listOfLi.get(i)).text())
                                              .append(LI_HTML_CLOSE_TAG);

                        } descriptionBuilder.append(UL_HTML_CLOSE_TAG);

                    } else if(!tagName.equals(P_TAG)) {
                        break;
                    }
                } return descriptionBuilder.toString();

            } else return currentElement.text();

        } else return null;
    }

    @Override
    public void close() {
        browserClient.close();
        browserService.close();   log.info(BROWSER_LOGS.get(4));
    }
}
