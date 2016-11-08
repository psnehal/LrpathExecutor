package org.ncibi.lrpath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ncibi.lrpath.config.RServerConfiguration;

public class LRPathTestCase1
{
	private int[] geneids;
	private double[] direction;
	private double[] sigvals;

	@Test
	public void testLRPath()
	{

		URL file = getClass().getResource("/org/ncibi/resource/gene_file_human.txt");
		// URL file =
		// getClass().getResource("/org/ncibi/resource/Rat_Test_Data-Directional.txt");

		reader(file.getFile());

		LRPathRServer rserver = new LRPathRServer(RServerConfiguration.rserverAddress(), RServerConfiguration.rserverPort());
		LRPath lrpath = new LRPath(rserver);
		LRPathArguments data = new LRPathArguments();
		// direction = new double[0];

		data.setGeneids(geneids);
		data.setDirection(direction);
		data.setSigvals(sigvals);
		data.setSpecies("hsa");
		data.setMaxg(99999);
		data.setSigcutoff(0.05);
		data.setMing(5);
		data.setOddsmin(0.0010);
		data.setOddsmax(0.5);
		data.setDatabase("pFAM");

		// System.out.println(data);
		// System.out.println(data);

		List<LRPathResult> results = lrpath.runAnalysis(data);

		System.out.println("results  " + results.size());

		for (LRPathResult result : results)
		{
			System.out.println(result);
		}

	}

	private void reader(String file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			ArrayList<Integer> g = new ArrayList<Integer>();
			ArrayList<Double> s = new ArrayList<Double>();
			ArrayList<Double> d = new ArrayList<Double>();

			while ((line = reader.readLine()) != null)
			{
				// System.out.println(line);
				String[] data = line.split("\t");
				if (data[0] != null && data[1] != null && data[0].matches("\\d+") && data[1].matches("-?\\d+(.\\d+)?")
						&& data[2].matches("-?\\d+(.\\d+)?"))
				{
					g.add((Integer) Integer.parseInt(data[0]));
					s.add((Double) Double.parseDouble(data[1]));
					d.add((Double) Double.parseDouble(data[2]));
				}

			}

			geneids = new int[g.size()];
			sigvals = new double[s.size()];
			direction = new double[d.size()];

			for (int i = 0; i < g.size(); i++)
			{
				geneids[i] = (Integer) g.get(i);
				sigvals[i] = (Double) s.get(i);
				direction[i] = (Double) d.get(i);
			}

			System.out.println("NUmber of genes : " + geneids.length);

			reader.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

	}

}
