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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * @author DGeesen
 * 
 */
public class ImagePictogramFigure extends Figure {

	private Image image;
	private ImageFigure imageFigure;
	private Label label;
	private RectangleFigure rectangle;
	private boolean visibile = true;

	public ImagePictogramFigure() {
		setLayoutManager(new XYLayout());
		setOpaque(false);
		setBorder(new LineBorder(ColorConstants.white));
		rectangle = new RectangleFigure();		
		rectangle.setOpaque(false);
		rectangle.setBorder(new LineBorder(ColorConstants.white));
		add(rectangle);
		
		label = new Label();		
		label.setOpaque(false);		
		add(label);
		
		imageFigure = new ImageFigure();
		add(imageFigure);
	}

	public void setImage(String filename) {
		this.image = new Image(Display.getDefault(), new ImageData(filename));
		
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	
	public void refresh(){
		this.repaint();		
	}

	public void paintFigure(Graphics g) {
		if (this.visibile) {
			imageFigure.setImage(image);
		} else {
			imageFigure.setImage(null);
		}
		Rectangle r = getBounds().getCopy();
		setConstraint(rectangle, new Rectangle(0, 0, r.width, r.height));
		setConstraint(label, new Rectangle(0, 0, r.width, r.height));
		setConstraint(imageFigure, new Rectangle(0, 0, r.width, r.height));
		rectangle.invalidate();
		label.invalidate();
		imageFigure.invalidate();
	}

}
