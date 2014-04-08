package org.cytoscape.CytoCluster.internal.MyUtils;

/**
 * the set of all the parameters used in clustering
 */

public class ParameterSet {

	public static String NETWORK = "network";
	public static String SELECTION = "selection";
	private String scope;
	private Long[] selectedNodes;

	// parameter used when clustering using MCODE
	private boolean includeLoops;
	private int degreeCutoff;
	private int kCore;
	private boolean optimize;
	private int maxDepthFromStart;
	private double nodeScoreCutoff;
	private boolean fluff;
	private boolean haircut;
	private double fluffNodeDensityCutoff;
	private int defaultRowHeight;

	// parameter used when clustering using EAGLE
	private int cliqueSizeThresholdEAGLE;
	private int complexSizeThresholdEAGLE;

	// parameter used in clustering using FAG-EC
	private boolean overlapped;
	private double fThresholdFAGEC;
	private int cliqueSizeThresholdFAGEC;
	private int complexSizeThresholdFAGEC;
	private boolean isWeakFAGEC;

	// parameter used in clustering using HC-PIN
	private double fThresholdHCPIN;
	private int complexSizeThresholdHCPIN;
	private boolean isWeakHCPIN;

	// parameter used in clustering using OH-PIN
	private double fThresholdOHPIN;
	private double OverlappingScore;

	// parameter used in clustering using IPCA
	private int ShortestPathLength;
	private double TinThreshold;
	private int complexSizeThresholdIPCA;

	// result viewing parameters (only used for dialog box of results)

	public static String MCODE = "MCODE";
	public static String EAGLE = "EAGLE";
	public static String FAGEC = "FAG-EC";
	public static String HCPIN = "HC-PIN";
	public static String OHPIN = "OH-PIN";
	public static String IPCA = "IPCA";
	private String algorithm;

	// constructor
	public ParameterSet() {
		setDefaultParams();
		this.defaultRowHeight = 80;
	}

	public ParameterSet(String scope, Long[] selectedNodes,
			boolean includeLoops, int degreeCutoff, int kCore,
			boolean optimize, int maxDepthFromStart, double nodeScoreCutoff,
			boolean fluff, boolean haircut, double fluffNodeDensityCutoff,
			int cliqueSizeThresholdEAGLE, int complexSizeThresholdEAGLE,
			boolean overlapped, double fThresholdFAGEC,
			int cliqueSizeThresholdFAGEC, int complexSizeThresholdFAGEC,
			boolean isWeakFAGEC, double fThresholdHCPIN,
			int complexSizeThresholdHCPIN, boolean isWeakHCPIN,
			double fThresholdOHPIN, double OverlappingScore,
			int ShortestPathLength, double TinThreshold,
			int complexSizeThresholdIPCA, String algorithm) {

		setAllAlgorithmParams(scope, selectedNodes, includeLoops, degreeCutoff,
				kCore, optimize, maxDepthFromStart, nodeScoreCutoff, fluff,
				haircut, fluffNodeDensityCutoff, cliqueSizeThresholdEAGLE,
				complexSizeThresholdEAGLE, overlapped, fThresholdFAGEC,
				cliqueSizeThresholdFAGEC, complexSizeThresholdFAGEC,
				isWeakFAGEC, fThresholdHCPIN, complexSizeThresholdHCPIN,
				isWeakHCPIN, fThresholdOHPIN, OverlappingScore,
				ShortestPathLength, TinThreshold, complexSizeThresholdIPCA,
				algorithm);

		this.defaultRowHeight = 80;
	}

	public void setDefaultParams() {
		setAllAlgorithmParams(NETWORK, new Long[0], true, 2, 2, true, 100, 0.2,
				true, true, 0.1, 3, 3, false, 2.0, 3, 3, true, 2.0, 3, true,
				2.0, 0.5, 2, 0.5, 2, "");
	}

