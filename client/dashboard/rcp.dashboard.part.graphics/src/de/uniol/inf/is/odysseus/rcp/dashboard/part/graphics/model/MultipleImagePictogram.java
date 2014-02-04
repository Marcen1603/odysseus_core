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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.MultipleImagesPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.MultipleImagesPictogramFigure;

/**
 * @author DGeesen
 * 
 */
public class MultipleImagePictogram extends AbstractPictogram {

	private ArrayList<ImagePictogram> images = new ArrayList<>();
	private boolean stretch = true;
	private boolean center = true;
	private boolean keepRatio = true;

	public MultipleImagePictogram() {

	}

	public MultipleImagePictogram(MultipleImagePictogram old) {
		super(old);
		this.images = new ArrayList<>();
		for (ImagePictogram ip : old.images) {
			this.images.add(ip.clone());
		}
		this.stretch = old.stretch;
		this.center = old.center;
		this.keepRatio = old.keepRatio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#load(java.util.Map)
	 */
	@Override
	protected void load(Map<String, String> values) {
		int count = loadValue(values.get("count"), 0);
		for (int i = 0; i < count; i++) {
			String image = values.get("image__" + i + "__filename");
			String predicate = values.get("image__" + i + "__predicate");
			addImage(predicate, image);
		}
		setStretch(loadValue(values.get("stretch"), true));
		setCenter(loadValue(values.get("center"), true));
		setKeepRatio(loadValue(values.get("keepRatio"), true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#save(java.util.Map)
	 */
	@Override
	protected void save(Map<String, String> values) {
		int i = 0;
		for (ImagePictogram ip : images) {
			Map<String, String> ipValues = new HashMap<String, String>();
			ip.save(ipValues);
			for (Entry<String, String> e : ipValues.entrySet()) {
				values.put("image__" + i + "__" + e.getKey(), e.getValue());
			}
			i++;
		}
		values.put("count", Integer.toString(i));
		values.put("stretch", Boolean.toString(stretch));
		values.put("center", Boolean.toString(center));
		values.put("keepRatio", Boolean.toString(keepRatio));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#init(java.util.Collection)
	 */
	@Override
	protected void open(IPhysicalOperator roots) {
		for (ImagePictogram image : images) {
			image.internalOpen(roots);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#process(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	protected void process(Tuple<?> tuple) {
		boolean oneMatched = false;
		for (ImagePictogram image : images) {
			if (!oneMatched && image.getPredicate().evaluate(tuple)) {
				image.setVisibile(true);
				oneMatched = true;
			} else {
				image.setVisibile(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#getConfigurationDialog()
	 */
	@Override
	public Class<? extends AbstractPictogramDialog<MultipleImagePictogram>> getConfigurationDialog() {
		return MultipleImagesPictogramDialog.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#createPictogramFigure()
	 */
	@Override
	public IFigure createPictogramFigure() {
		return new MultipleImagesPictogramFigure();
	}

	/**
	 * @return
	 */
	public boolean isStretch() {
		return this.stretch;
	}

	/**
	 * @param selection
	 */
	public void setStretch(boolean selection) {
		this.stretch = selection;
		for (ImagePictogram img : this.images) {
			img.setStretch(stretch);
		}
		setDirty();
	}

	public void clearImages() {
		this.images.clear();
	}

	public ImagePictogram addImage(String predicate, String image) {
		ImagePictogram ip = new ImagePictogram();
		ip.setFilename(image);
		ip.setConstraint(getConstraint());
		ip.setRelevancePredicate("true");
		ip.setPredicate(predicate);
		this.images.add(ip);
		if (getGraphicsLayer() != null) {
			ip.setGraphicsLayer(getGraphicsLayer());
		}
		setDirty();
		return ip;
	}

	/**
	 * @return
	 */
	public List<ImagePictogram> getImages() {
		return Collections.unmodifiableList(images);
	}

	@Override
	public void setGraphicsLayer(GraphicsLayer parentGroup) {
		super.setGraphicsLayer(parentGroup);
		for (ImagePictogram ip : this.images) {
			ip.setGraphicsLayer(parentGroup);
		}
	}

	public boolean isKeepRatio() {
		return keepRatio;
	}

	public void setKeepRatio(boolean keepRatio) {
		this.keepRatio = keepRatio;
		for (ImagePictogram img : this.images) {
			img.setKeepRatio(keepRatio);
		}
		setDirty();
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
		for (ImagePictogram img : this.images) {
			img.setCenter(center);
		}
		setDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#clone()
	 */
	@Override
	public MultipleImagePictogram clone() {
		return new MultipleImagePictogram(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram#setConstraint(org.eclipse.draw2d.geometry.Rectangle)
	 */
	@Override
	public void setConstraint(Rectangle newConstraint) {
		super.setConstraint(newConstraint);
		for (ImagePictogram img : this.images) {
			img.setConstraint(newConstraint);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram#setLocation(org.eclipse.draw2d.geometry.Point)
	 */
	@Override
	public void setLocation(Point location) {	
		super.setLocation(location);
		for (ImagePictogram img : this.images) {
			img.setLocation(location);
		}
	}
}