package de.uniol.inf.is.odysseus.rcp.viewer.queryview.view;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphEditorConstants;

public class QueryViewPart extends ViewPart implements IPlanModificationListener {

	private Logger logger = LoggerFactory.getLogger(QueryViewPart.class);
	private IAdvancedExecutor executor;

	private TableViewer tableViewer;

	private Collection<IQuery> queries = new ArrayList<IQuery>();

	public QueryViewPart() {}

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		idColumn.getColumn().setText("ID");
		idColumn.getColumn().setWidth(50);
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getID()));
			}
		});

		TableViewerColumn statusColumn = new TableViewerColumn( tableViewer, SWT.NONE ) ;
		statusColumn.getColumn().setText("Status");
		statusColumn.getColumn().setWidth(100);
		statusColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((IQuery)cell.getElement()).isRunning() == true ? "Running" : "Stopped");
			}
		});
		
		TableViewerColumn priorityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		priorityColumn.getColumn().setText("Priority");
		priorityColumn.getColumn().setWidth(100);
		priorityColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getPriority()));
			}
		});
		
		TableViewerColumn parserIdColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		parserIdColumn.getColumn().setText("Parser");
		parserIdColumn.getColumn().setWidth(100);
		parserIdColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IQuery) cell.getElement()).getParserId());
			}
		});

		TableViewerColumn userColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		userColumn.getColumn().setText("User");
		userColumn.getColumn().setWidth(400);
		userColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IQuery) cell.getElement()).getUser().getUsername());
			}
		});
		
		TableViewerColumn queryTextColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		queryTextColumn.getColumn().setText("Query");
		queryTextColumn.getColumn().setWidth(400);
		queryTextColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IQuery) cell.getElement()).getQueryText());
			}
		});
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(queries);
		getSite().setSelectionProvider(tableViewer);

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);

				try {
					handlerService.executeCommand(IGraphEditorConstants.CALL_GRAPH_EDITOR_COMMAND_ID, null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
		// Set the MenuManager
		tableViewer.getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, tableViewer);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(Activator.getDefault().getBundle().getBundleContext(), IAdvancedExecutor.class.getName(), null);
				execTracker.open();
				try {
					executor = (IAdvancedExecutor) execTracker.waitForService(0);
					if (executor != null) {
						addQueries(executor.getSealedPlan().getQueries());

						executor.addPlanModificationListener(QueryViewPart.this);
					} else {
						logger.error("cannot get executor service");
					}
					execTracker.close();
				} catch (InterruptedException e) {
					logger.error("cannot get executor service", e);
				} catch (PlanManagementException e) {
					logger.error("cannot get queries", e);
				}
			}

		});

		t.start();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if ("QUERY_REMOVE".equals(eventArgs.getID())) {
			removeQuery((IQuery) eventArgs.getValue());
		} else if ("QUERY_ADDED".equals(eventArgs.getID())) {
			addQuery((IQuery) eventArgs.getValue());
		} else if( "QUERY_STOP".equals(eventArgs.getID())) {
			tableViewer.refresh((IQuery) eventArgs.getValue());
		} else if( "QUERY_START".equals(eventArgs.getID())) {
			tableViewer.refresh((IQuery) eventArgs.getValue());
		}
	}

	private void addQueries(Collection<IQuery> qs) {
		queries.addAll(qs);
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				tableViewer.refresh();
			}

		});
	}

	private void removeQuery(IQuery q) {
		queries.remove(q);
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				tableViewer.refresh();
			}

		});
	}

	private void addQuery(IQuery q) {
		queries.add(q);
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				tableViewer.refresh();
			}

		});
	}
}
