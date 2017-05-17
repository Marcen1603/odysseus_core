package de.uniol.inf.is.odysseus.parser.novel.cql;

public class ExtensionService {

	
	public void addExtension(IExtension extension){
//		logger.debug("Adding visitor for CQL: "+name);
		ExtensionFactory.getInstance().addExtension(extension);
	}
	
	public void removeExtension(IExtension extension){
//		logger.debug("Removing visitor for CQL: "+name);
		ExtensionFactory.getInstance().removeExtension(extension);
	}
	
}
