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

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.AbstractPictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.ImagePictogramDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.ImagePictogramFigure;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author DGeesen
 * 
 */
public class ImagePictogram extends Pictogram {

	private String filename = "";
	private RelationalPredicate predicate;
	private boolean stretch;

	public ImagePictogram() {
		this.filename = "";
		this.stretch = true;
		setPredicate("true");
	}

	public IResource getFile() {
		if (getParentGroup() == null || getParentGroup().getProject() == null) {
			return null;
		}
		IResource file = getParentGroup().getProject().findMember(filename);
		return file;
	}

	public void setFilename(String filename) {
		this.filename = filename;
		changed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#init(java.util.Map)
	 */
	@Override
	protected void load(Map<String, String> values) {
		setFilename(loadValue(values.get("filename"), ""));
		setPredicate(loadValue(values.get("predicate"), "true"));
		setStretch(loadValue(Boolean.parseBoolean(values.get("stretch")), true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#save(java.util.Map)
	 */
	@Override
	protected void save(Map<String, String> values) {
		values.put("filename", filename);
		values.put("predicate", this.predicate.toString());
		values.put("stretch", Boolean.toString(stretch));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#process(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	protected void process(Tuple<?> tuple) {
		if (this.predicate.evaluate(tuple)) {
			setVisibile(true);
		} else {
			setVisibile(false);
		}

	}

	public void setPredicate(String predicate) {
		this.predicate = new RelationalPredicate(new SDFExpression(predicate, MEP.getInstance()));
		changed();
	}

	public IPredicate<Tuple<?>> getPredicate() {
		return this.predicate;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#open(java.util.Collection)
	 */
	@Override
	protected void open(Collection<IPhysicalOperator> roots) {
		try {
			this.predicate.init(roots.iterator().next().getOutputSchema(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	public boolean isStretch() {
		return this.stretch;
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
		changed();
	}

	@Override
	public Class<? extends AbstractPictogramDialog<ImagePictogram>> getConfigurationDialog() {
		return ImagePictogramDialog.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#createPictogramFigure()
	 */
	@Override
	public IFigure createPictogramFigure() {
		return new ImagePictogramFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#getPreferedSize()
	 */
	@Override
	public Dimension getPreferedSize() {
		IResource imgFile = getFile();
		if (imgFile != null && imgFile instanceof IFile) {
			Image img = new Image(Display.getDefault(), new ImageData(imgFile.getLocation().toOSString()));
			return new Dimension(img);
		}
		return super.getPreferedSize();
	}


}
