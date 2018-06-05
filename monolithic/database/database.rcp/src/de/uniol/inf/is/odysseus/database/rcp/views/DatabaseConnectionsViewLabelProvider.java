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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.rcp.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.database.rcp.Activator;

/**
 * 
 * @author Dennis Geesen Created at: 08.11.2011
 */
public class DatabaseConnectionsViewLabelProvider extends LabelProvider {

	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();	

	@Override
    public Image getImage(Object element) {
		ImageDescriptor descriptor = null;
		if (element instanceof DatabaseConnectionViewEntry) {
			descriptor = Activator.getImageDescriptor("database.png");
		} else if (element instanceof DatabaseInformationsViewEntry) {
			descriptor = Activator.getImageDescriptor("information-white.png");
		} else if (element instanceof DatabaseInformationItemViewEntry){
			descriptor = Activator.getImageDescriptor("bullet_black.png");
		} else if (element instanceof DatabaseTablesViewEntry){
			descriptor = Activator.getImageDescriptor("tables-stacks.png");
		} else if (element instanceof DatabaseTableItemViewEntry){
			descriptor = Activator.getImageDescriptor("table.png");
		}else{
			descriptor = Activator.getImageDescriptor("bullet_black.png");
		}

		// obtain the cached image corresponding to the descriptor
		Image image = imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}		

	@Override
	public void dispose() {
		for (Iterator<Image> i = imageCache.values().iterator(); i.hasNext();) {
			i.next().dispose();
		}
		imageCache.clear();
	}	
}
