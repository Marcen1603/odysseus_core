package de.uniol.inf.is.odysseus.ac.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;

/**
 * Ruft regelmäßig die Admission Control auf, um die Kostenschätzungen
 * zu aktualisieren. Dies geschieht asynchron in einem eigenen Thread.
 * 
 * @author Timo Michelsen
 *
 */
public class ACUpdater extends Thread {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ACUpdater.class);
		}
		return _logger;
	}

	private static final long UPDATE_INTERVAL = 5000;

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
			t.printStackTrace();
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
