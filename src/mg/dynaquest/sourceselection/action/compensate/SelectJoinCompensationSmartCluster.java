package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author  Jurij Henne 
 * 
 * Die CompensateAction versucht die Menge der Eingabepläne (Teilziele) so miteinander zu verknüpfen, dass ein 
 * Plan ensteht, der alle gewünschten Ausgabeattribute zurückliefert.
 * 
 * Normalerweise brechen SelectJoin-Algorithmen (CHAIN / Partition) die Verarbeitung ab,
 * wenn es nicht gelingt den ersten Cluster aufzubauen, sprich, wenn es keinen Plan(Teilziel) gibt, der mit den 
 * verfügbaren Attributbelegungen ausgeführt werden kann. Gleichzeitig findet die Kompensation der 
 * fehlenden Eingabeattribute erst nach diese Kompensationsstufe statt.
 * 
 * Die Überlegung ist es nun, eine Menge von  Plänen zu generieren, die bewusst fehlende Eingabeattributenbelegungen
 * akzeptieren, solange eine solche Annahme zu einem Plan führt, der alle Ausgabeattribute berücksichtigt.
 * 
 * Die Umsetzung des Ansatzes hat sich in bereits in Prototyp-Stadium bewährt und wurde mit dieser Klasse weiter 
 * ausgebaut. Es ist trotzdem lediglich eine Studie, daher darf höchstens ein Eingabeattribut mit der fehlenden 
 * Belegung als belegt angenommen werden(=sicher kompensierbar durch nächste Stufe). 
 * 
 * Die SMART-Cluster wird von Cluster abgeleitet und erbt somit alle wichtigen Features:
 * 1. Permutation der Pläne innerhalb der Cluster, wenn kein Kostenmodell vorliegt oder abgeschaltet ist.
 * 2. Optionaler Abbruch der Verarbeitung, sobald alle Teilziele gefunden wurden, die alle notwendigen Ausgabeattribute
 * 	  zurückliefern.
 * 3. Sollte eine Quelle mehrere AnnotatedPlans mit gleichem GOP(GlobalOutputPattern) aufweisen, kann jeweils nur ein Plan
 *    in der Verarbeitung berücksichtigt werden, da eine erneute Ausführung auf der Quelle bswp. mit anderem GIP keine neuen 
 *    Ergebnisse mitbringt und daher sinnlos und ineffizient ist. 
 * 3a.Liefert Grundlage für weitere Optimierung, indem bswp. generell solche Teilziele aus der Verarbeitung 
 * 	  ausgeschlossen werden(in den Ausführungsplan NICHT integriert), die keine neuen Ausgabeattribute liefern, als die 
 * 	  von den bisher in den Aushführungsplan eingebundenen Teilzielen schon bereits geliefert wurden.
 * 
 * Weitere Informationen bitte 
 * der DA-"Strukturelle Kompensationen zum Zugriff auf Web-Quellen" entnehmen (Abschnitt 4.3.1) und der JavaDoc zu 
 * den Cluster-CompensateAction-Klassen entnehmen.
 */

public class SelectJoinCompensationSmartCluster extends  SelectJoinCompensationCluster {

	/**
	 * Anzahl der Attribute, die als kompensierbar abgenommen werden DÜRFEN. Festgelegt auf 1, da Prototyp. In der DA-"Strukturelle Kompensationen zum Zugriff auf Web-Quellen" wurde gezeigt, wie sensibel der CLUSTER-Ansatz auf solche Annahmen reagieren kann. Daher ist höchste Vorsicht bei der Festlegung dieses Parameters geboten.
	 * @see  this#getNewHopeAttribute()
	 * @uml.property  name="flexibility"
	 */
	private int flexibility = 1; 
	

    public SelectJoinCompensationSmartCluster()
    {
    	super();
    }
	
    public SelectJoinCompensationSmartCluster(SDFQuery query)
    {
    	super(query);    
    }

