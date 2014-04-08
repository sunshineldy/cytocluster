package org.cytoscape.CytoCluster.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.cytoscape.CytoCluster.internal.Analyze.CloseAllResultsTask;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * CloseTaskFactory
 * 
 */
public class CloseTaskFactory implements TaskFactory,
		NetworkAboutToBeDestroyedListener {

	private final CySwingApplication swingApplication;
	private final CyServiceRegistrar registrar;
	private final ClusterUtil mcodeUtil;

	public CloseTaskFactory(CySwingApplication swingApplication,
			CyServiceRegistrar registrar, ClusterUtil mcodeUtil) {
		this.swingApplication = swingApplication;
		this.registrar = registrar;
		this.mcodeUtil = mcodeUtil;
	}

	public TaskIterator createTaskIterator() {
		TaskIterator taskIterator = new TaskIterator(new Task[0]);
		Collection resultPanels = this.mcodeUtil.getResultPanels();
		CloseAllResultsTask closeResultsTask = new CloseAllResultsTask(
				this.swingApplication, this.mcodeUtil);

		System.out.println("*********resultPanels.size : "
				+ resultPanels.size());

		/* if there exits results */
		if (resultPanels.size() > 0) {
			taskIterator.append(closeResultsTask);
		}

		/* close the control panel */
		taskIterator.append(new CloseTask(closeResultsTask, this.registrar,
				this.mcodeUtil));

		System.out.println("close - getNumTasks " + taskIterator.getNumTasks());

		return taskIterator;
	}

	public boolean isReady() {
		return this.mcodeUtil.isOpened();
	}

	public void handleEvent(NetworkAboutToBeDestroyedEvent e) {
		if (this.mcodeUtil.isOpened()) {
			CyNetwork network = e.getNetwork();
			Set resultIds = this.mcodeUtil.getNetworkResults(network.getSUID()
					.longValue());
			for (Iterator localIterator = resultIds.iterator(); localIterator
					.hasNext();) {
				int id = ((Integer) localIterator.next()).intValue();
				System.out.println("************intValue" + id);
				ResultPanel panel = this.mcodeUtil.getResultPanel(id);
				if (panel != null)
					panel.discard(false);
			}
		}
	}
}