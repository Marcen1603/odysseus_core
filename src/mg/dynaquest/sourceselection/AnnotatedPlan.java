package mg.dynaquest.sourceselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryoptimization.trafo.ProcessPlanTransform;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeBindung;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPairList;
import mg.dynaquest.sourcedescription.sdf.description.SDFConstantSetSelectionType;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBinding;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBindingFactory;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFIntensionalSourceDescription;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputAttributeBinding;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFManyToOneMapping;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFOneToManyMapping;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFOneToOneMapping;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMapping;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElementSet;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;
import mg.dynaquest.sourceselection.mapping.AttributeMapping;
import mg.dynaquest.sourceselection.mapping.ConversionFunction;
import mg.dynaquest.sourceselection.mapping.ConversionFunctionFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author  Marco Grawunder
 */
public class AnnotatedPlan {

    /**
	 * @uml.property  name="globalInputPattern"
	 */
    SDFInputPattern globalInputPattern = null;

    /**
	 * @uml.property  name="compensated"
	 */
    boolean compensated = false;
     /**
	 * @uml.property  name="globalOutputPattern"
	 */
    SDFOutputPattern globalOutputPattern = null;

    List<SDFSourceDescription> sourceDescriptionList = new ArrayList<SDFSourceDescription>();
    List<AnnotatedPlan> baseAnnotatedPlans = new ArrayList<AnnotatedPlan>();
    
    // Der oberste Operator im Plan
    // Kompensationen ergänzen i.d.R. neue Operatoren
    /**
	 * @uml.property  name="top"
	 * @uml.associationEnd  
	 */
    AlgebraPO top = null;

    // Soll bei toString der volle Text erscheinen?
    /**
	 * @uml.property  name="toStringVerbose"
	 */
    private boolean toStringVerbose = true;
    
    static int planCounter = 0;
    /**
	 * @uml.property  name="index"
	 */
    private int index;
    
    /**
	 * @return  the index
	 * @uml.property  name="index"
	 */
    public int getIndex(){return index;}

    /**
     * @return Returns the globalInputPattern.
     * 
     * @uml.property name="globalInputPattern"
     */
    public synchronized SDFInputPattern getGlobalInputPattern() {
        return globalInputPattern;
    }

    /**
     * @param globalInputPattern The globalInputPattern to set.
     * 
     * @uml.property name="globalInputPattern"
     */
    public synchronized void setGlobalInputPattern(SDFInputPattern globalInputPattern) {
        this.globalInputPattern = globalInputPattern;
    }

    /**
     * @return Returns the globalOutputPattern.
     * 
     * @uml.property name="globalOutputPattern"
     */
    public synchronized SDFOutputPattern getGlobalOutputPattern() {
        return globalOutputPattern;
    }

    /**
     * @param globalOutputPattern The globalOutputPattern to set.
     * 
     * @uml.property name="globalOutputPattern"
     */
    public synchronized void setGlobalOutputPattern(
        SDFOutputPattern globalOutputPattern) {
        this.globalOutputPattern = globalOutputPattern;
    }

    public AnnotatedPlan(SDFSourceDescription sourceDescr, int patternNo, SDFQuery query) throws SDFException {
    	this.sourceDescriptionList.add(sourceDescr);
        initPlan(sourceDescr, patternNo, query);
        increaseCounter();
    }
        
    public AnnotatedPlan(AnnotatedPlan aPlan){
    	globalInputPattern = new SDFInputPattern(aPlan.globalInputPattern);
    	compensated = aPlan.compensated;
    	globalOutputPattern = new SDFOutputPattern(aPlan.globalOutputPattern);
    	sourceDescriptionList = new ArrayList<SDFSourceDescription>(aPlan.sourceDescriptionList);
        baseAnnotatedPlans = new ArrayList<AnnotatedPlan>(aPlan.baseAnnotatedPlans);
        // TODO: Das Folgende richtig auslagern (nicht in ProcessPlanTransform lassen!)
        top = (AlgebraPO)ProcessPlanTransform.cloneTree(aPlan.top);
        toStringVerbose = aPlan.toStringVerbose;
        increaseCounter();
    }
    
    /**
     * 
     */
    public  AnnotatedPlan() {
        // Dient dazu, dass ein leerer AP erzeugt werden kann
        increaseCounter();
    }

    private void increaseCounter(){
        index = planCounter++;
    }
    
