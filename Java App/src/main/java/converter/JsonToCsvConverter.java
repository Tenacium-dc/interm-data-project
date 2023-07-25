package converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

/**
 * A utility class to convert JSON data to CSV format.
 */
public class JsonToCsvConverter {

  private JsonToCsvConverter() {}

  /**
   * Converts JSON data to CSV format.
   *
   * @param jsonData The JSON data as a string.
   * @param outputFolder The output folder to save the generated CSV file.
   * @return The path of the generated CSV file as a String, or null if conversion fails.
   * @throws IOException If an I/O error occurs.
   */
  public static String convertJsonStringToCsv(String jsonData, String outputFolder)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<List<Map<String, Object>>> typeRef =
        new TypeReference<List<Map<String, Object>>>() {};
    List<Map<String, Object>> data = objectMapper.readValue(jsonData, typeRef);

    // Create the output folder if it doesn't exist.
    File outputDir = new File(outputFolder);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    // Generate the CSV file with a default name.
    String csvFileName = "output.csv";
    File csvFile = new File(outputFolder, csvFileName);

    try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile))) {
      if (!data.isEmpty()) {
        // Write the column headings based on the first row's keys.
        Map<String, Object> firstRow = data.get(0);
        String[] header = firstRow.keySet().toArray(new String[0]);
        csvWriter.writeNext(header);

        // Write each row's values as a new line in the CSV file.
        for (Map<String, Object> row : data) {
          List<String> values = new ArrayList<>();
          for (Object value : row.values()) {
            values.add(value.toString());
          }
          csvWriter.writeNext(values.toArray(new String[0]));
        }
      }
    }

    return csvFile.getAbsolutePath();
  }
}
