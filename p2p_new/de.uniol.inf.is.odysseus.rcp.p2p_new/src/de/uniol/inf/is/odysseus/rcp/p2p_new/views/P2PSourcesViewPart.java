package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.eclipse.jface.action.MenuManager;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class P2PSourcesViewPart extends ViewPart implements IP2PDictionaryListener {

	private static class TableEntry {
		public SourceAdvertisement advertisement;
		
		public int index;
		public String type;
		
		public String sourceNames;
		public String peerNames;
		
		public String schema;
		public String portedName;
	}

	private static final String UNKNOWN_TEXT = "<unknown>";
	private static final String IMPORT_TAG = "[import]";
	private static final String EXPORT_TAG = "[export]";
	
	private static P2PSourcesViewPart instance;
	
	private IP2PDictionary p2pDictionary;
	private TableViewer sourcesTable;
	private List<TableEntry> input = Lists.newArrayList();
	
	public P2PSourcesViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = P2PDictionaryService.get();
		p2pDictionary.addListener(this);
		
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		sourcesTable = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		sourcesTable.getTable().setHeaderVisible(true);
		sourcesTable.getTable().setLinesVisible(true);
		sourcesTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* Index ****************/
		TableViewerColumn idColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		idColumn.getColumn().setText("Index");
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( String.valueOf(((TableEntry)cell.getElement()).index));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(sourcesTable, idColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return Integer.compare(te1.index,te2.index);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Type ****************/
		TableViewerColumn typeColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		typeColumn.getColumn().setText("Type");
		typeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).type);
			}
		});
		tableColumnLayout.setColumnData(typeColumn.getColumn(), new ColumnWeightData(1, 25, true));
		new ColumnViewerSorter(sourcesTable, typeColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.type.compareTo(te2.type);
			}
		};
		
		/************* ViewNames ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		nameColumn.getColumn().setText("Name(s)");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).sourceNames);
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		sorter = new ColumnViewerSorter(sourcesTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.sourceNames.compareTo(te2.sourceNames);
			}
		};


		/************* Peer Names ****************/
		TableViewerColumn peerColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		peerColumn.getColumn().setText("Peer(s)");
		peerColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).peerNames);
			}
		});
		tableColumnLayout.setColumnData(peerColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(sourcesTable, peerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};

		/************* Schema ****************/
		TableViewerColumn schemaColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		schemaColumn.getColumn().setText("Schema");
		schemaColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).schema);
			}
		});
		tableColumnLayout.setColumnData(schemaColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(sourcesTable, schemaColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return 0;
			}
		};
		
		/************* Imported as ****************/
		TableViewerColumn importedViewColumn = new TableViewerColumn(sourcesTable, SWT.NONE);
		importedViewColumn.getColumn().setText("Ported as");
		importedViewColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((TableEntry)cell.getElement()).portedName);
			}
		});
		tableColumnLayout.setColumnData(importedViewColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(sourcesTable, importedViewColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TableEntry te1 = (TableEntry)e1;
				TableEntry te2 = (TableEntry)e2;
				return te1.portedName.compareTo(te2.portedName);
			}
		};

		getSite().setSelectionProvider(sourcesTable);
		
		// Contextmenu
		final MenuManager menuManager = new MenuManager();
		final Menu contextMenu = menuManager.createContextMenu(sourcesTable.getTable());
		// Set the MenuManager
		sourcesTable.getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, sourcesTable);

		
		sourcesTable.setInput(input);
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
		if( sourcesTable != null ) {
			sourcesTable.getTable().setFocus();
		}
	}
	
	public ImmutableList<SourceAdvertisement> getSelectedSourceAdvertisements() {
		ImmutableList.Builder<SourceAdvertisement> resultBuilder = new ImmutableList.Builder<>();
		
		IStructuredSelection selection = (IStructuredSelection) sourcesTable.getSelection();
		if( !selection.isEmpty() ) {
			for( Object selectedObj : selection.toList() ) {
				resultBuilder.add(((TableEntry)selectedObj).advertisement);
			}
			
		}
		return resultBuilder.build();
	}

	// called by p2pDictionary
	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement viewName) {
		refreshTable();
	}

	// called by p2pDictionary
	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement viewName) {
		refreshTable();
	}
	
	// called by p2pDictionary
	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedName = IMPORT_TAG + " " + sourceName ;
			updateTable();
		}
	}
	
	// called by p2pDictionary
	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedName = "";
			updateTable();
		}
	}

	// called by p2pDictionary
	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		// do nothing
	}

	// called by p2pDictionary
	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		// do nothing
	}
	
	// called by p2pDictionary
	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedName = EXPORT_TAG + " " + sourceName ;
			updateTable();
		}
		
	}

	// called by p2pDictionary
	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String viewName) {
		Optional<TableEntry> optEntry = findEntry(advertisement);
		if( optEntry.isPresent() ) {
			optEntry.get().portedName = "";
			updateTable();
		}		
	}
	
	public void refreshTable() {
		input.clear();
		input.addAll( determineTableEntries(p2pDictionary));
		
		updateTable();
	}
	
	public static Optional<P2PSourcesViewPart> getInstance() {
		return Optional.fromNullable(instance);
	}

	private void updateTable() {
		if( !PlatformUI.getWorkbench().getDisplay().isDisposed() && !sourcesTable.getTable().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if( !sourcesTable.getTable().isDisposed() ) {
						sourcesTable.refresh();
					}
				}
			});
		}
	}
	
	private Optional<TableEntry> findEntry( SourceAdvertisement advertisement ) {
		for( TableEntry entry : input ) {
			if( entry.advertisement.equals(advertisement)) {
				return Optional.of(entry);
			}
		}
		return Optional.absent();
	}
	
	private static List<TableEntry> determineTableEntries(IP2PDictionary p2pDictionary) {

		List<TableEntry> result = Lists.newArrayList();

		List<SourceAdvertisement> publishedSources = p2pDictionary.getSources();
		Map<SourceAdvertisement, TableEntry> consumedAdvs = Maps.newHashMap();

		for (SourceAdvertisement publishedSource : publishedSources) {

			List<SourceAdvertisement> sameSources = p2pDictionary.getSame(publishedSource);

			TableEntry entry = null;
			for (SourceAdvertisement sameSource : sameSources) {
				if (consumedAdvs.containsKey(sameSource)) {
					entry = consumedAdvs.get(sameSource);
					break;
				}
			}

			if (entry == null) {
				entry = new TableEntry();

				entry.index = result.size() + 1;
				entry.schema = toString(publishedSource.getOutputSchema());
				entry.type = publishedSource.isStream() ? "Stream" : "View";
				entry.peerNames = determinePeerName(p2pDictionary, publishedSource.getPeerID());
				entry.advertisement = publishedSource;
				entry.portedName = determinePortedName(p2pDictionary, publishedSource);
				entry.sourceNames = publishedSource.getName();

				result.add(entry);
			} else {
				entry.peerNames += (" " + determinePeerName(p2pDictionary, publishedSource.getPeerID()));
				entry.sourceNames += publishedSource.getName();
			}
		}

		return result;
	}
	
	private static String toString(SDFSchema outputSchema) {
		if( outputSchema == null || outputSchema.isEmpty() ) {
			return "[]";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for( int i = 0; i < outputSchema.size(); i++ ) {
			SDFAttribute attribute = outputSchema.get(i);
			sb.append(attribute.getAttributeName()).append(":");
			sb.append(attribute.getDatatype().getQualName().toUpperCase());
			if( i < outputSchema.size() - 1 ) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String determinePeerName( IP2PDictionary dict, PeerID peerID ) {
		if( peerID.equals(dict.getLocalPeerID())) {
			return "_local_";
		}
		
		Optional<String> optPeerName = dict.getPeerRemoteName(peerID);
		if( optPeerName.isPresent() ) {
			return optPeerName.get();
		} 
		return UNKNOWN_TEXT;
	}
	
	private static String determinePortedName( IP2PDictionary dict, SourceAdvertisement srcAdv ) {
		if( dict.isExported(srcAdv.getName())) {
			return EXPORT_TAG + " " + srcAdv.getName();
		} else if( dict.isImported(srcAdv)) {
			return IMPORT_TAG + " " + dict.getImportedSourceName(srcAdv).get();
		} else {
			return "";
		}
	}
}
