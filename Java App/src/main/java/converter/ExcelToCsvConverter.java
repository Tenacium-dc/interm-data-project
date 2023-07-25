package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToCsvConverter {
  public static void convertExcelToCsv(String excelFilePath, String outputFolder)
      throws IOException {
    try (FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis)) {

      for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        String csvFilePath = outputFolder + File.separator + sheet.getSheetName() + ".csv";

        try (FileOutputStream fos = new FileOutputStream(csvFilePath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {

          DataFormatter dataFormatter = new DataFormatter();

          for (Row row : sheet) {
            StringBuilder csvRow = new StringBuilder();
            for (Cell cell : row) {
              String cellValue = dataFormatter.formatCellValue(cell);
              csvRow.append(cellValue).append(",");
            }
            // Remove the trailing comma and add a newline character at the end of each row.
            bw.write(csvRow.substring(0, csvRow.length() - 1));
            bw.newLine();
          }
        }
      }
    }
  }
}
