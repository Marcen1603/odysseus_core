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
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.base.Preconditions;

public class StreamEditorListContentProvider implements IStructuredContentProvider {

	private List<String> elements = new LinkedList<String>();
	private final int maxElements;
	
	public StreamEditorListContentProvider(int maxElements) {
		Preconditions.checkArgument(maxElements > 0, "Maximum elements must be positive.");
		
		this.maxElements = maxElements;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return elements.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void addElement(String element) {
		elements.add(0, element);
		if( elements.size() > maxElements ) {
			elements.remove(elements.size() - 1);
		}
	}

}
