package com.logAnalyzer;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Before
	public void init() {

	}

	@Test
	public void SuccessScenario() throws Exception {
		String[] args = { "src/main/resources" };
		App.main(args);
	}

	@Test(expected = InvalidParameterException.class)
	public void testRun_EmptyFilePath() {
		String[] args = {};
		App.main(args);
	}

	@Test
	public void testSingleFile() throws Exception {
		String[] args = { "src/main/resources/eventLogs/log1.txt" };
		App.main(args);
	}

}
