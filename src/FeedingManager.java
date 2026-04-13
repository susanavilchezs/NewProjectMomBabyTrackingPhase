
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The engine of the application that manages database logic.
 * Handles SQL connections and CRUD operations for feeding records.
 */

public class FeedingManager {
    private String url;

    /**
     * Connects to the SQLite database file.
     * @param dbPath The absolute path to the .db file.
     * @return true if connection is successful, false otherwise.
     */

    public boolean connect(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                String sql = "CREATE TABLE IF NOT EXISTS feedings ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "babyName TEXT,"
                        + "feedingType TEXT,"
                        + "amountOz REAL,"
                        + "feedingTime TEXT,"
                        + "durationMinutes INTEGER,"
                        + "notes TEXT);";
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return false;
    }


    public void addRecord(FeedingRecord record) {
        String sql = "INSERT INTO feedings(babyName, feedingType, amountOz, feedingTime, durationMinutes, notes) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getBabyName());
            pstmt.setString(2, record.getFeedingType());
            pstmt.setDouble(3, record.getAmountOz());
            pstmt.setString(4, record.getFeedingTime());
            pstmt.setInt(5, record.getDurationMinutes());
            pstmt.setString(6, record.getNotes());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public List<FeedingRecord> getAllRecords() {
        List<FeedingRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM feedings";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new FeedingRecord(
                        rs.getString("babyName"),
                        rs.getString("feedingType"),
                        rs.getDouble("amountOz"),
                        rs.getString("feedingTime"),
                        rs.getInt("durationMinutes"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


    public void removeRecord(int idInTable) {

        String sql = "DELETE FROM feedings WHERE id = (SELECT id FROM feedings LIMIT 1 OFFSET ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInTable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void updateRecord(int idInTable, FeedingRecord record) {
        String sql = "UPDATE feedings SET babyName = ?, feedingType = ?, amountOz = ?, feedingTime = ?, durationMinutes = ?, notes = ? "
                + "WHERE id = (SELECT id FROM feedings LIMIT 1 OFFSET ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getBabyName());
            pstmt.setString(2, record.getFeedingType());
            pstmt.setDouble(3, record.getAmountOz());
            pstmt.setString(4, record.getFeedingTime());
            pstmt.setInt(5, record.getDurationMinutes());
            pstmt.setString(6, record.getNotes());
            pstmt.setInt(7, idInTable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public double calculateAverageAmount() {
        String sql = "SELECT AVG(amountOz) as average FROM feedings";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("average");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }
}