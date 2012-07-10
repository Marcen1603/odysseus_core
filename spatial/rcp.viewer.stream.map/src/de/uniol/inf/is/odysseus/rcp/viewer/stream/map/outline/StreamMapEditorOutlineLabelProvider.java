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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.Layer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditorOutlineLabelProvider implements ILabelProvider {

	StreamMapEditor editor;
	Map<SDFAttribute, Image> images = null;

	public StreamMapEditorOutlineLabelProvider(StreamMapEditor editor) {
		this.editor = editor;
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
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof SDFSchema) {
			return ((SDFSchema) element).toString();
		} else if (element instanceof SDFAttribute) {
			return ((SDFAttribute) element).getAttributeName();
		}
		if (element instanceof Layer) {
			return ((Layer) element).getName();
		}
		return null;
	}
}
