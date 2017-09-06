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
package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.impl.AbstractConnectionSymbolElement;

public abstract class SWTConnectionSymbolElement<C> extends
		AbstractConnectionSymbolElement<C> {

	private GC gc;

	private final Color activeLineColor;
	private final Color inactiveLineColor;
	private final Color suspendColor;
	private final Color partialColor;

	public SWTConnectionSymbolElement(Color activeLineColor,
			Color inactiveLineColor, Color suspendColor, Color partialColor) {
		this.activeLineColor = activeLineColor;
		this.inactiveLineColor = inactiveLineColor;
		this.suspendColor = suspendColor;
		this.partialColor = partialColor;
	}

	public void setActualGC(GC gc) {
		this.gc = gc;
	}

	public GC getActualGC() {
		return gc;
	}

	protected void setContextColor(GC actualGC) {
		if (actualGC == null) {
			return;
		}
		if (isSuspended()) {
			actualGC.setLineStyle(SWT.LINE_DOT);
			actualGC.setForeground(suspendColor);
		} else if (isShedding()) {
			actualGC.setLineStyle(SWT.LINE_DOT);
			actualGC.setForeground(partialColor);
		} else if (isConnectionOpened()) {
			actualGC.setLineStyle(SWT.LINE_SOLID);
			actualGC.setForeground(activeLineColor);
		} else {
			actualGC.setLineStyle(SWT.LINE_DOT);
			actualGC.setForeground(inactiveLineColor);
		}
	}

	protected void setContextLineSize(GC actualGC){
		if (isSuspended() || isShedding()){
			int e = (int) Math.min(getBufferedElements()/10000,10); 
			actualGC.setLineWidth(e+2);
		}
	}
	
	protected void setContextBackgroundColor(GC actualGC) {
		if (actualGC == null) {
			return;
		}
		if (isSuspended()) {
			actualGC.setBackground(suspendColor);
			actualGC.setForeground(inactiveLineColor);
		} else if (isShedding()) {
			actualGC.setBackground(partialColor);
			actualGC.setForeground(inactiveLineColor);
		}else if (isConnectionOpened()) {
			actualGC.setBackground(activeLineColor);
			actualGC.setForeground(inactiveLineColor);
		} else {
			actualGC.setBackground(inactiveLineColor);
			actualGC.setForeground(inactiveLineColor);
		}
	}

	private boolean isConnectionOpened() {
		IConnectionModel<C> modelConnection = getConnectionView()
				.getModelConnection();
		if (modelConnection instanceof OdysseusConnectionModel) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel) modelConnection;
			return odyCon.getSubscriptionToSink().getOpenCalls() > 0;
		}
		return false;
	}

	private boolean isSuspended() {
		IConnectionModel<C> modelConnection = getConnectionView()
				.getModelConnection();
		if (modelConnection instanceof OdysseusConnectionModel) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel) modelConnection;
			AbstractPhysicalSubscription<?,?> sub = odyCon.getSubscriptionToSink();
			if (sub instanceof ControllablePhysicalSubscription){
				return ((ControllablePhysicalSubscription<?,?>)sub).isSuspended();
			}
		}
		return false;
	}
	
	private long getBufferedElements(){
		IConnectionModel<C> modelConnection = getConnectionView()
				.getModelConnection();
		if (modelConnection instanceof OdysseusConnectionModel) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel) modelConnection;
			AbstractPhysicalSubscription<?,?> sub = odyCon.getSubscriptionToSink();
			if (sub instanceof ControllablePhysicalSubscription){
				return ((ControllablePhysicalSubscription<?,?>)sub).getBufferSize();
			}
		}
		return 0;		
	}

	private boolean isShedding() {
		IConnectionModel<C> modelConnection = getConnectionView()
				.getModelConnection();
		if (modelConnection instanceof OdysseusConnectionModel) {
			OdysseusConnectionModel odyCon = (OdysseusConnectionModel) modelConnection;
			AbstractPhysicalSubscription<?,?> sub = odyCon.getSubscriptionToSink();
			if (sub instanceof ControllablePhysicalSubscription){
				return ((ControllablePhysicalSubscription<?,?>)sub).isShedding();
			}
		}
		return false;
	}

}
