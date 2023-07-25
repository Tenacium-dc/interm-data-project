package converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class FileDetectorTest {

  @Test
  public void testDetectExcelFileType() {
    String filePath = "C:\\Users\\tejas\\Documents\\Excel Files Store\\javaTest.xlsx";
    String fileType = FileDetector.detectFileType(filePath);
    assertEquals("Excel", fileType);
  }

  @Test
  public void testDetectJSONFileType() {
    String filePath = "C:\\Users\\tejas\\Documents\\Json Files Store\\jsonTest.json";
    String fileType = FileDetector.detectFileType(filePath);
    assertEquals("JSON", fileType);
  }

  @Test
  public void testDetectAvroFileType() {
    String filePath = "C:\\Users\\tejas\\Documents\\AVRO Files Store\\avroTest.avro";
    String fileType = FileDetector.detectFileType(filePath);
    assertEquals("Avro", fileType);
  }

  @Test
  public void testDetectParquetFileType() {
    String filePath = "C:\\Users\\tejas\\Documents\\Parquet Files Store\\parquetTest.parquet";
    String fileType = FileDetector.detectFileType(filePath);
    assertEquals("Parquet", fileType);
  }

  @Test
  public void testDetectUnsupportedFileType() {
    String filePath = "C:\\Users\\tejas\\Documents\\Java App Slide Info.docx";
    String fileType = FileDetector.detectFileType(filePath);
    assertEquals("File type is not supported", fileType);
  }
}
