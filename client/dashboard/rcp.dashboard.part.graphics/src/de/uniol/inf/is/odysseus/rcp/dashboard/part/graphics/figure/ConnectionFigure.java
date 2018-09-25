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

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection.TextPosition;

/**
 * @author DGeesen
 * 
 */
public class ConnectionFigure extends PolylineConnection {

	private Label targetTextLabel;
	private Label sourceTextLabel;

	private Label topTextlabel;
	private Label bottomTextlabel;

	private static final int TEXT_PADDING = 10;
	
	public ConnectionFigure() {
		setLineWidth(2);
		targetTextLabel = new Label("");
		targetTextLabel.setOpaque(false);
		add(targetTextLabel, new ConnectionEndpointLocator(this, true));

		sourceTextLabel = new Label("");
		sourceTextLabel.setOpaque(false);
		add(sourceTextLabel, new ConnectionEndpointLocator(this, false));

		topTextlabel = new Label("");
		bottomTextlabel = new Label("");
		add(topTextlabel, new ConnectionRelativeToMidpointLocator(this, 0, ((getLineWidth()/2)+TEXT_PADDING)*-1));
		add(bottomTextlabel, new ConnectionRelativeToMidpointLocator(this, 0, (getLineWidth()/2)+TEXT_PADDING));
	}

	// public void paintFigure(Graphics g) {
	// super.paintFigure(g);
	// Rectangle r = getBounds().getCopy();
	// Point center = new Point(r.width / 2, r.height / 2);
	// Dimension topDim = topTextlabel.getPreferredSize();
	// setConstraint(topTextlabel, new Rectangle(center.x - topDim.width / 2, 0, topDim.width, topDim.height));
	//
	// Dimension bottomDim = bottomTextlabel.getPreferredSize();
	// setConstraint(bottomTextlabel, new Rectangle(center.x - bottomDim.width / 2, r.height - bottomDim.height, bottomDim.width, bottomDim.height));
	//
	// topTextlabel.invalidate();
	// bottomTextlabel.invalidate();
	// targetTextLabel.invalidate();
	// sourceTextLabel.invalidate();
	// }

	@Override
	public void paintFigure(Graphics g) {
		super.paintFigure(g);
		setConstraint(topTextlabel, new ConnectionRelativeToMidpointLocator(this, 0, ((getLineWidth()/2)+TEXT_PADDING)*-1));
		setConstraint(bottomTextlabel, new ConnectionRelativeToMidpointLocator(this, 0, ((getLineWidth()/2)+TEXT_PADDING)));
		targetTextLabel.invalidate();
		sourceTextLabel.invalidate();
		topTextlabel.invalidate();
		bottomTextlabel.invalidate();
	}

	public void setTargetText(String targetText) {
		this.targetTextLabel.setText(targetText);
	}

	public void setSourceText(String sourceText) {
		this.sourceTextLabel.setText(sourceText);
	}

	public void setTopText(String topText) {
		this.topTextlabel.setText(topText);
	}

	public void setBottomText(String bottomText) {
		this.bottomTextlabel.setText(bottomText);
	}

	public TextPosition getNearestPosition(Point location) {
		if (targetTextLabel.getBounds().contains(location)) {
			return TextPosition.Target;
		}
		if (sourceTextLabel.getBounds().contains(location)) {
			return TextPosition.Source;
		}
		if (bottomTextlabel.getBounds().contains(location)) {
			return TextPosition.Bottom;
		}
		if (topTextlabel.getBounds().contains(location)) {
			return TextPosition.Top;
		}
		return null;
	}

	/**
	 * @param position
	 * @return
	 */
	public Label getLabelByPosition(TextPosition position) {
		switch (position) {
		case Bottom:
			return bottomTextlabel;
		case Top:
			return topTextlabel;
		case Source:
			return sourceTextLabel;
		case Target:
			return targetTextLabel;
		default:
			return null;
		}
	}

}
