package de.uniol.inf.is.odysseus.viewer.swt.diagram;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import de.uniol.inf.is.odysseus.viewer.view.stream.IStreamDiagram;
import de.uniol.inf.is.odysseus.viewer.view.stream.Parameters;


public abstract class AbstractSWTDiagram<T> implements IStreamDiagram<T> {

	private Composite composite;
	private Parameters params;
	
	public AbstractSWTDiagram( Composite composite, Parameters params ) {
		this.composite = composite;
		this.params = params;
	}

	@Override
	public final void dataElementRecived( final T element, final int port ) {
		if( Display.getDefault().isDisposed() )
			return;
		
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				dataElementRecievedSynchronized( element, port );				
			}
		});
	}
	
	public final Composite getComposite() {
		return composite;
	}
	
	public final Parameters getParameters() {
		return params;
	}
	
	public abstract void dataElementRecievedSynchronized( T element, int port );
}
