package de.uniol.inf.is.odysseus.report.executor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ExecutorReportProvider implements IReportProvider {

	private static final String NO_EXECUTOR_AVAILABLE_TEXT = "<no executor available>";

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Odysseus Report";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder report = new StringBuilder();
		
		IExecutor executor = ExecutorBugReportPlugIn.getServerExecutor();
		
		if (executor != null) {
			
			report.append("Used executor instance: ").append(executor).append("\n");
			report.append("Used executor class   : ").append(executor.getClass()).append("\n");
			report.append("\n");
			
			report.append("\n### Queries:\n");
			Collection<Integer> logicalQueries = executor.getLogicalQueryIds(session);
			for (Integer id : logicalQueries) {
				ILogicalQuery logicalQuery = executor.getLogicalQueryById(id.intValue(), session);
				report.append("\t* ").append(logicalQuery.getID()).append("\t").append(logicalQuery.getQueryText().replace('\n', ' ').replace('\r', ' ')).append("\n");
			}
			report.append("\n### Sinks:\n");
			List<SinkInformation> sinks = executor.getSinks(session);
			for (SinkInformation sink : sinks) {
				report.append("\t* ").append(sink.getName()).append(" (").append(sink.getOutputSchema()).append(")\n");
			}
			report.append("\n### Streams and Views:\n");
			List<ViewInformation> streams = executor.getStreamsAndViewsInformation(session);
			for (ViewInformation stream : streams) {
				report.append("\t* ").append(stream.getName()).append(" (").append(stream.getOutputSchema()).append(")\n");
			}
			report.append("\n### Datatypes:\n");
			Set<SDFDatatype> datatypes = executor.getRegisteredDatatypes(session);
			for (SDFDatatype datatype : datatypes) {
				report.append("\t* ").append(datatype).append("\n");
			}
			report.append("\n### Wrappers:\n");
			Set<String> wrappers = executor.getRegisteredWrapperNames(session);
			for (String wrapper : wrappers) {
				report.append("\t* ").append(wrapper).append("\n");
			}
			report.append("\n### Schedulers:\n");
			Set<String> schedulers = executor.getRegisteredSchedulers(session);
			for (String scheduler : schedulers) {
				report.append("\t* ").append(scheduler).append("\n");
			}
			report.append("\n### Scheduling Strategies:\n");
			Set<String> schedulingStrategies = executor.getRegisteredSchedulingStrategies(session);
			for (String schedulingStrategy : schedulingStrategies) {
				report.append("\t* ").append(schedulingStrategy).append("\n");
			}
			report.append("\n### Parsers:\n");
			Set<String> parsers = executor.getSupportedQueryParsers(session);
			for (String parser : parsers) {
				report.append("\t* ").append(parser).append("\n");
			}
			report.append("\n### Operators:\n");
			List<String> operators = executor.getOperatorNames(session);
			for (String operator : operators) {
				report.append("\t* ").append(operator).append("\n");
			}
		} else {
			report.append(NO_EXECUTOR_AVAILABLE_TEXT);
		}
		
		return report.toString();
	}

}
