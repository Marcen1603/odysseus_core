package de.uniol.inf.is.odysseus.rcp.login.view;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;

public class LoginWindow extends TitleAreaDialog implements ILoginWindow {

	private static final Logger LOG = LoggerFactory.getLogger(LoginWindow.class);

	private final Map<ILoginContribution, Integer> tabIndexMap = Maps.newHashMap();
	private Collection<ILoginContribution> contributions;

	private TabFolder tabFolder;
	private Button showWindowNotAgainCheck;

	private boolean isNotShowAgain;

	public LoginWindow(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void show(Collection<ILoginContribution> contributions, boolean showWindowSetting) {
		// Preconditions.checkNotNull(contributions, "Contributions collection must not be null!");
		// Preconditions.checkArgument(!contributions.isEmpty(), "Collection of contributions to show must not be empty!");
		
		LOG.debug("Showing login window with {} login contributions. ShowWindowSetting = {}", contributions.size(), showWindowSetting);

		this.contributions = Lists.newArrayList(contributions);
		this.isNotShowAgain = !showWindowSetting;

		setBlockOnOpen(true);
		open();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		getShell().setText("Odysseus Login");
		setTitle("Odysseus Login Information");
		setMessage("Please enter the needed information to log in Odysseus");
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		rootComposite.setLayout(new GridLayout(1, true));

		tabFolder = new TabFolder(rootComposite, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout(1, true));

		int currentTabIndex = 0;
		for (ILoginContribution contribution : contributions) {
			LOG.debug("Creating tab area for login contribution {} with tabIndex = {}", contribution, currentTabIndex);
			
			Composite contributionComposite = new Composite(tabFolder, SWT.NONE);
			contributionComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			contributionComposite.setLayout(new GridLayout(1, true));

			try {
				contribution.createPartControl(contributionComposite, this);

				TabItem contributionTab = new TabItem(tabFolder, SWT.NULL);
				contributionTab.setText(contribution.getTitle());

				contributionTab.setControl(contributionComposite);
				tabIndexMap.put(contribution, currentTabIndex++);

			} catch (Throwable t) {
				LOG.error("Could not create part control of login contribution {}", contribution, t);
			}
		}
		tabFolder.setSelection(0);

		rootComposite.layout();
		return rootComposite;
	}

	@Override
	public void show(ILoginContribution contributionToShow) {
		// Preconditions.checkNotNull(contributionToShow, "Login contribution to show in login window must not be null!");

		Integer index = tabIndexMap.get(contributionToShow);
		if (index != null) {
			tabFolder.setSelection(index);
		}
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite barComposite = new Composite(parent, SWT.NONE);
		barComposite.setLayout(new GridLayout(2, false));

		showWindowNotAgainCheck = new Button(barComposite, SWT.CHECK);
		showWindowNotAgainCheck.setText("Do not show this window again");
		showWindowNotAgainCheck.setSelection(isNotShowAgain);
		showWindowNotAgainCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotShowAgain = showWindowNotAgainCheck.getSelection();
			}
		});

		Control buttonBar = super.createButtonBar(barComposite);
		buttonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		refreshOKButton();
		return buttonBar;
	}

	@Override
	public void changed() {
		refreshOKButton();
	}

	private void refreshOKButton() {
		getButton(IDialogConstants.OK_ID).setEnabled(areAllContributionsValid());
	}

	private boolean areAllContributionsValid() {
		for( ILoginContribution contribution : contributions ) {
			if( !contribution.isValid() ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean close() {
		disposeContributions();
		return super.close();
	}

	private void disposeContributions() {
		for (ILoginContribution contribution : contributions) {
			contribution.dispose();
		}
		contributions.clear();
		tabIndexMap.clear();
	}

	@Override
	public boolean isShowAgain() {
		return !isNotShowAgain;
	}

	@Override
	public void setInformationMessage(String message) {
		setMessage(message, IMessageProvider.INFORMATION);
	}

	@Override
	public void setWarningMessage(String message) {
		setMessage(message, IMessageProvider.WARNING);
	}

	@Override
	public boolean isOK() {
		return getReturnCode() == Window.OK;
	}

	@Override
	public boolean isCancel() {
		return getReturnCode() == Window.CANCEL;
	}
}
