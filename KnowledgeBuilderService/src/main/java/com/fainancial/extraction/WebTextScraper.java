package com.fainancial.extraction;

import com.fainancial.extraction.locators.IncomeTaxLocators;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.fainancial.extraction.constants.Constants.*;


public class WebTextScraper {
    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
        IncomeTaxLocators incomeTaxLocators = new IncomeTaxLocators();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions action = new Actions(driver);
//        List<Pair<String, String>> ruleData = new ArrayList<>();

        try {
            driver.get(incomeTaxRuleUrl);

            //total records
            String totalRecords = driver.findElement(By.cssSelector(incomeTaxLocators.totalRecords)).getText().trim();
            String totalRecordsCount = "";
            if (totalRecords.contains(RECORD)) {
                totalRecordsCount = totalRecords.split(" ")[0];
            }
            List<WebElement> ruleLinks;
            for (int i = 1; i <= Integer.parseInt(totalRecordsCount); i++) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(incomeTaxLocators.ruleTitleLink)));
                if(i!=1) {
                    action.sendKeys(Keys.END).perform();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(incomeTaxLocators.inputPageNumber)));
                    js.executeScript("arguments[0].click();", driver.findElement(By.cssSelector(incomeTaxLocators.inputPageNumber)));
//                    js.executeScript("arguments[0].value='your text here';", input);
//                    driver.findElement(By.cssSelector(incomeTaxLocators.inputPageNumber)).click();
                    driver.findElement(By.cssSelector(incomeTaxLocators.inputPageNumber)).clear();
                    driver.findElement(By.cssSelector(incomeTaxLocators.inputPageNumber)).sendKeys(String.valueOf(i));
                    action.sendKeys(Keys.ENTER).perform();
                    Thread.sleep(2000);
                    driver.navigate().refresh();
                    Thread.sleep(2500);
                }
                ruleLinks = driver.findElements(By.cssSelector(incomeTaxLocators.ruleTitleLink));
                for (WebElement ruleLink : ruleLinks) {
                    js.executeScript("window.scrollBy(0,0)", "");
                    String ruleTitle = ruleLink.getText();
                    js.executeScript("arguments[0].click();", ruleLink);

                    // Wait and extract text content from the rule page
                    Thread.sleep(3000); // You can use WebDriverWait instead
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(incomeTaxLocators.ruleDialogFrame)));
                    driver.switchTo().frame(driver.findElement(By.className(incomeTaxLocators.ruleDialogFrame)));
                    StringBuilder ruleContent = new StringBuilder();
                    ruleContent.append(driver.findElement(By.className(incomeTaxLocators.ruleDialogContent)).getText());
                    ruleContent.append(END_OF_RULE);
                    System.out.println(ruleTitle);
                    driver.switchTo().defaultContent();
                    Thread.sleep(300);

                    action.sendKeys(Keys.ESCAPE).perform();

//                    ruleData.add((Pair.of(ruleTitle, ruleContent.toString())));

                    //FILE CREATION
                    String sanitizedTitle = ruleTitle.trim().replaceAll(REGEX_FILE_NAME, "");
                    String fileName = sanitizedTitle + ".txt";
                    File file = new File(RULE_FOLDER + fileName);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write(String.valueOf(ruleContent));
                        System.out.println("Text successfully written to " + file.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Error writing file: " + e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
