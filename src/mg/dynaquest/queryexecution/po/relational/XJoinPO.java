/*
 * Created on 17.02.2005
 *
 */
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
 * Die Klasse verbindet zwei Eingabeströme miteinader (join)  nach der XJoin-Methode.
 * @author  Maxim Bauer
 * @version  1.0
 */
public class XJoinPO extends RelationalPhysicalJoinPO {

	/**
	 * Maximale Größe einer Partition
	 * @uml.property  name="partSize"
	 */
	private int partSize;
	/**
	 * Zeit welche der Operator wartet bevor eine TimeoutException geworfen wird
	 * @uml.property  name="time"
	 */
	private long time;
	/**
	 * HashMap zum Speichern der Eingaben vom linken Eingabestrom
	 * @uml.property  name="buildInputLeft"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple" qualifier="key:mg.dynaquest.queryexecution.po.relational.object.RelationalTuple java.util.ArrayList"
	 */
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInputLeft = null;
	/** Ergebnisliste */
	private ArrayList<RelationalTuple> tmpResults = null;
	/** Liste zum Überprüfen auf Duplikate*/
	private ArrayList<RelationalTuple> testResults = null;
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
	 * Attribut zum Aufrufen der cleanUp-Methode
	 * @uml.property  name="cleanUp"
	 */
	boolean cleanUp;
	/** Liste zum Speichern der rechten Eingabetupel*/
	private ArrayList<RelationalTuple> rightInput = null;
	/**
	 * Zielordner wo die Partitionen gespeichert werden sollen
	 * @uml.property  name="baseDir"
	 */
	// TODO: baseDir in Config auslagern !!!!!!!!!
	private final String baseDir = ".";

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
	/**
	 * Aktuelles linker Eingabeelement
	 * @uml.property  name="leftElement"
	 * @uml.associationEnd  
	 */
	private RelationalTuple leftElement;
	/**
	 * Aktuelles rechter Eingabeelement
	 * @uml.property  name="rightElement"
	 * @uml.associationEnd  
	 */
	private RelationalTuple rightElement;
	


    /**
     * 
     */
    public XJoinPO() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param joinPO
     */
    public XJoinPO(JoinPO joinPO) {
        super(joinPO);
        // TODO Auto-generated constructor stub
    }
    
    /** Aktuell verwendete Konstruktor
     * 
     * @param compareAttrs Die zu verbindende Attribut-Paare
     * @param partSize Maximale Größe einer Partition
     * @param time Zeit, die der Operator auf Eingabedaten wartet
     * */
    public XJoinPO(RelationalTupleCorrelator compareAttrs, int partSize, long time) {
		this.setCompareAttrs(compareAttrs);
		this.partSize = partSize;
		this.time = time;
    }
    
    public XJoinPO(XJoinPO joinPO) {
    	super(joinPO);
	}

	/**
     * Methode zum Initialisieren des Operators,
     * Hier werden die verwendeten HashMaps, benötigten Listen, Ergebnisliste und
     * die Überwachungsattribute initialisiert
     * */
	protected boolean process_open() throws POException {
    	buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
    	tmpResults = new ArrayList<RelationalTuple>();
    	testResults = new ArrayList<RelationalTuple>();
    	rightInput = new ArrayList<RelationalTuple>();
    	leftDone = rightDone = cleanUp = false;
    	partNumberLeft = partNumberRight = 0;
    	leftElement = new RelationalTuple(0);
    	rightElement = new RelationalTuple(0);
		return true;
	}
    
