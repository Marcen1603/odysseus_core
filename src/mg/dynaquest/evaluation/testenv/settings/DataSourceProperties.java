package mg.dynaquest.evaluation.testenv.settings;

import java.text.DecimalFormat;
import mg.dynaquest.evaluation.testenv.server.DataSource;
import mg.dynaquest.evaluation.testenv.server.DataSourceManager;

/**
 * B�ndelt die Einstellungen, die eine Datenquelle  {@link DataSource  DataSource} betreffen, also etwa die Datenrate, die Anfangsverz�gerung, den Ausfallgrund, usw. Diese stehen zusammen mit weiteren Systemeinstellungen in der Konfigurationsdatei f�r den entsprechenden DatenQuellen-Manager.  ( {@link DataSourceManager  DataSourceManager} ).  <p> Es werden mehrere Konfigurationen(S�tze von Datenquellen-Eigenschaften und G�ltigkeitszeitr�ume) gleichzeitig gespeichert, deren einzelne Werte lassen sich �ber getter-Methoden mit dem Index der Konfiguration aufgerufen, wobei die Priorit�t einer Konfiguration mit steigendem Index abnimmt. Z.B. liefert <code>getInvRate(2)</code> die inverse Datenrate der  2. Konfiguration. Diese wird nur ber�cksichtigt, wenn die erste und die nullte Konfiguration f�r den betrachteten Zeitraum nicht gelten.  <p> Zur genauen Bedeutung der einzelnen Parameter siehe auch  TODO link nach dqt_server_conf.readme einf�gen
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class DataSourceProperties {
	
	/**
	 * Bestimmt die Periodizit�t einer Konfiguration.
	 * Zeigt an, dass die Konfiguration immer laufen m�chte.
	 * Diese Einstellung muss bei der default-Konfiguration verwendet werden.
	 */
	public static final int ALWAYS = 0; 
	/**
	 * Bestimmt die Periodizit�t einer Konfiguration.
	 * Die Konfiguration m�chte jeden Tag f�r einem bestimmten Zeitraum laufen.
	 */
	public static final int DAILY = 1;
	/**
	 * Bestimmt die Periodizit�t einer Konfiguration.
	 * Die Konfiguration m�chte an einem festen Wochentag in einem bestimmten 
	 * Zeitraum laufen. 
	 */
	public static final int WEEKLY = 2;
	/**
	 * Bestimmt die Periodizit�t einer Konfiguration.
	 * Die Konfiguration m�chte an festen Datum im Monat 
	 * zu einer bestimmten Zeit laufen. Wenn es diesen Tag in
	 * einem bestimmten Monat nicht gibt (z.B. 30.2 oder 31.11.)
	 * f�llt das Verhalten f�r den entsprechenden Monat aus.
	 */
	public static final int MONTHLY = 3;
	/**
	 * Gibt an, dass die Konfiguration immer am letzten Tag eines 
	 * Monats gelten soll. Kann f�r <code>dayOfMonth</code> verwendet werden,
	 * wenn <code>period = MONTHLY</code>.
	 */
	public static final int LAST_DAY_OF_MONTH = 32;
	
	// Anzahl der gespeicherten Konfigurationen
	/**
	 * @uml.property  name="configNumber"
	 */
	private int configNumber;
	// Werden die Datens�tze automatisch wiederholt
	/**
	 * @uml.property  name="repeat"
	 */
	private boolean repeat = false;
	
	// muss eine der obigen Konstanten annehmen
	/**
	 * @uml.property  name="period" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] period;
	
	// Bei period = DAILY werden dayOfWeek und dayOfMonth ignoriert,
	// bei WEEKLY muss dayOfWeek gesezt sein, 
	// bei ONCE dayOfMonth.
	/**
	 * @uml.property  name="dayOfWeek" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] dayOfWeek;  // Wochentag, 1-7
	/**
	 * @uml.property  name="dayOfMonth" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] dayOfMonth; // Monatstag, 1-31 
	/**
	 * @uml.property  name="startHour" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] startHour;   // 0-23
	/**
	 * @uml.property  name="startMinute" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] startMinute;  // 0-59
	/**
	 * @uml.property  name="endHour" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] endHour;		// 0-23
	/**
	 * @uml.property  name="endMinute" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] endMinute;   // 0-59
	
	/**
	 * @uml.property  name="invRate" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] invRate;  // inverse Datenrate in ms
	/**
	 * @uml.property  name="delay" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] delay;     // Anfangsverz�gerung in ms
	/**
	 * @uml.property  name="burstSize" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] burstSize;  // L�nge eines Bursts in Tupeln
	/**
	 * @uml.property  name="burstPause" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] burstPause;  // L�nge einer Burst-Pause in ms
	/**
	 * @uml.property  name="burstLag" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] burstLag;   // angestrebter realer Zeitabstand innerhlab eines Bursts
	/**
	 * @uml.property  name="breakdown" multiplicity="(0 -1)" dimension="1"
	 */
	private boolean[] breakdown;  // totaler Ausfall w�hrend der Konfiguration oder nicht.
	/**
	 * @uml.property  name="delayCompensation" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] delayCompensation;  // konstanter Verz�gerungsausgleich
	/**
	 * @uml.property  name="httpError" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] httpError;  // Ausfallgrund
	
	/**
	 * Erzeugt eine neue Repr�sentation von Datenquelleneigenschaften.
	 * Diese ist zun�chst leer, <code>repeat</code> wird mit <code>false</code> initialisiert.
	 * Sobald die Anzahl der zu speichernden Konfigurationss�tze fest steht, muss diese mit
	 * {@link #setConfigNumber(int) setConfigNumber(int)} gesetzt werden, damit die
	 * Felder f�r die einzelnen Einstellungen erzeugt werden. 
	 * @param i 
	 *  
	 * @see #setRepeat(boolean)
	 */
	public DataSourceProperties(int i)  {
        setConfigNumber(i);
	}
	
	/**
	 * @param configNumber  the configNumber to set
	 * @uml.property  name="configNumber"
	 */
	private void setConfigNumber(int number)  {
		configNumber = number;
		period = new int[number];
		dayOfWeek = new int[number];
		dayOfMonth = new int[number];
		startHour = new int[number];
		endHour = new int[number];
		startMinute = new int[number];
		endMinute = new int[number];
		invRate = new int[number];
		delay = new int[number];
		breakdown = new boolean[number];
		burstSize = new int[number];
		burstPause = new int[number];
		burstLag = new int[number];
		delayCompensation = new int[number];
		httpError = new String[number];
	}
	
	/**
	 * Setzt das Wiederholungsverhalten der Datenquelle. 
	 * @param r  <code>true</code>, falls die Datens�tze in st�ndiger Wiederholung geliefert werden  sollen, <code>false</code> f�r einmalige Lieferung
	 * @uml.property  name="repeat"
	 */
	public void setRepeat(boolean r)  {
		repeat = r;
	}
	
	/**
	 * Gibt an, wieviele Konfigurationen definiert sind.
	 * @return  die Anzahl der Einstellungss�tze
	 * @uml.property  name="configNumber"
	 */
	public int getConfigNumber()  {
		return configNumber;
	}
	
	/**
	 * Gibt an, ob diese Datenquelle sich realistisch verh�lt, und jedes Tupel nur einmal
	 * sendet, oder ob sie ihre Daten st�ndig wiederholt, was f�r l�ngere Tests sinnvoll
	 * sein kann. 
	 * @return <code>true</code>, falls die Datens�tze st�ndig wiederholt gesendet werden,
	 * 		   ansonsten <code>false</code>
	 * @see #setRepeat(boolean)
	 */
	public boolean isRepeating()  {
		return repeat;
	}
	
	/**
	 * Berechnet, wie lange eine Konfiguration l�uft.
	 * Dies ist die Differenz zwischen Start- und Endzeit plus 1 Minute,
	 * da die Konfiguration f�r die gesamte Endminute gelten soll.
	 * Z.B w�rde eine Endzeit von 23:59 bedeuten, dass die Konfiguration genau 
	 * bis 00:00.000 gilt.
	 * 
	 * @param configIndex Index der Konfiguration
	 * @return	die Laufzeit in ms
	 */
	public long getConfigRuntimeMillis(int configIndex) {
		return ((endHour[configIndex] -  startHour[configIndex]) * 60
					+ endMinute[configIndex] - startMinute[configIndex] + 1) * 60000;
	}
	
	/**
	 * Liefert den Typ der Periodizit�t einer Konfiguration.
	 * Dieser sollte aus den Konstanten dieser Klasse gew�hlt sein.
	 * 
	 * @param  index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Periodizit�t der gew�nschten Konfiguration
	 */
	public int getPeriod(int index)  {
		return period[index];
	}
	
	/**
	 * Liefert den Wochentag, an dem die Konfiguration gilt.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return den Wochentag
	 */
	public int getDayOfWeek(int index)  {
		return dayOfWeek[index];
	}
	
	/**
	 * Liefert den Tag im Monat, an dem die Konfiguration gilt.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return den Monatstag
	 */
	public int getDayOfMonth(int index)  {
		return dayOfMonth[index];
	}
	
	/**
	 * Liefert die Stunde, in der die Konfiguration beginnt.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Stunde
	 */
	public int getStartHour(int index)  {
		return startHour[index];
	}
	
	/**
	 * Liefert die Stunde, in der die Konfiguration endet.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Stunde
	 */
	public int getEndHour(int index)  {
		return endHour[index];
	}
	
	/**
	 * Liefert die Minute, in der die Konfiguration beginnt. 
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Minute
	 */
	public int getStartMinute(int index)  {
		return startMinute[index];
	}
	
	/**
	 * Liefert die Minute, in der die Konfiguration endet.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Minute
	 */
	public int getEndMinute(int index)  {
		return endMinute[index];
	}
	
	/**
	 * Liefert die inverse Datenrate der Quelle f�r die gew�hlte Konfiguration,
	 * also den Abstand zwischen den Tupeln.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die inverse Datenrate in ms 
	 */
	public int getInvRate(int index)  {
		return invRate[index];
	}
	
	/**
	 * Liefert die Anfangsverz�gerung der Quelle f�r die gew�hlte Konfiguration.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Zeit bis zur Lieferung des ersten Tupels in ms
	 */
	public int getDelay(int index)  {
		return delay[index];
	}
	
	/**
	 * Gibt an, ob die Quelle f�r die gesamte Dauer einer Konfiguration ausf�llt  
	 * oder nicht.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return <code>true</code>, falls die Quelle f�r die Dauer der Konfiguration ausf�llt,
	 *         ansonsten <code>false</code>
	 */
	public boolean getBreakdown(int index)  {
		return breakdown[index];
	}
	
	/**
	 * Liefert die L�nge eines Bursts f�r die gew�hlte Konfiguration.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die Burst-L�nge, gemessen in Tupeln
	 */
	public int getBurstSize(int index)  {
		return burstSize[index];
	}
	
	/**
	 * Liefert die L�nge einer Burst-Pause f�r die gew�hlte Konfiguration.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return die L�nge der Burst-Pausen in ms
	 */
	public int getBurstPause(int index)  {
		return burstPause[index];
	}
	
	/**
	 * Liefert den angestrebten realen Abstand innerhalb eines Bursts f�r die gew�hlte Konfiguration.
	 * Wird dieser auf <code>0</code> gesetzt, hinkt die Quelle dem Zeitplan hinterher, da dieser
	 * Abstand zwischen den Tupeln nicht m�glich ist. Der Verzug wird dann auf Kosten der 
	 * Burst-Pausen ausgeglichen.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return den angestrebten realen Tupelabstand w�hrend eines Bursts in ms
	 */
	public int getBurstLag(int index)  {
		return burstLag[index];
	}
	
	/**
	 * Liefert den konstanten Wert, der von der Wartezeit der Quelle zwischen zwei Tupeln
	 * bei der gew�hlten Konfiguration abgezogen wird.
	 * Dies dient zum Ausgleich von kleinen Verz�gerungen, etwa durch <i>RMI</i>-Aufrufe
	 * und Thread-/Prozess-Wechsel.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return den konstanten Verz�gerungsausgleich in ms
	 */
	public int getDelayCompensation(int index)  {
		return delayCompensation[index];
	}
	
	/**
	 * Liefert die Fehlermeldung bei Ausfall der Quelle. Dies sollte ein <i>http</i>-
	 * Fehlercode f�r expliziten, bzw. <code>none</code> f�r einen impliziten Fehler sein.
	 * 
	 * @param index Index (und damit auch Priorit�t) der Konfiguration, beginnend mit <code>0</code>
	 * @return den Ausfallgrund
	 */
	public String getHttpError(int index)  {
		return httpError[index];
	}
	
	/**
	 * Setzt eine komplette Konfiguration mit dem entsprechenden Index (und der selben Priorit�t).
	 * 
	 * @param index der Index der zu setzenden Konfiguration
	 * @param period die Periodizit�t der Konfiguration
	 * @param dow der Wochentag, an dem die Konfiguration gilt
	 * @param dom das Datum im Monat, an dem die Konfiguration gilt
	 * @param stHr die Stunde, ab der die Konfiguation gilt
	 * @param stMin die Minute, ab der die Konfiguration gilt
	 * @param endHr die Stunde, bis zu der die Konfiguration gilt
	 * @param endMin die Minute, bis zu der die Konfiguration gilt
	 * @param invRate die inverse Datenrate der Quelle in ms 
	 * @param delay die Anfangsverz�gerung der Quelle in ms 
	 * @param burstSize die Burst-L�nge der Quelle in Tupeln
	 * @param burstPause die Burst-Pause der Quelle in ms
	 * @param burstLag der angestrebte Abstand zwischen zwei Tupeln w�hrend eines Bursts in ms
	 * @param breakdown ist die Quelle ausgefallen oder nicht?
	 * @param delayComp der konstante Verz�gerungsausgleich
	 * @param httpError der Ausfallgrund
	 */
	 public void setConfiguration(int index, int period, int dow, int dom, int stHr, int stMin,
			int endHr, int endMin, int invRate,int delay, int burstSize, int burstPause,
			int burstLag, boolean breakdown, int delayComp,String httpError)  {
	
		this.period[index] = period;
		this.dayOfWeek[index] = dow;
		this.dayOfMonth[index] = dom;
		this.startHour[index] = stHr;
		this.startMinute[index] = stMin;
		this.endHour[index] = endHr;
		this.endMinute[index] = endMin;
		this.invRate[index] = invRate;
		this.delay[index] = delay;
		this.burstSize[index] = burstSize;
		this.burstPause[index] = burstPause;
		this.burstLag[index] = burstLag;
		this.breakdown[index] = breakdown;
		this.delayCompensation[index] = delayComp;
		this.httpError[index] = httpError;
	}
	
	public String toString() {
		int num = period.length;
		String s = "DataSourceProperties " + this.hashCode() + "[";
		s += "\n  repeat = " + repeat;
		for (int i = 0; i < num; i++)  {
			s += "\n  " +  i + ". Einstellungssatz :";
			s += "\n      period = " + period[i];
			s += "\n      dayOfWeek = " + dayOfWeek[i];
			s += "\n      dayOfMonth = " + dayOfMonth[i];
			s += "\n      startHour = " + startHour[i];
			s += "\n      startMinute = " + startMinute[i];
			s += "\n      endHour = " + endHour[i];
			s += "\n      endMinute = " + endMinute[i];
			s += "\n      invRate = " + invRate[i];
			s += "\n      delay = " + delay[i];
			s += "\n      burstSize = " + burstSize[i];
			s += "\n      burstPause = " + burstPause[i];
			s += "\n      burstLag = " + burstLag[i];
			s += "\n      breakdown = " + breakdown[i];
			s += "\n      delayCompensation = " + delayCompensation[i];
			s += "\n      httpError = " + httpError[i];
			
			if (i == num - 1)
				s += "]";
		}
		return s;
	}
	
	/**
	 * Beschreibt eine Konfiguration in kompakter Darstellung.
	 * 
	 * @param configIndex der Index der Konfiguration
	 * @return einen <code>String</code> in zwei Zeilen alle Einstellungen angibt
	 */
	public String getShortString(int configIndex)  {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
		df.setMinimumIntegerDigits(2);
		df.setMaximumIntegerDigits(2);
		
		String s = "Konfiguration " + configIndex + " [";
		s += "period=" + period[configIndex];
		s += ",dayOfWeek=" + dayOfWeek[configIndex];
		s += ",dayOfMonth=" + dayOfMonth[configIndex];
		s += ",interval=" + df.format(startHour[configIndex]) + ":"; 
		s += df.format(startMinute[configIndex]);
		s += "-" + df.format(endHour[configIndex]) + ":"; 
		s += df.format(endMinute[configIndex]);
		s += ",\ninvRate=" + invRate[configIndex];
		s += ",delay=" + delay[configIndex];
		s += ",burstSize=" + burstSize[configIndex];
		s += ",burstPause=" + burstPause[configIndex];
		s += ",burstLag=" + burstLag[configIndex];
		s += ",breakdown=" + breakdown[configIndex];
		s += ",delayCompensation=" + delayCompensation[configIndex];
		s += ",httpError=" + httpError[configIndex];
		s += "]";
		
		return s;
	}

	// Neue Setter-Methoden: Ich finde diese L�sung nicht sch�n ... aber halte mich erstmal an
    // die Dinge von Tobias ...
    
    public void setPeriod(int number, int index)  {
        period[index] = number;
    }

    public void setDayOfWeek(int number, int index)  {
        dayOfWeek[index] = number;
    }

    public void setDayOfMonth(int number, int index)  {
        dayOfMonth[index] = number;
    }

    public void setStartHour(int number, int index)  {
        startHour[index] = number;
    }

    public void setEndHour(int number, int index)  {
        endHour[index] = number;
    }

    public void setStartMinute(int number, int index)  {
        startMinute[index] = number;
    }

    public void setEndMinute(int number, int index)  {
        endMinute[index] = number;
    }

    public void setInvRate(int number, int index)  {
        invRate[index] = number;
        System.out.println("invRate "+number);
    }
    
    public void setDelay(int number, int index)  {
        delay[index] = number;
    }
    
    public void setBreakdown(boolean breakdown, int index)  {
        this.breakdown[index] = breakdown;
    }
    
    public void setBurstSize(int number, int index)  {
        burstSize[index] = number;
    }
    
    public void setBurstPause(int number, int index)  {
        burstPause[index] = number;
    }
    
    public void setBurstLag(int number, int index)  {
        burstLag[index] = number;
    }

    public void setDelayCompensation(int number, int index)  {
        delayCompensation[index] = number;
    }

    public void setHttpError(String error, int index)  {
        httpError[index] = error;
    }         
        
}







