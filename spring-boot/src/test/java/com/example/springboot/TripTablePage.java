package com.example.springboot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TripTablePage {

    public WebDriver driver;
    public TripTablePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[1]/td[1]")
    private WebElement firstRowId;

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[1]/td[2]")
    private WebElement firstRowName;

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[7]/td[1]")
    private WebElement seventhRowId;

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[7]/td[2]")
    private WebElement seventhRowName;

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[3]/td[1]")
    private WebElement thirdRowId;

    @FindBy(xpath = "//*[@id=\"j_idt5_data\"]/tr[3]/td[2]")
    private WebElement thirdRowName;

    public String getFirstRowId() {
        return firstRowId.getText();
    }

    public String getFirstRowName() {
        return firstRowName.getText();
    }

    public String getSeventhRowId() {
        return seventhRowId.getText();
    }

    public String getSeventhRowName() {
        return seventhRowName.getText();
    }

    public String getThirdRowId() {
        return thirdRowId.getText();
    }

    public String getThirdRowName() {
        return thirdRowName.getText();
    }


}
