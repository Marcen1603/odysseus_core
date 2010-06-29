package de.uniol.inf.is.odysseus.rcp.editor.model.activator;

import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.IOperatorDescriptorProvider;
import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.OperatorDescriptorRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.impl.OperatorDescriptorXMLReader;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Operatorbeschreibungen laden
		URL xmlFile = Activator.getContext().getBundle().getEntry("config/operators.xml");
		URL xsdFile = Activator.getContext().getBundle().getEntry("config/operatorsSchema.xsd");
		IOperatorDescriptorProvider provider = new OperatorDescriptorXMLReader(xmlFile, xsdFile);	
		OperatorDescriptorRegistry.getInstance().add(provider);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
