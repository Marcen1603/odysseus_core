/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * 
 * @author Dennis Geesen
 * Created at: 24.08.2011
 */
public class SinkViewContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof Set ) {
			return ((Set<?>)parentElement).toArray();
		}
		if( parentElement instanceof Entry ) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>)parentElement;
			return entry.getValue().getOutputSchema().toArray();
		}
		if( parentElement instanceof SDFAttribute ) {
			if(((SDFAttribute)parentElement).getDatatype().hasSchema()){
				return ((SDFAttribute)parentElement).getDatatype().getSubSchema().toArray();
			}
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof Entry ) 
			return true;
		if( element instanceof SDFAttribute )
			return ((SDFAttribute)element).getDatatype().getSubattributeCount() > 0;
		return false;
	}

}
