package de.uniol.inf.is.odysseus.report.generate;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReport;
import de.uniol.inf.is.odysseus.report.IReportGenerator;
import de.uniol.inf.is.odysseus.report.IReportProvider;
import de.uniol.inf.is.odysseus.report.activator.ReportPlugIn;

public class ReportGenerator implements IReportGenerator {

	private static final String NO_REPORT_TEXT = "<no report available>";

	@Override
	public IReport generateReport(ISession session) {
		return generateReport(session, null);
	}

	@Override
	public IReport generateReport(ISession session, Throwable exception) {
		Preconditions.checkNotNull(session, "Session must not be null!");

		List<IReportProvider> providers = ReportPlugIn.getReportProviderRegistry().getSortedReportProviders();
		Map<String, String> reportMap = createReportMap(session, providers);

		return new Report(reportMap, exception);
	}

	private static Map<String, String> createReportMap(ISession session, List<IReportProvider> providers) {
		Map<String, String> reportMap = Maps.newHashMap();
		for (IReportProvider provider : providers) {

			String reportTitle = provider.getTitle();
			String reportText = "<could not be created!";
			try {
				reportText = provider.getReport(session);
				if (Strings.isNullOrEmpty(reportText)) {
					reportText = NO_REPORT_TEXT;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			reportMap.put(reportTitle, reportText);
		}
		return reportMap;
	}
}
