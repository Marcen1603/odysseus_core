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

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;

public class DashboardOutlineLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof DashboardPartPlacement) {
			return DashboardPlugIn.getImageManager().get("dashboardPart");
		} else if (element instanceof String) {
			return DashboardPlugIn.getImageManager().get("setting");
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof DashboardPartPlacement) {
			final DashboardPartPlacement placement = (DashboardPartPlacement) element;
			return generateName(placement);
		} else if (element instanceof String) {
			return (String) element;
		}
		return null;
	}

	private static String generateName(DashboardPartPlacement placement) {
		StringBuilder sb = new StringBuilder();
		sb.append(placement.getDashboardPart().getClass().getSimpleName());
		String sinkName = placement.getDashboardPart().getSinkNames();
		if (!Strings.isNullOrEmpty(sinkName)) {
			sb.append(" [" + sinkName + "]");
		}
		return sb.toString();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
