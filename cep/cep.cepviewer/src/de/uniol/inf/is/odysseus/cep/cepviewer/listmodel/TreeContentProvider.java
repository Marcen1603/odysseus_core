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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * This is the ContentProvider of the TreeViewer within the lists of the
 * CEPListView.
 * 
 * @author Christian
 */
public class TreeContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {

	/**
	 * This method returns the children entries of the given entry.
	 * 
	 * @object parent is the entry which children entries should be returned
	 * 
	 * @return the children entries of the given entry
	 */
	public Object[] getChildren(Object parent) {
		AbstractTreeItem item = (AbstractTreeItem) parent;
		return item.getChildren().toArray();
	}

	/**
	 * This method returns the parent entry of the given entry.
	 * 
	 * @object element is the entry which parent entries should be returned
	 * 
	 * @return the parent entry of the given entry
	 */
	public Object getParent(Object element) {
		AbstractTreeItem item = (AbstractTreeItem) element;
		return item.getParent();
	}

	/**
	 * This method returns whether the given entry has children entries or not
	 * 
	 * @object element is the entry which should be checked
	 * 
	 * @return true if the given entry has children entries, else false
	 */
	public boolean hasChildren(Object element) {
		AbstractTreeItem item = (AbstractTreeItem) element;
		return !item.getChildren().isEmpty();
	}
}