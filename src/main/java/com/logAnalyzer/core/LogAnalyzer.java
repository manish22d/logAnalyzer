package com.logAnalyzer.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logAnalyzer.Constant.Param;
import com.logAnalyzer.model.Alert;
import com.logAnalyzer.model.Event;
import com.logAnalyzer.services.DBService;
import com.logAnalyzer.utils.ConfigProvider;
import com.logAnalyzer.utils.Helper;
import com.typesafe.config.Config;

/**
 * Core Class for analysis of logs
 * 
 * @author Manish
 *
 */
public class LogAnalyzer implements Callable<LogAnalyzer> {
	private static Logger log = LoggerFactory.getLogger(LogAnalyzer.class);
	List<Event> eventList = new ArrayList<Event>();
	Map<String, Event> eventMap = new HashMap<>();
	Map<String, Alert> alerts = new HashMap<>();
	File file;
	DBService db;
	Config conf;

	public LogAnalyzer(File file) {
		this.file = file;
		db = new DBService();
		conf = ConfigProvider.getConfig().getConfig(Param.APP_CONFIG);
	}

	@Override
	public LogAnalyzer call() throws IOException {

		readFile().processFile().saveAlerts();
		return this;
	}

	LogAnalyzer readFile() throws IOException {

		FileUtils.readLines(file, StandardCharsets.UTF_8).forEach(line -> {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				eventList.add(objectMapper.readValue(line, Event.class));
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.debug("There was issue in JSON Mapping" + line);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		log.info("All event was loaded into memory correctly from file : ", file.getName());
		return this;
	}

	LogAnalyzer processFile() {
		eventList.forEach(event -> {
			if (eventMap.containsKey(event.getId())) {
				Event e1 = eventMap.get(event.getId());
				long executionTime = Helper.getTimeDifference(event, e1);
				Alert alert = new Alert(event, Math.toIntExact(executionTime));
				if (executionTime > conf.getLong(Param.ALERT_THRESHOLD_TIME)) {
					alert.setAlert(Boolean.TRUE);
				}
				alerts.put(event.getId(), alert);
				eventMap.remove(event.getId());
			} else {
				eventMap.put(event.getId(), event);
			}
		});
		log.info("All Event was successfully processed from file ", file.getName());
		return this;
	}

	LogAnalyzer saveAlerts() {
		alerts.entrySet().forEach(set -> db.saveAlert(set.getValue()));
		log.info("All applicable alerts was save in db from file : ", file.getName());
		return this;
	}

}
