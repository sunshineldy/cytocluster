package org.cytoscape.CytoCluster.internal.Evaluation.JfreeCharts;

import java.util.List;

import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;

public class PrecisionChart extends BasicEvaluation{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8267399360756035859L;

	public PrecisionChart(String applicationTitle,
			List<ResultPanel> resultPanels, String figureA_X_Alias,
			String figureA_Y_Alias, String figureB_X_Alias,
			String figureB_Y_Alias) {
		super(applicationTitle, resultPanels, figureA_X_Alias, figureA_Y_Alias,
				figureB_X_Alias, figureB_Y_Alias);
		// TODO Auto-generated constructor stub
	}

}
