package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;

import net.jxta.peer.PeerID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManagerListener;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.PeerResourceManagerService;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.PingMapService;

public class PeerView extends ViewPart implements IP2PDictionaryListener, IPeerResourceUsageManagerListener {

	private static final String UNKNOWN_PEER_NAME = "<unknown>";

	private static PeerView instance;

	private final List<PeerID> foundPeerIDs = Lists.newArrayList();

	private IP2PDictionary p2pDictionary;

	private TableViewer peersTable;

	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = P2PDictionaryService.get();
		p2pDictionary.addListener(this);
		PeerResourceManagerService.get().addListener(this);

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		peersTable = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		peersTable.getTable().setHeaderVisible(true);
		peersTable.getTable().setLinesVisible(true);
		peersTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* Index ****************/
		TableViewerColumn indexColumn = new TableViewerColumn(peersTable, SWT.NONE);
		indexColumn.getColumn().setText("#");
		indexColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				int index = foundPeerIDs.indexOf(cell.getElement());
				cell.setText(String.valueOf(index));
			}
		});
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(peersTable, indexColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				int index1 = foundPeerIDs.indexOf(e1);
				int index2 = foundPeerIDs.indexOf(e2);
				return Integer.compare(index1, index2);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(peersTable, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(determinePeerName((PeerID) cell.getElement()));
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 25, true));
		new ColumnViewerSorter(peersTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determinePeerName((PeerID) e1);
				String n2 = determinePeerName((PeerID) e2);
				return n1.compareTo(n2);
			}
		};
		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(peersTable, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Optional<String> optAddress = P2PDictionaryService.get().getRemotePeerAddress((PeerID) cell.getElement());
				if (optAddress.isPresent()) {
					cell.setText(optAddress.get());
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(addressColumn.getColumn(), new ColumnWeightData(10, 25, true));
		new ColumnViewerSorter(peersTable, addressColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		/************* MEM ****************/
		TableViewerColumn memColumn = new TableViewerColumn(peersTable, SWT.NONE);
		memColumn.getColumn().setText("MEM (%)");
		memColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID peerID = (PeerID) cell.getElement();
				Optional<IResourceUsage> optResourceUsage = PeerResourceManagerService.get().getRemoteResourceUsage(peerID);
				if (optResourceUsage.isPresent()) {
					double memPerc = calcMemPercentage(optResourceUsage.get());
					cell.setText(String.format("%-3.1f", memPerc));
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(memColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, memColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optResourceUsage1 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optResourceUsage2 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e2);
				if (optResourceUsage1.isPresent() && !optResourceUsage2.isPresent()) {
					return 1;
				} else if (!optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return -1;
				} else if (optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return Double.compare(calcMemPercentage(optResourceUsage1.get()), calcMemPercentage(optResourceUsage2.get()));
				}
				return 0;
			}
		};

		/************* CPU ****************/
		TableViewerColumn cpuColumn = new TableViewerColumn(peersTable, SWT.NONE);
		cpuColumn.getColumn().setText("CPU (%)");
		cpuColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID peerID = (PeerID) cell.getElement();
				Optional<IResourceUsage> optResourceUsage = PeerResourceManagerService.get().getRemoteResourceUsage(peerID);
				if (optResourceUsage.isPresent()) {
					double cpuPerc = calcCpuPercentage(optResourceUsage.get());
					cell.setText(String.format("%-3.1f", cpuPerc));
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(cpuColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, cpuColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optResourceUsage1 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optResourceUsage2 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e2);
				if (optResourceUsage1.isPresent() && !optResourceUsage2.isPresent()) {
					return 1;
				} else if (!optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return -1;
				} else if (optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return Double.compare(calcCpuPercentage(optResourceUsage1.get()), calcCpuPercentage(optResourceUsage2.get()));
				}
				return 0;
			}
		};

		/************* Querycounts ****************/
		TableViewerColumn queriesColumn = new TableViewerColumn(peersTable, SWT.NONE);
		queriesColumn.getColumn().setText("Queries");
		queriesColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID peerID = (PeerID) cell.getElement();
				Optional<IResourceUsage> optResourceUsage = PeerResourceManagerService.get().getRemoteResourceUsage(peerID);
				if (optResourceUsage.isPresent()) {
					IResourceUsage usage = optResourceUsage.get();
					cell.setText(usage.getRunningQueriesCount() + " / " + (usage.getRunningQueriesCount() + usage.getStoppedQueriesCount()));
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(queriesColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, queriesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optResourceUsage1 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optResourceUsage2 = PeerResourceManagerService.get().getRemoteResourceUsage((PeerID) e2);
				if (optResourceUsage1.isPresent() && !optResourceUsage2.isPresent()) {
					return 1;
				} else if (!optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return -1;
				} else if (optResourceUsage1.isPresent() && optResourceUsage2.isPresent()) {
					return Double.compare(optResourceUsage1.get().getRunningQueriesCount(), optResourceUsage2.get().getRunningQueriesCount());
				}
				return 0;
			}
		};

		/************* Bandwidth ****************/
		TableViewerColumn netColumn = new TableViewerColumn(peersTable, SWT.NONE);
		netColumn.getColumn().setText("Network");
		netColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID peerID = (PeerID) cell.getElement();
				Optional<IResourceUsage> optResourceUsage = PeerResourceManagerService.get().getRemoteResourceUsage(peerID);
				if (optResourceUsage.isPresent()) {
					IResourceUsage usage = optResourceUsage.get();
					cell.setText((usage.getNetInputRate() + usage.getNetOutputRate()) + " / " + usage.getNetBandwidthMax());
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(netColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, netColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		/************* Ping ****************/
		TableViewerColumn pingColumn = new TableViewerColumn(peersTable, SWT.NONE);
		pingColumn.getColumn().setText("Ping (ms)");
		pingColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID peerID = (PeerID) cell.getElement();
				Optional<Double> optPing = PingMapService.get().getPing(peerID);
				if (optPing.isPresent()) {
					cell.setText(String.valueOf((long)(double)optPing.get()));
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(pingColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, pingColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<Double> ping1 = PingMapService.get().getPing((PeerID)e1);
				Optional<Double> ping2 = PingMapService.get().getPing((PeerID)e2);
				
				if( !ping1.isPresent() && !ping2.isPresent() ) {
					return 0;
				}
				if( !ping1.isPresent() ) {
					return -1;
				}
				if( !ping2.isPresent() ) {
					return 1;
				}
				
				return Double.compare(ping1.get(), ping2.get());
			}
		};

		/************* PeerID ****************/
		TableViewerColumn peerIDColumn = new TableViewerColumn(peersTable, SWT.NONE);
		peerIDColumn.getColumn().setText("PeerID");
		peerIDColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((PeerID) cell.getElement()).toString());
			}
		});
		tableColumnLayout.setColumnData(peerIDColumn.getColumn(), new ColumnWeightData(10, 25, true));
		sorter = new ColumnViewerSorter(peersTable, peerIDColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return ((PeerID) e1).toString().compareTo(((PeerID) e2).toString());
			}
		};

		peersTable.setInput(foundPeerIDs);
		refreshTable();

		instance = this;
	}

	private static double calcMemPercentage(IResourceUsage resourceUsage) {
		return 100.0 - (((double) resourceUsage.getMemFreeBytes() / (double) resourceUsage.getMemMaxBytes()) * 100.0);
	}

	private static double calcCpuPercentage(IResourceUsage resourceUsage) {
		return 100.0 - ((resourceUsage.getCpuFree() / resourceUsage.getCpuMax()) * 100.0);
	}
	
	@Override
	public void dispose() {
		instance = null;

		P2PDictionaryService.get().removeListener(this);
		PeerResourceManagerService.get().removeListener(this);

		super.dispose();
	}

	public void refreshTable() {
		final IP2PDictionary p2pDictionary = P2PDictionaryService.get();

		foundPeerIDs.clear();
		foundPeerIDs.addAll(p2pDictionary.getRemotePeerIDs());

		final Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!peersTable.getTable().isDisposed()) {
						peersTable.refresh();
					}
				}
			});
		}
	}

	@Override
	public void setFocus() {
		peersTable.getTable().setFocus();
	}

	public static Optional<PeerView> getInstance() {
		return Optional.fromNullable(instance);
	}

	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement advertisement) {
		// do nothing
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement advertisement) {
		// do nothing
	}

	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		refreshTable();
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		refreshTable();
	}

	private String determinePeerName(PeerID peerID) {
		Optional<String> optPeerName = p2pDictionary.getRemotePeerName(peerID);
		return optPeerName.isPresent() ? optPeerName.get() : UNKNOWN_PEER_NAME;
	}

	@Override
	public void remoteResourceUsageChanged(IPeerResourceUsageManager sender, IResourceUsage peerID) {
		refreshTable();
	}

	@Override
	public void localResourceUsageChanged(IPeerResourceUsageManager sender, IResourceUsage localUsage) {
		refreshTable();
	}
}
