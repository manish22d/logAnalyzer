package com.logAnalyzer.utils;

import java.util.Objects;
import java.util.stream.Stream;

import com.logAnalyzer.model.Event;
import com.logAnalyzer.model.State;

/**
 * Helper Class contains static methods
 * @author Manish
 *
 */
public class Helper {

	/**
	 * Get Time Difference between two events
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static long getTimeDifference(Event e1, Event e2) {
		Event endEvent = Stream.of(e1, e2).filter(e -> State.FINISHED.getValue().equals(e.getState())).findFirst()
				.orElse(null);
		Event startEvent = Stream.of(e1, e2).filter(e -> State.STARTED.getValue().equals(e.getState())).findFirst()
				.orElse(null);

		return Objects.requireNonNull(endEvent).getTimestamp() - Objects.requireNonNull(startEvent).getTimestamp();

	}

}
