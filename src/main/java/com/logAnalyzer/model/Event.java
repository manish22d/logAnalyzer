package com.logAnalyzer.model;

import lombok.Data;
/**
 * POJO Class serialization of Event JSON
 * @author Manish
 *
 */
@Data
public class Event {
	private String id;
	private String state;
	private String type;
	private String host;
	private long timestamp;
}
