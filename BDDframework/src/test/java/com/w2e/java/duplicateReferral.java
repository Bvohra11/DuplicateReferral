package com.w2e.java;

import java.io.BufferedReader;
import java.io.*;
import java.io.FileInputStream;

import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.List;

import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

public class duplicateReferral {
	static List<HashMap<String, String>> excelData = new ArrayList<HashMap<String, String>>();

	static WebDriver driver;
	static String candidateEmail;
	static String actualDate;
	static Boolean userAvailable;
	static int index;
	static Boolean intClear;
	static HashMap<String, String> rowData = new HashMap<>();

	static ArrayList<String> filelist = new ArrayList<String>();
	static ArrayList<String> mobilelist = new ArrayList<String>();
	static String interviewStatus;

	public static void main(String[] args) throws Throwable {

		System.setProperty("webdriver.chrome.driver", "src/test/resources/executables/chromedriver.exe");
		
		//-----For headless option ----
//	ChromeOptions options = new ChromeOptions();
//	options.addArguments("--headless");
//	WebDriver driver = new ChromeDriver(options);
		
	//	------comment below line as well for headless run---
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15L, TimeUnit.SECONDS);

		ReadFile();
		Search_Id(driver);
		

	}

	public static Boolean ReadCandidateFile(String candidateEmail) throws Throwable {
		intClear = null;

		File file = new File("src/test/resources/excel/Candidate File.xlsx");
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

		Sheet sheet = workbook.getSheet("sheet1");

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		for (int i = 1; i <= rowCount; i++) {

			Row row = sheet.getRow(0);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				String key = cell.getStringCellValue();
				if (sheet.getRow(i).getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {

					String value = sheet.getRow(i).getCell(j).getStringCellValue().trim();

					rowData.put(key, value);
				} else {
					DataFormatter formatter = new DataFormatter();
					String value = formatter.formatCellValue(sheet.getRow(i).getCell(j)).trim();

					rowData.put(key, value);
				}

			}
			String IntDate = "";

			for (String k : rowData.keySet()) {
				if (rowData.containsValue((candidateEmail))) {
					if (rowData.containsValue(("Interview Completed - DQ"))) {
						String status = "Rejected due to interview not clear";
						System.out.println(rowData.get(k));
						System.out.println("status" + status);
						IntDate = rowData.get("Date of Interview").toString();

						System.out.println("IntDate" + IntDate);

						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM");
						java.util.Date InterviewDate = sdf.parse(IntDate);

						SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
						Calendar c = Calendar.getInstance();
						c.add(Calendar.MONTH, -6);
						System.out.println(format.format(c.getTime()));
						java.util.Date sixtyDaysAgoDate = sdf.parse(format.format(c.getTime()));

						if (InterviewDate.before(sixtyDaysAgoDate)) {
							System.out.println(InterviewDate + " is older than 60 days!");
							interviewStatus = "Considered";
							intClear = true;
						} else {
							System.out.println(InterviewDate + " is less than 60 days!");
							intClear = false;
							interviewStatus = "NotConsidered";

						}
					}

					else if (rowData.containsValue("Interview Completed - Cleared")) {

						System.out.println("interview cleard" + rowData.get(k));
						interviewStatus = "Considered";
						intClear = true;

					} else if (rowData.containsValue(null)) {

						System.out.println("interview cleard" + rowData.get(k));
						interviewStatus = "Considered";
						intClear = true;

					}
				}

			}

		}
		System.out.println((intClear) + " interviewStatus");
		return intClear;
	}

	public static ArrayList<String> ReadFile() throws Throwable {

		FileInputStream fis = new FileInputStream("src/test/resources/excel/TestDatarecord.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet("Credentials");

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		int col_num = 0;
		int Mobile_col_num = 0;
		Row firstRow = sheet.getRow(0);

		for (int j = 0; j < firstRow.getLastCellNum(); j++) {

			if (firstRow.getCell(j).getStringCellValue().trim().equals("Email Id")) {
				col_num = j;
				System.out.println("emailid:" + firstRow.getCell(j).getStringCellValue());
			}
			if (firstRow.getCell(j).getStringCellValue().trim().contains("Mobile Number 1")) {
				Mobile_col_num = j;
				System.out.println("mobile:" + firstRow.getCell(j).getStringCellValue());
			}

		}
		for (int i = 1; i <= rowCount; i++) {
			Row row = sheet.getRow(i);
			String value1 = row.getCell(col_num).getStringCellValue();
			Cell Mobilenumber = row.getCell(Mobile_col_num);
			DataFormatter formatter = new DataFormatter();
			String MobileNumber = formatter.formatCellValue(Mobilenumber);
			System.out.println("Mobilennumber's Listed:" + value1 + MobileNumber);
			filelist.add(value1);
			mobilelist.add(MobileNumber);

		}
		return filelist;
	}

	public static int Search_Id(WebDriver driver) throws Throwable {
		for (index = 0; index < filelist.size(); index++) {
			candidateEmail = filelist.get(index);
			driverLaunch(driver);
			driver.findElement(By.xpath("//*[@id='st-peopleLink']")).click();

			WebElement element = driver.findElement(By.id("st-people-searchInput"));
			element.sendKeys("\"" + candidateEmail + "\"");
			Thread.sleep(8000);
			element.click();
			Thread.sleep(5000);
			element.sendKeys(Keys.ENTER);
			Thread.sleep(10000);
			validate_User(driver, index);
			ReadCandidateFile(candidateEmail);
			validate_jobs(driver, userAvailable, intClear);

		}
		return index;
	}

	public static void driverLaunch(WebDriver driver) throws InterruptedException {

		driver.navigate().to("https://www.smartrecruiters.com/referrals-portal/PublicisGroupe/global/refer/manual");
		Thread.sleep(1000);
		driver.manage().window().maximize();
		if (driver
				.findElements(By
						.xpath("//*[@class='body']/div[2]/div[2]/div/div[2]/div/form/div/div/following-sibling::div[1]"))
				.size() > 0) {
			driver.findElement(
					By.xpath("//*[@class='body']/div[2]/div[2]/div/div[2]/div/form/div/div/following-sibling::div[1]"))
					.click();
			signIn(driver);
			driver.manage().window().maximize();
		} else {
			System.out.println("Search ID By next emailId:" + candidateEmail);
		}

	}

	static void signIn(WebDriver driver) throws InterruptedException {
		Thread.sleep(30000);

	}

	public static Boolean validate_User(WebDriver driver, int index) throws Exception {

		if (driver
				.findElement(By
						.xpath("//*[@class='apologiser-wrapper']/p[contains(text(),' Your search did not match any candidates.')]"))
				.isDisplayed()) {
			validate_phonenumber(driver, index);

		} else {
			record_available(driver);

		}
		return userAvailable;
	}

	static Boolean record_available(WebDriver driver) throws InterruptedException {
		System.out.println("Yeah... Record Available: " + userAvailable);
		userAvailable = true;
		Thread.sleep(2000);

		element_click(driver, "//*[@class='grid grid--gutter margin--top--s']/div/div/h3/a");
		try {
			element_click(driver, "//*[@class='other-applications__row ng-binding ng-scope']");
		} catch (Exception e) {
			System.out.println("Exception found ");
			userAvailable = false;

		}
		Thread.sleep(5000);

		System.out.println("Jobs available" + userAvailable);
		return userAvailable;

	}

	static Boolean validate_phonenumber(WebDriver driver, int index) throws InterruptedException, Exception {

		String Mobilevalue = mobilelist.get(index);
		driver.findElement(By.xpath("//*[@id='st-peopleLink']")).click();
		WebElement element = driver.findElement(By.id("st-people-searchInput"));
		System.out.println("mobile search");
		element.sendKeys("\"" + Mobilevalue + "\"");
		Thread.sleep(8000);
		element.click();
		Thread.sleep(5000);
		element.sendKeys(Keys.ENTER);
		Thread.sleep(10000);
		if (driver
				.findElement(By
						.xpath("//*[@class='apologiser-wrapper']/p[contains(text(),' Your search did not match any candidates.')]"))
				.isDisplayed()) {
			System.out.println("Mobile user record not found");
			userAvailable = false;
			System.out.println("useravailable" + userAvailable);
			writeOutputFile();
		} else {
			record_available(driver);
			System.out.println("Mobile record user found");
		}
		return userAvailable;
	}

	public static void element_click(WebDriver driver, String locator) {
		WebElement Ele = new WebDriverWait(driver, 20)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", Ele);
	}

	@SuppressWarnings("finally")
	public static void validate_jobs(WebDriver driver, Boolean userAvailable, Boolean intClear) throws Exception {
		String source = null;
		long Days = 0;
		int j = 0;
		System.out.println("useravailbe for jobs" + userAvailable);
		if (userAvailable == !false && intClear == true) {
			List<WebElement> jobLinks = driver
					.findElements(By.xpath("//*[@class='margin--bottom--xs']/div/div/div/a/span"));
			System.out.println("Size of the List:" + jobLinks.size());
			for (int i = 0; i <= jobLinks.size(); i++) {
				j = i + 1;
				try {
					System.out.println(jobLinks.get(i).getText());
					System.out.println(
							"locatorLink " + "(//*[@class='margin--bottom--xs']/div/div/div/a/span)[" + j + "]");
					element_click(driver, "(//*[@class='margin--bottom--xs']/div/div/div/a/span)[" + j + "]");
				} catch (StaleElementReferenceException ex) {
					System.out.println("Stale element exception caught");
					jobLinks = driver.findElements(By.xpath("//*[@class='margin--bottom--xs']/div/div/div/a/span"));
					System.out.println("jobLinks text link" + jobLinks.get(i).getText());
					element_click(driver, "//*[@class='other-applications__row ng-binding ng-scope']");
					element_click(driver, "(//*[@class='margin--bottom--xs']/div/div/div/a/span)[" + j + "]");
				} finally {
					if (driver.findElement(By.xpath("//*[@class='tabs-primary']/li[6]/a")).isDisplayed()) {
						System.out.println("Activity Link found");
					} else {
						System.out.println("Activity Link not clicked");
						element_click(driver, "(//*[@class='margin--bottom--xs']/div/div/div/a/span)[" + j + "]");
						;
					}
					element_click(driver, "//*[@class='tabs-primary']/li[6]/a");
					// driver.findElement(By.xpath("//*[@class='tabs-primary']/li[6]/a")).click();
					actualDate = driver.findElement(By.xpath("(//*[@class='ago']/span)[1]")).getText();
					System.out.println("dateshown" + actualDate);
					source = getReferral_text(driver);
					Days = compare_date(actualDate);
					String SourceName = compare_source(source, Days);
					System.out.println("SourceName:" + SourceName);
					if (SourceName == "InternalReferral" || SourceName == "ThirdParty") {
						driver.navigate().back();
						driver.navigate().back();
						driver.navigate().refresh();
						continue;

					} else {
						System.out.println(
								"Candidate doesnt fulfull any condition-Duplicate Candidate:  " + candidateEmail);
						writeOutputFile();
						break;
					}
				}

			}

		} else if (userAvailable == false || userAvailable == !false && intClear == false
				|| userAvailable == false && intClear == null) {
			writeOutputFile();
		}

	}

	public static void writeOutputFile() throws IOException {
		System.out.println("recorduserfound" + userAvailable + candidateEmail);
		System.out.println("intClear in outputfilemethod" + intClear);
		String file = "src/test/resources/excel/TestDatarecord.xlsx";
		FileInputStream inputStream = new FileInputStream(new File(file));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheet("Credentials");

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		int col_num = 0;
		Row firstRow = sheet.getRow(0);
		for (int j = 0; j < firstRow.getLastCellNum(); j++) {
			if (firstRow.getCell(j).getStringCellValue().trim().equals("Output")) {

				col_num = j;

			}

		}

		System.out.println("last cell" + firstRow.getCell(col_num).getStringCellValue());

		for (int i = 1; i <= rowCount; i++) {
			Row row = sheet.getRow(i);

			System.out.println("cellgetuser value" + row.getCell(0).getStringCellValue());

			if (userAvailable == !false && intClear == true
					&& row.getCell(0).getStringCellValue().trim().equals(candidateEmail)) {

				row.getCell(col_num).setCellValue("duplicate");
				System.out.println("cellduplicate");
				
			} else if (userAvailable == false && intClear == null
					&& row.getCell(0).getStringCellValue().trim().equals(candidateEmail))
					//|| userAvailable == true && intClear == null
							// && row.getCell(0).getStringCellValue().trim().equals(candidateEmail))
					{
				System.out.println("last cell done" + row.getCell(col_num).getStringCellValue());
				row.getCell(col_num).setCellValue("RecordNotfound");
				
			} else if (userAvailable == false && intClear == false
					&& row.getCell(0).getStringCellValue().trim().equals(candidateEmail)) {
				System.out.println("last cell done 222");
				row.getCell(col_num).setCellValue("DuplicateduetoFailedInterview");
				
			}
		
			
		}
		FileOutputStream fileOut = new FileOutputStream(new File(file));
		workbook.write(fileOut);
		fileOut.close();
		inputStream.close();
		System.out.println("Output printed in the file has been generated successfully.");
		

	}

	static String compare_source(String source, long Days) throws Exception {
		File file = new File(
				"src/test/resources/excel/SourceNames.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		String SourceName = null;
		Boolean flag = false;
		while ((st = br.readLine()) != null) {
			// if (st.equalsIgnoreCase(source))
			if (st.contains(source))
				flag = false;
			else {
				flag = !flag;

			}
		}

		if ((Days <= 31) && !flag) {
			System.out.println("condition for source fulfilled" + !flag);
			SourceName = "InternalReferral";
		} else if ((Days <= 3) && flag) {
			System.out.println("condition for ThirdParty fulfilled" + flag);
			SourceName = "ThirdParty";
		}

		return SourceName;
	}

	static String getReferral_text(WebDriver driver) {
		String source = null;
		String[] sourceele = null;
		String text = driver.findElement(By.xpath("//*[@class='candidateSource']/span")).getText();
		text = text.toLowerCase();
		if (text.contains("by ")) {
			sourceele = text.split("by ");
		} else if (text.contains("from")) {
			sourceele = text.split("from ");
		}
		source = sourceele[1];

		System.out.println("source: " + source);
		return source;

	}

	static long compare_date(String actualDate) throws ParseException {
		Date date = new Date();
		Date d2Actual = null;
		// actualDate = "May 7, 2021";

		SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");

		String currentDate = formatter.format(date);
		Date currentdate = formatter.parse(currentDate);
		System.out.print("Current date: " + currentDate);
		// String d2 = formatter.format("May 7, 2021");

		try {
			if (actualDate.contains("hour") || actualDate.contains("minutes") || actualDate.contains("seconds")) {
				System.out.print("Actual date: " + actualDate);
				throw new ParseException("date is current date exception", 0);

			} else if (actualDate == null) {
				System.out.print("date is empty: " + actualDate);
			} else {
				d2Actual = formatter.parse(actualDate);
			}
		} catch (ParseException e) {
			d2Actual = formatter.parse(currentDate);
			System.out.print("d2Actualafterexception" + d2Actual);
		}

		System.out.print("d2finally taken: " + d2Actual);
		long Days = currentdate.getTime() - d2Actual.getTime();
		Days = TimeUnit.DAYS.convert(Days, TimeUnit.MILLISECONDS);
		System.out.println("Days: " + TimeUnit.DAYS.convert(Days, TimeUnit.MILLISECONDS));
		if (Days > 3) {
			System.out.println("Referral is not active for last 3 days: " + Days);
		} else {
			System.out.println("Referral is active for last 3 days " + Days);
		}

		return Days;

	}

}
