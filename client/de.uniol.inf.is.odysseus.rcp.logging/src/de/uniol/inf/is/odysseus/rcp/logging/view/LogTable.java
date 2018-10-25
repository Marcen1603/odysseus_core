package de.uniol.inf.is.odysseus.rcp.logging.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Level;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import java.util.Objects;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;
import de.uniol.inf.is.odysseus.rcp.logging.container.IRCPLogContainerListener;
import de.uniol.inf.is.odysseus.rcp.logging.container.LogContentProvider;
import de.uniol.inf.is.odysseus.rcp.logging.container.RCPLogContainer;
import de.uniol.inf.is.odysseus.rcp.logging.view.filter.IRCPLogFilter;
import de.uniol.inf.is.odysseus.rcp.logging.view.util.CellLabelProviderAndSorter;
import de.uniol.inf.is.odysseus.rcp.logging.view.util.LevelImageMap;

public class LogTable implements IRCPLogContainerListener {

	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private final TableViewer tableViewer;
	private final RCPLogContainer logContainer;

	private final LogContentProvider logContentProvider;
	private final List<RCPLogEntry> entriesList = Lists.newArrayList();

	private final LevelImageMap imageMap;

	private final List<ILogTableListener> listeners = new CopyOnWriteArrayList<ILogTableListener>();

	public LogTable(Composite parent, RCPLogContainer logContainer) {
		Objects.requireNonNull(parent, "Parent composite must not be null!");
		Objects.requireNonNull(logContainer, "RCP log container must not be null!");

		this.logContainer = logContainer;
		this.logContainer.addListener(this);

		imageMap = new LevelImageMap();

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				fireListeners();
			}
		});

		TableViewerColumn timeColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		timeColumn.getColumn().setText("Time");
		tableColumnLayout.setColumnData(timeColumn.getColumn(), new ColumnWeightData(2, 15, true));
		timeColumn.setLabelProvider(new CellLabelProviderAndSorter<RCPLogEntry, Long>(tableViewer, timeColumn) {
			@Override
			protected Long getValue(RCPLogEntry entry) {
				return entry.getTimestamp();
			}

			@Override
			protected int compareValues(Long a, Long b) {
				return Long.compare(a, b);
			}

			@Override
			protected String toString(Long value) {
				return TIME_FORMAT.format(new Date(value));
			}
		});

		TableViewerColumn levelColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		levelColumn.getColumn().setText("Level");
		tableColumnLayout.setColumnData(levelColumn.getColumn(), new ColumnWeightData(2, 15, true));
		levelColumn.setLabelProvider(new CellLabelProviderAndSorter<RCPLogEntry, Level>(tableViewer, levelColumn) {
			@Override
			protected Level getValue(RCPLogEntry entry) {
				return entry.getLevel();
			}

			@Override
			protected Image getImage(Level value) {
				return imageMap.get(value);
			}
		});

		TableViewerColumn classColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		classColumn.getColumn().setText("Location");
		tableColumnLayout.setColumnData(classColumn.getColumn(), new ColumnWeightData(6, 15, true));
		classColumn.setLabelProvider(new CellLabelProviderAndSorter<RCPLogEntry, String>(tableViewer, classColumn) {
			@Override
			protected String getValue(RCPLogEntry entry) {
				return entry.getSimpleClassName() + "." + entry.getMethodName() + "(): " + entry.getLineNumber();
			}
		});

		TableViewerColumn messageColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		messageColumn.getColumn().setText("Message");
		tableColumnLayout.setColumnData(messageColumn.getColumn(), new ColumnWeightData(20, 15, true));
		messageColumn.setLabelProvider(new CellLabelProviderAndSorter<RCPLogEntry, String>(tableViewer, messageColumn) {
			@Override
			protected String getValue(RCPLogEntry entry) {
				return getMiniMessage(entry.getMessage());
			}

		});

		TableViewerColumn throwableColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		throwableColumn.getColumn().setText("Exception");
		tableColumnLayout.setColumnData(throwableColumn.getColumn(), new ColumnWeightData(3, 15, true));
		throwableColumn.setLabelProvider(new CellLabelProviderAndSorter<RCPLogEntry, String>(tableViewer, throwableColumn) {
			@Override
			protected String getValue(RCPLogEntry entry) {
				if (entry.hasThrowable()) {
					return entry.getThrowable().getClass().getSimpleName();
				}
				return ""; // no throwable available
			}
		});

		logContentProvider = new LogContentProvider();
		tableViewer.setContentProvider(logContentProvider);

		loadTableInput(logContainer);
		tableViewer.setInput(entriesList);

		hideSelectionIfNeeded(tableViewer);
	}

	private void loadTableInput(RCPLogContainer logContainer) {
		entriesList.clear();
		entriesList.addAll(logContainer.getLogEntriesInstance());
	}

	private static String getMiniMessage(String message) {
		int pos = message.indexOf("\n");
		return pos == -1 ? message : message.substring(0, pos) + " [...]";
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

	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void dispose() {
		logContainer.removeListener(this);
	}

	@Override
	public void logAdded(RCPLogContainer sender, RCPLogEntry newLogEntry) {
		entriesList.add(newLogEntry);

		refreshTableAsync(tableViewer);
	}

	@Override
	public void logRemoved(RCPLogContainer sender, RCPLogEntry oldLogEntry) {
		entriesList.remove(oldLogEntry);

		refreshTableAsync(tableViewer);
	}

	private static void refreshTableAsync(final TableViewer tableViewer) {
		Display display = PlatformUI.getWorkbench().getDisplay();

		if (!tableViewer.getTable().isDisposed() && !display.isDisposed()) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!tableViewer.getTable().isDisposed()) {
						tableViewer.refresh();
					}
				}
			});
		}
	}

	public final TableViewer getTableViewer() {
		return tableViewer;
	}

	public void clear() {
		entriesList.clear();
		refreshTableAsync(tableViewer);
	}

	public void restore() {
		loadTableInput(logContainer);

		refreshTableAsync(tableViewer);
	}

	public void setFilter(IRCPLogFilter filter) {
		logContentProvider.setFilter(filter);

		refreshTableAsync(tableViewer);
	}

	public List<RCPLogEntry> getShownLogEntries() {
		return logContentProvider.applyFilter(entriesList);
	}

	public List<RCPLogEntry> getSelectedLogEntries() {
		List<RCPLogEntry> entries = Lists.newArrayList();

		ISelection selection = tableViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structSelection = (IStructuredSelection) selection;

			if (!structSelection.isEmpty()) {
				Object[] selectedEntriesAsObjects = structSelection.toArray();

				for (Object selectedEntryAsObject : selectedEntriesAsObjects) {
					entries.add((RCPLogEntry) selectedEntryAsObject);
				}
			}
		}

		return entries;
	}

	public void addListener(ILogTableListener listener) {
		Objects.requireNonNull(listener, "Listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(ILogTableListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected final void fireListeners() {
		List<RCPLogEntry> selectedEntries = getSelectedLogEntries();

		synchronized (listeners) {
			for (ILogTableListener listener : listeners) {
				try {
					listener.selectionChanged(this, Lists.newArrayList(selectedEntries));
				} catch (Throwable t) {
					System.err.println("Exception in log table listener");
					t.printStackTrace(System.err);
				}
			}
		}
	}
}
