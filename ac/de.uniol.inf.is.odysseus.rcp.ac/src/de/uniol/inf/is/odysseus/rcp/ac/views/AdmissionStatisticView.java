package de.uniol.inf.is.odysseus.rcp.ac.views;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.ac.standard.AdmissionStatus;
import de.uniol.inf.is.odysseus.ac.standard.IAdmissionStatusListener;
import de.uniol.inf.is.odysseus.ac.standard.StandardAC;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;

public class AdmissionStatisticView extends ViewPart implements IAdmissionStatusListener {

	private static final List<OperatorStatistic> operatorStatistics = Lists.newArrayList();

	private static AdmissionStatisticView instance = null;
	
	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);

		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		// Name
		TableViewerColumn column = createTableColumn(tableViewer, "Operator", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return statistic.getName();
			}
		});
		ColumnViewerSorter sorter = new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return e1.getName().compareTo(e2.getName());
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);
		
		// HashID
		column = createTableColumn(tableViewer, "Hash", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return String.valueOf(statistic.getHashID());
			}
		});
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return Integer.compare(e1.getHashID(), e2.getHashID());
			}
		};
		
		// Type
		column = createTableColumn(tableViewer, "Type", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return statistic.getType();
			}
		});
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return e1.getType().compareTo(e2.getType());
			}
		};
		
		// CPU
		column = createTableColumn(tableViewer, "CPU (%)", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return String.format("%-5.3f", (statistic.getCpuCost() / Runtime.getRuntime().availableProcessors()) * 100.0);
			}
		});
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return Double.compare(e1.getCpuCost(), e2.getCpuCost());
			}
		};
		
		// MEM
		column = createTableColumn(tableViewer, "Elements ", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				IPhysicalOperator op = statistic.getOp();
				if (op instanceof AbstractSource){
					return ""+((AbstractSource) op).getElementsStored(); 
				}else{
					return "NOS";
				}
			}
		});
//		new ColumnViewerSorter(tableViewer, column) {
//			@Override
//			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
//				return Double.compare(e1.getCpuCost(), e2.getCpuCost());
//			}
//		};
		
		
		column = createTableColumn(tableViewer, "Sel.", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return String.format("%-1.4f", statistic.getSelectivity());
			}
		});	
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return Double.compare(e1.getSelectivity(), e2.getSelectivity());
			}
		};
		
		column = createTableColumn(tableViewer, "Datarate", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return String.format("%-8.2f", statistic.getDataRate());
			}
		});	
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				return Double.compare(e1.getDataRate(), e2.getDataRate());
			}
		};

		
		tableViewer.setInput(operatorStatistics);
		
		instance = this;
	}

	@Override
	public void dispose() {
		instance = null;

		super.dispose();
	}
	
	public void refresh() {
		tableViewer.refresh();
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	// called async by admission control
	@Override
	public void updateAdmissionStatus(StandardAC admissionControl, AdmissionStatus status) {
		if (isShowing()) {
			Set<IPhysicalQuery> queries = status.getQueries();
			Map<IPhysicalOperator, OperatorStatistic> currentStatistics = determineStatistics(status, queries);
			
			synchronized( operatorStatistics ) {
				operatorStatistics.clear();
				operatorStatistics.addAll(currentStatistics.values());
			}

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if( isShowing() ) {
						synchronized(operatorStatistics) {
							getInstance().refresh(); // use operatorStatistics-list indirectly
						}
					}
				}
				
			});
			
		}
	}

	public static AdmissionStatisticView getInstance() {
		return instance;
	}

	public static boolean isShowing() {
		return getInstance() != null;
	}

	private static Map<IPhysicalOperator, OperatorStatistic> determineStatistics(AdmissionStatus status, Set<IPhysicalQuery> queries) {
		Map<IPhysicalOperator, OperatorStatistic> currentStatistics = Maps.newHashMap();
		
		for( IPhysicalQuery query : queries ) {
			OperatorCost<IPhysicalOperator> queryCost = (OperatorCost<IPhysicalOperator>)status.getQueryCosts(query);
			for( IPhysicalOperator operator : queryCost.getOperators() ) {
				
				OperatorCost<IPhysicalOperator> cost = (OperatorCost<IPhysicalOperator>)queryCost.getCostOfOperator(operator);
				OperatorEstimation<IPhysicalOperator> operatorEstimation = queryCost.getOperatorEstimation(operator);
				
				OperatorStatistic statistic = new OperatorStatistic(operator, cost, operatorEstimation);
				currentStatistics.put(operator, statistic);
			}
		}
		return currentStatistics;
	}
	
	private static TableViewerColumn createTableColumn(TableViewer tableViewer, String caption, final IOperatorStatisticReader reader) {
		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setText(caption);
		column.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				OperatorStatistic st = (OperatorStatistic)cell.getElement();
				
				cell.setText(reader.get(st));
			}
		});
		
		((TableColumnLayout)(tableViewer.getTable().getParent().getLayout())).setColumnData(column.getColumn(), new ColumnWeightData(5, 25, true));
		return column;
	}
	
	private interface IOperatorStatisticReader {
		public String get(OperatorStatistic statistic);
	}
}
