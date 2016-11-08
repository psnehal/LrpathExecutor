package org.ncibi.lrpath;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.Task;
import org.ncibi.lrpath.config.RServerConfiguration;
import org.ncibi.task.executor.lrpath.LRPathArgRetriever;

public class LRPathMouseTest {

	@Test
	public void testMouse() {
		LRPathRServer rserver = new LRPathRServer(RServerConfiguration.rserverAddress(), RServerConfiguration.rserverPort());
		final LRPath lrpath = new LRPath(rserver);
		
		
		PersistenceSession persistence = new PersistenceUnit(EntityManagers
	            .newEntityManagerFromProject("task"));;
		LRPathArgRetriever argRetriever = new LRPathArgRetriever(persistence);
		Task task = new Task();
		//task.setUuid("9160b786-6a88-4bf3-bfe6-79fb253abb63");
		
		task.setUuid("411dab2d-8896-4ac9-92c5-35ca40dbec87");
		System.out.println("retrieving args");
		LRPathArguments args = argRetriever.retrieveArgsForTask(task);
		System.out.println("running analysis");
        List<LRPathResult> results = lrpath.runAnalysis(args);
        System.out.println("Done");
	}

}
