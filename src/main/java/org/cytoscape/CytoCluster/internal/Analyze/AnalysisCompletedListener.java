package org.cytoscape.CytoCluster.internal.Analyze;

/**
 * the Listener of Analysis Task
 */
public interface AnalysisCompletedListener {
	public abstract void handleEvent(
			AnalysisCompletedEvent paramAnalysisCompletedEvent);
}
