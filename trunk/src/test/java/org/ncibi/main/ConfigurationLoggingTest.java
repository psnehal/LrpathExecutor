package org.ncibi.main;

import junit.framework.Assert;

import org.junit.Test;
import org.ncibi.commons.config.ProjectConfiguration;
import org.ncibi.main.Configuration;

public class ConfigurationLoggingTest {

	private String queueNamePropertyName = "queue.name";
	
	@Test
	public void congifurationLoggingTest(){
		
		String queueName = ProjectConfiguration.getProjectProperty(queueNamePropertyName);
		Assert.assertNotNull(queueName);
		
        Configuration c = new Configuration(false);
        c.logConfiguration();
	}
}
