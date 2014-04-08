package org.cytoscape.CytoCluster.internal.Analyze;

import java.util.List;

import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;

/**
 * the Analysis Task is completed
 */
public class AnalysisCompletedEvent {
	private final boolean successful;
	private final List<Cluster> clusters;

	public AnalysisCompletedEvent(boolean successful, List<Cluster> clusters) {
		this.successful = successful;
		this.clusters = clusters;
	}

	public boolean isSuccessful() {
		return this.successful;
	}

	public List<Cluster> getClusters() {
		return this.clusters;
	}
}