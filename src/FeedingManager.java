import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FeedingManager {
    private List<FeedingRecord> records = new ArrayList();

    public boolean addRecord(FeedingRecord record) {
        return record == null ? false : this.records.add(record);
    }

    public boolean removeRecord(int index) {
        if (index >= 0 && index < this.records.size()) {
            this.records.remove(index);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateRecord(int index, FeedingRecord updatedRecord) {
        if (index >= 0 && index < this.records.size() && updatedRecord != null) {
            this.records.set(index, updatedRecord);
            return true;
        } else {
            return false;
        }
    }

    public List<FeedingRecord> getAllRecords() {
        return new ArrayList(this.records);
    }

    public boolean loadFromFile(String filePath) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            } else {
                try {
                    Scanner fileScanner = new Scanner(file);

                    while(fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            try {
                                String babyName = parts[0].trim();
                                String feedingType = parts[1].trim();
                                double amountOz = Double.parseDouble(parts[2].trim());
                                String feedingTime = parts[3].trim();
                                int durationMinutes = Integer.parseInt(parts[4].trim());
                                String notes = parts[5].trim();
                                FeedingRecord record = new FeedingRecord(babyName, feedingType, amountOz, feedingTime, durationMinutes, notes);
                                this.records.add(record);
                            } catch (NumberFormatException var14) {
                            }
                        }
                    }

                    fileScanner.close();
                    return true;
                } catch (FileNotFoundException var15) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public double calculateAverageAmount() {
        if (this.records.isEmpty()) {
            return (double)0.0F;
        } else {
            double total = (double)0.0F;

            for(FeedingRecord record : this.records) {
                total += record.getAmountOz();
            }

            return total / (double)this.records.size();
        }
    }
}
