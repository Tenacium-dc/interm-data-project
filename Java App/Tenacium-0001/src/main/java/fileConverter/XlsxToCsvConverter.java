package fileConverter;

import org.apache.poi.ss.usermodel.*;

import java.io.*;

public class XlsxToCsvConverter {
  public void convert(File inputFile, File outputFile) throws IOException {
    // Load the XLSX file
    Workbook workbook;
    try (FileInputStream fis = new FileInputStream(inputFile)) {
      workbook = WorkbookFactory.create(fis);
    } catch (InvalidFormatException e) {
      throw new IOException("Invalid XLSX format.", e);
    }

    // Create a CSV writer
    try (Writer writer = new FileWriter(outputFile)) {
      for (Sheet sheet : workbook) {
        for (Row row : sheet) {
          StringBuilder csvLine = new StringBuilder();
          for (Cell cell : row) {
            String cellValue = getCellValueAsString(cell);
            csvLine.append(cellValue).append(",");
          }
          // Remove the trailing comma and write the CSV line
          writer.write(csvLine.substring(0, csvLine.length() - 1));
          writer.write(System.lineSeparator());
        }
      }
    }

    System.out.println("XLSX to CSV conversion completed successfully.");
  }

  private String getCellValueAsString(Cell cell) {
    if (cell == null) {
      return "";
    }

    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case NUMERIC:
        return String.valueOf(cell.getNumericCellValue());
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        // Handle formula cells if required
        return cell.getCellFormula();
      case BLANK:
        return "";
      default:
        return "";
    }
  }
}
