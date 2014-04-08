package org.cytoscape.CytoCluster.internal.MyUtils;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cytoscape.CytoCluster.internal.ClusterAnalysis.Algorithm.Algorithm;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;

public class Cluster {
	private int complexID;
	private ArrayList alNodes = null;
	private Long seedNode;
	private Map<Long, Boolean> nodeSeenHashMap;
	private double clusterScore;
	private String clusterName;
	private int rank;
	private int resultTitle;
	private int inDegree;
	private int totalDegree;
	private boolean mergeable;
	private boolean module;
	private double modularity;
	private int resultId;
	private ClusterGraph graph;
	private List<Long> alCluster;
	private CyNetworkView view;
	private double score;
	private Image image;
	private boolean disposed;
	private Algorithm algorithm;
	private String name;
	private int add;
	private Long edge;
	private int flag;

	public Cluster() {
		this.inDegree = 0;
		this.totalDegree = 0;
	}

	public Cluster(Long edge) {
		this.add = 1;
		this.inDegree = 0;
		this.totalDegree = 0;
		this.flag = 1;
	}

	public Cluster(int resultId, Long seedNode, ClusterGraph graph,
			double score, List<Long> alCluster,
			Map<Long, Boolean> nodeSeenHashMap) {
		assert (seedNode != null);
		assert (graph != null);
		assert (alCluster != null);
		assert (nodeSeenHashMap != null);

		this.resultId = resultId;
		this.seedNode = seedNode;
		this.graph = graph;
		setScore(score);
		this.alCluster = alCluster;
		this.nodeSeenHashMap = nodeSeenHashMap;
	}

	public Cluster(int ID) {
		this.alNodes = new ArrayList();
		this.complexID = ID;
		this.inDegree = 0;
		this.totalDegree = 0;
		this.mergeable = true;
		this.module = false;
		this.modularity = 0.0D;
	}

	public int getComplexID() {
		return this.complexID;
	}

	public void setComplexID(int complexID) {
		this.complexID = complexID;
	}

	public int getResultTitle() {
		return this.resultTitle;
	}

	public void setResultTitle(int resultTitle) {
		this.resultTitle = resultTitle;
	}

	public String getClusterName() {
		return this.clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public double getClusterScore() {
		return this.clusterScore;
	}

	public void setClusterScore(double clusterScore) {
		this.clusterScore = clusterScore;
	}

	public ArrayList getALNodes() {
		return this.alNodes;
	}

	public void setALNodes(ArrayList alNodes) {
		this.alNodes = alNodes;
	}

	public Long getSeedNode() {
		return this.seedNode;
	}

	public void setSeedNode(Long seedNode) {
		this.seedNode = seedNode;
	}

	public Map<Long, Boolean> getNodeSeenHashMap() {
		return this.nodeSeenHashMap;
	}

	public void setNodeSeenHashMap(Map<Long, Boolean> nodeSeenHashMap) {
		this.nodeSeenHashMap = nodeSeenHashMap;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
		this.clusterName = ("Complex " + (rank + 1));
	}

	public int getInDegree() {
		return this.inDegree;
	}

	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	public boolean isMergeable() {
		return this.mergeable;
	}

	public void setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
	}

	public boolean isModule() {
		return this.module;
	}

	public void setModule(boolean module) {
		this.module = module;
	}

	public int getTotalDegree() {
		return this.totalDegree;
	}

	public void setTotalDegree(int totalDegree) {
		this.totalDegree = totalDegree;
	}

	public double getModularity() {
		return this.modularity;
	}

	public void setModularity(double modularity) {
		this.modularity = modularity;
	}

	public void calModularity(CyNetwork currentNetwork) {
		int inDegree = 0;
		int totalDegree = 0;
		ArrayList nodes = getALNodes();
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			Long node = Long.valueOf(((Long) it.next()).longValue());

			totalDegree += Algorithm.getNodeDegree(currentNetwork, node);

			Long[] neighbors = Algorithm.getNeighborArray(currentNetwork, node);

			for (int i = 0; i < neighbors.length; i++)
				if (nodes.contains(new Long(neighbors[i].longValue())))
					inDegree++;
		}
		int outDegree = totalDegree - inDegree;
		inDegree /= 2;
		setInDegree(inDegree);
		setTotalDegree(totalDegree);
		double fModule = 0.0D;
		if (inDegree != 0 && outDegree != 0)
			fModule = inDegree / outDegree;
		else
			fModule = 0.0D;
		setModularity(fModule);
	}

	public ClusterGraph getGraph() {
		return this.graph;
	}

	public void setGraph(ClusterGraph graph) {
		this.graph = graph;
	}

	public List<Long> getAlCluster() {
		return this.alCluster;
	}

	public void setAlCluster(List<Long> alCluster) {
		this.alCluster = alCluster;
	}

	public CyNetworkView getView() {
		return this.view;
	}

	public void setView(CyNetworkView view) {
		this.view = view;
	}

	public synchronized void dispose() {
		if (isDisposed())
			return;
		if (this.view != null)
			this.view.dispose();
		this.graph.dispose();
		this.disposed = true;
	}

	public boolean isDisposed() {
		return this.disposed;
	}

	public void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}

	public synchronized CySubNetwork getNetwork() {
		return this.graph.getSubNetwork();
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getAdd() {
		return this.add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getFlag() {
		return this.flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void addnode(Long suid) {
		this.alNodes.add(suid);
	}

	public ArrayList getAlNodes() {
		return this.alNodes;
	}

	public void setAlNodes(ArrayList alNodes) {
		this.alNodes = alNodes;
	}

	public int getResultId() {
		return this.resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Long getEdge() {
		return this.edge;
	}

	public void setEdge(Long edge) {
		this.edge = edge;
	}
}