/*
 * Created on 17.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import org.w3c.dom.NodeList;

/**
 * 
 * Die Klasse verbindet zwei Eingabeströme miteinader (join) 
 * nach der Double-Pipeline-Methode. 
 * 
 * @author Maxim Bauer
 * @version 1.0
 */
public class DoublePipeJoinPO extends RelationalPhysicalJoinPO {
	
	/**
	 * HashMap zum Speichern der Eingaben vom linken Eingabestrom
	 * @uml.property  name="buildInputLeft"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple" qualifier="key:mg.dynaquest.queryexecution.po.relational.object.RelationalTuple java.util.ArrayList"
	 */
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInputLeft = null;
	/**
	 * HashMap zum Speichern der Eingaben vom rechten Eingabestrom
	 * @uml.property  name="buildInputRight"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple" qualifier="key:mg.dynaquest.queryexecution.po.relational.object.RelationalTuple java.util.ArrayList"
	 */
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInputRight = null;
	/** Ergebnisliste */
	private ArrayList<RelationalTuple> tmpResults = null;
	/**
	 * Attribute zum Überwachen der Eingabeströme, wenn ein Eingabestrom zu Ende ist wird das jeweilige Attribut auf true gesetzt
	 * @uml.property  name="leftDone"
	 */
	private boolean leftDone;
	/**
	 * Attribute zum Überwachen der Eingabeströme, wenn ein Eingabestrom zu Ende ist wird das jeweilige Attribut auf true gesetzt
	 * @uml.property  name="rightDone"
	 */
	private boolean rightDone;
	/**
	 * Zeit welche der Operator wartet bevor eine TimeoutException geworfen wird
	 * @uml.property  name="time"
	 */
	private long time;
	

    /**
     * 
     */
    public DoublePipeJoinPO() {
        super();
       
    }

    /**
     * @param joinPO
     */
    public DoublePipeJoinPO(DoublePipeJoinPO joinPO) {
        super(joinPO);
        
    }
    
    /** Aktuell verwendete Konstruktor
     * 
     * @param compareAttrs Die zu verbindende Attribut-Paare
     * @param time Zeit, welche der Operator auf Eingabedaten wartet
     * */
    
    public DoublePipeJoinPO(RelationalTupleCorrelator compareAttrs, long time) {
    	this.setCompareAttrs(compareAttrs);
    	this.time = time;
        
    }
    
    /**
     * Methode zum Initialisieren des Operators,
     * Hier werden die verwendeten HashMaps, Ergebnisliste und
     * die Überwachungsattribute initialisiert
     * */
    public  boolean process_open() throws POException {
		buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		buildInputRight = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		tmpResults = new ArrayList<RelationalTuple>();
		leftDone = rightDone = false;
		return true;
	}
    
    /**
     * In dieser Methode findet die eigentliche Verarbeitung der Eingabe
     * 
     * @return newAttrList Es wird jeweils ein Ergebnistupel zurückgeliefert wie 
     * Ergebnisse in der Liste vorhanden sind
     * */ 
    public  Object process_next() throws POException, TimeoutException {
    	
    	RelationalTuple newAttrList = null;
    	
    	/** Hier wird die Methode zum Einlesen und Verbinden gestartet */
    	tmpResults = join();
    	
    	/** Solange bis entweder mind. ein Ergebnis geliefert wird oder die beiden Eingaben leer sind
    	 *  wird die join()-Methode aufgerufen */
		while(tmpResults.size() == 0 && (!leftDone || !rightDone)){
			tmpResults = join();
		}
		
		/**
		 * Wenn Ergebnisse vorhanden sind, wird ein Ergebnis aus der Liste herausgenommen
		 * und zurückgeliefert
		 * */
		if (tmpResults.size() > 0) {
			newAttrList = (RelationalTuple) tmpResults.remove(0);
		}
		return newAttrList;
    } 
    
