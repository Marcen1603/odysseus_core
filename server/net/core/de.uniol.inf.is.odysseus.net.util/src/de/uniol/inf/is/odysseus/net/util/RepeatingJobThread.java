/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.net.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class RepeatingJobThread extends StoppableThread {

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		}
	});
	
	private static final String DEFAULT_THREAD_NAME = "Repeating job";

	private long executionIntervalMillis;
	private String threadName;

	private long lastExecutionTimestamp = 0;

	public RepeatingJobThread() {
		this(0);
	}

	public RepeatingJobThread(long executionIntervalMillis) {
		this(executionIntervalMillis, DEFAULT_THREAD_NAME);
	}

	public RepeatingJobThread(long executionIntervalMillis, String threadName) {
		Preconditions.checkArgument(executionIntervalMillis >= 0, "Execution interval must be zero or positive!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(threadName), "Thread name must not be null or empty!");

		this.executionIntervalMillis = executionIntervalMillis;

		this.threadName = threadName;
		
		
	}

	public void afterJob() {
	}

	public void beforeJob() {
	}

	public void doJob() {
	}

	@Override
	public final void run() {
		Thread.currentThread().setName("RepeatingThread: " + threadName);
		
		beforeJob();
		while (isRunning()) {
			lastExecutionTimestamp = System.currentTimeMillis();
			doJob();

			if (executionIntervalMillis > 0) {
				trySleep(executionIntervalMillis);
			}
		}
		Thread.currentThread().setName(Thread.currentThread().getName() + " (stopped)");
		afterJob();
	}
	
	public final void start() {
		EXECUTOR_SERVICE.execute(this);
	}

	protected final long getIntervalMillis() {
		return executionIntervalMillis;
	}
	
	protected final void setIntervalMillis( long intervalMillis) {
		Preconditions.checkArgument(intervalMillis >= 0, "Interval must be non-negative");
		
		executionIntervalMillis = intervalMillis;
	}

	protected final long getLastExecutionTimestamp() {
		return lastExecutionTimestamp;
	}

	private static void trySleep(long lengthMillis) {
		try {
			Thread.sleep(lengthMillis);
		} catch (final InterruptedException ex) {
		}
	}

	protected String getThreadName() {
		return threadName;
	}
}
