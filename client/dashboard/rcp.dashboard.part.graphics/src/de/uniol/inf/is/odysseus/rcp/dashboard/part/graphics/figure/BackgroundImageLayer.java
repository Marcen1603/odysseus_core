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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Image;

/**
 * @author DGeesen
 * 
 */
public class BackgroundImageLayer extends FreeformLayer {

	private Image image;
	private boolean stretch;

	public BackgroundImageLayer(String path, boolean stretch) {
		super();
		this.stretch = stretch;
		setImage(ImageFactory.getBackgroundImage(path));
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		if (getImage() != null) {
			org.eclipse.draw2d.geometry.Rectangle targetRect = getBounds()
					.getCopy();
			org.eclipse.swt.graphics.Rectangle imgBox = getImage().getBounds();
			if (stretch) {
				graphics.drawImage(getImage(), 0, 0, imgBox.width,
						imgBox.height, targetRect.x, targetRect.y,
						targetRect.width, targetRect.height);
			} else {
				graphics.drawImage(getImage(), 0, 0);
			}
		}
		super.paintFigure(graphics);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
