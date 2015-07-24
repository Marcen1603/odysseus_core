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
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.Activator;

public class LoadBalancingView extends ViewPart {
	
	private static final String CONTROL_NOT_BOUND = "Load Balancing Control not (yet) bound.";
	
	private static final String STRATEGY_LABEL = "Strategy:";
	private static final String ALLOCATOR_LABEL = "Allocator:";
	
	private String selectedStrategy = "";
	private String selectedAllocator ="";
	
	private Combo strategyCombo;
	private Combo allocatorCombo;
	
	@Override
	public void createPartControl(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		if(controller==null) {
			createLabel(parent,CONTROL_NOT_BOUND);
		}
		else {
			createChooserComposite(parent);
			
			
		}
		
		
	}
	
	private void createChooserComposite(Composite parent) {

		ILoadBalancingController controller = Activator.getLoadBalancingController();
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(2, false));
		
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
	
}
