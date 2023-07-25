package converter;

import java.io.File;

/**
 * A utility class to detect the file type based on the file extension.
 */
public class FileDetector {

  private FileDetector() {}

  /**
   * Detects the file type based on the file extension.
   *
   * @param filePath The path of the file.
   * @return The file type as a string, or "File type is not supported" if the file type is not
   *         recognised.
   */
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
    }
    return "File type is not supported";
  }
}
