/*
 * Created on 11.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection;

/**
 * Diese Klasse enthält einige globale Konstanten und Hilfsfunktionen. 
 * @author  Joerg Baeumer
 */
public final class Constants {
	
	public static final boolean debug = true ;

	/**
	 * @uml.property  name="id"
	 */
	private static int id = 0;
	
	/**
	 * Anzahl der geordneten Lösungen, die das CBR-System für ein Retrieval eines 
	 * neuen Problems zur Verfügung stellt 
	 */
	public static final int numberOfRetrieveCases = 3;
	/**
	 * Minimale Größe eines Blocks bei einer 'bursty' Datenrate. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */		
	public static final Integer minBlockSize = new Integer(50);
	/**
	 * Maximale Größe eines Blocks bei einer 'bursty' Datenrate. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */		
	public static final Integer maxBlockSize = new Integer(110);
	/**
	 * Minimale Größe des Austauschpuffers. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */		
	public static final Integer minBufferSize = new Integer(10);
	/**
	* Maximale Größe des Austauschpuffers. Hinweis: Dieser Wert muss mit
	* dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	*/		
	public static final Integer maxBufferSize = new Integer(50);
	/**
	 * Minimale Datenrate. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */			
	public static final Integer minDatarate = new Integer(0);
	/**
	* Maximale Datenrate. Hinweis: Dieser Wert muss mit
	* dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	*/		
	public static final Integer maxDatarate = new Integer(10);
	/**
	 * Minimale Größe des Operatorenspeichers. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */			
	public static final Integer minOperatorMemSize = new Integer(50);
	/**
	* Maximale Größe des Operatorenspeichers. Hinweis: Dieser Wert muss mit
	* dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	*/		
	public static final Integer maxOperatorMemSize = new Integer(100);
	
	/**
	 * Minimale TimeOut-Zeitspanne. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */		
	public static final Integer minTimeOutMilliSec = new Integer(50);
	/**
	 * Maximale TimeOut-Zeitspanne. Hinweis: Dieser Wert muss mit
	 * dem Wert der entsprechenden Entität im Creek-KnowledgeModel übereinstimmen.
	 */		
	public static final Integer maxTimeOutMilliSec = new Integer(100);
	
	/**
	 * 
	 * Diese Methode überprüft, ob ein Wert innerhalb der möglichen Grenzen
	 * für die durch attributeName bezeichnete Konstante liegt. 
	 * 
	 * @param attributeName Bezeichnung des Attributes, etwa 'BlockSize'
	 * @param attributeValue der zu überprüfende Wert
	 * @return Minimalwert, wenn Wert kleiner als Minimalwert; Maximalwert, wenn 
	 * 			Wert größer als Maximalwert; attributeValue sonst
	 */	
	public static Integer checkAttribute(String attributeName, Integer attributeValue){
		
		// Wenn innerhalb der Grenzen, liefer den Wert zurück		
		if (((getValue(attributeName,1)).compareTo(attributeValue) <= 0) &&
			((getValue(attributeName,2)).compareTo(attributeValue) >= 0)) {
			return attributeValue;}
		// kleiner als untere Grenze, liefer Minimalwert zurück
		else if	((getValue(attributeName,1)).compareTo(attributeValue) < 0)
			return getValue(attributeName,1);
		//	größer als obere Grenze, liefer Maximalwert zurück
		else if ((getValue(attributeName,1)).compareTo(attributeValue) > 0)
			return getValue(attributeName,2);
		// sonst Minimalwert
		else 
			return getValue(attributeName,1);	
		
	}

	/*
	 * Gibt den minimalen Wert (minmax = 1) bzw. den Maximalwert (minmax = 2)
	 * eines Attributs zurück
	 * 
	 */
	private static Integer getValue(String attributename, int minmax){

		switch(minmax){
			
			case 1: 
			
				if (attributename == "BlockSize") 
							return minBlockSize; 
				else if (attributename == "BufferSize") 
							return minBufferSize; 
				else if (attributename == "Datarate") 
							return minDatarate; 
				else if (attributename == "TimeOutMilliSec") 
							return minTimeOutMilliSec; 
				else return new Integer(0);			
				
			case 2:
			
				if (attributename == "BlockSize") 
					return maxBlockSize; 
				else if (attributename == "BufferSize") 
					return maxBufferSize; 
				else if (attributename == "Datarate") 
					return maxDatarate; 
				else if (attributename == "TimeOutMilliSec") 
					return maxTimeOutMilliSec; 	
				else return new Integer(0);	
						
		}
		
		return new Integer(0);

	}
	
	/**
	 * Liefert eine eindeutige ID (insbesondere zur Identifizierung von Ausführungssituationen)
	 * @return  eindeutige ID aus aktueller Zeit und Autoinkrement-Wert
	 * @uml.property  name="id"
	 */
	public static String getId() {
		
		if (id < 32000) 
			id++;
		else 
			id = 1;
					
		return System.currentTimeMillis() + new Integer(id).toString();
	}
	
}
