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

package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DashboardOutlineContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof Dashboard) {
			Dashboard dashboard = (Dashboard)parentElement;
			return dashboard.getDashboardPartPlacements().toArray();
		} else if( parentElement instanceof DashboardPartPlacement ) {
			DashboardPartPlacement placement = (DashboardPartPlacement)parentElement;
			return createList(placement);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object parentElement) {
		if( parentElement instanceof Dashboard) {
			Dashboard dashboard = (Dashboard)parentElement;
			return !dashboard.getDashboardPartPlacements().isEmpty();
		} else if( parentElement instanceof DashboardPartPlacement ) {
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
	}
	
	private static Object[] createList( DashboardPartPlacement placement) {
		return new Object[] {
			"File = " + placement.getFilename(),
			"X = " + placement.getX(),
			"Y = " + placement.getY(),
			"W = " + placement.getWidth(),
			"H = " + placement.getHeight()
		};
	}
}
