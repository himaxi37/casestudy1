import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MarketWatchProcessor {

    // Class to store the instrument data for each row
    static class Instrument {
        String instrument;
        String typeOfInstrument;
        String underlying;
        double notionalTurnover;

        public Instrument(String instrument, String typeOfInstrument, String underlying, double notionalTurnover) {
            this.instrument = instrument;
            this.typeOfInstrument = typeOfInstrument;
            this.underlying = underlying;
            this.notionalTurnover = notionalTurnover;
        }
    }

    public static void processInstrumentData(String excelFilePath, String outputFilePath) throws IOException {
        // Open the Excel file
        FileInputStream file = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        // Map to group by Type of Instrument -> Underlying -> List of Instruments
        Map<String, Map<String, List<Instrument>>> groupedData = new HashMap<>();

        // Reading Excel rows and populating the groupedData map
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            String instrument = row.getCell(0).getStringCellValue();
            String typeOfInstrument = row.getCell(1).getStringCellValue();
            String underlying = row.getCell(2).getStringCellValue();
            double notionalTurnover = 0.0;
            try {
                notionalTurnover = row.getCell(3).getNumericCellValue();
            } catch (Exception e) {
                // Handle possible non-numeric turnover values gracefully
            }

            Instrument inst = new Instrument(instrument, typeOfInstrument, underlying, notionalTurnover);

            groupedData
                .computeIfAbsent(typeOfInstrument, k -> new HashMap<>())
                .computeIfAbsent(underlying, k -> new ArrayList<>())
                .add(inst);
        }

        workbook.close();
        file.close();

        // Create a new workbook to write the summary
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();
        XSSFSheet outputSheet = outputWorkbook.createSheet("HighestTurnoverSummary");

        // Add headers
        Row header = outputSheet.createRow(0);
        header.createCell(0).setCellValue("Type of Instrument");
        header.createCell(1).setCellValue("Underlying");
        header.createCell(2).setCellValue("Instrument with Highest Turnover");
        header.createCell(3).setCellValue("Notional Turnover");

        int rowNum = 1;

        // Process each group and write to Excel
        for (String typeOfInstrument : groupedData.keySet()) {
            Map<String, List<Instrument>> underlyingMap = groupedData.get(typeOfInstrument);

            for (String underlying : underlyingMap.keySet()) {
                List<Instrument> instruments = underlyingMap.get(underlying);

                Instrument highestTurnoverInstrument = null;

                for (Instrument inst : instruments) {
                    if (highestTurnoverInstrument == null || inst.notionalTurnover > highestTurnoverInstrument.notionalTurnover) {
                        highestTurnoverInstrument = inst;
                    }
                }

                if (highestTurnoverInstrument != null) {
                    // Write into output Excel
                    Row outputRow = outputSheet.createRow(rowNum++);
                    outputRow.createCell(0).setCellValue(typeOfInstrument);
                    outputRow.createCell(1).setCellValue(underlying);
                    outputRow.createCell(2).setCellValue(highestTurnoverInstrument.instrument);
                    outputRow.createCell(3).setCellValue(highestTurnoverInstrument.notionalTurnover);

                    // Also print to console
                    System.out.println("\nType of Instrument: " + typeOfInstrument);
                    System.out.println("  Underlying: " + underlying);
                    System.out.println("    Instrument with highest Notional Turnover: " + highestTurnoverInstrument.instrument);
                    System.out.println("    Notional Turnover: " + highestTurnoverInstrument.notionalTurnover);
                }
            }
        }

        // Write the output Excel file
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        outputWorkbook.write(outputStream);
        outputStream.close();
        outputWorkbook.close();

        System.out.println("\n✅ Summary Excel File Created: " + outputFilePath);
    }

    public static void main(String[] args) throws IOException {
        // Path to the Excel file containing the instrument data
        String excelFilePath = "MarketWatchData.xlsx";
        // Path where output should be written
        String outputFilePath = "HighestTurnoverSummary.xlsx";

        processInstrumentData(excelFilePath, outputFilePath);
    }
}

