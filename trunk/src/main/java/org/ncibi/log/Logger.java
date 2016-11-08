package org.ncibi.log;

import org.ncibi.task.logger.ConsoleIdTaskLogger;
import org.ncibi.task.logger.TaskLogger;

public final class Logger
{
    private Logger() {}
    
    public static TaskLogger log = new ConsoleIdTaskLogger(System.getProperty("pid"));
}