	/**
     * In dieser Methode findet die eigentliche Verarbeitung der Eingabe
     * 
     * @return newAttrList Es wird jeweils ein Ergebnistupel zurückgeliefert wie 
     * Ergebnisse in der Liste vorhanden sind
     * */ 
	protected Object process_next() throws POException, TimeoutException {
    	
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
		try{
			//Eingaben vom linken Eingabestrom lesen
			if(!leftDone){
				//solange bis entweder keine weiteren Elemente da sind oder die 
				//Wartezeit überschritten wird
				while (buildInputLeft.size() < partSize && (leftElement = (RelationalTuple) this.getLeftNext(this,time)) != null){
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
				}
			}
		}catch (TimeoutException e){
			//Wartezeit überschritten
			//System.out.println("Verzögert links");
			//Die bereits gelesenen Elemente miteinander verbinden
			tmpResults = cleanUp();
		}
		try{	
			//Eingaben vom rechten Eingabestrom lesen
			if(!rightDone){
				//solange bis entweder keine weiteren Elemente da sind oder die 
				//Wartezeit überschritten wird
				while (rightInput.size() < partSize && (rightElement = (RelationalTuple) this.getRightNext(this,time)) != null){
					rightInput.add(rightElement);
					//Teste ob die Ströme leer sind
					if(rightInput.size() != 0 && buildInputLeft.size() != 0){
						RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
						ArrayList<RelationalTuple> elementsRight = buildInputLeft.get(key);
						//in tmpResults alle gefundenen Treffer ablegen
						if (elementsRight != null) {
							for (int i = 0; i < elementsRight.size(); i++) {
								/*System.out.print("Verknüpfe "
										+ (RelationalTuple) elementsRight.get(i) + " mit "
										+ rightElement);*/
								RelationalTuple res = rightElement.mergeRight(
										elementsRight.get(i),
										this.getCompareAttrs());
								//System.out.println("-->" + res);
								if(!testResults.contains(res)){
									tmpResults.add(res);
									testResults.add(res);
								}
								
							}
						}
					}
				}
			}
		}catch (TimeoutException e){
			//Wartezeit überschritten
			//System.out.println("Verzögert rechts");
			//Die bereits gelesenen Elemente miteinander verbinden
			tmpResults = cleanUp();
		}
		//Partitionsgröße links erreicht?
		if (buildInputLeft.size() == partSize){
			//linke HashMap auslagern 
			saveToDiskMap(buildInputLeft,partNumberLeft);
			partNumberLeft++;
			//HasMap löschen
			buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		}
		//Eingabestrom links ist zu ende und die Partitionsgröße noch nicht erreicht
		if (buildInputLeft.size() < partSize && leftElement == null){
			if(!leftDone && buildInputLeft.size() != 0){
				//linke HashMap auslagern
				saveToDiskMap(buildInputLeft,partNumberLeft);
				//HashMap löschen
				buildInputLeft = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
				partNumberLeft++;
				//Eingabestrom links zu ende
				leftDone = true;
			}
			//Eingabestrom links ist zu ende
			if(buildInputLeft.size() == 0 && !leftDone){
				leftDone = true;
			}
			
		}
		
		//Partitionsgröße rechts erreicht?
		if(rightInput.size() == partSize){
			//rechte Liste auslagern
			saveToDiskList(rightInput, partNumberRight);
			partNumberRight++;
			//Liste löschen
			rightInput = new ArrayList<RelationalTuple>();
		}
		
		//Eingabestrom rechts ist zu ende und die Partitionsgröße noch nicht erreicht
		if (rightInput.size() < partSize && rightElement == null){
			if(!rightDone && rightInput.size() != 0){
				//Liste auslagern
				saveToDiskList(rightInput,partNumberRight);
				//Liste löschen
				rightInput = new ArrayList<RelationalTuple>();
				partNumberRight++;
				rightDone = true;
			}
			//Eingabestrom ist zu ende
			if(rightInput.size() == 0 && !rightDone){
				rightDone = true;
			}
		}

		//Beide Eingabeströme zu ende, Restelemente verbinden
		if(leftDone && rightDone && !cleanUp){
			tmpResults = cleanUp();
			cleanUp = true;
		}
		
		return tmpResults;
	}
	
