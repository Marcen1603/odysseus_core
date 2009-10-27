package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

public interface ISWTParameterListener extends Listener{
	
	public void setValue(TableItem item, Object value);
	
}
