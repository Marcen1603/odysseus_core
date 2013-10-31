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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.editing;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection.TextPosition;

/**
 * @author DGeesen
 * 
 */
public class ConnectionDirectEditManager extends DirectEditManager {
	
	private String initialValue;

	public ConnectionDirectEditManager(GraphicalEditPart source, @SuppressWarnings("rawtypes") Class editorType, CellEditorLocator locator, String value, TextPosition feature) {
		super(source, editorType, locator, feature);				
		this.initialValue = value;
	}

	@Override
	protected void initCellEditor() {		
		getCellEditor().setValue(initialValue);

	}

}
