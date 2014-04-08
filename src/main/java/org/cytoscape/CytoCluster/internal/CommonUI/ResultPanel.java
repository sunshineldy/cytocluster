package org.cytoscape.CytoCluster.internal.CommonUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.cytoscape.CytoCluster.internal.Analyze.DiscardResultAction;
import org.cytoscape.CytoCluster.internal.ClusterAnalysis.Algorithm.Algorithm;
import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.CytoCluster.internal.MyUtils.Loader;
import org.cytoscape.CytoCluster.internal.MyUtils.ParameterSet;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.SavePolicy;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clustered ResultPanel
 */
public class ResultPanel extends JPanel implements CytoPanelComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3697901871735432568L;
	private static final int graphPicSize = 80;
	private static final int defaultRowHeight = 88;
	private final int resultId;
	private Algorithm alg;
	private String algname;
	private final List<Cluster> clusters;
	private final CyNetwork network;
	private CyNetworkView networkView;
	private CollapsiblePanel explorePanel;
	private JPanel[] exploreContent;
	private JButton closeButton;
	private ParameterSet currentParamsCopy;
	private int enumerationSelection = 0;
	private MCODEClusterBrowserPanel clusterBrowserPanel;
	private final ClusterUtil mcodeUtil;
	private final DiscardResultAction discardResultAction;
	private static final Logger logger = LoggerFactory
			.getLogger(ResultPanel.class);

	public ResultPanel(List<Cluster> clusters, Algorithm alg,
			ClusterUtil mcodeUtil, CyNetwork network,
			CyNetworkView networkView, int resultId,
			DiscardResultAction discardResultAction) {
		setLayout(new BorderLayout());

		this.alg = alg;
		this.mcodeUtil = mcodeUtil;
		this.resultId = resultId;
		this.clusters = Collections.synchronizedList(clusters);
		this.network = network;

		this.networkView = networkView;
		this.discardResultAction = discardResultAction;
		this.currentParamsCopy = mcodeUtil.getCurrentParameters()
				.getResultParams(resultId);
		setAlgname(currentParamsCopy.getAlgorithm());

		this.clusterBrowserPanel = new MCODEClusterBrowserPanel();
		StringBuffer sb = new StringBuffer("Complex Browser( ");
		sb.append(clusters.size());
		sb.append(" in total )");
		this.clusterBrowserPanel.setBorder(BorderFactory.createTitledBorder(sb
				.toString()));

		JPanel sortPanel = new JPanel();
		sortPanel.setLayout(new FlowLayout());
		boolean set3 = getAlgname().equals(ParameterSet.MCODE);
		boolean set2 = (getAlgname().equals(ParameterSet.IPCA))
				|| (!this.currentParamsCopy.isWeakFAGEC())
				|| (!this.currentParamsCopy.isWeakHCPIN());

		boolean set1 = (!set2) && (!set3);
		JRadioButton way1 = new JRadioButton("Size", set2);
		JRadioButton way2 = new JRadioButton("Modularity", set1);
		JRadioButton way3 = new JRadioButton("Score", set3);

		way1.setActionCommand("size");
		way2.setActionCommand("modu");
		way3.setActionCommand("score");

		way1.addActionListener(new SortWayAction(
				this.clusterBrowserPanel.table,
				this.clusterBrowserPanel.browserModel));
		way2.addActionListener(new SortWayAction(
				this.clusterBrowserPanel.table,
				this.clusterBrowserPanel.browserModel));
		way3.addActionListener(new SortWayAction(
				this.clusterBrowserPanel.table,
				this.clusterBrowserPanel.browserModel));
		ButtonGroup ways = new ButtonGroup();
		ways.add(way1);
		if (!getAlgname().equals(ParameterSet.IPCA)) {
			ways.add(way2);
		}
		if (getAlgname().equals(ParameterSet.MCODE)) {
			ways.add(way3);
		}
		JLabel label = new JLabel("Sort Complexes by (descend):");
		sortPanel.add(label);
		sortPanel.add(way1);
		sortPanel.add(way2);
		sortPanel.add(way3);
		way3.setVisible(false);
		if (this.currentParamsCopy.getAlgorithm().equals(ParameterSet.MCODE))
			way3.setVisible(true);
		sortPanel.setToolTipText("Select a way to sort the complexes");
		add(sortPanel, "North");
		add(this.clusterBrowserPanel, "Center");
		add(createBottomPanel(), "South");
		setSize(getMinimumSize());
	}

	public Component getComponent() {
		return this;
	}

	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	public Icon getIcon() {
		URL iconURL = Resources.getUrl(Resources.ImageName.LOGO_SMALL);
		return new ImageIcon(iconURL);
	}

	// title
	public String getTitle() {
		return "Result " + getResultId();
	}

	public int getResultId() {
		return this.resultId;
	}

	public CyNetworkView getNetworkView() {
		return this.networkView;
	}

	public List<Cluster> getClusters() {
		return this.clusters;
	}

	public CyNetwork getNetwork() {
		return this.network;
	}

	public int getSelectedClusterRow() {
		return this.clusterBrowserPanel.getSelectedRow();
	}

	/**
	 * discard methods
	 * 
	 * @param requestUserConfirmation
	 *            confirm the discard request
	 * */
	public void discard(final boolean requestUserConfirmation) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				boolean oldRequestUserConfirmation = Boolean.valueOf(
						ResultPanel.this.discardResultAction.getValue(
								"requestUserConfirmation").toString())
						.booleanValue();

				ResultPanel.this.discardResultAction.putValue(
						"requestUserConfirmation",
						Boolean.valueOf(requestUserConfirmation));

				// doClick method
				ResultPanel.this.closeButton.doClick();

				ResultPanel.this.discardResultAction.putValue(
						"requestUserConfirmation",
						Boolean.valueOf(oldRequestUserConfirmation));
			}
		});
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.explorePanel = new CollapsiblePanel("Explore");
		this.explorePanel.setCollapsed(false);
		this.explorePanel.setVisible(false);

		JPanel buttonPanel = new JPanel();

		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ExportAction());
		exportButton.setToolTipText("Export result set to a text file");

		// assign the [discardResultAction]--Action to the CloseButton
		this.closeButton = new JButton(this.discardResultAction);
		this.discardResultAction.putValue("requestUserConfirmation",
				Boolean.valueOf(true));

		buttonPanel.add(exportButton);
		buttonPanel.add(this.closeButton);

		panel.add(this.explorePanel, "North");
		panel.add(buttonPanel, "South");
		return panel;
	}

	/**
	 * 
	 * @param selectedRow
	 *            the selected row Number
	 * @return Jpanel
	 */
	private JPanel createExploreContent(int selectedRow) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, 1));
		JPanel nodeAttributesPanel = new JPanel(new BorderLayout());
		nodeAttributesPanel.setBorder(BorderFactory
				.createTitledBorder("Node Attribute Enumerator"));

		Collection<CyColumn> nodeColumns = this.network.getDefaultNodeTable()
				.getColumns();
		String[] availableAttributes = new String[nodeColumns.size()];

		int i = 0;
		for (CyColumn column : nodeColumns) {
			availableAttributes[(i++)] = column.getName();
		}
		Arrays.sort(availableAttributes, String.CASE_INSENSITIVE_ORDER);

		String[] attributesList = new String[availableAttributes.length + 1];
		System.arraycopy(availableAttributes, 0, attributesList, 1,
				availableAttributes.length);
		attributesList[0] = "Please Select";

		JComboBox nodeAttributesComboBox = new JComboBox(attributesList);

		// sizeSlider.addChangeListener(new SizeAction(selectedRow,
		// nodeAttributesComboBox));

		MCODEResultsEnumeratorTableModel modelEnumerator = new MCODEResultsEnumeratorTableModel(
				new HashMap());

		JTable enumerationsTable = new JTable(modelEnumerator);

		JScrollPane tableScrollPane = new JScrollPane(enumerationsTable);
		tableScrollPane.getViewport().setBackground(Color.WHITE);
		enumerationsTable.setPreferredScrollableViewportSize(new Dimension(100,
				80));
		enumerationsTable.setGridColor(Color.LIGHT_GRAY);
		enumerationsTable.setFont(new Font(enumerationsTable.getFont()
				.getFontName(), 0, 11));
		enumerationsTable.setDefaultRenderer(StringBuffer.class,
				new JTextAreaRenderer(0));
		enumerationsTable.setFocusable(false);

		nodeAttributesComboBox.addActionListener(new enumerateAction(
				modelEnumerator, selectedRow));

		nodeAttributesPanel.add(nodeAttributesComboBox, "North");
		nodeAttributesPanel.add(tableScrollPane, "South");

		JPanel bottomExplorePanel = createBottomExplorePanel(selectedRow);

		// panel.add(sizePanel);
		panel.add(nodeAttributesPanel);
		panel.add(bottomExplorePanel);

		return panel;
	}

	/**
	 * 
	 * @param selectedRow
	 *            the selected Row Number
	 * @return Jpanel
	 */
	private JPanel createBottomExplorePanel(int selectedRow) {
		JPanel panel = new JPanel();
		JButton createChildButton = new JButton("Create Sub-Network");
		createChildButton.addActionListener(new CreateSubNetworkAction(this,
				selectedRow));
		panel.add(createChildButton);
		return panel;
	}

	public double setNodeAttributesAndGetMaxScore() {
		Iterator localIterator2;
		for (Iterator localIterator1 = this.network.getNodeList().iterator(); localIterator1
				.hasNext(); localIterator2.hasNext()) {
			CyNode n = (CyNode) localIterator1.next();
			Long rgi = n.getSUID();
			CyTable netNodeTbl = this.network.getDefaultNodeTable();

			localIterator2 = this.clusters.iterator();
			Cluster cluster = (Cluster) localIterator2.next();
			// if (cluster.getAlCluster().contains(rgi)) {
			if (cluster.getALNodes().contains(rgi)) {

				Set clusterNameSet = new LinkedHashSet();

				/*
				 * if (nodeRow.isSet("MCODE_Cluster")) {
				 * clusterNameSet.addAll(nodeRow.getList("MCODE_Cluster",
				 * String.class)); }
				 * clusterNameSet.add(cluster.getClusterName());
				 * nodeRow.set("MCODE_Cluster", new ArrayList(clusterNameSet));
				 * 
				 * if (cluster.getSeedNode() == rgi)
				 * nodeRow.set("MCODE_Node_Status", "Seed"); else {
				 * nodeRow.set("MCODE_Node_Status", "Clustered"); }
				 */
			}
		}

		return this.alg.getMaxScore(this.resultId);
	}

	private StringBuffer getClusterDetails(Cluster cluster) {
		StringBuffer details = new StringBuffer();

		details.append("Rank: ");
		details.append(String.valueOf(cluster.getRank() + 1));
		details.append("\n");

		details.append("Nodes: ");
		details.append(cluster.getNetwork().getNodeCount());

		details.append("  Edges: ");
		details.append(cluster.getNetwork().getEdgeCount());

		details.append("\n");
		if (getAlgname().compareTo(ParameterSet.MCODE) == 0) {
			details.append("Score: ");
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);
			details.append(nf.format(cluster.getClusterScore()) + "  ");
		}

		if (getAlgname().compareTo(ParameterSet.IPCA) != 0) {
			details.append("Modularity: ");
			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMaximumFractionDigits(3);
			details.append(nf2.format(cluster.getModularity()));

			/*
			 * if(algname.compareTo(ParameterSet.HCPIN)==0){
			 * details.append("Modularity2: "); NumberFormat nf3 =
			 * NumberFormat.getInstance(); nf3.setMaximumFractionDigits(3);
			 * details.append(nf3.format(cluster.getModularity2())); }
			 */

			details.append("\n");
			details.append("InDeg: ");
			details.append(cluster.getInDegree());
			details.append(" OutDeg: ");
			int outDegree = cluster.getTotalDegree() - 2
					* cluster.getInDegree();
			details.append(outDegree);
		}
		return details;
	}

	private ArrayList sortMap(Map map) {
		ArrayList outputList = null;
		int count = 0;
		Set set = null;
		Map.Entry[] entries = null;

		set = (Set) map.entrySet();
		Iterator iterator = set.iterator();
		entries = new Map.Entry[set.size()];
		while (iterator.hasNext()) {
			entries[count++] = (Map.Entry) iterator.next();
		}

		// Sort the entries with own comparator for the values:
		Arrays.sort(entries, new Comparator() {
			public int compareTo(Object o1, Object o2) {
				Map.Entry le = (Map.Entry) o1;
				Map.Entry re = (Map.Entry) o2;
				return ((Comparable) le.getValue()).compareTo((Comparable) re
						.getValue());
			}

			public int compare(Object o1, Object o2) {
				Map.Entry le = (Map.Entry) o1;
				Map.Entry re = (Map.Entry) o2;
				return ((Comparable) le.getValue()).compareTo((Comparable) re
						.getValue());
			}
		});
		outputList = new ArrayList();
		for (int i = 0; i < entries.length; i++) {
			outputList.add(entries[i]);
		}
		return outputList;
	}

	public void selectCluster(CyNetwork custerNetwork) {
		if (custerNetwork != null) {
			this.mcodeUtil.setSelected(custerNetwork.getNodeList(),
					this.network);
		} else {
			this.mcodeUtil.setSelected(new ArrayList(), this.network);
		}
	}

	/**
	 * private class CreateSubNetworkAction
	 * 
	 */
	private class CreateSubNetworkAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int selectedRow;
		ResultPanel trigger;

		CreateSubNetworkAction(ResultPanel trigger, int selectedRow) {
			this.selectedRow = selectedRow;
			this.trigger = trigger;
		}

		public void actionPerformed(ActionEvent evt) {

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);
			final Cluster cluster = (Cluster) ResultPanel.this.clusters
					.get(this.selectedRow);
			final CyNetwork clusterNetwork = cluster.getNetwork();
			final String title = this.trigger.getResultId() + ": "
					+ cluster.getClusterName() + " (Score: "
					+ nf.format(cluster.getClusterScore()) + ")";

			// begin SwingWorker
			SwingWorker worker = new SwingWorker() {

				protected CyNetworkView doInBackground() throws Exception {
					CySubNetwork newNetwork = ResultPanel.this.mcodeUtil
							.createSubNetwork(clusterNetwork,
									clusterNetwork.getNodeList(),
									SavePolicy.SESSION_FILE);
					newNetwork.getRow(newNetwork).set("name", title);

					VisualStyle vs = ResultPanel.this.mcodeUtil
							.getNetworkViewStyle(ResultPanel.this.networkView);
					CyNetworkView newNetworkView = ResultPanel.this.mcodeUtil
							.createNetworkView(newNetwork, vs);

					newNetworkView.setVisualProperty(
							BasicVisualLexicon.NETWORK_CENTER_X_LOCATION,
							Double.valueOf(0.0D));
					newNetworkView.setVisualProperty(
							BasicVisualLexicon.NETWORK_CENTER_Y_LOCATION,
							Double.valueOf(0.0D));

					ResultPanel.this.mcodeUtil
							.displayNetworkView(newNetworkView);

					boolean layoutNecessary = false;
					CyNetworkView clusterView = cluster.getView();

					for (View nv : newNetworkView.getNodeViews()) {
						CyNode node = (CyNode) nv.getModel();
						View cnv = clusterView != null ? clusterView
								.getNodeView(node) : null;

						if (cnv != null) {
							double x = ((Double) cnv
									.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION))
									.doubleValue();
							double y = ((Double) cnv
									.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION))
									.doubleValue();
							nv.setVisualProperty(
									BasicVisualLexicon.NODE_X_LOCATION,
									Double.valueOf(x));
							nv.setVisualProperty(
									BasicVisualLexicon.NODE_Y_LOCATION,
									Double.valueOf(y));
						} else {
							double w = ((Double) newNetworkView
									.getVisualProperty(BasicVisualLexicon.NETWORK_WIDTH))
									.doubleValue();
							double h = ((Double) newNetworkView
									.getVisualProperty(BasicVisualLexicon.NETWORK_HEIGHT))
									.doubleValue();

							nv.setVisualProperty(
									BasicVisualLexicon.NODE_X_LOCATION,
									Double.valueOf(w * Math.random()));

							nv.setVisualProperty(
									BasicVisualLexicon.NODE_Y_LOCATION, Double
											.valueOf((h + 100.0D)
													* Math.random()));

							layoutNecessary = true;
						}
					}

					if (layoutNecessary) {
						SpringEmbeddedLayouter lay = new SpringEmbeddedLayouter(
								newNetworkView);
						lay.doLayout(0.0D, 0.0D, 0.0D, null);
					}

					newNetworkView.fitContent();
					newNetworkView.updateView();

					return newNetworkView;
				}
			};// end SwingWorker
			worker.execute();
		}
	}

	private class ExportAction extends AbstractAction {
		private ExportAction() {

		}

		public void actionPerformed(ActionEvent e) {
			mcodeUtil.exportResults(alg, clusters, network);
		}
	}

	// Draw the results with threads
	private class GraphDrawer implements Runnable {

		private boolean drawGraph;
		private boolean placeHolderDrawn;
		private boolean drawPlaceHolder;
		Cluster cluster;
		SpringEmbeddedLayouter layouter;
		boolean layoutNecessary;
		boolean clusterSelected;
		private Thread t;
		private final Loader loader;

		GraphDrawer(Loader loader) {
			this.loader = loader;
			this.layouter = new SpringEmbeddedLayouter();
		}

		public void drawGraph(Cluster cluster, boolean layoutNecessary,
				boolean drawPlaceHolder) {
			this.cluster = cluster;
			this.layoutNecessary = layoutNecessary;

			this.drawGraph = (!drawPlaceHolder);
			this.drawPlaceHolder = drawPlaceHolder;
			this.clusterSelected = false;
			this.t = new Thread(this);
			this.t.start();
		}

		public void run() {
			try {
				if (!this.drawPlaceHolder) {
					this.loader.start();
				}

				Thread currentThread = Thread.currentThread();
				while (this.t == currentThread) {
					if ((this.drawGraph) && (!this.drawPlaceHolder)) {
						Image image = ResultPanel.this.mcodeUtil
								.convertClusterToImage(this.loader,
										this.cluster, 80, 80, this.layouter,
										this.layoutNecessary);

						if ((image != null) && (this.drawGraph)) {
							this.loader.setProgress(100, "Selecting Nodes");
							ResultPanel.this.selectCluster(this.cluster
									.getNetwork());
							this.clusterSelected = true;
							this.cluster.setImage(image);

							ResultPanel.this.clusterBrowserPanel.update(
									new ImageIcon(image),
									this.cluster.getRank());
							this.drawGraph = false;
						}

						this.placeHolderDrawn = false;
					} else if ((this.drawPlaceHolder)
							&& (!this.placeHolderDrawn)) {
						Image image = ResultPanel.this.mcodeUtil
								.getPlaceHolderImage(80, 80);
						this.cluster.setImage(image);

						ResultPanel.this.clusterBrowserPanel.update(
								new ImageIcon(image), this.cluster.getRank());

						ResultPanel.this.selectCluster(this.cluster
								.getNetwork());
						this.drawGraph = false;

						this.placeHolderDrawn = true;
					} else if ((!this.drawGraph) && (this.drawPlaceHolder)
							&& (!this.clusterSelected)) {
						ResultPanel.this.selectCluster(this.cluster
								.getNetwork());
						this.clusterSelected = true;
					}

					if (((!this.drawGraph) && (!this.drawPlaceHolder))
							|| (this.placeHolderDrawn)) {
						stop();
					}

					Thread.sleep(100L);
				}// end of while
			} catch (Exception e) {
				ResultPanel.logger
						.error("Error while drawing cluster image", e);
			}
		}

		void stop() {
			this.loader.stop();
			this.layouter.interruptDoLayout();
			ResultPanel.this.mcodeUtil.interruptLoading();
			this.drawGraph = false;
			this.t = null;
		}
	}

	private static class JTextAreaRenderer extends JTextArea implements
			TableCellRenderer {
		int minHeight;

		public JTextAreaRenderer(int minHeight) {
			setLineWrap(true);
			setWrapStyleWord(true);
			setEditable(false);
			setFont(new Font(getFont().getFontName(), 0, 11));
			this.minHeight = minHeight;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			StringBuffer sb = (StringBuffer) value;
			setText(sb.toString());

			if (isSelected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}

			int currentRowHeight = table.getRowHeight(row);
			int rowMargin = table.getRowMargin();
			setSize(table.getColumnModel().getColumn(column).getWidth(),
					currentRowHeight - 2 * rowMargin);
			int textAreaPreferredHeight = (int) getPreferredSize().getHeight();

			if (currentRowHeight != Math.max(textAreaPreferredHeight + 2
					* rowMargin, this.minHeight + 2 * rowMargin)) {
				table.setRowHeight(row, Math.max(textAreaPreferredHeight + 2
						* rowMargin, this.minHeight + 2 * rowMargin));
			}
			return this;
		}
	}

	private class MCODEClusterBrowserPanel extends JPanel {
		private final ResultPanel.MCODEClusterBrowserTableModel browserModel;
		private final JTable table;

		public MCODEClusterBrowserPanel() {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createTitledBorder("Cluster Browser"));

			this.browserModel = new ResultPanel.MCODEClusterBrowserTableModel();

			this.table = new JTable(this.browserModel);
			this.table.setSelectionMode(0);
			this.table.setDefaultRenderer(StringBuffer.class,
					new ResultPanel.JTextAreaRenderer(88));
			this.table.setIntercellSpacing(new Dimension(0, 4));
			this.table.setFocusable(false);

			ListSelectionModel rowSM = this.table.getSelectionModel();
			rowSM.addListSelectionListener(new ResultPanel.TableRowSelectionHandler());

			JScrollPane tableScrollPane = new JScrollPane(this.table);
			tableScrollPane.getViewport().setBackground(Color.WHITE);

			add(tableScrollPane, "Center");
		}

		public int getSelectedRow() {
			return this.table.getSelectedRow();
		}

		public void update(ImageIcon image, int row) {
			this.table.setValueAt(image, row, 0);
		}

		public void update(Cluster cluster, int row) {
			StringBuffer details = getClusterDetails(cluster);
			this.table.setValueAt(details, row, 1);
		}

		JTable getTable() {
			return this.table;
		}
	}

	private class MCODEClusterBrowserTableModel extends AbstractTableModel {
		private final String[] columnNames = { "Network", "Details" };
		private Object[][] data;

		public MCODEClusterBrowserTableModel() {
			listIt();
		}

		public void listIt() {
			ResultPanel.this.exploreContent = new JPanel[ResultPanel.this.clusters
					.size()];
			this.data = new Object[ResultPanel.this.clusters.size()][this.columnNames.length];

			for (int i = 0; i < ResultPanel.this.clusters.size(); i++) {
				Cluster c = (Cluster) ResultPanel.this.clusters.get(i);
				c.setRank(i);
				StringBuffer details = getClusterDetails(c);
				this.data[i][1] = new StringBuffer(details);

				Image image = c.getImage();
				this.data[i][0] = (image != null ? new ImageIcon(image)
						: new ImageIcon());
			}
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

		public int getColumnCount() {
			return this.columnNames.length;
		}

		public int getRowCount() {
			return this.data.length;
		}

		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		public void setValueAt(Object object, int row, int col) {
			this.data[row][col] = object;
			fireTableCellUpdated(row, col);
		}

		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}

	private class MCODEResultsEnumeratorTableModel extends AbstractTableModel {
		String[] columnNames = { "Value", "Occurrence" };
		Object[][] data = new Object[0][this.columnNames.length];

		public MCODEResultsEnumeratorTableModel(HashMap enumerations) {
			listIt(enumerations);
		}

		public void listIt(HashMap enumerations) {
			ArrayList enumerationsSorted = ResultPanel.this
					.sortMap(enumerations);

			Object[][] newData = new Object[enumerationsSorted.size()][this.columnNames.length];
			int c = enumerationsSorted.size() - 1;

			for (Iterator i = enumerationsSorted.iterator(); i.hasNext();) {
				Map.Entry mp = (Map.Entry) i.next();
				newData[c][0] = new StringBuffer(mp.getKey().toString());
				newData[c][1] = mp.getValue().toString();
				c--;
			}

			if (getRowCount() == newData.length) {
				this.data = new Object[newData.length][this.columnNames.length];
				System.arraycopy(newData, 0, this.data, 0, this.data.length);
				fireTableRowsUpdated(0, getRowCount());
			} else {
				this.data = new Object[newData.length][this.columnNames.length];
				System.arraycopy(newData, 0, this.data, 0, this.data.length);
				fireTableDataChanged();
			}
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

		public int getRowCount() {
			return this.data.length;
		}

		public int getColumnCount() {
			return this.columnNames.length;
		}

		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		public void setValueAt(Object object, int row, int col) {
			this.data[row][col] = object;
			fireTableCellUpdated(row, col);
		}

		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}

	private class SizeAction implements ChangeListener {
		private final ScheduledExecutorService scheduler = Executors
				.newScheduledThreadPool(3);
		private ScheduledFuture<?> futureLoader;
		private int selectedRow;
		private JComboBox nodeAttributesComboBox;
		private boolean drawPlaceHolder;
		private final ResultPanel.GraphDrawer drawer;
		private final Loader loader;

		SizeAction(int selectedRow, JComboBox nodeAttributesComboBox) {
			this.selectedRow = selectedRow;
			this.nodeAttributesComboBox = nodeAttributesComboBox;
			this.loader = new Loader(selectedRow,
					ResultPanel.this.clusterBrowserPanel.getTable(), 80, 80);
			this.drawer = new ResultPanel.GraphDrawer(this.loader);
		}

		public void stateChanged(ChangeEvent e) {

			System.out.println("change!!!!!!!!");
			JSlider source = (JSlider) e.getSource();
			final double nodeScoreCutoff = source.getValue() / 1000.0D;
			final int clusterRow = this.selectedRow;

			final Cluster oldCluster = (Cluster) ResultPanel.this.clusters
					.get(clusterRow);

			if ((this.futureLoader != null) && (!this.futureLoader.isDone())) {
				this.drawer.stop();
				this.futureLoader.cancel(false);

				if (!oldCluster.equals(ResultPanel.this.clusters
						.get(clusterRow))) {
					oldCluster.dispose();
				}
			}
			Runnable command = new Runnable() {
				public void run() {
					System.out.println("run!!!!!!!!");
					List<Long> oldALCluster = oldCluster.getAlCluster();

					Cluster newCluster = ResultPanel.this.alg
							.exploreCluster(oldCluster, nodeScoreCutoff,
									ResultPanel.this.network,
									ResultPanel.this.resultId);

					List<Long> newALCluster = newCluster.getAlCluster();

					ResultPanel.SizeAction.this.drawPlaceHolder = (newALCluster
							.size() > 300);

					if (!newALCluster.equals(oldALCluster)) {
						ResultPanel.this.clusters.set(clusterRow, newCluster);

						System.out.println("update!!!!!!!!");
						ResultPanel.this.clusterBrowserPanel.update(newCluster,
								clusterRow);

						ResultPanel.SizeAction.this.nodeAttributesComboBox
								.setSelectedIndex(ResultPanel.SizeAction.this.nodeAttributesComboBox
										.getSelectedIndex());

						boolean layoutNecessary = newALCluster.size() > oldALCluster
								.size();

						if (!newCluster.isDisposed()) {
							ResultPanel.SizeAction.this.drawer
									.drawGraph(
											newCluster,
											layoutNecessary,
											ResultPanel.SizeAction.this.drawPlaceHolder);
							oldCluster.dispose();
						}
					}
					ResultPanel.this.mcodeUtil
							.destroyUnusedNetworks(ResultPanel.this.network,
									ResultPanel.this.clusters);
				}
			};
			this.futureLoader = this.scheduler.schedule(command, 100L,
					TimeUnit.MILLISECONDS);
		}
	}

	private class TableRowSelectionHandler implements ListSelectionListener {
		private TableRowSelectionHandler() {
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;

			ListSelectionModel lsm = (ListSelectionModel) e.getSource();

			if (!lsm.isSelectionEmpty()) {
				int selectedRow = lsm.getMinSelectionIndex();
				Cluster c = (Cluster) ResultPanel.this.clusters
						.get(selectedRow);
				CyNetwork gpCluster = c.getNetwork();

				System.out.println(gpCluster.getNodeList().size()
						+ "size!!!!!!!!!");

				ResultPanel.this.selectCluster(gpCluster);

				if (ResultPanel.this.exploreContent[selectedRow] == null) {
					ResultPanel.this.exploreContent[selectedRow] = ResultPanel.this
							.createExploreContent(selectedRow);
				}

				if (ResultPanel.this.explorePanel.isVisible()) {
					ResultPanel.this.explorePanel.getContentPane().remove(0);
				}

				ResultPanel.this.explorePanel.getContentPane().add(
						ResultPanel.this.exploreContent[selectedRow], "Center");

				if (!ResultPanel.this.explorePanel.isVisible()) {
					ResultPanel.this.explorePanel.setVisible(true);
				}

				String title = "Explore: ";

				if (c.getClusterName() != null)
					title = title + c.getClusterName();
				else {
					title = title + "Cluster " + (selectedRow + 1);
				}

				ResultPanel.this.explorePanel.setTitleComponentText(title);
				ResultPanel.this.explorePanel.updateUI();

				JComboBox nodeAttributesComboBox = (JComboBox) ((JPanel) ResultPanel.this.exploreContent[selectedRow]
						.getComponent(0)).getComponent(0);

				nodeAttributesComboBox
						.setSelectedIndex(ResultPanel.this.enumerationSelection);
			}
		}
	}

	private class enumerateAction extends AbstractAction {
		int selectedRow;
		ResultPanel.MCODEResultsEnumeratorTableModel modelEnumerator;

		enumerateAction(
				ResultPanel.MCODEResultsEnumeratorTableModel modelEnumerator,
				int selectedRow) {
			this.selectedRow = selectedRow;
			this.modelEnumerator = modelEnumerator;
		}

		public void actionPerformed(ActionEvent e) {
			HashMap attributeEnumerations = new HashMap();

			String attributeName = (String) ((JComboBox) e.getSource())
					.getSelectedItem();
			int selectionIndex = ((JComboBox) e.getSource()).getSelectedIndex();

			if (!attributeName.equals("Please Select")) {
				CyNetwork net = ((Cluster) ResultPanel.this.clusters
						.get(this.selectedRow)).getNetwork();
				Object value;
				for (Iterator localIterator1 = net.getNodeList().iterator(); localIterator1
						.hasNext();) {
					CyNode node = (CyNode) localIterator1.next();

					ArrayList attributeValues = new ArrayList();
					CyRow row = net.getRow(node);
					Class type = row.getTable().getColumn(attributeName)
							.getType();

					if (Collection.class.isAssignableFrom(type)) {
						Collection valueList = (Collection) row.get(
								attributeName, type);

						if (valueList != null)
							for (Iterator localIterator2 = valueList.iterator(); localIterator2
									.hasNext();) {
								value = localIterator2.next();
								attributeValues.add(value);
							}
					} else {
						attributeValues.add(row.get(attributeName, type));
					}

					value = attributeValues.iterator();
					Object aviElement = ((Iterator) value).next();
					if (aviElement != null) {
						value = aviElement.toString();

						if (!attributeEnumerations.containsKey(value)) {
							attributeEnumerations
									.put(value, Integer.valueOf(1));
						} else {
							Integer enumeration = (Integer) attributeEnumerations
									.get(value);
							enumeration = Integer.valueOf(enumeration
									.intValue() + 1);
							attributeEnumerations.put(value, enumeration);
						}
					}
				}

			}

			this.modelEnumerator.listIt(attributeEnumerations);

			ResultPanel.this.enumerationSelection = selectionIndex;
		}
	}

	private class SortWayAction extends AbstractAction {
		JTable browserTable;
		ResultPanel.MCODEClusterBrowserTableModel modelBrowser;

		SortWayAction(JTable browserTable,
				ResultPanel.MCODEClusterBrowserTableModel modelBrowser) {
			this.browserTable = browserTable;
			this.modelBrowser = modelBrowser;
		}

		public void actionPerformed(ActionEvent e) {
			String way = e.getActionCommand();
			ResultPanel.this.switchPlace(way);

			if (ResultPanel.this.clusters != null) {
				this.modelBrowser.listIt();
				this.modelBrowser.fireTableDataChanged();
				ResultPanel.this.clusterBrowserPanel.updateUI();
				((JPanel) ResultPanel.this.clusterBrowserPanel.getParent())
						.updateUI();
			} else {
				System.err.println("list null");
			}
		}
	}

	private void switchPlace(String field) {
		for (int i = 0; i < this.clusters.size() - 1; i++) {
			int max = i;
			for (int j = i + 1; j < this.clusters.size(); j++) {
				if (field.equals("size")) {
					System.out.println("size");
					if (((Cluster) this.clusters.get(j)).getNetwork()
							.getNodeList().size() > ((Cluster) this.clusters
							.get(max)).getNetwork().getNodeList().size()) {
						max = j;
					}
				} else if (field.equals("modu")) {
					if (((Cluster) this.clusters.get(j)).getModularity() > ((Cluster) this.clusters
							.get(max)).getModularity())
						max = j;
				} else if (field.equals("score")) {
					System.out.println("score");
					if (((Cluster) this.clusters.get(j)).getClusterScore() > ((Cluster) this.clusters
							.get(max)).getClusterScore())
						max = j;
				} else {
					System.err.println("In switchPlace:Erro Parameter");
					return;
				}
			}

			Collections.swap(this.clusters, i, max);
		}
	}

	public void setAlgname(String algname) {
		this.algname = algname;
	}

	public String getAlgname() {
		return algname;
	}

	public Algorithm getAlg() {
		return alg;
	}

	public void setAlg(Algorithm alg) {
		this.alg = alg;
	}

	public CollapsiblePanel getExplorePanel() {
		return explorePanel;
	}

	public void setExplorePanel(CollapsiblePanel explorePanel) {
		this.explorePanel = explorePanel;
	}

	public JPanel[] getExploreContent() {
		return exploreContent;
	}

	public void setExploreContent(JPanel[] exploreContent) {
		this.exploreContent = exploreContent;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public ParameterSet getCurrentParamsCopy() {
		return currentParamsCopy;
	}

	public void setCurrentParamsCopy(ParameterSet currentParamsCopy) {
		this.currentParamsCopy = currentParamsCopy;
	}

	public int getEnumerationSelection() {
		return enumerationSelection;
	}

	public void setEnumerationSelection(int enumerationSelection) {
		this.enumerationSelection = enumerationSelection;
	}

	public MCODEClusterBrowserPanel getClusterBrowserPanel() {
		return clusterBrowserPanel;
	}

	public void setClusterBrowserPanel(
			MCODEClusterBrowserPanel clusterBrowserPanel) {
		this.clusterBrowserPanel = clusterBrowserPanel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getGraphpicsize() {
		return graphPicSize;
	}

	public static int getDefaultrowheight() {
		return defaultRowHeight;
	}

	public ClusterUtil getMcodeUtil() {
		return mcodeUtil;
	}

	public DiscardResultAction getDiscardResultAction() {
		return discardResultAction;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void setNetworkView(CyNetworkView networkView) {
		this.networkView = networkView;
	}
}
