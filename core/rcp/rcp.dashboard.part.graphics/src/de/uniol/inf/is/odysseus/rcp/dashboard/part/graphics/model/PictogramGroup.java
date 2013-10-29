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
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class PictogramGroup extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = 3019435887229998016L;

	private String backgroundImagePath;
	private boolean backgroundFileStretch;

	List<Pictogram> nodes = new ArrayList<Pictogram>();

	private IProject project;

	private Collection<IPhysicalOperator> roots;

	public PictogramGroup(String backgroundImagePath, boolean backgroundFileStretch, IProject parentProject) {
		this.backgroundImagePath = backgroundImagePath;
		this.setProject(parentProject);
		this.backgroundFileStretch = backgroundFileStretch;
	}

	public List<Pictogram> getPictograms() {
		return nodes;
	}

	public void processTuple(IPhysicalOperator senderOperator, Tuple<?> tuple){
		for(Pictogram p : nodes){
			if(p.getSelectedRootName().equals(senderOperator.getName())){
				p.internalProcess(tuple);
			}
		}
	}

	public void open(Collection<IPhysicalOperator> roots) {
		this.roots = roots;
		for (Pictogram p : nodes) {
			p.internalOpen(roots);
		}
	}

	public void addPictogram(Pictogram pg) {
		getPictograms().add(pg);
		pg.setParentGroup(this);
		changed();
	}

	public void removePictogram(Pictogram pg) {
		getPictograms().remove(pg);
		pg.setParentGroup(null);
		changed();
	}

	public String getBackgroundImagePath() {
		return backgroundImagePath;
	}

	public void setBackgroundImagePath(String backgroundImagePath) {
		this.backgroundImagePath = backgroundImagePath;
	}

	/**
	 * @param backgroundFileStretch
	 */
	public void setBackgroundImageStretch(boolean backgroundFileStretch) {
		this.backgroundFileStretch = backgroundFileStretch;

	}

	public boolean isBackgroundFileStretch() {
		return backgroundFileStretch;
	}

	public void setBackgroundFileStretch(boolean backgroundFileStretch) {
		this.backgroundFileStretch = backgroundFileStretch;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	private void changed() {
		setChanged();
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		changed();
	}

	public void setDirty() {
		changed();
	}

	public Collection<IPhysicalOperator> getRoots() {
		return roots;
	}

	public void setRoots(Collection<IPhysicalOperator> roots) {
		this.roots = roots;
	}
}
