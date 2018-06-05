/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.Observable;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 *
 */
public class OperatorGraphSelectionProvider extends Observable{
	
	private static OperatorGraphSelectionProvider instance = null;
	private OperatorNode currentlySelected;	
	
	private OperatorGraphSelectionProvider(){
		
	}
	
	public synchronized static OperatorGraphSelectionProvider getInstance(){
		if(instance==null){
			instance = new OperatorGraphSelectionProvider();
		}
		return instance;
	}
	
	
	public OperatorNode getCurrentlySelected() {
		return currentlySelected;
	}

	public void setCurrentlySelected(OperatorNode currentlySelected) {
		this.currentlySelected = currentlySelected;
		setChanged();
		notifyObservers(currentlySelected);
	}

	/**
	 * 
	 */
	public void update() {		
//		setChanged();
//		notifyObservers(this.currentlySelected);		
	}

}
