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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeFigure extends Figure {

	private Label label;
	private Label toolTipLabel;
	private ConnectionAnchor connectionAnchor;
	private ImageData imageData;
	private Label imageLabel;
	static final int IMAGE_TEXT_SPACE = 3;

	public OperatorNodeFigure(OperatorNode operatorNode) {
		setLayoutManager(new XYLayout());
		// rectangle = new RectangleFigure();
		// rectangle.setBackgroundColor(ColorConstants.white);
		// add(rectangle);
		label = new Label();
		toolTipLabel = new Label();
		String imageID = "default." + operatorNode.getOperatorInformation().getOperatorName();
		if (!OdysseusRCPPlugIn.getImageManager().isRegistered(imageID)){
			imageID = "default.DEFAULT";
		}
		Image image = OdysseusRCPPlugIn.getImageManager().get(imageID);
		imageData = image.getImageData();
		imageLabel = new Label();
		imageLabel.setIcon(image);
		add(imageLabel);
		add(label);
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension dim = new Dimension();
		dim.setHeight(imageData.height + label.getPreferredSize().height + IMAGE_TEXT_SPACE);
		dim.setWidth(Math.max(imageData.width, label.getPreferredSize().width));
		return dim;
	}

	public Label getLabel() {
		return label;
	}

	public void setSatisfied(boolean value) {
		if (value) {
			label.setForegroundColor(ColorConstants.black);
		} else {
			label.setForegroundColor(ColorConstants.red);
		}
	}

	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		Point center = new Point(r.width / 2, r.height / 2);
		// setConstraint(rectangle, new Rectangle(0, 0, r.width, r.height));
		setConstraint(imageLabel, new Rectangle(0, 0, r.width, imageData.height));
		Dimension d = label.getPreferredSize();
		setConstraint(label, new Rectangle(center.x - d.width / 2, r.height - d.height, d.width, d.height));

		// rectangle.invalidate();
		imageLabel.invalidate();
		label.invalidate();
	}

	/**
	 * @return
	 */
	public ConnectionAnchor getConnectionAnchor() {
		if (connectionAnchor == null) {
			connectionAnchor = new ChopboxAnchor(this);
		}
		return connectionAnchor;
	}

	public void updateToolTip(String text) {
		toolTipLabel.setText(text);
		// toolTipLabel.setIcon(icon);
		setToolTip(toolTipLabel);
	}
}
