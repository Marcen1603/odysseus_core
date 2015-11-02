package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManagerListener;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;

public final class NodeTableViewer implements IOdysseusNodeManagerListener {

	private static final String UNKNOWN_TEXT = "<unknown>";

	private final List<OdysseusNodeID> nodeIDs = Lists.newArrayList();

	private TableViewer tableViewer;
	private boolean refreshing = false;

	public NodeTableViewer(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, nameColumn) {
			@Override
			protected String getValue(OdysseusNodeID nodeID) {
				return determinePeerName(nodeID);
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(10, 15, true));

		/************* NodeID ****************/
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		idColumn.getColumn().setText("ID");
		idColumn.setLabelProvider(new NodeViewCellLabelProviderAndSorter<String>(tableViewer, idColumn) {
			@Override
			protected String getValue(OdysseusNodeID nodeID) {
				return nodeID.toString();
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(20, 15, true));

		
		hideSelectionIfNeeded(tableViewer);

		tableViewer.setInput(nodeIDs);

		OdysseusNetRCPPlugIn.getOdysseusNodeManager().addListener(this);
		for (IOdysseusNode node : OdysseusNetRCPPlugIn.getOdysseusNodeManager().getNodes()) {
			nodeAdded(OdysseusNetRCPPlugIn.getOdysseusNodeManager(), node);
		}
		tableViewer.refresh();
	}

	public void dispose() {
		OdysseusNetRCPPlugIn.getOdysseusNodeManager().removeListener(this);
	}

	private static String determinePeerName(OdysseusNodeID nodeID) {
		Optional<IOdysseusNode> optNode = OdysseusNetRCPPlugIn.getOdysseusNodeManager().getNode(nodeID);
		if (optNode.isPresent()) {
			return optNode.get().getName();
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

	@Override
	public void nodeAdded(IOdysseusNodeManager sender, IOdysseusNode node) {
		if( !nodeIDs.contains(node.getID())) {
			nodeIDs.add(node.getID());
			refreshTableAsync();
		}
	}

	@Override
	public void nodeRemoved(IOdysseusNodeManager sender, IOdysseusNode node) {
		if( nodeIDs.contains(node.getID())) {
			nodeIDs.remove(node.getID());
			refreshTableAsync();
		}
	}
}
