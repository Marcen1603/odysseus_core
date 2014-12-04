package de.uniol.inf.is.odysseus.report.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
            List<Integer> logicalQueries = new ArrayList<>(executor.getLogicalQueryIds(session));
            Collections.sort(logicalQueries);
			for (Integer id : logicalQueries) {
				ILogicalQuery logicalQuery = executor.getLogicalQueryById(id.intValue(), session);
				report.append("\t* ").append(logicalQuery.getID()).append("\t").append(logicalQuery.getQueryText().replace('\n', ' ').replace('\r', ' ')).append("\n");
			}
			report.append("\n### Sinks:\n");
			List<SinkInformation> sinks = executor.getSinks(session);
            Collections.sort(sinks, new Comparator<SinkInformation>() {

                @Override
                public int compare(SinkInformation o1, SinkInformation o2) {
                    return o1.getName().compareTo(o2.getName());
                }

            });
			for (SinkInformation sink : sinks) {
				report.append("\t* ").append(sink.getName()).append(" (").append(sink.getOutputSchema()).append(")\n");
			}
			report.append("\n### Streams and Views:\n");
			List<ViewInformation> streams = executor.getStreamsAndViewsInformation(session);
            Collections.sort(streams, new Comparator<ViewInformation>() {

                @Override
                public int compare(ViewInformation o1, ViewInformation o2) {
                    return o1.getName().compareTo(o2.getName());
                }

            });
			for (ViewInformation stream : streams) {
				report.append("\t* ").append(stream.getName()).append(" (").append(stream.getOutputSchema()).append(")\n");
			}
			report.append("\n### Datatypes:\n");
            List<SDFDatatype> datatypes = new ArrayList<>(executor.getRegisteredDatatypes(session));
            Collections.sort(datatypes, new Comparator<SDFDatatype>() {

                @Override
                public int compare(SDFDatatype o1, SDFDatatype o2) {
                    return o1.getQualName().compareTo(o2.getQualName());
                }

            });
			for (SDFDatatype datatype : datatypes) {
				report.append("\t* ").append(datatype).append("\n");
			}
			report.append("\n### Wrappers:\n");
            List<String> wrappers = new ArrayList<>(executor.getRegisteredWrapperNames(session));
            Collections.sort(wrappers);
			for (String wrapper : wrappers) {
				report.append("\t* ").append(wrapper).append("\n");
			}
			report.append("\n### Schedulers:\n");
            List<String> schedulers = new ArrayList<>(executor.getRegisteredSchedulers(session));
            Collections.sort(schedulers);
			for (String scheduler : schedulers) {
				report.append("\t* ").append(scheduler).append("\n");
			}
			report.append("\n### Scheduling Strategies:\n");
            List<String> schedulingStrategies = new ArrayList<>(executor.getRegisteredSchedulingStrategies(session));
            Collections.sort(schedulingStrategies);
			for (String schedulingStrategy : schedulingStrategies) {
				report.append("\t* ").append(schedulingStrategy).append("\n");
			}
			report.append("\n### Parsers:\n");
            List<String> parsers = new ArrayList<>(executor.getSupportedQueryParsers(session));
            Collections.sort(parsers);
			for (String parser : parsers) {
				report.append("\t* ").append(parser).append("\n");
			}
			report.append("\n### Operators:\n");
			List<String> operators = executor.getOperatorNames(session);
            Collections.sort(operators);
			for (String operator : operators) {
				report.append("\t* ").append(operator).append("\n");
			}
		} else {
			report.append(NO_EXECUTOR_AVAILABLE_TEXT);
		}
		
		return report.toString();
	}

}
