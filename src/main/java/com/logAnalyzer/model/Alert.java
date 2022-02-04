package com.logAnalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
/**
 * POJO Class Alerts 
 * @author Manish
 *
 */
@Data
public class Alert {
	@JsonProperty("id")
	private String id;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("type")
	private String type;

	@JsonProperty("host")
	private String host;

	@JsonProperty("alert")
	private Boolean alert;

	public Alert() {
	}

	public Alert(Event event, int duration) {
		this.id = event.getId();
		this.duration = duration;
		this.type = event.getType();
		this.host = event.getHost();
		this.alert = Boolean.FALSE;
	}

}
