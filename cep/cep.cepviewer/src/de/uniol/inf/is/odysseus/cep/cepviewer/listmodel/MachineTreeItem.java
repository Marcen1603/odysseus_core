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

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;

/**
 * This class extends the class AbstractTreeItem and represents an StateMachine
 * within the TreeViewer.
 * 
 * @author Christian
 */
public class MachineTreeItem extends AbstractTreeItem {

	/**
	 * The constructor of this class.
	 * 
	 * @param parent
	 *            is the parent entry
	 * @param machine
	 *            the represented StateMachine object
	 */
	public MachineTreeItem(AbstractTreeItem parent, StateMachine<?> machine) {
		super(parent);
		this.name = Integer.toString(machine.hashCode());
		this.content = machine;
	}

	/**
	 * This method overrides the getContent() method of the class
	 * AbstractTreeItem and return the StateMachine object.
	 * 
	 * @return the StateMachine object
	 */
	@Override
    public StateMachine<?> getContent() {
		return (StateMachine<?>) this.content;
	}

	/**
	 * This method returns a String which represents the text of this entry.
	 * 
	 * @return the text of this entry
	 */
	@Override
    public String toString() {
		return StringConst.TREE_ITEM_MACHINE_LABEL + this.name + " (Total "
				+ this.getChildren().size() + ")";
	}

}
