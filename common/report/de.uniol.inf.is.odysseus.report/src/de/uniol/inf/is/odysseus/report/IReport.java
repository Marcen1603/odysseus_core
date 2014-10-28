package de.uniol.inf.is.odysseus.report;

import java.util.Collection;

import com.google.common.base.Optional;

public interface IReport {

	public Optional<Throwable> getException();
	
	public Collection<String> getTitles();
	public Optional<String> getReportText( String title );
	
}
