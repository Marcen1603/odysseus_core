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

import org.eclipse.draw2d.Graphics;
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

	public ImagePictogramFigure() {
		super();
	}	

	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		if (this.visibile) {
			org.eclipse.swt.graphics.Rectangle imgBox = image.getBounds();
			if (stretch) {
				g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, r.width, r.height);
			} else {
				g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, imgBox.width, imgBox.height);
			}

		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.AbstractPictogramFigure#updateValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram)
	 */
	@Override
	public void updateValues(ImagePictogram node) {		
		this.visibile = node.isVisibile();
		this.stretch = node.isStretch();
		try {			
			this.image = new Image(Display.getDefault(), new ImageData(node.getFile().getLocation().toOSString()));
		} catch (Exception e) {
			e.printStackTrace();
			this.image = new Image(Display.getDefault(), Activator.getImage("image.png").getImageData());
		}
		
	}

}
