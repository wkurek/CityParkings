package util;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbHelper {
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String PROPERTIES_FILE_PATH = "local.properties";
    private static final String CONNECTION_STRING_PROPERTY_KEY = "connectionString";

    private static Connection connection;

    private static String getConnectionString() throws IOException {
        InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH);

        Properties properties = new Properties();
        properties.load(inputStream);

        return properties.getProperty(CONNECTION_STRING_PROPERTY_KEY);
    }

    private static void connect() throws IOException, ClassNotFoundException, SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException exception) {
            System.err.println(exception.getMessage());
            throw exception;
        }

        String connectionString = getConnectionString();
        connection = DriverManager.getConnection(connectionString);
    }

    private static void disconnect() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static CachedRowSet executeQuery(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl cachedRowSet = null;

        try {
            connect();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }

                if(statement != null) {
                    statement.close();
                }

                disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cachedRowSet;
    }

    public static int executeUpdateQuery(String sql) {
        Statement statement = null;
        int affectedRows = 0;

        try {
            connect();

            statement = connection.createStatement();
            affectedRows = statement.executeUpdate(sql);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(statement != null) {
                    statement.close();
                }

                disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return affectedRows;
    }

}
