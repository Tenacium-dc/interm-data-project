package converter;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AvroToCsvConverterTest {

  @Test
  public void testAvroToCsvConversion() throws IOException {
    String avroFilePath = "C:\\Users\\tejas\\Documents\\AVRO Files Store\\avroTest.avro";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    AvroToCsvConverter.convertAvroToCsv(avroFilePath, outputFolder);

    int numberOfAvroFiles = 1; // Replace this with the number of Avro files you want to convert

    // Check if the correct number of CSV files were generated
    for (int i = 1; i <= numberOfAvroFiles; i++) {
      String csvFilePath = outputFolder + File.separator + "output_" + i + ".csv";
      File csvFile = new File(csvFilePath);
      Assertions.assertTrue(csvFile.exists());
      Assertions.assertTrue(csvFile.length() > 0);
    }
  }
}

