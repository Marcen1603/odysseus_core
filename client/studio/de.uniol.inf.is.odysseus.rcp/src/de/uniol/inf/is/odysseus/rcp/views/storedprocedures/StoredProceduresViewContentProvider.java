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
package de.uniol.inf.is.odysseus.rcp.views.storedprocedures;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;

public class StoredProceduresViewContentProvider implements ITreeContentProvider {

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
		if( parentElement instanceof StoredProcedure ) {
			return ((StoredProcedure)parentElement).getVariables().toArray();			
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

    @Override
	public boolean hasChildren(Object element) {
		if( element instanceof Collection ){ 
			return true;
		}
		if( element instanceof StoredProcedure){
			return ((StoredProcedure)element).getVariables().size() > 0;
		}
		return false;
	}

}
