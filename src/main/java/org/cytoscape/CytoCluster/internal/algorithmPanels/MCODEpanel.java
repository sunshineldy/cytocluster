package org.cytoscape.CytoCluster.internal.algorithmPanels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;

import org.cytoscape.CytoCluster.internal.*;
import org.cytoscape.application.swing.CySwingApplication;




public class MCODEpanel  extends JPanel{
	private final ClusterUtil mcodeUtil;
	private CySwingApplication desktopApp;
	ParameterSet currentParameters;
	
	DecimalFormat decimal; // used in the formatted text fields
    JScrollPane algorithmPanel;
    CollapsiblePanel clusteringPanel;
 //   CollapsiblePanel customizePanel;
    JPanel customizePanel;
    JPanel clusteringContent;
    JPanel customizeClusteringContent;
  
    //Scoring
    JCheckBox includeLoops;
    JFormattedTextField degreeThreshold;
    //clustering
    JFormattedTextField kCore;
    JFormattedTextField nodeScoreThreshold;
 //   JRadioButton optimize;
 //   JRadioButton customize;

    JCheckBox haircut;
    JCheckBox fluff;
    JFormattedTextField nodeDensityThreshold;
    JFormattedTextField maxDepth;

  
    
public MCODEpanel(CySwingApplication swingApplication, ClusterUtil mcodeUtil){

		
		desktopApp=swingApplication;
		
		this.mcodeUtil = mcodeUtil;
		
		this.currentParameters =this.mcodeUtil.getCurrentParameters().getParamsCopy(null);
		//currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
		//currentParameters = MainPanel.currentParamsCopy;
	   decimal = new DecimalFormat();
       decimal.setParseIntegerOnly(true);
	   
       this.setLayout(new BorderLayout());
       this.setBorder(BorderFactory.createTitledBorder(""));
       this.setVisible(false);
       
       CollapsiblePanel collapsiblePanel = new CollapsiblePanel("MCODE Options");
       
       JPanel panel = new JPanel();
       panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
       
       CollapsiblePanel createScoringPanel = createScoringPanel();
       clusteringPanel = createClusteringPanel();
       
       panel.add(createScoringPanel);
       panel.add(clusteringPanel);
       
       collapsiblePanel.getContentPane().add(panel,BorderLayout.NORTH);
       collapsiblePanel.setToolTipText("Customize clustering parameters (Optional)");
       this.add(collapsiblePanel);     
	}
	
	
	 
