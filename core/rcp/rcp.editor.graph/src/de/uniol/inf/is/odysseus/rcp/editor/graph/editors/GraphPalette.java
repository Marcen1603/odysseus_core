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
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.ConnectionFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.OperatorNodeFactory;

/**
 * @author DGeesen
 * 
 */
public class GraphPalette {

	public static PaletteRoot createGraphPalette() {
		PaletteRoot pr = new PaletteRoot();
		createToolsGroup(pr);
		createOperatorsGroup(pr);
		return pr;
	}

	private static void createOperatorsGroup(PaletteRoot palette) {
		PaletteDrawer sourcesToolbar = new PaletteDrawer("Sources");
		PaletteDrawer toolbar = new PaletteDrawer("Operators");
		// SelectionToolEntry entry = new SelectionToolEntry();
		// group.add(entry);
		// setDefaultEntry(entry);
		// group.add(new PaletteSeparator());

		// group.add(new CreationToolEntry("Node", "Creates a new node.", new OperatorNodeFactory(), null, null));

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
			CreationToolEntry entry = new CreationToolEntry(op.getOperatorName(), "Creates a new node.", new OperatorNodeFactory(op), null, null);
			if (op.getMaxPorts() == 0) {
				sourcesToolbar.add(entry);
			} else {
				toolbar.add(entry);
			}
		}
		palette.add(sourcesToolbar);
		palette.add(toolbar);
	}

	private static void createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
		toolbar.add(new MarqueeToolEntry());		
		toolbar.add(new ConnectionCreationToolEntry("Connection", "Creates a new connection.", new ConnectionFactory(),Activator.getImageDescriptor("icons/graph_edge_directed_16.png") ,Activator.getImageDescriptor("icons/graph_edge_directed_32.png")));
		palette.add(toolbar);		
	}
}
