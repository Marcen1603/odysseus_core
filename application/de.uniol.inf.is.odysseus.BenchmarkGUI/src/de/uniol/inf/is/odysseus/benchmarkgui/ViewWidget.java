package de.uniol.inf.is.odysseus.benchmarkgui;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TreeDropTargetEffect;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.benchmarker.BenchmarkException;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
//import de.uniol.inf.is.odysseus.benchmarker.IBenchmark.Configuration;

public class ViewWidget extends Composite {

	private final class DropEffect extends TreeDropTargetEffect {
		Tree tree;

		@Override
		public void dragEnter(DropTargetEvent event) {
			if (!tree.isFocusControl()) {
				event.detail = DND.DROP_NONE;
			} else {
				event.detail = DND.DROP_MOVE;
			}
		}

		@Override
		public void dragOperationChanged(DropTargetEvent event) {
			if (!tree.isFocusControl()) {
				event.detail = DND.DROP_NONE;
			} else {
				event.detail = DND.DROP_MOVE;
			}
		}

		private DropEffect(Tree tree) {
			super(tree);
			this.tree = tree;
		}

		public void dragOver(DropTargetEvent event) {
			if (event.detail == DND.DROP_NONE) {
				return;
			}
			event.feedback = DND.FEEDBACK_INSERT_BEFORE;
			super.dragOver(event);
		}

		@Override
		public void drop(DropTargetEvent event) {
			TreeItem item = (TreeItem) event.item;
			if (item == null) {
				return;
			}
			int index = tree.indexOf(item);
			for (TreeItem curItem : tree.getSelection()) {
				TreeItem newItem = new TreeItem(tree, SWT.NONE, index++);
				newItem.setText(curItem.getText());
			}
			for (TreeItem curItem : tree.getSelection()) {
				curItem.dispose();
			}
		}
	}

	private Group grpQuery = null;
	private StyledText txtQuery = null;
	private Group grpScheduler = null;
	private Tree treeScheduler = null;
	private Group grpSchedulingStrategies = null;
	private Tree treeSchedulingStrategies = null;
	private Group grpBufferPlacement = null;
	private Tree treeBufferPlacement = null;
	private Button btnExecute = null;
	private IBenchmark benchmarker = null;

	public ViewWidget(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridLayout());
		createGrpQuery();
		createGrpScheduler();
		createGrpSchedulingStrategies();
		createGrpBufferPlacement();
		this.btnExecute = new Button(this, SWT.NONE);
		this.btnExecute.setText("&Execute");

		final BundleContext bundleContext = Activator.getDefault().getBundle()
				.getBundleContext();
		ServiceTracker tracker = new ServiceTracker(bundleContext,
				IBenchmark.class.getName(), new ServiceTrackerCustomizer() {

					@Override
					public Object addingService(ServiceReference reference) {
						return bundleContext.getService(reference);
					}

					@Override
					public void modifiedService(ServiceReference reference,
							Object service) {

					}

					@Override
					public void removedService(ServiceReference reference,
							Object service) {
					}

				});
		tracker.open();
		try {
			this.benchmarker = (IBenchmark) tracker.waitForService(0);
		} catch (InterruptedException e3) {
			e3.printStackTrace();
		}

		this.btnExecute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
//				Configuration config = new Configuration();
//				try {
//					System.out
//							.println(benchmarker
//									.runBenchmark(
//											"CREATE STREAM XX (a INTEGER) FROM (([0,5),1), ([2,5),3)); SELECT * FROM XX",
//											"CQL", config));
//				} catch (BenchmarkException e1) {
//					e1.printStackTrace();
//				}
			}
		});
		this.setSize(new Point(609, 400));
	}

	public Tree getTreeScheduler() {
		return treeScheduler;
	}

	public Tree getTreeSchedulingStrategies() {
		return treeSchedulingStrategies;
	}

	public StyledText getTxtQuery() {
		return txtQuery;
	}

	public Tree getTreeBufferPlacement() {
		return treeBufferPlacement;
	}

	/**
	 * This method initializes grpQuery
	 * 
	 */
	private void createGrpQuery() {
		GridData gridData7 = new GridData();
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessVerticalSpace = true;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.heightHint = 100;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		grpQuery = new Group(this, SWT.NONE);
		grpQuery.setText("Query:");
		grpQuery.setLayoutData(gridData);
		grpQuery.setLayout(gridLayout1);
		txtQuery = new StyledText(grpQuery, SWT.WRAP);
		txtQuery.setLayoutData(gridData7);
	}

	/**
	 * This method initializes grpScheduler
	 * 
	 */
	private void createGrpScheduler() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new GridData(GridData.FILL_BOTH);
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		grpScheduler = new Group(this, SWT.NONE);
		grpScheduler.setLayout(new GridLayout());
		grpScheduler.setLayoutData(gridData2);
		grpScheduler.setText("Scheduler:");
		treeScheduler = new Tree(grpScheduler, SWT.NONE);
		treeScheduler.setLayoutData(gridData1);
		createDragAndDrop(treeScheduler);
	}

	/**
	 * This method initializes grpSchedulingStrategies
	 * 
	 */
	private void createGrpSchedulingStrategies() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpSchedulingStrategies = new Group(this, SWT.NONE);
		grpSchedulingStrategies.setLayout(new GridLayout());
		grpSchedulingStrategies.setLayoutData(gridData3);
		grpSchedulingStrategies.setText("Scheduling Strategies");
		treeSchedulingStrategies = new Tree(grpSchedulingStrategies, SWT.NONE);
		treeSchedulingStrategies.setLayoutData(gridData5);
		createDragAndDrop(treeSchedulingStrategies);
	}

	private void createDragAndDrop(Tree tree) {
		DragSource ds = new DragSource(tree, DND.DROP_DEFAULT | DND.DROP_MOVE);
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		ds.setTransfer(transfers);

		DropTarget dt = new DropTarget(tree, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt.setTransfer(transfers);
		dt.addDropListener(new DropEffect(tree));
	}

	/**
	 * This method initializes grpBufferPlacement
	 * 
	 */
	private void createGrpBufferPlacement() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData111 = new GridData();
		gridData111.horizontalAlignment = GridData.FILL;
		gridData111.grabExcessHorizontalSpace = true;
		gridData111.grabExcessVerticalSpace = true;
		gridData111.verticalAlignment = GridData.FILL;
		grpBufferPlacement = new Group(this, SWT.NONE);
		grpBufferPlacement.setLayout(new GridLayout());
		grpBufferPlacement.setLayoutData(gridData4);
		grpBufferPlacement.setText("BufferPlacement");
		treeBufferPlacement = new Tree(grpBufferPlacement, SWT.NONE);
		treeBufferPlacement.setLayoutData(gridData111);
	}
} // @jve:decl-index=0:visual-constraint="178,28"
