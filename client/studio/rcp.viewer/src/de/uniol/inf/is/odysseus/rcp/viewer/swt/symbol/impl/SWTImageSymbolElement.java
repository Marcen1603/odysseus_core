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

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.Margin;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTImageSymbolElement<C> extends UnfreezableSWTSymbolElement<C> {

	private String imageName;
	private final String iconSetName;
	private Image image;
	private int imageWidth;
	private int imageHeight;
	private boolean fromOperator;
	private Margin margin;

	public SWTImageSymbolElement(String imageName, boolean fromOperator, Margin margin, String iconSetName) {
		this.imageName = imageName;
		this.fromOperator = fromOperator;
		this.margin = margin;
		this.iconSetName = iconSetName;
//		loadImage();
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
				// if the image name is not explicitly set then retrieve image by icon set
				if (this.imageName == null && getNodeView() != null) {
					this.imageName = iconSetName + "." + getOperatorName();
					if (!OdysseusRCPPlugIn.getImageManager().isRegistered(this.imageName)){
						this.imageName = iconSetName + ".DEFAULT"; 
					}
				}
				image = OdysseusRCPPlugIn.getImageManager().get(imageName);
//				image = OdysseusRCPViewerPlugIn.getImageManager().getOperatorImage(imageName, iconSetName);
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

	private String getOperatorName() {
		INodeView<C> nodeView = getNodeView();
		INodeModel<C> nodeModel = nodeView.getModelNode();
		C content = nodeModel.getContent();
		
		return content.getClass().getSimpleName();
	}
}
