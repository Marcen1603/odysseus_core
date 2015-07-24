package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.views;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
		Combo strategyCombo = new Combo(rootComposite,SWT.READ_ONLY);
		strategyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedStrategy = ((Combo)e.getSource()).getText();
			}
		});
		
		createLabel(rootComposite,ALLOCATOR_LABEL);
		Combo allocatorCombo = new Combo(rootComposite, SWT.READ_ONLY);
		allocatorCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAllocator = ((Combo)e.getSource()).getText();
			}
		});
		
		

		Button startButton = new Button(rootComposite, SWT.NONE);
		startButton.setText("Start");
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
		
		Button stopButton = new Button(rootComposite, SWT.NONE);
		stopButton.setText("Stop");
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ILoadBalancingController controller = Activator.getLoadBalancingController();

				if (controller != null){
					controller.stopLoadBalancing();
				}
			}
		});
		

		populateStrategyList(strategyCombo, controller);
		populateAllocatorList(allocatorCombo, controller);
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
	
	private void populateStrategyList(Combo combo,ILoadBalancingController controller) {
		Set<String> strategies = controller.getAvailableStrategies();
		String[] stringArray = strategies.toArray(new String[strategies.size()]);
		combo.setItems(stringArray);
	}
	
	private void populateAllocatorList(Combo combo,ILoadBalancingController controller) {
		Set<String> allocators = controller.getAvailableAllocators();
		String[] stringArray = allocators.toArray(new String[allocators.size()]);
		combo.setItems(stringArray);
	}
	
}
