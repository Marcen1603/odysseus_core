package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

public abstract class AbstractPictogramDialog<T extends Pictogram> extends TitleAreaDialog {

	private Text relevancePredicateText;
	private String relevancePredicate = "";
	private T pictogram;

	public AbstractPictogramDialog() {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	}

	public void init(T pg) {
		this.pictogram = pg;
		relevancePredicate = pg.getRelevancePredicate().toString();		
		loadValues(this.pictogram);
	}

	public abstract Control createWidgetAdrea(Composite parent);

	@Override
	protected Control createDialogArea(final Composite parent) {
		setMessage("Add a new pictogram");
		setTitle("New Pictogram");

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lblRelevancePredicate = new Label(container, SWT.NONE);
		lblRelevancePredicate.setText("Predicate to evaluate tuple or not");		

		relevancePredicateText = new Text(container, SWT.BORDER);
		relevancePredicateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		relevancePredicateText.setText(relevancePredicate);		

		createWidgetAdrea(container);
		return parent;
	}

	@Override
	protected void okPressed() {
		this.relevancePredicate = relevancePredicateText.getText();
		this.pictogram.setRelevancePredicate(relevancePredicate);
		saveValues(this.pictogram);
		super.okPressed();
	}

	public abstract void saveValues(T pg);

	public abstract void loadValues(T pg);

	public String getRelevancePredicate() {
		return relevancePredicate;
	}

	public void setRelevancePredicate(String relevancePredicate) {
		this.relevancePredicate = relevancePredicate;
	}

}
