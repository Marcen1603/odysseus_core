package de.uniol.inf.is.odysseus.nexmark.generator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Der NEXMarkGenerator erzeugt eine Simulation der Streams person, auction und
 * bid. Er sendet ueber einen Stream {@link TupleContainer} die das Tupel und
 * den Typ des Tupels enthalten.
 * 
 * @author Bernd Hochschulz
 * 
 */
public class NEXMarkGenerator extends Thread {
	private NEXMarkGeneratorConfiguration configuration;

	private TupleStreamGenerator generator;

	private SimpleCalendar calender;

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private RelationalTuple<ITimeInterval> personTuple;
	private RelationalTuple<ITimeInterval> auctionTuple;
	private RelationalTuple<ITimeInterval> bidTuple;
	private RelationalTuple<ITimeInterval> currentTuple;

	private boolean burst = false;

	private NEXMarkBurstGenerator burstGenerator;;

	/**
	 * Erzeugt einen {@link NEXMarkGenerator}. Mittels getInputStream kann auf
	 * den Stream zugegriffen werden aus dem die Tupel gelesen werden koennen.
	 * Der Generator erstellt anhand der uebergebenen Konfiguration die Tupel.
	 * 
	 * @param configuration
	 *            - {@link NEXMarkGeneratorConfiguration} nach dem die Tupel
	 *            erzeugt werden sollen.
	 * @throws IOException
	 *             wenn die Streams zum senden der Tupel nicht erzeugt werden
	 *             koennen
	 */
	public NEXMarkGenerator(NEXMarkGeneratorConfiguration configuration,
			boolean deterministic) throws IOException {
		this.configuration = configuration;
		calender = new SimpleCalendar();
		generator = new TupleStreamGenerator(configuration, calender,
				deterministic);

		PipedInputStream pIn = new PipedInputStream();
		PipedOutputStream pOut = new PipedOutputStream();
		pIn.connect(pOut);

		outputStream = new ObjectOutputStream(pOut);
		inputStream = new ObjectInputStream(pIn);

		burstGenerator = new NEXMarkBurstGenerator(configuration.burstConfig,
				this, deterministic);
	}

	/**
	 * Bestimmt das naechste Tupel das gesendet werden soll. Das heisst das
	 * Tupel mit dem kleinsten Zeitstempel.
	 */
	private void setCurrentTuple() {
		currentTuple = personTuple;

		if ((Long) auctionTuple.getAttribute(0) < (Long) currentTuple
				.getAttribute(0)) {
			currentTuple = auctionTuple;
		}

		// falls kein Gebot erzeugt werden konnte, ignorieren
		if ((Long) bidTuple.getAttribute(0) < (Long) currentTuple
				.getAttribute(0)) {
			currentTuple = bidTuple;
		}

	}

	/**
	 * Gibt zurueck wie lange gewartet werden muss, bis das naechste Tupel
	 * gesendet werden soll.
	 * 
	 * @return wie lange gewartet werden muss.
	 */
	private long getTimeToWait() {
		long timeToWait = ((Long) currentTuple.getAttribute(0) / configuration.accelerationFactor)
				- calender.getTimeInMS();
		return timeToWait;
	}

	/**
	 * Wrappt das zu sendene Tupel in einen {@link TupleContainer} ein.
	 * 
	 * @return TupleContainer mit Tupel und Typ
	 * @throws CouldNotCreateAuction
	 *             wenn keine Auction erstellt werden konnte (kein Nutzer
	 *             vorhanden)
	 * @throws CouldNotCreateBid
	 *             wenn ein Gebot nicht erzeugt werden konnte (kein Nutzer oder
	 *             Auktion vorhanden)
	 */
	private synchronized TupleContainer createTupleContainer()
			throws CouldNotCreateAuction, CouldNotCreateBid {
		TupleContainer container = null;

		// System.out.println(" "+currentTuple+" "+personTuple+" "+auctionTuple+
		// " "+bidTuple);
		synchronized (generator) {

			// fuelle das Tupel von dem Typ was gesendet werden soll
			if (currentTuple == personTuple) {
				generator.fillPersonTuple(personTuple);
				container = new TupleContainer(personTuple,
						NEXMarkStreamType.PERSON);
				personTuple = generator.generateRawPersonTuple();

			} else if (currentTuple == auctionTuple) {
				// Es kann passieren, dass noch kein Nutzer registriert ist
				try {
					generator.fillAuctionTuple(auctionTuple);
				} catch (CouldNotGetExistingPersonId e) {
					throw new CouldNotCreateAuction();
				}
				container = new TupleContainer(auctionTuple,
						NEXMarkStreamType.AUCTION);
				auctionTuple = generator.generateRawAuctionTuple();

			} else if (currentTuple == bidTuple) {
				// es kann passieren dass ein Gebot nicht erzeugt werden
				// kann. Z.B. ist keine Auktion offen.
				try {
					generator.fillBidTuple(bidTuple);
				} catch (CouldNotGetExistingAuctionIdException e) {
					throw new CouldNotCreateBid();
				} catch (CouldNotGetExistingPersonId e) {
					throw new CouldNotCreateBid();
				}
				container = new TupleContainer(bidTuple, NEXMarkStreamType.BID);
				bidTuple = generator.generateRawBidTuple();
			} else {
				// sollte nie passieren
				// TODO exception?
			}
		}
		return container;
	}

