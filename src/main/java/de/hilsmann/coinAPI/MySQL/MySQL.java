package de.hilsmann.coinAPI.MySQL;

import de.hilsmann.coinAPI.CoinMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
  public static Connection con;

  private static final boolean USE_ONLINE_DB = CoinMain.instance.getConfig().getBoolean("database.useOnlineDb");;
  private static String MYSQL_URL;
  private static String MYSQL_USER;
  private static String MYSQL_PASSWORD;
  private static final String SQLITE_URL = "jdbc:sqlite:plugins/Retrostats/database/stats.db";

  private Connection connection;


  public static void connect() throws SQLException {
    if (USE_ONLINE_DB) {
      FileConfiguration config = CoinMain.instance.getConfig();

      String host = config.getString("database.host");
      int port = config.getInt("database.port");
      String dbName = config.getString("database.name");
      boolean useSSL = config.getBoolean("database.useSSL");
      MYSQL_URL = String.format("jdbc:mysql://%s:%d/%s?useSSL=%b", host, port, dbName, useSSL);
      MYSQL_USER = config.getString("database.username");
      MYSQL_PASSWORD = config.getString("database.password");

      con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
      System.out.println("MySQL-Connection created.");
    } else {
      con = DriverManager.getConnection(SQLITE_URL);
      System.out.println("SQLite-Connection created.");
    }
  }

  
  public static void disconnect() {
    if (isConnected())
      try {
        con.close();
        System.out.println("MySQL Verbindung getrennt!");
      } catch (SQLException e) {
        e.printStackTrace();
      }  
  }
  
  public static boolean isConnected() {
    return (con != null);
  }

  public static void createTable() {
    try {
      con.prepareStatement("CREATE TABLE IF NOT EXISTS coins (" +
              "UUID VARCHAR(100) PRIMARY KEY, " +  // UUID as PRIMARY KEY
              "NAME VARCHAR(100), " +
              "coins INT(16), " +
              "gems INT(16), " +
              "crystals INT(16))").executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
