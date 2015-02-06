package de.uniol.inf.is.odysseus.peer.rcp.views.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.jxta.peer.PeerID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.rcp.views.ColumnViewerSorter;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class PeerTableViewer {

	private static final String UNKNOWN_TEXT = "";
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

	private TableViewer tableViewer;
	private WarnImages warnImages;

	public PeerTableViewer(Composite parent, final PeerViewUsageContainer usageContainer) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		/************ Activity *************/
		TableViewerColumn activityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		activityColumn.getColumn().setText("Act");
		tableColumnLayout.setColumnData(activityColumn.getColumn(), new ColumnWeightData(3, 15, true));
		activityColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<Boolean>(tableViewer, activityColumn) {
			@Override
			protected Boolean getValue(PeerID pid) {
				return usageContainer.isPeerActive(pid);
			}
			
			@Override
			protected String toString(Boolean value) {
				return ""; // image only
			}
			
			@Override
			protected Image getImage(Boolean value) {
				return warnImages.getWarnImage(value == true ? 0 : 100); // 0 = blue, 100 = red
			}
			
			@Override
			protected int compareValues(Boolean a, Boolean b) {
				return Boolean.compare(a, b);
			}
		});
		
		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, nameColumn) {
			@Override
			protected String getValue(PeerID pid) {
				return determinePeerName(usageContainer, pid);
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 15, true));

		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, addressColumn) {
			@Override
			protected String getValue(PeerID pid) {
				return determineAddressString(pid);
			}
		});
		tableColumnLayout.setColumnData(addressColumn.getColumn(), new ColumnWeightData(10, 25, true));

		/************* Version ****************/
		TableViewerColumn versionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		versionColumn.getColumn().setText("Version");
		versionColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, versionColumn) {
			@Override
			protected String getValue(PeerID pid) {
				return determineVersionString(usageContainer, pid);
			}
		});
		tableColumnLayout.setColumnData(versionColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* Starttime ****************/
		TableViewerColumn startTimeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		startTimeColumn.getColumn().setText("Starttime");
		startTimeColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, startTimeColumn) {
			@Override
			protected String getValue(PeerID pid) {
				return determineStarttimeString(usageContainer, pid);
			}
		});
		tableColumnLayout.setColumnData(startTimeColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* MEM ****************/
		TableViewerColumn memColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		memColumn.getColumn().setText("MEM (%)");
		memColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<Double>(tableViewer, memColumn) {
			@Override
			protected Double getValue(PeerID pid) {
				Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
				if (optUsage.isPresent()) {
					return calcMemPercentage(optUsage.get());
				}
				return null;
			}

			@Override
			protected Image getImage(Double value) {
				return warnImages.getWarnImage(value);
			}

			@Override
			protected int compareValues(Double a, Double b) {
				return Double.compare(a, b);
			}

			@Override
			protected String toString(Double value) {
				return String.format("%-3.1f", value);
			}
		});
		tableColumnLayout.setColumnData(memColumn.getColumn(), new ColumnWeightData(3, 25, true));

		/************* CPU ****************/
		TableViewerColumn cpuColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		cpuColumn.getColumn().setText("CPU (%)");
		cpuColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<Double>(tableViewer, cpuColumn) {
			@Override
			protected Double getValue(PeerID pid) {
				Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
				if (optUsage.isPresent()) {
					return calcCpuPercentage(optUsage.get());
				}
				return null;
			}
			
			@Override
			protected Image getImage(Double value) {
				return warnImages.getWarnImage(value);
			}

			@Override
			protected int compareValues(Double a, Double b) {
				return Double.compare(a, b);
			}

			@Override
			protected String toString(Double value) {
				return String.format("%-3.1f", value);
			}
		});
		tableColumnLayout.setColumnData(cpuColumn.getColumn(), new ColumnWeightData(3, 25, true));

		/************* Querycounts ****************/
		TableViewerColumn queriesColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		queriesColumn.getColumn().setText("Queries");
		queriesColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(usage.getRunningQueriesCount() + " / " + (usage.getRunningQueriesCount() + usage.getStoppedQueriesCount()));
				} else {
					cell.setText(UNKNOWN_TEXT);
				}

				if (isLocalID(pid) || !usageContainer.isPeerActive(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(queriesColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, queriesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optUsage1 = usageContainer.determineResourceUsage((PeerID) e1);
				Optional<IResourceUsage> optUsage2 = usageContainer.determineResourceUsage((PeerID) e2);

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
		TableViewerColumn netColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		netColumn.getColumn().setText("Network");
		netColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				PeerID pid = (PeerID) cell.getElement();
				Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(String.format("%-3.1f", (usage.getNetInputRate() + usage.getNetOutputRate())) + " / " + usage.getNetBandwidthMax());
				} else {
					cell.setText(UNKNOWN_TEXT);
				}

				if (isLocalID(pid)  || !usageContainer.isPeerActive(pid)) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
		});
		tableColumnLayout.setColumnData(netColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, netColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		/************* Ping ****************/
		TableViewerColumn pingColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		pingColumn.getColumn().setText("Ping (ms)");
		pingColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<Double>(tableViewer, pingColumn) {
			@Override
			protected Double getValue(PeerID pid) {
				Optional<Double> optUsage = RCPP2PNewPlugIn.getPingMap().getPing(pid);
				if (optUsage.isPresent()) {
					return optUsage.get();
				}
				return null;
			}

			@Override
			protected Image getImage(Double value) {
				return warnImages.getWarnImage(value / 10.0);
			}

			@Override
			protected int compareValues(Double a, Double b) {
				return Double.compare(a, b);
			}

			@Override
			protected String toString(Double value) {
				return String.format("%-4.0f", value);
			}
		});
		tableColumnLayout.setColumnData(pingColumn.getColumn(), new ColumnWeightData(3, 25, true));

		/************* LastContact ****************/
		TableViewerColumn lastContactColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		lastContactColumn.getColumn().setText("Last seen");
		lastContactColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, lastContactColumn) {
			@Override
			protected String getValue(PeerID pid) {
				return determineLastContactString(usageContainer, pid);
			}
		});
		tableColumnLayout.setColumnData(lastContactColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* RemotePeerCount ****************/
		TableViewerColumn remotePeerCountColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		remotePeerCountColumn.getColumn().setText("#Peers");
		remotePeerCountColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<Integer>(tableViewer, remotePeerCountColumn) {
			@Override
			protected Integer getValue(PeerID pid) {
				Optional<IResourceUsage> optResourceUsage = usageContainer.determineResourceUsage(pid);
				if( optResourceUsage.isPresent() ) {
					return optResourceUsage.get().getRemotePeerCount();
				}
				
				return null;
			}
			
			@Override
			protected int compareValues(Integer a, Integer b) {
				return Integer.compare(a, b);
			}
		});
		tableColumnLayout.setColumnData(remotePeerCountColumn.getColumn(), new ColumnWeightData(3, 25, true));

		/************* PeerID ****************/
		TableViewerColumn peerIDColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		peerIDColumn.getColumn().setText("PeerID");
		peerIDColumn.setLabelProvider(new PeerViewCellLabelProviderAndSorter<String>(tableViewer, peerIDColumn) {

			@Override
			protected String getValue(PeerID pid) {
				return pid.toString();
			}
		});
		tableColumnLayout.setColumnData(peerIDColumn.getColumn(), new ColumnWeightData(10, 25, true));

		hideSelectionIfNeeded(tableViewer);
		tableViewer.setInput(usageContainer.getFoundPeerIDsList());

		warnImages = new WarnImages();
	}

	public void dispose() {
		warnImages.dispose();
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	private static boolean isLocalID(PeerID pid) {
		return RCPP2PNewPlugIn.getP2PNetworkManager().getLocalPeerID().equals(pid);
	}

	private static String determineAddressString(PeerID peerID) {
		if (isLocalID(peerID)) {
			try {
				return InetAddress.getLocalHost().getHostAddress() + ":" + RCPP2PNewPlugIn.getP2PNetworkManager().getPort();
			} catch (UnknownHostException e) {
				return UNKNOWN_TEXT;
			}
		}

		Optional<String> optAddress = RCPP2PNewPlugIn.getPeerDictionary().getRemotePeerAddress(peerID);
		if (optAddress.isPresent()) {
			return optAddress.get();
		}
		return UNKNOWN_TEXT;
	}

	private static String determineLastContactString(PeerViewUsageContainer usageContainer, PeerID pid) {
		Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
		if (optUsage.isPresent()) {
			return TIME_FORMAT.format(new Date(optUsage.get().getTimestamp()));
		}

		return UNKNOWN_TEXT;
	}

	private String determineVersionString(PeerViewUsageContainer usageContainer, PeerID pid) {
		Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
		if (optUsage.isPresent()) {
			IResourceUsage usage = optUsage.get();
			return toVersionString(usage.getVersion());
		}
		return UNKNOWN_TEXT;
	}

	private String determineStarttimeString(PeerViewUsageContainer usageContainer, PeerID pid) {
		Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(pid);
		if (optUsage.isPresent()) {
			IResourceUsage usage = optUsage.get();
			return convertTimestampToDate(usage.getStartupTimestamp());
		}

		return UNKNOWN_TEXT;
	}

	private static String toVersionString(int[] version) {
		return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
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

	private String determinePeerName(PeerViewUsageContainer usageContainer, PeerID pid) {
		Optional<String> optName = usageContainer.getPeerName(pid);
		if( optName.isPresent() ) {
			return optName.get();
		}
		
		return "<unknown>";
	}

	private static double calcMemPercentage(IResourceUsage resourceUsage) {
		return 100.0 - (((double) resourceUsage.getMemFreeBytes() / (double) resourceUsage.getMemMaxBytes()) * 100.0);
	}

	private static double calcCpuPercentage(IResourceUsage resourceUsage) {
		return 100.0 - ((resourceUsage.getCpuFree() / resourceUsage.getCpuMax()) * 100.0);
	}

	private static String convertTimestampToDate(long startupTimestamp) {
		return DATE_FORMAT.format(new Date(startupTimestamp));
	}

}
