package org.cytoscape.CytoCluster.internal.Evaluation;

import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class EvaluateTaskFactory implements TaskFactory {
	private final CySwingApplication swingApplication;
	private final CyServiceRegistrar registrar;
	private final ClusterUtil cUtil;
	private final CompareAction compareAction;

	public EvaluateTaskFactory(CySwingApplication swingApplication,
			CyServiceRegistrar registrar, ClusterUtil cUtil,
			CompareAction compareAction) {
		this.swingApplication = swingApplication;
		this.registrar = registrar;
		this.cUtil = cUtil;
		this.compareAction = compareAction;
	}

	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new EvaluateTask(swingApplication, registrar,
				cUtil, compareAction));
	}

	@Override
	public boolean isReady() {
		return !this.cUtil.isOpened();
	}
}
