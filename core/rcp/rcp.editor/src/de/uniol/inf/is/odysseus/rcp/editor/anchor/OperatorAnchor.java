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
package de.uniol.inf.is.odysseus.rcp.editor.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class OperatorAnchor extends AbstractConnectionAnchor {

	public enum Type {
		INPUT,
		OUTPUT
	}
	
	private Type type;
	private int number = -1;
	private int maxNumber = -1;
	
	public OperatorAnchor( IFigure figure, Type type, int number, int maxNumber ) {
		this.maxNumber = maxNumber;

		setOwner(figure);
		setType(type);
		setNumber(number);
	}
	
	@Override
	public Point getLocation(Point reference) {
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getOwner().getBounds());
		r.translate(-1, -1);
		r.resize(1, 1);

		getOwner().translateToAbsolute(r);

		long dWidth = Math.round(((float)r.width / (float)( maxNumber + 1 ) ) * number);
		if( type == Type.INPUT ) {
			// unten
			return new Point(Math.round(r.x + dWidth), Math.round(r.y + r.height));
		}
        // oben
        return new Point(Math.round(r.x + dWidth), Math.round(r.y));
	}

	public Type getType() {
		return type;
	}

	private void setType(Type type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
			this.number = number;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

}
