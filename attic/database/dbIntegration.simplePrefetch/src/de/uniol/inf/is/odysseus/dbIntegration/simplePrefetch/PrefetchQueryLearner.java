package de.uniol.inf.is.odysseus.dbIntegration.simplePrefetch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * Mit Hilfe dieser Klasse koennen zu Datenstromtupeln Tupel errechnet werden, 
 * welche in Zukunft vermutlich auftreten. Dies ist aber nur bei Mustern innerhalb
 * des Datenstroms moeglich.
 * 
 * @author crolfes
 *
 */
public class PrefetchQueryLearner {
	private DBQuery query;
	
	//Gibt an, wie lange Klassen gebaut werden soll. 
	//Danach nur noch lernen, aber keine neuen Klassen. 
	private int maxClassifyingObjs;
	private int currObj;
	private SDFAttributeList inputSchema;
	
	private RelationalTuple<?> attrTuple;
	
	private NaiveBayesUpdateable classifier;

	
	private IDataAccess dataAccess;
	private ICache cache;
	private Instances instances;
	
	private int previsionDepth = 0;
	private int previsionWidth = 1;
	private HashMap<String, RelationalTuple<?>> classTuples;
	private LinkedList<InOutPair<RelationalTuple<?>, RelationalTuple<?>>> inout;
	
	
	/**
	 * Konstruktor des Learners.
	 * 
	 * @param query - die identifizierende Datenbankanfrage.
	 * @param maxClassifyingObjs - Anzahl, bis zu der Tupel eingelesen werden sollen. Danach 
	 * wird mit der Klassifizierung begonnen.
	 * @param inputSchema - das Eingabeschema fuer die Bezeichnung der Attribute der Instance-Objekte
	 * @param dataAccess - das IDataAccess-Objekt zum Zugriff auf die Datenbank durch den Service.
	 * @param cache - das ICache-Objekt zum Zugriff auf den Cache.
	 */
	public PrefetchQueryLearner (DBQuery query, int maxClassifyingObjs, SDFAttributeList inputSchema, IDataAccess dataAccess, ICache cache) {
		this.query = query;
		this.maxClassifyingObjs = maxClassifyingObjs;
		this.inputSchema = inputSchema;
		this.dataAccess = dataAccess;
		this.cache = cache;
		inout = new LinkedList<InOutPair<RelationalTuple<?>, RelationalTuple<?>>>();
		
	}
	
	
	/**
	 * Innerhalb dieser Methode werden die Elemente des zuvor erzeugten Trainingsdatensatzes (inout) zu 
	 * Instance-Objekten ueberfuert. Aus diesen wiederum wird ein Instances-Objekt erstellt, mit welchem 
	 * der Klassifizier lernen kann.
	 * Als Lernklasse werden nur die Datenstromattribute genutzt, welche in der Datenbankanfrage eine Rolle 
	 * spielen. Die Eingangstupel werden als Attribute der Klassen genutzt.
	 * 
	 * @throws Exception - kann bei buildClassifier auftreten
	 */
	private void trainInitialData() throws Exception {
		LinkedList<SDFAttribute> sdfAttributes = new LinkedList<SDFAttribute>();
		for (String string : dataAccess.findDataStreamAttr(query.getQuery())) {
			sdfAttributes.add(new SDFAttribute(string));
		}
		SDFAttributeList outputSchema = new SDFAttributeList(sdfAttributes);
		dataAccess.setPrefetchQuery(query, outputSchema);
		
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (SDFAttribute attr : inputSchema) {
			attributes.add(new Attribute(attr.getPointURI()));
		}
		
		LinkedList<String> nominalClasses = new LinkedList<String>();
		for (int i = 0; i < inout.size(); i++) {
			String nominalClass = String.valueOf((dataAccess.getRelevantSQLObjects(inout.get(i).getOut(), null).toString()));
			if (!nominalClasses.contains(nominalClass)) {
				nominalClasses.add(nominalClass);
			}
		}
		
		Attribute classAttribute = new Attribute("theClass", nominalClasses);
		attributes.add(classAttribute);
		
		instances = new Instances(query.getQuery(), attributes, maxClassifyingObjs);
		instances.setClassIndex(attributes.size()-1);
		
		classTuples = new HashMap<String, RelationalTuple<?>>();
		
		for (int i = 0; i < inout.size(); i++) {
			Instance instance = generateWekaInstance(inout.get(i).getIn(), inout.get(i).getOut());
			instances.add(instance);
			classTuples.put(instance.stringValue(instance.classIndex()), inout.get(i).getOut());
		}

		classifier = new NaiveBayesUpdateable();
		
		classifier.buildClassifier(instances);
		
		
	}

	
	
