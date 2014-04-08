package org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluationUtils;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.ImageName;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class BasicEvaluation extends JFrame implements EvaluationAlgorithm,
		CytoPanelComponent {

	private static final long serialVersionUID = 1L;
	protected String componentTitle = "";
	protected List<ResultPanel> resultPanels;
	JPanel panelA = new JPanel();
	JPanel panelB = new JPanel();
	JPanel tableJPanel = new JPanel();
	JButton saveTableButton = new JButton("Save Table");
	private final int width = 1000;
	private final int height = 550;

	private final String figureATitle = "FigureA - Evaluation Values";
	private final String figureBTitle = "FigureB - Values' Statistics";
	private String figureA_X_Alias = "";
	private String figureA_Y_Alias = "";
	private String figureB_X_Alias = "";
	private String figureB_Y_Alias = "";

	public BasicEvaluation(String applicationTitle,
			List<ResultPanel> resultPanels, String figureA_X_Alias,
			String figureA_Y_Alias, String figureB_X_Alias,
			String figureB_Y_Alias) {

		this.componentTitle = applicationTitle;
		this.resultPanels = resultPanels;

		this.figureA_X_Alias = figureA_X_Alias;
		this.figureA_Y_Alias = figureA_Y_Alias;
		this.figureB_X_Alias = figureB_X_Alias;
		this.figureB_Y_Alias = figureB_Y_Alias;

		init();
	}

	private void init() {
		// title
		JLabel titleLabel = new JLabel(this.componentTitle);
		JComboBox chooseResultComboBox = new JComboBox();
		chooseResultComboBox.addItem("All Results Comparison");
		for (int i = 0; i < this.resultPanels.size(); i++) {
			String string = resultPanels.get(i).getAlgname() + "-"
					+ resultPanels.get(i).getTitle();
			chooseResultComboBox.addItem(string);
			System.out.println("chooseResultComboBox: " + resultPanels.get(i));
		}

		chooseResultComboBox.setSelectedIndex(0);
		chooseResultComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cmd = ((JComboBox) e.getSource()).getSelectedItem()
						.toString();
				System.out.println("cmd : " + cmd);
				if ("All Results Comparison".equals(cmd)) {

					// remove all panels
					BasicEvaluation.this.panelA.removeAll();
					BasicEvaluation.this.panelB.removeAll();
					BasicEvaluation.this.tableJPanel.removeAll();

					// draw new charts FigureA and FigureB
					drawChartA(resultPanels);
					drawChartB(resultPanels);

					JTable table = new JTable(10, 5);
					JScrollPane scrollPane = new JScrollPane(table);
					scrollPane
							.setPreferredSize(BasicEvaluation.this.tableJPanel
									.getSize());
					BasicEvaluation.this.tableJPanel.add(scrollPane);
					BasicEvaluation.this.saveTableButton.setEnabled(false);

					// update the PanelUI
					BasicEvaluation.this.panelA.updateUI();
					BasicEvaluation.this.panelB.updateUI();
					BasicEvaluation.this.tableJPanel.updateUI();

				} else {
					for (int i = 0; i < resultPanels.size(); i++) {
						ResultPanel resultPanel = resultPanels.get(i);
						String string = resultPanels.get(i).getAlgname() + "-"
								+ resultPanels.get(i).getTitle();
						if (string.equals(cmd)) {
							EvaluationUtils.currentResultPanel = resultPanel
									.getTitle();
							// remove all panels
							BasicEvaluation.this.panelA.removeAll();
							BasicEvaluation.this.panelB.removeAll();
							BasicEvaluation.this.tableJPanel.removeAll();

							// draw new charts FigureA and FigureB
							drawChartA(resultPanel);
							drawChartB(resultPanel);
							BasicEvaluation.this.saveTableButton
									.setEnabled(true);

							JTable table = EvaluationUtils.tableHashMap
									.get(resultPanel.getTitle());
							System.out.println("table rowcount= "
									+ table.getRowCount());
							JScrollPane scrollPane = new JScrollPane(table);
							scrollPane
									.setPreferredSize(BasicEvaluation.this.tableJPanel
											.getSize());
							BasicEvaluation.this.tableJPanel.add(scrollPane);

							// update the PanelUI
							BasicEvaluation.this.panelA.updateUI();
							BasicEvaluation.this.panelB.updateUI();
							BasicEvaluation.this.tableJPanel.updateUI();
						}
					}
				}
			}
		});

		JPanel panelTitle = new JPanel(new BorderLayout());
		panelTitle.add(titleLabel, BorderLayout.WEST);
		panelTitle.add(chooseResultComboBox, BorderLayout.EAST);

		JPanel panel = new JPanel(new GridLayout(1, 2));
		panelA.setToolTipText(figureATitle);
		panelB.setToolTipText(figureBTitle);
		// panel.add(panelA);
		// panel.add(panelB);
		JScrollPane sPaneA = new JScrollPane(panelA);
		JScrollPane sPaneB = new JScrollPane(panelB);
		panel.add(sPaneA);
		panel.add(sPaneB);

		/**
		 * the south panel
		 */
		JPanel panelSouth = new JPanel(new BorderLayout());
		JPanel savePanel = new JPanel();
		savePanel.setPreferredSize(new Dimension(width / 9, 20));
		savePanel.add(saveTableButton);
		saveTableButton.setEnabled(false);
		saveTableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JTable table = EvaluationUtils.tableHashMap
						.get(EvaluationUtils.currentResultPanel);

				System.out.println("table.getRowCount()= "
						+ table.getRowCount());

				// export the selected table
				EvaluationUtils.exportTable(table);
			}
		});
		panelSouth.setPreferredSize(new Dimension(width - 50, height / 3));
		tableJPanel.setPreferredSize(new Dimension(width - 100, height / 3));
		panelSouth.add(tableJPanel, BorderLayout.CENTER);
		panelSouth.add(savePanel, BorderLayout.EAST);

		this.setLayout(new BorderLayout());
		this.add(panelTitle, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(panelSouth, BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(width, height));
		this.pack();
		this.setLocation(125, 50);
		this.setVisible(true);
	}

	@Override
	public void drawChartA(ResultPanel resultPanel) {
		// This will create the dataset
		CategoryDataset dataset = createDataset(resultPanel);
		// based on the dataset we create the chart
		JFreeChart chart = createLineChart(dataset, figureATitle);

		// CategoryPlot set the parameters
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setBaseShapesVisible(true); // series 点（即数据点）可见
		renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见
		renderer.setUseSeriesOffset(true); // 设置偏移量
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);

		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width / 2, height * 7 / 11));
		// chartPanel.setSize(new Dimension(width / 3, height / 2));
		panelA.add(chartPanel);
	}

	@Override
	public void drawChartA(List<ResultPanel> resultPanels) {
		// This will create the dataset
		CategoryDataset dataset = createDataset(resultPanels);
		// based on the dataset we create the chart
		JFreeChart chart = createLineChart(dataset, figureATitle);
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width / 2, height * 7 / 11));
		// chartPanel.setSize(new Dimension(width / 3, height / 2));z
		panelA.add(chartPanel);
	}

	@Override
	public void drawChartB(ResultPanel resultPanel) {
		// This will create the dataset
		CategoryDataset dataset = createDatasetBarStatistics(resultPanel);
		// based on the dataset we create the chart
		JFreeChart chart = create3DChart(dataset, figureBTitle);
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width / 2, height * 7 / 11));
		// chartPanel.setSize(new Dimension(width / 3, height / 2));
		panelB.add(chartPanel);
	}

	@Override
	public void drawChartB(List<ResultPanel> resultPanels) {
		// This will create the dataset
		CategoryDataset dataset = createDatasetBarStatistics(resultPanels);
		// based on the dataset we create the chart
		JFreeChart chart = create3DChart(dataset, figureBTitle);
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width / 2, height * 7 / 11));
		// chartPanel.setSize(new Dimension(width / 3, height / 2));
		panelB.add(chartPanel);
	}

	public void drawPieChartB(ResultPanel resultPanel) {
		// This will create the dataset
		PieDataset dataset = createDatasetPieStatistics(resultPanel);
		// based on the dataset we create the chart
		JFreeChart chart = create3DChart(dataset, figureBTitle);
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(width / 2, height * 7 / 11));
		// chartPanel.setSize(new Dimension(width / 3, height / 2));
		panelB.add(chartPanel);
	}

	/****************************** 【create DataSet】 ***************************/
	@Override
	public CategoryDataset createDataset(ResultPanel resultPanel) {
		return null;
	}

	@Override
	public CategoryDataset createDataset(List<ResultPanel> resultPanels) {
		return null;
	}

	@Override
	public PieDataset createDatasetPieStatistics(ResultPanel resultPanel) {
		return null;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(ResultPanel resultPanel) {
		return null;
	}

	@Override
	public CategoryDataset createDatasetBarStatistics(
			List<ResultPanel> resultPanels) {
		return null;
	}

	@Override
	public void createTableHashMap() {
	}

	@Override
	public JFreeChart createLineChart(CategoryDataset dataset, String title) {
		// ChartFactory.createLineChart
		JFreeChart jfreechart = ChartFactory.createLineChart(title,
				this.figureA_X_Alias, // categoryAxisLabel （category轴，横轴，X轴标签）
				this.figureA_Y_Alias, // valueAxisLabel（value轴，纵轴，Y轴的标签）
				dataset, // dataset
				PlotOrientation.VERTICAL, true, // legend
				false, // tooltips
				false); // URLs

		// CategoryPlot set the parameters
		CategoryPlot plot = jfreechart.getCategoryPlot();

		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setTickUnit(new NumberTickUnit(20.0));

		CategoryAxis categoryaxis = plot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		plot.setBackgroundAlpha(0.5f);
		plot.setForegroundAlpha(0.6f);

		return jfreechart;
	}

	@Override
	public JFreeChart create3DChart(CategoryDataset dataset, String title) {

		JFreeChart jfreechart = ChartFactory.createBarChart3D(title,
				this.figureB_X_Alias, this.figureB_Y_Alias, dataset,
				PlotOrientation.VERTICAL, true, true, false);

		CategoryPlot categoryPlot = (CategoryPlot) jfreechart.getPlot();
		categoryPlot.setDomainGridlinesVisible(true);
		CategoryAxis categoryAxis = categoryPlot.getDomainAxis();

		// 设置X轴显示的分类名称的显示位置，如果不设置则水平显示
		// 设置后，可以斜像显示。分类角度、图表空间有限时，建议采用
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(0.39269908169872414D));
		categoryAxis.setCategoryMargin(0.0D);

		// 获得显示对象
		BarRenderer3D barRenderer3d = (BarRenderer3D) categoryPlot
				.getRenderer();
		// 设置不显示边框线
		barRenderer3d.setDrawBarOutline(false);

		return jfreechart;
	}

	@Override
	public JFreeChart create3DChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, // data
				true, // include legend
				true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(120);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	@Override
	public Icon getIcon() {
		URL url = Resources.getUrl(ImageName.LOGO_SMALL);
		return new ImageIcon(url);
	}

	@Override
	public String getTitle() {
		return this.componentTitle;
	}

}
