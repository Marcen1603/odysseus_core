package de.uniol.inf.is.odysseus.iec61970.dataaccessclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.Client;
import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.handler.ActionHandler;
import de.uniol.inf.is.odysseus.iec61970.dataaccessclient.handler.ContentHandler;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceIDService;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.ISession;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ApplicationGui extends javax.swing.JFrame implements ActionListener{

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private ContentHandler cHandler;
	private ActionHandler aHandler;
	private JScrollPane jScrollPane1;
	private JTable queryresultTable;
	private JScrollPane jScrollPane2;
	private JLabel resourceIDText;
	private JLabel resourceURIText;
	private JMenu mainMenuBar;
	private JMenuItem exitMenuItem;
	private JButton ok;
	private JLabel resourceURI;
	private JLabel resourceID;
	private JLabel itemAttribute;
	private JCheckBoxMenuItem aboutMenuItem;
	private JButton aboutOK;
	private JTextArea text;
	private JDialog about;
	private JScrollPane jScrollPane4;
	private JScrollPane jScrollPane3;
	private JList itemAttributeList;
	private JLabel aggregateLabel;
	private JButton aggregateOKButton;
	private JList aggregateList;
	private JDialog aggregateDialog;
	private JButton aggregate;
	private JDialog tsdaInfoDialog;
	private JButton info;
	private JButton deselect;
	private JButton select;
	private JMenu jMenu1;
	private JMenuBar menu;
	private JTable pathnameTable;
	private TableModel queryresultTableModel = null;

	public ApplicationGui(IFacade facade, IResourceIDService service, String host, int port, ISession session) {
		super("IEC 61970-404/407 Stream Client (Modus: "+(Client.hMode ? "HSDA" : "TSDA")+")");
		this.cHandler = new ContentHandler(facade, service);
		this.aHandler = new ActionHandler(host, port, session, facade);
		initGUI();
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jScrollPane1 = new JScrollPane();
				{
					String[] header = new String[] { "Pfadname", "CIM Klasse/"+cHandler.getFacade().getMode().toUpperCase()+" Type", cHandler.getFacade().getMode().toUpperCase()+" Objekt" };
					TableModel pathnameTableModel = 
						new DefaultTableModel(
								cHandler.getTableData(header), header);
					pathnameTable = new JTable();
					jScrollPane1.setViewportView(pathnameTable);
					pathnameTable.setModel(pathnameTableModel);
				}
			}
			{
				jScrollPane2 = new JScrollPane();
				jScrollPane2.setEnabled(true);
				jScrollPane2.setOpaque(true);
				jScrollPane2.setAutoscrolls(true);
				{
					queryresultTableModel = 
						new DefaultTableModel(new String[20][],
								new String[] { "Item", "Value/Values", "Timestamp", "Datatype", "Quality" });
					queryresultTable = new JTable();
					GroupLayout queryresultTableLayout = new GroupLayout((JComponent)queryresultTable);
					queryresultTable.setLayout(queryresultTableLayout);
					jScrollPane2.setViewportView(queryresultTable);
					queryresultTable.setModel(queryresultTableModel);
					queryresultTableLayout.setVerticalGroup(queryresultTableLayout.createParallelGroup());
					queryresultTableLayout.setHorizontalGroup(queryresultTableLayout.createParallelGroup());
				}
			}
			{
					aggregate = new JButton();
					aggregate.setText("Aggregate");
					aggregate.addActionListener(this);
					if(Client.hMode) {
						aggregate.setVisible(false);
					}
			}
			{
				tsdaInfoDialog = new JDialog(this);
				tsdaInfoDialog.setPreferredSize(new java.awt.Dimension(936, 272));
				tsdaInfoDialog.getContentPane().setLayout(null);
				tsdaInfoDialog.setTitle( (Client.hMode ? "HSDA" : "TSDA")+" Info");
				tsdaInfoDialog.setLocationByPlatform(true);
				{
					itemAttribute = new JLabel();
					tsdaInfoDialog.getContentPane().add(itemAttribute);
					itemAttribute.setText("ItemAttribute:");
					itemAttribute.setBounds(6, 143, 88, 23);
				}
				{
					resourceID = new JLabel();
					tsdaInfoDialog.getContentPane().add(resourceID);
					resourceID.setText("Resource Identifier:");
					resourceID.setBounds(6, 99, 122, 15);
				}
				{
					resourceURI = new JLabel();
					tsdaInfoDialog.getContentPane().add(resourceURI);
					resourceURI.setText("Resource URI:");
					resourceURI.setBounds(6, 52, 87, 15);
				}
				{
					ok = new JButton();
					ok.addActionListener(this);
					tsdaInfoDialog.getContentPane().add(ok);
					ok.setText("OK");
					ok.setBounds(876, 209, 44, 27);
				}
				{
					resourceURIText = new JLabel();
					tsdaInfoDialog.getContentPane().add(resourceURIText);
					resourceURIText.setBounds(147, 49, 735, 20);
					resourceURIText.setText("-");
				}
				{
					resourceIDText = new JLabel();
					tsdaInfoDialog.getContentPane().add(resourceIDText);
					tsdaInfoDialog.getContentPane().add(getJScrollPane3());
					resourceIDText.setText("-");
					resourceIDText.setBounds(147, 97, 729, 18);
				}
				tsdaInfoDialog.setSize(936, 272);
			}
			{
				info = new JButton();
				info.setText("Info");
				info.addActionListener(this);
			}
			{
				select = new JButton();
				select.setText("Select");
				select.addActionListener(this);
			}
			{
				deselect = new JButton();
				deselect.setText("Deselect All");
				deselect.addActionListener(this);
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(select, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				    .addComponent(deselect, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				    .addComponent(info, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				    .addComponent(aggregate, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 406, GroupLayout.PREFERRED_SIZE));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, 0, 986, Short.MAX_VALUE)
				    .addComponent(jScrollPane2, GroupLayout.Alignment.LEADING, 0, 986, Short.MAX_VALUE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addGap(0, 589, Short.MAX_VALUE)
				        .addComponent(aggregate, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
				        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE)
				        .addComponent(info, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
				        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE)
				        .addComponent(deselect, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
				        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE)
				        .addComponent(select, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap());
			{
				menu = new JMenuBar();
				setJMenuBar(menu);
				{
					mainMenuBar = new JMenu();
					menu.add(mainMenuBar);
					mainMenuBar.setText("Main");
					{
						exitMenuItem = new JMenuItem();
						mainMenuBar.add(exitMenuItem);
						exitMenuItem.setText("Beenden");
						exitMenuItem.addActionListener(this);
					}
				}
				{
					jMenu1 = new JMenu();
					menu.add(jMenu1);
					jMenu1.setText("About");
					jMenu1.setBounds(-53, 0, 53, 19);
					jMenu1.add(getAboutMenuItem());
				}
			}
			pack();
			this.setSize(1008, 657);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(select.getActionCommand())) {
			ArrayList<String> pathnames = new ArrayList<String>();
			for(int zeile : pathnameTable.getSelectedRows()) {
				pathnames.add((String) pathnameTable.getValueAt(zeile, 0));
			}
			if(!pathnames.isEmpty()) {
				aHandler.setTableModel(queryresultTableModel);
				aHandler.subscribe(pathnames);
			}
		}
		else if(e.getActionCommand().equals(deselect.getActionCommand())) {
			ArrayList<String> pathnames = new ArrayList<String>();
//			for(int zeile : pathnameTable.getSelectedRows()) {
//				pathnames.add((String) pathnameTable.getValueAt(zeile, 0));
//			}
			aHandler.unsubscribe();
			pathnameTable.clearSelection();
		}
		else if(e.getActionCommand().equals(info.getActionCommand())) {
			if(this.pathnameTable.getSelectedRow()!=-1) {
				resourceURIText.setText(cHandler.fillInfoBox((String)this.pathnameTable.getValueAt(this.pathnameTable.getSelectedRow(), 0))[0]);
				resourceIDText.setText(cHandler.fillInfoBox((String)this.pathnameTable.getValueAt(this.pathnameTable.getSelectedRow(), 0))[1]);
				//itemAttributeText.setText(cHandler.fillInfoBox((String)this.pathnameTable.getValueAt(this.pathnameTable.getSelectedRow(), 0))[2]);
				if(Client.hMode) {
					jScrollPane3.setVisible(false);
					itemAttribute.setVisible(false);
				}
				else {
					jScrollPane3.setViewportView(getItemAttributeList(pathnameTable.getValueAt(pathnameTable.getSelectedRow(),0).toString()));
				}
				tsdaInfoDialog.setVisible(true);
			}
		}
		else if(e.getActionCommand().equals(ok.getActionCommand())) {
			tsdaInfoDialog.setVisible(false);
		}
		else if(e.getActionCommand().equals(getAboutOK().getActionCommand())) {
			getAbout().setVisible(false);
		}
		else if(e.getActionCommand().equals(exitMenuItem.getActionCommand())) {
			System.exit(0);
		}
		else if(e.getActionCommand().equals(aggregate.getActionCommand())) {
			getAggregateDialog().setVisible(true);
		}
		else if(e.getActionCommand().equals(aboutMenuItem.getActionCommand())) {
			getAbout().setVisible(true);
		}
		else if(e.getActionCommand().equals(aggregateOKButton.getActionCommand())) {
			getAggregateDialog().setVisible(false);
		}
	}

	private JDialog getAggregateDialog() {
		if(aggregateDialog == null) {
			aggregateDialog = new JDialog(this);
			aggregateDialog.getContentPane().setLayout(null);
			aggregateDialog.setPreferredSize(new java.awt.Dimension(452, 242));
			aggregateDialog.getContentPane().add(getJScrollPane4());
			aggregateDialog.getContentPane().add(getAggregateOKButton());
			aggregateDialog.getContentPane().add(getAggregateLabel());
			aggregateDialog.setSize(452, 242);
		}
		return aggregateDialog;
	}
	
	private JList getAggregateList() {
//		if(aggregateList == null) {
			ListModel aggregateListModel = 
				new DefaultComboBoxModel(cHandler.getAggregateList());
			aggregateList = new JList();
			aggregateList.setModel(aggregateListModel);
			aggregateList.setBounds(151, 6, 226, 200);
//		}
		return aggregateList;
	}
	
	private JButton getAggregateOKButton() {
		if(aggregateOKButton == null) {
			aggregateOKButton = new JButton();
			aggregateOKButton.setText("ok");
			aggregateOKButton.setBounds(393, 179, 43, 27);
			aggregateOKButton.addActionListener(this);
		}
		return aggregateOKButton;
	}
	
	private JLabel getAggregateLabel() {
		if(aggregateLabel == null) {
			aggregateLabel = new JLabel();
			aggregateLabel.setText("AggregateDefinition:");
			aggregateLabel.setBounds(6, 101, 139, 15);
		}
		return aggregateLabel;
	}
	
	private JList getItemAttributeList(String pathname) {
		if(itemAttributeList == null && cHandler.fillItemAttributeList(pathname)!= null && !Client.hMode) {
			ListModel itemAttributeListModel = 
				new DefaultComboBoxModel(cHandler.fillItemAttributeList(pathname));
			itemAttributeList = new JList();
			itemAttributeList.setModel(itemAttributeListModel);
			itemAttributeList.setBounds(204, 143, 200, 95);
			itemAttributeList.setPreferredSize(new java.awt.Dimension(413, 89));
		}
		else if(cHandler.fillItemAttributeList(pathname)== null && !Client.hMode) {
			itemAttributeList = null;
		}
		return itemAttributeList;
	}
	
	private JScrollPane getJScrollPane3() {
		if(jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setBounds(141, 143, 729, 95);
//			jScrollPane3.setViewportView(getItemAttributeList(""));
		}
		return jScrollPane3;
	}
	
	private JScrollPane getJScrollPane4() {
		if(jScrollPane4 == null) {
			jScrollPane4 = new JScrollPane();
			jScrollPane4.setBounds(151, 6, 226, 200);
			jScrollPane4.setViewportView(getAggregateList());
		}
		return jScrollPane4;
	}
	
	private JDialog getAbout() {
		if(about == null) {
			about = new JDialog(this);
			about.getContentPane().setLayout(null);
			about.setPreferredSize(new java.awt.Dimension(332, 148));
			about.getContentPane().add(getText());
			about.getContentPane().add(getAboutOK());
			about.setSize(332, 148);
		}
		return about;
	}
	
	private JTextArea getText() {
		if(text == null) {
			text = new JTextArea();
			text.setText("HSDA/TSDA Stream Client Demo Application 1.0 \n\n\n Copyright Mart Köhler");
			text.setBounds(6, 6, 310, 75);
			text.setEnabled(false);
			text.setBackground(getAbout().getBackground());
		}
		return text;
	}
	
	private JButton getAboutOK() {
		if(aboutOK == null) {
			aboutOK = new JButton();
			aboutOK.setText("ok");
			aboutOK.setBounds(273, 85, 43, 27);
			aboutOK.addActionListener(this);
		}
		return aboutOK;
	}
	
	private JCheckBoxMenuItem getAboutMenuItem() {
		if(aboutMenuItem == null) {
			aboutMenuItem = new JCheckBoxMenuItem();
			aboutMenuItem.setText("Application");
			aboutMenuItem.addActionListener(this);
		}
		return aboutMenuItem;
	}

}
