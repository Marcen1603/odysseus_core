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

package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class RepeatingJobThread extends Thread{

	private static final String DEFAULT_THREAD_NAME = "Repeating job";

	private final int executionIntervalMillis;

	private boolean isRunning = true;

	public RepeatingJobThread(int executionIntervalMillis) {
		this(executionIntervalMillis, DEFAULT_THREAD_NAME);
	}

	public RepeatingJobThread(int executionIntervalMillis, String threadName) {
		Preconditions.checkArgument(executionIntervalMillis > 0, "Discover interval for other peers must be positive!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(threadName), "Thread name must not be null or empty!");

		this.executionIntervalMillis = executionIntervalMillis;

		setName(threadName);
		setDaemon(true);
	}

	@Override
	public final void run() {
		beforeJob();
		while (isRunning) {
			doJob();
			trySleep(executionIntervalMillis);
		}
		afterJob();
	}

	public void beforeJob() {
	}

	public void doJob() {
	}

	public void afterJob() {
	}

	public final void stopRunning() {
		isRunning = false;
	}

	private static void trySleep(int lengthMillis) {
		try {
			Thread.sleep(lengthMillis);
		} catch (InterruptedException ex) {
		}
	}

}
