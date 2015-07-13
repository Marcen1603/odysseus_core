/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.recsys.streamsimulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uniol.inf.is.recsys.streamsimulator.data.ConfigData;
import de.uniol.inf.is.recsys.streamsimulator.data.FeatureData;
import de.uniol.inf.is.recsys.streamsimulator.run.Streamer;

/**
 * @author Cornelius Ludmann
 *
 */
public class MainWnd {

	private DefaultListModel<String> userItemListModel = new DefaultListModel<>();
	private String currentUserItem = "";
	private JPanel featureAdjustPanel = null;
	private JFrame mainFrame = null;

	private Thread ratingsStreamerThread;
	private Streamer ratingsStreamer;
	private Thread rfrStreamerThread;
	private Streamer rfrStreamer;

	public MainWnd(Streamer ratingsStreamer, Streamer rfrStreamer) {
		this.ratingsStreamer = ratingsStreamer;
		this.rfrStreamer = rfrStreamer;

		mainFrame = new JFrame("RecSys Stream Simulator");
		mainFrame.setLayout(new BorderLayout(20, 20));
		mainFrame.add(createConfigPanel(), BorderLayout.NORTH);
		mainFrame.add(createUserItemList(), BorderLayout.WEST);
		mainFrame.add(createFeatureAdjustPanel(), BorderLayout.CENTER);
		mainFrame.add(createStartStopButtons(), BorderLayout.SOUTH);
		mainFrame.add(createDriftPanel(), BorderLayout.EAST);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * @return
	 */
	private Component createConfigPanel() {
		JPanel configPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		addConfigEditRow(configPanel, "No of users: ", ConfigData.NO_USERS_KEY,
				ConfigData.getInstance().getNoOfUsers());
		addConfigEditRow(configPanel, "No of items: ", ConfigData.NO_ITEMS_KEY,
				ConfigData.getInstance().getNoOfItems());
		addConfigEditRow(configPanel, "No of features: ",
				ConfigData.NO_FEATURES_KEY, ConfigData.getInstance()
						.getNoOfFeatures());
		configPanel.add(new JLabel());
		final JCheckBox checkBox = new JCheckBox("Add timestamp to tuples.");
		checkBox.setSelected(ConfigData.getInstance().getAddTimestamp());
		checkBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConfigData.getInstance().setAddTimestamp(checkBox.isSelected());
			}
		});
		configPanel.add(checkBox);
		return configPanel;
	}

	/**
	 * @param configPanel
	 * @param string
	 * @param noUsersKey
	 * @param noOfUsers
	 */
	private void addConfigEditRow(JPanel parent, final String label,
			final String configKey, int defaultValue) {
		final JLabel jlabel = new JLabel(label + defaultValue);
		parent.add(jlabel);

		final ConfigData configData = ConfigData.getInstance();
		int minValue = configData.getMinValue(configKey);
		int maxValue = configData.getMaxValue(configKey);

		final JSlider jslider = new JSlider(minValue, maxValue, defaultValue);
		jslider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				jlabel.setText(label + jslider.getValue());

				configData.setByKeyAsInt(configKey, jslider.getValue());

				configDataChanged(configKey);
			}

		});
		jslider.setPaintTicks(true);
		jslider.setMajorTickSpacing((maxValue - minValue) / 10);
		// jslider.setMinorTickSpacing((maxValue - minValue) / 100);
		parent.add(jslider);
	}

	/**
	 * @return
	 */
	private Component createUserItemList() {
		final JList<String> userItemList = new JList<>(userItemListModel);

		userItemNoChanged();
		userItemList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				userOrItemSelected(userItemList.getSelectedValue());
			}

		});
		userItemList.setSelectedIndex(0);
		userItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return new JScrollPane(userItemList);
	}

	/**
	 * @return
	 */
	private Component createStartStopButtons() {
		JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));

		addConfigEditRow(panel, "Delay (in ms): ",
				ConfigData.RATINGS_DELAY_KEY, ConfigData.getInstance()
						.getRatingsStreamDelay());
		addConfigEditRow(panel, "Delay (in ms): ", ConfigData.RFR_DELAY_KEY,
				ConfigData.getInstance().getRfrStreamDelay());
		addConfigEditTextRow(panel, "Host: ", ConfigData.RATINGS_HOST_KEY,
				ConfigData.getInstance().getRatingsStreamHost());
		addConfigEditTextRow(panel, "Host: ", ConfigData.RFR_HOST_KEY,
				ConfigData.getInstance().getRfrStreamHost());
		addConfigEditTextRow(panel, "Port: ", ConfigData.RATINGS_PORT_KEY,
				ConfigData.getInstance().getRatingsStreamPort());
		addConfigEditTextRow(panel, "Port: ", ConfigData.RFR_PORT_KEY,
				ConfigData.getInstance().getRfrStreamPort());

		final JButton button = new JButton("Start Ratings Stream");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (button.getText().equals("Start Ratings Stream")) {
					button.setText("Stop Ratings Stream");
					startRatingsStream();
				} else {
					button.setText("Start Ratings Stream");
					stopRatingsStream();
				}

			}
		});
		panel.add(new JLabel());
		panel.add(button);

		final JButton button1 = new JButton("Start RfR Stream");
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (button1.getText().equals("Start RfR Stream")) {
					button1.setText("Stop RfR Stream");
					startRfrStream();
				} else {
					button1.setText("Start RfR Stream");
					stopRfrStream();
				}

			}
		});
		panel.add(new JLabel());
		panel.add(button1);

		return panel;
	}

	/**
	 * @param panel
	 * @param string
	 * @param ratingsHostKey
	 * @param ratingsStreamHost
	 */
	private void addConfigEditTextRow(JPanel parent, String label,
			final String configKey, String defaultValue) {
		final JLabel jlabel = new JLabel(label);
		parent.add(jlabel);

		final ConfigData configData = ConfigData.getInstance();

		final JTextField textField = new JTextField(defaultValue);
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}
		});
		parent.add(textField);

	}

	/**
	 * @param panel
	 * @param string
	 * @param ratingsPortKey
	 * @param ratingsStreamPort
	 */
	private void addConfigEditTextRow(JPanel parent, String label,
			final String configKey, int defaultValue) {
		final JLabel jlabel = new JLabel(label);
		parent.add(jlabel);

		final ConfigData configData = ConfigData.getInstance();

		final JTextField textField = new JTextField(
				String.valueOf(defaultValue));
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println(textField.getText());
				configData.setByKey(configKey, textField.getText());
			}
		});
		parent.add(textField);
	}

	/**
	 * 
	 */
	protected void startRatingsStream() {
		System.out.println("start ratings");
		ratingsStreamerThread = new Thread(ratingsStreamer);
		ratingsStreamerThread.start();
	}

	/**
	 * 
	 */
	protected void stopRatingsStream() {
		System.out.println("stop ratings");
		ratingsStreamer.stop();
		try {
			ratingsStreamerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void startRfrStream() {
		System.out.println("start rfr");
		rfrStreamerThread = new Thread(rfrStreamer);
		rfrStreamerThread.start();
	}

	/**
	 * 
	 */
	protected void stopRfrStream() {
		System.out.println("stop rfr");
		rfrStreamer.stop();
		try {
			rfrStreamerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private Component createDriftPanel() {
		JPanel conceptDriftPanel = new JPanel(new GridLayout(9, 1, 5, 5));

		addConfigEditRow(conceptDriftPanel, "Standard Deviation of the Bias: ",
				ConfigData.STANDARD_DEVIATION_OF_BIAS, ConfigData.getInstance()
						.getStandardDeviationOfBias());

		JButton driftButton = new JButton("Drift 10 % of all users");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().drift(0.1f);
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);

		driftButton = new JButton("Drift 20 % of all users");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().drift(0.2f);
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);

		driftButton = new JButton("Drift 30 % of all users");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().drift(0.3f);
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);

		driftButton = new JButton("Drift 50 % of all users");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().drift(0.5f);
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);

		driftButton = new JButton("Drift 80 % of all users");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().drift(0.8f);
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);

		driftButton = new JButton("Randomly reset all features");
		driftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeatureData.getInstance().resetAllFeatures();
				userOrItemSelected(currentUserItem);
			}
		});
		conceptDriftPanel.add(driftButton);
		return conceptDriftPanel;
	}

	/**
	 * @return
	 */
	private Component createFeatureAdjustPanel() {
		int noOfFeatures = ConfigData.getInstance().getNoOfFeatures();
		featureAdjustPanel = new JPanel(new GridLayout(noOfFeatures, 2, 5, 5));
		for (int i = 0; i < noOfFeatures; ++i) {
			addFeatureEditRow(featureAdjustPanel, i);
		}
		return new JScrollPane(featureAdjustPanel);
	}

	/**
	 * @param featureAdjustPanel2
	 * @param i
	 */
	private void addFeatureEditRow(JPanel parent, final int feature) {
		final String userItemKey = currentUserItem == null
				|| currentUserItem.trim().equals("") ? FeatureData.USER_KEY_PREFIX + 0
				: currentUserItem;

		int value = FeatureData.getInstance().getFeature(userItemKey, feature);
		final JLabel jlabel = new JLabel("Feature " + feature + ": "
				+ (((double) value) / 100));
		parent.add(jlabel);

		int minValue = FeatureData.getInstance().getMinValue(
				FeatureData.FEATURE_KEY_PREFIX);
		int maxValue = FeatureData.getInstance().getMaxValue(
				FeatureData.FEATURE_KEY_PREFIX);

		final JSlider jslider = new JSlider(minValue, maxValue, value);
		jslider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				jlabel.setText("Feature " + feature + ": "
						+ (((double) jslider.getValue()) / 100));

				FeatureData.getInstance().setByKeyAsInt(userItemKey,
						jslider.getValue());
			}

		});
		jslider.setPaintTicks(true);
		jslider.setMajorTickSpacing((maxValue - minValue) / 10);
		// jslider.setMinorTickSpacing((maxValue - minValue) / 100);
		parent.add(jslider);

	}

	private void configDataChanged(String configKey) {
		if (ConfigData.NO_USERS_KEY.equals(configKey)
				|| ConfigData.NO_ITEMS_KEY.equals(configKey)) {
			userItemNoChanged();
		} else if (ConfigData.NO_FEATURES_KEY.equals(configKey)) {
			featuresNoChanged();
		}
	}

	/**
	 * 
	 */
	private synchronized void featuresNoChanged() {
		Container container = featureAdjustPanel.getParent();
		synchronized (container) {
			if (container.getComponentCount() > 1) {
				System.out
						.println("WARNING: Container has more than one component.");
			}
			container.remove(featureAdjustPanel);
			container.add(((Container) createFeatureAdjustPanel())
					.getComponent(0));
			container.notify();
		}
	}

	private synchronized void userItemNoChanged() {
		// userItemListModel.clear();

		int noOfUsers = ConfigData.getInstance().getNoOfUsers();
		int noOfItems = ConfigData.getInstance().getNoOfItems();

		for (int i = 0; i < noOfUsers; ++i) {
			if (!userItemListModel.contains(FeatureData.USER_KEY_PREFIX + i)) {
				userItemListModel.add(i, FeatureData.USER_KEY_PREFIX + i);
			}
		}

		for (int i = noOfUsers; userItemListModel
				.contains(FeatureData.USER_KEY_PREFIX + i); ++i) {
			userItemListModel.removeElementAt(noOfUsers);
		}

		for (int i = 0; i < noOfItems; ++i) {
			if (!userItemListModel.contains(FeatureData.ITEM_KEY_PREFIX + i)) {
				// userItemListModel.add(noOfUsers + i,
				// FeatureData.ITEM_KEY_PREFIX + i);
				userItemListModel.insertElementAt(FeatureData.ITEM_KEY_PREFIX
						+ i, noOfUsers + i);
			}
		}

		for (int i = noOfItems; userItemListModel
				.contains(FeatureData.ITEM_KEY_PREFIX + i); ++i) {
			userItemListModel.removeElementAt(noOfUsers + noOfItems);
		}
	}

	private void userOrItemSelected(String selectedValue) {
		currentUserItem = selectedValue;

		int f = 0;
		if (featureAdjustPanel != null) {
			for (int i = 0; i < featureAdjustPanel.getComponentCount(); ++i) {
				if (featureAdjustPanel.getComponent(i) instanceof JSlider) {
					((JSlider) featureAdjustPanel.getComponent(i))
							.setValue(FeatureData.getInstance().getFeature(
									selectedValue, f));
					++f;
				}
			}
		}
	}

}
