package org.cytoscape.CytoCluster.internal;

import java.util.*;

import org.cytoscape.CytoCluster.internal.CommonUI.MainPanel;
import org.cytoscape.CytoCluster.internal.CommonUI.ResultPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.*;
import org.cytoscape.view.model.CyNetworkViewManager;

/**
 * AbstractVizAction
 */
public abstract class AbstractVizAction extends AbstractCyAction {

	private static final long serialVersionUID = 0xbb92c5989a7b4b9L;
	protected final CySwingApplication swingApplication;
	protected final CyApplicationManager applicationManager;
	protected final CyNetworkViewManager netViewManager;

	public AbstractVizAction(String name,
			CyApplicationManager applicationManager,
			CySwingApplication swingApplication,
			CyNetworkViewManager netViewManager, String enableFor) {

		super(name, applicationManager, enableFor, netViewManager);
		this.applicationManager = applicationManager;
		this.swingApplication = swingApplication;
		this.netViewManager = netViewManager;
	}

	/**
	 * get the CytoPanel from the WEST location
	 * 
	 * @return
	 */
	protected CytoPanel getControlCytoPanel() {
		return swingApplication.getCytoPanel(CytoPanelName.WEST);
	}

	/**
	 * get the CytoPanel from the ESAT location
	 * 
	 * @return
	 */
	protected CytoPanel getResultsCytoPanel() {
		return swingApplication.getCytoPanel(CytoPanelName.EAST);
	}

	/**
	 * get the MainPanel from the controlPanel
	 * 
	 * @return the MainPanel
	 */
	protected MainPanel getMainPanel() {
		CytoPanel cytoPanel = getControlCytoPanel();
		int count = cytoPanel.getCytoPanelComponentCount();
		for (int i = 0; i < count; i++)
			if (cytoPanel.getComponentAt(i) instanceof MainPanel)
				return (MainPanel) cytoPanel.getComponentAt(i);
		return null;
	}

	
	
	/**
	 * get the ResultPanel list
	 * 
	 * @return Collection<ResultPanel>
	 */
	protected Collection getResultPanels() {
		Collection panels = new ArrayList();
		CytoPanel cytoPanel = getResultsCytoPanel();
		int count = cytoPanel.getCytoPanelComponentCount();
		for (int i = 0; i < count; i++)
			if (cytoPanel.getComponentAt(i) instanceof ResultPanel)
				panels.add((ResultPanel) cytoPanel.getComponentAt(i));

		return panels;
	}

	/**
	 * get the unique ResultPanel from the resultId
	 * 
	 * @param resultId
	 * @return ResultPanel
	 */
	protected ResultPanel getResultPanel(int resultId) {
		for (Iterator iterator = getResultPanels().iterator(); iterator
				.hasNext();) {
			ResultPanel panel = (ResultPanel) iterator.next();
			if (panel.getResultId() == resultId)
				return panel;
		}
		return null;
	}

	/**
	 * judge the MainPanel is opened or not
	 * 
	 * @return boolean
	 */
	protected boolean isOpened() {
		return getMainPanel() != null;
	}
}
