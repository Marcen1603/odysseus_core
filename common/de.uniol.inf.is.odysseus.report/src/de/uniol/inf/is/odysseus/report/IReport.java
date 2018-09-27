package de.uniol.inf.is.odysseus.report;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;

public interface IReport {

	Optional<Throwable> getException();
	
	Collection<String> getTitles();
	Optional<String> getReportText( String title );
	
	Map<String, String> getReportMap();
	
	
}
