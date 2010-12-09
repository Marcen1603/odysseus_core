package de.uniol.inf.is.odysseus.nexmark.generator;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Der TupleStreamGenerator erzeugt auf Anfrage Tupel vom Typ person, auction
 * oder bid. Dabei werden die Tupel mit Zeitstempeln versehen die mittels der
 * Konfiguration erstell werden.
 * 
 * @author Bernd Hochschulz
 * 
 */
public class TupleStreamGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger( TupleStreamGenerator.class );
	
	private static final int personAttributeCount = 7;
	private static final int auctionAttributeCount = 9;
	private static final int bidAttributeCount = 5;

	private Random rnd;

	private SimpleCalendar calender;

	private NEXMarkGeneratorConfiguration configuration;

	private long lastPersonGenerated = -1;
	private long lastAuctionGenerated = -1;
	private long lastBidGenerated = -1;

	private Persons persons = new Persons();
	private OpenAuctions openAuctions;
	private PersonGen personGenerator = new PersonGen();
	private int burstAcceleration = 1;

	/**
	 * Erzeugt einen neuen {@link TupleStreamGenerator}. Ist der Parameter
	 * determisitic true wird der Zufallsgenerator immer mit dem selben Seed
	 * initialisiert. So kann der {@link NEXMarkGenerator} die Tupel
	 * determisitisch erzeugen.
	 * 
	 * @param configuration
	 *            - die Konfiguration anhand die Zeitstempel erzeugt werden
	 *            sollen.
	 * @param calender
	 *            - der Kalender mit dem die Zeitpunkte bestimmt werden.
	 * @param deterministic
	 *            - ob der Zufallsgenerator mit festem Seed initialisert werden
	 *            soll.
	 */
	public TupleStreamGenerator(NEXMarkGeneratorConfiguration configuration,
			SimpleCalendar calender, boolean deterministic) {
		logger.debug("New Tuple Stream Generator created");
		this.configuration = configuration;
		if (deterministic) {
			rnd = new Random(103984);
		} else {
			rnd = new Random();
		}

		this.calender = calender;
		openAuctions = new OpenAuctions(calender, configuration);
	}

	/**
	 * Generiert eine Zufallszahl im Interval [lastTime + min, lastTime + max]
	 * 
	 * @param lastTime
	 * @param min
	 * @param max
	 * @return erzeugte Zufallszahl
	 */
	private long getTimeToSend(long lastTime, int min, int max) {
		int interval = (max - min);
		long timeToSend = lastTime + ((rnd.nextInt(interval + 1) + min) / burstAcceleration);

		return timeToSend;
	}

