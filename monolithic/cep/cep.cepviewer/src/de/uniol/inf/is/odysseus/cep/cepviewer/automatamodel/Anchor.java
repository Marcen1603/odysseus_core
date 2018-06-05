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
package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class defines an anchor of a state which can hold one end of a
 * connection.
 * 
 * @author Christian
 */
public class Anchor extends AbstractConnectionAnchor {

	// holds the location of this anchor
	protected Point place;

	/**
	 * This is the constructor.
	 * 
	 * @param owner
	 */
	public Anchor(IFigure owner) {
		super(owner);
	}

	/**
	 * This method returns the location of this anchor.
	 * 
	 * @param point
	 *            is not used.
	 * @return Returns the location of this anchor.
	 */
	@Override
    public Point getLocation(Point point) {
		Rectangle rect = getOwner().getBounds();
		int x = rect.x + place.x * rect.width / 2;
		int y = rect.y + place.y * rect.height / 2;
		Point location = new PrecisionPoint(x, y);
		getOwner().translateToAbsolute(location);
		return location;
	}

}