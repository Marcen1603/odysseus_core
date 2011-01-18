package de.uniol.inf.is.odysseus.p2p.thinpeer.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import de.uniol.inf.is.odysseus.p2p.gui.AbstractMainWindow;
import de.uniol.inf.is.odysseus.p2p.thinpeer.AbstractThinPeer;

public class MainWindow extends AbstractMainWindow implements ActionListener {
	//Thin-Peer spezifisch
	protected JList adminPeers;
	protected JLabel adminPeersLabel;
	protected JList querys;
	protected JList sources;
	protected JProgressBar sourcesProgress;
	protected JTabbedPane tabs;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow inst = new MainWindow();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	private JProgressBar adminPeerProgress;
	private JButton close;
	private JButton doQuery;
	private JLabel header;
	private JPanel jPanel1;
	private HashMap<String, List> listResults = new HashMap<String, List>();
	private HashMap<String, JLabel> listAdminPeer = new HashMap<String, JLabel>();
	private HashMap<String, JLabel> listStatus = new HashMap<String, JLabel>();
	private JLabel makeQuery;
	private JTextArea query;
	private JPanel queryPanel;
	private JLabel querysLabel;
	private JProgressBar querysProgress;
	private JLabel sourcesLabel;
	AbstractThinPeer thinPeer;

	public MainWindow() {
		super("Odysseus Thin-Peer");
		initGUI();
	}

	public MainWindow(AbstractThinPeer thinPeer) {
		super("Odysseus Thin-Peer");
		this.thinPeer = thinPeer;
		initGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("stopPeer".equals(e.getActionCommand())) {
			this.thinPeer.stopPeer();
		} 
		else if ("translateButton".equals(e.getActionCommand())) {
			thinPeer.publishQuerySpezification(query.getText(), "CQL");
			
		}

	}
	@Override
	public void addResult(String queryId, Object o) {
		List l = listResults.get(queryId);
		l.add(o.toString(), 0);
	}
	@Override
	public void addTab(String queryId, String queryAsString) {
		jPanel1 = new JPanel();
		List l = new List();
		listResults.put(queryId, l);
		tabs.addTab("Anfrage:" + queryId, null, jPanel1, null);

		jPanel1.setPreferredSize(new java.awt.Dimension(996, 373));
		jPanel1.setLayout(new GridLayout(2, 1));
		jPanel1.add(l);

		JPanel information = new JPanel();
		information.setLayout(new GridLayout(4, 2));
		information.add(new JLabel("Anfrage:"));
		information.add(new JLabel(queryAsString));
		information.add(new JLabel("Sprache:"));
		information.add(new JLabel("CQL"));
		information.add(new JLabel("Verwaltungs-Peer:"));

		JLabel adminPeer = new JLabel("");
		information.add(adminPeer);
		listAdminPeer.put(queryId, adminPeer);
		JLabel status = new JLabel("Status der Anfrage:");

		information.add(status);
		JLabel statusValue = new JLabel("");
		information.add(statusValue);
		listStatus.put(queryId, statusValue);
		jPanel1.setSize(900, 373);
		jPanel1.add(information);
	}
	@Override
	public void addAdminPeer(String queryId, String adminPeer) {
		JLabel label = listAdminPeer.get(queryId);
		label.setText(adminPeer);
	}

	@Override
	public void addStatus(String queryId, String status) {
		JLabel label = listStatus.get(queryId);
		label.setText(status);
	}

