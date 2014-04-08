package org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts;

import java.util.List;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public interface EvaluationAlgorithm {

	/**
	 * draw the figure A from single resultPanel
	 */
	public void drawChartA(ResultPanel resultPanel);

	/**
	 * draw the figure A from resultPanels
	 */
	public void drawChartA(List<ResultPanel> resultPanels);

	/**
	 * draw the figure B from single resultPanel
	 */
	public void drawChartB(ResultPanel resultPanel);

	/**
	 * draw the figure B (pie chart) from single resultPanel
	 */
	public void drawPieChartB(ResultPanel resultPanel);

	/**
	 * draw the figure B from ResultPanels
	 */
	public void drawChartB(List<ResultPanel> resultPanels);

	/**
	 * [FigureA] create the dataset for line chart from Single ResultPanel
	 * 
	 * @return CategoryDataset
	 */
	public CategoryDataset createDataset(ResultPanel resultPanel);

	/**
	 * [FigureA] create the dataset for line chart from Single ResultPanel
	 * 
	 * @return CategoryDataset
	 */
	public CategoryDataset createDataset(List<ResultPanel> resultPanels);

	/**
	 * [FigureB] create the data statistics for 3D bar chart
	 */
	public CategoryDataset createDatasetBarStatistics(ResultPanel resultPanel);

	/**
	 * [FigureB]create the data statistics for 3D bar chart
	 */
	public CategoryDataset createDatasetBarStatistics(
			List<ResultPanel> resultPanels);

	/**
	 * [FigureB] create the data statistics for 3D pie chart
	 */
	public PieDataset createDatasetPieStatistics(ResultPanel resultPanel);

	/**
	 * [FigureA] create the line chart from ChartFactory
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	public JFreeChart createLineChart(CategoryDataset dataset, String title);

	/**
	 * [FigureB] create the 3D pie chart from ChartFactory
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	public JFreeChart create3DChart(PieDataset dataset, String title);

	/**
	 * [FigureB] create the 3D bar chart from ChartFactory
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	public JFreeChart create3DChart(CategoryDataset dataset, String title);

	/**
	 * 
	 * @return
	 */
	public void createTableHashMap();
}
