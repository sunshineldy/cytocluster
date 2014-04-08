package org.cytoscape.CytoCluster.internal.Evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.CytoCluster.internal.MyUtils.Cluster;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.GO;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;

public class EvaluationUtils {

	// algorithms
	public static final String DENSITY = "Density";
	public static final String SIZEDISTRIBUTION = "SizeDistribution";
	public static final String PVALUE = "P-Value";
	public static final String PRECISION = "Precision";
	public static final String RECALL = "Recall";
	public static final String FMEASURE = "F-Measure";
	public static final String SENSITIVITY_SPECIFICITY = "Sensitivity/Specificity";
	public static final String KNOWNCOMPLEXESCMP = "KnownComplexesComparison";

	public static List<ResultPanel> CurrentSelectedResults = new ArrayList<ResultPanel>();
	public static String CurrentvaluationAlgorithm = "";
	public static HashMap<String, JTable> tableHashMap = new HashMap<String, JTable>();
	public static String currentResultPanel = "";

	// GO-MIPS
	public static final String GOANNOTATION = "GOAnnotation";
	public static final String GOCOMPONENT = "Component";
	public static final String GOFUNCTION = "Function";
	public static final String MIPSANNOTATION = "MIPSAnnotation";
	public static final String GOPROCESS = "Process";
	public static final String MIPSPROTEIN = "MIPS";
	public static final String OTHER_P_F = "Other_P_F";
	public static final String OTHER_F_A = "Other_F_A";
	public static String CURRENTANNOTATION = "";

	// maps
	public static HashMap<String, HashMap<String, Set<String>>> Annotation_Protein_Map = new HashMap<String, HashMap<String, Set<String>>>();
	public static HashMap<String, HashMap<String, Set<String>>> Protein_Annotation_Map = new HashMap<String, HashMap<String, Set<String>>>();
	public static HashMap<String, String> GO_Annotation_Map = new HashMap<String, String>();
	public static HashMap<String, String> MIPS_Annotation_Map = new HashMap<String, String>();

