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

import java.util.ArrayList;

/**
 * This class represents an entry within the TreeViewer.
 * 
 * @author Christian
 */
public abstract class AbstractTreeItem {
	// the text of the entry
	protected String name;
	// the parent entry
	protected AbstractTreeItem parent;
	// holds a list of all children entries
	protected ArrayList<AbstractTreeItem> children;
	// the content of the entry
	protected Object content;

	/**
	 * This is the constructor of this class.
	 * 
	 * @param parent
	 *            is the parent entry
	 */
	public AbstractTreeItem(AbstractTreeItem parent) {
		this.parent = parent;
		this.children = new ArrayList<AbstractTreeItem>();
	}

	/**
	 * This method add an entry to the list of children entries.
	 * 
	 * @param item
	 *            an entry which should be added to the list of children entries
	 */
	public void add(AbstractTreeItem item) {
		this.children.add(item);
	}

	/**
	 * This method removes all children entries of the list.
	 */
	public void removeAllChildren() {
		this.children = new ArrayList<AbstractTreeItem>();
	}

	/**
	 * This method should return the text form this entry.
	 */
	public abstract String toString();

	/**
	 * This is the getter for the parent entry.
	 * @return the parent entry
	 */
	public AbstractTreeItem getParent() {
		return this.parent;
	}

	/**
	 * This is the setter to set a new parent entry.
	 * @param newParent the parent entry
	 */
	public void setParent(AbstractTreeItem newParent) {
		this.parent = newParent;
	}

	/**
	 * This is the getter for the list of children entries.
	 * @return the list of children entries
	 */
	public ArrayList<AbstractTreeItem> getChildren() {
		return this.children;
	}

	/**
	 * This is the getter for the content.
	 * @return the content
	 */
	public Object getContent() {
		return this.content;
	}

}
