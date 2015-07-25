package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.views;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingControllerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLockListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.Activator;

public class LoadBalancingView extends ViewPart implements ILoadBalancingControllerListener, ILoadBalancingLockListener {
	
	private static final String CONTROL_NOT_BOUND = "Load Balancing Control not (yet) bound.";
	
	private static final String STRATEGY_LABEL = "Strategy:";
	private static final String ALLOCATOR_LABEL = "Allocator:";
	private static final String STATUS_DESCRIPTION_LABEL = "Current Status:";
	private static final String STATUS_LOCK = "Lock Status:";
	
	private String selectedStrategy = "";
	private String selectedAllocator ="";
	
	private Combo strategyCombo;
	private Combo allocatorCombo;
	private Label statusLabel;
	private Label lockLabel;
	private Button unlockButton;
	
	@Override
	public void createPartControl(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		ILoadBalancingLock lock = Activator.getLock();
		
		if(controller==null) {
			createLabel(parent,CONTROL_NOT_BOUND);
		}
		else {
			createChooserComposite(parent);
			controller.addControllerListener(this);
		}

		if(lock!=null) {
			lock.addListener(this);
		}
		
	}
	
	private void createChooserComposite(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		ILoadBalancingLock lock = Activator.getLock();
		
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(2, false));
		
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
		
		
		createLabel(rootComposite,STRATEGY_LABEL);
		strategyCombo = new Combo(rootComposite,SWT.READ_ONLY);
		strategyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedStrategy = ((Combo)e.getSource()).getText();
			}
		});
		
		createLabel(rootComposite,ALLOCATOR_LABEL);
		allocatorCombo = new Combo(rootComposite, SWT.READ_ONLY);
		allocatorCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAllocator = ((Combo)e.getSource()).getText();
			}
		});
		
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
		
		
		populateStrategyList(controller);
		populateAllocatorList(controller);
	}
	
	
	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
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
	
	
	
}
