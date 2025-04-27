import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class MarketWatchProcessor {

    // Class to store the instrument data for each row
    static class Instrument {
        String instrument;
        String underlying;
        double notionalTurnover;

        public Instrument(String instrument, String underlying, double notionalTurnover) {
            this.instrument = instrument;
            this.underlying = underlying;
            this.notionalTurnover = notionalTurnover;
        }
    }

    public static void processInstrumentData(String excelFilePath) throws IOException {
        // Open the Excel file
        FileInputStream file = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        // Map to group instruments by underlying
        Map<String, List<Instrument>> groupedData = new HashMap<>();

        // Reading Excel rows and populating the groupedData map
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            String instrument = row.getCell(0).getStringCellValue();
            String underlying = row.getCell(1).getStringCellValue();
            double notionalTurnover = row.getCell(2).getNumericCellValue();

            Instrument inst = new Instrument(instrument, underlying, notionalTurnover);

            // Group by underlying
            groupedData.computeIfAbsent(underlying, k -> new ArrayList<>()).add(inst);
        }

        // Process each group and find the highest Notional Turnover
        for (String underlying : groupedData.keySet()) {
            List<Instrument> instruments = groupedData.get(underlying);

            Instrument highestTurnoverInstrument = null;

            // Find the instrument with the highest Notional Turnover in this group
            for (Instrument inst : instruments) {
                if (highestTurnoverInstrument == null || inst.notionalTurnover > highestTurnoverInstrument.notionalTurnover) {
                    highestTurnoverInstrument = inst;
                }
            }

            // Print the result for this group
            if (highestTurnoverInstrument != null) {
                System.out.println("For Underlying: " + underlying);
                System.out.println("Instrument with highest Notional Turnover: " + highestTurnoverInstrument.instrument);
                System.out.println("Notional Turnover: " + highestTurnoverInstrument.notionalTurnover);
            }
        }

        workbook.close();
        file.close();
    }

    public static void main(String[] args) throws IOException {
        // Path to the Excel file containing the instrument data
        String excelFilePath = "MarketWatchData.xlsx";
        processInstrumentData(excelFilePath);
    }
}
