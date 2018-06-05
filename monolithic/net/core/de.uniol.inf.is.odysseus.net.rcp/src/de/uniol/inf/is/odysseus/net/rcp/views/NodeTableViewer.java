package de.uniol.inf.is.odysseus.net.rcp.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.rcp.util.CellLabelProviderAndSorter;
import de.uniol.inf.is.odysseus.rcp.util.ColumnViewerSorter;

public final class NodeTableViewer {

	private static final String UNKNOWN_TEXT = "<unknown>";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private TableViewer tableViewer;
	private boolean refreshing = false;
	private WarnImages warnImages;

	public NodeTableViewer(Composite parent, NodeViewUsageContainer container) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		/************ Number ***************/
		TableViewerColumn numberColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		numberColumn.getColumn().setText("#");
		numberColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<Integer>(tableViewer, numberColumn) {
			@Override
			protected Integer getValue(IOdysseusNode node) {
				return container.indexOf(node) + 1;
			}
		});
		tableColumnLayout.setColumnData(numberColumn.getColumn(), new ColumnWeightData(3, 15, true));
		
		/************ Activity *************/
		TableViewerColumn activityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		activityColumn.getColumn().setText("Act");
		tableColumnLayout.setColumnData(activityColumn.getColumn(), new ColumnWeightData(3, 15, true));
		activityColumn.setLabelProvider(new CellLabelProviderAndSorter<IOdysseusNode, Boolean>(tableViewer, activityColumn) {
			@Override
			protected Boolean getValue(IOdysseusNode node) {
				return container.isNodeActive(node);
			}

			@Override
			protected String toString(Boolean value) {
				return ""; // image only
			}

			@Override
			protected Image getImage(Boolean value) {
				return warnImages.getWarnImage(value == true ? 0 : 100);
			}

			@Override
			protected int compareValues(Boolean a, Boolean b) {
				return Boolean.compare(a, b);
			}
		});

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, nameColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return node.getName();
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 15, true));

		/************* Address ****************/
		TableViewerColumn addressColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		addressColumn.getColumn().setText("Address");
		addressColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, addressColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return determineAddressString(node);
			}

		});
		tableColumnLayout.setColumnData(addressColumn.getColumn(), new ColumnWeightData(10, 25, true));

		/************* Hostname ****************/
		TableViewerColumn hostnameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		hostnameColumn.getColumn().setText("Hostname");
		hostnameColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, hostnameColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return determineHostname(node);
			}

		});
		tableColumnLayout.setColumnData(hostnameColumn.getColumn(), new ColumnWeightData(10, 25, true));

		/************* Version ****************/
		TableViewerColumn versionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		versionColumn.getColumn().setText("Version");
		versionColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, versionColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return determineVersionString(container, node);
			}
		});
		tableColumnLayout.setColumnData(versionColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* Starttime ****************/
		TableViewerColumn startTimeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		startTimeColumn.getColumn().setText("Starttime");
		startTimeColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, startTimeColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return determineStarttimeString(container, node);
			}
		});
		tableColumnLayout.setColumnData(startTimeColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* MEM ****************/
		TableViewerColumn memColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		memColumn.getColumn().setText("MEM (%)");
		memColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<Double>(tableViewer, memColumn) {
			@Override
			protected Double getValue(IOdysseusNode node) {
				Optional<IResourceUsage> optUsage = container.determineResourceUsage(node);
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
		cpuColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<Double>(tableViewer, cpuColumn) {
			@Override
			protected Double getValue(IOdysseusNode node) {
				Optional<IResourceUsage> optUsage = container.determineResourceUsage(node);
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
				IOdysseusNode node = (IOdysseusNode) cell.getElement();
				Optional<IResourceUsage> optUsage = container.determineResourceUsage(node);
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(usage.getRunningQueriesCount() + " / " + (usage.getRunningQueriesCount() + usage.getStoppedQueriesCount()));
				} else {
					cell.setText(UNKNOWN_TEXT);
				}
			}
		});
		tableColumnLayout.setColumnData(queriesColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, queriesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Optional<IResourceUsage> optUsage1 = container.determineResourceUsage((IOdysseusNode) e1);
				Optional<IResourceUsage> optUsage2 = container.determineResourceUsage((IOdysseusNode) e2);

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
				IOdysseusNode node = (IOdysseusNode) cell.getElement();
				Optional<IResourceUsage> optUsage = container.determineResourceUsage(node);
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();
					cell.setText(String.format("%-3.1f", (usage.getNetInputRate() + usage.getNetOutputRate())) + " / " + usage.getNetBandwidthMax());
				} else {
					cell.setText(UNKNOWN_TEXT);
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
		pingColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<Double>(tableViewer, pingColumn) {
			@Override
			protected Double getValue(IOdysseusNode node) {
				if (!container.isNodeActive(node)) {
					return -1.0;
				}

				Optional<Double> optPing = OdysseusNetRCPPlugIn.getPingMap().getPing(node);
				if (optPing.isPresent()) {
					return optPing.get();
				}
				return -1.0;
			}

			@Override
			protected Image getImage(Double value) {
				if (value < 0) {
					return null;
				}

				return warnImages.getWarnImage(value / 10.0);
			}

			@Override
			protected int compareValues(Double a, Double b) {
				return Double.compare(a, b);
			}

			@Override
			protected String toString(Double value) {
				if (value < 0) {
					return "";
				}

				return String.format("%-4.0f", value);
			}
		});
		tableColumnLayout.setColumnData(pingColumn.getColumn(), new ColumnWeightData(3, 25, true));

		/************* LastContact ****************/
		TableViewerColumn lastContactColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		lastContactColumn.getColumn().setText("Last seen");
		lastContactColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, lastContactColumn) {
			@Override
			protected String getValue(IOdysseusNode node) {
				return determineLastContactString(container, node);
			}
		});
		tableColumnLayout.setColumnData(lastContactColumn.getColumn(), new ColumnWeightData(5, 25, true));

		/************* RemoteNodeCount ****************/
		TableViewerColumn remoteNodeCountColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		remoteNodeCountColumn.getColumn().setText("#Nodes");
		remoteNodeCountColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<Integer>(tableViewer, remoteNodeCountColumn) {
			@Override
			protected Integer getValue(IOdysseusNode node) {
				Optional<IResourceUsage> optResourceUsage = container.determineResourceUsage(node);
				if (optResourceUsage.isPresent()) {
					return optResourceUsage.get().getRemoteNodeCount();
				}

				return null;
			}

			@Override
			protected int compareValues(Integer a, Integer b) {
				return Integer.compare(a, b);
			}
		});
		tableColumnLayout.setColumnData(remoteNodeCountColumn.getColumn(), new ColumnWeightData(3, 25, true));

