package de.uniol.inf.is.odysseus.rcp.viewer.views;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.IOperatorDetailProvider;
import de.uniol.inf.is.odysseus.rcp.IOperatorGeneralDetailProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class OperatorDetailView extends ViewPart implements ISelectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorDetailView.class);
	
	private static final String UNKNOWN_PROVIDER_TITLE = "<Not set>";
	
	private final List<IOperatorDetailProvider<?>> activeProviders = Lists.newArrayList();
	private final List<IOperatorGeneralDetailProvider> activeGeneralProviders = Lists.newArrayList();	
	private final Map<IOperatorDetailProvider<?>, Composite> compositeMap = Maps.newHashMap();
	private final Map<IOperatorGeneralDetailProvider, Composite> generalCompositeMap = Maps.newHashMap();
	
	private Composite parent;
	private IPhysicalOperator selectedOperator;
	private TabFolder activeTabFolder;
	private String selectedTab;
	
	@Override
	public void createPartControl(Composite parent) {
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		
		this.parent = parent;
		this.parent.setLayout(new GridLayout());
	}

	@Override
	public void setFocus() {
		this.parent.setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this) {
			return;
		}

		final Optional<IPhysicalOperator> optSelectedOperator = determineSelectedOperatorClass(selection);
		if (optSelectedOperator.isPresent()) {
			IPhysicalOperator selectedOperator = optSelectedOperator.get();
			
			if( this.selectedOperator != selectedOperator ) {
				deleteProviders();
				createProviders(selectedOperator);
			}
			
		} else {
			deleteProviders();
		}
	}

	private <T extends IPhysicalOperator> void createProviders(T operator) {
		Preconditions.checkState(activeProviders.isEmpty(), "Creating providers without disposing old ones first!");

		@SuppressWarnings("unchecked")
		Class<T> operatorClass = (Class<T>) operator.getClass();
		List<Class<? extends IOperatorDetailProvider<T>>> providers = OperatorDetailProviderRegistry.getInstance().getProviders(operatorClass);
		List<Class<? extends IOperatorGeneralDetailProvider>> generalProviders = OperatorDetailProviderRegistry.getInstance().getGeneralProviders();

		List<? extends IOperatorDetailProvider<T>> instances = createProviderInstances(providers);
		List<? extends IOperatorGeneralDetailProvider> generalInstances = createGeneralProviderInstances(generalProviders);

		activeTabFolder = new TabFolder(this.parent, SWT.BORDER);
		activeTabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		activeTabFolder.setLayout(new GridLayout());
		
		int tabToSelect = -1;

		for (IOperatorGeneralDetailProvider generalInstance : generalInstances) {
			TabItem generalTabItem = new TabItem(activeTabFolder, SWT.NULL);
			generalTabItem.setText(determineTitle(generalInstance));
			
			if( selectedTab != null && generalTabItem.getText().equals(selectedTab)) {
				tabToSelect = activeTabFolder.indexOf(generalTabItem);
			}

			Composite generalProviderComposite = new Composite(activeTabFolder, SWT.NONE);
			generalProviderComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			generalProviderComposite.setLayout(new FillLayout());

			try {
				generalInstance.createPartControl(generalProviderComposite, operator);
				activeGeneralProviders.add(generalInstance);
			} catch (Throwable t) {
				LOG.error("Exception during creating part control in operator general detail provider", t);
			}
			
			generalCompositeMap.put(generalInstance, generalProviderComposite);
			generalTabItem.setControl(generalProviderComposite);
		}

		for (IOperatorDetailProvider<T> instance : instances) {

			TabItem tabItem = new TabItem(activeTabFolder, SWT.NULL);
			tabItem.setText(determineTitle(instance));
			
			if( selectedTab != null && tabItem.getText().equals(selectedTab)) {
				tabToSelect = activeTabFolder.indexOf(tabItem);
			}

			Composite providerComposite = new Composite(activeTabFolder, SWT.NONE);
			providerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			providerComposite.setLayout(new FillLayout());
			try {
				instance.createPartControl(providerComposite, operator);
				activeProviders.add(instance);
			} catch (Throwable t) {
				LOG.error("Exception during creating part control in operator detail provider", t);
			}
			tabItem.setControl(providerComposite);

			compositeMap.put(instance, providerComposite);
		}

		parent.layout();
		if( tabToSelect != -1 ) {
			activeTabFolder.setSelection(tabToSelect);
		}
	}

	private void deleteProviders() {
		if( !activeProviders.isEmpty()) {
			for( IOperatorDetailProvider<?> activeProvider : activeProviders ) {
				activeProvider.dispose();
				
				Composite comp = compositeMap.get(activeProvider);
				comp.dispose();
				
				compositeMap.remove(activeProvider);
			}
			
			activeProviders.clear();
		}
		
		if( !activeGeneralProviders.isEmpty()) {
			for( IOperatorGeneralDetailProvider activeGeneralProvider : activeGeneralProviders ) {
				activeGeneralProvider.dispose();
				
				Composite comp = generalCompositeMap.get(activeGeneralProvider);
				comp.dispose();
				
				compositeMap.remove(activeGeneralProvider);
			}
			
			activeGeneralProviders.clear();
		}
		
		if( activeTabFolder != null ) {
			int selectionIndex = activeTabFolder.getSelectionIndex();
			selectedTab = activeTabFolder.getItem(selectionIndex).getText();
			
			activeTabFolder.dispose();
			activeTabFolder = null;
		}
	}

	private static Optional<IPhysicalOperator> determineSelectedOperatorClass(ISelection selection) {
		if( !selection.isEmpty() && selection instanceof IStructuredSelection ) {
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			
			Object selectedObject = structSelection.getFirstElement();
			if( selectedObject instanceof IOdysseusNodeView ) {
				IOdysseusNodeView nodeView = (IOdysseusNodeView)selectedObject;
				if( nodeView.getModelNode() != null && nodeView.getModelNode().getContent() != null ) {
					return Optional.of(nodeView.getModelNode().getContent());
				}
			}
		}
		return Optional.absent();
	}
	
	private static <T extends IPhysicalOperator> List<? extends IOperatorDetailProvider<T>> createProviderInstances(List<Class<? extends IOperatorDetailProvider<T>>> providers) {
		List<IOperatorDetailProvider<T>> instances = Lists.newArrayList();
		for (Class<? extends IOperatorDetailProvider<T>> provider : providers) {
			try {
				instances.add(provider.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Could not create operator detail provider", e);
			}
		}
		return instances;
	}
	
	private static List<? extends IOperatorGeneralDetailProvider> createGeneralProviderInstances(List<Class<? extends IOperatorGeneralDetailProvider>> generalProviders) {
		List<IOperatorGeneralDetailProvider> instances = Lists.newArrayList();
		for (Class<? extends IOperatorGeneralDetailProvider> generalProvider : generalProviders) {
			try {
				instances.add(generalProvider.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Could not create operator detail provider", e);
			}
		}
		return instances;
	}
	
	private static String determineTitle(IOperatorDetailProvider<?> provider) {
		try {
			String title = provider.getTitle();
			return Strings.isNullOrEmpty(title) ? UNKNOWN_PROVIDER_TITLE : title;
		} catch( Throwable t ) {
			LOG.error("Could not get title from OperatorDetailProvider {}", provider, t);
			return UNKNOWN_PROVIDER_TITLE;
		}
	}
	
	private static String determineTitle(IOperatorGeneralDetailProvider provider) {
		try {
			String title = provider.getTitle();
			return Strings.isNullOrEmpty(title) ? UNKNOWN_PROVIDER_TITLE : title;
		} catch( Throwable t ) {
			LOG.error("Could not get title from OperatorDetailProvider {}", provider, t);
			return UNKNOWN_PROVIDER_TITLE;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		super.dispose();
	}
}
