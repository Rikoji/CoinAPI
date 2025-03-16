package de.hilsmann.coinAPI.MySQL;

import de.hilsmann.coinAPI.CoinMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
  public static Connection con;

  private static final boolean USE_ONLINE_DB = CoinMain.getInstance().getConfig().getBoolean("database.useOnlineDb");
  private static String MYSQL_URL;
  private static String MYSQL_USER;
  private static String MYSQL_PASSWORD;
  private static final String SQLITE_URL = "jdbc:sqlite:plugins/CoinAPI/database/stats.db";

  public static void connect() throws SQLException {
    if (USE_ONLINE_DB) {
      FileConfiguration config = CoinMain.getInstance().getConfig();

      String host = config.getString("database.host");
      int port = config.getInt("database.port");
      String dbName = config.getString("database.name");
      boolean useSSL = config.getBoolean("database.useSSL");

      MYSQL_URL = String.format(
              "jdbc:mysql://%s:%d/%s?useSSL=%b&autoReconnect=true&failOverReadOnly=false",
              host, port, dbName, useSSL
      );
      MYSQL_USER = config.getString("database.username");
      MYSQL_PASSWORD = config.getString("database.password");

      con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
      System.out.println("MySQL-Connection created.");

      createTable();
    } else {
      con = DriverManager.getConnection(SQLITE_URL);
      System.out.println("SQLite-Connection created.");
    }
  }

  public static void disconnect() {
    if (isConnected()) {
      try {
        con.close();
        System.out.println("MySQL Verbindung getrennt!");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean isConnected() {
    try {
      return con != null && !con.isClosed();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private static void createTable() {
    try (Statement st = con.createStatement()) {
      // Coins-Tabelle
      st.executeUpdate("CREATE TABLE IF NOT EXISTS coins (" +
              "UUID VARCHAR(100) PRIMARY KEY, " +
              "NAME VARCHAR(100), " +
              "coins INT(16), " +
              "gems INT(16), " +
              "crystals INT(16))");

      // Transactions-Tabelle mit erweiterter action-Spalte
      st.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
              "id INT AUTO_INCREMENT PRIMARY KEY, " +
              "playerUUID VARCHAR(100), " +
              "action VARCHAR(255), " +  // Erhöhte Länge
              "amount DOUBLE, " +
              "timestamp BIGINT, " +
              "receiverUUID VARCHAR(100), " +
              "senderUUID VARCHAR(100))");

      // Optional: Alter Table, falls die Spalte schon existiert
      st.executeUpdate("ALTER TABLE transactions MODIFY COLUMN action VARCHAR(255)");

      System.out.println("Tabelle(n) erfolgreich erstellt oder aktualisiert.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
