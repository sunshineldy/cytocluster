package org.cytoscape.CytoCluster.internal.Evaluation;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;
import org.cytoscape.CytoCluster.internal.AbstractVizAction;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts.DensityChart;
import org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts.PValueChart;
import org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts.SizeDistrubtionChart;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.GO;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.MIPS;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.view.model.CyNetworkViewManager;

public class CompareAction extends AbstractVizAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2906203362261036255L;
	private ClusterUtil clusterUtil;

	public CompareAction(String name, CyApplicationManager applicationManager,
			CySwingApplication swingApplication,
			CyNetworkViewManager netViewManager, String enableFor,
			ClusterUtil clusterUtil) {
		super(name, applicationManager, swingApplication, netViewManager,
				enableFor);
		this.clusterUtil = clusterUtil;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println("< " + e.getActionCommand() + " >");
		int size = EvaluationUtils.CurrentSelectedResults.size();
	
		// System.out.println("size = " + size);
		List<ResultPanel> resultPanels = EvaluationUtils.CurrentSelectedResults;

		if (resultPanels.size() > 0) {
			if ("".equals(EvaluationUtils.CurrentvaluationAlgorithm)) {
				JOptionPane.showMessageDialog(null,
						"Please Choose  One Evaluation Method£¡£¡£¡", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
			// density
			if (EvaluationUtils.DENSITY
					.equals(EvaluationUtils.CurrentvaluationAlgorithm)) {
				DensityChart pChart = new DensityChart("Density", resultPanels,
						"Cluster ID", "Density", "Density Scope",
						"Cluster Number");

				pChart.drawChartA(resultPanels);
				pChart.drawChartB(resultPanels);
			}
			// size distribution
			if (EvaluationUtils.SIZEDISTRIBUTION
					.equals(EvaluationUtils.CurrentvaluationAlgorithm)) {

				SizeDistrubtionChart distrubtionChart = new SizeDistrubtionChart(
						"Size Distribution",
						EvaluationUtils.CurrentSelectedResults, "Cluster ID",
						"Size", "Size", "Cluster Number");
				distrubtionChart.drawChartA(resultPanels);
				distrubtionChart.drawChartB(resultPanels);
			}

			// open the GO Annotation
			EvaluationUtils.open_GO_AnnotationCode_Annotation(Resources
					.getInputStream(GO.ANNOTATION));
			if (EvaluationUtils.GO_Annotation_Map != null) {
				if (EvaluationUtils.GOFUNCTION
						.equals(EvaluationUtils.CURRENTANNOTATION)) {
					EvaluationUtils.openProtein_Annotation(Resources
							.getInputStream(GO.FUNCTION));
				}
				if (EvaluationUtils.GOPROCESS
						.equals(EvaluationUtils.CURRENTANNOTATION)) {
					EvaluationUtils.openProtein_Annotation(Resources
							.getInputStream(GO.PROCESS));
				}
				if (EvaluationUtils.GOCOMPONENT
						.equals(EvaluationUtils.CURRENTANNOTATION)) {
					EvaluationUtils.openProtein_Annotation(Resources
							.getInputStream(GO.COMPONENT));
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Please choose one annotion way!!!", "warning",
						JOptionPane.WARNING_MESSAGE);
			}

			// open the MIPS Annotation
			EvaluationUtils.open_MIPS_AnnotationCode_Annotation(Resources
					.getInputStream(MIPS.ANNOTATION));
			if (EvaluationUtils.MIPS_Annotation_Map != null) {
				if (EvaluationUtils.MIPSPROTEIN
						.equals(EvaluationUtils.CURRENTANNOTATION)) {
					EvaluationUtils.open_MIPS_Protein_Annotation(Resources
							.getInputStream(MIPS.PROTEIN));
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Please choose one annotion way!!!", "warning",
						JOptionPane.WARNING_MESSAGE);
			}
			
			// p-value
			if (EvaluationUtils.PVALUE
					.equals(EvaluationUtils.CurrentvaluationAlgorithm)) {
				PValueChart pvChart = new PValueChart("P-Value", resultPanels,
						"Cluster ID", "lg(P-Value)", "P-Value Scope",
						"Cluster Number");
				pvChart.drawChartA(resultPanels);
				pvChart.drawChartB(resultPanels);
			}
		
		
		
		} else {
			JOptionPane.showMessageDialog(null,
					"Please Choose at least One Result£¡£¡£¡", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}

		System.out.println("</ " + e.getActionCommand() + " >");
	}
}
