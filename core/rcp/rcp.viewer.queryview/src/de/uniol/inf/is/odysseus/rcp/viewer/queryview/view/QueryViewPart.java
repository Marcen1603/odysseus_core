package de.uniol.inf.is.odysseus.rcp.viewer.queryview.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphEditorConstants;

public class QueryViewPart extends ViewPart implements IPlanModificationListener {

	private Logger logger = LoggerFactory.getLogger(QueryViewPart.class);
	private IExecutor executor;

	private TableViewer tableViewer;

	private Collection<IQuery> queries = new ArrayList<IQuery>();

	public QueryViewPart() {
	}

	Timer refreshTimer = null;

	@Override
	public void createPartControl(Composite parent) {

		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		/************* ID ****************/
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		idColumn.getColumn().setText("ID");
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getID()));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5, 25, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(tableViewer, idColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				if( id1.getID() > id2.getID() ) 
					return 1;
				else if( id1.getID() < id2.getID() )
					return -1;
				else 
					return 0;
			}
		};
		
		/************* Status ****************/
		TableViewerColumn statusColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		statusColumn.getColumn().setText("Status");
		// statusColumn.getColumn().setWidth(100);
		statusColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				String text = getQueryStatus((IQuery) cell.getElement());
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(statusColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, statusColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				String s1 = getQueryStatus(id1);
				String s2 = getQueryStatus(id2);
				
				if( s1.equals(s2)) 
					return 0;
				else if( s1.equals("Opened")) 
					return 1;
				else if( s1.equals("Active") )
					if( s2.equals("Inactive")) 
						return 1;
					else 
						return -1;
				else 
					return -1;
			}
		};

		/************* Priority ****************/		
		TableViewerColumn priorityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		priorityColumn.getColumn().setText("Priority");
		priorityColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IQuery) cell.getElement()).getPriority()));
			}
		});
		tableColumnLayout.setColumnData(priorityColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, priorityColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				if( id1.getPriority() > id2.getPriority() ) 
					return 1;
				else if( id1.getPriority() < id2.getPriority() )
					return -1;
				else 
					return 0;
			}
		};
		
		/************* Parser ID ****************/
		TableViewerColumn parserIdColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		parserIdColumn.getColumn().setText("Parser");
		// parserIdColumn.getColumn().setWidth(100);
		parserIdColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((IQuery) cell.getElement()).getParserId());
			}
		});
		tableColumnLayout.setColumnData(parserIdColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, parserIdColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				return id1.getParserId().compareToIgnoreCase(id2.getParserId());
			}
		};

		/************* User ****************/
		TableViewerColumn userColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		userColumn.getColumn().setText("User");
		// userColumn.getColumn().setWidth(400);
		userColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				IQuery query = (IQuery) cell.getElement();
				if (query.getUser() != null)
					cell.setText(query.getUser().getUsername());
				else
					cell.setText("[No user]");
			}
		});
		tableColumnLayout.setColumnData(userColumn.getColumn(), new ColumnWeightData(5, 25, true));
		new ColumnViewerSorter(tableViewer, userColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				return id1.getUser().getUsername().compareToIgnoreCase(id2.getUser().getUsername());
			}
		};

		/************* Query Text ****************/
		TableViewerColumn queryTextColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		queryTextColumn.getColumn().setText("Query text");
		// queryTextColumn.getColumn().setWidth(400);
		queryTextColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				String text = ((IQuery) cell.getElement()).getQueryText();
				if (text == null) {
					cell.setText("[No Text]");
					return;
				}
				text = text.replace('\n', ' ');
				text = text.replace('\r', ' ');
				text = text.replace('\t', ' ');
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(queryTextColumn.getColumn(), new ColumnWeightData(50, 200, true));
		new ColumnViewerSorter(tableViewer, queryTextColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				return id1.getQueryText().compareToIgnoreCase(id2.getQueryText());
			}
		};

		/************* Monitor ****************/
		TableViewerColumn monitorColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		monitorColumn.getColumn().setText("Monitors");
		// monitorColumn.getColumn().setWidth(100);
		monitorColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				String text = ((IQuery) cell.getElement()).getPlanMonitors() + "";
				cell.setText(text);
			}
		});
		tableColumnLayout.setColumnData(monitorColumn.getColumn(), new ColumnWeightData(40, 50, true));
		new ColumnViewerSorter(tableViewer, monitorColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				IQuery id1 = (IQuery)e1;
				IQuery id2 = (IQuery)e2;
				return (id1.getPlanMonitors() + "").compareToIgnoreCase(id2.getPlanMonitors() + "");
			}
		};

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
		
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

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
						addQueries(executor.getPlan().getQueries());

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

//		refreshTimer = new Timer();
//		refreshTimer.scheduleAtFixedRate(new TimerTask() {
//
//			@Override
//			public void run() {
//				try {
//					refreshTable();
//				} catch (Exception e) {
//					this.cancel();
//				}
//			}
//		}, 1000, 1000);
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
		} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
			// tableViewer.refresh((IQuery) eventArgs.getValue());
			refreshTable();
		} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
			// tableViewer.refresh((IQuery) eventArgs.getValue());
			refreshTable();
		}
	}

	public void refreshTable() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!tableViewer.getControl().isDisposed())
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
	
	private String getQueryStatus( IQuery q ) {
		if( q.isActive() ) 
			if( q.isOpened() ) 
				return "Opened";
			else
				return "Active";
		else 
			return "Inactive";
	}

	private static abstract class ColumnViewerSorter extends ViewerComparator {
		public static final int ASC = 1;

		public static final int NONE = 0;

		public static final int DESC = -1;

		private int direction = 0;

		private TableViewerColumn column;

		private ColumnViewer viewer;

		public ColumnViewerSorter(ColumnViewer viewer, TableViewerColumn column) {
			this.column = column;
			this.viewer = viewer;
			this.column.getColumn().addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					if (ColumnViewerSorter.this.viewer.getComparator() != null) {
						if (ColumnViewerSorter.this.viewer.getComparator() == ColumnViewerSorter.this) {
							int tdirection = ColumnViewerSorter.this.direction;

							if (tdirection == ASC) {
								setSorter(ColumnViewerSorter.this, DESC);
							} else if (tdirection == DESC) {
								setSorter(ColumnViewerSorter.this, NONE);
							}
						} else {
							setSorter(ColumnViewerSorter.this, ASC);
						}
					} else {
						setSorter(ColumnViewerSorter.this, ASC);
					}
				}
			});
		}

		public void setSorter(ColumnViewerSorter sorter, int direction) {
			if (direction == NONE) {
				column.getColumn().getParent().setSortColumn(null);
				column.getColumn().getParent().setSortDirection(SWT.NONE);
				viewer.setComparator(null);
			} else {
				column.getColumn().getParent().setSortColumn(column.getColumn());
				sorter.direction = direction;

				if (direction == ASC) {
					column.getColumn().getParent().setSortDirection(SWT.DOWN);
				} else {
					column.getColumn().getParent().setSortDirection(SWT.UP);
				}

				if (viewer.getComparator() == sorter) {
					viewer.refresh();
				} else {
					viewer.setComparator(sorter);
				}

			}
		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			return direction * doCompare(viewer, e1, e2);
		}

		protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
	}
}
