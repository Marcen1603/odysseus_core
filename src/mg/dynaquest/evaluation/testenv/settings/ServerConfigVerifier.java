package mg.dynaquest.evaluation.testenv.settings;


/**
 * Überprüft Datenquelleneigenschaften in Form von <code>DataSourceProperties</code>-Objekten, deren Einträge aus einer <i>xml</i>-Datei ausgelesen wurden. Die Eigenschaften, die das Lieferungs- verhalten der Datenquellen bestimmen, werden nur hier überprüft und nicht in jeder weiteren Methode, die diese Werte benutzt.  <p> Sinnvolle Beschränkungen für die einzelnen Einstellungen sind in dieser Klasse festgelegt. Fehler in den allgemeinen Einstellungen, wie etwa RMI-Properties oder Angaben zur Datenbankverbindung, deren Überprüfung hier zu (zeit-)aufwendig wäre, werden dort behandelt, wo sie sich bemerkbar machen. <p>  Verwendung: Aufruf von  {@link #checkProperties(DataSourceProperties)    checkProperties(DataSourceProperties)}  . Ist der Rückgabewert <code>false</code>, kann die genaue Fehlerbeschreibung mit  {@link #getErrors()  getErrors()}   abgefragt werden. 
 * @see settings.DataSourceProperties
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class ServerConfigVerifier {
	
	// eine eventuelle Fehlermeldung
	/**
	 * @uml.property  name="errors"
	 */
	private String errors;
	
	// sinnvolle Minimal- und Maximalwerte
	/**
	 * Der Minimalwert für die inverse Datenrate in ms.
	 */
	public static final int MIN_INV_RATE = 5;
	/**
	 * Der Maximalwert für die inverse Datenrate in ms.
	 */
	public static final int MAX_INV_RATE = 60000;
	/**
	 * Der Minimalwert für die Burst-Größe in Tupeln.
	 */
	public static final int MIN_BURST_SIZE = 1;
	/**
	 * Der Maximalwert für die Burst-Größe in Tupeln.
	 */
	public static final int MAX_BURST_SIZE = 100;
	/**
	 * Der Minimalwert für Burst-Pausen in ms.
	 */
	public static final int MIN_BURST_PAUSE = 100;
	/**
	 * Der Maximalwert für Burst-Pausen in ms.
	 */
	public static final int MAX_BURST_PAUSE = 60000;
	/**
	 * Der Minimalwert für den Abstand innerhalb eines Bursts in ms.
	 */
	public static final int MIN_BURST_LAG = 5;
	/**
	 * Der Maximalwert für den Abstand innerhalb eines Bursts in ms.
	 */
	public static final int MAX_BURST_LAG = 1000;
	/**
	 * Der Minimalwert für den konstanten Verzögerungsausgleich ms.
	 */
	public static final int MIN_DELAY_COMP = 0;
	/**
	 * Der Maximalwert für den konstanten Verzögerungsausgleich ms.
	 */
	public static final int MAX_DELAY_COMP = 50;
	
	/**
	 * Überprüft die Gültigkeit der Werte eines {@link DataSourceProperties
	 * DataSourceProperties}-Objektes. Es wird getestet, ob die Werte in 
	 * vernünftigen Bereichsgrenzen liegen. Meist sind die Grenzen offensichtlich,
	 * z.B. [0;59] für Minutenangaben. In nicht so eindeutigen
	 * Fällen werden die Grenzen in dieser Klasse durch Konstanten festgelegt, so etwa
	 * die Obergrenze der Datenrate.
	 * Zudem wird geprüft, ob bestimmte Abhängigkeiten zwischen den Feldern erfüllt sind.
	 * So dürfen Endzeiten nicht größer als die dazugehörigen Startzeiten sein, 
	 * bestimmte Periodizitäten erfordern die gültige Beseztung bestimmter Felder 
	 * (z.B. WEEKLY -> dayOfWeek), ...
	 * <p>
	 * Ist der Rückgabewert dieser Methode <code>false</code>, kann durch Aufruf von
	 * {@link #getErrors() getErrors()} eine genaue Beschreibung aller Fehler
	 * erhalten werden. 
	 * 
	 * @param dsp die zu verifizierenden Datenquelleneigenschaften
	 * @return <code>true</code>, falls diese Eigenschaften im obigen Sinne gültig sind,
	 * 			ansonsten <code>false</code>
	 */
	public boolean checkProperties(DataSourceProperties dsp)  {
		errors = new String("Fehler in den Datenquellen-Eigenschaften:\n");
		boolean always = false;  // Zeitangaben werden bei dieser Periodizität nicht berücksichtigt
		boolean ok = true;
		
		// 0 Konfigurationen?
		if (dsp.getConfigNumber() == 0)  {
			errors += "Keine Konfiguration definiert!";
			return false;
		}
		
		for (int i = 0; i < dsp.getConfigNumber(); i++)  {
			// Zeitangaben wichtig ?
			if (dsp.getPeriod(i) == DataSourceProperties.ALWAYS)
				always = true;
			else 
				always = false;
			errors += "  Konfigurationssatz " + i + " :\n";
			
			// Bereichsüberschreitung der Zeitangaben
			int sh = dsp.getStartHour(i);
			int sm = dsp.getStartMinute(i);
			int eh = dsp.getEndHour(i);
			int em = dsp.getEndMinute(i);
			if (outOfRange(sh,0,23) && !always)  {
				errors += errorString("startHour",sh,0,23);
				ok = false;
			}
			if (outOfRange(sm,0,59) && !always)  {
				errors += errorString("startMinute",sm,0,59);
				ok = false;
			}
			if (outOfRange(eh,0,23) && !always)  {
				errors += errorString("endHour",eh,0,23);
				ok = false;
			}
			if (outOfRange(em,0,59) && !always)  {
				errors += errorString("endMinute",em,0,59);
				ok = false;
			}
		
			
			// Periodizität
			if (outOfRange(dsp.getPeriod(i),0,3)) {
				errors += errorString("period",dsp.getPeriod(i),0,3);
				ok = false;
			}
     		// Besondere Bedingung für default-Eintrag
			if (i == dsp.getConfigNumber() - 1 && !always)  {
				errors += "    Periodizität des letzten Eintrages (default-Wert) muss 0 sein\n";
				ok = false;
			}
			
			// Zeiträume < 0
			if (sh > eh | (sh == eh && em < sm) && !always)  {
				errors += "    Zeitintervall <= 0 : "+ sh + ":" + sm + " - " + eh + ":" + em + "\n";
				ok = false;
			}
			// periodizitätsspezifische Bedingungen
			if (dsp.getPeriod(i) == DataSourceProperties.WEEKLY && outOfRange(dsp.getDayOfWeek(i),1,7)) {
				errors += errorString("dayOfWeek",dsp.getDayOfWeek(i),1,7);
				ok = false;
			}
			// 32 bedeutet "immer letzter des Monats"
			if (dsp.getPeriod(i) == DataSourceProperties.MONTHLY && 
											outOfRange(dsp.getDayOfMonth(i),1,32))  {
				errors += errorString("dayOfMonth",dsp.getDayOfMonth(i),1,32);
				ok = false;
			}
			
			// Wenn diese Konfiguration einen Ausfall vorsieht, ist nichts außer den
			// Zeitangaben zu beachten
			if (dsp.getBreakdown(i) == true)
				continue;
			
			// Werte, die in Abhängigkeit von bursty Verhalten gesetzt sein müssen
			boolean isBursty = dsp.getBurstSize(i) > 0 && dsp.getBurstPause(i) > 0;
			if (isBursty)  {
				if (outOfRange(dsp.getBurstSize(i),MIN_BURST_SIZE,MAX_BURST_SIZE)) {
					errors += errorString("burstSize",dsp.getBurstSize(i),
												MIN_BURST_SIZE,MAX_BURST_SIZE);
					ok = false;
				}
				if (outOfRange(dsp.getBurstPause(i),MIN_BURST_PAUSE,MAX_BURST_PAUSE)) {
					errors += errorString("burstPause",dsp.getBurstPause(i),
												MIN_BURST_PAUSE,MAX_BURST_PAUSE);
					ok = false;
				}
				if (outOfRange(dsp.getBurstLag(i),MIN_BURST_LAG,MAX_BURST_LAG)) {
					errors += errorString("burstLag",dsp.getBurstLag(i),
												MIN_BURST_LAG,MAX_BURST_LAG);
					ok = false;
				}
			}	
			// nicht bursty, Datenrate wird berücksichtigt
			else {
				if (outOfRange(dsp.getInvRate(i),MIN_INV_RATE,MAX_INV_RATE)) {
					errors += errorString("Inverse Rate",dsp.getInvRate(i),MIN_INV_RATE,MAX_INV_RATE);
					ok = false;
				}
			}
			// sonstige 
			
			// Anfangsverzögerung (delay) muss nicht beachtet werden, 
			// alle int-Werte sind erlaubt,
			// negative Werte werden für das Quellenverhalten nicht berücksichtigt
		    
			// Verzögerungsausgleich
			if (outOfRange(dsp.getDelayCompensation(i),MIN_DELAY_COMP,MAX_DELAY_COMP))  {
				errors += errorString("delay compensation",dsp.getDelayCompensation(i),
											MIN_DELAY_COMP,MAX_DELAY_COMP);
				ok = false;
			}
			
			// httperror kann auch jeden String-Wert annehmen
		}	
		return ok;
	}
	
	/**
	 * Liefert einen genauen Fehlerbericht, falls die letzten mit  {@link #checkProperties(DataSourceProperties)  checkProperties(DataSourceProperties)} geprüften Datenquellen-Eigenschaften fehlerhaft waren. 
	 * @return  einen übersichtlich formatierten String mit den nach Konfiguration geordneten   Fehlern
	 * @uml.property  name="errors"
	 */
	public String getErrors()  {
		return errors;
	}
	
	// Hilfsmethode um den doppelten Vergleich zu sparen und die Übersichtlichkeit zu erhöhen
	private boolean outOfRange(int val, int left, int right)  {
		return val < left || val > right;
	}
	
	// Hilfsmethode zum einheitliches und einfaches Erstellen von Fehlereinträgen
	private String errorString(String param,int val,int minVal,int maxVal)  {
		return "    Ungültiger Wert für " + param + "(" + val + ")" +
			 ",Bereich: " + minVal + "-" + maxVal + "\n";
	}
	
}
