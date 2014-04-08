package org.cytoscape.CytoCluster.internal;

import org.cytoscape.CytoCluster.internal.CommonUI.AboutDialog;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * 
 * Opens the about dialog
 * 
 * @author alvinleung
 * 
 */
public class AboutTask implements Task {
	private AboutDialog aDialog;
	private ClusterUtil clusterUtil;

	public AboutTask(ClusterUtil clusterUtil) {
		this.clusterUtil = clusterUtil;
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		aDialog = new AboutDialog(clusterUtil);
		aDialog.setLocationRelativeTo(null);
		aDialog.setVisible(true);
	}

	@Override
	public void cancel() {

	}

}
