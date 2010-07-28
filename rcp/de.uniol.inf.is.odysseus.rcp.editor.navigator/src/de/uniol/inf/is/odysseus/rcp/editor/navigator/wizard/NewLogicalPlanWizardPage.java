package de.uniol.inf.is.odysseus.rcp.editor.navigator.wizard;

import java.util.Collection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewLogicalPlanWizardPage extends WizardPage {

	private Text nameText;
	private Collection<String> usedNames;
	
	protected NewLogicalPlanWizardPage(Collection<String> usedNames) {
		super("Logical Plan");
		
		setTitle("New logical plan");
		setDescription("Creating an empty logical plan");
		setMessage(null);
		setControl(nameText);
		
		this.usedNames = usedNames;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("Name");
		
		nameText = new Text(composite, SWT.SINGLE);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if( nameText.getText().length() == 0 ) {
					setMessage("Name must be specified");
					setPageComplete(false);
				} else if( usedNames != null && usedNames.contains(nameText.getText())) {
					setMessage("Resource with specified name already exists");
					setPageComplete(false);
				} else {
					setMessage(null);
					setPageComplete(true);
				}
			}
			
		});
		
		setControl(composite);
		setVisible(true);
	}
	
	public String getName() {
		return nameText.getText();
	}

}
