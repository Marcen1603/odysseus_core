package de.uniol.inf.is.odysseus.test.component.parallelization;

import java.net.URL;

public class ParallelizationTest {
	private URL queryFile;
	private URL resultFile;
	private boolean enabled;

	public ParallelizationTest(String queryFile, String resultFile,
			boolean enabled) {
		super();
		this.setQueryFile(getURL(queryFile));
		this.setResultFile(getURL(resultFile));
		this.enabled = enabled;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private URL getURL(String file) { 
		return Activator.getContext().getBundle().getEntry(file);
	}


	public URL getQueryFile() {
		return queryFile;
	}


	public void setQueryFile(URL queryFile) {
		this.queryFile = queryFile;
	}


	public URL getResultFile() {
		return resultFile;
	}


	public void setResultFile(URL resultFile) {
		this.resultFile = resultFile;
	}
}

