/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.Margin;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTImageSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private final String imageName;
	private final String styleName;
	private Image image;
	private int imageWidth;
	private int imageHeight;
	private boolean fromOperator;
	private Margin margin;

	public SWTImageSymbolElement(String imageName, boolean fromOperator, Margin margin, String styleName) {
		this.imageName = imageName;
		this.fromOperator = fromOperator;
		this.margin = margin;
		this.styleName = styleName;
		loadImage();
	}

	@Override
	public void draw(Vector pos, int width, int height, Vector screenShift, float zoomFactor) {
		loadImage();
		if (image != null) {

			GC gc = getActualGC();

			if (gc == null)
				return;
			
			// calculate margin from percentage values
			double leftMargin = margin.getLeft() / 100.0 * width;
			double rightMargin = margin.getRight() / 100.0 * width;
			double topMargin = margin.getTop() / 100.0 * height;
			double bottomMargin = margin.getBottom() / 100.0 * height;

//			gc.drawImage(image, 0, 0, imageWidth, imageHeight, ((int) pos.getX()) + margin.getLeft(),
//					((int) pos.getY()) + margin.getTop(), width - margin.getLeft() - margin.getRight(),
//					height - margin.getTop() - margin.getBottom());
			gc.drawImage(image, 0, 0, imageWidth, imageHeight, (int)( pos.getX() + leftMargin),
					(int)( pos.getY() + topMargin), (int)(width - leftMargin - rightMargin),
					(int)(height - topMargin - bottomMargin));
		}
	}

	public final int getImageHeight() {
		return imageHeight;
	}

	public final int getImageWidth() {
		return imageWidth;
	}

	private void loadImage() {
		if (image == null || image.isDisposed()) {
			// Bild neu holen
			if (fromOperator) {
				image = OdysseusRCPViewerPlugIn.getImageManager().getOperatorImage(imageName, styleName);
			} else {
				image = OdysseusRCPViewerPlugIn.getImageManager().get(imageName);
			}
			if (image != null) {
				imageWidth = image.getBounds().width;
				imageHeight = image.getBounds().height;
			} else {
				// Kein Bild!
				return;
			}
		}
	}
}
