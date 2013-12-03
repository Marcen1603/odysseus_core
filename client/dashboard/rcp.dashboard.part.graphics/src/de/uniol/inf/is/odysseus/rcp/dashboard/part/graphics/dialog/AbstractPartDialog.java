/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart;

/**
 * @author DGeesen
 * 
 */
public abstract class AbstractPartDialog<T extends AbstractPart> extends TitleAreaDialog {

	private Text relevancePredicateText;
	private String relevancePredicate = "";
	private T part;

	private List<String> roots = new ArrayList<String>();
	private String selectedRoot;
	private Combo combo;

	public AbstractPartDialog() {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	}

	public void init(T part) {
		this.part = part;
		Collection<IPhysicalOperator> roots = part.getRoots();
		for (IPhysicalOperator op : roots) {
			this.roots.add(op.getName());
		}
		Collections.sort(this.roots);
		selectedRoot = part.getSelectedRootName();
		if (selectedRoot == null) {
			selectedRoot = roots.iterator().next().getName();
		}
		relevancePredicate = part.getRelevancePredicate().toString();		
		loadValues(this.part);
	}

	public abstract Control createWidgetAdrea(Composite parent);

	@Override
	protected Composite createDialogArea(final Composite parent) {
		setMessage("Here you can configure your graphical part");
		setTitle("Configure Part");

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

		createWidgetAdrea(container);
		return container;
	}

	@Override
	protected void okPressed() {
		this.relevancePredicate = relevancePredicateText.getText();
		this.part.setRelevancePredicate(relevancePredicate);		
		this.part.setSelectedRootName(combo.getText());
		saveValues(this.part);
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
		return this.part.getGraphicsLayer().getProject();
	}
	
	protected T getPart(){
		return this.part;
	}

}
