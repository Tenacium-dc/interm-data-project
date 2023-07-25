package converter;

import java.io.File;

public class FileDetector {

  public static String detectFileType(String filePath) {
    File file = new File(filePath);
    String fileName = file.getName().toLowerCase();

    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
      return "Excel";
    } else if (fileName.endsWith(".json")) {
      return "JSON";
    } else if (fileName.endsWith(".avro")) {
      return "Avro";
    } else if (fileName.endsWith(".parquet")) {
      return "Parquet";
    } else {
      return "File type is not supported";
    }
  }

}