    /**
     * Initialisiert den Plan basierend auf den im Konstruktor festgelegten
     * Paramtern
     * @param query 
     * @throws SDFException
     */
    private void initPlan(SDFSourceDescription sourceDescr, int patternNo, SDFQuery query) throws SDFException {
    	//System.out.println("Bin im ANNOTATED");
        SDFInputPattern localInputPattern = null;
        SDFOutputPattern localOutputPattern = null;
        ArrayList<AttributeMapping> inAttributeMappings = null;
        ArrayList<AttributeMapping> outAttributeMappings = null;
        
        SDFIntensionalSourceDescription intDescr = sourceDescr.getIntDesc();
        
        localInputPattern = sourceDescr.getIntDesc().getAccessPattern(
                patternNo).getInputPatt();
        //System.out.println("localInputPattern"+localInputPattern);
        localOutputPattern = sourceDescr.getIntDesc().getAccessPattern(
                patternNo).getOutputPatt();
        //System.out.println("localOutputPattern"+localOutputPattern);

        
        // inmappings und das globale InputPattern ermitteln
        ArrayList<SDFSchemaMapping> inmappings = new ArrayList<SDFSchemaMapping>();
        this.globalInputPattern = determineGlobalInputPattern(
                localInputPattern, intDescr, inmappings);
        ArrayList<SDFSchemaMapping> outmappings = new ArrayList<SDFSchemaMapping>();
        this.globalOutputPattern = determineGlobalOutputPattern(
                localOutputPattern, intDescr, outmappings);

        // Die beiden Pattern liegen nur vor, was nun zu tun ist, die Mappings
        // zu spezifzieren, die das globale InputPattern auf das lokale
        // InputPattern
        // abbilden,
        inAttributeMappings = determineAttributeMappings(inmappings,
                globalInputPattern, localInputPattern, true);
        // sowie die Abbildung des lokalen OutputPattern auf das
        // globale OutputPattern
        outAttributeMappings = new ArrayList<AttributeMapping>();
        outAttributeMappings = determineAttributeMappings(outmappings, localOutputPattern,
        	        globalOutputPattern, false);
        // Jetzt den Zugriffsoperator definieren
        top = new SchemaTransformationAccessPO();
        // Auf welche Quelle soll er zugreifen?
        ((AccessPO)top).setSource(sourceDescr.getAboutSource());
        top.setOutElements(globalOutputPattern.getAllAttributes());
        // TODO: Erst mal nur die aufsteigend sortierten Attribute ... 
        top.setSorted(globalOutputPattern.getSortAscAttributes());
        ((SchemaTransformationAccessPO)top).setInAttributeMappings(inAttributeMappings);
        ((SchemaTransformationAccessPO)top).setOutAttributeMappings(outAttributeMappings);
        ((SchemaTransformationAccessPO)top).setInputAttributes(globalInputPattern.getAllAttributes());
        ((SchemaTransformationAccessPO)top).setLocalInputAttributes(localInputPattern.getAllAttributes());
        ((SchemaTransformationAccessPO)top).setLocalOutputAttributes(localOutputPattern.getAllAttributes());
        ((AccessPO)top).setSource(sourceDescr.getAboutSource());       
        ((SchemaTransformationAccessPO)top).setLocalAttributeCompareOperatorAssignment(localInputPattern.getAttributeCompareOperatorAssignment());
        // Jetzt schon die Eingabewerte aus der Anfrage einfügen ... später kommt
        // man nur noch schwer daran
        ((SchemaTransformationAccessPO)top).setGlobalInputValues(query.getInputVectorAssignment(((SchemaTransformationAccessPO)top).getInputAttributes()));
        ((SchemaTransformationAccessPO)top).setQueryPredicates(query.getQueryPredicates(((SchemaTransformationAccessPO)top).getInputAttributes()));
        ((SchemaTransformationAccessPO)top).setSessionId(query.getSessionId());
        //((AccessPO)top).setSDFQuery(query);
        // Jetzt müssen noch die Operatoren gesetzt werden, die von der Quellen verwendet werden sollen (falls mehrere vorliegen)
        // Falls die Quelle den Operator nicht anbietet, wird per default der erste gewählt
        ((SchemaTransformationAccessPO)top).setPreferedGlobalInputOperators(query.getInputOperatorAssigment(((SchemaTransformationAccessPO)top).getInputAttributes()));
        
    }

