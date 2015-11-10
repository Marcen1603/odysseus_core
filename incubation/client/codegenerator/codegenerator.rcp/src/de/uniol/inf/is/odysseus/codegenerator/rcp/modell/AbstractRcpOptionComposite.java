package de.uniol.inf.is.odysseus.codegenerator.rcp.modell;

import org.eclipse.swt.widgets.Composite;

/**
 * abstract composite for the special options
 *  
 * @author MarcPreuschaft
 *
 */
public abstract class AbstractRcpOptionComposite extends Composite implements IRcpOptionComposite{

	public AbstractRcpOptionComposite(Composite parent, int style) {
		super(parent, style);
	}

}
