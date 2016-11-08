package org.ncibi.main;

import org.ncibi.commons.config.ProjectConfiguration;
import org.ncibi.log.Logger;

final class Configuration
{
    private final boolean logFromRunningExecutor;
    
    public Configuration(boolean logFromRunningExecutor)
    {
        this.logFromRunningExecutor = logFromRunningExecutor;
    }
    
    public void logConfiguration()
    {
        logMessage("Configuration parameters for executor:");
        logProperty("task.db.url");
        logProperty("task.db.username");
        logProperty("status.dir");
        logProperty("queue.name");
    }
    
    private void logMessage(String message)
    {
        if (logFromRunningExecutor)
        {
            Logger.log.logMessage(message);
        }
        else
        {
            System.out.println(message);
        }
    }
    
    private void logProperty(String property)
    {
    	String value = ProjectConfiguration.getProjectProperty(property);
    	boolean overridden = false;
    	if (value != null)
    			overridden = ProjectConfiguration.isProjectPropertyOverridder(property);
        String message = "   " + property + ": " + ProjectConfiguration.getProjectProperty(property);
        if (overridden) {
        	message += " - overridden";
        } else {
        	message += " - not overridden";
        }
        logMessage(message);  
    }
}
