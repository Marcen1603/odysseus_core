package de.uniol.inf.is.odysseus.parser.cql2.server;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionFactory {
	private Logger logger = LoggerFactory.getLogger(ExtensionFactory.class);

	private Map<String, IExtension> map = new HashMap<String, IExtension>();;

	static ExtensionFactory instance = null;

	public static synchronized ExtensionFactory getInstance() {
		if (instance == null)
			return (instance = new ExtensionFactory());
		return instance;
	}

	public void addExtension(IExtension extension) {
		logger.info("added " + extension.getName() + " for " + CQLParser.class.getCanonicalName());
		map.put(extension.getName(), extension);
	}

	public void removeExtension(IExtension extension) {
		logger.info("removed " + extension.getName() + " for " + CQLParser.class.getCanonicalName());
		map.remove(extension.getName());
	}

	public IExtension getExtension(String name) {
		return map.containsKey(name) ? map.get(name) : null;
	}

}