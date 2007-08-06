/*
 * Created on 19.10.2005
 *
 */
package mg.dynaquest.evaluation.testenv.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import mg.dynaquest.evaluation.testenv.settings.DataSourceProperties;
import mg.dynaquest.evaluation.testenv.shared.misc.Tuple;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDataSource;

/**
 * Die eigentliche simulierte Datenquelle. Liefert den Clients �ber <i>RMI</i> Ergebnistupel( {@link Tuple  Tuple} ), die ihr bei ihrer Konstruktion durch einen Datenquellen-Manager( {@link DataSourceManager  DataSourceManager} ) �bergeben wurden.  <p>  Das Lieferungsverhalten der Datenquelle ist in einem <code>DataSourceProperties</code>- Objekt gekapselt. Aus den verschiedenen Konfigurationen berechnet die Quelle  in Abh�ngigkeit derer Priorit�ten die aktuell g�ltige und liefert ihre Tupel entsprechend. <p> Angenommen es g�be zwei Kombinationen, conf_0 und conf_1, mit den Priorit�ten 0 und 1. conf_0 legt fest, das jeden Mittwoch zwischen 14:00 und 16:30 Uhr ein totaler Ausfall der Quelle eintritt, w�rend conf_1 f�r 24 Stunden an jedem Tag (default-Konfiguration) eine  Datenlieferung mit einem Tupelabstand von 500 ms definiert. Die Quelle w�rde sich also  immer gem�� conf_1 verhalten, bis auf Mittwoch von 14:00 - 16:30 Uhr. Dort w�rde conf_0 aufgrund ihrer h�heren Priorit�t conf_1 vollst�ndig �berdecken und die Quelle fiele f�r diesen Zeitraum aus.  <p> So k�nnen beliebig viele Konfiguration mit unterschiedlicher Periodizit�t und unterschiedlichem Verhalten definiert werden, wenn sie sich zeitlich �berschneiden gilt immer die mit der h�chsten Priorit�t.  <p> Zu den Details der Konfigurationen siehe auch TODO link zu dqt_server_conf.readme.txt einf�gen
 * @see settings.DataSourceProperties
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class DataSource extends UnicastRemoteObject implements RemoteDataSource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 69437751039997888L;

	/**
	 * Erh�ht die Typsicherheit bei Serialisierung
	 * @uml.property  name="name"
	 */
