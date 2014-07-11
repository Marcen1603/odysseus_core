package de.uniol.inf.is.odysseus.sports.sportsql;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * The bound {@link IDataDictionary}
	 */
	public static IDataDictionary dataDict;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	/**
	 * Binds an {@link IDataDictionary}.
	 * 
	 * @param dict
	 *            The {@link IDataDictionary} to bind.
	 */
	public static void bindDataDictionary(IDataDictionary dict) {
		Activator.dataDict = dict;
	}

	/**
	 * Unbinds an {@link IDataDictionary}, if <code>dict</code> is the bound
	 * one.
	 * 
	 * @param dict
	 *            The {@link IDataDictionary} to unbind.
	 */
	public static void unbindDataDictionary(IDataDictionary dict) {
		if (Activator.dataDict != null || Activator.dataDict.equals(dict))
			Activator.dataDict = null;
	}

	/**
	 * Returns the bound {@link IDataDictionary}, if there is one bound.
	 */
	public static Optional<IDataDictionary> getDataDictionary() {
		return Optional.fromNullable(Activator.dataDict);
	}
}
