package org.cytoscape.CytoCluster.internal.CommonUI;

//Copied from http://www.codeguru.com/java/articles/122.shtml

// JMultiLineToolTip.java

import javax.swing.*;

/**
 * A multiple line tiptool for the main panel Copied from
 * http://www.codeguru.com/java/articles/122.shtml
 */
public class MyTipTool extends JToolTip {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7508857926559783651L;
	String tipText;
	JComponent component;
	protected int columns = 0;
	protected int fixedwidth = 0;

	public MyTipTool() {
		updateUI();
	}

	public void updateUI() {
		setUI(MultiLineToolTipUI.createUI(this));
	}

	public void setColumns(int columns) {
		this.columns = columns;
		this.fixedwidth = 0;
	}

	public int getColumns() {
		return columns;
	}

	public void setFixedWidth(int width) {
		this.fixedwidth = width;
		this.columns = 0;
	}

	public int getFixedWidth() {
		return fixedwidth;
	}
}
