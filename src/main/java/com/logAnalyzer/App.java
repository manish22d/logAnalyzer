package com.logAnalyzer;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logAnalyzer.core.LogAnalyzer;
import com.logAnalyzer.services.DBService;

/**
 * driver class for log analyzer app
 * 
 * @author Manish
 *
 */
public class App {
	private static Logger log = LoggerFactory.getLogger(App.class);
	private static DBService db;

	public static void main(String[] args) {
		if (args.length == 0)
			throw new InvalidParameterException("Please provide a argument to continue");
		try {
			db = new DBService();
			db.startConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File dir = new File(args[0]);

		String[] extensions = new String[] { "txt" };

		try {
			log.info("Getting all .txt files in :", dir.getCanonicalPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<File> files = new ArrayList<>();

		if (dir.isDirectory())
			files.addAll((List<File>) FileUtils.listFiles(dir, extensions, true));
		else {
			files.add(FileUtils.getFile(dir));
		}
		
		if (files.size() == 0)
			throw new InvalidParameterException("Please provide a single log file filePath argument");
		ExecutorService executorService = Executors.newFixedThreadPool(files.size());
		Set<Callable<LogAnalyzer>> callables = new HashSet<>();

		for (File file : files) {
			callables.add(new LogAnalyzer(file));
		}
		try {
			executorService.invokeAll(callables);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		executorService.shutdown();

		// reading data from db
		try {
			db.readAlertData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
