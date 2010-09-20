package de.uniol.inf.is.odysseus.rcp.viewer.queryview.view;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
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
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphEditorConstants;

public class QueryViewPart extends ViewPart implements IPlanModificationListener {

	private Logger logger = LoggerFactory.getLogger(QueryViewPart.class);
	private IExecutor executor;

	private TableViewer tableViewer;

	private Collection<IQuery> queries = new ArrayList<IQuery>();

	public QueryViewPart() {}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite tableComposite = new Composite( parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		
		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		idColumn.getColumn().setText("ID");
//		idColumn.getColumn().setWidth(50);
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getID()));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5,25,true));

		TableViewerColumn statusColumn = new TableViewerColumn( tableViewer, SWT.NONE ) ;
		statusColumn.getColumn().setText("Status");
//		statusColumn.getColumn().setWidth(100);
		statusColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText( ((IQuery)cell.getElement()).isActive() == true ? "Active" : "Inactive");
			}
		});
		tableColumnLayout.setColumnData(statusColumn.getColumn(), new ColumnWeightData(10,50,true));
		
		TableViewerColumn priorityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		priorityColumn.getColumn().setText("Priority");
//		priorityColumn.getColumn().setWidth(100);
		priorityColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getPriority()));
			}
		});
		tableColumnLayout.setColumnData(priorityColumn.getColumn(), new ColumnWeightData(10,50,true));
		
		TableViewerColumn parserIdColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		parserIdColumn.getColumn().setText("Parser");
//		parserIdColumn.getColumn().setWidth(100);
		parserIdColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IQuery) cell.getElement()).getParserId());
			}
		});
		tableColumnLayout.setColumnData(parserIdColumn.getColumn(), new ColumnWeightData(10,50,true));

		TableViewerColumn userColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		userColumn.getColumn().setText("User");
//		userColumn.getColumn().setWidth(400);
		userColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQuery query = (IQuery) cell.getElement();
				if( query.getUser() != null ) 
					cell.setText(query.getUser().getUsername());
				else
					cell.setText("[No user]");
			}
		});
		tableColumnLayout.setColumnData(userColumn.getColumn(), new ColumnWeightData(20,75,true));
		
		TableViewerColumn queryTextColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		queryTextColumn.getColumn().setText("Query");
//		queryTextColumn.getColumn().setWidth(400);
		queryTextColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				String text = ((IQuery) cell.getElement()).getQueryText();
				if( text == null ) {
					cell.setText("[No Text]");
					return;
				}
				text = text.replace('\n', ' ');
				text = text.replace('\r', ' ');
				text = text.replace('\t', ' ');
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(queryTextColumn.getColumn(), new ColumnWeightData(80,200,true));
		
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
				ServiceTracker execTracker = new ServiceTracker(Activator.getDefault().getBundle().getBundleContext(), IExecutor.class.getName(), null);
				execTracker.open();
				try {
					executor = (IExecutor) execTracker.waitForService(0);
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
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			removeQuery((IQuery) eventArgs.getValue());
		} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			addQuery((IQuery) eventArgs.getValue());
		} else if( PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
//			tableViewer.refresh((IQuery) eventArgs.getValue());
			refreshTable();
		} else if( PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
//			tableViewer.refresh((IQuery) eventArgs.getValue());
			refreshTable();
		}
	}
	
	protected void refreshTable() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				tableViewer.refresh();
			}

		});
	}

	private void addQueries(Collection<IQuery> qs) {
		queries.addAll(qs);
		refreshTable();
	}

	private void removeQuery(IQuery q) {
		queries.remove(q);
		refreshTable();
	}

	private void addQuery(IQuery q) {
		queries.add(q);
		refreshTable();
	}
}
