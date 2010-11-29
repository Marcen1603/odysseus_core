package de.uniol.inf.is.odysseus.scars.emep;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.scars.emep.functions.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixAdd;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixGetEntry;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixInvert;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixMult;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixSub;
import de.uniol.inf.is.odysseus.scars.emep.functions.MatrixTranspose;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.scars.EMEP"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		MEP.registerFunction(new MatrixInvert());
		MEP.registerFunction(new MatrixAdd());
		MEP.registerFunction(new MatrixSub());
		MEP.registerFunction(new MatrixMult());
		MEP.registerFunction(new MatrixTranspose());
		MEP.registerFunction(new MatrixGetEntry());
		MEP.registerFunction(new GetAbsoluteValue());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		MEP.unregisterFunction("MatrixInv");
		MEP.unregisterFunction("MatrixAdd");
		MEP.unregisterFunction("MatrixSub");
		MEP.unregisterFunction("MatrixMult");
		MEP.unregisterFunction("MatrixTrans");
		MEP.unregisterFunction("MatrixEntry");
		MEP.unregisterFunction("AbsValue");
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
