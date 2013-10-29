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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.MultipleImagePictogram;

/**
 * @author DGeesen
 * 
 */
public class MultipleImagesPictogramFigure extends AbstractPictogramFigure<MultipleImagePictogram> {

	private List<Image> images = new ArrayList<>();
	private Map<Image, ImagePictogram> imagePictograms = new HashMap<>();
	private boolean visibile = true;
	private boolean stretch;

	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		if (this.visibile) {
			for (Image image : images) {
				ImagePictogram ip = imagePictograms.get(image);
				if (ip.isVisibile()) {
					org.eclipse.swt.graphics.Rectangle imgBox = image.getBounds();
					if (stretch) {
						g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, r.width, r.height);
					} else {
						g.drawImage(image, 0, 0, imgBox.width, imgBox.height, r.x, r.y, imgBox.width, imgBox.height);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure.AbstractPictogramFigure#updateValues(de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram)
	 */
	@Override
	public void updateValues(MultipleImagePictogram node) {
		this.visibile = node.isVisibile();
		this.stretch = node.isStretch();
		images.clear();
		imagePictograms.clear();
		for (ImagePictogram ip : node.getImages()) {
			try {
				Image image = new Image(Display.getDefault(), new ImageData(ip.getFile().getLocation().toOSString()));
				this.images.add(image);
				imagePictograms.put(image, ip);
			} catch (Exception e) {
				e.printStackTrace();
				Image image = new Image(Display.getDefault(), Activator.getImage("image.png").getImageData());
				this.images.add(image);
				imagePictograms.put(image, ip);
			}
		}

	}

}
