package org.cytoscape.CytoCluster.internal.Evaluation;

import java.util.Properties;

import org.cytoscape.CytoCluster.internal.CommonUI.MainPanel;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

public class EvaluateTask implements Task {
	private final CySwingApplication swingApplication;
	private final CyServiceRegistrar registrar;
	private final ClusterUtil cUtil;
	private final CompareAction compareAction;

	public EvaluateTask(CySwingApplication swingApplication,
			CyServiceRegistrar registrar, ClusterUtil cUtil,
			CompareAction compareAction) {
		this.swingApplication = swingApplication;
		this.registrar = registrar;
		this.cUtil = cUtil;
		this.compareAction = compareAction;
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		synchronized (this) {
			EvaluationMainPanel eMainPanel = null;
			if (eMainPanel == null) {
				/* if the emainPanel is null */
				eMainPanel = new EvaluationMainPanel(this.swingApplication,
						cUtil);
				eMainPanel.addAction(compareAction);
				this.registrar.registerService(eMainPanel,
						CytoPanelComponent.class, new Properties());
			}
			if (eMainPanel != null) {
				CytoPanel cytoPanel = this.cUtil.getControlCytoPanel();
				int index = cytoPanel.indexOfComponent(eMainPanel);
				cytoPanel.setSelectedIndex(index);
			}
		}
	}

	@Override
	public void cancel() {

	}
}
