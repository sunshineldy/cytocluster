package org.cytoscape.CytoCluster.internal.Evaluation;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import org.cytoscape.CytoCluster.internal.AbstractVizAction;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.GO;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskManager;

/**
 * click the Evaluation Button On MainPanel
 * 
 * @author kay
 * 
 */
public class EvaluateAction extends AbstractVizAction {
	private static final long serialVersionUID = -5009238701078256046L;
	private TaskManager taskManager;
	private ClusterUtil clusterUtil;
	private CyServiceRegistrar registrar;
	private CySwingApplication swingApplication;
	private CompareAction compareAction;

	public EvaluateAction(String name, CyApplicationManager applicationManager,
			CySwingApplication swingApplication,
			CyNetworkViewManager netViewManager, String enableFor,
			TaskManager taskManager, ClusterUtil clusterUtil,
			CyServiceRegistrar registrar, CompareAction compareAction) {
		super(name, applicationManager, swingApplication, netViewManager,
				enableFor);
		this.swingApplication = swingApplication;
		this.taskManager = taskManager;
		this.clusterUtil = clusterUtil;
		this.registrar = registrar;
		this.compareAction = compareAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// System.out.println(Resources.getUrl(Resources.GO.ANNOTATION));
		// HashMap hsHashMap = EvaluationUtils.openAnnotation(Resources
		// .getUrl(Resources.GO.ANNOTATION));
		// for (Object key : hsHashMap.keySet()) {
		// System.out.println(key + "/" + hsHashMap.get(key));
		// }

		// InputStream is = null;
		// try {
		// is = Resources.getInputStream(Resources.GO.COMPONENT);
		// EvaluationUtils.openProtein_Annotation(is);
		// Map<String, Set<String>> hashMap =
		// EvaluationUtils.Annotation_Protein_Map
		// .get("Component");
		// System.out.println(hashMap.size());
		// for (String key : hashMap.keySet()) {
		// Set<String> set = hashMap.get(key);
		// // System.out.println("key " + key);
		// for (String string : set) {
		// System.out.print(string + "/");
		// }
		// System.out.println();
		// }
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		System.out.println(e.getActionCommand() + " / Evaluating......");

		if (EvaluationMainPanel.isExist) {
			JOptionPane.showMessageDialog(null,
					"The Evaluation Panel Exists!!", "Warnning",
					JOptionPane.WARNING_MESSAGE);

			EvaluationMainPanel eMainPanel = this.clusterUtil
					.getEvaluationMainPanel();
			eMainPanel.getAnalyzedAlgorithmOptionsPanel();
			// eMainPanel.optionsComboBox.setSelectedIndex(0);

			// get the ControlPanel and set the selected panel
			CytoPanel cytoPanel = getControlCytoPanel();
			int index = cytoPanel.indexOfComponent(eMainPanel);
			cytoPanel.setSelectedIndex(index);
			EvaluationUtils.CurrentSelectedResults.clear();
			EvaluationUtils.currentResultPanel = "";

			System.out.println("EvaluationMainPanel exist!!");

			return;
		}

		if (this.clusterUtil.getResultPanels().size() > 0) {
			EvaluateTaskFactory evaluateTaskFactory = new EvaluateTaskFactory(
					swingApplication, registrar, clusterUtil, compareAction);
			taskManager.execute(evaluateTaskFactory.createTaskIterator());
		} else {
			JOptionPane.showMessageDialog(null,
					"Please Do the Cluster Analyze Action First!!", "Warnning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
