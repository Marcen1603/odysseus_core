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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ConnectionTreeContentProvider extends MapLayerViewTreeContentProvider {
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof MapEditorModel) {
			return input.getConnectionCollection().toArray(new LayerUpdater[0]);
		}
		return super.getChildren(parentElement);
	}

}
