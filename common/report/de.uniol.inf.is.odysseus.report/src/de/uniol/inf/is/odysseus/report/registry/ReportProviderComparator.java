package de.uniol.inf.is.odysseus.report.registry;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ReportProviderComparator implements Comparator<IReportProvider> {

	public static final ReportProviderComparator INSTANCE = new ReportProviderComparator();
	
	@Override
	public int compare(IReportProvider provider1, IReportProvider provider2) {
		if( provider1 == null && provider2 == null ) {
			return 0;
		}
		
		if( provider1 == null ) {
			return 1;
		}
		if( provider2 == null ) {
			return -1;
		}
		
		return Integer.compare(provider1.getPriority(), provider2.getPriority());
	}

}
