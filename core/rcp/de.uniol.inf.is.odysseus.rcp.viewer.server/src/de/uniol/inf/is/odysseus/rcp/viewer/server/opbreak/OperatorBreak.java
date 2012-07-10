/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

public class OperatorBreak implements IBufferedPipeListener {

	private ISource<?> operator;
	private BufferedPipeWithListener<?> buffer;
	private List<IOperatorBreakListener> listeners = new ArrayList<IOperatorBreakListener>();
	private boolean isBreaked = false;
	
	private int sourceOutPort;
	private int sinkInPort;
	private ISink<?> sink;
	
	public OperatorBreak( ISource<?> source ) {
		operator = source;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean startBreak() {
		if( isBreaked ) return true;
		
		final int subCount = operator.getSubscriptions().size();

		if (subCount == 1) {

			// Neuer Buffer
			buffer = new BufferedPipeWithListener();
			buffer.addListener(this);

			// Subscription erhalten, welchen wir ersetzen wollen
			PhysicalSubscription subscription = operator.getSubscriptions().iterator().next();
			sink = (ISink)subscription.getTarget();
			sourceOutPort = subscription.getSourceOutPort();
			sinkInPort = subscription.getSinkInPort();
			
			insertBuffer(operator, sourceOutPort, sink, sinkInPort, buffer);
			
			try {
				buffer.open();
			} catch (OpenFailedException e) {
				// Zurückfahren
				e.printStackTrace();
				removeBuffer(operator, sourceOutPort, sink, sinkInPort, buffer);
				buffer = null;
				sink = null;
				return false;
			}

			isBreaked = true;
			fireBreakStartEvent();
			
//			System.out.println(this + " : Start Break");
			
			return true;

		}
        // Fehlermeldung, dass der Operator
        // genau eine Senke haben muss
        MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
        box.setMessage("Only sources with exact one sink can be breaked");
        box.setText("Error");
        box.open();
        return false;
	}
	
	public void endBreak() {
		if( !isBreaked ) return;
		
		// bisher gespeicherte Daten senden
		flush();
		
		removeBuffer(operator, sourceOutPort, sink, sinkInPort, buffer);
		
		isBreaked = false;
		buffer = null;
		sink = null;
		fireBreakEndEvent();
		
//		System.out.println(this + " : End Break");
	}
	
	public void flush() {
		while( buffer.size() > 0) {
			buffer.transferNext();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void insertBuffer( ISource source, int sourceOutPort, ISink sink, int sinkInPort, BufferedPipeWithListener buffer ) {
		// Alte Verbindung entfernen
		source.disconnectSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema());
		
		// Neue Verbindung erstellen
		source.connectSink(buffer, 0, sourceOutPort, source.getOutputSchema());
		buffer.connectSink(sink, sinkInPort, 0, buffer.getOutputSchema());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void removeBuffer( ISource source, int sourceOutPort, ISink sink, int sinkInPort, BufferedPipeWithListener buffer ) {
		// Alte Verbindungen trennen
		buffer.disconnectSink(sink, sinkInPort, 0, buffer.getOutputSchema());
		source.disconnectSink(buffer, 0, sourceOutPort, source.getOutputSchema());
		
		// Neue (alte) Verbindung herstellen
		source.connectSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema());
	}
	
	public void step() {
		if( !isBreaked ) 
			return;
		
		if( buffer.size() > 0 ) {
			Thread t = new Thread( new Runnable() {
	
				@Override
				public void run() {
//					System.out.println("Send step");
					if( buffer != null && buffer.size() > 0 )
						buffer.transferNext();
				}
				
			});
			t.setDaemon(false);
			t.setName("OperatorBreak Transfer Step");
			t.start();
		}
		
//		System.out.println(this + " : Step");
	}
	
	public final ISource<?> getOperator() {
		return operator;
	}
	
	public final int getBufferSize() {
		return buffer != null ? buffer.size() : 0;
	}
	
	public void addListener( IOperatorBreakListener listener ) {
		if( listener == null ) return;
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IOperatorBreakListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireBreakStartEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.breakStarted(this);
			}
		}
	}
	
	protected final void fireBreakEndEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.breakStopped(this);
			}
		}
	}
	
	protected final void fireSizeEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.sizeChanged(this);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[OperatorBreak ").append(operator).append("]");
		return sb.toString();
	}

	@Override
	public void sizeChanged(BufferedPipeWithListener<?> buffer) {
		fireSizeEvent();
	}
	
	public boolean isBreaked() {
		return isBreaked;
	}
}
