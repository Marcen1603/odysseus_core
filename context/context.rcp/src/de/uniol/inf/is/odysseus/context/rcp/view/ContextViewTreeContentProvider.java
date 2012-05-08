/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.rcp.view;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.04.2012
 */
public class ContextViewTreeContentProvider implements ITreeContentProvider {

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
		if( parentElement instanceof Collection ) {
			return ((Collection<?>)parentElement).toArray();
		}
		if(parentElement instanceof IContextStore){
			IContextStore<?> store = (IContextStore<?>)parentElement;
			Object[] childs = new Object[2];
			childs[0] = store.getSchema();
			childs[1] = store.getType();
			return childs;
		}
		if(parentElement instanceof SDFSchema){
			return ((SDFSchema)parentElement).getAttributes().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof Collection ) {
			return ((Collection<?>)element).size()>0;
		}
		if(element instanceof IContextStore){
			return true;
		}		
		if(element instanceof SDFSchema){
			return ((SDFSchema)element).getAttributes().size()>0;
		}
		return false;
	}

}
