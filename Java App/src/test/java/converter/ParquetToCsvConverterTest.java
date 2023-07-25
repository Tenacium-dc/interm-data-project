package converter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParquetToCsvConverterTest {

  @Test
  public void testConvertParquetToCsv() throws IOException {
    // Test input and output file paths
    String inputParquetFile =
        "C:\\Users\\tejas\\Documents\\Parquet Files Store\\parquetTest.parquet";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    // Perform the conversion
    String newCsvFile = ParquetToCsvConverter.convertParquetToCsv(inputParquetFile, outputFolder);

    // Verify that the new CSV file was created
    Assertions.assertNotNull(newCsvFile);

    // Check the content of the CSV file
    Path csvFilePath = Paths.get(newCsvFile);
    Assertions.assertTrue(Files.exists(csvFilePath));

    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
      // Read the first line of the CSV file and verify its content
      String csvLine = reader.readLine();
      Assertions.assertEquals(
          ",1,Amanda,Jordan,ajordan0@com.com,Female,1.197.201.2,6759521864920116,Indonesia,3/8/1971,49756.53,Internal Auditor,1E+02",
          csvLine);
    }

    // Clean up: Delete the generated CSV file
    Files.deleteIfExists(csvFilePath);
  }
}
