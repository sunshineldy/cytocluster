package org.cytoscape.CytoCluster.internal;

import java.util.Properties;

import org.cytoscape.CytoCluster.internal.Analyze.AnalyzeAction;
import org.cytoscape.CytoCluster.internal.CommonUI.MainPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluateAction;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

public class OpenTask implements Task {
	private final CySwingApplication swingApplication;
	private final CyServiceRegistrar registrar;
	private final ClusterUtil mcodeUtil;
	private final AnalyzeAction analyzeAction;
	private final EvaluateAction evaluateAction;

	public OpenTask(CySwingApplication swingApplication,
			CyServiceRegistrar registrar, ClusterUtil mcodeUtil,
			AnalyzeAction analyzeAction, EvaluateAction evaluateAction) {
		this.swingApplication = swingApplication;
		this.registrar = registrar;
		this.mcodeUtil = mcodeUtil;
		this.analyzeAction = analyzeAction;
		this.evaluateAction = evaluateAction;
	}

	public void run(TaskMonitor taskMonitor) throws Exception {
		synchronized (this) {
			MainPanel mainPanel = null;
			/* if the mainPanel is null */
			if (!this.mcodeUtil.isOpened()) {
				mainPanel = new MainPanel(this.swingApplication, this.mcodeUtil);
				mainPanel.addAction(this.analyzeAction);
				mainPanel.addAction(this.evaluateAction);// evaluation
				this.registrar.registerAllServices(mainPanel, new Properties());
				this.analyzeAction.updateEnableState();
				this.evaluateAction.updateEnableState();

			} else {
				mainPanel = this.mcodeUtil.getMainPanel();
			}
			if (mainPanel != null) {
				CytoPanel cytoPanel = this.mcodeUtil.getControlCytoPanel();
				int index = cytoPanel.indexOfComponent(mainPanel);
				// System.out.println("\n\nmainPanel's index = " + index);
				cytoPanel.setSelectedIndex(index);
			}
		}
	}

	public void cancel() {

	}
}