    /**
     * 
     * @param mappings
     *            Die Menge der Mappings, die das from Pattern mit dem toPattern
     *            verbindet
     * @param fromPattern
     *            Das Ausgangspattern, d.h. von welchem Pattern soll die
     *            Abbildung ausgehen
     * @param toPattern
     *            Das Zielpattern, d.h. auf welches Pattern soll angebildet
     *            werden
     * @param fromGlobalToLocal
     *            Da die Mappings immer vom lokalen auf das globale Schema
     *            definiert sind, muss hier angegeben werden, wenn eine
     *            Abbildung vom globalen auf das lokale erfolgen soll 
     * @return Eine ArrayList mit den AttributMappings (Elemente sind vom Typ
     *         AttributeMapping)
     */
    private static ArrayList<AttributeMapping> determineAttributeMappings(ArrayList mappings,
            SDFPattern fromPattern, SDFPattern toPattern,
            boolean fromGlobalToLocal) {
        ArrayList<AttributeMapping> attributeMappings = new ArrayList<AttributeMapping>();
        for (int i = 0; i < mappings.size(); i++) {
            SDFSchemaMapping mapping = (SDFSchemaMapping) mappings.get(i);
            SDFFunction mapFunc = mapping.getMappingFunction();
            if (fromGlobalToLocal) {
                // Wenn die Abbildung vom globalen auf das lokale Schema gemacht
                // werden
                // soll, dann benötigt man die Umkehrfunktion, die die
                // Übertragung vom
                // globalen auf das lokale Schema vornimmt!
                if (mapFunc != null) {
                    mapFunc = mapFunc.getReverseFunction();          
                 }
            }
            ConversionFunction conv = ConversionFunctionFactory
                    .getFunction(mapFunc);
            SDFSchemaElementSet inAttribs = mapping.getInElements();
            SDFSchemaElementSet outAttribs = mapping.getOutElements();
            if (fromGlobalToLocal) {
                // Achtung. Die Mappings gehen *immer* von local nach global,
                // hier
                // interessiert aber anders herrum. Deswegen die folgenden
                // Zuweisung!
                outAttribs = mapping.getInElements();
                inAttribs = mapping.getOutElements();
            }

            // Position im in bzw out Vektor an denen die Eingaben für dieses
            // Mapping liegen
            int[] inPos = new int[inAttribs.size()];
            int[] outPos = new int[outAttribs.size()];
            for (int j = 0; j < inAttribs.size(); j++) {
                inPos[j] = fromPattern
                        .getAttributePosition((SDFAttribute) inAttribs
                                .get(j));
                //System.out.println((SDFAttribute)inAttribs.getElement(j)+"-->"+inPos[j]);
            }
            for (int j = 0; j < outAttribs.size(); j++) {
                outPos[j] = toPattern
                        .getAttributePosition((SDFAttribute) outAttribs
                                .get(j));
                //System.out.println((SDFAttribute)inAttribs.getElement(j)+"-->"+outPos[j]);
            }
            AttributeMapping attribMapping = new AttributeMapping(inPos, conv,
                    outPos);
            attributeMappings.add(attribMapping);
        }
        return attributeMappings;
    }


    /**
     * Ermittlung des SDFSchemaMappings in denen das Attribut entweder als
     * Ausgangs- oder als Zielement auftritt (es kann nur eine Abbildung geben!)
     * 
     * @param attribute
     *            der gesuchte SDFAttribute
     * @param isTarget
     *            wenn isTarget true ist, werden alle Abbildungen gesucht, in
     *            denen das Attribut als Ziel einer Abbildung auftritt,
     *            ansonsten diejenigen Abbildungen, in denen das attribute als
     *            Ausgangselement auftritt
     * @param sourceDescr
     * @return
     */
    static private SDFSchemaMapping findMappingWithAttribute( 
            SDFAttribute attribute, SDFIntensionalSourceDescription intDescr,
            boolean isTarget) {
        int count = intDescr.getSchemaMappingCount();
        for (int i = 0; i < count; i++) {
            SDFSchemaMapping mapping = intDescr.getSchemaMapping(i);
            if ((isTarget && mapping.isOutputElement(attribute))
                    || (!isTarget && mapping.isInputElement(attribute))) {
                return mapping;
            }
        }
        return null;
    }

    static private SDFSchemaMapping findMappingWithInAttribute(
            SDFAttribute attribute, SDFIntensionalSourceDescription intDescr) {
        return findMappingWithAttribute(attribute, intDescr, false);
    }

//    static private SDFSchemaMapping findMappingWithOutAttribute(
//            SDFAttribute attribute, SDFIntensionalSourceDescription intDescr) {
//        return findMappingWithAttribute(attribute, intDescr, true);
//    }