	public JProgressBar getAdminPeerProgress() {
		return adminPeerProgress;
	}
	public JList getAdminPeers() {
		return adminPeers;
	}
	public JLabel getAdminPeersLabel() {
		return adminPeersLabel;
	}
	public JList getQuerys() {
		return querys;
	}
	public JList getSources() {
		return sources;
	}
	public JProgressBar getSourcesProgress() {
		return sourcesProgress;
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				queryPanel = new JPanel();
				getContentPane().add(queryPanel, BorderLayout.CENTER);
				queryPanel.setPreferredSize(new java.awt.Dimension(1030, 551));
				queryPanel.setLayout(null);
				{
					tabs = new JTabbedPane();
					queryPanel.add(tabs);
					tabs.setBounds(12, 64, 950, 415);
					{
						jPanel1 = new JPanel();
						tabs.addTab("Anfrage", null, jPanel1, null);
						jPanel1.setPreferredSize(new java.awt.Dimension(996,
								373));
						jPanel1.setLayout(null);
						jPanel1.setSize(900, 373);
						{
							query = new JTextArea();
							jPanel1.add(query);
							query.setText("select * from nexmark:person2");
							query.setBounds(12, 35, 287, 292);
						}
						{
							ListModel QuerysModel = new DefaultComboBoxModel(
									new String[] {});
							querys = new JList();
							jPanel1.add(querys);
							querys.setModel(QuerysModel);
							querys.setBounds(330, 35, 182, 292);
						}
						{
							querysLabel = new JLabel();
							jPanel1.add(querysLabel);
							querysLabel.setText("Anfragen");
							querysLabel.setBounds(330, 12, 58, 17);
						}
						{
							adminPeersLabel = new JLabel();
							jPanel1.add(adminPeersLabel);
							adminPeersLabel.setText("Verwaltungs-Peers");
							adminPeersLabel.setBounds(524, 12, 119, 17);
						}
						{
							makeQuery = new JLabel();
							jPanel1.add(makeQuery);
							makeQuery.setText("Anfrage formulieren");
							makeQuery.setBounds(12, 12, 127, 17);
						}
						{
							adminPeerProgress = new JProgressBar();
							adminPeerProgress.setIndeterminate(true);
							jPanel1.add(adminPeerProgress);
							adminPeerProgress.setBounds(524, 339, 182, 22);
						}
						{
							doQuery = new JButton();
							jPanel1.add(doQuery);
							doQuery.setText("Anfrage ausführen");
							doQuery.setActionCommand("translateButton");
							doQuery.addActionListener(this);
							doQuery.setBounds(12, 339, 287, 29);
						}
						{
							ListModel AdminPeersModel = new DefaultComboBoxModel(
									new String[] {});
							adminPeers = new JList();
							jPanel1.add(adminPeers);
							adminPeers.setModel(AdminPeersModel);
							adminPeers.setBounds(524, 35, 182, 292);
						}
						{
							ListModel sourcesModel = new DefaultComboBoxModel(
									new String[] {});
							sources = new JList();
							jPanel1.add(sources);
							sources.setModel(sourcesModel);
							sources.setBounds(722, 35, 182, 292);
						}
						{
							sourcesProgress = new JProgressBar();
							sourcesProgress.setIndeterminate(true);
							jPanel1.add(sourcesProgress);
							sourcesProgress.setBounds(722, 340, 182, 21);
						}
						{
							sourcesLabel = new JLabel();
							jPanel1.add(sourcesLabel);
							sourcesLabel.setText("Quellen");
							sourcesLabel.setBounds(722, 12, 48, 17);
						}
						{
							querysProgress = new JProgressBar();
							querysProgress.setIndeterminate(true);
							jPanel1.add(querysProgress);
							querysProgress.setBounds(330, 340, 182, 20);
						}
					}

				}
				{
					header = new JLabel();
					queryPanel.add(header);
					header.setText("Odysseus Thin-Peer");
					header.setBounds(12, 12, 379, 40);
					header.setFont(new java.awt.Font("AlArabiya", 1, 12));
				}
				{
					close = new JButton();
					queryPanel.add(close);
					close.addActionListener(this);
					close.setActionCommand("stopPeer");
					close.setText("Schließen");
					close.setBounds(801, 482, 162, 29);
				}
			}
			pack();
			this.setSize(1004, 545);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeTab(String queryId) {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			if (tabs.getTitleAt(i).equals("Anfrage:" + queryId)) {
				tabs.removeTabAt(i);
				return;
			}
		}
	}

	public void setAdminPeers(JList adminPeers) {
		this.adminPeers = adminPeers;
	}

	public void setAdminPeersLabel(JLabel adminPeersLabel) {
		this.adminPeersLabel = adminPeersLabel;
	}

	public void setQuerys(JList querys) {
		this.querys = querys;
	}

	public void setSources(JList sources) {
		this.sources = sources;
	}

	public void setSourcesProgress(JProgressBar sourcesProgress) {
		this.sourcesProgress = sourcesProgress;
	}

	@Override
	public void addAction(String queryId, String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBids(String queryId, String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEvent(String queryId, String event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOperation(String queryId, String operation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addScheduler(String queryId, String scheduler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSchedulerStrategy(String queryId, String strategy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSplitting(String queryId, String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSubplans(String queryId, int s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTab(String queryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isQuery(String queryId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeQuery(String queryId) {
		// TODO Auto-generated method stub
		
	}

}