	// initial parameters set
	public void setAllAlgorithmParams(String scope, Long[] selectedNodes,
			boolean includeLoops, int degreeCutoff, int kCore,
			boolean optimize, int maxDepthFromStart, double nodeScoreCutoff,
			boolean fluff, boolean haircut, double fluffNodeDensityCutoff,
			int cliqueSizeThresholdEAGLE, int complexSizeThresholdEAGLE,
			boolean overlapped, double fThresholdFAGEC,
			int cliqueSizeThresholdFAGEC, int complexSizeThresholdFAGEC,
			boolean isWeakFAGEC, double fThresholdHCPIN,
			int complexSizeThresholdHCPIN, boolean isWeakHCPIN,
			double fThresholdOHPIN, double OverlappingScore,
			int ShortestPathLength, double TinThreshold,
			int complexSizeThresholdIPCA, String algorithm) {

		this.scope = scope;
		this.selectedNodes = selectedNodes;
		this.includeLoops = includeLoops;
		this.degreeCutoff = degreeCutoff;
		this.kCore = kCore;
		this.optimize = optimize;
		this.maxDepthFromStart = maxDepthFromStart;
		this.nodeScoreCutoff = nodeScoreCutoff;
		this.fluff = fluff;
		this.haircut = haircut;
		this.fluffNodeDensityCutoff = fluffNodeDensityCutoff;

		// parameter used when clustering using EAGLE
		this.cliqueSizeThresholdEAGLE = cliqueSizeThresholdEAGLE;
		this.complexSizeThresholdEAGLE = complexSizeThresholdEAGLE;

		// used in clustering using FAG-EC
		this.overlapped = overlapped;
		this.fThresholdFAGEC = fThresholdFAGEC;
		this.cliqueSizeThresholdFAGEC = cliqueSizeThresholdFAGEC;
		this.complexSizeThresholdFAGEC = complexSizeThresholdFAGEC;
		this.isWeakFAGEC = isWeakFAGEC;

		// used in clustering using HC-PIN
		this.fThresholdHCPIN = fThresholdHCPIN;
		this.complexSizeThresholdHCPIN = complexSizeThresholdHCPIN;
		this.isWeakHCPIN = isWeakHCPIN;

		// used in clustering using OH-PIN
		this.fThresholdOHPIN = fThresholdOHPIN;
		this.OverlappingScore = OverlappingScore;

		// used in clustering using IPCA
		this.ShortestPathLength = ShortestPathLength;
		this.TinThreshold = TinThreshold;
		this.complexSizeThresholdIPCA = complexSizeThresholdIPCA;

		this.algorithm = algorithm;
	}

