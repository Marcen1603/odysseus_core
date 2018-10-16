package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;

public class DashboardPartConfigWindow extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartConfigWindow.class);
	
	private static final String DEFAULT_MESSAGE = "Configure the individual settings of this dashboard part";
	private static final String WINDOW_TITLE = "Configure Dashboard Part";
	private static final String DISPLAY_TITLE = "Dashboard Part settings";
	
	private String selectedSinks;
	private boolean sinksSynced;
	private Button okButton;

	private IDashboardPart dashboardPart; 
	private IDashboardPartConfigurer<IDashboardPart> dashboardPartConfigurer;
	private DashboardPartController controller;

	public DashboardPartConfigWindow(Shell parentShell, IDashboardPart dashboardPart, DashboardPartController controller) {
		super(parentShell);
		// Preconditions.checkNotNull(dashboardPart, "DashboardPart must not be null!");
		// Preconditions.checkNotNull(controller, "DashboardPartController must not be null!");
		
		this.dashboardPart = dashboardPart;
		this.selectedSinks = dashboardPart.getSinkNames();
		this.sinksSynced = dashboardPart.isSinkSynchronized();
		this.controller = controller;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(DISPLAY_TITLE);
		setMessage(DEFAULT_MESSAGE);
		getShell().setText(WINDOW_TITLE);
		return contents;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite configComposite = new Composite(parent, SWT.NONE);
		configComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		configComposite.setLayout(new GridLayout(1, false));
		
		Optional<String> optRegName = DashboardPartRegistry.getRegistrationName(dashboardPart.getClass());
		if( optRegName.isPresent() ) {
			try {
				String regName = optRegName.get();
				dashboardPartConfigurer = (IDashboardPartConfigurer<IDashboardPart>) DashboardPartRegistry.createDashboardPartConfigurer(regName);
				dashboardPartConfigurer.init(dashboardPart, controller.getQueryRoots());
				
				dashboardPartConfigurer.createPartControl(configComposite);
			} catch( InstantiationException ex ) {
				LOG.error("Could not create dashboard part configurer");
			}
		}
		
		createSinkSelectControls(parent);
		createSyncSinksControls(parent);
		
		configComposite.pack();
		return configComposite;
	}

	private void createSyncSinksControls(Composite parent) {
		Composite syncSinkComposite = new Composite(parent, SWT.NONE);
		syncSinkComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		syncSinkComposite.setLayout(new GridLayout(1, false));
		
		final Button syncCheckBox = DashboardPartUtil.createCheckBox(syncSinkComposite, "Synchronize sinks", sinksSynced );
		syncCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sinksSynced = syncCheckBox.getSelection();
			}
		});
	}

	private void createSinkSelectControls(Composite parent) {
		Composite sinkNameComposite = new Composite(parent, SWT.NONE);
		sinkNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sinkNameComposite.setLayout(new GridLayout(3, false));
		
		DashboardPartUtil.createLabel(sinkNameComposite, "Name of sink");
		Text sinkNameText = createSinkSelectText(sinkNameComposite);
		createSinkSelectResetButton(sinkNameComposite, sinkNameText);
	}

	private static void createSinkSelectResetButton(Composite sinkNameComposite, final Text sinkNameText) {
		Button cleanTextButton = new Button(sinkNameComposite, SWT.PUSH);
		cleanTextButton.setImage(DashboardPlugIn.getImageManager().get("resetImage"));
		cleanTextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sinkNameText.setText("");
			}
		});
	}

	private Text createSinkSelectText(Composite sinkNameComposite) {
		final Text sinkNameText = new Text(sinkNameComposite, SWT.BORDER);
		sinkNameText.setText(selectedSinks != null ? selectedSinks : "");
		sinkNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sinkNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedSinks = sinkNameText.getText();
			}
		});
		return sinkNameText;
	}

	public String getSelectedSinkName() {
		return selectedSinks != null ? selectedSinks : "";
	}
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button contextMapButton = new Button(parent, SWT.PUSH);
		contextMapButton.setText("Context");
		setButtonLayoutData(contextMapButton);
		contextMapButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ContextMapEditorWindow wnd = new ContextMapEditorWindow(parent.getShell(), dashboardPart, dashboardPart.getClass().getSimpleName());
				wnd.open();
			}
		});

		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				dispose();
				close();
			}
		});
	}

	private void dispose() {
		if( dashboardPartConfigurer != null ) {
			dashboardPartConfigurer.dispose();
		}
	}

	public boolean isSinkSynced() {
		return sinksSynced;
	}
}
