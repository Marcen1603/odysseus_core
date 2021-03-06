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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTLineConnectionSymbolElement<C> extends SWTConnectionSymbolElement<C> {

	
	public SWTLineConnectionSymbolElement( Color activeLineColor, Color inactiveLineColor, Color suspendColor, Color partialColor ) {
		super(activeLineColor, inactiveLineColor, suspendColor, partialColor);
	}

	@Override
	public void draw(Vector start, Vector end, Vector screenShift, float zoomFactor ) {
		
		GC actualGC = getActualGC();
		setContextColor(actualGC);
	
		setContextLineSize(actualGC);
		getActualGC().drawLine( (int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY() );
		actualGC.setLineWidth(1);
	}
	
	@Override
	public void update(  ) {}
}
