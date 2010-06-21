package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class StreamEditorRegistry {

	public static final String EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.viewer.StreamEditor";
	private static StreamEditorRegistry instance;

	private List<StreamExtensionDefinition> definitions = new ArrayList<StreamExtensionDefinition>();
	
	private StreamEditorRegistry() {
		evaluateRegisteredExtensions();
	}

	public static StreamEditorRegistry getInstance() {
		if (instance == null)
			instance = new StreamEditorRegistry();
		return instance;
	}
	
	public List<StreamExtensionDefinition> getStreamExtensionDefinitions() {
		return definitions;
	}

	public void evaluateRegisteredExtensions() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
//		try {
			for (IConfigurationElement e : config) {
				
				StreamExtensionDefinition def = new StreamExtensionDefinition();
				def.setID(e.getAttribute("id"));
				def.setConfigElement(e);
				definitions.add(def);
				
//				final Object o = e.createExecutableExtension("class");
//				if (o instanceof IGreeter) {
//					ISafeRunnable runnable = new ISafeRunnable() {
//						@Override
//						public void handleException(Throwable exception) {
//							System.out.println("Exception in client");
//						}
//
//						@Override
//						public void run() throws Exception {
//							((IGreeter) o).greet();
//						}
//					};
//					SafeRunner.run(runnable);
//				}
			}
//		} catch (CoreException ex) {
//			System.out.println(ex.getMessage());
//		}
	}
}
