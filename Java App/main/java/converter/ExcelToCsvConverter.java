package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A utility class to convert Excel files (XLS and XLSX) to CSV format.
 */
public class ExcelToCsvConverter {

  private ExcelToCsvConverter() {}

  private static final Logger LOGGER = Logger.getLogger(ExcelToCsvConverter.class.getName());

  /**
   * Converts an Excel file (XLS or XLSX) to a CSV file.
   *
   * @param excelFilePath The path to the input Excel file.
   * @param outputFolder The output folder to save the generated CSV file.
   * @return The path of the generated CSV file, or null if conversion fails.
   * @throws IOException If an I/O error occurs.
   */
  public static String convertExcelToCsv(String excelFilePath, String outputFolder)
      throws IOException {
    File excelFile = new File(excelFilePath);

    if (!excelFile.exists()) {
      logError(() -> "Excel file not found: " + excelFilePath);
      return null;
    }

    if (!excelFile.isFile()) {
      logError(() -> "Input path is not a file: " + excelFilePath);
      return null;
    }

    try (FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = getWorkbook(fis, excelFile.getName())) {

      String convertedCsvFilePath = null;
      int sheetCounter = 1; // Initialize the sheet counter

      for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        String csvFilePath = outputFolder + File.separator
            + excelFile.getName().replace(".xlsx", "") + "-Sheet" + sheetCounter++ + ".csv";

        try (FileWriter fw = new FileWriter(csvFilePath);
            BufferedWriter bw = new BufferedWriter(fw)) {

          DataFormatter dataFormatter = new DataFormatter();

          for (Row row : sheet) {
            StringBuilder csvRow = new StringBuilder();
            for (Cell cell : row) {
              String cellValue = dataFormatter.formatCellValue(cell);
              csvRow.append(cellValue).append(",");
            }
            // Check if csvRow is not empty before removing the trailing comma
            if (csvRow.length() > 0) {
              // Remove the trailing comma and add a newline character at the end of each row.
              bw.write(csvRow.substring(0, csvRow.length() - 1));
              bw.newLine();
            }
          }

          convertedCsvFilePath = csvFilePath; // Save the CSV file path after successful conversion
        }
      }

      return convertedCsvFilePath; // Return the output CSV file path if conversion is successful
    } catch (IOException e) {
      logError(() -> "Error converting Excel file: " + excelFile.getName(), e);
      return null; // Return null in case of any error during conversion
    }
  }

  /**
   * Gets the appropriate Workbook instance based on the Excel file format (XLS or XLSX).
   *
   * @param fis The FileInputStream of the Excel file.
   * @param fileName The name of the Excel file.
   * @return The Workbook instance corresponding to the Excel file format.
   * @throws IOException If an I/O error occurs.
   */
  private static Workbook getWorkbook(FileInputStream fis, String fileName) throws IOException {
    try {
      // Try to open the file as XSSFWorkbook (XLSX format)
      return new XSSFWorkbook(fis);
    } catch (OfficeXmlFileException e) {
      // If it's not XLSX format, try to open as HSSFWorkbook (XLS format)
      fis.getChannel().position(0); // Reset the input stream to the beginning
      return new HSSFWorkbook(fis);
    } catch (Exception e) {
      logError(() -> "Unsupported file format: " + fileName, e);
      throw new IllegalArgumentException("Unsupported file format: " + fileName, e);
    }
  }

  /**
   * Logs an error message using the LOGGER.
   *
   * @param messageSupplier The supplier to provide the error message.
   */
  private static void logError(Supplier<String> messageSupplier) {
    if (LOGGER.isLoggable(Level.SEVERE)) {
      LOGGER.severe(messageSupplier.get());
    }
  }

  /**
   * Logs an error message with the given exception using the LOGGER.
   *
   * @param messageSupplier The supplier to provide the error message.
   * @param exception The exception to be logged.
   */
  private static void logError(Supplier<String> messageSupplier, Throwable exception) {
    if (LOGGER.isLoggable(Level.SEVERE)) {
      LOGGER.log(Level.SEVERE, messageSupplier.get(), exception);
    }
  }
}