	public ParameterSet copy() {

		ParameterSet newParam = new ParameterSet();
		newParam.setScope(this.scope);
		newParam.setSelectedNodes(this.selectedNodes);
		newParam.setIncludeLoops(this.includeLoops);
		newParam.setDegreeCutoff(this.degreeCutoff);
		newParam.setKCore(this.kCore);
		newParam.setOptimize(this.optimize);
		newParam.setMaxDepthFromStart(this.maxDepthFromStart);
		newParam.setNodeScoreCutoff(this.nodeScoreCutoff);
		newParam.setFluff(this.fluff);
		newParam.setHaircut(this.haircut);
		newParam.setFluffNodeDensityCutoff(this.fluffNodeDensityCutoff);
		newParam.setDefaultRowHeight(this.defaultRowHeight);

		newParam.setCliqueSizeThresholdEAGLE(this.cliqueSizeThresholdEAGLE);
		newParam.setComplexSizeThresholdEAGLE(this.complexSizeThresholdEAGLE);

		newParam.setOverlapped(this.overlapped);
		newParam.setfThresholdFAGEC(this.fThresholdFAGEC);
		newParam.setCliqueSizeThresholdEAGLE(this.cliqueSizeThresholdFAGEC);
		newParam.setComplexSizeThresholdFAGEC(this.complexSizeThresholdFAGEC);
		newParam.setWeakFAGEC(this.isWeakFAGEC);

		// used in clustering using HC-PIN
		newParam.setfThresholdHCPIN(this.fThresholdHCPIN);
		newParam.setComplexSizeThresholdHCPIN(this.complexSizeThresholdHCPIN);
		newParam.setWeakHCPIN(this.isWeakHCPIN);

		// used in clustering using OH-PIN
		newParam.setfThresholdOHPIN(this.fThresholdOHPIN);
		newParam.setOverlappingScore(this.OverlappingScore);

		// used in clustering using IPCA
		newParam.setShortestPathLength(this.ShortestPathLength);
		newParam.setTinThreshold(this.TinThreshold);
		newParam.setComplexSizeThresholdIPCA(this.complexSizeThresholdIPCA);

		newParam.setAlgorithm(this.algorithm);

		return newParam;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long[] getSelectedNodes() {
		return this.selectedNodes;
	}

	public void setSelectedNodes(Long[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public boolean isIncludeLoops() {
		return this.includeLoops;
	}

	public void setIncludeLoops(boolean includeLoops) {
		this.includeLoops = includeLoops;
	}

	public int getDegreeCutoff() {
		return this.degreeCutoff;
	}

	public void setDegreeCutoff(int degreeCutoff) {
		this.degreeCutoff = degreeCutoff;
	}

	public int getKCore() {
		return this.kCore;
	}

	public void setKCore(int kCore) {
		this.kCore = kCore;
	}

	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}

	public boolean isOptimize() {
		return this.optimize;
	}

	public int getMaxDepthFromStart() {
		return this.maxDepthFromStart;
	}

	public void setMaxDepthFromStart(int maxDepthFromStart) {
		this.maxDepthFromStart = maxDepthFromStart;
	}

	public double getNodeScoreCutoff() {
		return this.nodeScoreCutoff;
	}

	public void setNodeScoreCutoff(double nodeScoreCutoff) {
		this.nodeScoreCutoff = nodeScoreCutoff;
	}

	public boolean isFluff() {
		return this.fluff;
	}

	public void setFluff(boolean fluff) {
		this.fluff = fluff;
	}

	public boolean isHaircut() {
		return this.haircut;
	}

	public void setHaircut(boolean haircut) {
		this.haircut = haircut;
	}

	public double getFluffNodeDensityCutoff() {
		return this.fluffNodeDensityCutoff;
	}

	public void setFluffNodeDensityCutoff(double fluffNodeDensityCutoff) {
		this.fluffNodeDensityCutoff = fluffNodeDensityCutoff;
	}

	public int getDefaultRowHeight() {
		return this.defaultRowHeight;
	}

	public void setDefaultRowHeight(int defaultRowHeight) {
		this.defaultRowHeight = defaultRowHeight;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getCliqueSizeThresholdEAGLE() {
		return cliqueSizeThresholdEAGLE;
	}

	public void setCliqueSizeThresholdEAGLE(int cliqueSizeThresholdEAGLE) {
		this.cliqueSizeThresholdEAGLE = cliqueSizeThresholdEAGLE;
	}

	public int getComplexSizeThresholdEAGLE() {
		return complexSizeThresholdEAGLE;
	}

	public void setComplexSizeThresholdEAGLE(int complexSizeThresholdEAGLE) {
		this.complexSizeThresholdEAGLE = complexSizeThresholdEAGLE;
	}

	public boolean isOverlapped() {
		return overlapped;
	}

	public void setOverlapped(boolean overlapped) {
		this.overlapped = overlapped;
	}

	public double getfThresholdFAGEC() {
		return fThresholdFAGEC;
	}

	public void setfThresholdFAGEC(double fThresholdFAGEC) {
		this.fThresholdFAGEC = fThresholdFAGEC;
	}

	public int getCliqueSizeThresholdFAGEC() {
		return cliqueSizeThresholdFAGEC;
	}

	public void setCliqueSizeThresholdFAGEC(int cliqueSizeThresholdFAGEC) {
		this.cliqueSizeThresholdFAGEC = cliqueSizeThresholdFAGEC;
	}

	public int getComplexSizeThresholdFAGEC() {
		return complexSizeThresholdFAGEC;
	}

	public void setComplexSizeThresholdFAGEC(int complexSizeThresholdFAGEC) {
		this.complexSizeThresholdFAGEC = complexSizeThresholdFAGEC;
	}

	public boolean isWeakFAGEC() {
		return isWeakFAGEC;
	}

	public void setWeakFAGEC(boolean isWeakFAGEC) {
		this.isWeakFAGEC = isWeakFAGEC;
	}

	public double getfThresholdHCPIN() {
		return fThresholdHCPIN;
	}

	public void setfThresholdHCPIN(double fThresholdHCPIN) {
		this.fThresholdHCPIN = fThresholdHCPIN;
	}

	public int getComplexSizeThresholdHCPIN() {
		return complexSizeThresholdHCPIN;
	}

	public void setComplexSizeThresholdHCPIN(int complexSizeThresholdHCPIN) {
		this.complexSizeThresholdHCPIN = complexSizeThresholdHCPIN;
	}

	public boolean isWeakHCPIN() {
		return isWeakHCPIN;
	}

	public void setWeakHCPIN(boolean isWeakHCPIN) {
		this.isWeakHCPIN = isWeakHCPIN;
	}

	public double getfThresholdOHPIN() {
		return fThresholdOHPIN;
	}

	public void setfThresholdOHPIN(double fThresholdOHPIN) {
		this.fThresholdOHPIN = fThresholdOHPIN;
	}

	public double getOverlappingScore() {
		return OverlappingScore;
	}

	public void setOverlappingScore(double overlappingScore) {
		OverlappingScore = overlappingScore;
	}

	public int getShortestPathLength() {
		return ShortestPathLength;
	}

	public void setShortestPathLength(int shortestPathLength) {
		ShortestPathLength = shortestPathLength;
	}

	public double getTinThreshold() {
		return TinThreshold;
	}

	public void setTinThreshold(double tinThreshold) {
		TinThreshold = tinThreshold;
	}

	public int getComplexSizeThresholdIPCA() {
		return complexSizeThresholdIPCA;
	}

	public void setComplexSizeThresholdIPCA(int complexSizeThresholdIPCA) {
		this.complexSizeThresholdIPCA = complexSizeThresholdIPCA;
	}

	public String toString() {
		String lineSep = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		sb.append("   Network Scoring:" + lineSep + "      Include Loops: "
				+ this.includeLoops + "  Degree Cutoff: " + this.degreeCutoff
				+ lineSep);
		sb.append("   Cluster Finding:"
				+ lineSep
				+ "      Node Score Cutoff: "
				+ this.nodeScoreCutoff
				+ "  Haircut: "
				+ this.haircut
				+ "  Fluff: "
				+ this.fluff
				+ (this.fluff ? "  Fluff Density Cutoff "
						+ this.fluffNodeDensityCutoff : "") + "  K-Core: "
				+ this.kCore + "  Max. Depth from Seed: "
				+ this.maxDepthFromStart + lineSep);
		return sb.toString();
	}
}
