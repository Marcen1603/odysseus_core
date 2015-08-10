package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;


public class ODLTemplateService {
	// called by OSGi-DS
	public void bind( IODLTemplate template ) {
		ODLTemplateRegistry.getInstance().register(template);
	}
	
	// called by OSGi-DS
	public void unbind( IODLTemplate template ) {
		ODLTemplateRegistry.getInstance().unregister(template);
	}
}
