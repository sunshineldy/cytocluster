package org.cytoscape.CytoCluster.internal.MyUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

import org.cytoscape.CytoCluster.internal.Analyze.ClusterTask;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public class NodeDistance implements ClusterTask {
	public static final int INFINITY = 2147483647;
	protected List<CyNode> nodesList;
	protected CyNetwork network;
	protected int[][] distances;
	protected boolean directed;
	protected int currentProgress;
	protected int lengthOfTask;
	protected String statusMessage;
	protected boolean done;
	protected boolean canceled;
	protected Map<Long, Integer> nodeIndexToMatrixIndexMap;

	public NodeDistance(List<CyNode> nodesList, CyNetwork network,
			Map<Long, Integer> nodeIndexToMatrixIndexMap) {
		this.nodesList = nodesList;
		this.nodeIndexToMatrixIndexMap = nodeIndexToMatrixIndexMap;
		this.network = network;
		this.distances = new int[nodesList.size()][];
		this.directed = false;
	}

	public void start(boolean return_when_done) {
		SwingWorker worker = new SwingWorker() {
			protected Object doInBackground() throws Exception {
				return new NodeDistance.NodeDistancesTask();
			}
		};
		worker.execute();
		if (return_when_done)
			try {
				worker.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	}

	public boolean wasCanceled() {
		return this.canceled;
	}

	public int getCurrentProgress() {
		return this.currentProgress;
	}

	public int getLengthOfTask() {
		return this.lengthOfTask;
	}

	public String getTaskDescription() {
		return "Calculating Node Distances";
	}

	public String getCurrentStatusMessage() {
		return this.statusMessage;
	}

	public boolean isDone() {
		return this.done;
	}

	public void stop() {
		this.canceled = true;
		this.statusMessage = null;
	}

	public int[][] calculate() {
		this.currentProgress = 0;
		this.lengthOfTask = this.distances.length;
		this.done = false;
		this.canceled = false;

		CyNode[] nodes = new CyNode[this.nodesList.size()];

		Integer[] integers = new Integer[nodes.length];

		for (int i = 0; i < nodes.length; i++) {
			CyNode from_node = (CyNode) this.nodesList.get(i);

			if (from_node != null) {
				int index = ((Integer) this.nodeIndexToMatrixIndexMap
						.get(from_node.getSUID())).intValue();

				if ((index < 0) || (index >= nodes.length)) {
					System.err.println("WARNING: GraphNode \"" + from_node
							+ "\" has an index value that is out of range: "
							+ index
							+ ".  Graph indices should be maintained such "
							+ "that no index is unused.");
					return null;
				}
				if (nodes[index] != null) {
					System.err.println("WARNING: GraphNode \"" + from_node
							+ "\" has an index value ( " + index
							+ " ) that is the same as "
							+ "that of another GraphNode ( \"" + nodes[index]
							+ "\" ).  Graph indices should be maintained such "
							+ "that indices are unique.");
					return null;
				}
				nodes[index] = from_node;
				integers[index] = Integer.valueOf(index);
			}
		}
		LinkedList queue = new LinkedList();
		boolean[] completed_nodes = new boolean[nodes.length];

		for (int from_node_index = 0; from_node_index < nodes.length; from_node_index++) {
			if (this.canceled) {
				this.distances = null;
				return this.distances;
			}

			CyNode from_node = nodes[from_node_index];

			if (from_node == null) {
				if (this.distances[from_node_index] == null) {
					this.distances[from_node_index] = new int[nodes.length];
				}

				Arrays.fill(this.distances[from_node_index], 2147483647);
			} else {
				if (this.distances[from_node_index] == null) {
					this.distances[from_node_index] = new int[nodes.length];
				}
				Arrays.fill(this.distances[from_node_index], 2147483647);
				this.distances[from_node_index][from_node_index] = 0;

				Arrays.fill(completed_nodes, false);

				queue.add(integers[from_node_index]);

				while (!queue.isEmpty()) {
					if (this.canceled) {
						this.distances = null;
						return this.distances;
					}

					int index = ((Integer) queue.removeFirst()).intValue();
					if (completed_nodes[index]) {
						completed_nodes[index] = true;

						CyNode to_node = nodes[index];
						int to_node_distance = this.distances[from_node_index][index];
						int i;
						if (index < from_node_index) {
							for (i = 0; i < nodes.length; i++) {
								if (this.distances[index][i] != 2147483647) {
									int distance_through_to_node = to_node_distance
											+ this.distances[index][i];
									if (distance_through_to_node <= this.distances[from_node_index][i]) {
										if (this.distances[index][i] == 1) {
											completed_nodes[i] = true;
										}
										this.distances[from_node_index][i] = distance_through_to_node;
									}
								}

							}

						} else {
							Collection<CyNode> neighbors = getNeighbors(to_node);

							for (CyNode neighbor : neighbors) {
								if (this.canceled) {
									this.distances = null;
									return this.distances;
								}

								int neighbor_index = ((Integer) this.nodeIndexToMatrixIndexMap
										.get(neighbor.getSUID())).intValue();

								if (nodes[neighbor_index] == null) {
									this.distances[from_node_index][neighbor_index] = 2147483647;
								} else if (completed_nodes[neighbor_index]) {
									int neighbor_distance = this.distances[from_node_index][neighbor_index];

									if ((to_node_distance != 2147483647)
											&& (neighbor_distance > to_node_distance + 1)) {
										this.distances[from_node_index][neighbor_index] = (to_node_distance + 1);
										queue.addLast(integers[neighbor_index]);
									}
								}
							}
						}
					}
				}
				this.currentProgress += 1;
				double percentDone = this.currentProgress * 100
						/ this.lengthOfTask;
				this.statusMessage = ("Completed " + percentDone + "%.");
			}
		}
		this.done = true;
		this.currentProgress = this.lengthOfTask;
		return this.distances;
	}

	public int[][] getDistances() {
		return this.distances;
	}

	private Collection<CyNode> getNeighbors(CyNode node) {
		Set result = new HashSet();
		Collection<CyEdge> edges = this.network.getAdjacentEdgeList(node,
				CyEdge.Type.ANY);

		if ((edges == null) || (edges.size() == 0))
			return result;

		Long targetID = node.getSUID();

		for (CyEdge curEdge : edges) {
			if (curEdge.getSource().getSUID() != targetID) {
				result.add(curEdge.getSource());
			} else if (curEdge.getTarget().getSUID() != targetID)
				result.add(curEdge.getTarget());
		}

		return result;
	}

	class NodeDistancesTask {
		NodeDistancesTask() {
			calculate();
		}
	}
}