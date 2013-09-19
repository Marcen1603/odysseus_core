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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.ConnectionFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.OperatorNodeFactory;

/**
 * @author DGeesen
 * 
 */
public class GraphPalette extends PaletteRoot {
	public GraphPalette() {
		PaletteGroup group = new PaletteGroup("Graph Controls");
		SelectionToolEntry entry = new SelectionToolEntry();
		group.add(entry);
		setDefaultEntry(entry);
		group.add(new PaletteSeparator());
		group.add(new ConnectionCreationToolEntry("Connection", "Creates a new connection.", new ConnectionFactory(), null, null));
		//group.add(new CreationToolEntry("Node", "Creates a new node.", new OperatorNodeFactory(), null, null));
		group.add(new PaletteSeparator());
		
		List<LogicalOperatorInformation> ops = Activator.getDefault().getExecutor().getOperatorInformations(Activator.getDefault().getCaller());
		for (LogicalOperatorInformation op : ops) {
			op.setOperatorName(op.getOperatorName().toUpperCase());
		}
		
				
		Collections.sort(ops, new Comparator<LogicalOperatorInformation>() {
			@Override
			public int compare(LogicalOperatorInformation o1, LogicalOperatorInformation o2) {
				return o1.getOperatorName().compareTo(o2.getOperatorName());
			}
		});
		for (LogicalOperatorInformation op : ops) {
			group.add(new CreationToolEntry(op.getOperatorName(), "Creates a new node.", new OperatorNodeFactory(op), null, null));
		}
		add(group);
	}
}
