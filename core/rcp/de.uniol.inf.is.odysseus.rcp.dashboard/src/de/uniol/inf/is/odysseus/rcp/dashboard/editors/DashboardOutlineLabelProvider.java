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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;

public class DashboardOutlineLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if( element instanceof DashboardPartPlacement ) {
			return DashboardPlugIn.getImageManager().get("dashboardPart");
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof DashboardPartPlacement ) {
			DashboardPartPlacement placement = (DashboardPartPlacement)element;
			return placement.getDashboardPart().getClass().getSimpleName();
		} else if( element instanceof String ) {
			return (String)element;
		}
		return null;
	}
}
