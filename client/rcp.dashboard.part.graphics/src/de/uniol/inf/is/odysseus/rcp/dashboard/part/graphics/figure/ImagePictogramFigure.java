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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;

/**
 * @author DGeesen
 * 
 */
public class ImagePictogramFigure extends AbstractPictogramFigure<ImagePictogram> {

	private Image image;
	private boolean visibile = true;
	private boolean stretch = true;
	private boolean center = true;
	private boolean keepRatio = true;

	public ImagePictogramFigure() {
		super();
	}

	@Override
	public void paintGraphic(Graphics g) {
		Rectangle r = getContentBounds().getCopy();
		if (this.visibile) {
			org.eclipse.swt.graphics.Rectangle imgBox = image.getBounds();
			if (stretch) {
				if (keepRatio) {
					g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, r.width, r.height);
				} else {
					g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, r.width, r.height);
				}
			} else {
				if (center) {
					int left = (r.width - imgBox.width) / 2;
					int top = (r.height - imgBox.height) / 2;
					g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x + left, r.y + top, imgBox.width, imgBox.height);
				} else {
					g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, imgBox.width, imgBox.height);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.
	 * AbstractPictogramFigure
	 * #updateValues(de.uniol.inf.is.odysseus.rcp.dashboard
	 * .part.graphics.model.Pictogram)
	 */
	@Override
	public void updateValues(ImagePictogram node) {
		this.visibile = node.isVisibile();
		this.stretch = node.isStretch();
		this.center = node.isCenter();
		this.keepRatio = node.isKeepRatio();
		if (image != null){
			image.dispose();
		}
		try {
			IResource file = node.getFile();
			if(file!=null){
				this.image = new Image(Display.getDefault(), new ImageData(file.getLocation().toOSString()));
			}else{
				this.image = new Image(Display.getDefault(), Activator.getImage("image.png").getImageData());
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.
	 * AbstractPictogramFigure#getContentSize()
	 */
	@Override
	public Dimension getContentSize() {
		if (image != null) {
			return new Dimension(image);
		}
		return getPreferredSize();

	}

}
