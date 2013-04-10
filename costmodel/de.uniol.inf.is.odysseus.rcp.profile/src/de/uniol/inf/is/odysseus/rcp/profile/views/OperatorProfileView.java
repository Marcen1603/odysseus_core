package de.uniol.inf.is.odysseus.rcp.profile.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.rcp.profile.ProfilePlugIn;
import de.uniol.inf.is.odysseus.rcp.profile.util.RepeatingJobThread;

public class OperatorProfileView extends ViewPart implements ISelectionProvider {

	private interface IOperatorStatisticReader {
		public String get(OperatorStatistic statistic);
	}

	private static final List<OperatorStatistic> operatorStatistics = Lists.newArrayList();

	private static OperatorProfileView instance = null;
	private TableViewer tableViewer;

	private final Collection<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();

	private RepeatingJobThread updater;

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		if (!ProfilePlugIn.hasOperatorCostModel()) {
			createErrorLabel(parent, "Could not provide profiling for physical operators. Operator cost model not bound.");
			return;
		}
		if (!ProfilePlugIn.hasServerExecutor()) {
			createErrorLabel(parent, "Could not provide profiling for physical operators. Server executor not bound");
			return;
		}

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
		final ColumnViewerSorter sorter = new ColumnViewerSorter(tableViewer, column) {
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

		// Elements in operator
		column = createTableColumn(tableViewer, "Elements", new IOperatorStatisticReader() {
			@Override
			public String get(OperatorStatistic statistic) {
				return statistic.hasElementsStoredCount() ? String.valueOf(statistic.getElementsStoredCount()) : "";
			}
		});
		new ColumnViewerSorter(tableViewer, column) {
			@Override
			protected int doCompare(Viewer viewer, OperatorStatistic e1, OperatorStatistic e2) {
				if (e1.hasElementsStoredCount()) {
					if (e2.hasElementsStoredCount()) {
						return Long.compare(e1.getElementsStoredCount(), e2.getElementsStoredCount());
					}
					return -1;
				}

				if (e2.hasElementsStoredCount()) {
					return 1;
				}

				return 0;
			}
		};

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
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setSelection(createSelection());
			}

		});

		getSite().setSelectionProvider(this);
		instance = this;

		updater = new RepeatingJobThread(3000, "Operator profile view Updater") {
			@Override
			public void doJob() {
				updateProfile();
			}
		};

		updater.start();

	}

	@Override
	public void dispose() {
		updater.stopRunning();
		updater = null;
		instance = null;

		super.dispose();
	}

	@Override
	public ISelection getSelection() {
		return createSelection();
	}

	public void refresh(Map<IPhysicalOperator, OperatorStatistic> updatedStatistics) {
		if (!tableViewer.getTable().isDisposed()) {
			
			IPhysicalOperator selectedOperator = null;
			IStructuredSelection structSelect = (IStructuredSelection)tableViewer.getSelection();
			if( !structSelect.isEmpty() ) {
				selectedOperator = ((OperatorStatistic)structSelect.getFirstElement()).getOperator();
			}
			
			synchronized( operatorStatistics ) {
				operatorStatistics.clear();
				operatorStatistics.addAll(updatedStatistics.values());
			}
						
			tableViewer.refresh();
			
			if( selectedOperator != null ) {
				tableViewer.setSelection(new StructuredSelection(updatedStatistics.get(selectedOperator)));
			}
		}
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Override
	public void setSelection(ISelection selection) {
		for (final ISelectionChangedListener l : listeners) {
			if (l != null) {
				l.selectionChanged(new SelectionChangedEvent(this, selection));
			}
		}
	}

	private IStructuredSelection createSelection() {
		final IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (!selection.isEmpty()) {
			final OperatorStatistic stat = (OperatorStatistic) selection.getFirstElement();
			return new StructuredSelection(stat.getOperator());
		}
		return StructuredSelection.EMPTY;
	}

	public static OperatorProfileView getInstance() {
		return instance;
	}

	private static void createErrorLabel(Composite parent, String message) {
		final Label l = new Label(parent, SWT.NONE);
		l.setText(message);
	}

	private static TableViewerColumn createTableColumn(TableViewer tableViewer, String caption, final IOperatorStatisticReader reader) {
		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setText(caption);
		column.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final OperatorStatistic st = (OperatorStatistic) cell.getElement();

				cell.setText(reader.get(st));
			}
		});

		((TableColumnLayout) (tableViewer.getTable().getParent().getLayout())).setColumnData(column.getColumn(), new ColumnWeightData(5, 25, true));
		return column;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<IPhysicalOperator, OperatorStatistic> determineStatistics(Collection<IPhysicalQuery> queries) {
		final Map<IPhysicalOperator, OperatorStatistic> currentStatistics = Maps.newHashMap();

		for (final IPhysicalQuery query : queries) {
			final List operators = query.getPhysicalChilds();
			final OperatorCost<IPhysicalOperator> queryCost = (OperatorCost<IPhysicalOperator>) ProfilePlugIn.getOperatorCostModel().estimateCost(operators, true);
			for (final IPhysicalOperator operator : queryCost.getOperators()) {

				final OperatorCost<IPhysicalOperator> cost = (OperatorCost<IPhysicalOperator>) queryCost.getCostOfOperator(operator);
				final OperatorEstimation<IPhysicalOperator> operatorEstimation = queryCost.getOperatorEstimation(operator);

				final OperatorStatistic statistic = new OperatorStatistic(operator, cost, operatorEstimation);
				currentStatistics.put(operator, statistic);
			}
		}
		return currentStatistics;
	}

	private static void updateProfile() {
		final Collection<IPhysicalQuery> queries = ProfilePlugIn.getServerExecutor().getExecutionPlan().getQueries();
		final Map<IPhysicalOperator, OperatorStatistic> currentStatistics = determineStatistics(queries);

		final Display display = PlatformUI.getWorkbench().getDisplay();
		if (!display.isDisposed()) {
			display.asyncExec(new Runnable() {

				@Override
				public void run() {
					getInstance().refresh(currentStatistics);
				}

			});
		}

	}

}
