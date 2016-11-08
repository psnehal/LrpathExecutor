package org.ncibi.lrpath;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.ncibi.lrpath.config.RServerConfiguration;



public class LRPathTest
{
	
	public static void main(String[] args) 
	{
		LRPathTest lr = new LRPathTest();
		lr.testLRPath();
	}
	
	public void testLRPath()
	{
		/*
		LRPathRServer rserver = new LRPathRServer(RServerConfiguration.rserverAddress(), RServerConfiguration.rserverPort());
		LRPath lrpath = new LRPath(rserver);
		DataValidator validate = new DataValidator("/home/snehal/DataFiles/LRpath/HPVpos_vs_neg-SCCs-expression-Directional.txt","\t", false, "yes", "hsa", "geneId","no");			
		LRPathArguments data = new LRPathArguments();	
		LRPathArguments data1 = new LRPathArguments();	
		LRPathParser lr = new LRPathParser();
		data1 = lr.fileParser("/home/snehal/DataFiles/LRpath/HPVpos_vs_neg-SCCs-expression-Directional.txt","\t", false, true);
		data.setGeneids(data1.getGeneids());
		data.setDirection(validate.getDirection());
		data.setSigvals(validate.getSigvals());
		System.out.println("size of gene ds is :==" + data1.getGeneids() + "size from datavalid " + validate.getGenes() + "size from sigvals " + validate.getSigvals() );
		data.setSpecies("hsa");
		data.setMing(10);
		 data.setDatabase("GO");
	    //data.setDatabaseExternal(true);
		 data.setRnaseq("no");
		 data.setReadcount(new double[]{23,45,23});
		 data.setAvgread("yes");
		 
		 
		 data.setDatabase("KEGG Pathway");
		System.out.println(data);
		List<LRPathResult> results = lrpath.runAnalysis(data);
		for (LRPathResult result : results)
		{
			System.out.println(result);
		}*/
	}
}
