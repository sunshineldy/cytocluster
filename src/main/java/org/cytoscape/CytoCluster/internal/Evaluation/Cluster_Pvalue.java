package org.cytoscape.CytoCluster.internal.Evaluation;

import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;

public class Cluster_Pvalue implements Comparable {
	private Cluster cluster;
	private String clusterID;
	private double precision;
	private double pvalue;
	private double recall;
	private double measure;
	private int funProtein;
	private int netProtein;
	private String functionCode;
	private String function = "";
	private int probableFunctions;
	public static String comparePara = "P-value";
	private int tp;
	private int fp;
	private int fn;

	public Cluster_Pvalue() {

	}

	public int getTp() {
		return tp;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getFp() {
		return fp;
	}

	public void setFp(int fp) {
		this.fp = fp;
	}

	public int getFn() {
		return fn;
	}

	public void setFn(int fn) {
		this.fn = fn;
	}

	public int getProbableFunctions() {
		return this.probableFunctions;
	}

	public void setProbableFunctions(int probableFunctions) {
		this.probableFunctions = probableFunctions;
	}

	public String getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public int getFunProtein() {
		return this.funProtein;
	}

	public void setFunProtein(int funProtein) {
		this.funProtein = funProtein;
	}

	public int getNetProtein() {
		return this.netProtein;
	}

	public void setNetProtein(int netProtein) {
		this.netProtein = netProtein;
	}

	public double getPrecision() {
		return this.funProtein / this.cluster.getNetwork().getNodeCount();
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getPvalue() {
		return this.pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}

	public double getRecall() {
		return this.funProtein / this.netProtein;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getMeasure() {
		return 2.0D * getRecall() * getPrecision()
				/ (getRecall() + getPrecision());
	}

	public void setMeasure(double measure) {
		this.measure = measure;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public static String getComparePara() {
		return comparePara;
	}

	public static void setComparePara(String comparePara) {
		Cluster_Pvalue.comparePara = comparePara;
	}

	public String getClusterID() {
		return clusterID;
	}

	public void setClusterID(String clusterID) {
		this.clusterID = clusterID;
	}

	public int compareTo(Object o) {
		Cluster_Pvalue c = (Cluster_Pvalue) o;
		if (comparePara.equals("P-value"))
			return new Double(getPvalue()).compareTo(new Double(c.getPvalue()));
		if (comparePara.equals("Precision"))
			return new Double(getPrecision()).compareTo(new Double(c
					.getPrecision()));
		if (comparePara.equals("Recall"))
			return new Double(getRecall()).compareTo(new Double(c.getRecall()));
		if (comparePara.equals("f-measure"))
			return new Double(getMeasure())
					.compareTo(new Double(c.getMeasure()));
		return 1;
	}
}