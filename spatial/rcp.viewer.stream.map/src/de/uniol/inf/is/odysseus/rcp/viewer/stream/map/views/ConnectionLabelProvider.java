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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ConnectionLabelProvider implements ILabelProvider {

	Map<SDFAttribute, Image> images = null;

	public ConnectionLabelProvider() {
		images = new TreeMap<SDFAttribute, Image>();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		for (Image image : images.values()) {
			image.dispose();
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof VectorLayer) {
			if (((VectorLayer) element).getStyle() != null) {
				return ((VectorLayer) element).getStyle().getImage();
			}
		}
		if (element instanceof Style) {
			return ((Style) element).getImage();
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof LayerUpdater) {
			return ((LayerUpdater) element).getQuery().getLogicalQuery().getQueryText();
		} else if (element instanceof SDFAttribute) {
			return ((SDFAttribute) element).getAttributeName();
		} else if (element instanceof IFile) {
			return ((IFile) element).getName();
		} else {
			return "Unknown Type";
		}
	}
}
