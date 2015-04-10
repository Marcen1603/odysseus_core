package de.uniol.inf.is.odysseus.peer.jxta.impl;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class JxtaJobExecutionThread extends RepeatingJobThread {

	private final JxtaJobExecutor jobExecutor;
	
	public JxtaJobExecutionThread(JxtaJobExecutor jobExecutor) {
		super(2000, "Jxta Job Execution");
		Preconditions.checkNotNull(jobExecutor, "JxtaJobExecutor must not be null!");
		
		this.jobExecutor = jobExecutor;
	}
	
	@Override
	public void doJob() {
		jobExecutor.executeJobs();
	}
}
