package converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

public class JsonToCsvConverter {

  public static File convertJsonToCsv(File jsonFile, String outputFolder) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Map<?, ?>> data = objectMapper.readValue(jsonFile, ArrayList.class);

    // Create the output folder if it doesn't exist.
    File outputDir = new File(outputFolder);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    // Generate a unique filename for the CSV file using the JSON file's name.
    String jsonFileName = jsonFile.getName();
    String csvFileName = jsonFileName.replace(".json", ".csv");
    File csvFile = new File(outputFolder, csvFileName);

    try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile))) {
      if (!data.isEmpty()) {
        // Write the column headings based on the first row's keys.
        Map<?, ?> firstRow = data.get(0);
        String[] header = firstRow.keySet().toArray(new String[0]);
        csvWriter.writeNext(header);

        // Write each row's values as a new line in the CSV file.
        for (Map<?, ?> row : data) {
          List<String> values = new ArrayList<>();
          for (Object value : row.values()) {
            values.add(value.toString());
          }
          csvWriter.writeNext(values.toArray(new String[0]));
        }
      }
    }

    return csvFile;
  }
}
