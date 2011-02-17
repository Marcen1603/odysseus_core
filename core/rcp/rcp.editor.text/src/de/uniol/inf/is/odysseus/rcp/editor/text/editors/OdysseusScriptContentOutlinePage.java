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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class OdysseusScriptContentOutlinePage extends ContentOutlinePage {

	private String queryText;
	
	public OdysseusScriptContentOutlinePage( String queryText ) {
		super();
		this.queryText = queryText;
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		TreeViewer treeViewer = getTreeViewer();
		
		treeViewer.setContentProvider(new OdysseusScriptContentProvider());
		treeViewer.setLabelProvider(new OdysseusScriptLabelProvider());
		treeViewer.addSelectionChangedListener(this);
		treeViewer.setInput(new StringTreeRoot(queryText));
	}
}
