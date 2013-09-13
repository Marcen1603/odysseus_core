/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class PictogramGroup extends Observable implements Serializable {
	
	private static final long serialVersionUID = 3019435887229998016L;
	
	List<Pictogram> nodes = new ArrayList<Pictogram>();;

	public List<Pictogram> getPictograms() {		
		return nodes;
	}
	
	public void processTuple(Tuple<?> tuple){
		for(Pictogram p : nodes){
			p.internalProcess(tuple);
		}
	}

	public void addPictogram(Pictogram pg) {
		getPictograms().add(pg);
		setChanged();
		notifyObservers();
	}

	public void removePictogram(Pictogram pg) {
		getPictograms().remove(pg);
		setChanged();
		notifyObservers();
	}
}
