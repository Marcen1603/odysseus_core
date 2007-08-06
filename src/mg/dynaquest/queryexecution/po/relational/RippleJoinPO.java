package mg.dynaquest.queryexecution.po.relational;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import org.w3c.dom.NodeList;

/**
 * 
 * Die Klasse verbindet zwei Eingabeströme miteinader (join) 
 * nach der Ripple-Join-Methode.
 *
 *@author Maxim Bauer
 *@version 1.0
 */

public class RippleJoinPO extends RelationalPhysicalJoinPO {
	
	/**
	 * Maximale Größe einer Partition
	 * @uml.property  name="partSize"
	 */
	private int partSize;
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
	/** Liste zum Speichern der linken Eingabetupel*/
	private ArrayList<RelationalTuple> leftInput = null;
	/** Liste zum Speichern der rechten Eingabetupel*/
	private ArrayList<RelationalTuple> rightInput = null;
	/**
	 * Zielordner wo die Partitionen gespeichert werden sollen
	 * @uml.property  name="baseDir"
	 */
	// TODO: baseDir in Config auslagern !!!!!!!!!
	private final String baseDir = ".";
	/**
	 * Attribute zum Überwachen der Eingabeströme, wenn ein Eingabestrom zu Ende ist wird das jeweilige Attribut auf true gesetzt
	 * @uml.property  name="leftDone"
	 */
	boolean leftDone;
	/**
	 * Attribute zum Überwachen der Eingabeströme, wenn ein Eingabestrom zu Ende ist wird das jeweilige Attribut auf true gesetzt
	 * @uml.property  name="rightDone"
	 */
	boolean rightDone;
	/**
	 * Zeit welche der Operator wartet bevor eine TimeoutException geworfen wird
	 * @uml.property  name="time"
	 */
	private long time;
	/**
	 * Zähler und Ids für die Partitionen
	 * @uml.property  name="partNumberLeft"
	 */
	private int partNumberLeft;
	/**
	 * Zähler und Ids für die Partitionen
	 * @uml.property  name="partNumberRight"
	 */
	private int partNumberRight;
    	
	public RippleJoinPO() {
		super();
	}

	public RippleJoinPO(JoinPO joinPO) {
		super(joinPO);
	}

	
	/** Aktuell verwendete Konstruktor
     * 
     * @param compareAttrs Die zu verbindende Attribut-Paare
     * @param partSize Maximale Größe einer Partition
     * @param time Zeit, die der Operator auf Eingabedaten wartet
     * */
	public RippleJoinPO(RelationalTupleCorrelator compareAttrs, int partSize, long time) {
		this.setCompareAttrs(compareAttrs);
		this.partSize = partSize;
		this.time = time;
    }
	
	public RippleJoinPO(RelationalPhysicalJoinPO joinPO) {
		super(joinPO);
	}

	/**
     * Methode zum Initialisieren des Operators,
     * Hier werden die verwendeten HashMaps, benötigten Listen, Ergebnisliste und
     * die Überwachungsattribute initialisiert
     * */
	public synchronized boolean process_open() throws POException {
		buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		buildInputRight = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		leftInput = new ArrayList<RelationalTuple>();
		rightInput = new ArrayList<RelationalTuple>();
		tmpResults = new ArrayList<RelationalTuple>();
		leftDone = rightDone = false; 
		partNumberLeft = partNumberRight = 0;
		return true;
	}
	
