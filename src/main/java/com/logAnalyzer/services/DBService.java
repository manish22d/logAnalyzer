package com.logAnalyzer.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logAnalyzer.Constant.Param;
import com.logAnalyzer.model.Alert;
import com.logAnalyzer.utils.ConfigProvider;
import com.typesafe.config.Config;

/**
 * DB related operation
 * 
 * @author Manish
 *
 */
public class DBService {
	private static Logger log = LoggerFactory.getLogger(DBService.class);
	private static Connection connection;
	private static Config conf;
	private final static String sql = "INSERT INTO alert (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";

	public DBService() {
		conf = ConfigProvider.getConfig().getConfig(Param.DB_CONFIG);
	}

	public void startConnection() throws Exception {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			connection = DriverManager.getConnection(conf.getString(Param.DB_URL), conf.getString(Param.DB_USERNAME),
					conf.getString(Param.DB_PASSWORD));
			connection.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS alert (id VARCHAR(20), duration INTEGER, type VARCHAR(50), host VARCHAR(50), alert BOOLEAN)");
		} catch (Exception e) {
			log.error("Failure initialising HSQL JDBCDriver");
			throw new Exception("Connection Failed to create a Connection");
		}
	}

	public void saveAlert(Alert alert) {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, alert.getId());
			statement.setLong(2, alert.getDuration());
			statement.setString(3, alert.getType());
			statement.setString(4, alert.getHost());
			statement.setBoolean(5, alert.getAlert());
			statement.executeUpdate();
		} catch (Exception e) {
			log.error("Failure saving event, skipping", e);
		}
	}

	public void readAlertData() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("SELECT * FROM alert");
		log.info("size : ", result.getFetchSize());
		while (result.next()) {
			log.info(result.getString("id") + " | " + result.getString("duration") + " | " + result.getString("type")
					+ " | " + result.getString("host") + " | " + result.getString("alert"));
		}
	}

}
