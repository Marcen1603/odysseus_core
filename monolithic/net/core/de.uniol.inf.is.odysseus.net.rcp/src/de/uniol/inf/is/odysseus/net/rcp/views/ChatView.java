package de.uniol.inf.is.odysseus.net.rcp.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;
import de.uniol.inf.is.odysseus.net.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.rcp.util.ColumnViewerSorter;

public class ChatView extends ViewPart implements IOdysseusNodeCommunicatorListener {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.ChatView";
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatView.class);
	
	private final List<IOdysseusNode> nodes = Lists.newArrayList();

	private TableViewer nodesTable;
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

		OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().addListener(this, ChatMessage.class);
	}
	
	@Override
	public void dispose() {
		updater.stopRunning();

		OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().removeListener(this, ChatMessage.class);

		super.dispose();
	}

	private void createNamesList(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.minimumWidth = 250;
		gd.widthHint = 250;
		tableComposite.setLayoutData(gd);

		nodesTable = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		nodesTable.getTable().setHeaderVisible(true);
		nodesTable.getTable().setLinesVisible(true);
		nodesTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* Index ****************/
		TableViewerColumn indexColumn = new TableViewerColumn(nodesTable, SWT.NONE);
		indexColumn.getColumn().setText("Index");
		indexColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				int index = nodes.indexOf(cell.getElement());
				cell.setText(String.valueOf(index));
			}
		});
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(nodesTable, indexColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				int index1 = nodes.indexOf(e1);
				int index2 = nodes.indexOf(e2);
				return Integer.compare(index1, index2);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* Name ****************/
		TableViewerColumn nameColumn = new TableViewerColumn(nodesTable, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(determineNodeName((IOdysseusNode) cell.getElement()));
			}
		});
		tableColumnLayout.setColumnData(nameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(nodesTable, nameColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				String n1 = determineNodeName((IOdysseusNode) e1);
				String n2 = determineNodeName((IOdysseusNode) e2);
				return n1.compareTo(n2);
			}
		};

		nodesTable.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (nodesTable.getCell(new Point(e.x, e.y)) == null) {
					nodesTable.setSelection(StructuredSelection.EMPTY);
				}
			}
		});

		nodesTable.setInput(nodes);

		updater = new RepeatingJobThread(5000, "ChatView updater") {
			@Override
			public void doJob() {
				updateTable();
			}
		};
		updater.start();

		updateTable();
	}

	private static String determineNodeName(IOdysseusNode node) {
		return node.getName();
	}

	private void updateTable() {
		nodes.clear();
		nodes.addAll(OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().getDestinationNodes());

		refreshTableAsync();
	}

	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!nodesTable.getTable().isDisposed()) {
						nodesTable.refresh();
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

		Optional<IOdysseusNode> optSelectedNodeConnection = determineSelectedNodeConnection();

		try {
			if (optSelectedNodeConnection.isPresent()) {
				IOdysseusNode node = optSelectedNodeConnection.get();
				
				appendToChatTextAsync(getCurrentTime() + " " +  OdysseusNetRCPPlugIn.getOdysseusNetStartupManager().getLocalOdysseusNode().getName() + " (to " + node.getName() + "): " + text);

				ChatMessage chat = new ChatMessage(text, true);
				OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().send(node, chat);
			} else {
				appendToChatTextAsync(getCurrentTime() + " " + OdysseusNetRCPPlugIn.getOdysseusNetStartupManager().getLocalOdysseusNode().getName() + ": " + text);

				ChatMessage chat = new ChatMessage(text, false);
				for (IOdysseusNode node : OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().getDestinationNodes()) {
					OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().send(node, chat);
				}
			}

		} catch (OdysseusNodeCommunicationException e) {
			LOG.error("Cannot send message", e);
		} catch (OdysseusNetException e) {
			LOG.error("Could not send message", e);
		}
	}

	private static String getCurrentTime() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm");
		return "[" + date.format(new Date()) + "]";
	}

	private Optional<IOdysseusNode> determineSelectedNodeConnection() {
		IStructuredSelection selection = (IStructuredSelection) nodesTable.getSelection();
		if (selection.isEmpty()) {
			return Optional.absent();
		}

		return Optional.of((IOdysseusNode) selection.getFirstElement());
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
	public void setFocus() {
		inputText.setFocus();
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communciator, IOdysseusNode senderNode, IMessage message) {
		ChatMessage chatMessage = (ChatMessage) message;

		String senderNodeName = senderNode.getName();
		if (chatMessage.isWhisper()) {
			appendToChatTextAsync(getCurrentTime() + " " + senderNodeName + " (whisper): " + chatMessage.getText());
		} else {
			appendToChatTextAsync(getCurrentTime() + " " + senderNodeName + ": " + chatMessage.getText());
		}
	}
}