//	public static final long serialVersionUID = 2015576995119203547L;
	
	//Name mit dem diese Datenquelle gebunden wurde
	private String name;
	
	// die Tupel, die von dieser Datenquelle geliefert werden
	/**
	 * @uml.property  name="data"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Tuple[] data = null;
	// Eigenschaften dieser Datenquelle
	/**
	 * @uml.property  name="properties"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private DataSourceProperties properties;
	
	// Index des zur Zeit g�ltigen Satzes von Einstellungen
	/**
	 * @uml.property  name="configIndex"
	 */
	private int configIndex;
	// Zeitpunkt, seit dem die aktuelle Konfiguration gilt
	/**
	 * @uml.property  name="configurationStart"
	 */
	private long configurationStart = 0;
	// absoluter Zeitpunkt, an dem das aktuelle Verhalten endet und ein neues berechnet werden muss
	/**
	 * @uml.property  name="configurationExpires"
	 */
	private long configurationExpires = 0;
	// geplante absolute Zeit, zu der das n�chste Tupel geliefert werden soll, 
	// wird von computeSleepTime() zur Ausrichtung benutzt
	/**
	 * @uml.property  name="perfectTime"
	 */
	private long perfectTime = 0;
	

	// Index der n�chsten zu liefernden Tupels
    /**
	 * @uml.property  name="tupleIndex"
	 */
    private int tupleIndex = 0;
    // Anzahl der Tupel
    /**
	 * @uml.property  name="tupleNumber"
	 */
    private int tupleNumber;
    // Gibt an ob gerade das allererste Tupel geliefert wird.
    /**
	 * @uml.property  name="veryFirstTuple"
	 */
    private boolean veryFirstTuple = true;
		
	// ausf�hrliche Meldungen?
	/**
	 * @uml.property  name="dEBUG"
	 */
	private boolean DEBUG = true; 
	// Zur Ausgabe der Zeiten
	/**
	 * @uml.property  name="df"
	 */
	private SimpleDateFormat df;
	
	
	/**
	 * Erzeugt eine neue Datenquelle mit den �bergebenen Tupeln und Einstellungen.
	 * Soll nur vom Datenquellen-Manager ({@link DataSourceManager DataSourceManager})
	 * verwendet werden.
	 *  
	 * @param data  die Tupel, die diese Datenquelle liefern kann
	 * @param dsp bestimmt das Verhalten dieser Datenquelle  
	 * @throws RemoteException
	 */
	 DataSource(Tuple[] data, DataSourceProperties dsp) throws RemoteException  {
		super();
		this.data = data;
		tupleNumber = data.length;
		this.properties = dsp;
		configurationExpires = System.currentTimeMillis();
		String pattern = "EEE, d'.' MMM yyyy',' HH:mm:ss'.'SSS";
		df = new SimpleDateFormat(pattern);
	}
    
	
	/**
	 * Liefert das n�chste Tupel, f�ngt mit wieder mit dem ersten an,
	 * wenn alle geliefert wurden.
	 * 
	 * @return das n�chste Tupel ({@link Tuple Tuple})
	 * 
	 */
	public Tuple getNext () throws RemoteException {
		// f�ngt von vorne an falls Maximalwert erreicht und entspr. konfiguriert
		if (tupleIndex == tupleNumber && properties.isRepeating())
			tupleIndex = 0;
		// muss das Verhalten neu berechnet werden?
		long now = System.currentTimeMillis();
		if (now > configurationExpires)  { 
			if (DEBUG)  {
				System.out.println("\nDataSource.getNext: Zeit: " + df.format(new Date(now)) +
						" ,berechne neues Verhalten");
			}
			computeBehavior();
		}
		// So lange aufeinander folgende Ausfall-Perioden definiert sind,
		// wird nichts geliefert
		while (properties.getBreakdown(configIndex) == true) {
			// Bis zum Ende der Konfiguration schlafen
			now = System.currentTimeMillis();
			try {
				Thread.sleep(configurationExpires - now);
			} catch(Exception e)  {
				System.out.println("DataSource.getNext: Exception w�hrend sleep() :");
				e.printStackTrace();
			}
			if (DEBUG)
				System.out.println("\nDataSource.getNext: Zeit: " + df.format(new Date()) +
					" ,berechne neues Verhalten");
			computeBehavior();
		}
		try  {
			long sleepTime = computeSleepTime();
			if (sleepTime > 0)  {
				
				Thread.sleep(sleepTime);
			}	
		}   catch (Exception e)  {
			System.out.println("DataSource.getNext: Exception w�hrend sleep(): ");
			e.printStackTrace();
		}
		//Date d = new Date();
		//System.out.println("DataSource.getNext: Liefere Tupel Nr. " + tupleIndex + ", " +
		//		"Zeit = " + df.format(d));
		if (data.length > tupleIndex){
		    return data[tupleIndex ++];
        }else{
            return null;
        }
	}
	
	/**
	 * Gibt an, ob diese Datenquelle f�r eine st�ndige Wiederholung
	 * der Daten konfiguriert ist. Ist dies der Fall, muss
	 * der Client vor Anforderung des n�chsten Tupels nicht 
	 * jedes Mal #hasMore() aufrufen.
	 * 
	 * @return <code>true</code>, wenn diese Datenquelle ihre Daten in
	 * 			st�ndiger Wiederholung sendet, ansonsten <code>false</code>
	 * @throws RemoteException
	 */
	public boolean isRepeating() throws RemoteException  {
		return properties.isRepeating() && tupleNumber > 0;
	}
	
	/**
	 * Gibt an, ob diese Quelle noch mehr Tupel liefert. Falls <code>repeat</code> gesetzt ist,
	 * ist der R�ckgabewert immer <code>true</code>.
	 * 
	 * @see DataSourceProperties#isRepeating()
	 */
	public boolean hasMore() throws RemoteException  {
		return tupleIndex < tupleNumber || properties.isRepeating();
	}
	
	/**
	 * Gibt den Status der Quelle an.
	 * 
	 * @return eine kurze Beschreibung des aktuellen Lieferverhaltens
	 *         bzw. eine Meldung �ber
	 * 		   Ausfallgrund und -zeitpunkt
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException  {
		if (tupleIndex == tupleNumber && !properties.isRepeating())  {
			return "Lieferung beendet, alle " + tupleNumber + " Tupel wurden gesendet.";
		}
		String s = "";
		if (properties.getBreakdown(configIndex))  {
			if (properties.getHttpError(configIndex) == "none")
				s+= "Impliziter Ausfall\n";
			else 
				s+= "Expliziter Ausfall\n";
		}
		else  {
			s += "Aktiv\n";
		}	
		s+= "Aktuelle Konfiguration: \n";
		s+= properties.getShortString(configIndex) + "\n";
		s+= "G�ltig: " + df.format(new Date(configurationStart)) + " - ";
		s+= df.format(new Date(configurationExpires));
		return s;
	}
	
	/**
	 * N�tig, um den Namen zur Deregistrierung an den Datenquellenmanager zu �bergeben.
	 * @return  den RMI-Namen dieser Quelle
	 * @uml.property  name="name"
	 */
	public String getName() throws RemoteException  {
		return name;
	}
	
	/*
	 * Muss nach erfolgreichem Binden durch den Datenquellenmanager 
	 * aufgerufen werden, damit der Client diesen Namen sp�ter 
	 * zum deregistrieren abfragen kann.
	 */
	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	void setName(String name)  {
		this.name = name;
	}
	
	/* 
	 * Wird aufgerufen, wenn das aktuelle Verhalten ausl�uft. Legt das neue Verhalten
	 * (Index einer Konfiguration in den Eigenschaften der Quelle (DataSourceProperties))
	 * fest. Dieses und der Zeitpunkt an dem das Verhalten neu berechnet werden muss, 
	 * werden global gespeichert.
	 *
	 */
	private void computeBehavior()  {
		// aktuelle Zeit (minutengenau), 
		// als konstant angenommen w�hrend der Berechnung des neuen Verhaltens
		GregorianCalendar nowCal = new GregorianCalendar();
		// zur Bestimmung der Zeit, wann eine Konfiguration abl�uft
		GregorianCalendar expCal = (GregorianCalendar)nowCal.clone();
		// immer mit glatten Minuten rechnen
		nowCal.set(Calendar.SECOND,0);
		nowCal.set(Calendar.MILLISECOND,0);
		
		// Anzahl der Konfigurationen
		int confCount = properties.getConfigNumber();
		// Zeitpunkt des n�chsten Starts der entsprechenden Konfiguration
		long[] nextStart = new long[confCount];
		
		if (DEBUG)  {
			System.out.println("DataSource.computeBehavior: Startzeiten berechnet:");
		}
		for (int i = 0; i < confCount; i++)  {
			nextStart[i] = getNextStart(i,nowCal);
			if (DEBUG)
				System.out.println("    Konfiguration " + i + ": " + 
						df.format(new Date(nextStart[i])));
		}
		
		
		// Welches ist die Konfiguration mit der h�chsten Priorit�t, die gerade laufen m�chte?
		long now = nowCal.getTimeInMillis();
		int newConfigIndex = 0;
		for (int i = 0; i < confCount; i++)  {
			if (nextStart[i] <= now)  {
				newConfigIndex = i;   
				break;
			}
		}
		// Neue Konfiguration endet, wenn ihre geplante Zeit abgelaufen ist
		// oder vorher eine h�her priorisierte Konfiguration starten will
		expCal.set(Calendar.HOUR_OF_DAY,properties.getEndHour(newConfigIndex));
		expCal.set(Calendar.MINUTE,properties.getEndMinute(newConfigIndex));
		// gilt noch w�hrend der gesamten Endminute
		expCal.add(Calendar.MINUTE,1);
		expCal.set(Calendar.SECOND,0);
		expCal.set(Calendar.MILLISECOND,0);
		long newConfExp = expCal.getTimeInMillis();
		for (int i = 0; i < newConfigIndex; i++)  {
			if (nextStart[i] < newConfExp)
				newConfExp = nextStart[i];
		}
		// Globale Variablen aktualisieren
		configIndex = newConfigIndex;
		configurationStart = configurationExpires;
		configurationExpires = newConfExp;
		perfectTime = configurationStart;		
		
		if (DEBUG)  {
			System.out.println("DataSource.computeBehavior: neues Verhalten: " 
				 + properties.getShortString(configIndex) +
	            "\n  g�ltig bis " + df.format(new Date(configurationExpires)));
		}
	}
	
	/*
	 * Hilfsmethode f�r computeBehavior(), berechnet f�r eine gegebene Konfiguration
	 * den absoluten Zeitpunkt des n�chsten Starts als long.
	 * Gerechnet wird meist in Minuten (entspricht der Angabe in der Konfigurationsdatei).
	 *
	 */
	private long getNextStart(int configIndex, GregorianCalendar nowCal)  {
		// Enth�lt am Ende dieser Methode den Zeitpunkt des n�chsten geplanten Starts einer
		// Konfiguration.
		GregorianCalendar nextCal = (GregorianCalendar)nowCal.clone();
		
		// Einstellungen des "aktuellen" Starts in Abh�ngigkeit der Periodizit�t
		switch (properties.getPeriod(configIndex))  {
		
		case DataSourceProperties.MONTHLY :
			// letzer Tag des Monats gew�hlt?
			if (properties.getDayOfMonth(configIndex) == DataSourceProperties.LAST_DAY_OF_MONTH) {
				nextCal.set(Calendar.DAY_OF_MONTH, nextCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			}	
			// Monatstag ist direkt angegeben, wenn es diesen im aktuellem Monat nicht gibt,
			// n�chsten Monat w�hlen (sp�testens der hat 31 Tage)
			else if (properties.getDayOfMonth(configIndex) > 
												nextCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				nextCal.add(Calendar.MONTH,1);
				nextCal.set(Calendar.DAY_OF_MONTH,properties.getDayOfMonth(configIndex));
			}	
			// Normalfall
			else  {
				nextCal.set(Calendar.DAY_OF_MONTH,properties.getDayOfMonth(configIndex));
			}	
			nextCal.set(Calendar.HOUR_OF_DAY,properties.getStartHour(configIndex));
			nextCal.set(Calendar.MINUTE,properties.getStartMinute(configIndex));
			break;
			
		case DataSourceProperties.WEEKLY : 
			nextCal.set(Calendar.DAY_OF_WEEK,properties.getDayOfWeek(configIndex));
			
		case DataSourceProperties.ALWAYS:	
			
		case DataSourceProperties.DAILY : 
			nextCal.set(Calendar.HOUR_OF_DAY,properties.getStartHour(configIndex));
			nextCal.set(Calendar.MINUTE,properties.getStartMinute(configIndex));
		}
		
		// Unterscheiden, ob die Konfiguration gerade gilt, sp�ter aber noch in der 
		// selben Periode, oder erst in der n�chsten Periode
		long diff = nextCal.getTimeInMillis() - nowCal.getTimeInMillis();
		if (diff >= 0)  {
			// Start beginnt genau jetzt (siehe ALWAYS) oder liegt in der Zukunft
			return nextCal.getTimeInMillis();
		}
		else if (-diff < properties.getConfigRuntimeMillis(configIndex))  {
			// l�uft gerade, n�chster Start liegt weniger als die Dauer der Konfiguration zur�ck
			return nowCal.getTimeInMillis();
		}
		else {
			// Startzeit eine Periode (Tag,Woche,Monat) weiter 
			switch (properties.getPeriod(configIndex))  {
			case DataSourceProperties.DAILY :
				nextCal.add(Calendar.DAY_OF_MONTH,1);
				break;
			
			case DataSourceProperties.WEEKLY :
				nextCal.add(Calendar.DAY_OF_MONTH,7);
				break;
				
			case DataSourceProperties.MONTHLY :
				nextCal.add(Calendar.MONTH,1);
				// War letzter Tag des Monats gew�hlt?
				if (properties.getDayOfMonth(configIndex) == DataSourceProperties.LAST_DAY_OF_MONTH)
					nextCal.set(Calendar.DAY_OF_MONTH,nextCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				// Gibt es den entsprechenden Tag im neuen Monat?
				if (properties.getDayOfMonth(configIndex) > nextCal.getActualMaximum(Calendar.MONTH))  {
					nextCal.add(Calendar.MONTH,1);
					// Der Wert des Tages wurde beim ersten add ge�ndert (Calendar add rule 2).
					nextCal.set(Calendar.DAY_OF_MONTH, properties.getDayOfMonth(configIndex));
				}	
			}
			return nextCal.getTimeInMillis();
		}
	}	
		
		
		
	
	/*
	 * Berechnet in Abh�ngigkeit der Konfiguration der Datenquelle die 
	 * Wartezeit, bis das n�chste Tupel zu liefern ist. Wird von 
	 * getNext() aufgerufen.
	 * Arbeitet mit Zeiten in ms, relativ zum Start der aktuellen Konfiguration.
	 *
	 * @return  die Wartezeit bis zur Lieferung des n�chsten Tupels in ms
	 */
	private long computeSleepTime()   {
		// der aktuell g�ltige Satz von Konstanten
		int DS_INV_RATE = properties.getInvRate(configIndex);
		int DS_DELAY = properties.getDelay(configIndex);
		int DS_BURST_SIZE = properties.getBurstSize(configIndex);
		int DS_BURST_PAUSE = properties.getBurstPause(configIndex);
		int DS_BURST_LAG = properties.getBurstLag(configIndex);
		int DS_DELAY_COMPENSATION = properties.getDelayCompensation(configIndex);
		// Zeitabstand zwischen dem letzen und dem aktuellen Tupel in ms
		long lag;
		// Die Schlafzeit, unter Voraussetzung dass die Konfiguration nicht vorher ausl�uft
		long sleepTime;
		// Die f�r diese Methode als konstant angenommene Zeit
		long now = System.currentTimeMillis();
	
		
		// Ist bursty Verhalten sinnvoll eingestellt?
		// Dann wird mit einer Pause begonnen.
		if (DS_BURST_PAUSE > 0 && DS_BURST_SIZE > 0)  {
			// unterscheiden zwischen Burst und Pause
			if (tupleIndex % (DS_BURST_SIZE) == 0)  {
				// Pause, falls gerade das allererste Tupel geliefert wird, 
				// wird eine eventuelle Anfangsverz�gerung ber�cksichtigt.
				lag = (veryFirstTuple && DS_DELAY > DS_BURST_PAUSE) ? DS_DELAY : DS_BURST_PAUSE;
			}
			else  {
				// Burst, Verz�gerung sollte eigentlich 0, sein, aber das f�hrt bei l�ngeren Bursts 
				// zu Verzerrungen, da der reale Minimalwert knapp unter 10 liegt
				lag = DS_BURST_LAG; 
			}
		}
		// Normale Lieferung, evtl. mit Anfangsverz�gerung
		else   { 
			if (veryFirstTuple == true)
				lag = Math.max(DS_DELAY,DS_INV_RATE);
			else
				lag = DS_INV_RATE;
		}	
		
		// Zeitdifferenz von jetzt bis zur geplanten n�chten Lieferung
		 sleepTime = perfectTime + lag - now - DS_DELAY_COMPENSATION;
		// planm��ige Testzeit aktualisieren
		perfectTime += lag;
		
		// Ab jetzt keine Anfangsverz�gerung mehr ber�cksichtigen
		veryFirstTuple = false; 
		// entsprechend dem Verhalten schlafen, es sei denn die Konfiguration l�uft zwischenzeitlich ab
		return Math.min(sleepTime, configurationExpires - now);
	} 
}
