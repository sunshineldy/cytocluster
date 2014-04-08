package org.cytoscape.CytoCluster.internal.CommonUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import org.cytoscape.CytoCluster.internal.MyUtils.ClusterUtil;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources;
import org.cytoscape.CytoCluster.internal.MyUtils.Resources.ImageName;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.util.swing.OpenBrowser;

public class AboutDialog extends JDialog implements CytoPanelComponent {
	private static final long serialVersionUID = 1L;
	private String version;
	private OpenBrowser openBrowser;
	private ClusterUtil clusterUtil;
	private JEditorPane editorPane;
	private JPanel buttonPanel;

	public AboutDialog(ClusterUtil clusterUtil) {
		this.clusterUtil = clusterUtil;
		this.openBrowser = clusterUtil.getOpenBrowser();
		this.version = "1.0";
		setResizable(true);
		this.setTitle("About CytoCluster");
		JScrollPane jScrollPane = new JScrollPane(getMainContainer());
		getContentPane().add(jScrollPane, BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		pack();
	}

	private JEditorPane getMainContainer() {

		editorPane = new JEditorPane();
		editorPane.setMargin(new Insets(10, 10, 10, 10));
		editorPane.setEditable(false);
		editorPane.setEditorKit(new HTMLEditorKit());
		editorPane.addHyperlinkListener(new HyperlinkAction());

		String text = "<html><body><img src='"
				+ Resources.getUrl(ImageName.About_CSU)
				+ "'/>"
				+ "<P align=center><b>Cytoscape APP -- CytoCluster"
				+ "</b><BR>"
				+ "A Cytoscape App<BR><BR>"
				+ "<a href='http://netlab.csu.edu.cn/'> Our Lab  </a>,in (Central South University, China)<BR>"
				+ "Version "
				+ version
				+ " by Yu Tang (Central South University, China)<BR>"
				+ "If you use this app in your research, please cite:<BR>"
				+ "Yu Tang, Min Li, Jianxin Wang, CytoCluster: a Cytoscape app for analysis and visualization of clusters from network"
				+ "<br> <a href='https://code.google.com/p/cytocluster-cytoscape/'> source codes </a><BR>"
				+ "<BR>" + "</P></body></html>";

		editorPane.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER: // Enter key
				case KeyEvent.VK_ESCAPE: // Esc key
					dispose();
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		editorPane.setText(text);
		return editorPane;
	}

	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		JButton closeBtn = new JButton("Close");

		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		buttonPanel.add(closeBtn);
		closeBtn.setAlignmentX(CENTER_ALIGNMENT);
		return buttonPanel;
	}

	private class HyperlinkAction implements HyperlinkListener {
		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				System.out.println("URL is ..... " + e.getURL().toString());
				openBrowser.openURL(e.getURL().toString());
			}
		}
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return null;
	}

	@Override
	public Icon getIcon() {
		ImageIcon bkIcon = new ImageIcon(
				Resources.getUrl(Resources.ImageName.About_CSU));
		return bkIcon;
	}
}