    /**
     * Create a collapsible panel that holds network scoring parameter inputs
     *
     * @return panel containing the network scoring parameter inputs
     */
   private CollapsiblePanel createScoringPanel() {
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Scoring");        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        
        //Include Loop input
        JLabel includeLoopsLabel = new JLabel("Include Loop");
        includeLoops = new JCheckBox() {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        
       includeLoops.addItemListener(new IncludeLoopsCheckBoxAction());
       includeLoops.setSelected(currentParameters.isIncludeLoops());
        String includeLoopsTip = "Regard loops when clustering.\n" +
                "This will influence the score of cliques";
        includeLoops.setToolTipText(includeLoopsTip);
        
        JPanel includeLoopsPanel = new JPanel() {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        includeLoopsPanel.setLayout(new BorderLayout());
        includeLoopsPanel.setToolTipText(includeLoopsTip);
        includeLoopsPanel.add(includeLoopsLabel, BorderLayout.WEST);
        includeLoopsPanel.add(includeLoops, BorderLayout.EAST);

        //Degree Threshold input
        JLabel degreeThresholdLabel = new JLabel("Degree Threshold");
        degreeThreshold = new JFormattedTextField(decimal) {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        degreeThreshold.setColumns(3);
        degreeThreshold.addPropertyChangeListener("value", new FormattedTextFieldAction());
        String degreeThresholdTip = "degree cutoff of the nodes";
        degreeThreshold.setToolTipText(degreeThresholdTip);
        degreeThreshold.setText((new Integer(currentParameters.getDegreeCutoff()).toString()));
        JPanel degreeThresholdPanel = new JPanel() {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        degreeThresholdPanel.setLayout(new BorderLayout());
        degreeThresholdPanel.setToolTipText(degreeThresholdTip);
        degreeThresholdPanel.add(degreeThresholdLabel, BorderLayout.WEST);
        degreeThresholdPanel.add(degreeThreshold, BorderLayout.EAST);
        
        //add the components to the panel
        panel.add(includeLoopsPanel);
        panel.add(degreeThresholdPanel);
        collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
        return collapsiblePanel;
    }
    
   /**
    * Creates a collapsible panel that holds 2 other collapsible panels for 
    * either customizing or optimized clustering parameters
    *
    */
	 private CollapsiblePanel createClusteringPanel() {
	        CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Clustering");
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	 //       customize = new JRadioButton("Customize", !currentParameters.isOptimize());
	 //       optimize = new JRadioButton("Optimize", currentParameters.isOptimize());
	 //       ButtonGroup clusteringChoice = new ButtonGroup();
	  //      clusteringChoice.add(customize);
	//        clusteringChoice.add(optimize);
	 //       customize.addActionListener(new CustomizeAction());
	//        optimize.addActionListener(new CustomizeAction());
	        
	//        customizePanel = createClusterParaPanel(customize);
	        customizePanel = createClusterParaPanel();
	//        CollapsiblePanel optimalPanel = createOptimizePanel(optimize);
	        panel.add(customizePanel);
//	        panel.add(optimalPanel);        
	        this.clusteringContent = panel;        
	        collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
	        return collapsiblePanel;
	    }

	 
	 
	 /**
	     * Creates a collapsible panel that holds clustering parameters
	     * placed within the cluster finding collapsible panel
	     *
	     * @param component Any JComponent that may appear in the titled border of the panel
	     * @return collapsablePanel
	     */
	 //   private CollapsiblePanel createClusterParaPanel(JRadioButton component) {
	 	  private JPanel createClusterParaPanel() {
	  //      CollapsiblePanel collapsiblePanel = new CollapsiblePanel(component);
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	        //Node Score Threshold
	        String nodeScoreThresholdTip = "Sets the acceptable score deviance from\n" +
	        		"the seed node's score for expanding a cluster\n" +
	        		"(most influental parameter for cluster size).";
	        JLabel nodeScoreThresholdLabel = new JLabel("NodeScore Threshold");
	        nodeScoreThreshold = new JFormattedTextField(new DecimalFormat("0.000")) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        nodeScoreThreshold.setColumns(3);
	        nodeScoreThreshold.addPropertyChangeListener("value", new FormattedTextFieldAction());
	        nodeScoreThreshold.setToolTipText(nodeScoreThresholdTip);
	        nodeScoreThreshold.setText((new Double(currentParameters.getNodeScoreCutoff()).toString()));
	        JPanel nodeScoreThresholdPanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        nodeScoreThresholdPanel.setToolTipText(nodeScoreThresholdTip);
	        nodeScoreThresholdPanel.add(nodeScoreThresholdLabel, BorderLayout.WEST);
	        nodeScoreThresholdPanel.add(nodeScoreThreshold,BorderLayout.EAST);

	        //K-Core input
	        JLabel kCoreLabel = new JLabel("K-Core Threshold");
	        kCore = new JFormattedTextField(decimal) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        kCore.setColumns(3);
	        kCore.addPropertyChangeListener("value", new FormattedTextFieldAction());
	        String kCoreTip = "Filters out clusters lacking a\n" +
	        		"maximally inter-connected core\n" +
	        		"of at least k edges per node.";
	        kCore.setToolTipText(kCoreTip);
	        kCore.setText((new Integer(currentParameters.getKCore()).toString()));
	        JPanel kCorePanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        kCorePanel.setToolTipText(kCoreTip);
	        kCorePanel.add(kCoreLabel, BorderLayout.WEST);
	        kCorePanel.add(kCore, BorderLayout.EAST);

	        //Haircut Input
	        String haircutTip = "Remove singly connected\n" +
	        		"nodes from clusters.";
	        JLabel haircutLabel = new JLabel("Haircut");
	        haircut = new JCheckBox() {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        
	        haircut.addItemListener(new HaircutCheckBoxAction());
	        //haircut.setSelected(true);
	        haircut.setSelected(currentParameters.isHaircut());
	        haircut.setToolTipText(haircutTip);
	        
	        JPanel haircutPanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        }; 
	        haircutPanel.setToolTipText(haircutTip);
	        haircutPanel.add(haircutLabel, BorderLayout.WEST);
	        haircutPanel.add(haircut, BorderLayout.EAST);

	        //Fluffy Input
	        JLabel fluffLabel = new JLabel("Fluff");
	        fluff = new JCheckBox() {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        
	       // fluff.addItemListener(new FluffCheckBoxAction());
	       // fluff.setSelected(currentParameters.isFluff());
	        String fluffTip = "Expand core cluster by one\n" +
	        		"neighbour shell (applied\n"+
	        		"after the optional haircut).";
	        fluff.setToolTipText(fluffTip);
	        
	        JPanel fluffPanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        fluffPanel.setToolTipText(fluffTip);
	        fluffPanel.add(fluffLabel, BorderLayout.WEST);
	        fluffPanel.add(fluff, BorderLayout.EAST);

	        //Fluff node density cutoff input
	        JLabel nodeDensityThresholdLabel = new JLabel("NodeDensity Threshold");
	        nodeDensityThreshold = new JFormattedTextField(new DecimalFormat("0.000")) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        nodeDensityThreshold.setColumns(3);
	        nodeDensityThreshold.addPropertyChangeListener("value", new FormattedTextFieldAction());
	        String fluffNodeDensityCutoffTip = "Limits fluffing by setting the acceptable\n" +
	        		"node density deviance from the core cluster\n" +
	        		"density (allows clusters' edges to overlap).";
	        nodeDensityThreshold.setToolTipText(fluffNodeDensityCutoffTip);
	        nodeDensityThreshold.setText((new Double(currentParameters.getFluffNodeDensityCutoff()).toString()));
	        JPanel nodeDensityThresholdPanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        nodeDensityThresholdPanel.setToolTipText(fluffNodeDensityCutoffTip);
	        nodeDensityThresholdPanel.add(nodeDensityThresholdLabel, BorderLayout.WEST);
	        nodeDensityThresholdPanel.add(nodeDensityThreshold, BorderLayout.EAST);
	        nodeDensityThresholdPanel.setVisible(currentParameters.isFluff());
	        fluff.addItemListener(new FluffCheckBoxAction());
	        //fluff.setSelected(true);
		    fluff.setSelected(currentParameters.isFluff());
	        
	        //Max depth input
	        JLabel maxDepthLabel = new JLabel("MaxDepth");
	        maxDepth = new JFormattedTextField(decimal) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        maxDepth.setColumns(3);
	        maxDepth.addPropertyChangeListener("value", new FormattedTextFieldAction());
	        String maxDepthTip = "Limits the cluster size by setting the\n" +
	        		"maximum search distance from a seed\n" +
	        		"node (100 virtually means no limit).";
	        maxDepth.setToolTipText(maxDepthTip);
	        maxDepth.setText((new Integer(currentParameters.getMaxDepthFromStart()).toString()));
	        JPanel maxDepthPanel = new JPanel(new BorderLayout()) {
	            public JToolTip createToolTip() {
	                return new MyTipTool();
	            }
	        };
	        maxDepthPanel.setToolTipText(maxDepthTip);
	        maxDepthPanel.add(maxDepthLabel, BorderLayout.WEST);
	        maxDepthPanel.add(maxDepth, BorderLayout.EAST);
	        
	        panel.add(haircutPanel);
	        panel.add(fluffPanel);
	        panel.add(nodeDensityThresholdPanel);
	        panel.add(nodeScoreThresholdPanel);
	        panel.add(kCorePanel);
	        panel.add(maxDepthPanel);
	        this.customizeClusteringContent = panel;

	//        collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
	  //      return collapsiblePanel;
	        return panel;
	    }
	
	 
	    
	    /**
	     * Creates a collapsible panel that holds a benchmark file input, placed within the cluster finding collapsible panel
	     *
	     * @param component the radio button that appears in the titled border of the panel
	     * @return A collapsible panel holding a file selection input
	     * @see CollapsiblePanel
	     */
/*	    private CollapsiblePanel createOptimizePanel(JRadioButton component) {
	        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(component);
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        JLabel benchmarkStarter = new JLabel("Benchmark file location");
	        JPanel benchmarkStarterPanel = new JPanel(new BorderLayout());
	        benchmarkStarterPanel.add(benchmarkStarter, BorderLayout.WEST);
	        JFormattedTextField benchmarkFileLocation = new JFormattedTextField();
	        JButton browseButton = new JButton("Browse...");
	        JPanel fileChooserPanel = new JPanel(new BorderLayout());
	        fileChooserPanel.add(benchmarkFileLocation, BorderLayout.SOUTH);
	        fileChooserPanel.add(browseButton, BorderLayout.EAST);
	        panel.add(benchmarkStarterPanel);
	        panel.add(fileChooserPanel);
	        collapsiblePanel.getContentPane().add(panel, BorderLayout.NORTH);
	        return collapsiblePanel;
	    }
	   */
	    
/*	    public void clean(){
			   
		       clusteringPanel.getContentPane().remove(clusteringContent);
		       clusteringPanel.getContentPane().add(customizeClusteringContent, BorderLayout.NORTH);
		   }
	    */
	    
	    
	  /**
	     * Handles setting of the include loops parameter
	     */
	    private class IncludeLoopsCheckBoxAction implements ItemListener {
	    	private IncludeLoopsCheckBoxAction()
	       {
	    	   }
	    	public void itemStateChanged(ItemEvent e) {
	  //      	if(mcodeUtil.getMainPanel()!=null)
	 //       		currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
	            if (e.getStateChange() == ItemEvent.DESELECTED) {
	                currentParameters.setIncludeLoops(false);
	            } else {
	                currentParameters.setIncludeLoops(true);
	            }
	            System.out.println("set includeloops success!");
	        }
	    }
	    
	    
	    
	    /**
	    /**
	     * Sets the optimization parameter depending on which radio button is selected (cusomize/optimize)
	     */
	/*   private class CustomizeAction extends AbstractAction {
	        public void actionPerformed(ActionEvent e) {
	            if (optimize.isSelected()) {
	                currentParameters.setOptimize(true);
	            } else {
	                currentParameters.setOptimize(false);
	            }
	            System.out.println("set customize success!");
	        }
	    }
	    
	    
	    /**
	     * Handles setting for the text field parameters that are numbers.
	     * Makes sure that the numbers make sense.
	     */
	    private class FormattedTextFieldAction implements PropertyChangeListener {
	        public void propertyChange(PropertyChangeEvent e) {
	        	currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
	        	JFormattedTextField source = (JFormattedTextField) e.getSource();
	            String message = "Invaluled input\n";
	            boolean invalid = false;
	           
	          //  currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
	            if (source == degreeThreshold) {
	                Number value = (Number) degreeThreshold.getValue();
	                if ((value != null) && (value.intValue() > 1)) {
	                    currentParameters.setDegreeCutoff(value.intValue());
	                } else {
	                    //source.setValue(new Integer (2));
	                    message += "the node degree cutoff should no less than 2.";
	                    invalid = true;
	                }
	                source.setText((new Integer(currentParameters.getDegreeCutoff()).toString()));
	            } else if (source == nodeScoreThreshold) {
	                String value = nodeScoreThreshold.getText();
	                if ((value != null) && (Double.parseDouble(value) >= 0.0) && (Double.parseDouble(value) <= 1.0)) {
	                    currentParameters.setNodeScoreCutoff(Double.parseDouble(value));
	                    System.out.println(Double.parseDouble(value));
	                } else {
	                    
	                    message += "the node score cutoff should set between 0 to 1.";
	                    invalid = true;
	                }
	                source.setValue(new Double (currentParameters.getNodeScoreCutoff()));
	            } else if (source == kCore) {
	                Number value = (Number) kCore.getValue();
	                if ((value != null) && (value.intValue() > 1)) {
	                    currentParameters.setKCore(value.intValue());
	                } else {
	                    //source.setValue(new Integer (2));
	                    message += "the k value of K-Core should be greater than 1.";
	                    invalid = true;
	                }
	                source.setText((new Integer(currentParameters.getKCore()).toString()));
	            } else if (source == maxDepth) {
	                Number value = (Number) maxDepth.getValue();
	                if ((value != null) && (value.intValue() > 0)) {
	                    currentParameters.setMaxDepthFromStart(value.intValue());
	                } else {
	                   // source.setValue(new Integer (1));
	                    message += "max depth should be no less than 1.";
	                    invalid = true;
	                }
	                source.setValue(new Double (currentParameters.getMaxDepthFromStart()));
	            } else if (source == nodeDensityThreshold) {
	                String value = nodeDensityThreshold.getText();
	                if ((value != null) && (Double.parseDouble(value) >= 0.0) && (Double.parseDouble(value) <= 1.0)) {
	                    currentParameters.setFluffNodeDensityCutoff(Double.parseDouble(value));
	                } else {
	                    
	                    message += "fluff node density cutoff should\n" +
	                    		"be set between 0 and 1.";
	                    invalid = true;
	                }
	                source.setValue(new Double (currentParameters.getFluffNodeDensityCutoff()));
	            }
				if (invalid) {
	           //     JOptionPane.showMessageDialog(Cytoscape.getDesktop(), message, "paramter out of boundary", JOptionPane.WARNING_MESSAGE);
	            }
	        }
	    }

	    
	    /**
	     * Handles setting of the haircut parameter
	     */
	    private class HaircutCheckBoxAction implements ItemListener {
	    	private HaircutCheckBoxAction()
		       {
		    	   }
	        public void itemStateChanged(ItemEvent e) {
	    //    	if(mcodeUtil.getMainPanel()!=null)
	     //   		currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
	            if (e.getStateChange() == ItemEvent.DESELECTED) {
	                currentParameters.setHaircut(false);
	            } else {
	                currentParameters.setHaircut(true);
	            }
	        }
	    }
	    
	    
	    /**
	     * Handles setting of the fluff parameter and showing or hiding of the fluff node density cutoff input
	     */
	   private class FluffCheckBoxAction implements ItemListener {
		   private FluffCheckBoxAction()
	       {
	    	   }
	        public void itemStateChanged(ItemEvent e) {
	     //   	if(mcodeUtil.getMainPanel()!=null)
	     //   		currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();
	        	//currentParameters=mcodeUtil.getCurrentParameters();
	            if (e.getStateChange() == ItemEvent.DESELECTED) {
	                currentParameters.setFluff(false);
	            } else {
	                currentParameters.setFluff(true);
	               // nodeDensityThreshold.getParent().setVisible(true);
	            }
	          //  nodeDensityThresholdPanel.setVisible(currentParameters.isFluff());
	            nodeDensityThreshold.getParent().setVisible(currentParameters.isFluff());
	        }
	    }
	   
}
