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
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

/**
 * @author DGeesen
 * 
 */
public class ConnectionFigure extends PolylineConnection {

	private Label targetPortLabel;
	private Label sourcePortLabel;

	public ConnectionFigure() {
		setLineWidth(2);
		PolygonDecoration deco = new PolygonDecoration();
		deco.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		setTargetDecoration(deco);

		targetPortLabel = new Label(Integer.toString(0));
		targetPortLabel.setOpaque(false);		
		add(targetPortLabel, new ConnectionEndpointLocator(this, true));

		sourcePortLabel = new Label(Integer.toString(0));
		sourcePortLabel.setOpaque(false);
		add(sourcePortLabel, new ConnectionEndpointLocator(this, false));
	}

	public void paintFigure(Graphics g) {
		super.paintFigure(g);
		targetPortLabel.invalidate();
		sourcePortLabel.invalidate();
	}
	
	public Label getTargetPortLabel() {
		return targetPortLabel;
	}

	public void setTargetPortLabel(Label targetPortLabel) {
		this.targetPortLabel = targetPortLabel;
	}

	public Label getSourcePortLabel() {
		return sourcePortLabel;
	}

	public void setSourcePortLabel(Label sourcePortLabel) {
		this.sourcePortLabel = sourcePortLabel;
	}

}
