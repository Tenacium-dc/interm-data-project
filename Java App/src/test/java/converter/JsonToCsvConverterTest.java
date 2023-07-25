package converter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class JsonToCsvConverterTest {

  @Test
  public void testJsonToCsvConversion() {
    // Provide the path of the test JSON file and the output folder for CSV files.
    String jsonFilePath = "C:\\Users\\tejas\\Documents\\Json Files Store\\jsonTest.json";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    try {
      File jsonFile = new File(jsonFilePath);
      File csvFile = JsonToCsvConverter.convertJsonToCsv(jsonFile, outputFolder);

      // Verify that the CSV file was generated.
      assertTrue(csvFile.exists() && csvFile.isFile());

      System.out.println("JSON to CSV conversion test passed!");
    } catch (IOException e) {
      fail("Conversion failed with an exception: " + e.getMessage());
    }
  }
}
