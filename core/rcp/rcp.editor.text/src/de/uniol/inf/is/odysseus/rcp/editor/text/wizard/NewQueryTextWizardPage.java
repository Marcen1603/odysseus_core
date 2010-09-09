package de.uniol.inf.is.odysseus.rcp.editor.text.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.editor.text.IEditorTextConstants;

public class NewQueryTextWizardPage extends WizardPage {

	private static final int GRID_LAYOUT_COLUMNS = 2;
	
	private static final String INFORMATION_TEXT = "Note: The endling '." + IEditorTextConstants.QUERY_TEXT_EXTENSION +"' will be automatically added";
	private static final String NO_NAME_ERROR_TEXT = "A query name must be specified";
	private static final String TITLE_TEXT = "New Query Text";
	private static final String DESCRIPTION_TEXT = "Creates a new file for queries";
	private static final String NAME_EXISTS_TEXT = "Name already in use";
	
	private String queryName;
	private IContainer container;
	
	public NewQueryTextWizardPage(IContainer container) {
		super(TITLE_TEXT);
		
		setTitle(TITLE_TEXT);
		setDescription(DESCRIPTION_TEXT);
		setFileContainer(container);
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = GRID_LAYOUT_COLUMNS;
		composite.setLayout(gridLayout);
		
		createLabel( composite, "Name");
		final Text nameText = createText(composite, "");
		createHugeLabel( composite, INFORMATION_TEXT );
		
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if( nameText.getText() != null && nameText.getText().length() > 0 ) {
					if( !isNameAlreadyUsed(getFileContainer(), nameText.getText())) {
						setPageComplete(true);
						setErrorMessage(null);
						queryName = nameText.getText();
					} else {
						setErrorMessage(NAME_EXISTS_TEXT);
						setPageComplete(false);
					}
				} else {
					setErrorMessage(NO_NAME_ERROR_TEXT);
					setPageComplete(false);
				}
			}
		});
		
		setControl(composite);
		setPageComplete(false);
		setErrorMessage(NO_NAME_ERROR_TEXT);
	}
	
	public String getQueryName() {
		return queryName;
	}
	
	public String getFullQueryName() {
		return queryName + "." + IEditorTextConstants.QUERY_TEXT_EXTENSION;
	}
	
	public IContainer getFileContainer() {
		return container;
	}
	
	protected void setFileContainer( IContainer container ) {
		Assert.isNotNull(container, "container");
		
		this.container = container;
	}

	private static Label createLabel( Composite comp, String text ) {
		Label label = new Label( comp, SWT.NONE);
		label.setText(text);
		return label;
	}
	
	private static Label createHugeLabel( Composite comp, String text ) {
		Label label = createLabel(comp, text);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = GRID_LAYOUT_COLUMNS;
		label.setLayoutData(data);
		return label;
	}
	
	private static Text createText( Composite comp, String value ) {
		Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
		text.setText(value);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}
	
	private static boolean isNameAlreadyUsed( IContainer container, String name ) {
		return container.findMember(name + "." + IEditorTextConstants.QUERY_TEXT_EXTENSION) != null;
	}
}
