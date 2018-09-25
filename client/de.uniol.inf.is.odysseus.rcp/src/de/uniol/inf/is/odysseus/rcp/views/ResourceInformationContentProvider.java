/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.views;

// TODO: Double code (see also GraphOutlineContentProvider)

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.planmanagement.AbstractResourceInformation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * 
 * @author Dennis Geesen
 * Created at: 24.08.2011
 */
public class ResourceInformationContentProvider implements ITreeContentProvider {

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
		if( parentElement instanceof List ) {
			return ((List<?>)parentElement).toArray();
		}		
		if (parentElement instanceof AbstractResourceInformation){
			Collection<Object> children = new ArrayList<Object>();
			children.addAll(((AbstractResourceInformation)parentElement).getOutputSchema().getAttributes());
			return children.toArray();
		}
		if (parentElement instanceof SDFAttribute) {
			Collection<Object> children = new ArrayList<Object>();
			if (((SDFAttribute) parentElement).getDatatype().hasSchema()) {
				children.addAll(((SDFAttribute) parentElement).getDatatype().getSchema().getAttributes());
			}
			if (((SDFAttribute) parentElement).getDtConstraints().size() > 0) {
				children.addAll(((SDFAttribute) parentElement).getDtConstraints());
			}
			if (((SDFAttribute) parentElement).getUnit() != null) {
				children.add(((SDFAttribute) parentElement).getUnit());
			}	
			return children.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof AbstractResourceInformation){
			return true;
		}
		if( element instanceof Entry ) 
			return true;
		if( element instanceof SDFAttribute )
			return ((SDFAttribute) element).getDatatype().getSubattributeCount() > 0 ||
					((SDFAttribute) element).getDtConstraints().size() > 0 ||
					((SDFAttribute) element).getUnit() != null;
		return false;
	}

}
