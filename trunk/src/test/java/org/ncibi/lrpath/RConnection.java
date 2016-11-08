package org.ncibi.lrpath;

import java.io.IOException;

import org.ncibi.lrpath.LRPathRServer;
import org.ncibi.lrpath.config.RServerConfiguration;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RserveException;

public class RConnection
{
	

	public void testLRPath() throws REXPMismatchException, IOException
	{
		
		
		LRPathRServer rserver = new LRPathRServer(RServerConfiguration.rserverAddress(),
                RServerConfiguration.rserverPort());
		rserver.assignRVariable("check", "NULL");
		String r = rserver.evalRCommand("check").asString();
		rserver.voidEvalRCommand("library(chipenrich)");
		//voidEvalRCommand("library(chipenrich)");
		System.out.println(r);
		
		//ChipEnrichRServer rserver = new ChipEnrichRServer(RServerConfiguration.rserverAddress(), RServerConfiguration.rserverPort());
		
	
		
	}
		
		
        
        
		
		
	}

