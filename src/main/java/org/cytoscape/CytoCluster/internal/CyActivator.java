package org.cytoscape.CytoCluster.internal;

import org.cytoscape.CytoCluster.internal.Analyze.AnalyzeAction;
import org.cytoscape.CytoCluster.internal.Evaluation.CompareAction;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluateAction;
import org.cytoscape.CytoCluster.internal.Evaluation.EvaluateTaskFactory;
import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.ActionEnableSupport;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.events.CytoPanelComponentSelectedListener;
import org.osgi.framework.BundleContext;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.edit.MapTableToNetworkTablesTaskFactory;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineFactory;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskManager;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.util.swing.OpenBrowser;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * the main entry to this application
 * 
 */
public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}

	public void start(BundleContext bc) {

		/**
		 * change the UI / UIManager
		 */

		// try {
		// UIManager.setLookAndFeel(UIManager
		// .getCrossPlatformLookAndFeelClassName());
		// } catch (Exception e) {
		// JOptionPane.showMessageDialog(null, e.toString());
		// }
		/***
		 * get service
		 ***/
		CyApplicationManager appMgr = (CyApplicationManager) getService(bc,
				CyApplicationManager.class);
		CyNetworkViewManager netViewMgr = (CyNetworkViewManager) getService(bc,
				CyNetworkViewManager.class);
		CyNetworkManager netMgr = (CyNetworkManager) getService(bc,
				CyNetworkManager.class);
		TaskManager taskMgr = (TaskManager) getService(bc, TaskManager.class);
		CyNetworkViewFactory netViewFactory = (CyNetworkViewFactory) getService(
				bc, CyNetworkViewFactory.class);
		CyRootNetworkManager rootNetworkMgr = (CyRootNetworkManager) getService(
				bc, CyRootNetworkManager.class);
		CySwingApplication swingApp = (CySwingApplication) getService(bc,
				CySwingApplication.class);
		RenderingEngineFactory dingRenderingEngineFactory = (RenderingEngineFactory) getService(
				bc, RenderingEngineFactory.class, "(id=ding)");
		CyServiceRegistrar serviceRegistrar = (CyServiceRegistrar) getService(
				bc, CyServiceRegistrar.class);
		VisualStyleFactory visualStyleFactory = (VisualStyleFactory) getService(
				bc, VisualStyleFactory.class);
		VisualMappingManager visualMappingMgr = (VisualMappingManager) getService(
				bc, VisualMappingManager.class);
		VisualMappingFunctionFactory discreteMappingFactory = (VisualMappingFunctionFactory) getService(
				bc, VisualMappingFunctionFactory.class,
				"(mapping.type=discrete)");
		VisualMappingFunctionFactory continuousMappingFactory = (VisualMappingFunctionFactory) getService(
				bc, VisualMappingFunctionFactory.class,
				"(mapping.type=continuous)");

		CyTableFactory cyDataTableFactoryServiceRef = getService(bc,
				CyTableFactory.class);
		MapTableToNetworkTablesTaskFactory mapNetworkAttrTFServiceRef = getService(
				bc, MapTableToNetworkTablesTaskFactory.class);

		FileUtil fileUtil = (FileUtil) getService(bc, FileUtil.class);
		OpenBrowser openBrowser = (OpenBrowser) getService(bc,
				OpenBrowser.class); // openBrowser;
		CyEventHelper eventHelper = (CyEventHelper) getService(bc,
				CyEventHelper.class);
		ClusterUtil clusterUtil = new ClusterUtil(dingRenderingEngineFactory,
				netViewFactory, rootNetworkMgr, appMgr, netMgr, netViewMgr,
				visualStyleFactory, visualMappingMgr, swingApp, eventHelper,
				discreteMappingFactory, continuousMappingFactory, fileUtil,
				mapNetworkAttrTFServiceRef, openBrowser);

		/*--------------------¡¾Analyze Action¡¿-----------------------*/
		AnalyzeAction analyzeAction = new AnalyzeAction("Analyze", appMgr,
				swingApp, netViewMgr, serviceRegistrar, taskMgr, null,
				clusterUtil);
		/*---------------------¡¾VisualStyleAction¡¿-------------------*/
		VisualStyleAction visualStyleAction = new VisualStyleAction(
				"Apply Viz style", appMgr, swingApp, netViewMgr,
				visualMappingMgr, clusterUtil);
		/*----------------------¡¾compareAction¡¿----------------------*/
		CompareAction compareAction = new CompareAction("Begin Compare",
				appMgr, swingApp, netViewMgr, null, clusterUtil);

		/*--------------------¡¾Evaluation Task¡¿----------*/
		EvaluateAction evaluateAction = new EvaluateAction("Evaluate", appMgr,
				swingApp, netViewMgr, null, taskMgr, clusterUtil,
				serviceRegistrar, compareAction);

		registerAllServices(bc, evaluateAction, new Properties());
		registerAllServices(bc, analyzeAction, new Properties());
		registerService(bc, visualStyleAction, CyAction.class, new Properties());
		registerService(bc, visualStyleAction,
				CytoPanelComponentSelectedListener.class, new Properties());

		/*-----------------¡¾ Open Task¡¿--------------*/
		// add Action to Open menu and Opentask
		OpenTaskFactory openTaskFactory = new OpenTaskFactory(swingApp,
				serviceRegistrar, clusterUtil, analyzeAction, evaluateAction);

		Properties openTaskFactoryProps = new Properties();
		openTaskFactoryProps.setProperty("preferredMenu", "Apps.CytoCluster");
		openTaskFactoryProps.setProperty("title", "Open");
		openTaskFactoryProps.setProperty("menuGravity", "1.0");
		registerService(bc, openTaskFactory, TaskFactory.class,
				openTaskFactoryProps);

		/*-----------------¡¾ Close Task¡¿--------------*/
		CloseTaskFactory closeTaskFactory = new CloseTaskFactory(swingApp,
				serviceRegistrar, clusterUtil);
		Properties closeTaskFactoryProps = new Properties();
		closeTaskFactoryProps.setProperty("preferredMenu", "Apps.CytoCluster");
		closeTaskFactoryProps.setProperty("title", "Close");
		closeTaskFactoryProps.setProperty("menuGravity", "2.0");
		registerService(bc, closeTaskFactory, TaskFactory.class,
				closeTaskFactoryProps);
		registerService(bc, closeTaskFactory,
				NetworkAboutToBeDestroyedListener.class, new Properties());

		/*-----------------¡¾ About¡¿--------------*/
		AboutTaskFactory openAboutTaskFactory = new AboutTaskFactory(
				clusterUtil);
		Properties aboutActionProps = new Properties();
		aboutActionProps.setProperty("preferredMenu", "Apps.CytoCluster");
		aboutActionProps.setProperty("title", "About");
		aboutActionProps.setProperty("menuGravity", "3.0");
		registerService(bc, openAboutTaskFactory, TaskFactory.class,
				aboutActionProps);

	}
}