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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

/**
 * @author DGeesen
 * 
 */
public abstract class AbstractPictogramFigure<T extends Pictogram> extends Figure {

	private Label topTextlabel;
	private Label bottomTextlabel;
	private static final int TEXT_HEIGHT_MARGIN = 3;

	public AbstractPictogramFigure() {
		setLayoutManager(new XYLayout());
		setOpaque(false);		
		topTextlabel = new Label();
		bottomTextlabel = new Label();
		add(topTextlabel);
		add(bottomTextlabel);
	}

	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		Point center = new Point(r.width / 2, r.height / 2);
		Dimension topDim = topTextlabel.getPreferredSize();
		setConstraint(topTextlabel, new Rectangle(center.x - topDim.width / 2, 0, topDim.width, topDim.height));

		Dimension bottomDim = bottomTextlabel.getPreferredSize();
		setConstraint(bottomTextlabel, new Rectangle(center.x - bottomDim.width / 2, r.height - bottomDim.height, bottomDim.width, bottomDim.height));

		topTextlabel.invalidate();
		bottomTextlabel.invalidate();
		paintGraphic(g);
	}

	public void refresh() {
		this.repaint();
	}

	public void updateValuesInternal(T node) {
		this.topTextlabel.setText(node.getTextTop());
		this.bottomTextlabel.setText(node.getTextBottom());
		recalcPreferedSize();
		updateValues(node);
	}

	private void recalcPreferedSize() {

		int height = topTextlabel.getPreferredSize().height + bottomTextlabel.getPreferredSize().height + (2 * TEXT_HEIGHT_MARGIN);
		height = height + getContentSize().height;
		int width = Math.max(topTextlabel.getPreferredSize().width, bottomTextlabel.getPreferredSize().width);
		width = Math.max(width, getContentSize().width);
		setPreferredSize(width, height);
	}

	protected Rectangle getContentBounds() {
		Rectangle r = getBounds().getCopy();
		r.y = r.y + topTextlabel.getPreferredSize().height;
		r.height = r.height - topTextlabel.getPreferredSize().height - bottomTextlabel.getPreferredSize().height;
		return r;
	}

	public abstract void updateValues(T node);

	public abstract void paintGraphic(Graphics g);

	public abstract Dimension getContentSize();
}
