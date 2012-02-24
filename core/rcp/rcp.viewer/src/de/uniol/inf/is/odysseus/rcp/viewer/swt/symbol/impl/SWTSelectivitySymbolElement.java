/** Copyright [2011] [The Odysseus Team]
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataModel;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;


public class SWTSelectivitySymbolElement<C> extends SWTSymbolElement<C> {

	private double value = 0.0;
	
	@Override
	public void draw( Vector position, int width, int height, float zoomFactor ) {
		
		GC gc = getActualGC();
		if( gc == null ) 
			return;
		
		// Rechteck zeichnen
		if( value > 0.0 ) { 
			int realHeight = (int)(height * 0.3f);
			// Balken reinzeichnen
			gc.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_RED ) );
			gc.fillRectangle( (int)position.getX(), (int)position.getY(), (int)(width * value), realHeight );
			
			gc.setForeground( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( (int)position.getX(), (int)position.getY(), width,  realHeight );
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		IMetadataModel<C> node = (IMetadataModel<C>)(getNodeView().getModelNode());
		if( node == null )
			return;
		
		// Selektivitätswert ermitteln
		IMonitoringData<Double> selectivity = (IMonitoringData<Double>)node.getMetadataItem( "selectivity" );
		if( selectivity != null )
			value = selectivity.getValue();
		else
			value = 0.0;
	}

}
