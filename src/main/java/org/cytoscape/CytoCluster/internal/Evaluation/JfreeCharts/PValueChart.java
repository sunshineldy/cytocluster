package org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;

import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.Cluster_Pvalue;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluationUtils;
import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PValueChart extends BasicEvaluation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 533308864563586223L;

	private HashMap<String, Set<String>> fun_pro_map = EvaluationUtils.Annotation_Protein_Map
			.get(EvaluationUtils.CURRENTANNOTATION);
	private HashMap<String, Set<String>> pro_fun_map = EvaluationUtils.Protein_Annotation_Map
			.get(EvaluationUtils.CURRENTANNOTATION);

	public PValueChart(String applicationTitle, List<ResultPanel> resultPanels,
			String figureA_X_Alias, String figureA_Y_Alias,
			String figureB_X_Alias, String figureB_Y_Alias) {
		super(applicationTitle, resultPanels, figureA_X_Alias, figureA_Y_Alias,
				figureB_X_Alias, figureB_Y_Alias);
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
			double density = 0.0;
			Cluster c = clusters.get(i);
			// System.out.println(c.getNetwork().getRootNetwork().getNodeCount());
			// System.out.println(c.getNetwork().getRootNetwork().getEdgeCount());
			int nodeNum = c.getNetwork().getNodeCount();
			int edgeNum = c.getNetwork().getEdgeCount();
			if (nodeNum > 0) {
				density = 2.0D * edgeNum / (nodeNum * (nodeNum - 1));
				String colKeys = i + "";
				dataset.addValue(density, rowKeys, colKeys);
				System.out.println(i + " density: " + density);
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
			int size = clusters.size();

			String rowKeys = resultPanel.getAlgname() + "-"
					+ resultPanel.getTitle();

			for (int j = 0; j < size; j++) {
				double density = 0.0;
				Cluster c = clusters.get(j);
				int nodeNum = c.getNetwork().getNodeCount();
				int edgeNum = c.getNetwork().getEdgeCount();
				if (nodeNum > 0) {
					density = 2.0D * edgeNum / (nodeNum * (nodeNum - 1));
					String colKeys = j + "";
					dataset.addValue(density, rowKeys, colKeys);
					// System.out.println(j + " density: " + density);
				}
			}
		}
		return dataset;
	}

	@Override
	public PieDataset createDatasetPieStatistics(ResultPanel resultPanel) {
		DefaultPieDataset result = new DefaultPieDataset();
		String[] columnkeys = { "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4",
				"0.4-0.5", "0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-9", "0.9-1" };
		int num[] = new int[columnkeys.length];
		List<Cluster> clusters = resultPanel.getClusters();
		for (int i = 0; i < clusters.size(); i++) {
			double density = 0.0;
			Cluster c = clusters.get(i);
			int nodeNum = c.getNetwork().getNodeCount();
			int edgeNum = c.getNetwork().getEdgeCount();
			if (nodeNum > 0) {
				density = 2.0D * edgeNum / (nodeNum * (nodeNum - 1));
			}
			if (density <= 0.1D)
				num[0]++;
			else if (density <= 0.2D)
				num[1]++;
			else if (density <= 0.3D)
				num[2]++;
			else if (density <= 0.4D)
				num[3]++;
			else if (density <= 0.5D)
				num[4]++;
			else if (density <= 0.6D)
				num[5]++;
			else if (density <= 0.7D)
				num[6]++;
			else if (density <= 0.8D)
				num[7]++;
			else if (density <= 0.9D)
				num[8]++;
			else if (density <= 1.0D) {
				num[9]++;
			}
		}
		for (int i = 0; i < columnkeys.length; i++) {
			result.setValue(columnkeys[i], num[i]);
		}
		return result;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(ResultPanel resultPanel) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		String[] columnkeys = { "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4",
				"0.4-0.5", "0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-9", "0.9-1" };
		int num[] = new int[columnkeys.length];
		List<Cluster> clusters = resultPanel.getClusters();
		String rowKeys = resultPanel.getAlgname() + "-"
				+ resultPanel.getTitle();
		for (int i = 0; i < clusters.size(); i++) {
			double density = 0.0;
			Cluster c = clusters.get(i);
			int nodeNum = c.getNetwork().getNodeCount();
			int edgeNum = c.getNetwork().getEdgeCount();
			if (nodeNum > 0) {
				density = 2.0D * edgeNum / (nodeNum * (nodeNum - 1));
			}
			if (density <= 0.1D)
				num[0]++;
			else if (density <= 0.2D)
				num[1]++;
			else if (density <= 0.3D)
				num[2]++;
			else if (density <= 0.4D)
				num[3]++;
			else if (density <= 0.5D)
				num[4]++;
			else if (density <= 0.6D)
				num[5]++;
			else if (density <= 0.7D)
				num[6]++;
			else if (density <= 0.8D)
				num[7]++;
			else if (density <= 0.9D)
				num[8]++;
			else if (density <= 1.0D) {
				num[9]++;
			}
		}
		for (int i = 0; i < columnkeys.length; i++) {
			// result.setValue(columnkeys[i], num[i]);
			result.addValue(num[i], rowKeys, columnkeys[i]);
		}
		return result;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(
			List<ResultPanel> resultPanels) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		String[] columnkeys = { "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4",
				"0.4-0.5", "0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-9", "0.9-1" };
		for (int i = 0; i < resultPanels.size(); i++) {
			ResultPanel resultPanel = resultPanels.get(i);
			int num[] = new int[columnkeys.length];
			List<Cluster> clusters = resultPanel.getClusters();
			String rowKeys = resultPanel.getAlgname() + "-"
					+ resultPanel.getTitle();
			for (int j = 0; j < clusters.size(); j++) {
				double density = 0.0;
				Cluster c = clusters.get(j);
				int nodeNum = c.getNetwork().getNodeCount();
				int edgeNum = c.getNetwork().getEdgeCount();
				if (nodeNum > 0) {
					density = 2.0D * edgeNum / (nodeNum * (nodeNum - 1));
				}
				if (density <= 0.1D)
					num[0]++;
				else if (density <= 0.2D)
					num[1]++;
				else if (density <= 0.3D)
					num[2]++;
				else if (density <= 0.4D)
					num[3]++;
				else if (density <= 0.5D)
					num[4]++;
				else if (density <= 0.6D)
					num[5]++;
				else if (density <= 0.7D)
					num[6]++;
				else if (density <= 0.8D)
					num[7]++;
				else if (density <= 0.9D)
					num[8]++;
				else if (density <= 1.0D) {
					num[9]++;
				}
			}
			for (int k = 0; k < columnkeys.length; k++) {
				// result.setValue(columnkeys[i], num[i]);
				result.addValue(num[k], rowKeys, columnkeys[k]);
			}
		}
		return result;
	}

	@Override
	public void createTableHashMap() {
		System.out.println("<create table> ");
		int size = EvaluationUtils.CurrentSelectedResults.size();
		String[] columnNames = { "Cluster ID", "Nodes Num", "TP", "FP", "FN",
				"P-Value", "FunctionCode", "Function", "Contain Functions Num" };
		for (int i = 0; i < size; i++) {
			ResultPanel resultPanel = EvaluationUtils.CurrentSelectedResults
					.get(i);
			
			List<Cluster_Pvalue> c_Ps = EvaluationUtils.getPValue_ClusterList(
					resultPanel, fun_pro_map, pro_fun_map,
					EvaluationUtils.GO_Annotation_Map);

			String data[][] = new String[c_Ps.size()][columnNames.length];

			for (int j = 0; j < c_Ps.size(); j++) {
				Cluster_Pvalue cp = c_Ps.get(j);
				Cluster c = cp.getCluster();
				int nodeNum = c.getNetwork().getNodeCount();
				data[j][0] = cp.getClusterID();
				data[j][1] = nodeNum + "";
				data[j][2] = cp.getTp() + "";
				data[j][3] = cp.getFp() + "";
				data[j][4] = cp.getFn() + "";
				data[j][5] = cp.getPvalue() + "";
				data[j][6] = cp.getFunctionCode() + "";
				data[j][7] = cp.getFunction() + "";
				data[j][8] = cp.getProbableFunctions() + "";

			}
			JTable table = new JTable(data, columnNames);
			EvaluationUtils.tableHashMap.put(resultPanel.getTitle(), table);
		}
		System.out.println("</create table> ");
	}
}
