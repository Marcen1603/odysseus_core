package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.IExcludedQueryRegistryListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingControllerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLockListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.Activator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;

public class LoadBalancingView extends ViewPart implements ILoadBalancingControllerListener, ILoadBalancingLockListener, IExcludedQueryRegistryListener {
	

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingView.class);
	
	private static final String CONTROL_NOT_BOUND = "Load Balancing Control not (yet) bound.";
	
	private static final String STRATEGY_LABEL = "Strategy:";
	private static final String ALLOCATOR_LABEL = "Allocator:";
	private static final String STATUS_DESCRIPTION_LABEL = "Current Status:";
	private static final String STATUS_LOCK = "Lock Status:";
	private static final String EXCLUDED_LABEL = "Excluded Queries:";
	
	private String selectedStrategy = "";
	private String selectedAllocator ="";
	
	private Combo strategyCombo;
	private Combo allocatorCombo;
	private Label statusLabel;
	private Label lockLabel;
	private Button unlockButton;
	private Label excludedQueries;
	
	
	
	@Override
	public void createPartControl(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		ILoadBalancingLock lock = Activator.getLock();
		IExcludedQueriesRegistry excludedQueryRegistry = Activator.getExcludedQueryRegistry();
		
		if(controller==null) {
			createLabel(parent,CONTROL_NOT_BOUND);
			return;
		}
		else {
			createMainView(parent);
			controller.addControllerListener(this);
		}

		if(lock!=null) {
			lock.addListener(this);
		}
		
		if(excludedQueryRegistry!=null) {
			excludedQueryRegistry.addListener(this);
		}
		else {
			LOG.error("Query Registry not bound!");
		}
		
	}
	
	private void createMainView(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		ILoadBalancingLock lock = Activator.getLock();
		
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(2, true));
		rootComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		createLoadBalancingStatusAndLockStatusView(controller, lock,
				rootComposite);
		
		createStrategyAndAllocatorChooser(rootComposite);
		
		createExcludedQueriesListView(rootComposite);
		
		createLeftButtonComposite(rootComposite);

		createRightButtonComposite(lock, rootComposite);
				
		populateStrategyList(controller);
		populateAllocatorList(controller);
	}

	private void createExcludedQueriesListView(Composite rootComposite) {
		createLabel(rootComposite,EXCLUDED_LABEL);
		excludedQueries = createLabel(rootComposite,"");
		excludedQueries.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
	}

	private void createLeftButtonComposite(Composite rootComposite) {
		Composite buttonComposite = new Composite(rootComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(3, true));

		Button startButton = new Button(buttonComposite, SWT.NONE);
		Image startImage = Activator.getImageDescriptor("icons/start.png").createImage();
		startButton.setImage(startImage);
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILoadBalancingController controller = Activator.getLoadBalancingController();

				if (controller != null){
					controller.setLoadBalancingAllocator(selectedAllocator);
					controller.setLoadBalancingStrategy(selectedStrategy);
					controller.startLoadBalancing();
				}
			}
		});
		
		Button stopButton = new Button(buttonComposite, SWT.NONE);
		Image stopImage = Activator.getImageDescriptor("icons/stop.png").createImage();
		stopButton.setImage(stopImage);
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILoadBalancingController controller = Activator.getLoadBalancingController();

				if (controller != null){
					controller.stopLoadBalancing();
				}
			}
		});
		

		Button refreshButton = new Button(buttonComposite,SWT.None);
		Image refreshImage = Activator.getImageDescriptor("icons/refresh.png").createImage();
		refreshButton.setImage(refreshImage);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILoadBalancingController controller = Activator.getLoadBalancingController();

				if (controller != null){
					populateStrategyList(controller);
					populateAllocatorList(controller);
				}
			}
		});
	}

	private void createRightButtonComposite(ILoadBalancingLock lock,
			Composite rootComposite) {
		Composite rightButtonComposite = new Composite(rootComposite, SWT.NONE);
		rightButtonComposite.setLayout(new GridLayout(3, true));

		unlockButton = new Button(rightButtonComposite, SWT.NONE);
		Image unlockImages = Activator.getImageDescriptor("icons/lock_break.png").createImage();
		unlockButton.setImage(unlockImages);
		unlockButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILoadBalancingLock lock = Activator.getLock();

				if (lock != null){
					lock.forceUnlock();
				}
			}
		});
		if(lock==null || !lock.isLocked()) {
			unlockButton.setEnabled(false);
		}
		
		

		Button forceButton = new Button(rightButtonComposite, SWT.NONE);
		Image forceImage = Activator.getImageDescriptor("icons/force.png").createImage();
		forceButton.setImage(forceImage);
		forceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new Thread()
				{
				    public void run() {
				    	ILoadBalancingController controller = Activator.getLoadBalancingController();

						if (controller != null){
							controller.forceLoadBalancing();;
						}
				    }
				}.start();

			}
		});
	}

	private void createStrategyAndAllocatorChooser(Composite rootComposite) {
		createLabel(rootComposite,STRATEGY_LABEL);
		strategyCombo = new Combo(rootComposite,SWT.READ_ONLY);
		strategyCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		strategyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedStrategy = ((Combo)e.getSource()).getText();
			}
		});
		
		createLabel(rootComposite,ALLOCATOR_LABEL);
		allocatorCombo = new Combo(rootComposite, SWT.READ_ONLY);
		allocatorCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		allocatorCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAllocator = ((Combo)e.getSource()).getText();
			}
		});
	}

	private void createLoadBalancingStatusAndLockStatusView(
			ILoadBalancingController controller, ILoadBalancingLock lock,
			Composite rootComposite) {
		createLabel(rootComposite,STATUS_DESCRIPTION_LABEL);
		
		statusLabel = createLabel(rootComposite,STATUS_DESCRIPTION_LABEL);
		if(controller!=null) {
			if(controller.isLoadBalancingRunning()) {
				statusLabel.setText("Active");
			}
			else {
				statusLabel.setText("Inactive");
			}
		}
		
		createLabel(rootComposite,STATUS_LOCK);
		lockLabel = createLabel(rootComposite,"unbound");
		
		if(lock!=null) {
			if(lock.isLocked()) {
				lockLabel.setText("locked");
			}
			else {
				lockLabel.setText("unlocked");
			}
		}
	}
	
	
	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.LEFT);
		label.setText(string);
		return label;
	}
	
	@Override
    public void setFocus() {
		 
	}
	
	@Override
	public void dispose() {
		ILoadBalancingController controller = Activator.getLoadBalancingController();
		if(controller!=null) {
			controller.removeControllerListener(this);
		}
		ILoadBalancingLock lock = Activator.getLock();
		if(lock!=null) {
			lock.removeListener(this);
		}
		

		IExcludedQueriesRegistry excludedQueryRegistry = Activator.getExcludedQueryRegistry();
		if(excludedQueryRegistry!=null) {
			excludedQueryRegistry.addListener(this);
		}
		
	}
	
	private void populateStrategyList(ILoadBalancingController controller) {
		Set<String> strategies = controller.getAvailableStrategies();
		String[] stringArray = strategies.toArray(new String[strategies.size()]);
		strategyCombo.setItems(stringArray);
	}
	
	private void populateAllocatorList(ILoadBalancingController controller) {
		Set<String> allocators = controller.getAvailableAllocators();
		String[] stringArray = allocators.toArray(new String[allocators.size()]);
		allocatorCombo.setItems(stringArray);
	}

	
	
	@Override
	public synchronized void notifyLoadBalancingStatusChanged(boolean isRunning) {
		
		if(isRunning) {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	statusLabel.setText("Active");
			    }
			});
		}
		else {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	statusLabel.setText("Inactive");
			    }
			});
		}
		
	}

	@Override
	public synchronized void notifyLockStatusChanged(boolean status) {
		if(status) {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	lockLabel.setText("locked");
					unlockButton.setEnabled(true);
			    }
			});
			
		}
		else {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	lockLabel.setText("unlocked");
					unlockButton.setEnabled(false);
			    }
			});
			
		}		
	}

	@Override
	public void notifyExcludedQueriesChanged(List<Integer> queryIDs) {
		final String queryIDsAsString = integerListToString(queryIDs);
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	excludedQueries.setText(queryIDsAsString);
		    }
		});
		
	}
	
	private synchronized String integerListToString(List<Integer> integers) {
		List<Integer> integersCopy = new ArrayList<Integer>(integers);
		StringBuilder sb = new StringBuilder();
		Iterator<Integer> iter = integersCopy.iterator();
		while(iter.hasNext()) {
			sb.append(iter.next().toString());
			if(iter.hasNext())
				sb.append(',');
		}
		return sb.toString();
	}
	
	
	
	
}
