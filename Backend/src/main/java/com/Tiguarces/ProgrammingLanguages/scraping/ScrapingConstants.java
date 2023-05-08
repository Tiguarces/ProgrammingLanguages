package com.Tiguarces.ProgrammingLanguages.scraping;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.regex.Pattern;

public final class ScrapingConstants {
    private ScrapingConstants() { }

    // Common
    public static final char SEMICOLON_CHAR = ';';
    public static final char COMMA_CHAR = ',';
    public static final char DOT_CHAR = '.';
    public static final char s_CHAR = 's';

    public static final String SEMICOLON = ";";

    /// Browser
    public static final String P_TAG = "p";
    public static final String UL_TAG = "ul";
    public static final String OL_TAG = "ol";
    public static final String LI_TAG = "li";
    public static final String SRC_ATTR = "src";
    public static final String SRC_ATTR_SELECTOR = "[src]";
    public static final String HREF_ATTR = "href";
    public static final String HREF_ATTR_SELECTOR = "[href]";

    public static final String NEW_HTML_LINE = "<br>";
    public static final String UL_HTML_OPEN_TAG = "<ul>";
    public static final String UL_HTML_CLOSE_TAG = "</ul>";
    public static final String LI_HTML_OPEN_TAG = "<li>";
    public static final String LI_HTML_CLOSE_TAG = "</li>";

    // Regex
    public static final String DATE_YEARS_AGO_REGEX = "(years ago|late)";
    public static final String WIKIPEDIA_TEXT_REGEX_REPLACE = "\\[(\\d+|a)\\]";

    /// Patterns
    public static final Pattern DATE_PATTERN = Pattern.compile("(\\d+ years ago|late \\d+s)");
    public static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+[,.]?\\d*");

    // Logs
    public static final Map<Integer, String> BROWSER_LOGS = ImmutableMap.<Integer, String> builder()
              .put(1, "Initializing Browser")
              .put(2, "Initializing browser finished successfully")
              .put(3, "Entering to given url >> ")
              .put(4, "Browser closed successfully")
            .build(); // Number as step

    public static final String SINGLE_LANGUAGE_TASK_LOG = "%s >= Process {}/{} languages scraped >> Current Language: {}";

    // Exceptions etc.
    public static final String PARSING_LANGUAGE_EXCEPTION_MESSAGE = "Field cannot be null and empty >> Field: ";
    public static final String PARSING_DATE_LANGUAGE_EXCEPTION_MESSAGE = "Given date is invalid >> Date: ";
    public static final String DECIMAL_VALUE_NOT_FOUND_EXCEPTION_MESSAGE = "Not found decimal value >> Given String: ";
}
