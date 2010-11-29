package de.uniol.inf.is.odysseus.rcp.viewer.queryview.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.activator.Activator;

public class PartialPlanViewPart extends ViewPart implements IPlanModificationListener {

	private Logger logger = LoggerFactory.getLogger(PartialPlanViewPart.class);
	
	private TableViewer tableViewer;

	private Collection<IPartialPlan> partialPlans = new HashSet<IPartialPlan>();

	IExecutor executor;
	
	public PartialPlanViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.SINGLE
				| SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		TableViewerColumn idColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		idColumn.getColumn().setText("ID");
		// idColumn.getColumn().setWidth(50);
		idColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getId()));
			}
		});
		tableColumnLayout.setColumnData(idColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		TableViewerColumn queryColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		queryColumn.getColumn().setText("Queries");
		// idColumn.getColumn().setWidth(50);
		queryColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getQueries()));
			}
		});
		tableColumnLayout.setColumnData(queryColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		TableViewerColumn sourceColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		sourceColumn.getColumn().setText("Iteratable Sources");
		// idColumn.getColumn().setWidth(50);
		sourceColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getIterableSources()));
			}
		});
		tableColumnLayout.setColumnData(sourceColumn.getColumn(),
				new ColumnWeightData(5, 25, true));

		TableViewerColumn rootsColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		rootsColumn.getColumn().setText("Roots");
		// idColumn.getColumn().setWidth(50);
		rootsColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getRoots()));
			}
		});
		tableColumnLayout.setColumnData(rootsColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		TableViewerColumn basePrioColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		basePrioColumn.getColumn().setText("Base Priority");
		// idColumn.getColumn().setWidth(50);
		basePrioColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getBasePriority()));
			}
		});
		tableColumnLayout.setColumnData(basePrioColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		TableViewerColumn curPrioColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		curPrioColumn.getColumn().setText("Current Priority");
		// idColumn.getColumn().setWidth(50);
		curPrioColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getCurrentPriority()));
			}
		});
		tableColumnLayout.setColumnData(curPrioColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		TableViewerColumn slaRateColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		slaRateColumn.getColumn().setText("SLA Infos");
		// idColumn.getColumn().setWidth(50);
		slaRateColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(String.valueOf(((IPartialPlan) cell.getElement())
						.getScheduleMeta()));
			}
		});
		
		tableColumnLayout.setColumnData(slaRateColumn.getColumn(),
				new ColumnWeightData(5, 25, true));
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(partialPlans);
		getSite().setSelectionProvider(tableViewer);
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(Activator
						.getDefault().getBundle().getBundleContext(),
						IExecutor.class.getName(), null);
				execTracker.open();
				try {
					executor = (IExecutor) execTracker.waitForService(0);
					if (executor != null) {
						setExecutionPlan(executor.getExecutionPlan());
						executor.addPlanModificationListener(PartialPlanViewPart.this);
					} else {
						logger.error("cannot get executor service");
					}
					execTracker.close();
				} catch (InterruptedException e) {
					logger.error("cannot get executor service", e);
				} 
			}


		});

		t.start();
		
		Timer refreshTimer = new Timer();
		refreshTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				try{
					refreshTable();
				}catch(Exception e){
					this.cancel();
				}
			}
		}, 1000, 1000);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		setExecutionPlan(executor.getExecutionPlan());
	}

	public void refreshTable() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				tableViewer.refresh();
			}

		});
	}


	private void setExecutionPlan(IExecutionPlan executionPlan) {
		partialPlans.clear();
		partialPlans.addAll(executionPlan.getPartialPlans());
		refreshTable();		
	}
	
	
}
