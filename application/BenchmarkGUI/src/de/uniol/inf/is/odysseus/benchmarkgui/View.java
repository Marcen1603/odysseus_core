package de.uniol.inf.is.odysseus.benchmarkgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

public class View extends ViewPart implements ServiceListener {
	public static final String ID = "de.uniol.inf.is.odysseus.BenchmarkGUI.view";

	private static String SCHEDULER_NAME = ISchedulerFactory.class.getName();
	private static String SCHEDULING_STRATEGY_NAME = ISchedulingFactory.class
			.getName();
	private static String BUFFER_PLACEMENT_NAME = IBufferPlacementStrategy.class
			.getName();

	private ViewWidget widget;

	private Menu menuScheduler = null;
	private Menu menuSchedulingStrategy = null;
	private Menu menuBufferPlacement = null;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.widget = new ViewWidget(parent, SWT.DEFAULT);

		this.menuScheduler = new Menu(widget.getTreeScheduler());
		MenuItem removeScheduler = new MenuItem(menuScheduler, SWT.PUSH);
		@SuppressWarnings("unused")
		MenuItem separator = new MenuItem(menuScheduler, SWT.SEPARATOR);
		removeScheduler.setText("remove");
		
		removeScheduler.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final TreeItem[] items = widget.getTreeScheduler().getSelection();
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						for (TreeItem treeItem : items) {
							final MenuItem item = new MenuItem(menuScheduler, SWT.PUSH);
							final String text = treeItem.getText();
							item.setText(text);
							treeItem.dispose();
							item.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(final SelectionEvent e) {
									Display.getDefault().asyncExec(new Runnable() {
										@Override
										public void run() {
											TreeItem treeItem = new TreeItem(widget.getTreeScheduler(), SWT.NONE);
											treeItem.setText(text);
											item.dispose();
										}
									});
								}
							});
						}						
					}
				});
				
			};
		});
		this.menuSchedulingStrategy = new Menu(widget
				.getTreeSchedulingStrategies());
		this.menuBufferPlacement = new Menu(widget.getTreeBufferPlacement());
		this.widget.getTreeScheduler().setMenu(this.menuScheduler);
		this.widget.getTreeSchedulingStrategies().setMenu(
				this.menuSchedulingStrategy);
		this.widget.getTreeBufferPlacement().setMenu(this.menuBufferPlacement);

		BundleContext bundleContext = Activator.getDefault().getBundle()
				.getBundleContext();
		initUi(bundleContext);
	}

	private void initUi(BundleContext bundleContext) {

		try {
			bundleContext.addServiceListener(this, "(|(objectclass="
					+ SCHEDULER_NAME + ")(objectclass="
					+ SCHEDULING_STRATEGY_NAME + ")(objectclass="
					+ BUFFER_PLACEMENT_NAME + "))");
		} catch (InvalidSyntaxException e1) {
			throw new RuntimeException(e1);
		}
		try {
			ServiceReference[] refs = bundleContext.getAllServiceReferences(
					ISchedulerFactory.class.getName(), null);
			if (refs != null) {
				for (ServiceReference ref : refs) {
					createMenu(ref, this.widget.getTreeScheduler(),
							this.menuScheduler);
				}
			}

			refs = bundleContext.getAllServiceReferences(
					IBufferPlacementStrategy.class.getName(), null);
			if (refs != null) {
				for (ServiceReference ref : refs) {
					createMenu(ref, this.widget.getTreeBufferPlacement(),
							this.menuBufferPlacement);
				}
			}

			refs = bundleContext.getAllServiceReferences(
					ISchedulingFactory.class.getName(), null);
			if (refs != null) {
				for (ServiceReference ref : refs) {
					createMenu(ref, this.widget.getTreeSchedulingStrategies(),
							this.menuSchedulingStrategy);
				}
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	private void createMenu(ServiceReference ref, Tree tree, Menu menu) {
		final String text = componentName(ref);
		createMenu(text, tree, menu);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		this.widget.setFocus();
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		final String className = ((String[]) event.getServiceReference()
				.getProperty("objectclass"))[0];
		String componentName = componentName(event.getServiceReference());
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			if (className.equals(SCHEDULER_NAME)) {
				createMenu(componentName, widget.getTreeScheduler(),
						this.menuScheduler);
			} else {
				if (className.equals(SCHEDULING_STRATEGY_NAME)) {
					createMenu(componentName, widget
							.getTreeSchedulingStrategies(),
							this.menuSchedulingStrategy);
				} else {
					createMenu(componentName, widget.getTreeBufferPlacement(),
							this.menuBufferPlacement);
				}
			}
			break;
		default:
			break;
		}
	}

	private String componentName(ServiceReference ref) {
		String componentName = (String) ref
				.getProperty("component.readableName");
		if (componentName == null) {
			componentName = (String) ref.getProperty("component.name");
		}
		return componentName;
	}

	private void createMenu(final String text, final Tree tree, final Menu menu) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				final MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText(text);
				item.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								TreeItem treeItem = new TreeItem(tree, SWT.NONE);
								treeItem.setText(text);
								item.dispose();
							}
						});
					}
				});
			}
		});
	}
}