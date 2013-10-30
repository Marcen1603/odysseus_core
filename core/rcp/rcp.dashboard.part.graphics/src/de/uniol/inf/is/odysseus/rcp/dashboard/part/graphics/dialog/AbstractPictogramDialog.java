package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

public abstract class AbstractPictogramDialog<T extends Pictogram> extends TitleAreaDialog {

	private Text relevancePredicateText;
	private String relevancePredicate = "";

	private Text topTextText;
	private String topText = "";

	private Text bottomTextText;
	private String bottomText = "";

	private T pictogram;

	private List<String> roots = new ArrayList<String>();
	private String selectedRoot;
	private Combo combo;

	public AbstractPictogramDialog() {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

	}

	public void init(T pg) {
		this.pictogram = pg;
		Collection<IPhysicalOperator> roots = pg.getRoots();
		for (IPhysicalOperator op : roots) {
			this.roots.add(op.getName());
		}
		Collections.sort(this.roots);
		selectedRoot = pg.getSelectedRootName();
		if (selectedRoot == null) {
			selectedRoot = roots.iterator().next().getName();
		}
		relevancePredicate = pg.getRelevancePredicate().toString();
		bottomText = pg.getTextBottom();
		topText = pg.getTextTop();
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

		Label lblSinkCoice = new Label(container, SWT.NONE);
		lblSinkCoice.setText("The sink which provides the data");

		combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] names = roots.toArray(new String[0]);
		combo.setItems(names);
		int select = 0;
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItem(i).equals(selectedRoot)) {
				select = i;
				break;
			}
		}
		combo.select(select);
		Label lblRelevancePredicate = new Label(container, SWT.NONE);
		lblRelevancePredicate.setText("Predicate to evaluate tuple or not");

		relevancePredicateText = new Text(container, SWT.BORDER);
		relevancePredicateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		relevancePredicateText.setText(relevancePredicate);

		Label lblTextTop = new Label(container, SWT.NONE);
		lblTextTop.setText("Text at the top of the figure");

		topTextText = new Text(container, SWT.BORDER);
		topTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		topTextText.setText(topText);

		Label lblTextBottom = new Label(container, SWT.NONE);
		lblTextBottom.setText("Text at the bottom of the figure");

		bottomTextText = new Text(container, SWT.BORDER);
		bottomTextText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		bottomTextText.setText(bottomText);

		createWidgetAdrea(container);
		return parent;
	}

	@Override
	protected void okPressed() {
		this.relevancePredicate = relevancePredicateText.getText();
		this.pictogram.setRelevancePredicate(relevancePredicate);
		this.pictogram.setTextBottom(bottomTextText.getText());
		this.pictogram.setTextTop(topTextText.getText());
		this.pictogram.setSelectedRootName(combo.getText());
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

	protected IProject getProject() {
		return this.pictogram.getGraphicsLayer().getProject();
	}

}
