/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.AbstractLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;


/**
 * 
 * 
 * @author Stephan Jansen 
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditorOutlinePage extends ContentOutlinePage {

	private StreamMapEditor editor;
	private TreeViewer treeViewer;

	private static final Logger LOG = LoggerFactory.getLogger(StreamMapEditorOutlinePage.class);
	
	public StreamMapEditorOutlinePage(StreamMapEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		treeViewer = getTreeViewer();
		treeViewer.setContentProvider(new StreamMapEditorOutlineTreeContentProvider());
		treeViewer.setLabelProvider(new StreamMapEditorOutlineLabelProvider(editor));
		treeViewer.addSelectionChangedListener(this);
		Object[] input = new Object[2];
		input[0] = editor.getSchema();
		input[1] = editor.getLayerOrder();
		treeViewer.setInput(input);
		createContextMenu();
	}

	public void setInput(StreamMapEditor editor) {
		this.editor = editor;
		Object[] input = new Object[2];
		input[0] = editor.getSchema();
		input[1] = editor.getLayerOrder();
		treeViewer.setInput(input);
	}

	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu("", menuMgr, new ISelectionProvider() {

			@Override
			public void setSelection(ISelection selection) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public ISelection getSelection() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
				// TODO Auto-generated method stub

			}
		});
	}

	int counter = 0;
	
	public String[] TILESERVERS = {
	//new String("http://tah.openstreetmap.org/Tiles/tile/"),
	new String("http://oatile2.mqcdn.com/tiles/1.0.0/sat/"),
	new String("http://otile2.mqcdn.com/tiles/1.0.0/osm/"),
	new String("http://tile.opencyclemap.org/cycle/"),
	new String("http://tile2.opencyclemap.org/transport/"),
	new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/1/256/"),
	new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/2/256/"),
	new String("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/3/256/"),
	new String("http://otile1.mqcdn.com/tiles/1.0.0/osm/"),
	new String("http://oatile1.mqcdn.com/naip/"),
	new String("http://oatile1.mqcdn.com/naip/"), 
	new String("http://tile.openstreetmap.org/")
	};

	
	private void fillContextMenu(final IMenuManager mgr) {
		mgr.add(new Action("To VectorLayer"){ 
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof SDFAttribute) {
				
					SDFAttribute attribute  = (SDFAttribute) i.getFirstElement();
					
					editor.getSchema().contains(attribute);
					int position = editor.getSchema().indexOf(attribute);
					
					Style style = editor.getStyle((SDFSpatialDatatype)attribute.getDatatype());
					editor.addVectorLayer(attribute, position, style);
					LOG.info("Add Vector Layer for: " + attribute.getAttributeName());
				}
				getTreeViewer().refresh(true);
				editor.getScreenManager().getCanvas().redraw();
			}
		});
		
		
		mgr.add(new Action("Layer to Top"){ 
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof AbstractLayer) {
					int position = editor.getLayerOrder().lastIndexOf(i.getFirstElement());
					editor.getLayerOrder().remove(position);
					editor.getLayerOrder().addLast((ILayer) i.getFirstElement());
					
				}
				getTreeViewer().refresh(true);
				editor.getScreenManager().getCanvas().redraw();
			}
		});
		
		mgr.add(new Action("Remove Layer"){ 
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (!(i.getFirstElement() instanceof BasicLayer)) {
					
					//If the Element is a Vector - remove the related stream element
					if (i.getFirstElement() instanceof VectorLayer) {
						VectorLayer vectorLayer = (VectorLayer)i.getFirstElement();
						SDFAttribute attribute  = vectorLayer.getNativeAttribute();
						int position = editor.getSchema().indexOf(attribute);
						editor.removeVectorLayer(position);
					}
					
					//Remove the Layer 
					if (i.getFirstElement() instanceof AbstractLayer) {
						int position = editor.getLayerOrder().lastIndexOf(i.getFirstElement());
						editor.getLayerOrder().remove(position);	
					}
				}
				getTreeViewer().refresh(true);
				editor.getScreenManager().getCanvas().redraw();
			}
		});
		
		mgr.add(new Action("Add Layer"){ 
			public void run() {
				editor.getLayerOrder().add(new RasterLayer(editor.getScreenManager(), 0));
				getTreeViewer().refresh(true);
				editor.getScreenManager().getCanvas().redraw();
			}
		});
		
		
		mgr.add(new Action("Change Server") {
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof RasterLayer) {
					RasterLayer mapLayer = (RasterLayer) i.getFirstElement();
					if(TILESERVERS.length == counter){
						counter=0;
					}
					mapLayer.setTileServer(TILESERVERS[counter++]);
				}
				getTreeViewer().refresh(true);
				editor.getScreenManager().getCanvas().redraw();
			}
		});
		
		mgr.add(new Action("Linecolor") {
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof CollectionStyle) return;
				if (i.getFirstElement() instanceof Style) {
					Style element = (Style) i.getFirstElement();
					ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
					RGB color = element.getLineColor().getRGB();
					colorDialog.setRGB(color);
					colorDialog.setText("ColorDialog");
					RGB selectedColor = colorDialog.open();
					if (!selectedColor.equals(color))
						element.setLineColor(ColorManager.getInstance().getColor(selectedColor));
					getTreeViewer().refresh(element, true);
				}
				
			}
		});
		
		mgr.add(new Action("Fillcolor") {
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof Style) {
					Style element = (Style) i.getFirstElement();
					ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
					RGB color = element.getFillColor().getRGB();
					colorDialog.setRGB(color);
					colorDialog.setText("ColorDialog");
					RGB selectedColor = colorDialog.open();
					if (!selectedColor.equals(color))
						element.setFillColor(ColorManager.getInstance().getColor(selectedColor));
					getTreeViewer().refresh(element, true);
				}

			}
		});
	}
}
