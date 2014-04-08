package org.cytoscape.CytoCluster.internal;

import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class AboutTaskFactory implements TaskFactory {
		
	private ClusterUtil clusterUtil;
	public AboutTaskFactory (ClusterUtil clusterUtil){
		this.clusterUtil=clusterUtil;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new AboutTask(clusterUtil));
	}

	@Override
	public boolean isReady() {
		return true;
	}

}
