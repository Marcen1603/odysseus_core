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
package de.uniol.inf.is.odysseus.ac.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;


/**
 * Ruft regelmäßig die Admission Control auf, um die Kostenschätzungen
 * zu aktualisieren. Dies geschieht asynchron in einem eigenen Thread.
 * 
 * @author Timo Michelsen
 *
 */
public class ACUpdater extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ACUpdater.class);

	private static final long UPDATE_INTERVAL = 10000;

	private IAdmissionControl ac;
	private boolean isRunning = true;
	
	/**
	 * Konstruktor, erstellt eine neue {@link ACUpdater}-Instanz 
	 * mit gegebener Admission Control, dessen Kostenschätzungen
	 * regelmäßig aktualisiert werden sollen. Gestartet wird mit
	 * <code>startRunning()</code> der Klasse.
	 * 
	 * @param ac Admission Conrtrol
	 */
	public ACUpdater(IAdmissionControl ac) {
		this.ac = ac;
		this.setName("ACUpdater");
	}

	@Override
	public void run() {
		try {
			while (isRunning) {

				ac.updateEstimations();
				
				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException ex) {
				}
				
				
			}
		} catch( Throwable t ) {
		    LOGGER.error("Exception during running ACUpdater", t);
		}
	}

	/**
	 * Stoppt die automatische Aktualisierung
	 * der Kostenschätzungen.
	 */
	public void stopRunning() {
		isRunning = false;
	}

	/**
	 * Startet die automatische Aktualisierung
	 * der Kostenschätzungen.
	 */
	public void startRunning() {
		isRunning = true;
		if (!isAlive())
			start();
	}
}
