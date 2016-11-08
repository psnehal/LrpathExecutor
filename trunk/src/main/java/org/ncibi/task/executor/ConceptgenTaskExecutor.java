package org.ncibi.task.executor;

import java.util.Date;
import java.util.List;

import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.conceptgen.LRpathConcept;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.lrpath.LRPathResult;

public class ConceptgenTaskExecutor {

	private PersistenceSession persistence;
	private int conceptTypeId = 34;
	
	public ConceptgenTaskExecutor(List<LRPathResult> results, LRPathArguments args)
	{
		persistence = new PersistenceUnit(EntityManagers.newEntityManagerFromProject("conceptgen"));
		createConcept(args);
	}
	
	private void createConcept(LRPathArguments args)
	{
		LRpathConcept concept = new LRpathConcept();
		concept.setConceptName("");
		concept.setConceptTypeId(conceptTypeId);
		concept.setOwner("");
		concept.setElementSize(args.getGeneids().length);
		concept.setCreateDate(new Date());

		persistence.session().saveOrUpdate(concept);
	}

}