	/**
     * In dieser Methode findet die eigentliche Verarbeitung der Eingabe
     * 
     * @return newAttrList Es wird jeweils ein Ergebnistupel zurückgeliefert wie 
     * Ergebnisse in der Liste vorhanden sind
     * */ 
	public synchronized Object process_next() throws POException, TimeoutException {
		
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
     * Diese Methode liest die Eingaben ein, teilt diese in die jeweiligen HashMaps
     * und Listen ein und verbindet die zu einander passenden Tupel miteinander. Die verbundenen
     * Tupel werden in die tmpResults hinzugefügt
     * 
     * @return tmpResults Liste mit gespeicherten Ergebnistupel
     * */
	private ArrayList<RelationalTuple> join() throws POException, TimeoutException{
		
		RelationalTuple leftElement = null;
		RelationalTuple rightElement = null;
			//Hierbei werden die Elemente aus dem linken Eingabestrom gelesen und partitioniert
			if(!leftDone){
				try{
					while (leftInput.size() < partSize && (leftElement = (RelationalTuple) this.getLeftNext(this,time)) != null){
						//Zur Liste hinzufügen
						leftInput.add(leftElement);
						//linke HashMap aufbauen 
						RelationalTuple key = leftElement.restrict(getCompareAttrs().getAllSources());
						ArrayList<RelationalTuple> elements = buildInputLeft.get(key);
						//Schlüssel bereits vorhanden?
						if (elements != null) {
							elements.add(leftElement);
						} else {
							elements = new ArrayList<RelationalTuple>();
							elements.add(leftElement);
							buildInputLeft.put(key, elements);
						}
					}
					//Partitionierung der gelesenen Elemente
					if(leftInput.size() == partSize || (leftInput.size() < partSize && leftElement == null)){
						ArrayList<RelationalTuple> elements = null;
						ArrayList<RelationalTuple> rightInputTemp = new ArrayList<RelationalTuple>();
						//Alle rechte Partitionen nacheinander laden und mit linken HashMap vergeleichen
						for(int i=0; i<partNumberRight; i++){
							rightInputTemp = loadFromDiskList(i, false);
							for(int j=0; j<rightInputTemp.size(); j++){
								rightElement = rightInputTemp.get(j);
								RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
								elements = buildInputLeft.get(key);
								if (elements != null) {
									for (int a = 0; a < elements.size(); a++) {
										/*System.out.print("Verknüpfe "
												+ (RelationalTuple) elements.get(a) + " mit "
												+ rightElement);*/
										RelationalTuple res = rightElement.mergeLeft(
												elements.get(a),
												this.getCompareAttrs());
										//System.out.println("-->" + res);
										tmpResults.add(res);
									}
								}
							}
						}
						//Die aktuelle Partition auslagern
						saveToDiskList(leftInput,partNumberLeft, true);
						partNumberLeft++;
						//Die aktuelle Liste und die aktuelle HashMap löschen
						leftInput = new ArrayList<RelationalTuple>();
						buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
					}
					//Linker Eingabestrom zu ende?
					if(leftElement == null){
						leftDone = true;
					}
					
				}catch (TimeoutException e) {
					//System.out.println("Datenverzögerung links");
				}
			}
			//Engaben von rechts lesen solange diese verfügbar sind
			if(!rightDone){
				try{
					while (rightInput.size() < partSize && (rightElement = (RelationalTuple) this.getRightNext(this,time)) != null){
						//gelesenes Element zur Liste hinzufügen
						rightInput.add(rightElement);
						//rechte HashMap aufbauen
						RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
						ArrayList<RelationalTuple> elements = buildInputRight.get(key);
						//Schlüssel bereits vorhanden?
						if (elements != null) {
							elements.add(rightElement);
						} else {
							elements = new ArrayList<RelationalTuple>();
							elements.add(rightElement);
							buildInputRight.put(key, elements);
						}
					}
					//Partitionierung der gelesenen Elemente
					if(rightInput.size() == partSize || (rightInput.size() < partSize && rightElement == null)){
						ArrayList<RelationalTuple> elements = null;
						//Alle linken Partitionen nacheinander laden und mit rechten HashMap vergeleichen
						ArrayList<RelationalTuple> leftInputTemp = new ArrayList<RelationalTuple>();
						for(int i=0; i<partNumberLeft; i++){
							leftInputTemp = loadFromDiskList(i, true);
							for(int j=0; j<leftInputTemp.size(); j++){
								leftElement = leftInputTemp.get(j);
								RelationalTuple key = leftElement.restrict(getCompareAttrs().getAllSources());
								elements = buildInputRight.get(key);
								if (elements != null) {
									for (int a = 0; a < elements.size(); a++) {
										/*System.out.print("Verknüpfe "
												+ (RelationalTuple) elements.get(a) + " mit "
												+ leftElement);*/
										RelationalTuple res = leftElement.mergeLeft(
												elements.get(a),
												this.getCompareAttrs());
										//System.out.println("-->" + res);
										tmpResults.add(res);
									}
								}
							}
						}
						//Die aktuelle Partition auslagern
						saveToDiskList(rightInput,partNumberRight, false);
						partNumberRight++;
						//Die aktuelle Liste und die aktuelle HashMap löschen
						rightInput = new ArrayList<RelationalTuple>();
						buildInputRight = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
					}
					//Rechter Eingabestrom zu ende?
					if(rightElement == null){
						rightDone = true;
					}
					
				}catch (TimeoutException e){
					//System.out.println("Datenverzögerung rechts");
				}
			}
			return tmpResults;
	}
	
	/**Methode zum Auslagern der Listen-Partitionen in den Tertiärspeicher*/
	private void saveToDiskList(ArrayList<RelationalTuple> list, int number, boolean left){
    	try{
	    	//linke Liste
    		if(left){
	    		FileOutputStream fileStream = new FileOutputStream(baseDir+"listLeft"+number+".ser");
				ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
				outputStream.writeObject(list);
				outputStream.close();
	    	//rechte Liste
    		}else{
	    		FileOutputStream fileStream = new FileOutputStream(baseDir+"listRight"+number+".ser");
				ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
				outputStream.writeObject(list);
				outputStream.close();
	    	}
    		
    	}catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    
    /** Methode zum Laden der Listen aus dem Tertiärspeicher
     * 
     * @return partition Liste, die aktuell eingelesen wurde
     * */
    @SuppressWarnings("unchecked")
	private ArrayList<RelationalTuple> loadFromDiskList(int number, boolean left){
    	ArrayList<RelationalTuple> partition = null;
    	try{
    		//linke Liste
    		if(left){
    			FileInputStream fileStream = new FileInputStream(baseDir+"listLeft"+number+".ser");
    	        ObjectInputStream inputStream = new ObjectInputStream(fileStream);
    	        partition = (ArrayList<RelationalTuple>)inputStream.readObject();
    	        inputStream.close();
    		//rechte Liste
    		}else{
    			FileInputStream fileStream = new FileInputStream(baseDir+"listRight"+number+".ser");
    	        ObjectInputStream inputStream = new ObjectInputStream(fileStream);
    	        partition = (ArrayList<RelationalTuple>)inputStream.readObject();
    	        inputStream.close();
    		}
	    	
    	}catch (IOException e) {
            System.err.println(e.toString());
        }catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }
    	return partition;
    }

	@Override
	public String getInternalPOName() {
		return "RippleJoinPO";
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO Auto-generated method stub
		
	}

	public SupportsCloneMe cloneMe() {
		return new RippleJoinPO(this);
	}
}
