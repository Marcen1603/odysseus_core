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

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.Layer;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditorOutlineTreeContentProvider implements
		ITreeContentProvider {

	Object[] input;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.input = (Object[]) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return this.input;
		} else if (parentElement instanceof SDFSchema) {
			return ((SDFSchema) parentElement).toArray();
		} else if (parentElement instanceof SDFAttribute) {
			return null;
		} else if (parentElement instanceof Collection<?>) {
			return ((Collection<?>) parentElement).toArray();
		} else if (parentElement instanceof Layer) {
			return null;
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof SDFSchema) {
			return null;
		} else if (element instanceof SDFAttribute) {
			return this.input[0];
		} else if (element instanceof Collection<?>) {
			return null;
		} else if (element instanceof Layer) {
			return this.input[1];
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof SDFSchema) {
			return true;
		} else if (element instanceof Collection<?>) {
			return true;
		}
		return false;
	}

}
