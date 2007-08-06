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
 * 
 * Die Klasse verbindet zwei Eingabeströme miteinader (join) 
 * nach der HybridHashJoin-Methode. Es wird hier ausgegangen dass der linke
 * Eingabestrom der kleinere ist.
 *
 *@author Maxim Bauer
 *@version 1.0
 */
public class HybridHashJoinPO extends RelationalPhysicalJoinPO implements UsesHashtable{

	/**
	 * HashMap zum Speichern der Eingaben vom linken Eingabestrom
	 * @uml.property  name="buildInput"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple" qualifier="key:mg.dynaquest.queryexecution.po.relational.object.RelationalTuple java.util.ArrayList"
	 */
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInput = null;
	/** Liste zum Speichern der Eingaben vom rechten Eingabestrom */
	private ArrayList<RelationalTuple> partInput = null;
	/** Ergebnisliste */
	private ArrayList<RelationalTuple> tmpResults = null;
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
	 * Maximale Größe einer Partition
	 * @uml.property  name="partSize"
	 */
	private int partSize;
	/**
	 * Attribut zum abgrenzen des ersten Durchlaufs
	 * @uml.property  name="firstrun"
	 */
	private boolean firstrun;
	/**
	 * Zielordner wo die Partitionen gespeichert werden sollen
	 * @uml.property  name="baseDir"
	 */
	// TODO: baseDir in Config auslagern !!!!!!!!!
	private final String baseDir = ".";


    /**
     * 
     */
    public HybridHashJoinPO() {
        super();
        
    }

    /**
     * @param joinPO
     */
    public HybridHashJoinPO(JoinPO joinPO) {
        super(joinPO);
        
    }
    
    /** Aktuell verwendeter Konstruktor
     * @param compareAttrs Die zu verbindende Attribut-Paare
     * @param partSize Maximale Größe einer Partition
     * */
    public HybridHashJoinPO(RelationalTupleCorrelator compareAttrs, int partSize) {
		this.setCompareAttrs(compareAttrs);
		this.partSize = partSize;
    }
    
    public HybridHashJoinPO(HybridHashJoinPO joinPO) {
    	super(joinPO);
	}

	/**
     * Methode zum Initialisieren des Operators,
     * Hier werden die verwendeten HashMaps, Ergebnisliste und
     * die Überwachungattribute initialisiert
     * */
    public synchronized boolean process_open() throws POException {
		buildInput = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		partInput = new ArrayList<RelationalTuple>();
		tmpResults = new ArrayList<RelationalTuple>();
		firstrun = true;
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
		RelationalTuple leftElement = null;
		RelationalTuple rightElement = null;
    	
    	//Partitionierungsphase der Eingabeströme: Zuerst den linken
		//dann den rechten Eingabestrom
		if (firstrun){
			try {
				//Hash-Map aufbauen, solange bis keine Elemente mehr da sind
				while ((leftElement = (RelationalTuple) this.getLeftNext(this,-1)) != null){
					RelationalTuple key = leftElement.restrict(getCompareAttrs().getAllSources());
					ArrayList<RelationalTuple> elements = buildInput.get(key);
					//Schlüssel bereits vorhanden?
					if (elements != null) {
						elements.add(leftElement);
					} else {
						elements = new ArrayList<RelationalTuple>();
						elements.add(leftElement);
						buildInput.put(key, elements);
					}
					//Wenn Partitionsgrösse erreicht ist, dann die Partition auslagern
					//Der linke Einagstrom wird als HashMap gespeichert
					if (buildInput.size()==partSize){
						saveToDiskMap(buildInput,partNumberLeft);
						buildInput.clear();
						partNumberLeft++;
					}
				}
				//Falls keine Elemente mehr vorhanden sind wird die letzte Partition geschrieben
				if(buildInput.size()>0){
					saveToDiskMap(buildInput,partNumberLeft);
					partNumberLeft++;
				}
				
				//Für den rechten Eingabestrom werden die Partitionen in Listen gespeichert
				while ((rightElement = (RelationalTuple) this.getRightNext(this,-1)) != null){
					partInput.add(rightElement);
					//Wenn die Partition voll ist dann die Partition auslagern
					if (partInput.size()==partSize){
						saveToDiskList(partInput,partNumberRight);
						partInput.clear();
						partNumberRight++;
					}
				}
				//Restelemente in die letzte Partition auslagern
				if(partInput.size()>0){
					saveToDiskList(partInput,partNumberRight);
					partNumberRight++;
				}
				
				firstrun = false;
				}catch (TimeoutException e) {
					e.printStackTrace();
				}
			
			ArrayList<RelationalTuple> elements = null;
			//Partitionierung abgeschlossen, dann die erste HashMap wieder laden
			//und mit allen verfügbaren rechten Tupel auf Übereinstimmung überprüfen 
			for(int i=0; i<partNumberLeft; i++){
				buildInput = loadFromDiskMap(i);
				//Nun sollten die Partitionen des rechten Eigabestroms nacheinander geladen und
				//mit dem linken Eingabestrom verglichen werden
				for(int b=0; b<partNumberRight;b++){
					partInput = loadFromDiskList(b);
					//Vergleiche alle Elemente der Partition mit der Hash-Map
					for(int j=0; j<partInput.size(); j++){
						rightElement = partInput.get(j);
						RelationalTuple key = rightElement.restrict(getCompareAttrs().getAllDestinations());
						elements = buildInput.get(key);
						if (elements != null) {
							for (int a = 0; a < elements.size(); a++) {
								/*System.out.print("Verknüpfe "
										+ (RelationalTuple) elements.get(a) + " mit "
										+ rightElement);*/
								RelationalTuple res = rightElement.mergeRight(
										elements.get(a),
										this.getCompareAttrs());
								//System.out.println("-->" + res);
								tmpResults.add(res);
							}
						}
					}
				}
			}
			
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
    
    /**Methode zum Auslagern der Listen-Partitionen in den Tertiärspeicher*/
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
//    	RelationalTuple element = null;
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
//    	RelationalTuple key = null;
//    	ArrayList<RelationalTuple> element = null;
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

	public HashMap<RelationalTuple, ArrayList<RelationalTuple>> getHashtable(int input) {
		
		return buildInput;
	}

	public boolean setHashtable(int input, HashMap<RelationalTuple, ArrayList<RelationalTuple>> newTable) {
		
		this.buildInput = newTable;
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new HybridHashJoinPO(this);
	}

	@Override
	public String getInternalPOName() {
		return "HybridHashJoinPO";
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
