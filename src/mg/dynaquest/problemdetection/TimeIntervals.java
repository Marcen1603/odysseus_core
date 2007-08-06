/*
 * Created on 13.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Eine Liste aus Zeitintervallen. Diese Intervalle stellen die
 * Zeitspanne dar, in der ein Operator blockiert war (Entry- bis Leave-Zeit)
 * 
 * @author Joerg Baeumer
 *  
 */
@SuppressWarnings("unused")
public class TimeIntervals {

	/**
	 * @uml.property  name="initialTime"
	 */
	private long initialTime = 0;
	/**
	 * @uml.property  name="intervalList"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="this$0:mg.dynaquest.problemdetection.TimeIntervals$Interval"
	 */
	private LinkedList<Interval> intervalList;
			
	/**
	 * Konstruktor
	 */
	public TimeIntervals() {
		super();
		intervalList = new LinkedList<Interval>();
		initialTime = System.currentTimeMillis();
	}
	
	/**
	 * Einfügen eines neuen, offenen Intervalls (bedeutet, dass der Operator
	 * aktuell blockiert ist)
	 * 
	 * @param entry Eintrittszeitpunkt in die Blockierung
	 */
	public void blockOperator(long entry) {
				
		intervalList.add(new Interval(entry));
		
	}
	
	/**
	 * Schließen eines Intervalls (gliechbedeutend damit, dass ein Operator
	 * nicht mehr blockiert ist
	 * 
	 * @param leave Austrittszeitpunkt aus der Blockierung
	 */
	public void unblockOperator(long leave) {
		
		// Alle offenen Intervalle schließen
		
		Interval interval;
		ListIterator list = intervalList.listIterator();
		
		while (list.hasNext()) {
			
			interval = (Interval) list.next();
			
			if (interval.intervalIsOpen()) 
			
				interval.setLeaveTime(leave);	
			
		}
		
	}
		
	/**
	 * Überprüfen, ob die Liste der Intervalle ein intervall enthält, in das 
	 * der zu überprüfende Zeitpunkt fällt. Dient der Überprüfung, ob ein 
	 * Operator zu einem bestimmten Zeitpunkt blockiert war.
	 * 
	 * @param time der zu überprüfende Zeitpunkt
	 * @return true - es wurde ein Intervall in der Liste gefunden, welches den 
	 * 			zu überprüfenden Zeitpunkt beinhaltet
	 */
	public boolean operatorBlockedAtTime(long time){
		
		// Durchlaufen der Intervalle und prüfen, ob Zeitpunkt 'time' in einem
		// Intervall, wenn ja war/ist der Operator zum Zeitpunkt 'time' blockiert
		
		Interval interval;
		ListIterator list = intervalList.listIterator();
		
		while (list.hasNext()) {
	
			interval = (Interval) list.next();
	
			if (interval.isInInterval(time))
			
				return true;
			 	
		}

		return false;
				
	}
		
	public String toString(){
		
		String s = "";
		Interval interval;
		ListIterator list = intervalList.listIterator();
		
		while (list.hasNext()) {
	
			interval = (Interval) list.next();
	
			s = s + interval;
			 	
		}
		
		return s;
	
	}
		
		
	

	/**
	 *  Ein Interval mit Entry- und Leave-Zeitpunkten.
	 * 
	 *  @author Joerg Baeumer
	 *
	 */
	public class Interval {

		public long entry;
		public long leave;

		/**
		 * Konstruktor
		 * 
		 * @param entry unterer Zeitgrenze
		 * @param leave obere Zeitgrenze
		 */
		public Interval(long entry, long leave) {
			super();
			this.entry = entry;
			this.leave = leave;
			
		}
		/**
		 * Konstruktor
		 * 
		 * @param entry untere Zeitgrenze
		 */
		public Interval(long entry) {
			super();
			this.entry = entry;
			this.leave = 0;
			
		}
		/**
		 * Liefert den Entry-Zeitpunkt des Intervalls
		 * 
		 * @return Entry-Zeitpunkt
		 */
		public long getEntryTime() {
			
			return entry;
			
		}
		
		/**
		 * Liefert den Leave-Zeitpunkt des Intervalls
		 * 
		 * @return leave-Zeitpunkt
		 */
		public long getLeaveTime() {
			
			return leave;
		
		}
		
		/**
		 * Setzt den Entry-Zeitpunkt
		 * 
		 * @param leave Entry-Zeitpunkt
		 */
		public void setLeaveTime(long leave) {
			
			this.leave = leave;
					
		}

		/**
		 * Überprüft, ob ein Zeitpunkt innerhalb des Intervalls liegt
		 * 
		 * @param time zu überprüfender Zeitpunkt
		 * @return true - Zeitpunkt liegt im Intervall
		 */
		public boolean isInInterval (long time) {
			
			if (time < this.entry)
			
				return false;
				
			else {
				
				if ((time <= this.leave) || (leave == 0))
				
					return true; 
				
			}		
			return false;
		}
		
		/**
		 * Überprüft, ob das Intervall offen ist.
		 * 
		 * @return true - Intervall ist offen.
		 */
		public boolean intervalIsOpen (){
			
			return (leave == 0);
			
		}
		
		public String toString() {
			
			return "[Interval von: " + entry + " bis " + leave + "]"; 			
		}
		
	
	}

	

}
