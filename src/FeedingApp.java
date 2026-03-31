import java.util.List;
import java.util.Scanner;

public class FeedingApp {
    public static void main(String[] args) {
        FeedingManager manager = new FeedingManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running) {
            System.out.println("\n--- Baby Feeding Tracker ---");
            System.out.println("1. Load Records From File");
            System.out.println("2. Add Record");
            System.out.println("3. View All Records");
            System.out.println("4. Remove Record");
            System.out.println("5. Update Record");
            System.out.println("6. Calculate Average Amount");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException var33) {
                System.out.println("Invalid menu option.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    if (manager.loadFromFile(filePath)) {
                        System.out.println("File loaded successfully.");
                    } else {
                        System.out.println("Invalid file.");
                    }
                    break;
                case 2:
                    System.out.print("Baby Name: ");
                    String babyName = scanner.nextLine();
                    System.out.print("Feeding Type: ");
                    String feedingType = scanner.nextLine();

                    double amount;
                    try {
                        System.out.print("Amount (oz): ");
                        amount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException var32) {
                        System.out.println("Invalid amount.");
                        break;
                    }

                    System.out.print("Feeding Time: ");
                    String feedingTime = scanner.nextLine();

                    int duration;
                    try {
                        System.out.print("Duration (minutes): ");
                        duration = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException var31) {
                        System.out.println("Invalid duration.");
                        break;
                    }

                    System.out.print("Notes: ");
                    String notes = scanner.nextLine();
                    FeedingRecord record = new FeedingRecord(babyName, feedingType, amount, feedingTime, duration, notes);
                    manager.addRecord(record);
                    System.out.println("Record added.");
                    break;
                case 3:
                    List<FeedingRecord> records = manager.getAllRecords();
                    if (records.isEmpty()) {
                        System.out.println("No records found.");
                    } else {
                        for(int i = 0; i < records.size(); ++i) {
                            FeedingRecord r = (FeedingRecord)records.get(i);
                            System.out.println(i + ": " + r.getBabyName() + " | " + r.getFeedingType() + " | " + r.getAmountOz() + " oz | " + r.getFeedingTime() + " | " + r.getDurationMinutes() + " min | " + r.getNotes());
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter index to remove: ");

                    try {
                        int index = Integer.parseInt(scanner.nextLine());
                        if (manager.removeRecord(index)) {
                            System.out.println("Record removed.");
                        } else {
                            System.out.println("Invalid index.");
                        }
                    } catch (NumberFormatException var27) {
                        System.out.println("Invalid number.");
                    }
                    break;
                case 5:
                    System.out.print("Enter index to update: ");
                    int updateIndex;
                    try {
                        updateIndex = Integer.parseInt(scanner.nextLine());
                        List<FeedingRecord> currentRecords = manager.getAllRecords();

                        if (updateIndex < 0 || updateIndex >= currentRecords.size()) {
                            System.out.println("Invalid index.");
                            break;
                        }

                        FeedingRecord existing = currentRecords.get(updateIndex);
                        System.out.println("Updating record for: " + existing.getBabyName());
                        System.out.println("What would you like to update?");
                        System.out.println("1. Name\n2. Type\n3. Amount\n4. Time\n5. Duration\n6. Notes\n7. Cancel");

                        int updateChoice = Integer.parseInt(scanner.nextLine());

                        // Variables temporales con los datos actuales
                        String name = existing.getBabyName();
                        String type = existing.getFeedingType();
                        double amt = existing.getAmountOz();
                        String time = existing.getFeedingTime();
                        int dur = existing.getDurationMinutes();
                        String nts = existing.getNotes();

                        switch(updateChoice) {
                            case 1: System.out.print("New Name: "); name = scanner.nextLine(); break;
                            case 2: System.out.print("New Type: "); type = scanner.nextLine(); break;
                            case 3: System.out.print("New Amount (oz): "); amt = Double.parseDouble(scanner.nextLine()); break;
                            case 4: System.out.print("New Time: "); time = scanner.nextLine(); break;
                            case 5: System.out.print("New Duration (min): "); dur = Integer.parseInt(scanner.nextLine()); break;
                            case 6: System.out.print("New Notes: "); nts = scanner.nextLine(); break;
                            default: System.out.println("Update cancelled."); continue;
                        }

                        FeedingRecord updated = new FeedingRecord(name, type, amt, time, dur, nts);
                        manager.updateRecord(updateIndex, updated);
                        System.out.println("Record updated successfully!");

                    } catch (Exception e) {
                        System.out.println("Error during update: " + e.getMessage());
                    }
                    break;

                case 6:
                    double avg = manager.calculateAverageAmount();
                    System.out.println("Average Amount: " + avg + " oz");
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}
