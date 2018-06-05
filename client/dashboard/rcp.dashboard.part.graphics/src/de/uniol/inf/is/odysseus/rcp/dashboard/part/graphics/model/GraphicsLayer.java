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

public class GraphicsLayer extends Observable implements Serializable, Observer {

	private static final long serialVersionUID = 3019435887229998016L;

	private String backgroundImagePath;
	private boolean backgroundFileStretch;

	List<AbstractPictogram> pictograms = new ArrayList<AbstractPictogram>();
	List<AbstractPart> allParts = new ArrayList<AbstractPart>();

	private IProject project;

	private Collection<IPhysicalOperator> roots;

	public GraphicsLayer(String backgroundImagePath, boolean backgroundFileStretch, IProject parentProject) {
		this.backgroundImagePath = backgroundImagePath;
		this.setProject(parentProject);
		this.backgroundFileStretch = backgroundFileStretch;
	}

	public List<AbstractPictogram> getPictograms() {
		return pictograms;
	}

	public void processTuple(IPhysicalOperator senderOperator, Tuple<?> tuple) {
		for (AbstractPart p : allParts) {
			if (p.getSelectedRootName().equals(senderOperator.getName())) {
				p.internalProcess(tuple);
			}
		}
	}

	public void open(Collection<IPhysicalOperator> roots) {
		this.roots = roots;
		for (AbstractPart p : allParts) {
			p.internalOpen(roots);
		}
	}
	
	public void addPart(AbstractPart part){
		allParts.add(part);
		part.setGraphicsLayer(this);
		changed();
	}
	
	public void removePart(AbstractPart part){
		allParts.remove(part);
		part.setGraphicsLayer(null);
		changed();
	}

	public void addPictogram(AbstractPictogram pg) {
		getPictograms().add(pg);		
		resetUniqueIds();
		addPart(pg);
	}

	public void removePictogram(AbstractPictogram pg) {
		getPictograms().remove(pg);		
		resetUniqueIds();
		removePart(pg);
	}

	public AbstractPictogram getAbstractPictogramById(int id) {
		for (AbstractPictogram p : this.pictograms) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	public String getBackgroundImagePath() {
		return backgroundImagePath;
	}

	public void setBackgroundImagePath(String backgroundImagePath) {
		if(backgroundImagePath != null)
			this.backgroundImagePath = backgroundImagePath.replace('\\', '/');
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

	public synchronized void resetUniqueIds() {
		int id = 1;
		for (AbstractPictogram node : this.pictograms) {
			node.setId(id);
			id++;
		}
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
