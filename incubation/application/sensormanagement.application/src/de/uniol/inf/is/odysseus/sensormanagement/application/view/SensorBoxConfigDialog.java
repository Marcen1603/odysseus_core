package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.SensorConfig.State;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.live.LiveSession;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.JTreeItemClickListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorType;

@SuppressWarnings("rawtypes")
public class SensorBoxConfigDialog extends JDialog 
{
	private static final long serialVersionUID = 777778513810676189L;

	SensorBox sensorBox;
	
	private JTree boxTree;
	private JPanel settingsPanel;
	private JTextField sensorPositionEdit;
	private JTextField boxEthernetAddrEdit;
	private JTextField boxPositionEdit;
	private JTextField sensorNameEdit;
	private JTextField boxNameEdit;
	private JPanel boxSettingsPanel;
	private JPanel sensorSettingsPanel;	
	private JList boxQueryList;

	private List<SensorConfig> sensors = new ArrayList<>();
	private SensorConfig lastShownSensor;
	private JTable optionsTable;
	private JComboBox sensorTypeComboBox;
	
	class ConfigTreeCellRenderer extends TreeCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Icon getIconByNode(Object node) 
		{
			if (node instanceof DefaultMutableTreeNode) 
			{	
				Object obj = ((DefaultMutableTreeNode) node).getUserObject();
	            if (obj instanceof SensorConfig) 
	            {
	            	SensorConfig sensorConfig = (SensorConfig) obj;
	            	RemoteSensor originalSensor = sensorConfig.getOriginalSensor();
	            	if (originalSensor == null)
	            		return sensorIcon; // TODO: newSensorIcon
	                if (originalSensor.isLogging()) //getRunningQueries().contains(RemoteSensor.loggingQueryName)) 
	                	return sensorIconRec;

	                return sensorIcon;
	            }
	        }
				
			return sensorBoxIcon;
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */	
	@SuppressWarnings("unchecked")
	public SensorBoxConfigDialog(LiveSession session, SensorBox sensorBox, RemoteSensor sensorToShow) 
	{
		super(Application.getMainFrame(), "", Dialog.ModalityType.DOCUMENT_MODAL);
		String hostName = sensorBox != null ? sensorBox.getEthernetAddr() : "0.0.0.0:65535"; // For Window Builder Design time GUI generation
		setTitle("RemoteSensor box configuration: " + hostName);
	
		this.sensorBox = sensorBox;
		
		setBounds(100, 100, 623, 446);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel treePanel = new JPanel();
		treePanel.setMinimumSize(new Dimension(150, 10));
		treePanel.setPreferredSize(new Dimension(150, 10));
		treePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sensors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(treePanel);
		treePanel.setLayout(new BorderLayout(0, 0));

		DefaultMutableTreeNode boxNode = new DefaultMutableTreeNode(sensorBox.getName());
		
		SensorConfig sensorConfigToShow = null;		
		for (RemoteSensor sensor : sensorBox.getClient().getSensors())
		{
			DefaultMutableTreeNode sensorNode = new DefaultMutableTreeNode();
			SensorConfig sensorConfig = new SensorConfig(sensor, sensorNode);
			sensorNode.setUserObject(sensorConfig);
			sensors.add(sensorConfig);
			boxNode.add(sensorNode);
			
			if (sensor == sensorToShow)
				sensorConfigToShow = sensorConfig;
		}
		
		boxTree = new JTree(boxNode);
		boxTree.setShowsRootHandles(true);
		boxTree.setCellRenderer(new ConfigTreeCellRenderer());
		boxTree.addMouseListener(	new JTreeItemClickListener()
							  		{
										public void mouseDblClicked(MouseEvent e, Object item)	{ }
										public void mouseClicked(MouseEvent e, Object item) 	{ boxTreeDoubleClick(item); }
							  		});		
		
		treePanel.add(boxTree, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton addSensorButton = new JButton("Add sensor...");
		addSensorButton.addMouseListener(	new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent arg0) { addSensorButtonClick(); }
											});
		buttonPanel.add(addSensorButton);
		
