package de.uniol.inf.is.odysseus.peer.smarthome.rcp.views;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.ISmartDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.rcp.SmartHomeRCPActivator;


public class SmartDeviceLogicView extends ViewPart {
	private static final Logger LOG = LoggerFactory.getLogger(SmartDeviceLogicView.class);
	private ASmartDevice localSmartDevice;
	ISmartDeviceListener smartDeviceListener = new ISmartDeviceListener(){
		@Override
		public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
			if(device instanceof Sensor){
				LOG.debug("fieldDeviceConnected Sensor:"+device.getName());
				
			}else if(device instanceof Actor){
				LOG.debug("fieldDeviceConnected Actor:"+device.getName());
				
			}else{
				LOG.debug("fieldDeviceConnected device:"+device.getName());
				
			}
		}

		@Override
		public void readyStateChanged(ASmartDevice smartDevice, boolean state) {
		}
	};
	private TableViewer smartDevicesTable;
	private List<LogicRule> logicRules = Lists.newArrayList();
	private Collection<LogicRule> refreshing = Lists.newLinkedList();
	
	@Override
	public void createPartControl(Composite parent) {
		localSmartDevice = SmartHomeRCPActivator.getLocalSmartDevice();
		localSmartDevice.addListener(smartDeviceListener);
		
		setPartName("Smart Device Logic");
		
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		smartDevicesTable = new TableViewer(tableComposite, SWT.SINGLE);
		smartDevicesTable.getTable().setHeaderVisible(true);
		smartDevicesTable.getTable().setLinesVisible(true);
		smartDevicesTable.setContentProvider(ArrayContentProvider.getInstance());
		
		//TODO: Show current running logic rules of the local SmartDevice:
		//TODO: further show logic of the selected smart device:
		
		/************* Entität ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(smartDevicesTable, SWT.NONE);
		nameColumn.getColumn().setText("Entität");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				//LogicRule rule = (LogicRule) cell.getElement();
				cell.setText("Entität 1");
				/*
				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				*/
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 25, true));
		
		
		/************* Aktivität ****************/
		TableViewerColumn contextnameColumn = new TableViewerColumn(smartDevicesTable, SWT.NONE);
		contextnameColumn.getColumn().setText("Aktivität");
		contextnameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				LogicRule rule = (LogicRule) cell.getElement();
				cell.setText(rule.getActivityName());
				/*
				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				*/
			}
		});
		tableColumnLayout.setColumnData(contextnameColumn.getColumn(), new ColumnWeightData(10, 25, true));
		
		
		
		/************* Reaktion ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(smartDevicesTable, SWT.NONE);
		addressColumn.getColumn().setText("Reaktion");
		addressColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				LogicRule rule = (LogicRule) cell.getElement();
				cell.setText("Actor:"+rule.getActor().getName()+" ");
				
				
				/*
				if (isLocalID((PeerID) cell.getElement())) {
					try {
						cell.setText(InetAddress.getLocalHost().getHostAddress() + ":" + SmartHomeRCPActivator.getP2PNetworkManager().getPort());
					} catch (UnknownHostException e) {
						cell.setText("<unknown>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				} else {
					Optional<String> optAddress = SmartHomeRCPActivator.getP2PDictionary().getRemotePeerAddress((PeerID) cell.getElement());
					if (optAddress.isPresent()) {
						cell.setText(optAddress.get());
					} else {
						cell.setText("<unknown>");
					}
				}
				*/
			}
		});
		tableColumnLayout.setColumnData(addressColumn.getColumn(), new ColumnWeightData(10, 25, true));
		
		
		smartDevicesTable.setInput(getLogicRules());
		
		refreshAsync();
		
	}
	
	
	private void refreshAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					
					refresh();
				}
			}
		});
		t.setName("Smart Device LogicRule refresher thread.");
		t.setDaemon(true);
		t.start();
	}


	public void refresh() {
		Collection<LogicRule> logicRulesCopy = null;
		synchronized (getLogicRules()) {
			getLogicRules().clear();
			getLogicRules().addAll(localSmartDevice.getLogicRules());
			logicRulesCopy = Lists.newArrayList(getLogicRules());
		}
		refreshTableAsync();
		
		for (final LogicRule remotePeerID : logicRulesCopy) {
			synchronized (refreshing) {
				if (refreshing.contains(remotePeerID)) {
					continue;
				}
				refreshing.add(remotePeerID);
			}
		}
		
		//methodXY();
	}
	
	
	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {
				@Override
				public void run() {
					synchronized (smartDevicesTable) {
						if (!smartDevicesTable.getTable().isDisposed()) {
							smartDevicesTable.refresh();

							
							//synchronized (foundPeerIDs) {
							//	setPartName("Smart Devices (" + foundPeerIDs.size() + ")");
							//}
							
						}
					}
				}
			});
		}
	}
	
	
	//methodXY
	/*
	Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				Future<Optional<IResourceUsage>> futureUsage = usageManager.getRemoteResourceUsage(remotePeerID);
				try {
					Optional<IResourceUsage> optResourceUsage = futureUsage.get();
					if (optResourceUsage.isPresent()) {
						IResourceUsage resourceUsage = optResourceUsage.get();
						synchronized (usageMap) {
							usageMap.put(remotePeerID, resourceUsage);
						}
						refreshTableAsync();
					}
				} catch (InterruptedException | ExecutionException e) {
					LOG.error("Could not get resource usage", e);
				}

			} finally {
				synchronized (refreshing) {
					refreshing.remove(remotePeerID);
				}
			}
		}
	});

	t.setDaemon(true);
	t.setName("PeerView update for peer " + p2pDictionary.getRemotePeerName(remotePeerID));
	t.start();
	*/

	private Collection<LogicRule> getLogicRules() {
		return this.logicRules;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
