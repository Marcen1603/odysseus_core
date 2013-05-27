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
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class PeerView extends ViewPart implements IP2PDictionaryListener {

	private static final String UNKNOWN_PEER_NAME = "<unknown>";

	private static PeerView instance;
	
	private final List<PeerID> foundPeerIDs = Lists.newArrayList();
	
	private IP2PDictionary p2pDictionary;

	private TableViewer peersTable; 
	
	@Override
	public void createPartControl(Composite parent) {
		p2pDictionary = P2PDictionaryService.get();
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
		indexColumn.getColumn().setText("Index");
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
				return Integer.compare(index1,index2);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(peersTable, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(determinePeerName((PeerID)cell.getElement()));
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determinePeerName((PeerID) e1);
				String n2 = determinePeerName((PeerID) e2);
				return n1.compareTo(n2);
			}
		};
		
		/************* PeerID ****************/
		TableViewerColumn peerIDColumn = new TableViewerColumn(peersTable, SWT.NONE);
		peerIDColumn.getColumn().setText("PeerID");
		peerIDColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((PeerID)cell.getElement()).toString());
			}
		});
		tableColumnLayout.setColumnData(peerIDColumn.getColumn(), new ColumnWeightData(10, 25, true));
		sorter = new ColumnViewerSorter(peersTable, peerIDColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return ((PeerID)e1).toString().compareTo(((PeerID)e2).toString());
			}
		};
			
		peersTable.setInput(foundPeerIDs);
		refreshTable();
		
		instance = this;
	}

	@Override
	public void dispose() {
		instance = null;

		P2PDictionaryService.get().removeListener(this);

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
					if( !peersTable.getTable().isDisposed() ) {
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
		synchronized (foundPeerIDs) {
			foundPeerIDs.add(id);
		}

		refreshTable();
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		synchronized (foundPeerIDs) {
			foundPeerIDs.remove(id);
		}

		refreshTable();
	}
	
	private String determinePeerName( PeerID peerID ) {
		Optional<String> optPeerName = p2pDictionary.getPeerRemoteName(peerID);
		return optPeerName.isPresent() ? optPeerName.get() : UNKNOWN_PEER_NAME;
	}

}
