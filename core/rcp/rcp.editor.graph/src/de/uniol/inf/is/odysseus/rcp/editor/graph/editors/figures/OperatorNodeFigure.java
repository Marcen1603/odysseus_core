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
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeFigure extends Figure {

	private Label label;
	private RectangleFigure rectangle;
	private ConnectionAnchor connectionAnchor;

	public OperatorNodeFigure() {
		setLayoutManager(new XYLayout());
		rectangle = new RectangleFigure();
		rectangle.setBackgroundColor(ColorConstants.lightGray);
		add(rectangle);
		label = new Label();
		add(label);
	}

	public Label getLabel() {
		return label;
	}
	
	public void setSatisfied(boolean value){
		if(value){
			rectangle.setBackgroundColor(ColorConstants.lightGray);
		}else{
			rectangle.setBackgroundColor(ColorConstants.red);
		}
	}

	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		setConstraint(rectangle, new Rectangle(0, 0, r.width, r.height));
		setConstraint(label, new Rectangle(0, 0, r.width, r.height));
		rectangle.invalidate();
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

}
