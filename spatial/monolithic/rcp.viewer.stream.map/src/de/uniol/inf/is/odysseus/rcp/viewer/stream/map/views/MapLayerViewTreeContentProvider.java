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

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.GroupLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.CollectionStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class MapLayerViewTreeContentProvider implements ITreeContentProvider {

	MapEditorModel input;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.input = (MapEditorModel) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof LayerUpdater[]) {
			return (LayerUpdater[]) parentElement;
		} else if (parentElement instanceof LayerUpdater) {
			return ((LayerUpdater) parentElement).getConnection().getOutputSchema().toArray();
		} else if (parentElement instanceof MapEditorModel) {
			return input.getLayers().toArray(new ILayer[0]);
		} else if (parentElement instanceof SDFSchema) {
			return ((SDFSchema) parentElement).toArray();
		} else if (parentElement instanceof SDFAttribute) {
			return new Object[0];
		} else if (parentElement instanceof Collection<?>) {
			return ((Collection<?>) parentElement).toArray();
		} else if (parentElement instanceof GroupLayer) {
			return ((GroupLayer) parentElement).toArray();
		} else if (parentElement instanceof VectorLayer) {
			Style style = ((VectorLayer) parentElement).getStyle();
			if (style instanceof CollectionStyle)
				return style.getSubstyles();
			return new Style[] { ((VectorLayer) parentElement).getStyle() };
		} else if (parentElement instanceof Style) {
			return ((Style) parentElement).getSubstyles();
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IStreamConnection<?>) {
			return null;
		} else if (element instanceof SDFAttribute) {
			return this.input.getConnectionCollection().toArray();
		} else if (element instanceof Collection<?>) {
			return null;
		} else if (element instanceof ILayer) {
			String groupname = ((ILayer) element).getConfiguration().getGroup();
			if (groupname != null) {
				return this.input.getGroup(groupname).toArray();
			}
			return this.input.getLayers().toArray();
		} else if (element instanceof Style) {
			List<ILayer> layer = this.input.getLayers();
			for (ILayer iLayer : layer) {
				Style style = iLayer.getStyle();
				if (style != null) {
					if (element == style)
						return iLayer;
					if (style.contains(style))
						return style;
				}
			}
			return null;
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof SDFSchema) {
			return true;
//		} else if (element instanceof LayerUpdater[]) {
//			return true;
//		} else if (element instanceof LayerUpdater) {
//			return true;
//		} else if (element instanceof Collection<?>) {
//			return true;
		} else if (element instanceof VectorLayer) {
			return true;
		} else if (element instanceof GroupLayer) {
			return true;
		} else if (element instanceof Style) {
			return ((Style) element).hasSubstyles();
		}

		return false;
	}

}
