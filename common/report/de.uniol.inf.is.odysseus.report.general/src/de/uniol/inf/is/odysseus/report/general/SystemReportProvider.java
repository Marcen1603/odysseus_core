package de.uniol.inf.is.odysseus.report.general;

import java.util.Locale;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class SystemReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "System Information";
	}
	
	@Override
	public String getReport(ISession session) {
		StringBuilder report = new StringBuilder();
		
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		String osName = System.getProperty("os.name");
		String vmVersion = System.getProperty("java.vm.version");
		String vmVendor = System.getProperty("java.vm.vendor");
		String vmName = System.getProperty("java.vm.name");
		
		Runtime rt = Runtime.getRuntime();
		long mem = rt.totalMemory();
		int cpu = rt.availableProcessors();
		IProduct product = Platform.getProduct();
		Bundle bundle = product.getDefiningBundle();
		Version version = bundle.getVersion();
		String productVersion = version.toString();
		String locale = Locale.getDefault().toString();
		
		report.append("\t* Operating System: ").append(osName).append(" ").append(osVersion).append(" (").append(osArch).append(")\n");
		report.append("\t* Java VM: ").append(vmName).append(" ").append(vmVersion).append(" (").append(vmVendor).append(")\n");
		report.append("\t* Product: ").append(productVersion).append("\n");
		report.append("\t* CPUs: ").append(cpu).append("\n");
		report.append("\t* Memory: ").append(mem).append("\n");
		report.append("\t* Locale: ").append(locale).append("\n");
		report.append("\t* System Properties: ").append("\n");
		
		for (Entry<Object, Object> property : System.getProperties().entrySet()) {
			report.append("\t\t- ").append(property.getKey()).append(": ").append(property.getValue()).append("\n");
		}
		
		report.append("\t* System Environment: ").append("\n");
		for (Entry<String, String> property : System.getenv().entrySet()) {
			report.append("\t\t- ").append(property.getKey()).append(": ").append(property.getValue()).append("\n");
		}
		
		return report.toString();
	}

}