	/**
	 * export JTable to excel
	 * 
	 * @param table
	 *            the table needs to export
	 */
	public static void exportTable(JTable table) {
		TableModel model = table.getModel();
		BufferedWriter bWriter;
		JFileChooser jfc = new JFileChooser();
		jfc.setLocation(40, 150);
		// jfc.setVisible(true);
		File file = null;
		int returnVal = jfc.showDialog(null, "Save Table");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = jfc.getSelectedFile();
			try {
				bWriter = new BufferedWriter(new FileWriter(file));

				for (int i = 0; i < model.getColumnCount(); i++) {
					bWriter.write(model.getColumnName(i));
					bWriter.write("\t");
				}
				bWriter.newLine();
				for (int i = 0; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++) {
						bWriter.write(model.getValueAt(i, j).toString());
						bWriter.write("\t");
					}
					bWriter.newLine();
				}
				bWriter.close();
				JOptionPane.showMessageDialog(null, "Sucessfully!", "Message",
						JOptionPane.CANCEL_OPTION);
			} catch (Exception e) {
			}
			System.out.println("write out to: " + file);
		}
	}

	/**
	 * calculate the Combination / hints: n > m
	 * 
	 * @param n
	 * @param m
	 * @return C(m,n)
	 */
	public static BigInteger getCombination(int n, int m) {

		BigInteger valueN = BigInteger.ONE;
		BigInteger valueM = BigInteger.ONE;
		BigInteger nCopy = BigInteger.valueOf(n);

		for (int i = 1; i <= m; i++) {
			valueN = valueN.multiply(nCopy);
			valueM = valueM.multiply(BigInteger.valueOf(i));
			nCopy = nCopy.subtract(BigInteger.ONE);// nCopy--

			System.out.println("" + valueN + "/" + valueM + "/" + nCopy);
		}

		return valueN.divide(valueM);
	}

	/**
	 * calaulate the P-value [parameters: N/C/K/F]
	 * 
	 * @param total_protein_N
	 *            the network proteins num
	 * @param complex_protein_C
	 *            the complex's protein num
	 * @param complex_funs_K
	 *            the num of proteins in the complex when has assumptive
	 *            function
	 * @param net_funs_F
	 *            the num of proteins in the network when has assumptive
	 *            function
	 * @return P-value
	 */
	public static double CalPvalue(int total_protein_N, int complex_protein_C,
			int complex_funs_K, int net_funs_F) {
		BigDecimal result = BigDecimal.ZERO;
		for (int i = 0; i < complex_funs_K; i++) {
			// numerator - 分子
			BigInteger numerator = getCombination(net_funs_F, i).multiply(
					getCombination(total_protein_N - net_funs_F,
							complex_protein_C - i));
			// denominator 分母
			BigInteger denominator = getCombination(total_protein_N,
					complex_protein_C);

			BigDecimal a = new BigDecimal(numerator.toString());
			BigDecimal b = new BigDecimal(denominator.toString());

			a = a.setScale(100);
			b = b.setScale(100);
			a = a.divide(b, 0);
			result = result.add(a);
		}
		// P-Value = 1- result
		BigDecimal pValue = BigDecimal.ONE;
		pValue = pValue.subtract(result);
		return pValue.doubleValue();
	}

	/**
	 * 
	 * @param is
	 *            the GO.annotation InputStream
	 * @return store data in the map
	 */
	public static void open_GO_AnnotationCode_Annotation(InputStream is) {
		// Scanner scanner = null;
		// HashMap annotataionMap = new HashMap();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			while (str != null) {
				if (!str.equals("")) {
					String code = str.substring(0, str.indexOf(" ")).trim();
					String annoation = str.substring(code.length(),
							str.length()).trim();
					EvaluationUtils.GO_Annotation_Map.put(code, annoation);
				}
				str = br.readLine();
			}
			is.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			EvaluationUtils.GO_Annotation_Map.clear();
		}
		// return annotataionMap;
	}

	/**
	 * 
	 * @param is
	 *            the MIPS.annotation InputStream
	 * @return store data in the map
	 */
	public static void open_MIPS_AnnotationCode_Annotation(InputStream is) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			// System.out.println(str);
			Scanner scanner = null;
			while (str != null) {
				if (!str.equals("")) {
					scanner = new Scanner(str);
					String code = scanner.next().trim();
					// String code = str.substring(0,
					// str.indexOf("[ \t]")).trim();
					String annoation = str.substring(code.length(),
							str.length()).trim();
					System.out.println(code + "/" + code.length());
					// System.out.println(annoation + annoation.length());
					EvaluationUtils.MIPS_Annotation_Map.put(code, annoation);
				}
				str = br.readLine();
			}
			is.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			// EvaluationUtils.MIPS_Annotation_Map.clear();
		}
		// return annotataionMap;
	}

	/**
	 * building the map annotation -> protein && building the map protein
	 * ->annotation
	 * 
	 * @param is
	 *            the protein_annotationcode InputStream
	 */
	public static void openProtein_Annotation(InputStream is) {
		HashMap<String, Set<String>> annotataion_protein_Map = new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> protein_annotataion_Map = new HashMap<String, Set<String>>();
		String annotationType = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			Scanner scanner = null;
			while (str != null) {
				scanner = new Scanner(str);
				// proteins annoationCode type
				String[] proteins = scanner.next().trim().split("[|]");// proteins
				String annoationCode = scanner.next().trim();
				annotationType = scanner.next();

				/**
				 * annotation -> protein
				 */
				// add protein in a set
				HashSet<String> set = new HashSet<String>();
				for (String protein : proteins) {
					// all proteins' chars are upper case
					set.add(protein.toUpperCase());
				}
				// one annotation code maps many proteins
				if (annotataion_protein_Map.containsKey(annoationCode)) {
					// System.out.println(annoationCode);
					Set<String> hasSet = annotataion_protein_Map
							.get(annoationCode);
					hasSet.addAll(set);
					annotataion_protein_Map.put(annoationCode, hasSet);
				} else {
					annotataion_protein_Map.put(annoationCode, set);
				}

				/**
				 * protein -> annotation
				 */
				// add protein in a set
				HashSet<String> s = new HashSet<String>();
				for (String protein : proteins) {
					// one annotation code maps many proteins
					Set<String> annSet = new HashSet<String>();
					annSet.add(annoationCode);

					if (protein_annotataion_Map.containsKey(protein.toUpperCase())) {
						// System.out.println(annoationCode);
						Set<String> hSet = protein_annotataion_Map.get(protein.toUpperCase());
						hSet.addAll(annSet);
						protein_annotataion_Map.put(annoationCode, hSet);
					} else {
						protein_annotataion_Map.put(protein.toUpperCase(), annSet);
					}
				}

				if (annotationType.equals("F")) {
					EvaluationUtils.Annotation_Protein_Map
							.put(EvaluationUtils.GOFUNCTION,
									annotataion_protein_Map);
					EvaluationUtils.Protein_Annotation_Map
							.put(EvaluationUtils.GOFUNCTION,
									protein_annotataion_Map);
					annotationType = EvaluationUtils.GOFUNCTION;
				}
				if (annotationType.equals("C")) {
					EvaluationUtils.Annotation_Protein_Map.put(
							EvaluationUtils.GOCOMPONENT,
							annotataion_protein_Map);
					EvaluationUtils.Protein_Annotation_Map.put(
							EvaluationUtils.GOCOMPONENT,
							protein_annotataion_Map);
					annotationType = EvaluationUtils.GOCOMPONENT;
				}
				if (annotationType.equals("P")) {
					EvaluationUtils.Annotation_Protein_Map.put(
							EvaluationUtils.GOPROCESS, annotataion_protein_Map);
					EvaluationUtils.Protein_Annotation_Map.put(
							EvaluationUtils.GOPROCESS, protein_annotataion_Map);
					annotationType = EvaluationUtils.GOPROCESS;
				}
				str = br.readLine();
			}
			is.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			annotataion_protein_Map.clear();
			EvaluationUtils.Annotation_Protein_Map.put(annotationType, null);
		}
	}

	/**
	 * building the map annotation -> protein && building the map protein
	 * ->annotation
	 * 
	 * @param is
	 *            the protein_annotationcode InputStream
	 */
	public static void open_MIPS_Protein_Annotation(InputStream is) {
		HashMap<String, Set<String>> annotataion_protein_Map = new HashMap<String, Set<String>>();
		HashMap<String, Set<String>> protein_annotataion_Map = new HashMap<String, Set<String>>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			Scanner scanner = null;
			while (str != null) {
				scanner = new Scanner(str);
				// proteins annoationCode type
				String protein = scanner.next().trim();// proteins
				String annoationCode = scanner.next().trim();

				/**
				 * annotation -> protein
				 */
				// add protein in a set
				HashSet<String> set = new HashSet<String>();
				// all proteins' chars are upper case
				set.add(protein.toUpperCase());

				// one annotation code maps many proteins
				if (annotataion_protein_Map.containsKey(annoationCode)) {
					// System.out.println(annoationCode);
					Set<String> hasSet = annotataion_protein_Map
							.get(annoationCode);
					hasSet.addAll(set);
					annotataion_protein_Map.put(annoationCode, hasSet);
				} else {
					annotataion_protein_Map.put(annoationCode, set);
				}

				/**
				 * protein -> annotation
				 */
				// add protein in a set
				HashSet<String> s = new HashSet<String>();

				// one annotation code maps many proteins
				Set<String> annSet = new HashSet<String>();
				annSet.add(annoationCode);

				if (protein_annotataion_Map.containsKey(protein.toUpperCase())) {
					// System.out.println(annoationCode);
					Set<String> hSet = protein_annotataion_Map.get(protein.toUpperCase());
					hSet.addAll(annSet);
					protein_annotataion_Map.put(annoationCode, hSet);
				} else {
					protein_annotataion_Map.put(protein.toUpperCase(), annSet);
				}

				EvaluationUtils.Annotation_Protein_Map.put(
						EvaluationUtils.MIPSPROTEIN, annotataion_protein_Map);
				EvaluationUtils.Protein_Annotation_Map.put(
						EvaluationUtils.MIPSPROTEIN, protein_annotataion_Map);

				str = br.readLine();
			}
			is.close();
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			annotataion_protein_Map.clear();
			EvaluationUtils.Annotation_Protein_Map.put(
					EvaluationUtils.MIPSPROTEIN, null);
		}
	}

	/**
	 * calculate the P-value and stored in a Vector
	 * 
	 * @param resultPanel
	 *            the result panel
	 * @param functionProtein
	 *            the HashMap<String, Set<String>> function code -> protein
	 * @param proteinFunction
	 *            the HashMap<String, Set<String>> protein -> function code
	 * @param funcode_annotation_Map
	 *            the HashMap<String, String> function code -> function
	 * @return Vector<Cluster_Pvalue>
	 */
	public static Vector<Cluster_Pvalue> getPValue_ClusterList(
			ResultPanel resultPanel,
			HashMap<String, Set<String>> functionProtein,
			HashMap<String, Set<String>> proteinFunction,
			HashMap<String, String> funcode_annotation_Map) {

		Vector<Cluster_Pvalue> PValue_ClusterList = new Vector<Cluster_Pvalue>();
		Set<String> funlist = new HashSet<String>();
		int complex_protein = 0;
		// total_protein = GraphInfo.nodelist.size();
		int complex_fun = 0;
		int network_fun = 0;
		double min_value = 100.0D;
		String function = "";

		List<Cluster> clusters = resultPanel.getClusters();
		int total_protein = clusters.get(0).getNetwork().getRootNetwork()
				.getNodeCount();
		for (int i = 0; i < clusters.size(); i++) {

			/**
			 * reset the variables
			 */
			complex_protein = 0;
			complex_fun = 0;
			network_fun = 0;
			min_value = 100.0D;
			function = "";
			int c_f = 0;
			int n_f = 0;
			funlist.clear();

			Cluster cluster = clusters.get(i);
			ArrayList<CyNode> nodes = (ArrayList<CyNode>) cluster.getNetwork()
					.getNodeList();
			complex_protein = cluster.getNetwork().getNodeCount();

			/**
			 * get all-functions list of this cluster
			 */
			for (int j = 0; j < nodes.size(); j++) {
				/**
				 * get the protein name
				 */
				// ?? 如何通过suid获得蛋白质的字符串标识符
				CyNode node = nodes.get(j);
				CyRow row = cluster.getNetwork().getRow(node);
				String nodeID = node.getSUID().toString();
				String protein = "";
				if (row.isSet("name"))
					protein = (String) row.get("name", String.class);

				if (proteinFunction.get(protein) != null) {
					funlist.addAll((Collection) proteinFunction.get(protein));
				}
			}

			/**
			 * calculate the p-value for each function
			 */
			Iterator<String> it = funlist.iterator();
			while (it.hasNext()) {
				String assumed_function = it.next();
				complex_fun = 0;

				/**
				 * calculate the num of this assumed_function in the current
				 * complexes
				 */
				for (int l = 0; l < nodes.size(); l++) {
					/**
					 * get the protein name
					 */
					CyNode node = nodes.get(l);
					CyRow row = cluster.getNetwork().getRow(node);
					// String nodeID = node.getSUID().toString();
					String protein = "";
					if (row.isSet("name"))
						protein = (String) row.get("name", String.class);

					Set s = (Set) proteinFunction.get(protein);
					if ((s != null) && (s.contains(assumed_function))) {
						complex_fun++;
					}
				}
				network_fun = ((Set<String>) functionProtein
						.get(assumed_function)).size();

				double c_p = CalPvalue(total_protein, complex_protein,
						complex_fun, network_fun);

				/**
				 * get the min p-value
				 */
				if (c_p <= min_value) {
					c_f = complex_fun;
					n_f = network_fun;
					min_value = c_p;
					function = assumed_function;
				}
			}

			/**
			 * calculate the tp/fp/fn/precision/recall/f-measure
			 */
			int tp = c_f;
			int fp = complex_protein - c_f;
			int fn = n_f - c_f;
			double precision = 0.0;
			double recall = 0.0;
			double f_measure = 0.0;
			if (complex_protein != 0) {
				precision = tp / (tp + fp);
				recall = tp / (tp + fn);
				if (precision + recall != 0.0)
					f_measure = (2 * precision * recall) / (precision + recall);
			}

			/**
			 * store the values in a Cluster_Pvalue class
			 */
			Cluster_Pvalue cp = new Cluster_Pvalue();
			cp.setProbableFunctions(funlist.size());
			cp.setCluster(cluster);
			cp.setClusterID("Cluster" + i);
			cp.setFunProtein(c_f);
			cp.setNetProtein(n_f);
			cp.setPrecision(precision);
			cp.setTp(tp);
			cp.setFp(fp);
			cp.setFn(fn);
			cp.setRecall(recall);
			cp.setMeasure(f_measure);
			cp.setFunctionCode(function);
			String ff = (String) funcode_annotation_Map.get(function);
			if (ff != null && !ff.equals(""))
				cp.setFunction(ff);
			// cp.setProteins(tempv);
			if (funlist.size() == 0)
				cp.setPvalue(0.0D);
			else
				cp.setPvalue(min_value);
			PValue_ClusterList.add(cp);
		}
		return PValue_ClusterList;
	}

	public static void main(String[] args) {

		EvaluationUtils.open_MIPS_Protein_Annotation(Resources
				.getInputStream(Resources.MIPS.PROTEIN));
		System.out.println(EvaluationUtils.Annotation_Protein_Map
				.get(EvaluationUtils.MIPSPROTEIN).keySet().size());

		// EvaluationUtils.open_GO_AnnotationCode_Annotation(Resources
		// .getInputStream(Resources.GO.ANNOTATION));
		// System.out.println(EvaluationUtils.GO_Annotation_Map.keySet().size());

		// InputStream is = null;
		// try {
		// is = Resources.class.getResourceAsStream(GO.PROCESS.toString());
		// EvaluationUtils.openProtein_Annotation(is);
		Map<String, Set<String>> hashMap = EvaluationUtils.Protein_Annotation_Map
				.get(EvaluationUtils.MIPSPROTEIN);

		// System.out.println(hashMap.size());
		// System.out.println(hashMap.get("YOL126C").size());
		for (String string : hashMap.get("YFR040W")) {
			System.out.println(string + "/" );
		}
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}
