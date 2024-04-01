import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static DB instance = null;

    private Connection connection;
    private String url = "jdbc:postgresql://mel.db.elephantsql.com/xiuevihj";
    private String username = "xiuevihj";
    private String password = "cy9tD92X-T3RSiIzwxdDSxPsPjF0kf5Y";

    // Private constructor to prevent instantiation from outside
    private DB() throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    // Method to get the singleton instance
    public static synchronized DB getInstance() throws SQLException {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public int executeProcedural(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
