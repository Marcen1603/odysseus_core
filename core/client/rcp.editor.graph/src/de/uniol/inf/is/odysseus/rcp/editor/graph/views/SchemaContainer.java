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
package de.uniol.inf.is.odysseus.rcp.editor.graph.views;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author DGeesen
 * 
 */
public class SchemaContainer {

	private ScrolledComposite schemaScroller;
	private Composite schemaContainer;

	public void updateSchemas(Map<Integer, SDFSchema> schemas){
		for (Control c : schemaContainer.getChildren()) {
			c.dispose();
		}
		schemaContainer.layout(true);

		for (Entry<Integer, SDFSchema> input : schemas.entrySet()) {
			SDFSchema schema = input.getValue();
			Group group = new Group(schemaContainer, SWT.None);			
			if (schema == null) {
				group.setLayout(new GridLayout(1, true));				
				group.setText("Unkown (Port " + input.getKey() + ")");	
				Label name = new Label(group, SWT.NONE);
				name.setText("Schema could not be resolved");
			} else {
				group.setText(schema.getURI() + " (Port " + input.getKey() + ")");			
				group.setLayout(new GridLayout(2, true));
				for (SDFAttribute attribute : schema) {
					Label name = new Label(group, SWT.NONE);
					name.setText(attribute.getAttributeName());
					Label type = new Label(group, SWT.NONE);
					type.setText(attribute.getDatatype().getQualName());
				}
			}
		}
		schemaScroller.setMinHeight(schemaContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		schemaContainer.layout();
		schemaScroller.layout(true);
	}
	
	
	public void createContainer(CTabFolder tabFolder, String title) {
		CTabItem tabSchema = new CTabItem(tabFolder, SWT.NONE);
		tabSchema.setText(title);
		schemaScroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL);
		schemaScroller.setLayoutData(new FillLayout());
		schemaContainer = new Composite(schemaScroller, SWT.NONE);
		schemaContainer.setLayout(new FillLayout());
		schemaScroller.setContent(schemaContainer);
		schemaScroller.setExpandVertical(true);
		schemaScroller.setExpandHorizontal(true);
		schemaScroller.setMinHeight(schemaContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		tabSchema.setControl(schemaScroller);
	}
}
