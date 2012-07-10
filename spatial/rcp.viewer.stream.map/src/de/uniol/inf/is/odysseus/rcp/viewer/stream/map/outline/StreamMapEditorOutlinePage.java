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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;

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

	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(new Action("Linecolor") {
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof VectorLayer) {
					VectorLayer element = (VectorLayer) i.getFirstElement();
					ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
					RGB color = element.getStyle().getLineColor().getRGB();
					colorDialog.setRGB(color);
					colorDialog.setText("ColorDialog");
					RGB selectedColor = colorDialog.open();
					if (!selectedColor.equals(color))
						element.getStyle().setLineColor(ColorManager.getInstance().getColor(selectedColor));
					getTreeViewer().refresh(element, true);
				}
				
			}
		});
		mgr.add(new Action("Fillcolor") {
			public void run() {
				ITreeSelection i = (ITreeSelection) treeViewer.getSelection();
				if (i.getFirstElement() instanceof VectorLayer) {
					VectorLayer element = (VectorLayer) i.getFirstElement();
					ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
					RGB color = element.getStyle().getFillColor().getRGB();
					colorDialog.setRGB(color);
					colorDialog.setText("ColorDialog");
					RGB selectedColor = colorDialog.open();
					if (!selectedColor.equals(color))
						element.getStyle().setFillColor(ColorManager.getInstance().getColor(selectedColor));
					getTreeViewer().refresh(element, true);
				}

			}
		});
	}
}
