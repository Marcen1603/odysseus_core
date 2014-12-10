package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;

public class SmartDeviceShowView extends ViewPart implements
		IP2PDictionaryListener, ISelectionProvider {
	private static final long REFRESH_INTERVAL_MILLIS = 5000;

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartDeviceShowView.class);

	private IP2PDictionary p2pDictionary;
	private final List<PeerID> foundPeerIDs = Lists.newArrayList();
	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private TableViewer smartDevicesTable;
	private RepeatingJobThread refresher;
	private Collection<PeerID> refreshing = Lists.newLinkedList();

	private static SmartDeviceShowView instance;

	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = SmartHomeRCPActivator.getP2PDictionary();
		p2pDictionary.addListener(this);

		setPartName("Smart Devices");

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		smartDevicesTable = new TableViewer(tableComposite, SWT.SINGLE);
		smartDevicesTable.getTable().setHeaderVisible(true);
		smartDevicesTable.getTable().setLinesVisible(true);
		smartDevicesTable
				.setContentProvider(ArrayContentProvider.getInstance());

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(smartDevicesTable,
				SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				cell.setText(determinePeerName(pid));
				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(),
				new ColumnWeightData(10, 25, true));
		/*
		 * ColumnViewerSorter sorter = new ColumnViewerSorter(smartDevicesTable,
		 * nameColumn) {
		 * 
		 * @Override protected int doCompare(Viewer viewer, Object e1, Object
		 * e2) { String n1 = determinePeerName((PeerID) e1); String n2 =
		 * determinePeerName((PeerID) e2); return n1.compareTo(n2); } };
		 * sorter.setSorter(sorter, ColumnViewerSorter.NONE);
		 */

		/************* Context ****************/
		TableViewerColumn contextnameColumn = new TableViewerColumn(
				smartDevicesTable, SWT.NONE);
		contextnameColumn.getColumn().setText("Context");
		contextnameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				cell.setText(determinePeerContextName(pid));
				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(contextnameColumn.getColumn(),
				new ColumnWeightData(10, 25, true));

		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(
				smartDevicesTable, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (isLocalID((PeerID) cell.getElement())) {
					try {
						cell.setText(InetAddress.getLocalHost()
								.getHostAddress()
								+ ":"
								+ SmartHomeRCPActivator.getP2PNetworkManager()
										.getPort());
					} catch (UnknownHostException e) {
						cell.setText("<unknown>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_GRAY));
				} else {
					cell.setText("<unknown>");
					Optional<String> optAddress = SmartHomeRCPActivator
							.getPeerDictionary().getRemotePeerAddress(
									(PeerID) cell.getElement());
					if (optAddress.isPresent()) {
						cell.setText(optAddress.get());
					} else {
						cell.setText("<unknown>");
					}
				}
			}
		});
		tableColumnLayout.setColumnData(addressColumn.getColumn(),
				new ColumnWeightData(10, 25, true));
		/*
		 * new ColumnViewerSorter(smartDevicesTable, addressColumn) {
		 * 
		 * @Override protected int doCompare(Viewer viewer, Object e1, Object
		 * e2) { return 0; } };
		 */

		hideSelectionIfNeeded(smartDevicesTable);

		smartDevicesTable.setInput(foundPeerIDs);

		refresher = new RepeatingJobThread(REFRESH_INTERVAL_MILLIS,
				"Refresher of PeerView") {
			@Override
			public void doJob() {
				refresh();
			}
		};
		refresher.start();

		smartDevicesTable.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				if (selection.size() == 1) {
					Object firstElement = selection.getFirstElement();

					if (firstElement instanceof PeerID) {
						PeerID selectedPeer = (PeerID) firstElement;

						System.out.println("double click on peerID:"
								+ selectedPeer.intern());

						// Open Configuration for PeerID:selectedPeer.intern()
						// 1. get the current configuration from the peer
						// 2. display the configuration
						// 3. click on save button: send the new configuration
						// to the peer

					}
				} else {
					// Configuration for more then one peer at the same time is
					// not implemented.
				}
			}
		});

		smartDevicesTable
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();

						if (selection.size() == 1) {
							Object firstElement = selection.getFirstElement();

							if (firstElement instanceof PeerID) {
								@SuppressWarnings("unused")
								PeerID selectedPeer = (PeerID) firstElement;

								// System.out.println("SmartDeviceView selectionChanged to peerID:"+selectedPeer.intern());
							}
						}
					}
				});

		getSite().setSelectionProvider(smartDevicesTable);
		instance = this;
	}

	public void refresh() {
		Collection<PeerID> foundPeerIDsCopy = null;
		synchronized (foundPeerIDs) {
			try {
				foundPeerIDs.clear();
				if (SmartHomeRCPActivator.getPeerDictionary() != null
						&& SmartHomeRCPActivator.getPeerDictionary()
								.getRemotePeerIDs() != null) {
					foundPeerIDs.add(SmartHomeRCPActivator.getP2PNetworkManager().getLocalPeerID());
					foundPeerIDs.addAll(SmartHomeRCPActivator
							.getPeerDictionary().getRemotePeerIDs());
				}
				foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
			} catch (NullPointerException ex) {
				LOG.error(ex.getMessage(), ex);
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		}
		refreshTableAsync();

		if(foundPeerIDsCopy!=null){
		for (final PeerID remotePeerID : foundPeerIDsCopy) {

			synchronized (refreshing) {
				if (refreshing.contains(remotePeerID)) {
					continue;
				}

				refreshing.add(remotePeerID);
			}

			/*
			 * Thread t = new Thread(new Runnable() {
			 * 
			 * @Override public void run() { try {
			 * Future<Optional<IResourceUsage>> futureUsage =
			 * usageManager.getRemoteResourceUsage(remotePeerID); try {
			 * Optional<IResourceUsage> optResourceUsage = futureUsage.get(); if
			 * (optResourceUsage.isPresent()) { IResourceUsage resourceUsage =
			 * optResourceUsage.get(); synchronized (usageMap) {
			 * usageMap.put(remotePeerID, resourceUsage); } refreshTableAsync();
			 * } } catch (InterruptedException | ExecutionException e) {
			 * LOG.error("Could not get resource usage", e); }
			 * 
			 * } finally { synchronized (refreshing) {
			 * refreshing.remove(remotePeerID); } } } });
			 * 
			 * t.setDaemon(true); t.setName("PeerView update for peer " +
			 * p2pDictionary.getRemotePeerName(remotePeerID)); t.start();
			 */
		}
		}

		synchronized (usageMap) {
			for (PeerID peerID : usageMap.keySet().toArray(new PeerID[0])) {
				if (!foundPeerIDs.contains(peerID)) {
					usageMap.remove(peerID);
				}
			}
		}
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

							synchronized (foundPeerIDs) {
								setPartName("Smart Devices ("
										+ foundPeerIDs.size() + ")");
							}
						}
					}
				}
			});
		}
	}

	private static String determinePeerName(PeerID id) {
		try {
			if (isLocalID(id)) {
				return "<local>"+SmartHomeRCPActivator.getP2PNetworkManager().getLocalPeerName();
			}
		} catch (NullPointerException ex) {

		}
		return SmartHomeRCPActivator.getPeerDictionary().getRemotePeerName(id);
	}

	@SuppressWarnings("unused")
	private static String determinePeerContextName(PeerID pid) {
		// TODO:
		return "none";
	}

	private static boolean isLocalID(PeerID pid) {
		boolean value = false;
		try {
			if (SmartHomeRCPActivator.getP2PNetworkManager() != null
					&& SmartHomeRCPActivator.getP2PNetworkManager()
							.getLocalPeerID() != null && pid != null) {
				value = SmartHomeRCPActivator.getP2PNetworkManager()
						.getLocalPeerID().equals(pid);
			}
		} catch (NullPointerException ex) {
			throw ex;
		}
		return value;
	}

	private static void hideSelectionIfNeeded(final TableViewer tableViewer) {
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (tableViewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					tableViewer.setSelection(new StructuredSelection());
				}
			}
		});
	}

	@Override
	public void setFocus() {
		smartDevicesTable.getTable().setFocus();
	}

	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceAdded");

	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceRemoved");

	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImported");

	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved");

	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExported");

	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExportRemoved");

	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {

	}

	@Override
	public ISelection getSelection() {
		return null;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {

	}

	@Override
	public void setSelection(ISelection selection) {
		
	}
	
	public static Optional<SmartDeviceShowView> getInstance(){
		return Optional.fromNullable(instance);
	}

	public ArrayList<PeerID> getSmartDevicesCopy() {
		return Lists.newArrayList(foundPeerIDs);
	}
}
