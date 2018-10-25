package de.uniol.inf.is.odysseus.report.generate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.report.IReport;

public class Report implements IReport {

	private final Map<String, String> reportMap = Maps.newHashMap();
	private final Optional<Throwable> optException;
	
	public Report( Map<String, String> reportMap, Throwable exception ) {
		Objects.requireNonNull(reportMap, "ReportMap must not be null!");
		
		this.reportMap.putAll(reportMap);
		optException = Optional.fromNullable(exception);
	}
	
	@Override
	public Collection<String> getTitles() {
		return Lists.newArrayList(reportMap.keySet());
	}

	@Override
	public Optional<String> getReportText(String title) {
		return Optional.fromNullable(reportMap.get(title));
	}

	@Override
	public Optional<Throwable> getException() {
		return optException;
	}
	
	@Override
	public Map<String, String> getReportMap() {
		return new HashMap<>(reportMap);
	}
}
