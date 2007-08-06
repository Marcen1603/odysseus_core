package mg.dynaquest.sourceselection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author  Jurij Henne  Eine bespielhafte Umsetzung eines ConstantRepository. Es speichert lediglich   die Zuordnungen von qualifizierenden Attributnamen zu möglichen Belegungen in Form von   KonstantenListen, Intervalldefinitionen und Anfragen an Hilfsquellen.  Es existieren keinerlei Assoziationen zwischen den einzelnen Attributen in   Form von IF "Autor = 'Defou' THEN Titel ='Robinson Crusoe'". D.h es lassen sich nur entweder alle oder garkeine  Belegungen zu einem Attribut auslesen.  (siehe DA:Strukturelle Kompesationen zum Zugriff auf Web-Quellen,Abschnitt 4.3.2)  Das ConstantRepository wird vom RepositoryManager initialisiert und angesprochen werden. Sie kann nur  die Zuordnungen liefern, diese werden dann vom RepositoryManager aufbereitet und an aufrufende CompensateAction  in Form von SDFConstantSets zurückgeliefert.
 */

public class ConstantRepository {

	/**
	 * String = Qualname des Attributes, ArrayList(Values für das Attribut)
	 * @uml.property  name="constantList"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.lang.String" qualifier="get:java.lang.String java.util.ArrayList"
	 */
	private HashMap <String, ArrayList> constantList = new HashMap <String, ArrayList>();
	
	/**
	 * @uml.property  name="attributes" multiplicity="(0 -1)" dimension="1"
	 */
	final  String [] attributes = {"Autor", "Titel", "ISBN", "Verlag", "AutorAnfrage1", "TitelAnfrage1"};
	/**
	 * @uml.property  name="attributeValues" multiplicity="(0 -1)" dimension="2"
	 */
	final  String [][] attributeValues = 
	{
		{"Verne", "Wells", "Defoe", "Shakespeare"},
		{"Von der Erde zum Mond", "Der Krieg der Welten", "Robinson Crusoe", "Othello", "In 80 Tagen um die Welt"},
		{"0809599619", "3898859169", "3899964489", "3257201710", "3899964489"},
		{"Springer", "bHV", "Langenscheidt", "Klett"},
		// Folgende Belegungen lassen sich DERZEIT NICHT direkt über RepositoryManager.getConstantList() abfragen, 
		// sondern simulieren das Ergebnis der Anfrage auf einer Hilfsquelle für ein bestimmtes Attribut
		// @see mg.dynaquest.sourceselection.action.RepositoryManager.getSourceList()
		{"Pushkin", "Lermontov", "Tolstoj", "Strugazkie"},  // "AutorAnfrage1"
		{"Eugen Onegin", "Der Dämon", "Krieg und Frieden" , "Es ist schwer, Gott zu sein"} // "TitelAnfrage1"
		
	};
	
	/**
	 * String = Qualname des Attributes, ArrayList(0=Start, 1=Ende, 2=Granularität)
	 * @uml.property  name="intervalList"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.lang.Integer" qualifier="attrQualName:java.lang.String java.util.ArrayList"
	 */
	private HashMap <String, ArrayList> intervalList = new HashMap <String, ArrayList>();
	
	/**
	 * @uml.property  name="attributes2" multiplicity="(0 -1)" dimension="1"
	 */
	final String [] attributes2 = {"Bewertung", "Jahr", "Preis"};
	/**
	 * @uml.property  name="attributeValues2" multiplicity="(0 -1)" dimension="2"
	 */
	final  int [][] attributeValues2 = 
	{
		{1, 5, 1},
		{1998, 2002, 1},
		{6,10, 2}
	};

	/**
	 * String = Qualname des Attributes, ArrayList(Queries an Quellen)
	 * @uml.property  name="sourceList"
	 * @uml.associationEnd  qualifier="attrQualName:java.lang.String java.util.ArrayList"
	 */
	private  HashMap <String, ArrayList> sourceList = new HashMap <String, ArrayList>();
	
	/**
	 * @uml.property  name="attributes3" multiplicity="(0 -1)" dimension="1"
	 */
	final  String [] attributes3 = {"Autor", "Titel"};
	/**
	 * @uml.property  name="attributeValues3" multiplicity="(0 -1)" dimension="2"
	 */
	final  String [][] attributeValues3 = 
	{
		{"AutorAnfrage1", "AutorAnfrage2"},  // Platzhalter für spätere Anfragen
		{"TitelAnfrage1", "TitelAnfrage2"},   // Platzhalter für spätere Anfragen
		{"ISBNAnfrage1", "ISBNAnfrage1"}   // Platzhalter für spätere Anfragen
	};
	
	public ConstantRepository()
	{
		init();
	}
	
	/** Initialisiert Dummy-Repository mit Values, darüber hinaus keine besondere Bedeutung*/
	private  void init()
	{	
		// Listenvariablen
		for(int i = 0; i<attributes.length; i++)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j<attributeValues[i].length; j++)
				temp.add(attributeValues[i][j]);
			constantList.put(attributes[i], temp);
		}
		// Intervall-Variablen
		for(int i = 0; i<attributes2.length; i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j<attributeValues2[i].length; j++)
				temp.add(new Integer(attributeValues2[i][j]));
			intervalList.put(attributes2[i], temp);
		}	
		// Quellen-Variablen
		for(int i = 0; i<attributes3.length; i++)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j<attributeValues3[i].length; j++)
				temp.add(attributeValues3[i][j]);
			sourceList.put(attributes3[i], temp);
		}					
	}

	
	public HashMap getconstantList()
	{
		return this.constantList;
	}
	
	/**
	 * @return  the intervalList
	 * @uml.property  name="intervalList"
	 */
	public HashMap getIntervalList()
	{
		return this.intervalList;
	}
	
	/**
	 * @return  the sourceList
	 * @uml.property  name="sourceList"
	 */
	public HashMap getSourceList()
	{
		return this.sourceList;
	}
}
