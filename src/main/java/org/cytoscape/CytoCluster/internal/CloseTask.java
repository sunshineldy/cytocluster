package org.cytoscape.CytoCluster.internal;

import org.cytoscape.CytoCluster.internal.Analyze.CloseAllResultsTask;
import org.cytoscape.CytoCluster.internal.CommonUI.MainPanel;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluationMainPanel;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * close task
 * 
 * @author kay
 */
public class CloseTask implements Task {
	private final CloseAllResultsTask closeAllResultsTask;
	private final CyServiceRegistrar registrar;
	private final ClusterUtil mcodeUtil;

	public CloseTask(CloseAllResultsTask closeAllResultsTask,
			CyServiceRegistrar registrar, ClusterUtil mcodeUtil) {
		this.closeAllResultsTask = closeAllResultsTask;
		this.registrar = registrar;
		this.mcodeUtil = mcodeUtil;
	}

	public void run(TaskMonitor taskMonitor) throws Exception {
		if ((this.closeAllResultsTask == null)
				|| (this.closeAllResultsTask.close)) {
			MainPanel mainPanel = this.mcodeUtil.getMainPanel();
			EvaluationMainPanel evaluationMainPanel = this.mcodeUtil
					.getEvaluationMainPanel();
			if (mainPanel != null) {
				this.registrar.unregisterService(mainPanel,
						CytoPanelComponent.class);
			}
			if (evaluationMainPanel != null) {
				this.registrar.unregisterService(evaluationMainPanel,
						CytoPanelComponent.class);
			}
			this.mcodeUtil.reset();
		}
	}

	public void cancel() {

	}
}