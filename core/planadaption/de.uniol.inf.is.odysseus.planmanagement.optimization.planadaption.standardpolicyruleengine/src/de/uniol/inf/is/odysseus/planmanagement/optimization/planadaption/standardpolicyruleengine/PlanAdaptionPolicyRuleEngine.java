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

package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardpolicyruleengine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionPolicyListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionPolicyRuleEngine;

/**
 * Timed implementation
 * 
 * @author Merlin Wasmann
 * 
 */
public class PlanAdaptionPolicyRuleEngine implements
		IPlanAdaptionPolicyRuleEngine, ActionListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(PlanAdaptionPolicyRuleEngine.class);

	private Timer timer;
	private long blockedTime;
	private long wait;
	private Set<IPlanAdaptionPolicyListener> listener;

	public PlanAdaptionPolicyRuleEngine() {
		this.listener = new HashSet<IPlanAdaptionPolicyListener>();
	}

	/**
	 * starts the rule engine with a blockedTime and a waiting time
	 * 
	 * @param blockedTime
	 *            Blocks any adaption after an adaption for this long.
	 * @param wait
	 *            Waits this long before each new adaption attempt.
	 */
	public void initialize(long blockedTime, long wait) {
		this.wait = wait;
		this.blockedTime = blockedTime;
		this.timer = new Timer((int) wait, this);
		timer.setRepeats(true);
	}

	public void setWaitingTime(long wait) {
		this.wait = wait;
	}

	public long getWait() {
		return this.wait;
	}

	public Timer getTimer() {
		return this.timer;
	}

	@Override
	public long getBlockedTime() {
		return this.blockedTime;
	}

	@Override
	public void fireAdaptionStartEvent() {
		for (IPlanAdaptionPolicyListener listener : this.listener) {
			listener.adaptionEventFired(this);
		}
	}

	@Override
	public void register(IPlanAdaptionPolicyListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void unregister(IPlanAdaptionPolicyListener listener) {
		if (this.listener.contains(listener)) {
			this.listener.remove(listener);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		LOG.debug("Timer has fired");
		fireAdaptionStartEvent();
	}

	@Override
	public void stop() {
		LOG.debug("Stopping");
		this.timer.stop();
	}

	@Override
	public void start() {
		if (this.timer.isRunning()) {
			LOG.debug("Is already running");
			return;
		}
		LOG.debug("Starting");
		this.timer.setInitialDelay((int) this.blockedTime);
		this.timer.start();
	}

}
