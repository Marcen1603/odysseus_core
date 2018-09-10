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

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.Lists;


public class DashboardOutlineContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Dashboard) {
			final Dashboard dashboard = (Dashboard) parentElement;
			return dashboard.getDashboardPartPlacements().toArray();
		} else if (parentElement instanceof DashboardPartPlacement) {
			final DashboardPartPlacement placement = (DashboardPartPlacement) parentElement;
			return createSettingList(placement);
		}
		return null;
	}
	
	private static Object[] createSettingList(DashboardPartPlacement placement) {
		List<Object> objects = Lists.newArrayList();
		objects.add("File = " + placement.getFilename());
		objects.add("X = " + placement.getX());
		objects.add("Y = " + placement.getY());
		objects.add("W = " + placement.getWidth());
		objects.add("H = " + placement.getHeight());
		return objects.toArray();
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object parentElement) {
		if (parentElement instanceof Dashboard) {
			final Dashboard dashboard = (Dashboard) parentElement;
			return !dashboard.getDashboardPartPlacements().isEmpty();
		} else if (parentElement instanceof DashboardPartPlacement) {
			return true;
		}
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}


}
