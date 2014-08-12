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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;

public class SWTLineConnectionSymbolElement<C> extends SWTConnectionSymbolElement<C> {

	private final Color activeLineColor;
	private final Color inactiveLineColor;
	
	public SWTLineConnectionSymbolElement( Color activeLineColor, Color inactiveLineColor ) {
		this.activeLineColor = activeLineColor;
		this.inactiveLineColor = inactiveLineColor;
	}

	@Override
	public void draw(Vector start, Vector end, Vector screenShift, float zoomFactor ) {
		
		GC actualGC = getActualGC();
		if( actualGC == null ) {
			return;
		}
		if (isSuspended()){
			actualGC.setLineStyle(SWT.LINE_DOT);
			actualGC.setForeground( activeLineColor );			
		}else if( isConnectionOpened() ) {
			actualGC.setLineStyle(SWT.LINE_SOLID);
			actualGC.setForeground( activeLineColor );
		} else {
			actualGC.setLineStyle(SWT.LINE_DOT);
			actualGC.setForeground( inactiveLineColor );
		}
	
		actualGC.setLineWidth(2);
		getActualGC().drawLine( (int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY() );
		actualGC.setLineWidth(1);
	}
	
	private boolean isConnectionOpened() {
		IConnectionModel<C> modelConnection = getConnectionView().getModelConnection();
		if( modelConnection instanceof OdysseusConnectionModel ) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel)modelConnection;
			return odyCon.getSubscriptionToSink().getOpenCalls() > 0;
		}
		return false;
	}
	
	private boolean isSuspended(){
		IConnectionModel<C> modelConnection = getConnectionView().getModelConnection();
		if( modelConnection instanceof OdysseusConnectionModel ) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel)modelConnection;
			return odyCon.getSubscriptionToSink().isSuspended();
		}
		return false;
	}

	@Override
	public void update(  ) {}
}