    static private SDFInputPattern determineGlobalInputPattern(
            SDFInputPattern localInputPattern,
            SDFIntensionalSourceDescription intDescr, ArrayList<SDFSchemaMapping> schemaMappings)
            throws SDFException {
        SDFInputPattern globalPattern = new SDFInputPattern();  /// Leeren globalen Zugriffmuster anlegen
        int localPatternCount = localInputPattern
                .getAttributeAttributeBindingPairCount();
        for (int i = 0; i < localPatternCount; i++) { /// Für alle Attribute des lokalen  Zugriffsmusters
            SDFAttributeAttributeBindingPair attribAttributeBindingtPair = localInputPattern  /// akuelle Bindungspaar(Attribut, Bindung)
                    .getAttributeAttributeBindingPair(i);
            SDFAttribute localAttribute = attribAttributeBindingtPair.getAttribute(); // aktuelle Attribut des lokalen Zugriffsmusters
            // in welchen Mappings tritt dieses Attribut als AusgangsElement
            // auf?
            SDFSchemaMapping schemaMapping = findMappingWithInAttribute(
                    localAttribute, intDescr);
            //System.out.print("HUHU:"+schemaMapping.toString());
            // Nur wenn das Mapping noch nicht behandelt worden ist, ist es von
            // Interesse. Ansonsten wurden
            // die AttributeBinding bereits in das globale Pattern aufgenommen
            if (!schemaMappings.contains(schemaMapping)) { 
                schemaMappings.add(schemaMapping); /// Mapping in Liste der IN- bzw OUT-Mappings aufnehmen
   ///############################ 1 zu 1 Mapping-Handling######################################### 
                if (schemaMapping instanceof SDFOneToOneMapping) {
                    // Ein Attribut des lokalen Schemas entspricht einem
                    // Attribut des globalen Schemas
                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
                            .getOutElements().get(0);
                    // Die Attributbindung wird dann einfach kopiert
                    SDFInputAttributeBinding localAttributeBinding = (SDFInputAttributeBinding) attribAttributeBindingtPair
                            .getAttributeBinding();
                    SDFInputAttributeBinding globalAttributeBinding = new SDFInputAttributeBinding("tmp"); /// erzeugen mit Fake-URI, da...
                    // Notwendigkeit kopieren
                    globalAttributeBinding.setNecessity(localAttributeBinding.getNecessity()); ///....sowieso kopiert wird von local
                    // Operatoren kopieren
                    globalAttributeBinding.setCompareOps(localAttributeBinding.getCompareOps());
                    // Selektionstyp
                    globalAttributeBinding.setSetSelectionType(localAttributeBinding
                            .getSetSelectionType());
                    // Konstanten
                    if (localAttributeBinding.getConstantSet() != null) {	
                        // Übertragung der Konstantenmenge auf das globale Schema
                        // Wende dazu die Umkehr-Transformationsfunktion auf die Konstanten an
                        SDFFunction mapFktn = schemaMapping.getMappingFunction();
                        SDFConstantList localConstantSet = localAttributeBinding.getConstantSet();
                        
                        if (mapFktn != null){
                            ConversionFunction covFktn = ConversionFunctionFactory.getFunction(mapFktn.getReverseFunction());
                            SDFConstantList globalConstantSet = new SDFConstantList();
                            for (int j=0;j<localConstantSet.size();j++){
                                SDFConstant cons = localConstantSet.getConstant(j);
                                Object value = null;
                                if (SDFDatatypes.String.equals(cons.getDatatype())){
                                    value = cons.getString();
                                }
                                if (SDFDatatypes.Number.equals(cons.getDatatype())){
                                    value = new Double(cons.getDouble());
                                }
                                List<Object> input = new ArrayList<Object>();
                                input.add(value);
                                List result = covFktn.process(input);
                                if (result.size() == 1){
                                    SDFConstant gConst = null;
                                    Iterator iter = result.iterator();
                                    Object retVal = iter.next();
                                    if (retVal instanceof String){
                                        gConst = new SDFStringConstant("", retVal.toString());
                                    }
                                    if (retVal instanceof Double){
                                        gConst = new SDFNumberConstant("", retVal.toString());
                                    }
                                    if (gConst != null){
                                        globalConstantSet.add(gConst);
                                    }
                                } // for
                                globalAttributeBinding.setConstantSet(globalConstantSet);
                            }
                        }else{
                            // Keine Funktion. Einfach die Konstanten kopieren
                            System.out.println("Konstanten kopiert "+localConstantSet.toString());
                            globalAttributeBinding.setConstantSet(localConstantSet);
                        }
                        
                        
                    }
                    // Abhängige Attribute
                    if (localAttributeBinding.getRequiredAttributes() != null) {
//                      Umsetzung der lokalen Notwendigen Attribute auf globale
                        throw new NotImplementedException();
                    }
                    // Jetzt die neue Attributbindung dem globalen Muster
                    // hinzufügen
                    SDFAttributeAttributeBindingPair globAttrAttributeBindingPair = new SDFAttributeAttributeBindingPair(
                            "", globalAttribute, globalAttributeBinding);
                    globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBindingPair);
                    continue;
                }
  ///########################## 1 zu n Mapping-Handling ############################################
                /// (n zu 1) - Case, jedoch richtungsabhängig(Lokal->Global) interpretiert daher umgekehrt
                if (schemaMapping instanceof SDFOneToManyMapping) { 
                	// Ein Attribut des lokalen Schemas wird auf eine Menge von g
                    // Attributten des globalen Schemas abgebildet
    
                    for (int j=0;j<schemaMapping.getOutElements().size();j++)
                    {/// globale Attribute iterieren
	                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
	                            .getOutElements().get(j);
	                    // Die Attributbindung wird dann einfach kopiert   
	                    SDFInputAttributeBinding localAttributeBinding = (SDFInputAttributeBinding) attribAttributeBindingtPair
	                            .getAttributeBinding();
	                    SDFInputAttributeBinding globalAttributeBinding = new SDFInputAttributeBinding("tmp");
	                    // Notwendigkeit kopieren
	                    globalAttributeBinding.setNecessity(localAttributeBinding.getNecessity());
	                    // Operatoren kopieren
	                    globalAttributeBinding.setCompareOps(localAttributeBinding.getCompareOps());
	                    // Selektionstyp
	                    globalAttributeBinding.setSetSelectionType(localAttributeBinding
	                            .getSetSelectionType());
	                    // Konstanten
	                    if (localAttributeBinding.getConstantSet() != null) {
	                        // Hier muss jetzt noch die Funktion angewandt werden
	                        throw new NotImplementedException();
	                    }
	                    // Abhängige Attribute
	                    if (localAttributeBinding.getRequiredAttributes() != null) {
	                        // Umsetzung der lokalen Notwendigen Attribute auf globale
	                        throw new NotImplementedException();
	                    }
	                    // Jetzt die neue Attributbindung dem globalen Muster
	                    // hinzufügen
	                    SDFAttributeAttributeBindingPair globAttrAttributeBindingPair = new SDFAttributeAttributeBindingPair(
	                            "", globalAttribute, globalAttributeBinding);
	                    globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBindingPair);
                    }
                    continue;
                }
                ///########################## n zu 1 Mapping-Handling ############################################
                if (schemaMapping instanceof SDFManyToOneMapping) {
                    // Ein Attribut des globalem Schemas entspricht einer Menge von
                    // Attributen des lokalen Schemas
                    // Das Übertragen der Attributbindungen erfolgt durch die Vereinigung
                    // aller lokalen Attributbindungen
                    SDFSchemaElementSet inElems = schemaMapping.getInElements();
                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
                    .getOutElements().get(0);
                    ArrayList<SDFAttributeBindung> tmpAttributeBinding = new ArrayList<SDFAttributeBindung>();
                    // Jetzt im Eingabemuster schauen, welche AttributeBinding den Attributen
                    // aus inElems zugeordnet sind und sich die AttributeBinding merken
                    for (int j=0;j<inElems.size();j++){
                        tmpAttributeBinding.add(localInputPattern.getAttributeAttributeBindingPair((SDFAttribute)inElems.get(i)).getAttributeBinding());
                    }
                    
                    // Zuerst in der Menge aller lokalen Attributbindungen schauen,
                    // ob ein notwendiges Element vorhanden ist, wenn ja, muss das globale Element
                    // auch auch notwendig sein.
                    SDFInputAttributeBinding globalAttributeBinding = new SDFInputAttributeBinding("tmp");
                    globalAttributeBinding.setNecessity(SDFInputAttributeBindingFactory.getNecessityState(SDFIntensionalDescriptions.Optional));
                    for (int j=0;j<tmpAttributeBinding.size();j++){
                        if (((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getNecessity().equals(SDFInputAttributeBindingFactory.getNecessityState(SDFIntensionalDescriptions.Required))){
                            globalAttributeBinding.setNecessity(SDFInputAttributeBindingFactory.getNecessityState(SDFIntensionalDescriptions.Required));
                            break; // Dann nicht weiter betrachten, da bereits 'Required' und keine weitere Verschärfung
                        } 
                    }
                    	
                    // Operatoren
                    // Nur wenn alle Operatoren gleich (oder nicht belegt sind) kann hier
                    // eine Abbildung vorgenommen werden
                    
                    SDFCompareOperatorList globalCompareOp = ((SDFInputAttributeBinding)tmpAttributeBinding.get(0)).getCompareOps(); 
                    globalAttributeBinding.setCompareOps(globalCompareOp);
                    for (int j=1;j<tmpAttributeBinding.size();j++){
                        SDFCompareOperatorList compareOp = ((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getCompareOps();
                        if (globalCompareOp == null && compareOp == null) continue;
                        
                        if ((globalCompareOp == null && compareOp != null) ||
                            (globalCompareOp != null && compareOp == null) ||    
                            !compareOp.equals(globalCompareOp)){
                            throw new SDFException("AttributeBinding nicht vereinigbar <Unterschiedliche Operatoren>");
                        }
                    }
                    // Selektionstyp
                    SDFConstantSetSelectionType selType = ((SDFInputAttributeBinding)tmpAttributeBinding.get(0)).getSetSelectionType(); 
                    globalAttributeBinding.setSetSelectionType(selType);
             
                    for (int j=1;j<tmpAttributeBinding.size();j++){
                        SDFConstantSetSelectionType lSelType = ((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getSetSelectionType();
                        if (selType == null && lSelType == null) continue;
                        
                        if ((selType != null && lSelType == null ) || 
                            (selType == null && lSelType != null ) ||
                            !(selType.equals(lSelType))){
                            throw new SDFException("AttributeBinding nicht vereinigbar <Unterschiedlicher Selektionstyp>");
                        }
                    }
                    
                    // Konstanten
                    // vereinigen
                    for (int j=1;j<tmpAttributeBinding.size();j++){
                        if (((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getConstantSet() != null){
                            System.out.println(((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getConstantSet() != null);
                            throw new NotImplementedException();
                        }
                    }
                    
                    // Abhängige Attribute
                    // vereinigen
                    for (int j=1;j<tmpAttributeBinding.size();j++){
                        if (((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getRequiredAttributes() != null){
                            System.out.println(((SDFInputAttributeBinding)tmpAttributeBinding.get(j)).getRequiredAttributes());
                            throw new NotImplementedException();
                        }
                    }
                    SDFAttributeAttributeBindingPair globAttrAttributeBinding = new SDFAttributeAttributeBindingPair(
                            "", globalAttribute, globalAttributeBinding);
                    globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBinding);                
                    continue;
                }

            }

        }
        return globalPattern;
    }

    static private SDFOutputPattern determineGlobalOutputPattern(SDFOutputPattern localOutputPattern,
            SDFIntensionalSourceDescription intDescr, ArrayList<SDFSchemaMapping> schemaMappings)
            throws NotImplementedException, SDFException {
        SDFOutputPattern globalPattern = new SDFOutputPattern();
        int localPatternCount = localOutputPattern
                .getAttributeAttributeBindingPairCount();
        for (int i = 0; i < localPatternCount; i++) {
            SDFAttributeAttributeBindingPair attribAttributeBindingPair = localOutputPattern
                    .getAttributeAttributeBindingPair(i);
            SDFAttribute localAttribute = attribAttributeBindingPair.getAttribute();
            // in welchen Mappings tritt dieses Attribut als AusgangsElement
            // auf?
            SDFSchemaMapping schemaMapping = findMappingWithInAttribute(
                    localAttribute, intDescr);
            // Nur wenn das Mapping noch nicht behandelt worden ist, ist es von
            // Interesse. Ansonsten wurden
            // die AttributeBinding bereits in das globale Pattern aufgenommen
            if (!schemaMappings.contains(schemaMapping)) {
                schemaMappings.add(schemaMapping);
            ///########################## 1 zu 1 Mapping-Handling ############################################
                if (schemaMapping instanceof SDFOneToOneMapping) {
                    // Ein Attribut des lokalen Schemas entspricht einem
                    // Attribut des globalen Schemas
                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
                            .getOutElements().get(0);
                    // Die Attributbindung wird dann einfach kopiert
                    SDFOutputAttributeBinding localAttributeBinding = (SDFOutputAttributeBinding) attribAttributeBindingPair
                            .getAttributeBinding();
                    SDFOutputAttributeBinding globalAttributeBinding = localAttributeBinding;
                    // Jetzt die neue Attributbindung dem globalen Muster
                    // hinzufügen
                    SDFAttributeAttributeBindingPair globAttrAttributeBindingPair = new SDFAttributeAttributeBindingPair(
                            "", globalAttribute, globalAttributeBinding);
                    globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBindingPair);
                    continue;
                }
                if (schemaMapping instanceof SDFOneToManyMapping) {
                    // das Attribut des lokalen Schemas wird auf n Attribute des globalen
                    // Schema angebildet und jedes AttributeBinding einfach kopiert
			        SDFOutputAttributeBinding localAttributeBinding = (SDFOutputAttributeBinding) attribAttributeBindingPair
			                .getAttributeBinding();
			        for (int j=0;j<schemaMapping.getOutElements().size();j++){
	                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
	                    .getOutElements().get(j);
				        SDFOutputAttributeBinding globalAttributeBinding = localAttributeBinding;
				        // Jetzt die neue Attributbindung dem globalen Muster
				        // hinzufügen
				        SDFAttributeAttributeBindingPair globAttrAttributeBindingPair = new SDFAttributeAttributeBindingPair(
				                "", globalAttribute, globalAttributeBinding);
				        globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBindingPair);	            
			        }
			        continue;
                }
                if (schemaMapping instanceof SDFManyToOneMapping) {
                    // Die Elemente des lokalen Schemas werden auf ein Element im globalen Schema
                    // abgebildet. Das geht aber nur, wenn alle Attributbindungen identisch sind
                   
                    SDFSchemaElementSet inElems = schemaMapping.getInElements();
                    SDFAttribute globalAttribute = (SDFAttribute) schemaMapping
                    .getOutElements().get(0);
                    ArrayList<SDFAttributeBindung> tmpAttributeBinding = new ArrayList<SDFAttributeBindung>();
                    // Jetzt im Eingabemuster schauen, welche AttributeBinding den Attributen
                    // aus inElems zugeordnet sind und sich die AttributeBinding merken
                    /// OK, hier evtl anders aufbauen, weil, mal sehen
                    for (int j=0;j<inElems.size();j++){
                        SDFAttributeBindung outAttributeBinding = localOutputPattern.getAttributeAttributeBindingPair((SDFAttribute)inElems.get(i)).getAttributeBinding();
                        if (!tmpAttributeBinding.contains(outAttributeBinding)) tmpAttributeBinding.add(outAttributeBinding); //nur unterscheidliche Addornmenttypen hinzufügen
                    }
                    if (tmpAttributeBinding.size()==1){ 
                        SDFAttributeAttributeBindingPair globAttrAttributeBindingPair = new SDFAttributeAttributeBindingPair(
                                "",globalAttribute,((SDFAttributeBindung)tmpAttributeBinding.get(0)));
                        globalPattern.addAttributeAttributeBindingPair(globAttrAttributeBindingPair);	
                    }else{/// wenn mehr als nur ein typ, dann hatten lokale Attribute unterschiedliche Bindungen und sind nichtvereinbar
                        throw new SDFException("AttributeBinding nicht vereinigbar");
                    }
                    
                    
                    continue;
                }

            }

        }
        return globalPattern;
    }


    public String toString() {
        StringBuffer ret = new StringBuffer();
        
        ret.append("AnnotatedPlan: Quelle(n) \n");
        for(SDFSourceDescription s:sourceDescriptionList){
            ret.append(s.getURI(true)+"\n");
        }
        if (toStringVerbose){
            if (globalInputPattern != null)
                ret.append(" GlobalInputPattern "
                        + this.globalInputPattern.toString() + "\n");
            if (globalOutputPattern != null)
                ret.append(" GlobalOuputPattern "
                        + this.globalOutputPattern.toString() + "\n");
        
            ret.append("Ausführungsplan \n");
            // Hier nun auch noch den Plan hinzufügen
           ProcessPlanTransform.dumpPlan(this.getAccessPlan()," ",ret);
        
        }
        
        return ret.toString();
    }


    
    /**
     * Diese Methode liefert true, wenn der Plan die übergebenen Attribute
     * alle in seinem Ausgabemuster enthält und false wenn nicht
     * @param attrSet Die zu überprüfende Menge der Attribute
     * @return
     */
    public boolean containsOutAttributes(SDFAttributeList attrSet) {
        for (int i=0;i<attrSet.size();i++){
            SDFAttribute attribute = attrSet.getAttribute(i);
            if (!globalOutputPattern.containsAttribute(attribute)){
                return false;
            }
        }
        // Alle enthalten
        return true;
    }
    
    /**
     * Diese Methode liefert true, wenn der Plan gleiches Ausgabemuster und dieselbe URI mit einem Plan aus 
     * der Menge planSet hat.
     * @param planSet Die zu überprüfende Menge der Pläne
     * @return
     */
    public boolean inSetWithSameGOP(ArrayList<AnnotatedPlan> planSet) 
    {
    	
    	// TODO: FUNKTION ÜBERPRÜFEN
        for (int i=0;i<planSet.size();i++)
        {
        	AnnotatedPlan nextPlan = planSet.get(i);
        	String uri = nextPlan.getSourceDescriptionList().get(0).getURI(false); // URI holen
        	String uribase = uri.substring(0, uri.lastIndexOf("#") + 1); //Namespace(Base) zum Abgleich
        	
        	String thisUri = this.getSourceDescriptionList().get(0).getURI(false); // URI vom this holen
        	String thisUribase = thisUri.substring(0, thisUri.lastIndexOf("#") + 1); //Namespace(Base) vom this zum Abgleich
        //	System.out.println("#### THIS>"+thisUribase+"\n"+"#### THAT>"+uribase+"("+thisUribase.equals(uribase)+")");
    		// GlobalOutputPattern(GOP) abgleichen
        	if(thisUribase.equals(uribase)
        			&& nextPlan.hasSameGOP(this.getGlobalOutputPattern()))
                return true;
        }
        // keine Kollision in for() entdeckt
        return false;
    }

    /**
     * Liefert true, wenn das GOP des THIS mit dem GOP des anderen Plans übereinstimmt oder 
     * im GOP des anderen Plans liegt
     * @param attrSet
     * @return
     */
    public boolean hasSameGOP(SDFPattern gop) 
    {	
    	SDFAttributeList attrSet = gop.getAllAttributes();
    	if((this.globalOutputPattern.getAllAttributes().getAttributeCount()!=attrSet.size())
    			 && !SDFAttributeList.subset(this.globalOutputPattern.getAllAttributes(), attrSet))
        	return false; // Grösse weicht ab, also schon mal anderes GOP
//    	for (int i=0;i<attrSet.getElementCount();i++)
//        {
//            SDFAttribute attribute = attrSet.getAttribute(i);
//            if (!globalOutputPattern.containsAttribute(attribute))
//            {
//                return false;
//            }
//        }
    	else 
         return true;
    }
    
    /**
     * @return
     */
    public SDFAttributeList getRequiredInAttributes() {
        return globalInputPattern.getRequiredInAttributes();
    }

    public SDFAttributeAttributeBindingPairList getRequiredInAttrAttributeBindingPairs(){
        return globalInputPattern.getRequiredInAttrAttributeBindingPairs();
    }
    
    /**
     * @param attrSet
     * @return
     */
    public boolean containsInAttributes(SDFAttributeList attrSet) {
        for (int i=0;i<attrSet.size();i++){
            SDFAttribute attribute = attrSet.getAttribute(i);
            if (!globalInputPattern.containsAttribute(attribute)){
                return false;
            }
        }
        // Alle enthalten
        return true;
    }

    /**
     * Diese Methode überprüft, ob die Quelle das übergebene Prädikat
     * ausführen kann, d.h. ist für das entsprechende Attribut der
     * zugehörige Operator definiert.
     * @param pred
     * @return
     */
    public boolean canProcess(SDFSimplePredicate pred) {
        for (int i=0;i<this.globalInputPattern.getAttributeAttributeBindingPairCount();i++){
            SDFAttributeAttributeBindingPair aap = globalInputPattern.getAttributeAttributeBindingPair(i);
            // Wenn Operator und Attribut übereinstimmen, dann true
            if (aap.getAttribute().equals(pred.getAttribute())){ 
            	SDFCompareOperatorList compareOps = ((SDFInputAttributeBinding)aap.getAttributeBinding()).getCompareOps();
               return compareOps.contains(pred.getCompareOp());
            }
         }
        return false;
               
    }
    
    public String getPlanSource(){
        return this.sourceDescriptionList.toString();
    }
    
    public AlgebraPO getAccessPlan(){
        return this.top;
    }
    
    public void setAccessPlan(AlgebraPO accessPlan){
        this.top = accessPlan;
    }


    /**
     * @param annoPlan
     */
    public void addBasePlan(AnnotatedPlan annoPlan) {
        this.baseAnnotatedPlans.add(annoPlan);
        this.sourceDescriptionList.addAll(annoPlan.getSourceDescriptionList());
        
    }

	/**
	 * @return  the sourceDescriptionList
	 * @uml.property  name="sourceDescriptionList"
	 */
	public List<SDFSourceDescription> getSourceDescriptionList() {
		return sourceDescriptionList;
	}
	

	public void replaceGlobalAttributeByConstant(SDFAttribute toReplace, SDFConstant value){
		SDFConstantList set = new SDFConstantList();
		set.add(value);
		replaceGlobalAttributeByConstantList(toReplace, set);
	}

	public void replaceGlobalAttributeByConstantList(SDFAttribute toReplace, SDFConstantList value){
		replaceGlobalAttribute(getAccessPlan(), toReplace, value);
		removeAttributeFromGlobalInputPattern(toReplace);
	}

	
	private void replaceGlobalAttribute(AlgebraPO accessPlan, SDFAttribute toReplace, SDFConstantList value) {
		if (accessPlan == null) return;
		
		if (accessPlan instanceof SchemaTransformationAccessPO){
			((SchemaTransformationAccessPO)accessPlan).replaceGlobalAttributeByConstantList(toReplace, value);
		}else{
			for (int i=0;i<accessPlan.getNumberOfInputs();i++){
				replaceGlobalAttribute(accessPlan.getInputPO(i), toReplace, value);
			}
		}				
	}
	
	
	public void removeAttributeFromGlobalInputPattern(SDFAttributeList compensatedAttributes) {
		// Bereinigen des Eingabemsuters um kompensierte Attribute
		for(int i = 0; i< globalInputPattern.getAttributeAttributeBindingPairCount(); i++) // alle Attribute des InputMusters durchlaufen
		{
			SDFAttribute nextAttribute = globalInputPattern.getAttributeAttributeBindingPair(i).getAttribute();
			// ist Attribute in der Liste zu kompensierenden Attribute?
			if(compensatedAttributes.contains(nextAttribute)) 
			{
				globalInputPattern.removeAttributeAttributeBindingtPair(i); // dann aus dem InputPattern entfernen =>
		    }
		}
	}

	public void removeAttributeFromGlobalInputPattern(SDFAttribute compensatedAttribute) {
		// Bereinigen des Eingabemsuters um kompensierte Attribute
		for(int i = 0; i< globalInputPattern.getAttributeAttributeBindingPairCount(); i++){ // alle Attribute des InputMusters durchlaufen
			SDFAttribute nextAttribute = globalInputPattern.getAttributeAttributeBindingPair(i).getAttribute();
			// ist Attribute zu kompensierendes Attribute?
			if(nextAttribute.equals(compensatedAttribute)){
				globalInputPattern.removeAttributeAttributeBindingtPair(i); // dann aus dem InputPattern entfernen =>
				break;
		    }
		}
	}
	
	public void setInputValuesOnAccessPOs(SDFQuery newQuery){
		setInputValuesOnAccessPOs(top, newQuery);
	}
    
	//Rekursive Methode zum setzen der Eingabewerte in allen AccessPOs
	private void setInputValuesOnAccessPOs(AlgebraPO planOperator, SDFQuery newQuery) {
		if(planOperator instanceof SchemaTransformationAccessPO){			
			((SchemaTransformationAccessPO)planOperator).setGlobalInputValues(newQuery.getInputVectorAssignment(((SchemaTransformationAccessPO)planOperator).getInputAttributes()));
		}
		for(int i=0; i<planOperator.getNumberOfInputs(); i++){
			setInputValuesOnAccessPOs(planOperator.getInputPO(i), newQuery);
		}
	}
	
}