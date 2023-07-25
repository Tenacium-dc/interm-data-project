package converter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ExcelToCsvConverterTest {

  @Test
  public void testExcelToCsvConversion() {
    // Provide the path of the test Excel file and the output folder for CSV files.
    String excelFilePath = "C:\\Users\\tejas\\Documents\\Excel Files Store\\javaTest.xlsx";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    try {
      ExcelToCsvConverter.convertExcelToCsv(excelFilePath, outputFolder);

      // Verify that the CSV files have been generated for each sheet.
      File outputDir = new File(outputFolder);
      assertTrue(outputDir.exists() && outputDir.isDirectory());

      // Verify that the CSV files were created with the correct names.
      String[] expectedCsvFiles = {"Sheet1.csv"};
      for (String expectedFile : expectedCsvFiles) {
        File csvFile = new File(outputFolder + File.separator + expectedFile);
        assertTrue(csvFile.exists() && csvFile.isFile());
      }

      System.out.println("Excel to CSV conversion test passed!");
    } catch (IOException e) {
      fail("Conversion failed with an exception: " + e.getMessage());
    }
  }
}
