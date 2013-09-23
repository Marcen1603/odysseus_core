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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class ListParameterDialog extends TitleAreaDialog {

	private LogicalParameterInformation logicalParameterInfos;
	private List<Object> parameters;
	private ParameterPresentationFactory factory = new ParameterPresentationFactory();
	
	/**
	 * @param shell
	 * @param childs
	 */
	public ListParameterDialog(Shell shell, LogicalParameterInformation lpi, List<Object> values) {
		super(shell);
		this.logicalParameterInfos = lpi;
		this.parameters = values;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout(2, true);
		parent.setLayout(layout);
		
		for(Object e : this.parameters){
			IParameterPresentation presenter = factory.createPresentation(logicalParameterInfos);
			presenter.createWidget(parent, logicalParameterInfos, e);
		}
		return super.createDialogArea(parent);
	}

}