	/**
	 * Generiert person, auction und bid Tupel und sendet sie den Zeitstempeln
	 * entsprechend ueber den Stream.
	 */
	@Override
	public void run() {
		calender.reset();

		if (burstGenerator.shouldGenerateBursts()) {
			burstGenerator.start();
		} else {
			burstGenerator = null;
		}

		// generiere die ersten Tupel
		synchronized (generator) {
			// == erstmal raus ==
			// Sicherstellen, dass es mindestens eine Person gibt
			// personTuple = generator.generateFirstRawPersonTuple();
			// ==================
			personTuple = generator.generateRawPersonTuple();
			auctionTuple = generator.generateRawAuctionTuple();
			bidTuple = generator.generateRawBidTuple();
		}

		setCurrentTuple();

		// generiere solange Tupel bis der Generator interrupted wird
		try {
			while (!interrupted()) { // && counter < limit
				// Wartezeit bestimmen
				long timeToWait = 0;
				synchronized (this) {
					timeToWait = getTimeToWait();
				}

				// und dem entsprechend warten
				if (timeToWait > 0) {
					long curTime = System.currentTimeMillis();
					long expectedTme = curTime + timeToWait;
					while (curTime < expectedTme) {
						try {
							long millis = expectedTme - curTime;
							Thread.sleep(millis);
							curTime = System.currentTimeMillis();
							// Thread.sleep(timeToWait);
						} catch (InterruptedException e) {
							break;
						}
					}
				}

				synchronized (this) {
					// falls ein Burst aufgetreten ist ignoriere was zu letzt
					// als zu sendendes Tupel angesehen wurde und bestimme
					// Wartezeit neu
					if (burst) {
						burst = false;
						// System.out.println("BURST");
						continue;
					}

					// fuelle zu sendendes Tupel
					TupleContainer container = null;
					try {
						container = createTupleContainer();
					} catch (CouldNotCreateBid e) {
//						e.printStackTrace();
						// falls kein Gebot erzeugt worden konnte versuche es
						// naechstes mal erneut
						synchronized (generator) {
							bidTuple = generator.generateRawBidTuple();
						}

						setCurrentTuple();
						continue;
					} catch (CouldNotCreateAuction e) {
//						e.printStackTrace();
						// falls keine Auktion erzeugt worden konnte versuche es
						// naechstes mal erneut
						synchronized (generator) {
							auctionTuple = generator.generateRawAuctionTuple();
						}

						setCurrentTuple();
						continue;
					}

					// sende Tupel
					outputStream.writeObject(container);
					outputStream.flush();

					// bestimme naechstes Tupel
					setCurrentTuple();
				}
			}
		} catch (IOException e) {
		} finally {
			if (burstGenerator != null) {
				burstGenerator.interrupt();
			}

			try {
				outputStream.close();
			} catch (IOException e) {
			}
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Aufrufen um dem Generator mitzuteilen, dass ein Burst eingetreten ist.
	 * Wird vom {@link NEXMarkBurstGenerator} aufgerufen.
	 * 
	 * @param time
	 *            - Zeit zu der der Burst eingetreten ist
	 */
	public void startBurst(long time) {
		// Um sofort neue Tupel im Burst zu senden, erstelle neue Tupel.
		synchronized (this) {
			synchronized (generator) {
				generator.startBurst(time);
				personTuple = generator.generateRawPersonTuple();
				auctionTuple = generator.generateRawAuctionTuple();
				bidTuple = generator.generateRawBidTuple();
			}

			setCurrentTuple();
			// Wenn der Generator gerade darauf wartet neue Tupel zu senden,
			// wecke ihn auf.
			if (this.getState() == Thread.State.TIMED_WAITING) {
				burst = true;
				this.notify();
			}
		}
	}

	/**
	 * Aufrufen um dem Generator mitzuteilen, dass ein Burst beendet wurde. Wird
	 * vom {@link NEXMarkBurstGenerator} aufgerufen.
	 * 
	 */
	public void stopBurst() {
		synchronized (generator) {
			generator.stopBurst();
		}

		// synchronized (this) {
		// synchronized (generator) {
		// generator.stopBurst();
		// personTuple = generator.generateRawPersonTuple();
		// auctionTuple = generator.generateRawAuctionTuple();
		// bidTuple = generator.generateRawBidTuple();
		// }
		// getCurrentTuple();
		// if (this.getState() == Thread.State.TIMED_WAITING) {
		// this.notify();
		// }
		// }
	}
}

class CouldNotCreateAuction extends Exception {
	private static final long serialVersionUID = 7205123255194069115L;
}

class CouldNotCreateBid extends Exception {
	private static final long serialVersionUID = -2241555793889688555L;
}
