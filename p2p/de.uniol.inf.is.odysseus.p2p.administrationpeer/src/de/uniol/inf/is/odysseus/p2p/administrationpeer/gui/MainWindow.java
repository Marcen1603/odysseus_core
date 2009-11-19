package de.uniol.inf.is.odysseus.p2p.administrationpeer.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import de.uniol.inf.is.odysseus.p2p.gui.AbstractMainWindow;

public class MainWindow extends AbstractMainWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<String, List> listMessages = new HashMap<String, List>();

	private HashMap<String, List> listEvents = new HashMap<String, List>();

	private HashMap<String, JLabel> listSubplans = new HashMap<String, JLabel>();

	private HashMap<String, JLabel> listSplitting = new HashMap<String, JLabel>();

	private HashMap<String, JLabel> listStatus = new HashMap<String, JLabel>();

	private HashMap<String, JLabel> listBids = new HashMap<String, JLabel>();

	private JPanel jPanel1;
	private JLabel title;
	private JTabbedPane tabs;

	public MainWindow() {
		super("Odysseus Verwaltungs-Peer");

		initGUI();
	}

	private void initGUI() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					title = new JLabel();
					jPanel1.add(title);
					title.setText("Odysseus Verwaltungs-Peer");
					title.setBounds(12, 12, 453, 32);
					title.setFont(new java.awt.Font("AlArabiya", 1, 28));
				}
				{
					tabs = new JTabbedPane();
					jPanel1.add(tabs);
					tabs.setBounds(12, 63, 750, 237);
					
				}
			}
			pack();
			this.setSize(784, 342);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void addTab(String queryId) {
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(2, 1));
		List list = new List();
		this.listMessages.put(queryId, list);
		panel.add(list);

		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1, 2));

		JPanel information = new JPanel();
		information.setLayout(new GridLayout(4, 2));

		information.add(new JLabel("Teilpläne:"));
		JLabel subplans = new JLabel("");
		listSubplans.put(queryId, subplans);
		information.add(subplans);

		information.add(new JLabel("Status:"));
		JLabel status = new JLabel("");
		listStatus.put(queryId, status);
		information.add(status);

		information.add(new JLabel("Anzahl Gebote:"));
		JLabel bids = new JLabel("0");
		listBids.put(queryId, bids);
		information.add(bids);

		information.add(new JLabel("Zerlegunsstrategie:"));
		JLabel splitting = new JLabel("");
		listSplitting.put(queryId, splitting);
		information.add(splitting);

		bottom.add(information);

		List eventList = new List();
		bottom.add(eventList);
		this.listEvents.put(queryId, eventList);
		panel.add(bottom);

		tabs.addTab(queryId, panel);
	}
	@Override
	public void addAction(String queryId, String action) {
		listMessages.get(queryId).add(action, 0);
	}
	@Override
	public void addEvent(String queryId, String event) {
		listEvents.get(queryId).add(event, 0);
	}
	@Override
	public void addSubplans(String queryId, int s) {
		listSubplans.get(queryId).setText(String.valueOf(s));
	}
	@Override
	public void addStatus(String queryId, String s) {
		listStatus.get(queryId).setText(s);
	}
	@Override
	public void addBids(String queryId, String s) {
		listBids.get(queryId).setText(s);
	}
	@Override
	public void addSplitting(String queryId, String s) {
		listSplitting.get(queryId).setText(s);
	}
	@Override
	public void removeQuery(String queryId) {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			if (tabs.getTitleAt(i).equals(queryId)) {
				tabs.removeTabAt(i);
				return;
			}
		}
		listMessages.remove(queryId);
		listEvents.remove(queryId);
		listSubplans.remove(queryId);
		listSplitting.remove(queryId);
		listStatus.remove(queryId);
		listBids.remove(queryId);

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
	public boolean isQuery(String queryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