    /**
     * Die Methode wird aufgerufen wenn der Operator seine Arbeit beendet hat
     * */
    protected boolean process_close() throws POException {
		return true;
	}
    
    /**
     * Diese Methode liest die Eingaben ein, teilt diese in die jeweiligen HashMap
     * ein und verbindet die zu einander passenden Tupel miteinander. Die verbundenen
     * Tupel werden in die tmpResults hinzugefügt
     * 
     * @return tmpResults Liste mit gespeicherten Ergebnistupel
     * */
    private ArrayList<RelationalTuple> join() throws POException, TimeoutException{
    	
    	RelationalTuple leftElement = null;
		RelationalTuple rightElement = null;
    	
    	/**Schritt 1: Aufbau der Hash-Maps für beide Eingabeströme
		* Beim Aufbau sollte gleichzeitig die Übeprüfung der Tupel mit der anderen
		* Hash-Map erfolgen 
		* 
		* */
		if(!leftDone){
			try{
				//Daten anfordern vom linken Eingabestrom
				while ((leftElement = (RelationalTuple) this.getLeftNext(this,time)) != null){
					//Eingaben vorbereiten, Key erstellen und auf Duplikate überprüfen
					RelationalTuple key = leftElement.restrict(getCompareAttrs().getAllSources());
					ArrayList<RelationalTuple> elementsLeft = buildInputLeft.get(key);
					//Schlüssel bereits vorhanden?
					if (elementsLeft != null) {
						elementsLeft.add(leftElement);
					} else {
						elementsLeft = new ArrayList<RelationalTuple>();
						elementsLeft.add(leftElement);
						buildInputLeft.put(key, elementsLeft);
					}
					//Teste ob in der rechten HashMap schon Elemente vorhanden sind
					if (buildInputRight.size()!=0 && buildInputLeft.size() !=0){
						//Wenn ja, suche nach Join-Partnern und verbinde diese
						ArrayList<RelationalTuple> elementsRight = buildInputRight.get(key);
						if (elementsRight != null) {
							for (int a = 0; a < elementsRight.size(); a++) {
								/*System.out.print("Verknüpfe "
										+ (RelationalTuple) elementsRight.get(a) + " mit "
										+ leftElement);*/
								RelationalTuple res = leftElement.mergeLeft(
										elementsRight.get(a),
										this.getCompareAttrs());
								//System.out.println("-->" + res);
								tmpResults.add(res);
							}
						}
					}
				}
				leftDone = true;
			//Verzögerung der linken Daten. In diesem Fall wird das Anfragen der Daten
			//unterbrochen und der Operator fordert Daten vom rechten Einagbestrom	
			}catch (TimeoutException e) {
				
				if(!rightDone){
					try{
						//Daten anfordern vom linken Eingabestrom
						while ((rightElement = (RelationalTuple) this.getRightNext(this,time)) != null){
							//Eingaben vorbereiten, Key erstellen und auf Duplikate überprüfen
							RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
							ArrayList<RelationalTuple> elementsRight = buildInputRight.get(key);
							//Schlüssel bereits vorhanden?
							if (elementsRight != null) {
								elementsRight.add(rightElement);
							} else {
								elementsRight = new ArrayList<RelationalTuple>();
								elementsRight.add(rightElement);
								buildInputRight.put(key, elementsRight);
							}
							//Teste ob in der linken HashMap schon Elemente vorhanden sind
							if (buildInputLeft.size()!=0 && buildInputRight.size()!=0){
								//Wenn ja, suche nach Join-Partnern und verbinde diese
								ArrayList<RelationalTuple> elementsLeft = buildInputLeft.get(key);
								if (elementsLeft != null) {
									for (int a = 0; a < elementsLeft.size(); a++) {
										/*System.out.print("Verknüpfe "
												+ (RelationalTuple) elementsLeft.get(a) + " mit "
												+ rightElement);*/
										RelationalTuple res = rightElement.mergeLeft(
												elementsLeft.get(a),
												this.getCompareAttrs());
										//System.out.println("-->" + res);
										tmpResults.add(res);
									}
								}
							}
						}
						//Alle Eingaben vom rechten Eingabestrom wurden erfolgreich gelesen
						rightDone = true;
					}catch (TimeoutException ex) {
						//Wartezeit überschritten
						//System.out.println("Verzögert rechts");
					}
				}
			}
		}
		//Daten von rechten Eingabestrom anfordern solange bis Daten verfügbar sind
		if(!rightDone){
			try{
				while ((rightElement = (RelationalTuple) this.getRightNext(this,time)) != null){
					//Eingaben vorbereiten, Key erstellen und auf Duplikate überprüfen
					RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
					ArrayList<RelationalTuple> elementsRight = buildInputRight.get(key);
					//Schlüssel bereits vorhanden?
					if (elementsRight != null) {
						elementsRight.add(rightElement);
					} else {
						elementsRight = new ArrayList<RelationalTuple>();
						elementsRight.add(rightElement);
						buildInputRight.put(key, elementsRight);
					}
					//Teste ob in der linken HashMap schon Elemente vorhanden sind
					if (buildInputLeft.size()!=0 && buildInputRight.size()!=0){
						//Wenn ja, suche nach Join-Partnern und verbinde diese
						ArrayList<RelationalTuple> elementsLeft = buildInputLeft.get(key);
						if (elementsLeft != null) {
							for (int a = 0; a < elementsLeft.size(); a++) {
								/*System.out.print("Verknüpfe "
										+ (RelationalTuple) elementsLeft.get(a) + " mit "
										+ rightElement);*/
								RelationalTuple res = rightElement.mergeLeft(
										elementsLeft.get(a),
										this.getCompareAttrs());
								//System.out.println("-->" + res);
								tmpResults.add(res);
							}
						}
					}
				}
				rightDone = true;
			}catch (TimeoutException e) {
				//Daten vom linken Eingabestrom anfordern, solange diese verfügbar sind
				if(!leftDone){
					try{
						while ((leftElement = (RelationalTuple) this.getLeftNext(this,time)) != null){
							//Eingaben vorbereiten, Key erstellen und auf Duplikate überprüfen
							RelationalTuple key = leftElement.restrict(getCompareAttrs().getAllSources());
							ArrayList<RelationalTuple> elementsLeft = buildInputLeft.get(key);
							//Schlüssel bereits vorhanden?
							if (elementsLeft != null) {
								elementsLeft.add(leftElement);
							} else {
								elementsLeft = new ArrayList<RelationalTuple>();
								elementsLeft.add(leftElement);
								buildInputLeft.put(key, elementsLeft);
							}
							//Teste ob in der rechten HashMap schon Elemente vorhanden sind
							if (buildInputRight.size()!=0 && buildInputLeft.size()!=0){
								//Wenn ja, suche nach Join-Partnern und verbinde diese
								ArrayList<RelationalTuple> elementsRight = buildInputRight.get(key);
								if (elementsRight != null) {
									for (int a = 0; a < elementsRight.size(); a++) {
										/*System.out.print("Verknüpfe "
												+ (RelationalTuple) elementsRight.get(a) + " mit "
												+ leftElement);*/
										RelationalTuple res = leftElement.mergeLeft(
												elementsRight.get(a),
												this.getCompareAttrs());
										//System.out.println("-->" + res);
										tmpResults.add(res);
									}
								}
							}
						}
						//Alle Eingaben vom linken Eingabestrom wurden erfolgreich gelesen
						leftDone = true;
					}catch (TimeoutException ex) {
						//Wartezeit überschritten
						//System.out.println("Verzögert links");
					}
				}
			}
		}
    	return tmpResults;
    }

	public SupportsCloneMe cloneMe() {
		return new DoublePipeJoinPO(this);
	}

	@Override
	public String getInternalPOName() {
		return "DoublePipeJoinPO";
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO Auto-generated method stub
		
	}
}