    /**
     * Die Ausführung der ersten Phase muss für SMART-Cluster angepasst werden. Es sind prinzipiell mind zwei Ansätze 
     * möglich:
     * 1. Beim Aufbauen des ersten Clusters wird einer der Pläne als ausführbar angenommen (Anzahl seiner nicht belegten 
     *    Eingabeattribute darf die flexibility selbstverständlich überschreiten).
     *    Dieser Plan wird nun dem Cluster hinzugefügt und die Verarbeitung sofort mit de Aufbau des zweiten Clustes 
     *    fortgesetzt. Ab dem zweiten Cluster erfolgt die Verarbeitung dann identisch zum Standard-Cluster.
     * 2. Bevor wir die Cluster aufbauen, suchen wir ein Attribut bzw. Menge der Attribute, von dem/den wir annehmen können,
     *    diese seien kompensierbar. Wir nennen diese Attribute HOPE-Attribute ;)
     *    @see this#getNewHopeAttribute()
     *    Nun hängt die Wahscheinlichkeit, einen ausführbaren PLan zu finden, sehr stark von der Ausführungsreihenfolge der
     *    Teilziele ab. Die Annahme über kompensierte Eingabeattribute priorisiert auf diese Weise Teilziele, die dieses
     *    Attribut in ihrem GIP (Global Input Pattern) enthalten . Somit kann passieren, das ein solches zwar
     *    Teilziel zuerst ausgeführt wird, jedoch keine weiteren fehlenden Belegungen für nachfolgende Teilziele liefert
     *    und somit kein Plan gefunden wird.
     *    Aus diesem Grund, wird für JEDES HOPE-Attribut getestet, ob mit diesem die Phase 1 abgeschlossen werden kann.
     *    Sobald dies der Fall ist, wird die Phase1 beendet.
     *    
     * @param toCompensate Menge der zu kompensierenden Pläne
     * @return Menge vor Cluster
     */
    protected ArrayList <ArrayList> buildClusters(ArrayList <AnnotatedPlan> toCompensate)
    {
    	//da wir u.U mehrere Durchläufe machen, benötigen wir toCompensate als Backup und arbeiten auf dem workingSet
    	ArrayList<AnnotatedPlan> workingSet = new ArrayList<AnnotatedPlan> ();
    	ArrayList <ArrayList> clusterSet = new ArrayList<ArrayList>(); // gesuchte ClusterSet
    	SDFAttributeList predicateAttributes = new SDFAttributeList(getQuery().getPredicateAttributes());
    	SDFAttributeList newHopeAttrSet = getNewHopeAttribute(toCompensate, predicateAttributes);  // Attribute zur Annahme der Kompensierbarkeit
    	// da wir nicht wissen, ob ein bestimmtes Attribut zu Generierung eines brauchbaren Plans führt, probieren wir
    	// alle nacheinander, bis die erste Phase so abgeschlossen ist, dass alle gesuchten Ausgabeattribute geliefert werden#
    	// natürlich kann es auch passieren, dass u.U. kein Attribut dazu führt.
    	newHope:
    	for(int a = 0; a<newHopeAttrSet.getAttributeCount(); a++) // alle! Attribute durchlaufen
    	{
			logger.debug("> ############################ NEW HOPE-ATTRIBUTE: "+newHopeAttrSet.getAttribute(a)+" #############################################");

			//workingSet.clear();
			//workingSet.addAll((ArrayList<AnnotatedPlan>)toCompensate);
			workingSet = new ArrayList<AnnotatedPlan>(toCompensate);
           	clusterSet = new ArrayList<ArrayList>(); // bei jedem Durchlauf neu initialisieren
           	this.clusteredPlans  = new ArrayList<AnnotatedPlan>(); // bei jedem Durchlauf neu initialisieren
           	predicateAttributes = new SDFAttributeList(getQuery().getPredicateAttributes()); // bei jedem Durchlauf neu initialisieren
    		predicateAttributes.addAttribute(newHopeAttrSet.getAttribute(a)); //um das Hope-Attribut erweitern
    		logger.debug("> WorkingSet-Size: "+workingSet.size()+"/ toCompensate-Size: "+toCompensate.size());   	
    		while(workingSet.size()>0)
    		{
	    		ArrayList <AnnotatedPlan> newCluster = new ArrayList<AnnotatedPlan>(); // Aktuelle Cluster
	    		// WICHTIG: je zwei unterschiedliche Annotated EINER Beschreibung(mehr als ein Zugrifsmuster) werden als
	    		// eigenständige Pläne betrachtet, so dass der fertige logische Plan unter Umständen entsprechend soviele Male auf eine und
	    		// dieselbe Quelle zugreifen wird, für jedes Zugrifsmuster einmal
	    		for(int i=0; i<workingSet.size();i++)
	    		{
	    			AnnotatedPlan actPlan = (AnnotatedPlan)workingSet.get(i); // Plan holen
	    			SDFAttributeList inputAttributes = actPlan.getRequiredInAttributes(); // Required-Eingabeattribute von den Plan holen
	    			logger.debug("> ------------------------- NEW LOOP -----------------------------------");
	    			logger.debug("> NextPlan ["+actPlan.getIndex()+"]/ GIP: "+actPlan.getGlobalInputPattern()+"/ GOP: "+actPlan.getGlobalOutputPattern());
	    			// Hier Unterschied zu normalen Version. Beim ersten Cluster werden Annahmen über die Kompensierbarkeit der nicht belegten Eingabeattribute gemacht
	     			if(actPlan.inSetWithSameGOP(clusteredPlans))
	        			logger.debug("> Plan ["+actPlan.getIndex()+"] SAME GOP => KICKED");   
	    			if(SDFAttributeList.subset(inputAttributes,predicateAttributes) &&  !actPlan.inSetWithSameGOP(clusteredPlans))			
	     			{		    	 
			     		StringBuffer sb = new StringBuffer();
			     		for(int l=0; l<predicateAttributes.getAttributeCount(); l++) 	 // NUR DEBUG
			    			sb.append(" "+predicateAttributes.getAttribute(l));
			    		logger.debug("> InputAttributes of Plan ["+actPlan.getIndex()+"] GIP:"+actPlan.getGlobalInputPattern()+" are covered with PredicateAttributes {"+sb+" }");

			    		newCluster.add(actPlan);
			    		this.clusteredPlans.add(actPlan);
	    		    	logger.debug("> Adding Plan to Cluster ["+actPlan.getIndex()+"]/ GIP:"+actPlan.getGlobalInputPattern());
	     			}
	    		}
    		
	    		// Cluster gebildet => Analyse
				if(newCluster != null &&  !newCluster.isEmpty())  // aktueller Cluster nicht leer
				{
					clusterSet.add(newCluster); // Cluster in die Menge aufnehmen
					logger.debug(">  PlanCount in NewCluster: "+newCluster.size());
		    		for(int i=0; i<newCluster.size();i++) // Nun alle Annotated im Cluster durchlaufen und....
		    		{
		    			logger.debug("> Plans remanining: "+workingSet.size()+" Preparing for cleanup..");
		    			//...die Output-Attribute des Plan in die Liste aufnehmen(keine Dubletten, darum kein addAttributes()
		    			AnnotatedPlan nextPlan = (AnnotatedPlan)newCluster.get(i); // Plan holen
		    			logger.debug("> Removing Plan ["+nextPlan.getIndex()+"]/ GIP: "+nextPlan.getGlobalInputPattern());
		    			// Ausgabeattribute des Plans der Menge der "bestückten" Eingabeattribute (predicateAttributes) hinzufügen
		    			predicateAttributes = SDFAttributeList.union(predicateAttributes, nextPlan.getGlobalOutputPattern().getAllAttributes());
		    			workingSet.remove(nextPlan); // und aus der toCompensate-Liste entfernen
		    	   	    logger.debug("> Plancount after cleanup: "+workingSet.size());
		    		}
				}
				else // keine Cluster mehr möglich, handeln
				{
					if(clusterSet.isEmpty()) // Bisher überhaupt keine Cluster gebildet, kann man nichts machen
						break newHope; // nächstes Attribut probieren
					// sonst  überprüfen ob alle erforderlichen Attribute zurückgeliefert werden
					// TODO: Also ob da wirklich nichts kompensiert werden soll, ist zu überlegen, denn
					// alternativ könnte man die NICHT vollständig kompensierten pläne den toCompensate hinzufügen
					SDFAttributeList outputPattern =  getOutputPatternForClusterSet(clusterSet);
					SDFAttributeList queryRequiredAttr =  getQuery().getRequiredAttributes();
					if(SDFAttributeList.subset(queryRequiredAttr,outputPattern)) // alle QueryAttribute im Plan-GOP
						return clusterSet;
					// trifft nichts zu, dann haben wir  ersteinmal ausreichend viele Cluster für einen Kompensationsplan
					// gesammelt und die verbliebenen sollten verworfen werden, damit compensate() terminiert
					while(workingSet.size()>0)  
					{
						AnnotatedPlan pl = (AnnotatedPlan)workingSet.get(workingSet.size()-1);
		    			logger.debug("> Removing Plan ["+pl.getIndex()+"]/ GIP: "+pl.getGlobalInputPattern()); 
		    			workingSet.remove(workingSet.size()-1); // pop ;)

					}						
				}// gibts ne dritte möglichkeit? TODO zum Nachdenken
	    	} // while
    	}
	    return clusterSet;
    }

