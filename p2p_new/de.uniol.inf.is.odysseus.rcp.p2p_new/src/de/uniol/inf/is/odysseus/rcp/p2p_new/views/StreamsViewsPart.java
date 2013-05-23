package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class StreamsViewsPart extends ViewPart implements IP2PDictionaryListener {

	private static class TableEntry {
		public String name;
		public ID viewStreamID;
		public String type;
		public String peerNames;
		public String schema;
	}
	
	private static StreamsViewsPart instance;
	
	private IP2PDictionary p2pDictionary;
	private TableViewer viewsStreamsTable;
	private List<TableEntry> input = Lists.newArrayList();
	
	public StreamsViewsPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = P2PDictionaryService.get();
		p2pDictionary.addListener(this);
		
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		viewsStreamsTable = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		viewsStreamsTable.getTable().setHeaderVisible(true);
		viewsStreamsTable.getTable().setLinesVisible(true);
		viewsStreamsTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* ID ****************/
		TableViewerColumn idColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		idColumn.getColumn().setText("ID");
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).viewStreamID.toString());
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5, 25, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(viewsStreamsTable, idColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.viewStreamID.toString().compareTo(te2.viewStreamID.toString());
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		nameColumn.getColumn().setText("Name(s)");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).name);
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		sorter = new ColumnViewerSorter(viewsStreamsTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.name.compareTo(te2.name);
			}
		};

		/************* Type ****************/
		TableViewerColumn typeColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		typeColumn.getColumn().setText("Type");
		typeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).type);
			}
		});
		tableColumnLayout.setColumnData(typeColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(viewsStreamsTable, typeColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.type.compareTo(te2.type);
			}
		};

		/************* PeerIDs ****************/
		TableViewerColumn peerColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		peerColumn.getColumn().setText("Peer(s)");
		peerColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).peerNames);
			}
		});
		tableColumnLayout.setColumnData(peerColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(viewsStreamsTable, peerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		/************* Schema ****************/
		TableViewerColumn schemaColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		schemaColumn.getColumn().setText("Schema");
		schemaColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).schema);
			}
		});
		tableColumnLayout.setColumnData(schemaColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(viewsStreamsTable, schemaColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};
		
		getSite().setSelectionProvider(viewsStreamsTable);
		
		viewsStreamsTable.setInput(input);
		refreshTable();
		
		instance = this;
	}

	@Override
	public void dispose() {
		instance = null;
		
		p2pDictionary.removeListener(this);
		p2pDictionary = null;
		
		super.dispose();
	}

	@Override
	public void setFocus() {
		if( viewsStreamsTable != null ) {
			viewsStreamsTable.getTable().setFocus();
		}
	}
	
	public Optional<ID> getSelectedStreamOrViewID() {
		IStructuredSelection selection = (IStructuredSelection) viewsStreamsTable.getSelection();
		if( !selection.isEmpty() ) {
			TableEntry selectedEntry = (TableEntry) selection.getFirstElement();
			return Optional.of(selectedEntry.viewStreamID);
		}
		return Optional.absent();
	}

	@Override
	public void publishedViewAdded(IP2PDictionary sender, ViewAdvertisement viewName) {
		refreshTable();
	}

	@Override
	public void publishedViewRemoved(IP2PDictionary sender, ViewAdvertisement viewName) {
		refreshTable();
	}

	@Override
	public void peerAdded(IP2PDictionary sender, PeerID id) {
		// do nothing
	}

	@Override
	public void peerRemoved(IP2PDictionary sender, PeerID id) {
		// do nothing
	}
	
	public static Optional<StreamsViewsPart> getInstance() {
		return Optional.fromNullable(instance);
	}
	
	private void refreshTable() {
		input.clear();
		input.addAll( determineTableEntries(p2pDictionary));
		
		if( !PlatformUI.getWorkbench().getDisplay().isDisposed() && !viewsStreamsTable.getTable().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if( !viewsStreamsTable.getTable().isDisposed() ) {
						viewsStreamsTable.refresh();
					}
				}
			});
		}
	}
	
	private static List<TableEntry> determineTableEntries(IP2PDictionary p2pDictionary) {
		
		List<TableEntry> result = Lists.newArrayList();
		
		List<ID> publishedViewIDs = p2pDictionary.getViewIDs();
		for( ID publishedViewID : publishedViewIDs ) {
			TableEntry entry = new TableEntry();
			ImmutableList<ViewAdvertisement> viewAdvs = p2pDictionary.getViews(publishedViewID);
			
			entry.name = getViewNames(viewAdvs);
			entry.schema = "TODO";
			entry.type = "View";
			entry.peerNames = "<unknown>";//getPeerNames(viewAdvs, p2pDictionary);
			entry.viewStreamID = publishedViewID;
			
			result.add(entry);
		}
		
		return result;
	}

	private static String getViewNames(List<ViewAdvertisement> viewAdvs) {
		List<String> viewNames = Lists.newArrayList();
		for( ViewAdvertisement viewAdv : viewAdvs ) {
			if( !viewNames.contains(viewAdv.getViewName())) {
				viewNames.add(viewAdv.getViewName());
			}
		}
		
		StringBuilder sb = new StringBuilder();
		
		for( int i = 0; i < viewNames.size(); i++ ) {
			sb.append(viewNames.get(i));
			if( i < viewNames.size() - 1 ) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

//	private static String getPeerNames(List<ViewAdvertisement> viewAdvs, IP2PDictionary dictionary) {	
//		StringBuilder sb = new StringBuilder();
//		
//		for( int i = 0; i < viewAdvs.size(); i++ ) {
//			ViewAdvertisement viewAdv = viewAdvs.get(i);
//			Optional<String> optPeerName = dictionary.getPeerName(viewAdv.getPeerID());
//			
//			sb.append( optPeerName.isPresent() ? optPeerName.get() : "<unknwon>");
//			if( i < viewAdvs.size() - 1 ) {
//				sb.append(", ");
//			}
//		}
//		return sb.toString();
//	}

}