	/**
	 * Generiert ein Weka-Instance-Objekt aus zwei uebergebenden Tupeln.
	 * 
	 * @param attrTuple - das Tupel fuer die Attribute
	 * @param classTuple - das Tupel fuer die entsprechende Klasse
	 * @return Das erzeugt Instance-Objekt
	 */
	private Instance generateWekaInstance(RelationalTuple<?> attrTuple, RelationalTuple<?> classTuple) {
		Instance instance = new DenseInstance(inputSchema.getAttributeCount()+1);
		instance.setDataset(instances);
		
		for (int i = 0; i < attrTuple.getAttributeCount(); i++) {
			try {
			instance.setValue(i, (Double) attrTuple.getAttribute(i));
			} catch (ClassCastException e) {
				instance.setValue(i, attrTuple.getAttribute(i).hashCode());
			}
		}
		try {
			if (classTuple == null) {
				instance.setClassMissing();
			} else {
				instance.setClassValue(dataAccess.getRelevantSQLObjects(classTuple, null).toString());
			}
		} catch (Exception e) {
			//falls die Klasse nicht in den Instanzen enthalten ist, tritt eine Exception auf. 
			//Dann kann die Klasse auf fehlend gesetzt werden.
			instance.setClassMissing();
		}
		return instance;
	}
	
	
	
	/**
	 * Mit Hilfe dieser Methode werden anfangs eine Reihe von Tupeln gesammelt. Aus diesen wird 
	 * dann ein Instances-Objekt erzeugt. Ist die Klassifizierung abgeschlossen, werden die 
	 * eingehenden Tupel zum weiteren Trainineren des Klassifizierers genutzt.
	 * 
	 * @param streamTupel - Das eingehende Tupel. Dieses wird als Klasse dem vorherigen Tupel 
	 * zugeordnet. Beim naechsten einkommenden Tupel werden die Attribute dieses Tupels als 
	 * Attribute der neuen Klasse genutzt.
	 */
	public void addInputTupel(RelationalTuple<?> streamTupel) {
		try {
			
			if (currObj > 0 && currObj <= maxClassifyingObjs) {
				inout.add(new InOutPair<RelationalTuple<?>, RelationalTuple<?>>(attrTuple, streamTupel));
				
				if (maxClassifyingObjs == currObj) {
					trainInitialData();
				}
				
			} else if (currObj >= maxClassifyingObjs){
				Instance instance = generateWekaInstance(attrTuple, streamTupel);
				classifier.updateClassifier(instance);
			}
			currObj++;
			attrTuple = streamTupel;
		} catch (Exception e) {
			//Exception kann bei updateClassifier auftreten.
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * Diese Methode erzeugt eine Liste mit RelationalTupel-Objekten. Von diesen Tupeln wird erhofft, dass
	 * sie in zukuenftigen Anfrage eine Rolle spielen und dementsprechend genutzt werden.
	 * 
	 * @param dataStreamTuple - das eingehende Datenstromtupel.
	 * @return eine Liste mit RelationalTuple-Objekten, welche in Zukunft auftreten koennen.
	 */
	public LinkedList<RelationalTuple<?>> getNextTuples (RelationalTuple<?> dataStreamTuple) {
		LinkedList<RelationalTuple<?>> tuples = new LinkedList<RelationalTuple<?>>();
		String streamTupleString = dataAccess.getRelevantSQLObjects(dataStreamTuple, null).toString();
		
		if (currObj > maxClassifyingObjs) {
			Instance instance = generateWekaInstance(dataStreamTuple, null);
			double[] dist;
			
			try {
				dist = classifier.distributionForInstance(instance);
				for (int a = 0; a < previsionWidth; a++) {
					double max = 0;
					int index = 0;
					
					for (int i = 0; i < dist.length; i++) {
						if (max < dist[i]) {
							max = dist[i];
							index = i;
						}
					}
					dist[index] = 0;
					Attribute classAttribute = instances.classAttribute();
					String classString = String.valueOf(classAttribute.value(index));
					
					if (!classString.equals(streamTupleString)) {
						if (!cache.dataCached(query, classTuples.get(classString))) {
							tuples.add(classTuples.get(classString));
						} 
					}
				}
				
				if (previsionDepth > 0) {
					RelationalTuple<?> tempTuple;
					
					if (tuples.size() > 0) {
						tempTuple = tuples.getFirst();
						for (int a = 0; a < previsionDepth; a++) {
							instance = generateWekaInstance(tempTuple, null);
							dist = classifier.distributionForInstance(instance);
							double max = 0; 
							int index = 0; 
							
							for (int i = 0; i < dist.length; i++) {
								if (max < dist[i]) {
									max = dist[i];
									index = i;
								}
							}
							Attribute classAttribute = instances.classAttribute();
							String classString = String.valueOf(classAttribute.value(index));
							
							if (!classString.equals(streamTupleString)) {
								if (!cache.dataCached(query, classTuples.get(classString))) {
									boolean contain = false;
									for (RelationalTuple<?> tuple : tuples) {
										if (dataAccess.getRelevantSQLObjects(tuple, null).toString().equals(classString)){
											contain = true;
											break;
										}
									}
									if (!contain) {
										tempTuple = classTuples.get(classString);
										tuples.add(tempTuple);
									}
								} 
							}	
						}
					}	
				}
				tuples.addFirst(dataStreamTuple);
				return tuples;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	public IDataAccess getDataAccess() {
		return dataAccess;
	}

	public ICache getCache() {
		return cache;
	}

	public int getPrevisionDepth() {
		return previsionDepth;
	}

	public void setPrevisionDepth(int previsionDepth) {
		this.previsionDepth = previsionDepth;
	}

	public int getPrevisionWidth() {
		return previsionWidth;
	}

	public void setPrevisionWidth(int previsionWidth) {
		this.previsionWidth = previsionWidth;
	}
}