    /**
     * ACHTUNG: lediglich ein PROTOTYP als ProveOfConcept und dementsprechend NAIV bzw Simple umgesetzt.
     * Bestimmt ein Attribut (auch denkbar: Menge von Attributen), welches am Besten für die Annahme 
     * der Kompensierbarkeit passen soll. Dafür werden sowohl alle Teilziele(AnnotatedPlan) analysiert, sowie die Menge
     * der von den Query-Prädikaten "abgedekte" Menge der Attrbute betrachtet. "Abgedeckt" heisst: die Belegungen sind da.
     * 
     * Ansatz: Sucht zunächst alle Attribute zusammen, die folgende Eigenschaft erfüllen: 
     * Das Attribut ist das einzige notwendige Attribut, welches im GIP des Teilziels vorkommt und keine Belegungen 
     * durch die Query hat.
     * Anschliessende wird die Menge an Phase 1 übergeben.
     * 
     * TODO: Es könnte sinnvoll sein, alle Attribute vor ihren Übergabe darauf zu Prüfen, ob sie wirklich
     * kompensierbar sind. Zu diesem Zweck sollte der RepositoryManager eine entsprechende Methode anbieten, 
     * die jedes Attribut nach dieser Eigenschaft testet (siehe RepositoryManager#hasValues()). Auf diese Weise 
     * könnte eine Generierng der Pläne verhindert werden, die in der zweiten Stufen wieder aussortiert werden.
     * 
     * @param toCompensate
     * @return
     */
    protected SDFAttributeList getNewHopeAttribute(ArrayList <AnnotatedPlan> toCompensate, SDFAttributeList predicateAttributes)
    {
	 	this.logger.debug("> IN HOPE:"+toCompensate.size());
    	HashMap <SDFAttribute, Integer>  assocSet = new HashMap <SDFAttribute, Integer>();
    	// Zunächst alle Teilziel durchlaufen und alle EINZELNE(!) Attribute hinzufügen, die zur Ausführung fehlen
    	for(int i = 0; i<toCompensate.size(); i++)
    	{
    		AnnotatedPlan nextPlan = toCompensate.get(i);
    		SDFAttributeList planReqAttribs = nextPlan.getGlobalInputPattern().getRequiredInAttributes();
 
     		StringBuffer sb = new StringBuffer();
     		for(int l=0; l<planReqAttribs.getAttributeCount(); l++) 	 // NUR DEBUG
    			sb.append(" "+planReqAttribs.getAttribute(l));
    		logger.debug("> Plan ["+nextPlan.getIndex()+"]  GIP:"+nextPlan.getGlobalInputPattern()+" > Required Attributes: {"+sb+" }");
	    	 
    		SDFAttributeList _attrSet = SDFAttributeList.difference(planReqAttribs, predicateAttributes);
 
     		sb = new StringBuffer();
     		for(int l=0; l<_attrSet.getAttributeCount(); l++) 	 // NUR DEBUG
    			sb.append(" "+_attrSet.getAttribute(l));
    		logger.debug("> UNCOVERED SET "+_attrSet.getAttributeCount()+": {"+sb+" }");
    		
	    	 
    		if(_attrSet.getAttributeCount()<=this.flexibility) // nur wenn Anzahl der Attribute der Grenze entspricht
    		{
    	    	for(int j = 0; j<_attrSet.getAttributeCount(); j++)
    	    	{
    	    		SDFAttribute nextAttrib = _attrSet.getAttribute(j);
	      	    	this.logger.debug("> INFO: nextAttrib: "+nextAttrib);

 	    			if(assocSet.containsValue(nextAttrib))
	    			{
 	      	    		this.logger.debug("> IN HOPE 2: "+i+" / "+j+" /"+_attrSet.getAttributeCount());

	    				Integer _int = assocSet.get(_attrSet.getAttribute(j))+1;
	    				assocSet.remove(_attrSet.getAttribute(j));
	    				assocSet.put(_attrSet.getAttribute(j), _int);
	    			}
	    			else
	    			  assocSet.put(_attrSet.getAttribute(j), 1);
    	    	}
    		}
    	}
    	// Map durchlaufen und Attribut mit dem besten Count auswählen <---DISABLED
    	SDFAttributeList newHopeAttrSet = new SDFAttributeList();
//    	SDFAttributeList newHopeAttr = null;
//    	int bestCount = 0;
		Iterator iter = assocSet.entrySet().iterator();
		while ( iter.hasNext() ) 
		{	
			Map.Entry entry = (Map.Entry) iter.next();
		 	this.logger.debug("> Content of assocSet(Hope):"+entry.getKey() + ":" +entry.getValue());
//			Nur wenn ein Attribut benötigt wird wird
//			
//			if((Integer)entry.getValue()>bestCount)  
//			{
//				newHopeAttr = (SDFAttribute)entry.getKey(); // momentan am geeignetsten
//				bestCount = (Integer)entry.getValue(); // neue bestanzhal festlegen
//			}	
		 	//Da wir aber noch keinen zuverlässigen Ansatz haben, nehmen wir zunächst alle die finden.
	    	
		 	newHopeAttrSet.addAttribute((SDFAttribute)entry.getKey());
		}	
    	return newHopeAttrSet;
    }