		JButton removeSensorButton = new JButton("Remove sensor");
		removeSensorButton.addMouseListener(new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent arg0) { removeSensorButtonClick(); }
											});
		
		buttonPanel.add(removeSensorButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(	new MouseAdapter() 
										{
											public void mouseClicked(MouseEvent arg0) { SensorBoxConfigDialog.this.setVisible(false); }
										});
		
		JButton okButton = new JButton("Ok");
		okButton.addMouseListener(	new MouseAdapter() 
									{
										public void mouseClicked(MouseEvent e) { okButtonClick(); }
									});
		buttonPanel.add(okButton);
		
		JButton applyButton = new JButton("Apply");
		applyButton.addMouseListener(	new MouseAdapter() 
										{
											public void mouseClicked(MouseEvent e) { applyButtonClick(); }
										});		
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		
		settingsPanel = new JPanel();
		panel.add(settingsPanel, BorderLayout.CENTER);
		
		boxSettingsPanel = new JPanel();
		
		sensorSettingsPanel = new JPanel();
		settingsPanel.setLayout(new CardLayout(0, 0));
		settingsPanel.add(boxSettingsPanel, "boxSettingsPanel");
		SpringLayout sl_boxSettingsPanel = new SpringLayout();
		boxSettingsPanel.setLayout(sl_boxSettingsPanel);
		
		JLabel boxEthernetAddrLabel = new JLabel("Ethernet Address");
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxEthernetAddrLabel, 10, SpringLayout.WEST, boxSettingsPanel);
		boxSettingsPanel.add(boxEthernetAddrLabel);
		
		boxEthernetAddrEdit = new JTextField(sensorBox.getEthernetAddr());
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxEthernetAddrLabel, 3, SpringLayout.NORTH, boxEthernetAddrEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxEthernetAddrEdit, 6, SpringLayout.EAST, boxEthernetAddrLabel);
		boxEthernetAddrEdit.setColumns(10);
		boxSettingsPanel.add(boxEthernetAddrEdit);
		
		boxPositionEdit = new JTextField(sensorBox.getPosition());
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxPositionEdit, 6, SpringLayout.SOUTH, boxEthernetAddrEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxPositionEdit, 0, SpringLayout.WEST, boxEthernetAddrEdit);
		boxPositionEdit.setColumns(10);
		boxSettingsPanel.add(boxPositionEdit);
		
		JLabel boxPositionLabel = new JLabel("Position");
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxPositionLabel, 3, SpringLayout.NORTH, boxPositionEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxPositionLabel, 0, SpringLayout.WEST, boxEthernetAddrLabel);
		boxSettingsPanel.add(boxPositionLabel);
		
		JLabel currentQueriesLabel = new JLabel("Current Querys");
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, currentQueriesLabel, 10, SpringLayout.SOUTH, boxPositionEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, currentQueriesLabel, 0, SpringLayout.WEST, boxEthernetAddrLabel);
		boxSettingsPanel.add(currentQueriesLabel);
		
		boxQueryList = new JList(new DefaultListModel());
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxQueryList, 10, SpringLayout.WEST, boxSettingsPanel);
		sl_boxSettingsPanel.putConstraint(SpringLayout.SOUTH, boxQueryList, 198, SpringLayout.SOUTH, currentQueriesLabel);
		sl_boxSettingsPanel.putConstraint(SpringLayout.EAST, boxQueryList, 250, SpringLayout.WEST, boxSettingsPanel);
		boxQueryList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxQueryList, 6, SpringLayout.SOUTH, currentQueriesLabel);
		boxSettingsPanel.add(boxQueryList);
		
		JLabel boxNameLabel = new JLabel("Name");
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxNameLabel, 0, SpringLayout.WEST, boxEthernetAddrLabel);
		boxSettingsPanel.add(boxNameLabel);
		
		boxNameEdit = new JTextField(sensorBox.getName());
		boxNameEdit.getDocument().addDocumentListener(	new DocumentListener()
														{
															public void insertUpdate(DocumentEvent ev) 	{ boxNameEditChanged(); }
															public void removeUpdate(DocumentEvent ev) 	{ boxNameEditChanged(); }
															public void changedUpdate(DocumentEvent ev) { boxNameEditChanged(); }
														});
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxEthernetAddrEdit, 6, SpringLayout.SOUTH, boxNameEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxNameLabel, 3, SpringLayout.NORTH, boxNameEdit);
		sl_boxSettingsPanel.putConstraint(SpringLayout.NORTH, boxNameEdit, 10, SpringLayout.NORTH, boxSettingsPanel);
		sl_boxSettingsPanel.putConstraint(SpringLayout.WEST, boxNameEdit, 0, SpringLayout.WEST, boxEthernetAddrEdit);
		boxNameEdit.setColumns(10);
		boxSettingsPanel.add(boxNameEdit);
		settingsPanel.add(sensorSettingsPanel, "sensorSettingsPanel");
		SpringLayout sl_sensorSettingsPanel = new SpringLayout();
		sensorSettingsPanel.setLayout(sl_sensorSettingsPanel);
		
		JLabel sensorTypeLabel = new JLabel("Sensor type");
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorTypeLabel, 10, SpringLayout.WEST, sensorSettingsPanel);
		sensorSettingsPanel.add(sensorTypeLabel);
		
		JLabel sensorPositionLabel = new JLabel("Position");
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorPositionLabel, 12, SpringLayout.SOUTH, sensorTypeLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorPositionLabel, 0, SpringLayout.WEST, sensorTypeLabel);
		sensorSettingsPanel.add(sensorPositionLabel);
		
		sensorPositionEdit = new JTextField();
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorPositionEdit, -3, SpringLayout.NORTH, sensorPositionLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorPositionEdit, 53, SpringLayout.EAST, sensorPositionLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.EAST, sensorPositionEdit, -168, SpringLayout.EAST, sensorSettingsPanel);
		sensorPositionLabel.setLabelFor(sensorPositionEdit);
		sensorPositionEdit.getDocument().addDocumentListener(new DocumentListener()
															{
																public void insertUpdate(DocumentEvent ev) 	{ sensorEditChanged(); }
																public void removeUpdate(DocumentEvent ev) 	{ sensorEditChanged(); }
																public void changedUpdate(DocumentEvent ev) { sensorEditChanged(); }
															});
		sensorPositionEdit.setColumns(10);
		sensorSettingsPanel.add(sensorPositionEdit);
		
		sensorNameEdit = new JTextField();
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorNameEdit, 0, SpringLayout.WEST, sensorPositionEdit);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.EAST, sensorNameEdit, 0, SpringLayout.EAST, sensorPositionEdit);
		sensorNameEdit.getDocument().addDocumentListener(	new DocumentListener()
															{
																public void insertUpdate(DocumentEvent ev) 	{ sensorEditChanged(); }
																public void removeUpdate(DocumentEvent ev) 	{ sensorEditChanged(); }
																public void changedUpdate(DocumentEvent ev) { sensorEditChanged(); }
															});
		sensorNameEdit.setColumns(10);
		sensorSettingsPanel.add(sensorNameEdit);
		
		JLabel sensorNameLabel = new JLabel("Sensor name");
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorNameEdit, -3, SpringLayout.NORTH, sensorNameLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorNameLabel, 13, SpringLayout.NORTH, sensorSettingsPanel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorTypeLabel, 12, SpringLayout.SOUTH, sensorNameLabel);
		sensorNameLabel.setLabelFor(sensorNameEdit);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorNameLabel, 10, SpringLayout.WEST, sensorSettingsPanel);
		sensorSettingsPanel.add(sensorNameLabel);
		
		optionsTable = new JTable();
		optionsTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Option", "Value"
			}
		));
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, optionsTable, -269, SpringLayout.SOUTH, sensorSettingsPanel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, optionsTable, 10, SpringLayout.WEST, sensorSettingsPanel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.SOUTH, optionsTable, -10, SpringLayout.SOUTH, sensorSettingsPanel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.EAST, optionsTable, 440, SpringLayout.WEST, sensorSettingsPanel);
		sensorSettingsPanel.add(optionsTable);
		
		sensorTypeComboBox = new JComboBox();
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		
		for (SensorType type : sensorBox.getSensorTypes())
			comboBoxModel.addElement(type);		
		
		sensorTypeComboBox.setModel(comboBoxModel);
		sensorTypeComboBox.setSelectedIndex(0);
		sensorTypeComboBox.addActionListener(	new ActionListener () 
											 	{
		    										public void actionPerformed(ActionEvent e) 
		    										{
		    											sensorTypeChanged();
		    										}
											 	});
		sl_sensorSettingsPanel.putConstraint(SpringLayout.NORTH, sensorTypeComboBox, -3, SpringLayout.NORTH, sensorTypeLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.WEST, sensorTypeComboBox, 32, SpringLayout.EAST, sensorTypeLabel);
		sl_sensorSettingsPanel.putConstraint(SpringLayout.EAST, sensorTypeComboBox, 214, SpringLayout.EAST, sensorTypeLabel);
		sensorSettingsPanel.add(sensorTypeComboBox);
		optionsTable.getModel().addTableModelListener(	new TableModelListener()
														{
															@Override public void tableChanged(TableModelEvent arg0) 
															{
																optionsTableChanged();
															}
														});
		
		if (sensorConfigToShow != null)			
			showSensorPage(sensorConfigToShow);
		else
			showBoxPage();
		
		setVisible(true);
	}

	public SensorBoxConfigDialog(LiveSession session, SensorBox box) 
	{
		this(session, box, null);
	}

	protected void applyButtonClick() 
	{
		if (!sensorBox.getEthernetAddr().equals(boxEthernetAddrEdit.getText()))
			throw new IllegalArgumentException("Cannot change ethernet address of existing box. TODO: Add possibility to set ethernet address of new box :)");
		
		// Apply sensor box changes
		sensorBox.setPosition(boxPositionEdit.getText());
		sensorBox.setName(boxNameEdit.getText());
		
		// Add sensors which are in the new list and not in the original list
		// Remove sensors which are in the original list, but aren't in the change list		
		// Update sensors which are in both lists and marked as changed		
		for (SensorConfig sc : sensors)
		{
			try
			{
				switch (sc.getState())
				{
				case NewSensor:
					sensorBox.getClient().addSensor(sc.createSensor(sensorBox));
					break;
						
				case ExistingChangedSensor:
					sensorBox.getClient().updateSensor(sc.getOriginalSensor(), sc.getSensorInfo());
					break;
						
				case DeletedSensor:
					sensorBox.getClient().removeSensor(sc.getOriginalSensor());
					break;
						
				case ExistingUnchangedSensor:
					break;
				}
				
				sc.applied();
			}
			catch (Exception e)
			{
				Application.showException(e);
			}
		}		
		
		// Update config dialog sensor list
		Iterator<SensorConfig> i = sensors.iterator();
		while (i.hasNext())
		{
			SensorConfig sc = i.next();			
			if (sc.getState() == State.DeletedSensor) 
				i.remove(); 	
		}
	}

	protected void removeSensorButtonClick() 
	{
		if (lastShownSensor == null) return;
		
		DefaultMutableTreeNode node = lastShownSensor.getTreeNode();
		assert(node != null);
		
		DefaultMutableTreeNode previousNode = node.getPreviousSibling();
		node.removeFromParent();
		((DefaultTreeModel) boxTree.getModel()).reload();

		if (lastShownSensor.getState() == State.NewSensor)
			sensors.remove(lastShownSensor);
		else
			lastShownSensor.markDeleted();
		
		lastShownSensor = null;
		
		if (previousNode != null)
			showSensorPage((SensorConfig)previousNode.getUserObject());
		else
			showBoxPage();
	}

	private void showBoxPage() 
	{
		lastShownSensor = null;
		TreePath path = new TreePath(boxTree.getModel().getRoot());
		boxTree.scrollPathToVisible(path);
		boxTree.setSelectionPath(path);
		
/*		DefaultListModel listModel = ((DefaultListModel)boxQueryList.getModel());
		listModel.clear();
		for (RemoteSensor sensor : sensorBox.getSensors())
		{
			for (String queryName : sensor.getRunningQueries())
				listModel.addElement(sensor.getName() + ": " + queryName);
		}*/		
		
		settingsPanelShow("boxSettingsPanel");
	}

	protected void boxNameEditChanged() 
	{
		DefaultMutableTreeNode root = ((DefaultMutableTreeNode) boxTree.getModel().getRoot());
		root.setUserObject(boxNameEdit.getText());
		((DefaultTreeModel) boxTree.getModel()).nodeChanged(root); //reload();
	}

	protected void sensorEditChanged() 
	{
		if (lastShownSensor == null) return;
		
		// TODO: Position from string
//		lastShownSensor.getSensorInfo().position = sensorPositionEdit.getText();
		
		lastShownSensor.getSensorInfo().displayName = sensorNameEdit.getText();
		
		lastShownSensor.changed();		
		((DefaultTreeModel) boxTree.getModel()).reload();
		selectSensorInTree(lastShownSensor);					
	}	

	protected void sensorTypeChanged() 
	{
		if (lastShownSensor == null) return;
		
		SensorType type = (SensorType) sensorTypeComboBox.getSelectedItem();		
		if (type.name.equals(lastShownSensor.getSensorInfo().type)) return;
		
		lastShownSensor.getSensorInfo().type = type.name;

		DefaultTableModel model = (DefaultTableModel)optionsTable.getModel();		
		int rows = model.getRowCount();
		model.setRowCount(0);
		model.setRowCount(rows);
			
		int row = 0;
		for (String key : type.optionsInformation.keySet())
		{
			model.setValueAt(key, row, 0);
			row++;
		}
		
		lastShownSensor.changed();		
		((DefaultTreeModel) boxTree.getModel()).reload();
		selectSensorInTree(lastShownSensor);							
	}	
	
	protected void optionsTableChanged() 
	{
		if (lastShownSensor == null) return;

		DefaultTableModel model = (DefaultTableModel)optionsTable.getModel();		
		lastShownSensor.getSensorInfo().options.clear();
		
		for (int row=0;row<model.getRowCount();row++)
		{
			Object left = model.getValueAt(row, 0);
			Object right = model.getValueAt(row, 1);			
			if (left == null || right == null) continue;
			
			lastShownSensor.getSensorInfo().options.put((String) left, (String) right);
		}
		
		lastShownSensor.changed();		
		((DefaultTreeModel) boxTree.getModel()).reload();
		selectSensorInTree(lastShownSensor);							
	}
	
	protected void okButtonClick() 
	{
		applyButtonClick();
		
		setVisible(false);
	}

	protected void addSensorButtonClick() 
	{
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) boxTree.getModel().getRoot();
		DefaultMutableTreeNode sensorNode = new DefaultMutableTreeNode();

		SensorConfig newSensor = new SensorConfig(null, sensorNode);				
		sensorNode.setUserObject(newSensor);
		rootNode.add(sensorNode);
		sensors.add(newSensor);
		((DefaultTreeModel) boxTree.getModel()).reload();
				
		showSensorPage(newSensor);
	}

	private void boxTreeDoubleClick(Object item) 
	{
		if (item == boxTree.getModel().getRoot())
		{
			showBoxPage();
		}
		else
		{
			SensorConfig sensor = (SensorConfig) ((DefaultMutableTreeNode) item).getUserObject();
			showSensorPage(sensor);
		}
	}

	private void showSensorPage(SensorConfig sensor) 
	{
		lastShownSensor = null;

		DefaultTableModel model = (DefaultTableModel)optionsTable.getModel();
		
		int rows = model.getRowCount();
		model.setRowCount(0);
		model.setRowCount(rows);
		
		Map<String, String> optionsMap = sensor.getSensorInfo().options;
		
		int row = 0;
		for (Entry<String, String> entry : optionsMap.entrySet())
		{
			model.setValueAt(entry.getKey(), row, 0);
			model.setValueAt(entry.getValue(), row, 1);
			row++;
		}
		
		sensorNameEdit.setText(sensor.getSensorInfo().displayName);
		sensorPositionEdit.setText(sensor.getSensorInfo().position.toString());

		String type = sensor.getSensorInfo().type;
		if (type != null)
		{
			boolean ok = false;
			List<SensorType> types = sensorBox.getSensorTypes();		
			for (SensorType curType : types)
				if (curType.name.equals(type))
				{
					sensorTypeComboBox.setSelectedItem(curType);
					ok = true;
					break;
				}
			if (!ok)
				throw new IllegalArgumentException("Invalid sensor type: " + type);
		}
		else
		{
			sensorTypeComboBox.setSelectedIndex(-1);
		}
				
		lastShownSensor = sensor;		
		selectSensorInTree(lastShownSensor);				
		settingsPanelShow("sensorSettingsPanel");
	}

	private void selectSensorInTree(SensorConfig s) 
	{
		TreePath path = new TreePath(s.getTreeNode().getPath());
		boxTree.scrollPathToVisible(path);
		boxTree.setSelectionPath(path);
	}
	
	private void settingsPanelShow(String panelName)
	{
		CardLayout layout = (CardLayout) settingsPanel.getLayout();
		layout.show(settingsPanel, panelName);
	}
}
