import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

public class TransferData {
  // Replace these values with your own Google Sheet and MySQL details
  private static final String SPREADSHEET_ID = "your-spreadsheet-id";
  private static final String RANGE_NAME = "Sheet1!A:D";
  private static final String MYSQL_URL = "jdbc:mysql://localhost/your-database-name";
  private static final String MYSQL_USERNAME = "your-mysql-username";
  private static final String MYSQL_PASSWORD = "your-mysql-password";

  public static void main(String[] args) throws Exception {
    // Connect to the Google Sheet
    Sheets sheetsService = GoogleSheetsApi.getSheetsService();
    ValueRange response = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, RANGE_NAME)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
      return;
    }

    // Connect to the MySQL database
    Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);

    // Loop through the rows of the Google Sheet and insert them into the MySQL database
    for (List row : values) {
      String sql = "INSERT INTO sheet_data (column1, column2, column3, column4) VALUES (?, ?, ?, ?)";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, row.get(0).toString());
      statement.setString(2, row.get(1).toString());
      statement.setString(3, row.get(2).toString());
      statement.setString(4, row.get(3).toString());
      statement.executeUpdate();
    }

    conn.close();
  }
}
