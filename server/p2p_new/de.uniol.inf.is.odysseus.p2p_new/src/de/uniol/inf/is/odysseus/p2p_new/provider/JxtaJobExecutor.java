package de.uniol.inf.is.odysseus.p2p_new.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JxtaJobExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaJobExecutor.class);
	private static final int JOBS_PER_BATCH = 5;
	
	private final List<IJxtaJob> pendingJobs = Lists.newLinkedList();
	private final JxtaJobExecutionThread jobThread;
	
	public JxtaJobExecutor() {
		jobThread = new JxtaJobExecutionThread(this);
	}
	
	public void start() {
		jobThread.start();
	}
	
	public void executeJobs() {
		synchronized( pendingJobs ) {
			
			int jobsExecuted = 0;
			while( jobsExecuted < JOBS_PER_BATCH && !pendingJobs.isEmpty()) {
				
				IJxtaJob pendingJob = pendingJobs.remove(0);
				tryExecuteJob(pendingJob);
				
				jobsExecuted++;
			}
			
		}
	}
	
	private static void tryExecuteJob(IJxtaJob pendingJob) {
		try {
			pendingJob.execute();
		} catch( Throwable t ) {
			LOG.error("Could not execute pending job", t);
		}
	}

	public void stop() {
		jobThread.stopRunning();
		
		synchronized(pendingJobs ) {
			pendingJobs.clear();
		}
	}
	
	public void addJob( IJxtaJob job ) {
		Preconditions.checkNotNull(job, "JxtaJob to add must not be null!");
		
		synchronized(pendingJobs ) {
			pendingJobs.add(job);
		}
	}
	
	public void removeJob( IJxtaJob job ) {
		Preconditions.checkNotNull(job, "JxtaJob to remove must not be null!");
		
		synchronized(pendingJobs) {
			pendingJobs.remove(job);
		}
	}
	
	public int getPendingJobCount() {
		synchronized(pendingJobs){
			return pendingJobs.size();
		}
	}
}
