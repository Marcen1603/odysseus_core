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
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

/**
 * @author DGeesen
 * 
 */
public abstract class AbstractPictogramFigure<T extends Pictogram> extends Figure {

	public AbstractPictogramFigure() {
		setLayoutManager(new XYLayout());
		setOpaque(false);
		setBorder(new LineBorder(ColorConstants.white));
	}

	public void refresh() {
		this.repaint();
	}

	/**
	 * @param node
	 */
	public abstract void updateValues(T node);
}
