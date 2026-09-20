package org.example.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ExcelUtils.java
 * Purpose: Handle Excel file operations using Apache POI
 * Used for reading test data from Excel files for DataProviders
 */
public class ExcelUtils {

    private static Workbook workbook;
    private static Sheet sheet;

    /**
     * Open Excel file
     *
     * @param filePath path to Excel file
     * @param sheetName name of the sheet to read
     */
    public static void openExcel(String filePath, String sheetName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get data from specific cell
     *
     * @param rowNum row number
     * @param colNum column number
     * @return cell value as string
     */
    public static String getCellData(int rowNum, int colNum) {
        try {
            Cell cell = sheet.getRow(rowNum).getCell(colNum);
            if (cell == null) {
                return "";
            }

            // Handle different cell types
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    return String.valueOf((int) cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                default:
                    return "";
            }
        } catch (Exception e) {
            System.out.println("Error reading cell data: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get row count from sheet
     *
     * @return number of rows in sheet
     */
    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * Get column count from sheet
     *
     * @return number of columns in sheet
     */
    public static int getColumnCount() {
        if (sheet.getRow(0) != null) {
            return sheet.getRow(0).getPhysicalNumberOfCells();
        }
        return 0;
    }

    /**
     * Get all data from sheet as Object[][]
     * Used for DataProviders in TestNG
     *
     * @param filePath path to Excel file
     * @param sheetName name of the sheet
     * @return 2D array of data
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        openExcel(filePath, sheetName);
        int rows = getRowCount();
        int cols = getColumnCount();

        Object[][] data = new Object[rows - 1][cols];

        // Skip first row (header) and read data
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i - 1][j] = getCellData(i, j);
            }
        }

        closeExcel();
        return data;
    }

    /**
     * Close Excel file
     */
    public static void closeExcel() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing Excel file: " + e.getMessage());
        }
    }
}

