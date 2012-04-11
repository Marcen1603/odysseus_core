/** Copyright [2011] [The Odysseus Team]
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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;

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
//		treeViewer.getTree().addListener(SWT.EraseItem, new Listener() {
//			
//			@Override
//			public void handleEvent(Event event) {
//		        event.gc.setBackground(event.gc.getBackground(event.item.getData(), event.index));
//		        event.gc.fillRectangle(event.getBounds());
//			}
//		});
		Object[] input = new Object[2];
		input[0] = editor.getSchema();
		input[1] = editor.getSpatialDataIndex().values();
		treeViewer.setInput(input);
	}
	
	public void setInput(StreamMapEditor editor){
		this.editor = editor;
		Object[] input = new Object[2];
		input[0] = editor.getSchema();
		input[1] = editor.getSpatialDataIndex().values();
		treeViewer.setInput(input);
	}
}
