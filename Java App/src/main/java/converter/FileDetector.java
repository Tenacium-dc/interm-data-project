package converter;

import java.io.File;

/**
 * A utility class to detect the file type based on the file extension.
 */
public class FileDetector {

  // Private constructor to prevent instantiation, as this is a utility class.
  private FileDetector() {}

  /**
   * Detects the file type based on the file extension.
   *
   * @param filePath The path of the file.
   * @return The file type as a string, or "File type is not supported" if the file type is not
   *         recognised.
   */
  public static String detectFileType(String filePath) {
    // Create a File instance from the provided file path.
    File file = new File(filePath);
    // Get the file name from the File instance and convert it to lowercase for case-insensitive
    // comparisons.
    String fileName = file.getName().toLowerCase();

    // Check the file extension to determine the file type.
    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
      return "Excel";
    } else if (fileName.endsWith(".json")) {
      return "JSON";
    } else if (fileName.endsWith(".avro")) {
      return "Avro";
    } else if (fileName.endsWith(".parquet")) {
      return "Parquet";
    }

    // Return "File type is not supported" if the file type is not recognised.
    return "File type is not supported";
  }
}
