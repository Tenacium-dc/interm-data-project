package fileConverter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class JsonToCsvConverter {

    public void convertJsonToCsv(InputStream jsonInputStream, OutputStream csvOutputStream) {
        try {
            JsonFactory jsonFactory = new MappingJsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(jsonInputStream);

            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(csvOutputStream));

            boolean isFirstRow = true;

            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                // Read the JSON object into a Map
                Map<String, Object> jsonData = jsonParser.readValueAs(Map.class);

                // Write CSV headers if it's the first row
                if (isFirstRow) {
                    writeHeaders(csvWriter, jsonData);
                    isFirstRow = false;
                }

                // Write CSV data for the current row
                writeRow(csvWriter, jsonData);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeaders(BufferedWriter csvWriter, Map<String, Object> jsonData) throws IOException {
        Iterator<String> fieldNames = jsonData.keySet().iterator();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            csvWriter.write(fieldName);
            if (fieldNames.hasNext()) {
                csvWriter.write(",");
            }
        }
        csvWriter.newLine();
    }

    private void writeRow(BufferedWriter csvWriter, Map<String, Object> jsonData) throws IOException {
        Iterator<Object> values = jsonData.values().iterator();
        while (values.hasNext()) {
            Object value = values.next();
            csvWriter.write(String.valueOf(value));
            if (values.hasNext()) {
                csvWriter.write(",");
            }
        }
        csvWriter.newLine();
    }

    public static void main(String[] args) {
        // Example usage:
        JsonToCsvConverter converter = new JsonToCsvConverter();
        try {
            InputStream jsonInputStream = new FileInputStream("input.json");
            OutputStream csvOutputStream = new FileOutputStream("output.csv");
            converter.convertJsonToCsv(jsonInputStream, csvOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
