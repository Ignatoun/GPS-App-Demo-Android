package com.example.springboot;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class TripTableTest {

	public static TripTablePage tripTablePage;

	public static WebDriver driver;

	@Test
	public void tripTableTest() {

		System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));

		driver = new ChromeDriver();

		tripTablePage = new TripTablePage(driver);

		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(ConfProperties.getProperty("tripHistoryPage"));

		String title = driver.getTitle();
		Assert.assertEquals("Trip History", title);

		Assert.assertEquals("1", tripTablePage.getFirstRowId());
		Assert.assertEquals("newTrip", tripTablePage.getFirstRowName());

		Assert.assertEquals("7", tripTablePage.getSeventhRowId());
		Assert.assertEquals("Morning run", tripTablePage.getSeventhRowName());

		Assert.assertEquals("3", tripTablePage.getThirdRowId());
		Assert.assertEquals("Around the Worl Part 1", tripTablePage.getThirdRowName());

	}

	@AfterClass
	public static void tearDown() {
		driver.quit();
	}

}
