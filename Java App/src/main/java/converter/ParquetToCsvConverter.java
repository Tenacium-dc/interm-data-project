package converter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ColumnDescriptor;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;

public class ParquetToCsvConverter {

  public static String convertParquetToCsv(String inputParquetFile, String outputFolder)
      throws IOException {
    Configuration conf = new Configuration();
    Path file = new Path(inputParquetFile);
    MessageType parquetSchema;

    try (ParquetFileReader reader = ParquetFileReader.open(conf, file)) {
      parquetSchema = reader.getFooter().getFileMetaData().getSchema();
    }

    ParquetReader<Group> reader =
        ParquetReader.builder(new GroupReadSupport(), file).withConf(conf).build();

    List<String> csvLines = new ArrayList<>();
    Group group;
    while ((group = reader.read()) != null) {
      csvLines.add(groupToCsv(group, parquetSchema));
    }

    reader.close();

    if (!csvLines.isEmpty()) {
      String outputCsvFile = generateCsvFileName(outputFolder);
      writeCsvFile(outputCsvFile, csvLines);
      return outputCsvFile;
    }

    return null;
  }

  private static String groupToCsv(Group group, MessageType schema) {
    StringBuilder csvLine = new StringBuilder();

    List<ColumnDescriptor> columns = schema.getColumns();
    for (ColumnDescriptor column : columns) {
      String columnName = column.getPath()[0];
      PrimitiveType.PrimitiveTypeName columnType = column.getPrimitiveType().getPrimitiveTypeName();
      String columnValue = getValueAsString(group, columnName, columnType);
      csvLine.append(columnValue).append(",");
    }

    // Remove the trailing comma
    csvLine.setLength(csvLine.length() - 1);

    return csvLine.toString();
  }

  private static String getValueAsString(Group group, String columnName,
      PrimitiveType.PrimitiveTypeName columnType) {
    String columnValue;
    int repetitionCount = group.getFieldRepetitionCount(columnName);
    if (repetitionCount > 0) {
      switch (columnType) {
        case INT32:
          columnValue = Integer.toString(group.getInteger(columnName, 0));
          break;
        case INT64:
          columnValue = Long.toString(group.getLong(columnName, 0));
          break;
        case FLOAT:
          columnValue = Float.toString(group.getFloat(columnName, 0));
          break;
        case DOUBLE:
          columnValue = Double.toString(group.getDouble(columnName, 0));
          break;
        case BOOLEAN:
          columnValue = Boolean.toString(group.getBoolean(columnName, 0));
          break;
        case BINARY:
        case FIXED_LEN_BYTE_ARRAY:
          columnValue = group.getBinary(columnName, 0).toStringUsingUTF8();
          break;
        default:
          columnValue = ""; // Handle other types as needed
          break;
      }
    } else {
      columnValue = ""; // Column is not set (null or missing)
    }
    return columnValue;
  }


  private static void writeCsvFile(String outputCsvFile, List<String> csvLines) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputCsvFile))) {
      for (String line : csvLines) {
        writer.write(line);
        writer.newLine();
      }
    }
  }

  private static String generateCsvFileName(String outputFolder) {
    return outputFolder + "/output_" + System.currentTimeMillis() + ".csv";
  }
}

