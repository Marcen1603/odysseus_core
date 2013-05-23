package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;
import java.util.Map;

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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class StreamsViewsPart extends ViewPart implements IP2PDictionaryListener {

	private static class TableEntry {
		public ViewAdvertisement advertisement;
		
		public int index;
		public String type;
		
		public String viewNames;
		public String peerNames;
		
		public String schema;
		public String portedViewName;
	}

	private static final String IMPORT_TAG = "[import]";
	private static final String EXPORT_TAG = "[export]";
	
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

		/************* Index ****************/
		TableViewerColumn idColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		idColumn.getColumn().setText("Index");
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( String.valueOf(((TableEntry)cell.getElement()).index));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(2, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(viewsStreamsTable, idColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return Integer.compare(te1.index,te2.index);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

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
		
		/************* ViewNames ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		nameColumn.getColumn().setText("Name(s)");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).viewNames);
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		sorter = new ColumnViewerSorter(viewsStreamsTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.viewNames.compareTo(te2.viewNames);
			}
		};


		/************* Peer Names ****************/
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
		
		/************* Imported as ****************/
		TableViewerColumn importedViewColumn = new TableViewerColumn(viewsStreamsTable, SWT.NONE);
		importedViewColumn.getColumn().setText("Ported as");
		importedViewColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).portedViewName);
			}
		});
		tableColumnLayout.setColumnData(importedViewColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(viewsStreamsTable, importedViewColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.portedViewName.compareTo(te2.portedViewName);
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
	
	public Optional<ViewAdvertisement> getSelectedStreamOrViewID() {
		IStructuredSelection selection = (IStructuredSelection) viewsStreamsTable.getSelection();
		if( !selection.isEmpty() ) {
			TableEntry selectedEntry = (TableEntry) selection.getFirstElement();
			return Optional.of(selectedEntry.advertisement);
		}
		return Optional.absent();
	}

	// called by p2pDictionary
	@Override
	public void viewAdded(IP2PDictionary sender, ViewAdvertisement viewName) {
		refreshTable();
	}

	// called by p2pDictionary
	@Override
	public void viewRemoved(IP2PDictionary sender, ViewAdvertisement viewName) {
		refreshTable();
	}
	
	// called by p2pDictionary
	@Override
	public void viewImported(IP2PDictionary sender, ViewAdvertisement advertisement, String viewName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedViewName = IMPORT_TAG + " " + viewName ;
			updateTable();
		}
	}
	
	// called by p2pDictionary
	@Override
	public void viewImportRemoved(IP2PDictionary sender, ViewAdvertisement advertisement, String viewName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedViewName = "";
			updateTable();
		}
	}

	// called by p2pDictionary
	@Override
	public void peerAdded(IP2PDictionary sender, PeerID id) {
		// do nothing
	}

	// called by p2pDictionary
	@Override
	public void peerRemoved(IP2PDictionary sender, PeerID id) {
		// do nothing
	}
	
	// called by p2pDictionary
	@Override
	public void viewExported(IP2PDictionary sender, ViewAdvertisement advertisement, String viewName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedViewName = EXPORT_TAG + " " + viewName ;
			updateTable();
		}
		
	}

	// called by p2pDictionary
	@Override
	public void viewExportRemoved(IP2PDictionary sender, ViewAdvertisement advertisement, String viewName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedViewName = "";
			updateTable();
		}		
	}
	
	public static Optional<StreamsViewsPart> getInstance() {
		return Optional.fromNullable(instance);
	}
	
	private void refreshTable() {
		input.clear();
		input.addAll( determineTableEntries(p2pDictionary));
		
		updateTable();
	}

	private void updateTable() {
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
	
	private Optional<TableEntry> findEntry( ViewAdvertisement advertisement ) {
		for( TableEntry entry : input ) {
			if( entry.advertisement.equals(advertisement)) {
				return Optional.of(entry);
			}
		}
		return Optional.absent();
	}
	
	private static List<TableEntry> determineTableEntries(IP2PDictionary p2pDictionary) {
		
		List<TableEntry> result = Lists.newArrayList();
		
		List<ViewAdvertisement> publishedViews = p2pDictionary.getViews();
		Map<ViewAdvertisement, TableEntry> consumedAdvs = Maps.newHashMap();
		
		for( ViewAdvertisement publishedView : publishedViews ) {
			
			List<ViewAdvertisement> sameViews = p2pDictionary.getSame(publishedView);
			
			TableEntry entry = null;
			for( ViewAdvertisement sameView : sameViews ) {
				if( consumedAdvs.containsKey(sameView)) {
					entry = consumedAdvs.get(sameView);
					break;
				}
			}
			
			if( entry == null ) {
				entry = new TableEntry();
				
				entry.index = result.size() + 1;
				entry.schema = "TODO";
				entry.type = publishedView.isStream() ? "Stream" : "View" ;
				entry.peerNames = "<unknown>";//getPeerNames(viewAdvs, p2pDictionary);
				entry.advertisement = publishedView;
				
				if( p2pDictionary.isExported(publishedView.getName())) {
					entry.portedViewName = EXPORT_TAG + " " + publishedView.getName();
				} else if( p2pDictionary.isImported(publishedView)) {
					entry.portedViewName = IMPORT_TAG + " " + p2pDictionary.getImportedViewName(publishedView).get();
				} else {
					entry.portedViewName = "";
				}
				entry.viewNames = publishedView.getName();
				
				result.add(entry);
			} else {
				entry.peerNames += " <unknown>";
				entry.viewNames += publishedView.getName();
			}
		}
		
		return result;
	}
}
