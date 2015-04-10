package de.uniol.inf.is.odysseus.peer.rcp.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class ChatView extends ViewPart implements IPeerCommunicatorListener, IP2PDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(ChatView.class);

	private final List<PeerID> peerIDs = Lists.newArrayList();

	private TableViewer peersTable;
	private Text chatText;
	private Text inputText;

	private RepeatingJobThread updater;

	@Override
	public void createPartControl(Composite parent) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(2, false));

		createNamesList(rootComposite);
		createChatTextfield(rootComposite);
		createChatInput(rootComposite);

		RCPP2PNewPlugIn.getPeerCommunicator().addListener(this, ChatMessage.class);
		
		RCPP2PNewPlugIn.getP2PDictionary().addListener(this);
	}

	private void createNamesList(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.minimumWidth = 250;
		gd.widthHint = 250;
		tableComposite.setLayoutData(gd);

		peersTable = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		peersTable.getTable().setHeaderVisible(true);
		peersTable.getTable().setLinesVisible(true);
		peersTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* Index ****************/
		TableViewerColumn indexColumn = new TableViewerColumn(peersTable, SWT.NONE);
		indexColumn.getColumn().setText("Index");
		indexColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				int index = peerIDs.indexOf(cell.getElement());
				cell.setText(String.valueOf(index));
			}
		});
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(peersTable, indexColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				int index1 = peerIDs.indexOf(e1);
				int index2 = peerIDs.indexOf(e2);
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
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(peersTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determinePeerName((PeerID) e1);
				String n2 = determinePeerName((PeerID) e2);
				return n1.compareTo(n2);
			}
		};
		
		peersTable.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(peersTable.getCell(new Point(e.x, e.y)) == null) {
					peersTable.setSelection(StructuredSelection.EMPTY);
				}
			}
		});

		peersTable.setInput(peerIDs);
		
		updater = new RepeatingJobThread(5000, "ChatView updater") {
			@Override
			public void doJob() {
				updateTable();
			}
		};
		updater.start();
		
		updateTable();
	}

	private static String determinePeerName(PeerID peerID) {
		return RCPP2PNewPlugIn.getPeerDictionary().getRemotePeerName(peerID);
	}

	private void updateTable() {
		peerIDs.clear();
		peerIDs.addAll(RCPP2PNewPlugIn.getPeerDictionary().getRemotePeerIDs());

		refreshTableAsync();
	}

	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
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

	private void createChatTextfield(Composite parent) {
		chatText = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		chatText.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createChatInput(Composite parent) {
		inputText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		inputText.setLayoutData(gd);

		inputText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Enter pressed
				if (e.keyCode == 13 && !Strings.isNullOrEmpty(inputText.getText())) {
					sendMessageAsync(inputText.getText());
					inputText.setText("");
				}
			}
		});
	}

	private void sendMessageAsync(String text) {
		LOG.debug("Sending chat message '{}'", text);
		
		Optional<PeerID> selectedPeerID = determineSelectedPeerID();

		try {
			if (selectedPeerID.isPresent()) {
				appendToChatTextAsync(getCurrentTime() + " " + RCPP2PNewPlugIn.getP2PNetworkManager().getLocalPeerName() + " (to " + determinePeerName(selectedPeerID.get()) + "): " + text);

				ChatMessage chat = new ChatMessage(text, true);
				RCPP2PNewPlugIn.getPeerCommunicator().send(selectedPeerID.get(), chat);
			} else {
				appendToChatTextAsync(getCurrentTime() + " " + RCPP2PNewPlugIn.getP2PNetworkManager().getLocalPeerName() + ": " + text);
				
				ChatMessage chat = new ChatMessage(text, false);
				for (PeerID peerID : RCPP2PNewPlugIn.getPeerDictionary().getRemotePeerIDs()) {
					RCPP2PNewPlugIn.getPeerCommunicator().send(peerID, chat);
				}
			}
			
		} catch (PeerCommunicationException e) {
			LOG.error("Cannot send message", e);
		}
	}

	private static String getCurrentTime() {
		SimpleDateFormat date=new SimpleDateFormat("HH:mm");
		return "["+ date.format(new Date()) + "]";
	}

	private Optional<PeerID> determineSelectedPeerID() {
		IStructuredSelection selection = (IStructuredSelection) peersTable.getSelection();
		if (selection.isEmpty()) {
			return Optional.absent();
		}

		return Optional.of((PeerID) selection.getFirstElement());
	}

	private void appendToChatTextAsync(final String text) {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!chatText.isDisposed()) {
						chatText.append("\n" + text);
					}
				}
			});
		}
	}

	@Override
	public void dispose() {
		updater.stopRunning();
		
		RCPP2PNewPlugIn.getP2PDictionary().removeListener(this);
		
		RCPP2PNewPlugIn.getPeerCommunicator().removeListener(this, ChatMessage.class);
		
		super.dispose();
	}

	@Override
	public void setFocus() {
		inputText.setFocus();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communciator, PeerID senderPeer, IMessage message) {
		ChatMessage chatMessage = (ChatMessage)message;
		
		String senderPeerName = determinePeerName(senderPeer);
		if( chatMessage.isWhisper() ) {
			appendToChatTextAsync(getCurrentTime() + " " + senderPeerName + " (whisper): " + chatMessage.getText());
		} else {
			appendToChatTextAsync(getCurrentTime() + " " + senderPeerName + ": " + chatMessage.getText());
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