//		/************* OdysseusNodeID ****************/
//		TableViewerColumn nodeIDColumn = new TableViewerColumn(tableViewer, SWT.NONE);
//		nodeIDColumn.getColumn().setText("ID");
//		nodeIDColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, nodeIDColumn) {
//
//			@Override
//			protected String getValue(IOdysseusNode node) {
//				return node.getID().toString();
//			}
//		});
//		tableColumnLayout.setColumnData(nodeIDColumn.getColumn(), new ColumnWeightData(10, 25, true));
//
		hideSelectionIfNeeded(tableViewer);

		tableViewer.setInput(container.getFoundNodesList());
		warnImages = new WarnImages();

		tableViewer.refresh();
	}

	public void dispose() {
		warnImages.dispose();
	}

	private String determineVersionString(NodeViewUsageContainer usageContainer, IOdysseusNode node) {
		Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(node);
		if (optUsage.isPresent()) {
			IResourceUsage usage = optUsage.get();
			return toVersionString(usage.getVersion());
		}
		return UNKNOWN_TEXT;
	}

	private static String toVersionString(int[] version) {
		return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
	}

	private String determineStarttimeString(NodeViewUsageContainer container, IOdysseusNode node) {
		Optional<IResourceUsage> optUsage = container.determineResourceUsage(node);
		if (optUsage.isPresent()) {
			IResourceUsage usage = optUsage.get();
			return convertTimestampToDate(usage.getStartupTimestamp());
		}

		return UNKNOWN_TEXT;
	}

	private static String convertTimestampToDate(long startupTimestamp) {
		return DATE_FORMAT.format(new Date(startupTimestamp));
	}

	private static String determinePropertyValue(IOdysseusNode node, String propertyKey) {
		Optional<String> optPropertyValue = node.getProperty(propertyKey);
		return optPropertyValue.isPresent() ? optPropertyValue.get() : "";
	}

	private static String determineLastContactString(NodeViewUsageContainer usageContainer, IOdysseusNode node) {
		Optional<IResourceUsage> optUsage = usageContainer.determineResourceUsage(node);
		if (optUsage.isPresent()) {
			return TIME_FORMAT.format(new Date(optUsage.get().getTimestamp()));
		}

		return UNKNOWN_TEXT;
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

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void refreshTableAsync() {
		if (refreshing) {
			return;
		}

		if (!tableViewer.getTable().isDisposed()) {

			refreshing = true;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!tableViewer.getTable().isDisposed()) {
						tableViewer.refresh();
					}
					refreshing = false;
				}
			});
		}
	}

	public Collection<IOdysseusNode> getSelectedNodes() {
		IStructuredSelection structSelection = (IStructuredSelection) tableViewer.getSelection();
		Object[] selectedObjects = structSelection.toArray();

		Collection<IOdysseusNode> selectedNodes = Lists.newArrayList();
		for (Object selectedObject : selectedObjects) {
			selectedNodes.add((IOdysseusNode) selectedObject);
		}
		return selectedNodes;
	}

	private static double calcMemPercentage(IResourceUsage resourceUsage) {
		return 100.0 - (((double) resourceUsage.getMemFreeBytes() / (double) resourceUsage.getMemMaxBytes()) * 100.0);
	}

	private static double calcCpuPercentage(IResourceUsage resourceUsage) {
		return 100.0 - ((resourceUsage.getCpuFree() / resourceUsage.getCpuMax()) * 100.0);
	}

	private static String determineHostname(IOdysseusNode node) {
		return determinePropertyValue(node, "serverHostname");
	}

	private static String determineAddressString(IOdysseusNode node) {
		return determinePropertyValue(node, "serverAddress") + ":" + determinePropertyValue(node, "serverPort");
	}

}