//	private void setAttribute(RelationalTuple<ITimeInterval> tuple, int pos, int value) {
//		tuple.setAttribute(pos, value);
//	}
//
//	private void setAttribute(RelationalTuple<ITimeInterval> tuple, int pos, double value) {
//		tuple.setAttribute(pos, value);
//	}
//
//	private void setAttribute(RelationalTuple<ITimeInterval> tuple, int pos, long value) {
//		tuple.setAttribute(pos, value);
//	}
//
//	private void setAttribute(RelationalTuple<ITimeInterval> tuple, int pos, String value) {
//		tuple.setAttribute(pos, value);
//	}
//
//	private void setAttribute(RelationalTuple<ITimeInterval> tuple, int pos, CharBuffer value) {
//		String stringValue = new String(value.array(), 0, value.position());
//		tuple.setAttribute(pos, stringValue);
//	}

	/**
	 * Generiert ein initiales leeres person Tupel mit timestamp 1
	 * 
	 * @return das generierte Tupel
	 */
	public RelationalTuple<ITimeInterval> generateFirstRawPersonTuple() {
		RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(
				personAttributeCount);

		// time to send
		long timeToSend = 1;
		lastPersonGenerated = timeToSend;
		tuple.setAttribute( 0, timeToSend);
		// logger.debug("GenerateFirstRawPersonTuple "+tuple);

		return tuple;
	}

	/**
	 * Generiert ein leeres person Tupel mit timestamp
	 * 
	 * @return das generierte Tupel
	 */
	public RelationalTuple<ITimeInterval> generateRawPersonTuple() {
		RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(
				personAttributeCount);

		// time to send
		long timeToSend = getTimeToSend(lastPersonGenerated, configuration.minDistBetweenPersons,
				configuration.maxDistBetweenPersons);
		lastPersonGenerated = timeToSend;
		tuple.setAttribute( 0, timeToSend);
		//logger.debug("GenerateRawPersonTuple "+tuple);

		return tuple;
	}

	/**
	 * Fuellt das uebergebene (mit {@link #generateRawPersonTuple()} erzeugte)
	 * Tupel mit Werten
	 * 
	 */
	public void fillPersonTuple(RelationalTuple<ITimeInterval> tuple) {
		personGenerator.generateValues(openAuctions);

		// id
		tuple.setAttribute( 1, persons.getNewId());

		// name
		tuple.setAttribute( 2, personGenerator.m_stName);

		// email
		tuple.setAttribute( 3, personGenerator.m_stEmail);

		// creditcard
		if (personGenerator.has_creditcard) {
			tuple.setAttribute( 4, personGenerator.m_stCreditcard);
		}

		if (personGenerator.has_address) {
			// city
			tuple.setAttribute( 5, personGenerator.m_address.m_stCity);

			// state
			tuple.setAttribute( 6, personGenerator.m_address.m_stProvince);

		}
	}

	/**
	 * Generiert ein leeres auction Tupel mit timestamp
	 * 
	 * @return das generierte Tupel
	 */
	public RelationalTuple<ITimeInterval> generateRawAuctionTuple() {
		RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(
				auctionAttributeCount);

		// time to send
		long timeToSend = getTimeToSend(lastAuctionGenerated==-1?lastPersonGenerated:lastAuctionGenerated, configuration.minDistBetweenAuctions,
				configuration.maxDistBetweenAuctions);
		lastAuctionGenerated = timeToSend;
		tuple.setAttribute( 0, timeToSend);
		//logger.debug("generateRawAuctionTuple "+tuple);

		return tuple;
	}

	/**
	 * Fuellt das uebergebene (mit {@link #generateRawAuctionTuple()} erzeugte)
	 * Tupel mit Werten
	 * 
	 * @throws CouldNotGetExistingPersonId
	 *             wenn versucht wird eine Auktion zu erstellen aber es keinen
	 *             Nutzer gibt der es erstellt haben koennte
	 * 
	 */
	public void fillAuctionTuple(RelationalTuple<ITimeInterval> tuple)
			throws CouldNotGetExistingPersonId {
		// Frage jetzt schon Person id an, da hier bereits eine Exception
		// geworfen werden kann.
		// Wenn erst eine Auktion erstellt wurde und danach eine
		// CouldNotGetExistingPersonId Exception geworfen wird, befindet sich
		// eine falsche Auktion im System.
		int personId = persons.getExistingId();
		OpenAuction auction = openAuctions.getNewAuction();

		// id
		tuple.setAttribute(1, auction.id);

		// itemname
		tuple.setAttribute(2, "Item Number " + auction.id);

		// description
		tuple.setAttribute(3, "No description available.");

		// initialbid
		tuple.setAttribute(4, auction.getCurrPrice());

		// reserve
		tuple.setAttribute(5, (int) Math.round((auction.getCurrPrice())
				* (1.2 + (rnd.nextDouble() + 1))));

		// expires
		tuple.setAttribute(6, auction.getEndTime());

		// seller
		tuple.setAttribute(7, personId);

		// category
		int catid = rnd.nextInt(Categories.CATEGORY_COUNT);
		tuple.setAttribute(8, catid);
	}

	/**
	 * Generiert ein leeres bid Tupel mit timestamp
	 * 
	 * @return das generierte Tupel
	 */
	public RelationalTuple<ITimeInterval> generateRawBidTuple() {
		RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(bidAttributeCount);

		// time to send
		long timeToSend = getTimeToSend(lastBidGenerated==-1?lastAuctionGenerated:lastBidGenerated, configuration.minDistBetweenBids,
				configuration.maxDistBetweenBids);
		lastBidGenerated = timeToSend;
		tuple.setAttribute(0, timeToSend);
		//logger.debug("generateRawBidTuple "+tuple);
		return tuple;
	}

	/**
	 * Fuellt das uebergebene (mit {@link #generateRawBidTuple()} erzeugte)
	 * Tupel mit Werten
	 * 
	 * 
	 * @throws CouldNotGetExistingAuctionIdException
	 *             wenn kein Gebot erzeugt werden konnte. Z.B sind keine offenen
	 *             Auktionen.
	 * @throws CouldNotGetExistingPersonId
	 *             wenn versucht wird eine Auktion zu erstellen aber es keinen
	 *             Nutzer gibt der es erstellt haben koennte
	 */
	public void fillBidTuple(RelationalTuple<ITimeInterval> tuple)
			throws CouldNotGetExistingAuctionIdException, CouldNotGetExistingPersonId {
		OpenAuction auction = openAuctions.getExistingAuction();
		int personId = persons.getExistingId();

		// auction
		tuple.setAttribute(1, auction.id);

		// bidder
		tuple.setAttribute(2, personId);

		// datetime
		tuple.setAttribute(3, calender.getTimeInMS());

		// price
		tuple.setAttribute(4, auction.increasePrice());
	}

	/**
	 * Startet einen Burst. Tupel waehrend eines Bursts werden schneller
	 * erzeugt. Beschleunigungsfaktor wird in der
	 * {@link NEXMarkBurstConfiguration} festgelegt.
	 * 
	 * @param time
	 *            - Zeit zu der der Burst anfaengt
	 */
	public void startBurst(long time) {
		// Da Tupel bereits beschleunigt gesendet werden koennen finde
		// "richtige"
		// Zeit herraus
		time *= configuration.accelerationFactor;
		lastPersonGenerated = time;
		lastAuctionGenerated = time;
		lastBidGenerated = time;

		burstAcceleration = configuration.burstConfig.burstAccelerationFactor;

	}

	/**
	 * Stoppt einen Burst. Tupel werden nun wieder normal erzeugt.
	 */
	public void stopBurst() {
		burstAcceleration = 1;
	}
}