    /*
     *  (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#initInternalBaseValues(org.w3c.dom.NodeList)
     */
	public void initInternalBaseValues(NodeList children)
	{
		for (int i = 0; i < children.getLength(); i++)
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("costControlled"))
				this.costControlled = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("planCostLimit"))
				this.M = Double.parseDouble(cNode.getFirstChild().getNodeValue());
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("flexibility"))
				this.flexibility = Integer.parseInt(cNode.getFirstChild().getNodeValue());
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("searchMinimalPlan"))
				this.searchMinimalPlan = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
	 */
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue)
    {
    	xmlRetValue.append(baseIndent + "<costControlled>");
    	xmlRetValue.append((this.costControlled)?1:0);
    	xmlRetValue.append("</costControlled>\n");

    	xmlRetValue.append(baseIndent + "<planCostLimit>");
    	xmlRetValue.append(this.M);
    	xmlRetValue.append("</planCostLimit>\n");
    	
    	xmlRetValue.append(baseIndent + "<flexibility>");
    	xmlRetValue.append(this.flexibility);
    	xmlRetValue.append("</flexibility>\n");
    	
    	xmlRetValue.append(baseIndent + "<searchMinimalPlan>");
    	xmlRetValue.append((this.searchMinimalPlan)?1:0);
    	xmlRetValue.append("</searchMinimalPlan>\n");
    }

    public static void main(String[] args)
    {

    }
}
