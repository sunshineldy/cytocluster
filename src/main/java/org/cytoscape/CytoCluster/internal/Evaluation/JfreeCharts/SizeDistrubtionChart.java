package org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;

import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluationUtils;
import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * draw the DensityChart
 * 
 * @author kayzhao
 */
public class SizeDistrubtionChart extends BasicEvaluation {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6283129454198925837L;

	public SizeDistrubtionChart(String applicationTitle,
			List<ResultPanel> resultPanels, String figureA_X_Alias,
			String figureA_Y_Alias, String figureB_X_Alias,
			String figureB_Y_Alias) {

		super(applicationTitle + " Comparison", resultPanels, figureA_X_Alias,
				figureA_Y_Alias, figureB_X_Alias, figureB_Y_Alias);
		createTableHashMap();
	}

	@Override
	public CategoryDataset createDataset(ResultPanel resultPanel) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Cluster> clusters = resultPanel.getClusters();
		int size = clusters.size();
		String rowKeys = resultPanel.getAlgname() + "-"
				+ resultPanel.getTitle();
		for (int i = 0; i < size; i++) {
			Cluster c = clusters.get(i);
			int nodeNum = c.getNetwork().getNodeCount();
			if (nodeNum > 0) {
				String colKeys = i + "";
				dataset.addValue(nodeNum, rowKeys, colKeys);
				System.out.println(i + " nodeNum: " + nodeNum);
			}
		}
		return dataset;
	}

	@Override
	public CategoryDataset createDataset(List<ResultPanel> resultPanels) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < resultPanels.size(); i++) {

			ResultPanel resultPanel = resultPanels.get(i);
			List<Cluster> clusters = resultPanel.getClusters();
			String rowKeys = resultPanel.getAlgname() + "-"
					+ resultPanel.getTitle();
			int size = clusters.size();
			for (int j = 0; j < size; j++) {
				Cluster c = clusters.get(j);
				int nodeNum = c.getNetwork().getNodeCount();
				if (nodeNum > 0) {
					String colKeys = j + "";
					dataset.addValue(nodeNum, rowKeys, colKeys);
					System.out.println(j + " nodeNum: " + nodeNum);
				}
			}
		}
		return dataset;
	}

	@Override
	public PieDataset createDatasetPieStatistics(ResultPanel resultPanel) {
		DefaultPieDataset result = new DefaultPieDataset();
		HashMap<Integer, Integer> hsMap = new HashMap<Integer, Integer>();
		List<Cluster> clusters = resultPanel.getClusters();
		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			int nodeNum = c.getNetwork().getNodeCount();
			if (hsMap.containsKey(nodeNum)) {
				int num = hsMap.get(nodeNum);
				hsMap.put(nodeNum, (++num));
			} else {
				hsMap.put(nodeNum, 1);
			}
		}

		Object[] keys = hsMap.keySet().toArray();
		Arrays.sort(keys);
		for (Object key : keys) {
			Integer k = (Integer) key;
			result.setValue(k, hsMap.get(k));
		}
		return result;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(ResultPanel resultPanel) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		HashMap<Integer, Integer> hsMap = new HashMap<Integer, Integer>();
		List<Cluster> clusters = resultPanel.getClusters();
		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			int nodeNum = c.getNetwork().getNodeCount();
			if (hsMap.containsKey(nodeNum)) {
				int num = hsMap.get(nodeNum);
				hsMap.put(nodeNum, (++num));
			} else {
				hsMap.put(nodeNum, 1);
			}
		}
		Object[] keys = hsMap.keySet().toArray();
		Arrays.sort(keys);
		String rowKeys = resultPanel.getAlgname() + "-"
				+ resultPanel.getTitle();
		for (Object key : keys) {
			Integer k = (Integer) key;
			// result.setValue(key, hsMap.get(key));
			result.addValue(hsMap.get(k), rowKeys, k);
		}
		return result;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(
			List<ResultPanel> resultPanels) {

		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for (int i = 0; i < resultPanels.size(); i++) {
			HashMap<Integer, Integer> hsMap = new HashMap<Integer, Integer>();
			ResultPanel resultPanel = resultPanels.get(i);
			List<Cluster> clusters = resultPanel.getClusters();

			for (int j = 0; j < clusters.size(); j++) {
				Cluster c = clusters.get(j);
				int nodeNum = c.getNetwork().getNodeCount();
				if (hsMap.containsKey(nodeNum)) {
					int num = hsMap.get(nodeNum);
					hsMap.put(nodeNum, (++num));
				} else {
					hsMap.put(nodeNum, 1);
				}
			}

			Object[] keys = hsMap.keySet().toArray();
			Arrays.sort(keys);
			String rowKeys = resultPanel.getAlgname() + "-"
					+ resultPanel.getTitle();
			for (Object key : keys) {
				Integer k = (Integer) key;
				// result.setValue(key, hsMap.get(key));
				result.addValue(hsMap.get(k), rowKeys, k);
			}
		}
		return result;
	}

	@Override
	public void createTableHashMap() {
		System.out.println("<create table> ");

		// HashMap<String, JTable> hsMap = new HashMap<String, JTable>();
		int size = EvaluationUtils.CurrentSelectedResults.size();
		String[] columnNames = { "Cluster ID", "Node Num" };
		for (int i = 0; i < size; i++) {
			ResultPanel resultPanel = EvaluationUtils.CurrentSelectedResults
					.get(i);
			List<Cluster> clusters = resultPanel.getClusters();
			String data[][] = new String[clusters.size()][columnNames.length];
			System.out.println("clusters . size = " + clusters.size());
			for (int j = 0; j < clusters.size(); j++) {
				Cluster cluster = clusters.get(j);
				int nodeNum = cluster.getNetwork().getNodeCount();
				data[j][0] = "Cluster " + j;
				data[j][1] = nodeNum + "";
			}
			JTable table = new JTable(data, columnNames);
			EvaluationUtils.tableHashMap.put(resultPanel.getTitle(), table);
		}
		System.out.println("</ create table> ");
	}
}