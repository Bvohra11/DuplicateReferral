//package com.w2e.utilities;
//
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import com.w2e.java.duplicateReferral;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
// 
//public class ReadExcelData extends duplicateReferral 
//{
//    public static void ReadFile() throws Throwable
//    {
//        FileInputStream fis = new FileInputStream("C:/Users/bhavohra/Automation Flipkart/BDDframework/src/test/resources/excel/TestData.xlsx");
//        XSSFWorkbook workbook = new XSSFWorkbook(fis);
//        XSSFSheet sheet = workbook.getSheet("Credentials");
//        XSSFRow row = sheet.getRow(0);
// 
//        int col_num = -1;
// 
//        for(int i=0; i < row.getLastCellNum(); i++)
//        {
//            if(row.getCell(i).getStringCellValue().trim().equals("UserName"))
//                col_num = i;
//        }
// 
//        row = sheet.getRow(1);
//        XSSFCell cell = row.getCell(col_num);
// 
//        String value = cell.getStringCellValue();
//        System.out.println("Value of the Excel Cell is - "+ value);
// 
//    }
//}
