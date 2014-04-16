package de.uniol.inf.is.odysseus.peer.rcp.views;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.peer.PeerID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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

	private static final long REFRESH_INTERVAL_MILLIS = 5000;

	private static PeerView instance;
	private static Image[] warnImages;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final List<PeerID> foundPeerIDs = Lists.newArrayList();

	private IP2PDictionary p2pDictionary;

	private TableViewer peersTable;
	private RepeatingJobThread refresher;
	private Collection<PeerID> refreshing = Lists.newLinkedList();

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

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(peersTable, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				cell.setText(determinePeerName(pid));
				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 25, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(peersTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determinePeerName((PeerID) e1);
				String n2 = determinePeerName((PeerID) e2);
				return n1.compareTo(n2);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(peersTable, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				if (isLocalID((PeerID) cell.getElement())) {
					try {
						cell.setText(InetAddress.getLocalHost().getHostAddress() + ":" + RCPP2PNewPlugIn.getP2PNetworkManager().getPort());
					} catch (UnknownHostException e) {
						cell.setText("<unknown>");
					}
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				} else {
					Optional<String> optAddress = RCPP2PNewPlugIn.getP2PDictionary().getRemotePeerAddress((PeerID) cell.getElement());
					if (optAddress.isPresent()) {
						cell.setText(optAddress.get());
					} else {
						cell.setText("<unknown>");
					}
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

		/************* Version ****************/
		TableViewerColumn versionColumn = new TableViewerColumn(peersTable, SWT.NONE);
		versionColumn.getColumn().setText("Version");
		versionColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				Optional<IResourceUsage> optUsage = determineResourceUsage(pid);
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(toVersionString(usage.getVersion()));
				} else {
					cell.setText("<unknown>");
				}

				if (isLocalID(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}

			private String toVersionString(int[] version) {
				return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
			}
		});
		tableColumnLayout.setColumnData(versionColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, versionColumn) {
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
				Optional<IResourceUsage> optUsage = determineResourceUsage((PeerID) cell.getElement());
				if (optUsage.isPresent()) {
					double memPerc = calcMemPercentage(optUsage.get());
					cell.setText(String.format("%-3.1f", memPerc));

					Image img = getWarnImage(memPerc);
					cell.setImage(img);

				} else {
					cell.setText("<unknown>");
				}
				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(memColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, memColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optUsage1 = determineResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optUsage2 = determineResourceUsage((PeerID) e2);

				if (!optUsage1.isPresent() && !optUsage2.isPresent()) {
					return 0;
				} else if (!optUsage1.isPresent()) {
					return 1;
				} else if (!optUsage2.isPresent()) {
					return -1;
				}

				IResourceUsage usage1 = optUsage1.get();
				IResourceUsage usage2 = optUsage2.get();
				return Double.compare(calcMemPercentage(usage1), calcMemPercentage(usage2));
			}
		};

		/************* CPU ****************/
		TableViewerColumn cpuColumn = new TableViewerColumn(peersTable, SWT.NONE);
		cpuColumn.getColumn().setText("CPU (%)");
		cpuColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Optional<IResourceUsage> optUsage = determineResourceUsage((PeerID) cell.getElement());
				if (optUsage.isPresent()) {
					double cpuPerc = calcCpuPercentage(optUsage.get());
					cell.setText(String.format("%-3.1f", cpuPerc));

					Image img = getWarnImage(cpuPerc);
					cell.setImage(img);

				} else {
					cell.setText("<unknown>");
					cell.setImage(null);
				}

				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(cpuColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, cpuColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optUsage1 = determineResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optUsage2 = determineResourceUsage((PeerID) e2);

				if (!optUsage1.isPresent() && !optUsage2.isPresent()) {
					return 0;
				} else if (!optUsage1.isPresent()) {
					return 1;
				} else if (!optUsage2.isPresent()) {
					return -1;
				}

				IResourceUsage usage1 = optUsage1.get();
				IResourceUsage usage2 = optUsage2.get();
				return Double.compare(calcCpuPercentage(usage1), calcCpuPercentage(usage2));
			}
		};

		/************* Querycounts ****************/
		TableViewerColumn queriesColumn = new TableViewerColumn(peersTable, SWT.NONE);
		queriesColumn.getColumn().setText("Queries");
		queriesColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Optional<IResourceUsage> optUsage = determineResourceUsage((PeerID) cell.getElement());
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(usage.getRunningQueriesCount() + " / " + (usage.getRunningQueriesCount() + usage.getStoppedQueriesCount()));
				} else {
					cell.setText("<unknown>");
				}

				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(queriesColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, queriesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optUsage1 = determineResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optUsage2 = determineResourceUsage((PeerID) e2);

				if (!optUsage1.isPresent() && !optUsage2.isPresent()) {
					return 0;
				} else if (!optUsage1.isPresent()) {
					return 1;
				} else if (!optUsage2.isPresent()) {
					return -1;
				}

				IResourceUsage usage1 = optUsage1.get();
				IResourceUsage usage2 = optUsage2.get();
				return Double.compare(usage1.getRunningQueriesCount(), usage2.getRunningQueriesCount());
			}
		};

		/************* Bandwidth ****************/
		TableViewerColumn netColumn = new TableViewerColumn(peersTable, SWT.NONE);
		netColumn.getColumn().setText("Network");
		netColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Optional<IResourceUsage> optUsage = determineResourceUsage((PeerID) cell.getElement());
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(String.format("%-3.1f", (usage.getNetInputRate() + usage.getNetOutputRate())) + " / " + usage.getNetBandwidthMax());
				} else {
					cell.setText("<unknown>");
				}

				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
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
				Optional<Double> optPing = RCPP2PNewPlugIn.getPingMap().getPing((PeerID) cell.getElement());
				if (optPing.isPresent()) {
					long ping = (long) (double) optPing.get();
					cell.setText(String.valueOf(ping));

					Image img = getWarnImage((ping / 10.0));
					cell.setImage(img);
				} else {
					cell.setText("<unknown>");
				}

				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(pingColumn.getColumn(), new ColumnWeightData(3, 25, true));
		new ColumnViewerSorter(peersTable, pingColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<Double> ping1 = RCPP2PNewPlugIn.getPingMap().getPing((PeerID) e1);
				Optional<Double> ping2 = RCPP2PNewPlugIn.getPingMap().getPing((PeerID) e2);

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
				cell.setText(((PeerID) cell.getElement()).toString());
				if (isLocalID((PeerID) cell.getElement())) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(peerIDColumn.getColumn(), new ColumnWeightData(10, 25, true));
		sorter = new ColumnViewerSorter(peersTable, peerIDColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		hideSelectionIfNeeded(peersTable);

		peersTable.setInput(foundPeerIDs);

		refresher = new RepeatingJobThread(REFRESH_INTERVAL_MILLIS, "Refresher of PeerView") {
			@Override
			public void doJob() {
				refresh();
			}
		};
		refresher.start();

		warnImages = createWarnImages();

		instance = this;
	}

	private static boolean isLocalID(PeerID pid) {
		return RCPP2PNewPlugIn.getP2PNetworkManager().getLocalPeerID().equals(pid);
	}

	private void hideSelectionIfNeeded(final TableViewer tableViewer) {
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (tableViewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					tableViewer.setSelection(new StructuredSelection());
				}
			}
		});
	}

	private static Image[] createWarnImages() {
		Image[] images = new Image[4];

		images[0] = createWarnImage(SWT.COLOR_BLUE);
		images[1] = createWarnImage(SWT.COLOR_GREEN);
		images[2] = createWarnImage(SWT.COLOR_YELLOW);
		images[3] = createWarnImage(SWT.COLOR_RED);

		return images;
	}

	private static Image createWarnImage(int color) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		Image img = new Image(display, 10, 10);

		GC gc = new GC(img);
		gc.setBackground(display.getSystemColor(color));
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		return img;
	}

	private static Image getWarnImage(double perc) {
		int imageIndex = Math.max(0, Math.min(3, (int) (perc / 25.0)));
		if (warnImages == null) {
			warnImages = createWarnImages();
		}

		return warnImages[imageIndex];
	}

	private static double calcMemPercentage(IResourceUsage resourceUsage) {
		return 100.0 - (((double) resourceUsage.getMemFreeBytes() / (double) resourceUsage.getMemMaxBytes()) * 100.0);
	}

	private static double calcCpuPercentage(IResourceUsage resourceUsage) {
		return 100.0 - ((resourceUsage.getCpuFree() / resourceUsage.getCpuMax()) * 100.0);
	}

	@Override
	public void dispose() {
		disposeWarnImages();

		instance = null;
		p2pDictionary.removeListener(this);

		if (refresher != null) {
			refresher.stopRunning();
			refresher = null;
		}

		super.dispose();
	}

	private static void disposeWarnImages() {
		for (Image image : warnImages) {
			image.dispose();
		}
		warnImages = null;
	}

	public Collection<PeerID> getSelectedPeerIDs() {
		IStructuredSelection selection = (IStructuredSelection) peersTable.getSelection();
		Collection<PeerID> peers = Lists.newArrayList();

		for (Object selectedObject : selection.toArray()) {
			peers.add((PeerID) selectedObject);
		}

		return peers;
	}

	public void refresh() {
		Collection<PeerID> foundPeerIDsCopy = null;
		synchronized (foundPeerIDs) {
			foundPeerIDs.clear();
			foundPeerIDs.addAll(p2pDictionary.getRemotePeerIDs());
			foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
		}
		refreshTableAsync();

		final IPeerResourceUsageManager usageManager = RCPP2PNewPlugIn.getPeerResourceUsageManager();
		for (final PeerID remotePeerID : foundPeerIDsCopy) {

			synchronized (refreshing) {
				if (refreshing.contains(remotePeerID)) {
					continue;
				}

				refreshing.add(remotePeerID);
			}

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
					synchronized (peersTable) {
						if (!peersTable.getTable().isDisposed()) {
							peersTable.refresh();

							synchronized (foundPeerIDs) {
								setPartName("Peers (" + foundPeerIDs.size() + ")");
							}
						}
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

	private String determinePeerName(PeerID id) {
		if (isLocalID(id)) {
			return "<local>";
		}
		return p2pDictionary.getRemotePeerName(id);
	}

	private Optional<IResourceUsage> determineResourceUsage(PeerID id) {
		synchronized (usageMap) {
			return Optional.fromNullable(usageMap.get(id));
		}
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
}
