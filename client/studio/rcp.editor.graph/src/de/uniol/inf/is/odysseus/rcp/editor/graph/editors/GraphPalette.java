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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.factories.ConnectionFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.factories.OperatorNodeFactory;

/**
 * @author DGeesen
 * 
 */
public class GraphPalette {

	private static final int MAX_IMAGE_SIZE = 16;

	public static PaletteRoot createGraphPalette() {
		PaletteRoot pr = new PaletteRoot();
		createToolsGroup(pr);
		createOperatorsGroup(pr);
		return pr;
	}

	private static void createOperatorsGroup(PaletteRoot palette) {

		Map<String, PaletteDrawer> pds = new TreeMap<String, PaletteDrawer>();

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
			String imageID = "default." + op.getOperatorName();
			if (!OdysseusRCPPlugIn.getImageManager().isRegistered(imageID)){
				imageID = "default.DEFAULT";
			}
			ImageDescriptor imgBig = OdysseusRCPPlugIn.getImageManager().getDescriptor(imageID);
			ImageDescriptor imgSmall = ImageDescriptor.createFromImageData(resizeImage(imgBig.getImageData()));
			CreationToolEntry entry = new CreationToolEntry(op.getOperatorName(), op.getDoc(), new OperatorNodeFactory(op), imgSmall, imgBig);
			String category = op.getCategories()[0];
			if (!pds.containsKey(category)) {
				pds.put(category, new PaletteDrawer(category));
			}
			pds.get(category).add(entry);
		}
		List<PaletteDrawer> list = new ArrayList<>(pds.values());
		Collections.sort(list, new Comparator<PaletteDrawer>() {

			@Override
			public int compare(PaletteDrawer arg0, PaletteDrawer arg1) {
				return arg0.getLabel().compareTo(arg1.getLabel());
			}

		});
		for (PaletteDrawer d : list) {
			d.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
			palette.add(d);
		}
	}

	private static ImageData resizeImage(ImageData oldData) {
		double oldW = oldData.width;
		double oldH = oldData.height;		
		if (oldW > oldH) {
			int newW = MAX_IMAGE_SIZE;
			int newH = (int) ((oldH / oldW) * MAX_IMAGE_SIZE);			
			return oldData.scaledTo(newW, newH);
		}
		int newH = MAX_IMAGE_SIZE;
		int newW = (int) (oldW / oldH) * MAX_IMAGE_SIZE;
		
		return oldData.scaledTo(newW, newH);
	}

	private static void createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
		ToolEntry tool = new PanningSelectionToolEntry();		
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
		toolbar.add(new MarqueeToolEntry());
		toolbar.add(new ConnectionCreationToolEntry("Connection", "Creates a new connection.", new ConnectionFactory(), Activator.getImageDescriptor("icons/graph_edge_directed_16.png"), Activator.getImageDescriptor("icons/graph_edge_directed_32.png")));
		palette.add(toolbar);
	}
}