	/**Diese Methode soll alle übrigen Ergebnistupeln finden und verbinden
	 * 
	 * @return tmpResults Liste mit Ergebnistupel
	 * */
    private ArrayList<RelationalTuple> cleanUp(){
    	
    	RelationalTuple rightElement = null;
		HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInputLeftTemp = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		ArrayList<RelationalTuple> rightInputTemp = new ArrayList<RelationalTuple>();
		//Überprüfen ob die HashMap-Partitionen überhaupt vorhanden sind
		if(partNumberLeft > 0){
    		ArrayList<RelationalTuple> elements = null;
    		for(int i=0; i<partNumberLeft; i++){
    			if(partNumberRight > 0){
    				buildInputLeftTemp = loadFromDiskMap(i);
    				for(int b=0; b<partNumberRight; b++){
    					rightInputTemp = loadFromDiskList(b);
    					//Vergleiche alle Elemente der Partition mit der Hash-Map
    					for(int j=0; j<rightInputTemp.size(); j++){
    						rightElement = rightInputTemp.get(j);
    						RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
    						elements = buildInputLeftTemp.get(key);
    						if (elements != null) {
    							for (int a = 0; a < elements.size(); a++) {
    								/*System.out.print("Verknüpfe "
    										+ (RelationalTuple) elements.get(a) + " mit "
    										+ rightElement);*/
    								RelationalTuple res = rightElement.mergeRight(
    										elements.get(a),
    										this.getCompareAttrs());
    								//System.out.println("-->" + res);
    								if(!testResults.contains(res)){
    									tmpResults.add(res);
    									testResults.add(res);
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	return tmpResults;
    }
	
    /**Methode zum Auslagern der Hash-Map Partitionen in den Tertiärspeicher*/
	private void saveToDiskMap(HashMap<RelationalTuple, ArrayList<RelationalTuple>> hashMap,int number){
    	try{
    		FileOutputStream fileStream = new FileOutputStream(baseDir+"map"+number+".ser");
    		ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
    		outputStream.writeObject(hashMap);
    		outputStream.close();
    	}catch (IOException e) {
            System.err.println(e.toString());
        }
    }	
    
    /**Methode zum Auslagern der Listen Partitionen in den Tertiärspeicher*/
    private void saveToDiskList(ArrayList<RelationalTuple> list, int number){
    	try{
	    	FileOutputStream fileStream = new FileOutputStream(baseDir+"list"+number+".ser");
			ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
			outputStream.writeObject(list);
			outputStream.close();
    	}catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    /**Methode zum Laden der Listen aus dem Tertiärspeicher
     * 
     * @return partition Liste, die aktuell eingelesen wurde
     * */
    @SuppressWarnings("unchecked")
	private ArrayList<RelationalTuple> loadFromDiskList(int number){
    	ArrayList<RelationalTuple> partition = null;
    	//RelationalTuple element = null;
    	try{
	    	FileInputStream fileStream = new FileInputStream(baseDir+"list"+number+".ser");
	        ObjectInputStream inputStream = new ObjectInputStream(fileStream);
	        partition = (ArrayList<RelationalTuple>)inputStream.readObject();
	        inputStream.close();
    	}catch (IOException e) {
            System.err.println(e.toString());
        }catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }
    	return partition;
    }
    
    /**Methode zum Laden der Hash-Maps aus dem Tertiärspeicher
     * 
     * @return partition HashMap, die aktuell eingelesen wurde
     * */
    @SuppressWarnings("unchecked")
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> loadFromDiskMap(int number){
    	HashMap<RelationalTuple, ArrayList<RelationalTuple>> partition =null;
    	//RelationalTuple key = null;
    	//ArrayList<RelationalTuple> element = null;
    	try{
	    	FileInputStream fileStream = new FileInputStream(baseDir+"map"+number+".ser");
	        ObjectInputStream inputStream = new ObjectInputStream(fileStream);
	        partition = (HashMap<RelationalTuple, ArrayList<RelationalTuple>>)inputStream.readObject();
	        inputStream.close();
    	}catch (IOException e) {
            System.err.println(e.toString());
        }catch (ClassNotFoundException e) {
            System.err.println(e.toString());
        }
    	return partition;
    }

	@Override
	public String getInternalPOName() {
		// TODO Auto-generated method stub
		return null;
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
		return new XJoinPO(this);
	}
}
