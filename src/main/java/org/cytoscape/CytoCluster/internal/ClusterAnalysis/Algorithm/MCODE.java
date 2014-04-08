package org.cytoscape.CytoCluster.internal.ClusterAnalysis.Algorithm;

import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyEdge;

import java.util.*;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyEdge;

public class MCODE extends Algorithm {

	public MCODE(Long networkID, ClusterUtil clusterUtil) {
		super(networkID, clusterUtil);
	}

	@Override
	public Cluster[] run(CyNetwork inputNetwork, int resultTitle) {
		return (this.K_CoreFinder(inputNetwork, resultTitle));
	}
}
