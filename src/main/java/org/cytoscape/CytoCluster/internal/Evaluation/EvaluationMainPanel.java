package org.cytoscape.CytoCluster.internal.Evaluation;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import org.cytoscape.CytoCluster.internal.CommonUI.MyTipTool;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

public class EvaluationMainPanel extends JPanel implements CytoPanelComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157848088882843292L;
	private CySwingApplication desktopApp;
	private ClusterUtil clusterUtil;
	private CyApplicationManager cyApplicationManagerServiceRef;
	// private EvaluateAction evaluateAction;
	public static boolean isExist;

	// private List<CyAction> actions;
	private JPanel optionPanel;// the north panel
	JPanel choicePanel;// the center panel
	private JPanel bottomPanel;// the south panel

	DecimalFormat decimal; // used in the formatted text fields
	JLabel descriptionLabel;
	String description = "";// describe the evaluation operation options or
							// methods
	JComboBox optionsComboBox;// choose the options evaluation Main Panel
	JPanel descriptionPanel;// describe the options details
	JPanel basicInfoJPanel;// basic Information Panel
	JPanel cScoreJPanel;// C-Score Panel
	JPanel knownComplexesCmpJPanel;// Comparison with known complexes
	JPanel choiceMethodsPanel;// Evaluation Methods
	JPanel paramPanel; // some parameters
	JPanel analyzedAlgorithmOptions;// analyzed Cluster Analysis Methods

	public EvaluationMainPanel() {
		init();
	}

	public EvaluationMainPanel(CySwingApplication desktopApp,
			ClusterUtil clusterUtil) {
		this.desktopApp = desktopApp;
		this.clusterUtil = clusterUtil;
		isExist = true;
		init();
	}

	public void init() {

		// this.actions = new ArrayList();
		// layout
		setLayout(new BorderLayout());

		// this.bottomPanel = createBottomPanel();
		this.add(createEvalOptionsPanel(), BorderLayout.NORTH);

		// AlgorithmPanel ---- in the center of MainPanel
		this.add(createAlgorithmPanel(), BorderLayout.CENTER);

		this.add(getBottomPanel(), BorderLayout.SOUTH);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(300, 800));
	}

	// panel to select the scope
	@SuppressWarnings("unchecked")
	private JPanel createEvalOptionsPanel() {
		if (this.optionPanel == null) {
			this.optionPanel = new JPanel();
			this.optionPanel.setLayout(new BoxLayout(this.optionPanel,
					BoxLayout.LINE_AXIS));
			this.optionPanel.setBorder(BorderFactory
					.createTitledBorder("Evaluation Options"));

			optionsComboBox = new JComboBox();
			optionsComboBox.addItem("Please Choose One Option");
			optionsComboBox.addItem("Show All Results Statistics");
			optionsComboBox.addItem("Basic Informaion");
			optionsComboBox.addItem("Calculate C-Score");
			optionsComboBox.addItem("Comparison With Known Complexes");
			optionsComboBox
					.setToolTipText("Evaluation Options for Cluster Analysis");
			optionsComboBox.setSelectedIndex(0);
			optionsComboBox.addActionListener(new OptionsComboxListener());
			this.optionPanel.add(optionsComboBox);
		}
		return this.optionPanel;
	}

	/**
	 * create Algorithm Panel
	 * 
	 * @return
	 */
	private JScrollPane createAlgorithmPanel() {
		choicePanel = new JPanel();
		choicePanel.setLayout(new BorderLayout());

		descriptionLabel = new JLabel() {
			private static final long serialVersionUID = -7944948599084600034L;

			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		description = "<html><body>" + "Please Choose One Option:\n<br>"
				+ "1,Show All Results Statistics\n<br>"
				+ "2,Basic Information\n<br>"
				+ "3,Calculate the C-Score(P-value)\n<br>"
				+ "4,Comparison with Known Complexes\n<br>" + "</body></html>";
		descriptionLabel.setText(description);
		JScrollPane jScrollPane = new JScrollPane(descriptionLabel);
		jScrollPane.setToolTipText(description);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());

		descriptionPanel = new JPanel();
		choiceMethodsPanel = new JPanel();
		descriptionPanel.add(jScrollPane);

		descriptionPanel.setLayout(new BoxLayout(descriptionPanel,
				BoxLayout.Y_AXIS));
		choiceMethodsPanel.setLayout(new BoxLayout(choiceMethodsPanel,
				BoxLayout.Y_AXIS));

		descriptionPanel.setPreferredSize(new Dimension(260, 130));
		choiceMethodsPanel.setPreferredSize(new Dimension(260, 100));

		descriptionPanel.setBorder(BorderFactory
				.createTitledBorder("Description"));
		choiceMethodsPanel.setBorder(BorderFactory
				.createTitledBorder("Methods"));

		descriptionPanel.setToolTipText("Description For Evaluaion");
		choiceMethodsPanel.setToolTipText("Methods For Evaluation");
		choicePanel.setToolTipText("Please select an evaluation algorithm");
		choicePanel.add(descriptionPanel, BorderLayout.NORTH);
		choicePanel.add(choiceMethodsPanel, BorderLayout.CENTER);

		paramPanel = new JPanel();
		paramPanel.setToolTipText("add Parameters or Formulas or other Datas ");
		paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
		paramPanel.setBorder(BorderFactory.createTitledBorder("Data Sets"));
		paramPanel.setPreferredSize(new Dimension(260, 100));

		analyzedAlgorithmOptions = new JPanel();
		analyzedAlgorithmOptions
				.setToolTipText("Choose the Cluster Algorithm to Evaluate");
		analyzedAlgorithmOptions.setBorder(BorderFactory
				.createTitledBorder("Algorithms"));
		analyzedAlgorithmOptions.setPreferredSize(new Dimension(260, 100));
		getAnalyzedAlgorithmOptionsPanel();

		JTextPane jTextPane = new JTextPane();
		jTextPane.setText(description);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(choicePanel, BorderLayout.NORTH);
		p.add(paramPanel, BorderLayout.CENTER);
		p.add(analyzedAlgorithmOptions, BorderLayout.SOUTH);

		JScrollPane scrollPanel = new JScrollPane(p);
		scrollPanel.setBorder(BorderFactory
				.createTitledBorder("Evaluation Details"));
		return scrollPanel;
	}

	/**
	 * get the analyzed Algorithm in analyzedAlgorithmOptions panel
	 */
	public void getAnalyzedAlgorithmOptionsPanel() {
		this.analyzedAlgorithmOptions.removeAll();
		ArrayList<ResultPanel> collResultPanels = (ArrayList<ResultPanel>) this.clusterUtil
				.getResultPanels();
		int resultsSize = collResultPanels.size();
		JPanel oJPanel = new JPanel();
		oJPanel.setLayout(new GridLayout(resultsSize / 2, 2));
		oJPanel.setBorder(BorderFactory.createEmptyBorder());
		System.out.println(" resultsSize = " + resultsSize);

		for (int i = 0; i < resultsSize; i++) {
			final ResultPanel resultPanel = collResultPanels.get(i);
			String boxName = resultPanel.getAlgname() + "-"
					+ resultPanel.getTitle();
			System.out.println("Name " + boxName);
			final Checkbox checkbox = new Checkbox(boxName);
			checkbox.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// System.out.println("checkbox state " +
					// checkbox.getState());
					// System.out
					// .println("EvaluationUtils.CurrentSelectedResults size"
					// + EvaluationUtils.CurrentSelectedResults
					// .size());
					if (!checkbox.getState()) {
						if (!EvaluationUtils.CurrentSelectedResults
								.contains(resultPanel))
							EvaluationUtils.CurrentSelectedResults
									.add(resultPanel);
					} else {
						EvaluationUtils.CurrentSelectedResults
								.remove(resultPanel);
					}
					checkbox.setState(!checkbox.getState());
					// System.out.println("checkbox state " +
					// checkbox.getState());
					// System.out
					// .println("EvaluationUtils.CurrentSelectedResults size"
					// + EvaluationUtils.CurrentSelectedResults
					// .size());
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});

			oJPanel.add(checkbox);
		}
		JScrollPane jScrollPane = new JScrollPane(oJPanel);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.analyzedAlgorithmOptions.add(jScrollPane);
	}

	/**
	 * @return bottomPanel
	 */
	public JPanel getBottomPanel() {
		if (this.bottomPanel == null) {
			this.bottomPanel = new JPanel();
			this.bottomPanel.setLayout(new FlowLayout());
		}
		return this.bottomPanel;
	}

	/*---------------------------- ¡¾listener¡¿  ----------------------------------------*/
	public void addAction(CyAction cyAction) {
		JButton bt = new JButton(cyAction);
		bt.setText("Begin Compare");
		getBottomPanel().add(bt);
	}

	private class OptionsComboxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(optionsComboBox)) {

				String cmd = optionsComboBox.getSelectedItem().toString();

				System.out.println("\ncmd is :" + cmd);

				if ("Please Choose One Option".equals(cmd)) {
					EvaluationMainPanel.this.choiceMethodsPanel.removeAll();

					description = "<html><body>"
							+ "Please Choose One Option:\n<br/>"
							+ "1,Show All Results Statistics\n<br/>"
							+ "2,Basic Information\n<br>"
							+ "3,Calculate the C-Score(P-value)\n<br/>"
							+ "4,Comparison with Known Complexes\n<br/>"
							+ "</body></html>";
				}
				if ("Show All Results Statistics".equals(cmd)) {
					if (clusterUtil.getResultPanels().size() > 0) {
						// update the analyzed results
						getAnalyzedAlgorithmOptionsPanel();
						EvaluationMainPanel.this.choiceMethodsPanel.removeAll();

						description = "<html><body>"
								+ "Show All Results Statistics: \n<br>"
								+ "The Result Number\n<br> "
								+ " The Used Algorithm \n<br>"
								+ " Clusters Number \n<br>" + "Max Size \n<br>"
								+ " Min Size \n<br>" + "Avg Degree \n<br>"
								+ "</body></html>";

						// new the ResultsStatistics Panel
						new ResultsStatisticsPanel(clusterUtil);
					} else {
						JOptionPane.showMessageDialog(null,
								"there does not exist results!!");
					}
				}
				if ("Basic Informaion".equals(cmd)) {
					getBasicInfoJPanel();

					description = "<html><body>\n"
							+ "Density: \n Density is to describe how closely the nodes in the cluster interact with each other. \n<br>"
							+ "Given a cluster consisting of n nodes and m edges, its density is 2m/n(n-1).\n<br>\n<br>"
							+ "Size Distribution:\n The size distribution is to describe the basic information of the cluster results \n<br>"
							+ "by showing the charts with the nodes size and the cluster number distributed on it.\n<br>"
							+ "</body></html>";
				}
				if ("Calculate C-Score".equals(cmd)) {
					getCScoreJPanel();

					description = "<html><body>\n"
							+ "In order to detect the functional characteristics of the predicted clusters,\n<br/>"
							+ "we compare the predicted clusters with known functional classification.\n<br/>"
							+ "Evaluation methods we used as follows:\n<br>"
							+ "1,P-value\n<br/>" + "2,Precision \n<br>"
							+ "3,Recall \n<br/>" + "4,F-Measure \n<br>"
							+ "</html></body>";
				}
				if ("Comparison With Known Complexes".equals(cmd)) {
					getKnownComplexesCmpJPanel();

					description = "<html><body>\n"
							+ "To evaluate the effectiveness of a algorithm\n<br> "
							+ "for detecting protein complexes,\n<br>"
							+ "we compare the predicted clusters produced by \n<br>"
							+ "the algorithm with known protein complexes,\n<br>"
							+ "The overlapping scoreOS(Pc,Kc) between a predicted cluster Pc and a known \n<br>"
							+ "</html></body> ";
				}

				// update the UI
				EvaluationMainPanel.this.choiceMethodsPanel.updateUI();
				EvaluationUtils.CurrentvaluationAlgorithm = "";

				// update the description
				descriptionLabel.setText(description);
				descriptionLabel.setToolTipText(description);
			}
		}
	}

	/**
	 * listening the basicInfoPanel which contains DENSITY/SIZEDISTRIBUTION
	 */
	private class BasicInfoPanelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String cmd = e.getActionCommand();
			if (EvaluationUtils.SIZEDISTRIBUTION.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.SIZEDISTRIBUTION;
				return;
			}
			if (EvaluationUtils.DENSITY.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.DENSITY;
				return;
			}
		}
	}

	/**
	 * listening the cScorePanel which contains PVALUE/PRECISION/RECALL/FMEASURE
	 */
	private class CScorePanelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getActionCommand());
			String cmd = e.getActionCommand();
			if (EvaluationUtils.PVALUE.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.PVALUE;
				return;
			}
			if (EvaluationUtils.PRECISION.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.PRECISION;
				return;
			}
			if (EvaluationUtils.RECALL.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.RECALL;
				return;
			}
			if (EvaluationUtils.FMEASURE.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.FMEASURE;
				return;
			}
			if (EvaluationUtils.GOFUNCTION.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.GOFUNCTION;
				return;
			}
			if (EvaluationUtils.GOPROCESS.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.GOPROCESS;
				return;
			}
			if (EvaluationUtils.GOCOMPONENT.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.GOCOMPONENT;
				return;
			}
			if (EvaluationUtils.MIPSPROTEIN.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.MIPSPROTEIN;
				return;
			}
			if (EvaluationUtils.OTHER_P_F.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.OTHER_P_F;
				return;
			}
			if (EvaluationUtils.OTHER_F_A.equals(cmd)) {
				EvaluationUtils.CURRENTANNOTATION = EvaluationUtils.OTHER_F_A;
				return;
			}
		}
	}

	/**
	 * listening the basicInfoPanel which contains
	 * KNOWNCOMPLEXESCMP/SENSITIVITY_SPECIFICITY
	 */
	private class KnownComplexesCmpPanelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String cmd = e.getActionCommand();
			if (EvaluationUtils.KNOWNCOMPLEXESCMP.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.KNOWNCOMPLEXESCMP;
				return;
			}
			if (EvaluationUtils.SENSITIVITY_SPECIFICITY.equals(cmd)) {
				EvaluationUtils.CurrentvaluationAlgorithm = EvaluationUtils.SENSITIVITY_SPECIFICITY;
				return;
			}
		}
	}

	/********************************** ¡¾get the panel¡¿ *******************************************/
	/**
	 * get BasicInfo JPanel
	 */
	public void getBasicInfoJPanel() {

		basicInfoJPanel = new JPanel(new GridLayout(2, 1));
		JRadioButton densityButton = new JRadioButton("Density") {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		densityButton
				.setToolTipText("Density: \n "
						+ "Density is to describe how closely the nodes in the cluster interact with each other. \n"
						+ "Given a cluster consisting of n nodes and m edges, its density is 2m/n(n-1).\n");
		densityButton.setActionCommand(EvaluationUtils.DENSITY);

		JRadioButton sizeDistributionButton = new JRadioButton(
				"Size Distribution") {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		sizeDistributionButton
				.setToolTipText("Size Distribution:\n"
						+ "The size distribution is to describe the basic information of the cluster results \n"
						+ "by showing the charts with the nodes size and the cluster number distributed on it.");
		sizeDistributionButton
				.setActionCommand(EvaluationUtils.SIZEDISTRIBUTION);

		// add to button group
		ButtonGroup basicInfoOptions = new ButtonGroup();
		basicInfoOptions.add(densityButton);
		basicInfoOptions.add(sizeDistributionButton);

		// add action listener
		densityButton.addActionListener(new BasicInfoPanelListener());
		sizeDistributionButton.addActionListener(new BasicInfoPanelListener());

		// add to panel
		basicInfoJPanel.add(densityButton);
		basicInfoJPanel.add(sizeDistributionButton);
		this.choiceMethodsPanel.removeAll();
		this.choiceMethodsPanel.add(basicInfoJPanel);
	}

	/**
	 * get C-Score JPanel
	 */
	public void getCScoreJPanel() {
		cScoreJPanel = new JPanel(new GridLayout(2, 2));
		/**
		 * P-value RadioButton
		 */
		JRadioButton pValueButton = new JRadioButton(EvaluationUtils.PVALUE) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		pValueButton
				.setToolTipText("The P-value based on hypergeometric distribution is often used to estimate \n"
						+ "whether a given set of proteins is accumulated by chance.\n"
						+ "It has been used as a criteria to assign each predicted cluster a main function.\n"
						+ "Here,we also calculate P-value for each predicted cluster and assign a function category to it\n"
						+ "when the minimum P-value occurrs.\n");
		pValueButton.setActionCommand(EvaluationUtils.PVALUE);

		/**
		 * Precision RadioButton
		 */
		JRadioButton precisionButton = new JRadioButton(
				EvaluationUtils.PRECISION) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		precisionButton
				.setToolTipText("The Precision for a cluster is \n"
						+ "the number of true positives divided by the total number of elements labeled as belonging to the positive cluster.\n"
						+ "precision = tp/(tp+fp) where tp is the number of overlap \n"
						+ "and fp+tp is the namuber of the nodes in the cluster.\n"
						+ "Fig A shows the precision of each cluster.\n"
						+ "Fig B shows the number of the clusters in different interval of the precision.");
		precisionButton.setActionCommand(EvaluationUtils.PRECISION);

		/**
		 * Recall RadioButton
		 */
		JRadioButton recallButton = new JRadioButton(EvaluationUtils.RECALL) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		recallButton
				.setToolTipText("Recall is defined as the number of true positives divided by\n "
						+ "the total number of elements that actually belong to the positive.\n"
						+ "recall=tp/(tp+fn) where tp is the number of overlap and tp+fn is the number of the background\n");
		recallButton.setActionCommand(EvaluationUtils.RECALL);

		/**
		 * F-measure RadioButton
		 */
		JRadioButton fMeasureButton = new JRadioButton(EvaluationUtils.FMEASURE) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		fMeasureButton
				.setToolTipText("A measure that combines Precision and Recall \n"
						+ "is the harmonic mean of precision and recall, the traditional F-measure or balanced F-score.\n"
						+ "f-measure=2*precision*recall/(precision+recall)\n");
		fMeasureButton.setActionCommand(EvaluationUtils.FMEASURE);

		// add to button group
		ButtonGroup CScoreOptions = new ButtonGroup();
		CScoreOptions.add(pValueButton);
		CScoreOptions.add(precisionButton);
		CScoreOptions.add(recallButton);
		CScoreOptions.add(fMeasureButton);

		// add action listener
		pValueButton.addActionListener(new CScorePanelListener());
		precisionButton.addActionListener(new CScorePanelListener());
		recallButton.addActionListener(new CScorePanelListener());
		fMeasureButton.addActionListener(new CScorePanelListener());

		// add to panel
		cScoreJPanel.add(pValueButton);
		cScoreJPanel.add(precisionButton);
		cScoreJPanel.add(recallButton);
		cScoreJPanel.add(fMeasureButton);

		// update the methods panel
		this.choiceMethodsPanel.removeAll();
		this.choiceMethodsPanel.add(cScoreJPanel);

		/**
		 * go.function RadioButton
		 */
		JRadioButton go_function_Button = new JRadioButton(
				EvaluationUtils.GOFUNCTION) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		go_function_Button.setToolTipText("GO Function");
		go_function_Button.setActionCommand(EvaluationUtils.GOFUNCTION);

		/**
		 * go.process RadioButton
		 */
		JRadioButton go_process_Button = new JRadioButton(
				EvaluationUtils.GOPROCESS) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		go_process_Button.setToolTipText("GO Process");
		go_process_Button.setActionCommand(EvaluationUtils.GOPROCESS);

		/**
		 * go.component RadioButton
		 */
		JRadioButton go_component_Button = new JRadioButton(
				EvaluationUtils.GOCOMPONENT) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		go_component_Button.setToolTipText("GO Component");
		go_component_Button.setActionCommand(EvaluationUtils.GOCOMPONENT);

		/**
		 * mips RadioButton
		 */
		JRadioButton mipsButton = new JRadioButton(EvaluationUtils.MIPSPROTEIN) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		mipsButton.setToolTipText("MIPS");
		mipsButton.setActionCommand(EvaluationUtils.MIPSPROTEIN);

		// add to button group
		ButtonGroup dataGroups = new ButtonGroup();
		dataGroups.add(go_process_Button);
		dataGroups.add(go_component_Button);
		dataGroups.add(go_function_Button);
		dataGroups.add(mipsButton);

		JButton other_P_F = new JButton("import P->F") {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		other_P_F
				.setToolTipText("Open Other Protein -> Function File To Evaluate\n"
						+ "File Fromat as follows :\n"
						+ "'YLR226W 0007049' \n per line");
		other_P_F.setActionCommand(EvaluationUtils.OTHER_P_F);

		JButton other_F_A = new JButton("import F->A") {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		other_F_A
				.setToolTipText("Open Other Function -> Annotation File To Evaluate\n"
						+ "File Fromat as follows :\n"
						+ "'0007049 cell cycle' \n per line");
		other_F_A.setActionCommand(EvaluationUtils.OTHER_F_A);

		JPanel dataJPanel = new JPanel(new GridLayout(3, 2));
		// add to panel
		dataJPanel.add(go_process_Button);
		dataJPanel.add(go_component_Button);
		dataJPanel.add(go_function_Button);
		dataJPanel.add(mipsButton);
		dataJPanel.add(other_P_F);
		dataJPanel.add(other_F_A);

		go_process_Button.addActionListener(new CScorePanelListener());
		go_component_Button.addActionListener(new CScorePanelListener());
		go_function_Button.addActionListener(new CScorePanelListener());
		mipsButton.addActionListener(new CScorePanelListener());
		other_P_F.addActionListener(new CScorePanelListener());
		other_F_A.addActionListener(new CScorePanelListener());

		// update the methods panel
		this.paramPanel.removeAll();
		this.paramPanel.add(dataJPanel);
	}

	/**
	 * get Known Complexes Comparison Panel
	 */
	public void getKnownComplexesCmpJPanel() {
		knownComplexesCmpJPanel = new JPanel(new GridLayout(2, 1));
		/**
		 * knownComplexesCmp RadioButton
		 */
		JRadioButton knownComplexesCmpButton = new JRadioButton(
				EvaluationUtils.KNOWNCOMPLEXESCMP) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		knownComplexesCmpButton
				.setToolTipText("To evaluate the effectiveness of a algorithm for detecting protein complexes,\n"
						+ "we compare the predicted clusters produced by the algorithm with known protein complexes,\n"
						+ "The overlapping scoreOS(Pc,Kc) between a predicted cluster Pc and a known complex Kc \n "
						+ "is calculated by the following formula: OS (Pc,Kc)=i*i/a*b \n"
						+ "where i is the size of the intersection set of the predicted cluster and the known complex,  \n "
						+ "a is the size of the predicted cluster and b is the size of the known complex.");
		knownComplexesCmpButton
				.setActionCommand(EvaluationUtils.KNOWNCOMPLEXESCMP);

		/**
		 * SnSp(Sensitivity/Specificity) RadioButton
		 */
		JRadioButton SensitivitySpecificityButton = new JRadioButton(
				EvaluationUtils.SENSITIVITY_SPECIFICITY) {
			public JToolTip createToolTip() {
				return new MyTipTool();
			}
		};
		SensitivitySpecificityButton
				.setToolTipText("Sensitivity and specificity are two important aspects to estimate the performance of algorithms for detecting protein complexes.\n"
						+ "Sensitivity is the fraction of the true-positive predictions out of all the true predictions,\n"
						+ "defined by the following formula:Sn = TP/(TP+FN)\n"
						+ "where TP(true positive)is the number of the predicted  clusters matched by the known complexes with OS(Pc,Kc)¡Ýos(the default os value is 0.2,here you can also set the os value),\n"
						+ "and FN(false negative)is the number of the known complexes that are not matched by the predicted clusters.\n"
						+ "Specificity is the fraction of the true-positive predictions out of all the positive predictions,\n"
						+ "defined by the following formula:Sp=TP/(TP+FP) where FP(false positive)equals the total number of the predicted clusters minus TP.\n"
						+ "According to the assumption ,a predicted cluster and a known complex are considered to be matched if OS(Pc,Kc)¡Ýos(os is the value you set).\n"
						+ "Generally,we use 0.2 as the matched overlapping threshold but here you can set the value you like.");
		SensitivitySpecificityButton
				.setActionCommand(EvaluationUtils.SENSITIVITY_SPECIFICITY);

		// add to button group
		ButtonGroup knownComplexesCmpOptions = new ButtonGroup();
		knownComplexesCmpOptions.add(knownComplexesCmpButton);
		knownComplexesCmpOptions.add(SensitivitySpecificityButton);

		// add action listener
		knownComplexesCmpButton
				.addActionListener(new KnownComplexesCmpPanelListener());
		SensitivitySpecificityButton
				.addActionListener(new KnownComplexesCmpPanelListener());

		// add to panel
		knownComplexesCmpJPanel.add(knownComplexesCmpButton);
		knownComplexesCmpJPanel.add(SensitivitySpecificityButton);
		this.choiceMethodsPanel.removeAll();
		this.choiceMethodsPanel.add(knownComplexesCmpJPanel);
	}

	/**
	 * show the cluster Results
	 * 
	 * @author Administrator
	 * 
	 */
	private class ResultsStatisticsPanel extends JFrame {
		private static final long serialVersionUID = 1L;
		private ClusterUtil clusterUtil;
		private JTable resJTable;
		private JScrollPane scrollPane;

		public ResultsStatisticsPanel(ClusterUtil clusterUtil) {
			this.clusterUtil = clusterUtil;
			init();
		}

		public void init() {
			String[] columnNames = { "No.", "Algorithm", "Clusters Num",
					"Max Size", "Min Size", "Avg Degree" };
			int colSize = columnNames.length;
			ArrayList<ResultPanel> collResultPanels = (ArrayList<ResultPanel>) clusterUtil
					.getResultPanels();
			int resultsSize = collResultPanels.size();
			String data[][] = new String[resultsSize][colSize];

			System.out.println("\ncolSize: " + colSize + "\nresultsSize  "
					+ resultsSize);

			for (int i = 0; i < resultsSize; i++) {
				ResultPanel rPanel = collResultPanels.get(i);

				data[i][0] = rPanel.getTitle();
				data[i][1] = rPanel.getAlgname();
				data[i][2] = rPanel.getClusters().size() + "";

				List<Cluster> clusters = rPanel.getClusters();
				// get the node count use method
				// cluster.getNetwork().getNodeCount()
				int minSize = clusters.get(0).getNetwork().getNodeCount();
				int maxSize = minSize;
				double totalDegree = clusters.get(0).getInDegree();
				int size;
				for (int j = 1; j < clusters.size(); j++) {
					size = clusters.get(j).getNetwork().getNodeCount();
					if (minSize > size) {
						minSize = size;
					}
					if (maxSize < size) {
						maxSize = size;
					}
					totalDegree += clusters.get(j).getTotalDegree();
				}
				data[i][3] = maxSize + "";
				data[i][4] = minSize + "";
				data[i][5] = totalDegree / clusters.size() + "";

				String string = "   " + data[i][0] + "\t" + data[i][1] + "\t"
						+ data[i][2] + "\t" + data[i][3] + "\t" + data[i][4]
						+ "\t" + data[i][5];
				System.out.println("data[" + i + "][]" + string);
			}

			this.resJTable = new JTable(data, columnNames);
			this.scrollPane = new JScrollPane(resJTable);

			// set the location
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;
			this.setLocation(width * 2 / 5, height * 2 / 5);

			this.setTitle("All Results Statistics");
			this.getContentPane().add(scrollPane);
			this.setVisible(true);
			this.setSize(new Dimension(600, 300));
		}
	}

	/*---------------------------[getter/setter]---------------------------*/
	public Component getComponent() {
		return this;
	}

	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}

	public String getTitle() {
		return "Evaluation";
	}

	public Icon getIcon() {
		return null;
	}

	public CyApplicationManager getCyApplicationManagerServiceRef() {
		return cyApplicationManagerServiceRef;
	}

	public void setCyApplicationManagerServiceRef(
			CyApplicationManager cyApplicationManagerServiceRef) {
		this.cyApplicationManagerServiceRef = cyApplicationManagerServiceRef;
	}

	public JPanel getAnalyzedAlgorithmOptions() {
		return analyzedAlgorithmOptions;
	}

	public void setAnalyzedAlgorithmOptions(JPanel analyzedAlgorithmOptions) {
		this.analyzedAlgorithmOptions = analyzedAlgorithmOptions;
	}
}
