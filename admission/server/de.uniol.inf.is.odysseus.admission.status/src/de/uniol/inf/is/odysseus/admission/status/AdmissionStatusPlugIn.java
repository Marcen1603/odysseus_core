package de.uniol.inf.is.odysseus.admission.status;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.admission.status.impl.SystemLoadPrinter;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;

public class AdmissionStatusPlugIn implements BundleActivator {

	private static final SystemLoadPrinter SYSTEM_LOAD_PRINTER = new SystemLoadPrinter();
	private static final boolean USE_SYSTEM_LOAD_PRINTER = false;
	
	private static IServerExecutor executor;
	private static IPhysicalCostModel physicalCostModel;
	private static ILogicalCostModel logicalCostModel;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if (physicalCostModel == serv) {
			physicalCostModel = null;
		}
	}

	// called by OSGi-DS
	public static void bindLogicalCostModel(ILogicalCostModel serv) {
		logicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindLogicalCostModel(ILogicalCostModel serv) {
		if (logicalCostModel == serv) {
			logicalCostModel = null;
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		if( USE_SYSTEM_LOAD_PRINTER ) {
			SYSTEM_LOAD_PRINTER.start();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if( USE_SYSTEM_LOAD_PRINTER ) {
			SYSTEM_LOAD_PRINTER.stopRunning();
		}
	}

	public static IServerExecutor getServerExecutor() {
		return executor;
	}

	public static ILogicalCostModel getLogicalCostModel() {
		return logicalCostModel;
	}

	public static IPhysicalCostModel getPhysicalCostModel() {
		return physicalCostModel;
	}
}
