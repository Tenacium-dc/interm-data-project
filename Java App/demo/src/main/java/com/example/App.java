package com.example;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.apache.arrow.vector.ipc.ArrowFileReader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.VectorUnloader;

import java.io.FileWriter;
import java.io.IOException;

public class ParquetToCsvConverter {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: ParquetToCsvConverter <input.parquet> <output.csv>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            convertParquetToCsv(inputFile, outputFile);
            System.out.println("Conversion completed successfully!");
        } catch (IOException e) {
            System.err.println("Error converting Parquet to CSV: " + e.getMessage());
        }
    }

    public static void convertParquetToCsv(String inputFile, String outputFile) throws IOException {
        try (ArrowFileReader fileReader = new ArrowFileReader(new RootAllocator(Long.MAX_VALUE), inputFile)) {
            VectorSchemaRoot root = fileReader.getVectorSchemaRoot();
            VectorUnloader unloader = new VectorUnloader(root);
            
            try (FileWriter writer = new FileWriter(outputFile)) {
                while (fileReader.loadNextBatch()) {
                    ArrowRecordBatch batch = fileReader.getRecordBatch();
                    unloader.getRecordBatchLoader(batch).load();
                    
                    int numRows = batch.getLength();
                    for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                        StringBuilder csvRow = new StringBuilder();
                        for (int columnIndex = 0; columnIndex < root.getFieldVectors().size(); columnIndex++) {
                            String value = root.getFieldVectors().get(columnIndex).getObject(rowIndex).toString();
                            csvRow.append(value).append(",");
                        }
                        csvRow.deleteCharAt(csvRow.length() - 1); // Remove the trailing comma
                        csvRow.append(System.lineSeparator());
                        writer.write(csvRow.toString());
                    }
                }
            }
        }
    }
}