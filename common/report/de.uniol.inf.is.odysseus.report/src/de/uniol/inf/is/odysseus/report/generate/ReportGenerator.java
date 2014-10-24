package de.uniol.inf.is.odysseus.report.generate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportGenerator;
import de.uniol.inf.is.odysseus.report.IReportProvider;
import de.uniol.inf.is.odysseus.report.activator.ReportPlugIn;

public class ReportGenerator implements IReportGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(ReportGenerator.class);
	private static final String NO_REPORT_TEXT = "<no report available>";

	@Override
	public String generateReport(ISession session) {
		Preconditions.checkNotNull(session, "Session must not be null!");
		
		List<IReportProvider> providers = ReportPlugIn.getReportProviderRegistry().getSortedReportProviders();

		StringBuilder sb = new StringBuilder();

		for (IReportProvider provider : providers) {
			sb.append(generateTitle(provider)).append("\n");
			sb.append(generateReportText(session, provider));
			
			sb.append("\n\n");
		}

		return sb.toString();
	}
	
	@Override
	public String generateReport(ISession session, Throwable exception) {
		Preconditions.checkNotNull(session, "Session must not be null!");
		
		StringBuilder sb = new StringBuilder();
		
		if( exception != null ) {
			sb.append(getLine());
			sb.append("## Stack Trace\n");
			sb.append(getLine()).append("\n");
			sb.append(getStackTraceReport(exception));
			sb.append("\n\n");
		}
		
		sb.append(generateReport(session));
		
		return sb.toString();
	}

	private static StringBuilder getStackTraceReport(Throwable e) {
		final StringBuilder report = new StringBuilder();
		report.append("Message:\n").append(e.getMessage()).append("\n\n");
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement s : stack) {
			report.append("\tat ");
			report.append(s);
			report.append("\n");
		}
		report.append("\n");
		// cause
		Throwable throwable = e.getCause();
		while (throwable != null) {
			final StackTraceElement[] trace = throwable.getStackTrace();
			int m = trace.length - 1, n = stack.length - 1;
			while ((m >= 0) && (n >= 0) && trace[m].equals(stack[n])) {
				m--;
				n--;
			}
			final int framesInCommon = trace.length - 1 - m;

			report.append("Caused by: ").append(throwable.getClass().getSimpleName()).append(" - ").append(throwable.getMessage());
			report.append("\n\n");
			for (int i = 0; i <= m; i++) {
				report.append("\tat ").append(trace[i]).append("\n");
			}
			if (framesInCommon != 0) {
				report.append("\t... ").append(framesInCommon).append(" more\n");
			}
			throwable = throwable.getCause();
		}
		return report;
	}

	private static String generateTitle(IReportProvider provider) {
		StringBuilder sb = new StringBuilder();

		String title = provider.getTitle();

		sb.append(getLine());
		sb.append("## ").append(title).append("\n");
		sb.append(getLine());

		return sb.toString();
	}

	private static String getLine() {
		return "##########################################################################################\n";
	}

	private static String generateReportText(ISession session, IReportProvider provider) {
		String reportText = provider.getReport(session);
		if( Strings.isNullOrEmpty(reportText)) {
			reportText = NO_REPORT_TEXT;
			LOG.error("No report text from report provider '{}'", provider.getClass().getName());
		}
		
		return reportText;
	}

}
