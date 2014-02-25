package de.uniol.inf.is.odysseus.peer.rcp.views;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class PeerView extends ViewPart implements IP2PDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerView.class);

	private static final String UNKNOWN_PEER_NAME = "<unknown>";
	private static final long REFRESH_INTERVAL_MILLIS = 5000;
	private static PeerView instance;

	private final Map<IResourceUsage, PeerID> peerIDMap = Maps.newHashMap();
	private final List<IResourceUsage> resourceUsages = Lists.newArrayList();

	private IP2PDictionary p2pDictionary;

	private TableViewer peersTable;
	private RepeatingJobThread refresher;

	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = RCPP2PNewPlugIn.getP2PDictionary();
		p2pDictionary.addListener(this);

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
				int index = resourceUsages.indexOf(cell.getElement());
				cell.setText(String.valueOf(index));
			}
		});
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(peersTable, indexColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				int index1 = resourceUsages.indexOf(e1);
				int index2 = resourceUsages.indexOf(e2);
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
				cell.setText(determinePeerName((IResourceUsage) cell.getElement()));
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 25, true));
		new ColumnViewerSorter(peersTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determinePeerName((IResourceUsage) e1);
				String n2 = determinePeerName((IResourceUsage) e2);
				return n1.compareTo(n2);
			}
		};
		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(peersTable, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Optional<PeerID> optPeerID = determinePeerID((IResourceUsage) cell.getElement());
				if (optPeerID.isPresent()) {
					Optional<String> optAddress = RCPP2PNewPlugIn.getP2PDictionary().getRemotePeerAddress(optPeerID.get());
					if (optAddress.isPresent()) {
						cell.setText(optAddress.get());
					} else {
						cell.setText("<unknown>");
					}
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
				IResourceUsage usage = (IResourceUsage) cell.getElement();
				double memPerc = calcMemPercentage(usage);
				cell.setText(String.format("%-3.1f", memPerc));
			}
		});
		tableColumnLayout.setColumnData(memColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, memColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IResourceUsage usage1 = (IResourceUsage) e1;
				IResourceUsage usage2 = (IResourceUsage) e2;
				return Double.compare(calcMemPercentage(usage1), calcMemPercentage(usage2));
			}
		};

		/************* CPU ****************/
		TableViewerColumn cpuColumn = new TableViewerColumn(peersTable, SWT.NONE);
		cpuColumn.getColumn().setText("CPU (%)");
		cpuColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IResourceUsage usage = (IResourceUsage) cell.getElement();
				double cpuPerc = calcCpuPercentage(usage);
				cell.setText(String.format("%-3.1f", cpuPerc));
			}
		});
		tableColumnLayout.setColumnData(cpuColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, cpuColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IResourceUsage usage1 = (IResourceUsage) e1;
				IResourceUsage usage2 = (IResourceUsage) e2;
				return Double.compare(calcCpuPercentage(usage1), calcCpuPercentage(usage2));
			}
		};

		/************* Querycounts ****************/
		TableViewerColumn queriesColumn = new TableViewerColumn(peersTable, SWT.NONE);
		queriesColumn.getColumn().setText("Queries");
		queriesColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IResourceUsage usage = (IResourceUsage) cell.getElement();
				cell.setText(usage.getRunningQueriesCount() + " / " + (usage.getRunningQueriesCount() + usage.getStoppedQueriesCount()));
			}
		});
		tableColumnLayout.setColumnData(queriesColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, queriesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IResourceUsage usage1 = (IResourceUsage) e1;
				IResourceUsage usage2 = (IResourceUsage) e2;
				return Double.compare(usage1.getRunningQueriesCount(), usage2.getRunningQueriesCount());
			}
		};

		/************* Bandwidth ****************/
		TableViewerColumn netColumn = new TableViewerColumn(peersTable, SWT.NONE);
		netColumn.getColumn().setText("Network");
		netColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IResourceUsage usage = (IResourceUsage) cell.getElement();
				cell.setText((usage.getNetInputRate() + usage.getNetOutputRate()) + " / " + usage.getNetBandwidthMax());
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
				Optional<PeerID> optPeerID = determinePeerID((IResourceUsage) cell.getElement());

				if (optPeerID.isPresent()) {
					Optional<Double> optPing = RCPP2PNewPlugIn.getPingMap().getPing(optPeerID.get());
					if (optPing.isPresent()) {
						cell.setText(String.valueOf((long) (double) optPing.get()));
					} else {
						cell.setText("<unknown>");
					}
				} else {
					cell.setText("<unknown>");
				}
			}
		});
		tableColumnLayout.setColumnData(pingColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, pingColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<PeerID> optPeerID1 = determinePeerID((IResourceUsage) e1);
				Optional<PeerID> optPeerID2 = determinePeerID((IResourceUsage) e2);

				if (!optPeerID1.isPresent() && !optPeerID2.isPresent()) {
					return 0;
				}
				if (!optPeerID1.isPresent()) {
					return -1;
				}
				if (!optPeerID2.isPresent()) {
					return 1;
				}

				Optional<Double> ping1 = RCPP2PNewPlugIn.getPingMap().getPing(optPeerID1.get());
				Optional<Double> ping2 = RCPP2PNewPlugIn.getPingMap().getPing(optPeerID2.get());

				if (!ping1.isPresent() && !ping2.isPresent()) {
					return 0;
				}
				if (!ping1.isPresent()) {
					return -1;
				}
				if (!ping2.isPresent()) {
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
				cell.setText(determinePeerID((IResourceUsage) cell.getElement()).get().toString());
			}
		});
		tableColumnLayout.setColumnData(peerIDColumn.getColumn(), new ColumnWeightData(10, 25, true));
		sorter = new ColumnViewerSorter(peersTable, peerIDColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		peersTable.setInput(resourceUsages);
		refreshTableAsync();

		refresher = new RepeatingJobThread(REFRESH_INTERVAL_MILLIS, "Refresher of PeerView") {
			@Override
			public void doJob() {
				refreshTableAsync();
			}
		};
		refresher.start();

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
		p2pDictionary.removeListener(this);

		if (refresher != null) {
			refresher.stopRunning();
			refresher = null;
		}

		super.dispose();
	}

	public void refreshTableAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (resourceUsages) {
					resourceUsages.clear();
					peerIDMap.clear();

					IPeerResourceUsageManager usageManager = RCPP2PNewPlugIn.getPeerResourceUsageManager();
					for (PeerID remotePeerID : usageManager.getRemotePeerIDs()) {
						Future<Optional<IResourceUsage>> futureUsage = usageManager.getRemoteResourceUsage(remotePeerID);
						try {
							Optional<IResourceUsage> optResourceUsage = futureUsage.get();
							if (optResourceUsage.isPresent()) {
								resourceUsages.add(optResourceUsage.get());
								peerIDMap.put(optResourceUsage.get(), remotePeerID);
							}
						} catch (InterruptedException | ExecutionException e) {
							LOG.error("Could not get resource usage", e);
						}
					}

					Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.asyncExec(new Runnable() {

							@Override
							public void run() {
								if (!peersTable.getTable().isDisposed()) {
									peersTable.refresh();
									setPartName("Peers (" + resourceUsages.size() + ")");
								}
							}
						});
					}
				}
			}
		});

		t.setDaemon(true);
		t.setName("PeerView update");
		t.start();
	}

	@Override
	public void setFocus() {
		peersTable.getTable().setFocus();
	}

	public static Optional<PeerView> getInstance() {
		return Optional.fromNullable(instance);
	}

	private String determinePeerName(IResourceUsage usage) {
		Optional<PeerID> optPeerID = determinePeerID(usage);
		if (!optPeerID.isPresent()) {
			return UNKNOWN_PEER_NAME;
		}
		Optional<String> optPeerName = p2pDictionary.getRemotePeerName(optPeerID.get());
		return optPeerName.isPresent() ? optPeerName.get() : UNKNOWN_PEER_NAME;
	}

	private Optional<PeerID> determinePeerID(IResourceUsage usage) {
		return Optional.fromNullable(peerIDMap.get(usage));
	}

	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement advertisement) {
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement advertisement) {
	}

	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
	}

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		refreshTableAsync();
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		refreshTableAsync();
	}
}
