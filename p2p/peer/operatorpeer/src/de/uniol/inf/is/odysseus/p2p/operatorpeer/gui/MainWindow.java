/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.operatorpeer.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import de.uniol.inf.is.odysseus.p2p.gui.AbstractMainWindow;

public class MainWindow extends AbstractMainWindow {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JLabel title;
	private JTabbedPane tabs;

	private Map<String, List> listOperations = new HashMap<String, List>();

	private Map<String, List> listActions = new HashMap<String, List>();

	private Map<String, JLabel> listStatus = new HashMap<String, JLabel>();

	private Map<String, JLabel> listSubplan = new HashMap<String, JLabel>();

	private Map<String, JLabel> listScheduler = new HashMap<String, JLabel>();

	private Map<String, JLabel> listSchedulerStrategy = new HashMap<String, JLabel>();

	private LinkedList<String> queryTabIndex = new LinkedList<String>();

	public MainWindow(String title) {
		super(title);
		initGUI(title);
		// TODO: Timer der nicht zugeordnete Anfragen entfernt

	}

	private void initGUI(String titleText) {
		try {
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					title = new JLabel();
					jPanel1.add(title);
					title.setText(titleText);
					title.setBounds(12, 6, 445, 34);
					title.setFont(new java.awt.Font("AlArabiya", 1, 28));
				}
				{
					tabs = new JTabbedPane();
					jPanel1.add(tabs);
					tabs.setBounds(12, 52, 760, 254);
				}
			}
			pack();
			this.setSize(794, 348);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addTab(String queryId) {
		synchronized (queryTabIndex) {
			queryTabIndex.add(queryId);
			JPanel panel = new JPanel();

			panel.setLayout(new GridLayout(2, 1));

			List list = new List();
			listActions.put(queryId, list);
			panel.add(list);

			JPanel bottom = new JPanel();
			bottom.setLayout(new GridLayout(1, 2));

			JPanel information = new JPanel();

			information.setLayout(new GridLayout(4, 2));

			information.add(new JLabel("Teilplaene:"));
			JLabel subplan = new JLabel("");
			listSubplan.put(queryId, subplan);
			information.add(subplan);

			information.add(new JLabel("Status:"));
			JLabel status = new JLabel("");
			listStatus.put(queryId, status);
			information.add(status);

			information.add(new JLabel("Scheduler:"));
			JLabel scheduler = new JLabel("");
			listScheduler.put(queryId, scheduler);
			information.add(scheduler);

			information.add(new JLabel("Scheduler-Strategie:"));
			JLabel schedulerStrategy = new JLabel("");
			listSchedulerStrategy.put(queryId, schedulerStrategy);
			information.add(schedulerStrategy);

			bottom.add(information);

			List events = new List();
			bottom.add(events);
			listOperations.put(queryId, events);

			panel.add(bottom);

			tabs.addTab(queryId.substring(
					queryId.length() - Math.min(queryId.length(), 10),
					queryId.length()), panel);
		}
	}

	@Override
    public void removeTab(String queryId) {
		synchronized (queryTabIndex) {
			// Find Index of Query in Tabs
			int pos = queryTabIndex.indexOf(queryId);
			tabs.removeTabAt(pos);
			queryTabIndex.remove(pos);
		}
	}

	@Override
	public void addStatus(String queryId, String status) {
		listStatus.get(queryId).setText(status);
	}

	@Override
	public void addSubplans(String queryId, int count) {
		listSubplan.get(queryId).setText(String.valueOf(count));
	}

	@Override
	public void addScheduler(String queryId, String scheduler) {
		listScheduler.get(queryId).setText(scheduler);
	}

	@Override
	public void addSchedulerStrategy(String queryId, String strategy) {
		listSchedulerStrategy.get(queryId).setText(strategy);
	}

	@Override
	public void addAction(String queryId, String action) {
		if( listActions.get(queryId) != null ) 
			listActions.get(queryId).add(action, 0);
	}

	@Override
	public void addOperation(String queryId, String operation) {
		listOperations.get(queryId).add(operation, 0);
	}

	@Override
	public boolean isQuery(String queryId) {
		return listActions.get(queryId) != null;
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
	public void addSplitting(String queryId, String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAdminPeer(String queryId, String adminPeer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addResult(String queryId, Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTab(String queryId, String queryAsString) {
		// TODO Auto-generated method stub

	}

}
