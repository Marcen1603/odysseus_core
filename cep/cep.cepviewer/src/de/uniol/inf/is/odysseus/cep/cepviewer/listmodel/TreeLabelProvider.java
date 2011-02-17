/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.cep.cepviewer.listmodel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This is the LabelProvider of the TreeViewer within the lists of the
 * CEPListView.
 * 
 * @author Christian
 */
public class TreeLabelProvider extends LabelProvider {

	/**
	 * This method returns the image of the entry or null if the entry is not an
	 * instance of the classes InstanceTreeItem or LabelTreeItem.
	 * 
	 * @param object is the entry to get the image from.
	 * 
	 * @return the image of the given entry
	 */
	public Image getImage(Object object) {
		if (object instanceof InstanceTreeItem) {
			InstanceTreeItem instance = (InstanceTreeItem) object;
			return instance.getContent().getImage();
		} else if (object instanceof LabelTreeItem) {
			LabelTreeItem instance = (LabelTreeItem) object;
			return instance.getImage();
		}
		return null;
	}

	/**
	 * This method returns the name of the entry or null if the entry is not an
	 * instance of the class AbstractTreeItem.
	 * 
	 * @param object is the entry to get the name from.
	 * 
	 * @return the name of the given entry
	 */
	public String getText(Object object) {
		if (object instanceof AbstractTreeItem) {
			AbstractTreeItem item = (AbstractTreeItem) object;
			return item.toString();
		}
		return null;
	}
}