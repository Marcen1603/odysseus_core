/*
 * Created on 16.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mg.dynaquest.sourceselection.mapping;

//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//import mg.dynaquest.sourcedescription.sdf.description.SDFAccessPattern;
//import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;
//import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBinding;
//import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBindingFactory;
//import mg.dynaquest.sourcedescription.sdf.description.SDFIntensionalSourceDescription;
//import mg.dynaquest.sourcedescription.sdf.description.SDFNecessityState;
//import mg.dynaquest.sourcedescription.sdf.description.SDFOutputAttributeBinding;
//import mg.dynaquest.sourcedescription.sdf.description.SDFOutputAttributeBindingFactory;
//import mg.dynaquest.sourcedescription.sdf.description.SDFPattern;
//import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
//import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;
//import mg.dynaquest.sourcedescription.sdf.function.SDFFunctionFactory;
//import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMapping;
//import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMappingFactory;
//import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
//import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;
//import mg.dynaquest.sourcedescription.sdf.schema.SDFEntity;
//import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFFunctions;
//import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;
//import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFMappings;
//import mg.dynaquest.sourceselection.AnnotatedPlan;
//import mg.dynaquest.sourceselection.SDFException;
import junit.framework.TestCase;

/**
 * @author Marco Grawunder
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestAnnotatedPlan extends TestCase {
//      
//    private SDFDataschema createLocalSchema(){
//        SDFDataschema localSchema = new SDFDataschema("localSchema1");
//        SDFEntity entityA = new SDFEntity("local:EntityA");
//        localSchema.addEntity(entityA);
//        SDFAttribute localA = new SDFAttribute("local:A");
//        SDFAttribute localB = new SDFAttribute("local:B");
//        SDFAttribute localC = new SDFAttribute("local:C");
//        SDFAttribute localD = new SDFAttribute("local:D");
//        SDFAttribute localE = new SDFAttribute("local:E");
//        SDFAttribute localF = new SDFAttribute("local:F");
//        
//        entityA.addAttribute(localA);
//        entityA.addAttribute(localB);
//        entityA.addAttribute(localC);
//        entityA.addAttribute(localD);
//        entityA.addAttribute(localE);
//        entityA.addAttribute(localF);
//        
//        return localSchema;
//    }
//    
//    private SDFDataschema createGlobalSchema(){
//        SDFDataschema globalSchema = new SDFDataschema("globalSchema1");
//        SDFEntity entityA = new SDFEntity("global:EntityA");
//        globalSchema.addEntity(entityA);
//        SDFAttribute globalA = new SDFAttribute("global:A");
//        SDFAttribute globalB = new SDFAttribute("global:B");
//        SDFAttribute globalC = new SDFAttribute("global:C");
//        SDFAttribute globalD = new SDFAttribute("global:D");
//        SDFAttribute globalE = new SDFAttribute("global:E");
//        SDFAttribute globalF = new SDFAttribute("global:F");
//        
//        entityA.addAttribute(globalA);
//        entityA.addAttribute(globalB);
//        entityA.addAttribute(globalC);
//        entityA.addAttribute(globalD);
//        entityA.addAttribute(globalE);
//        entityA.addAttribute(globalF);
//        
//        return globalSchema;
//    }
//
//    private SDFPattern createSimpleInputPattern(SDFDataschema localSchema){
//        SDFPattern inputPattern = new SDFPattern("InputPattern1");
//        SDFNecessityState requiredState = SDFInputAttributeBindingFactory.getNecessityState(SDFIntensionalDescriptions.Required);
//        
//        int noOfAttributes = localSchema.getEntity(0).getNoOfAttributes();
//        for (int i=0;i<noOfAttributes;i++){
//            SDFAttribute attrib = localSchema.getEntity(0).getAttribute(i);
//            SDFInputAttributeBinding inAdorn = new SDFInputAttributeBinding("local_"+attrib.toString());
//            inAdorn.setNecessity(requiredState);
//            SDFAttributeAttributeBindingPair aaP = new SDFAttributeAttributeBindingPair("inAttrPair_"+attrib.toString(), attrib, inAdorn);
//            inputPattern.addAttributeAdornmentPair(aaP);
//        }
//        return inputPattern;
//    }
//    
//    private SDFPattern createSimpleOutputPattern(SDFDataschema localSchema){
//        SDFPattern outputPattern = new SDFPattern("OutputPattern1");
//               
//        int noOfAttributes = localSchema.getEntity(0).getNoOfAttributes();
//        for (int i=0;i<noOfAttributes;i++){
//            SDFAttribute attrib = localSchema.getEntity(0).getAttribute(i);
//            SDFOutputAttributeBinding outAdorn = SDFOutputAttributeBindingFactory.getOutputAdornment(SDFIntensionalDescriptions.Id);
//            SDFAttributeAttributeBindingPair aaP = new SDFAttributeAttributeBindingPair("outAttrPair_"+attrib.toString(), attrib, outAdorn);
//            outputPattern.addAttributeAdornmentPair(aaP);
//        }
//        return outputPattern;
//    }
//
//    protected SDFSourceDescription createSimpleSourceDescription(SDFDataschema localSchema, SDFDataschema globalSchema){
//
//        SDFSourceDescription simpleSourceDesc = new SDFSourceDescription("SourceDesc:Simple");
//        SDFIntensionalSourceDescription intDesc = new SDFIntensionalSourceDescription("IntSourceDesc:Simple");
//        SDFPattern inputPattern = createSimpleInputPattern(localSchema);
//        SDFPattern outputPattern = createSimpleOutputPattern(localSchema);
//               
//        SDFAccessPattern accessPattern = new SDFAccessPattern("accessPattern1");
//        accessPattern.setInputPatt(inputPattern);
//        accessPattern.setOutputPatt(outputPattern);
//        intDesc.addAccessPattern(accessPattern);
//        
//        // Neben den Zugriffsmustern müssen auch die Mappings definiert werden        
//        // Hier ein sehr einfaches OneToOne Mapping
//        int noOfAttributes = localSchema.getEntity(0).getNoOfAttributes();
//        for (int i=0;i<noOfAttributes;i++){
//            SDFSchemaMapping mapping = SDFSchemaMappingFactory.getSchemaMapping("mapping0"+i, SDFMappings.OneToOne , localSchema, globalSchema);
//            SDFFunction function = SDFFunctionFactory.getFunction(null, SDFFunctions.Identity);
//            function.setReverseFunction(SDFFunctionFactory.getFunction(null, SDFFunctions.Identity));
//            mapping.setMappingFunction(function);
//            mapping.addInSchemaElement(localSchema.getEntity(0).getAttribute(i));
//            mapping.addOutSchemaElement(globalSchema.getEntity(0).getAttribute(i));
//            intDesc.addSchemaMapping(mapping);
//        }
//                        
//        simpleSourceDesc.setIntDesc(intDesc);
//        return simpleSourceDesc;
//
//    }
//    
//    /**
//     * @param localSchema
//     * @param globalSchema
//     * @return
//     */
//    private SDFSourceDescription createOneToManyInputSourceDesc(SDFDataschema localSchema, SDFDataschema globalSchema) {
//        SDFSourceDescription sourceDesc = new SDFSourceDescription("SourceDesc:OneToManyInput");
//        SDFIntensionalSourceDescription intDesc = new SDFIntensionalSourceDescription("IntSourceDesc:Simple2");
//               
//        SDFPattern inputPattern = new SDFPattern("InputPattern2");
//        SDFNecessityState requiredState = SDFInputAttributeBindingFactory.getNecessityState(SDFIntensionalDescriptions.Required);
//        
//        int noOfAttributes = 1;
//        for (int i=0;i<noOfAttributes;i++){
//            SDFAttribute attrib = localSchema.getEntity(0).getAttribute(i);
//            SDFInputAttributeBinding inAdorn = new SDFInputAttributeBinding("local_"+attrib.toString());
//            inAdorn.setNecessity(requiredState);
//            SDFAttributeAttributeBindingPair aaP = new SDFAttributeAttributeBindingPair("inAttrPair_"+attrib.toString(), attrib, inAdorn);
//            inputPattern.addAttributeAdornmentPair(aaP);
//        }
//        
//        SDFPattern outputPattern = new SDFPattern("OutputPattern2");
//        
//        noOfAttributes = 1;
//        for (int i=0;i<noOfAttributes;i++){
//            SDFAttribute attrib = localSchema.getEntity(0).getAttribute(i);
//            SDFOutputAttributeBinding outAdorn = SDFOutputAttributeBindingFactory.getOutputAdornment(SDFIntensionalDescriptions.Id);
//            SDFAttributeAttributeBindingPair aaP = new SDFAttributeAttributeBindingPair("outAttrPair_"+attrib.toString(), attrib, outAdorn);
//            outputPattern.addAttributeAdornmentPair(aaP);
//        }
//        
//        SDFAccessPattern accessPattern = new SDFAccessPattern("accessPattern2");
//        accessPattern.setInputPatt(inputPattern);
//        accessPattern.setOutputPatt(outputPattern);
//        intDesc.addAccessPattern(accessPattern);
//
//        
//        SDFSchemaMapping mapping = SDFSchemaMappingFactory.getSchemaMapping("mapping1", SDFMappings.OneToMany , localSchema, globalSchema);
//        SDFFunction function = SDFFunctionFactory.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#BlankSplitter", SDFFunctions.TSplitt);
//        function.setReverseFunction(SDFFunctionFactory.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#StringConcatWithBlank", SDFFunctions.Merge));
//        mapping.setMappingFunction(function);
//        mapping.addInSchemaElement(localSchema.getEntity(0).getAttribute(0));
//        for (int i=0;i<globalSchema.getEntity(0).getNoOfAttributes();i++){
//            mapping.addOutSchemaElement(globalSchema.getEntity(0).getAttribute(i));
//        }
//        intDesc.addSchemaMapping(mapping);
//        sourceDesc.setIntDesc(intDesc);
//        return sourceDesc;
//    }
//
//    /**
//     * @param localSchema
//     * @param globalSchema
//     * @return
//     */
//    private SDFSourceDescription createManyToOneInputSourceDesc(SDFDataschema localSchema, SDFDataschema globalSchema) {
//        SDFSourceDescription sourceDesc = new SDFSourceDescription("SourceDesc:ManyToOneInput");
//        SDFIntensionalSourceDescription intDesc = new SDFIntensionalSourceDescription("IntSourceDesc:Simple3");
//                     
//        SDFPattern inputPattern = createSimpleInputPattern(localSchema);
//        SDFPattern outputPattern = createSimpleOutputPattern(localSchema);
//        
//        SDFAccessPattern accessPattern = new SDFAccessPattern("accessPattern3");
//        accessPattern.setInputPatt(inputPattern);
//        accessPattern.setOutputPatt(outputPattern);
//        intDesc.addAccessPattern(accessPattern);
//
//        
//        SDFSchemaMapping mapping = SDFSchemaMappingFactory.getSchemaMapping("mapping2", SDFMappings.ManyToOne , localSchema, globalSchema);
//        SDFFunction function = SDFFunctionFactory.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#StringConcatWithBlank", SDFFunctions.Merge);
//        function.setReverseFunction(SDFFunctionFactory.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#BlankSplitter", SDFFunctions.TSplitt));
//        mapping.setMappingFunction(function);
//        for (int i=0;i<localSchema.getEntity(0).getNoOfAttributes();i++){
//            mapping.addInSchemaElement(localSchema.getEntity(0).getAttribute(i));
//        }
//        mapping.addOutSchemaElement(globalSchema.getEntity(0).getAttribute(0));
//        intDesc.addSchemaMapping(mapping);
//        sourceDesc.setIntDesc(intDesc);
//        return sourceDesc;
//    }
//    
//    
//    
//    /*
//     * @see TestCase#setUp()
//     */
//    protected void setUp() throws Exception {
//        
//
//        
//    }
//
//
//    /*
//     * @see TestCase#tearDown()
//     */
//    protected void tearDown() throws Exception {
//        super.tearDown();
//    }
//    
//    public void __testOneToOne(){
//        SDFSourceDescription oneToOneInputSourceDesc = null;
//        SDFDataschema localSchema = this.createLocalSchema();
//        SDFDataschema globalSchema = this.createGlobalSchema();
//        oneToOneInputSourceDesc = createSimpleSourceDescription(localSchema, globalSchema);
//        
//        AnnotatedPlan annotatedPlan1 = null;
//        try {
//            annotatedPlan1 = new AnnotatedPlan(oneToOneInputSourceDesc,0,);
//        } catch (NotImplementedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SDFException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        // Jetzt eigentlich Assertions ...
//
//        System.out.println(annotatedPlan1.toString()); 
//    }
//
//    public void __testOneToMany(){
//        SDFSourceDescription oneToManyInputSourceDesc = null;
//        SDFDataschema localSchema = this.createLocalSchema();
//        SDFDataschema globalSchema = this.createGlobalSchema();
//        oneToManyInputSourceDesc  = this.createOneToManyInputSourceDesc(localSchema, globalSchema);
//        @SuppressWarnings("unused")
//        AnnotatedPlan annotatedPlan2 = null;
//        try {
//            annotatedPlan2 = new AnnotatedPlan(oneToManyInputSourceDesc,0);
//        } catch (NotImplementedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SDFException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        // Jetzt eigentlich Assertions ...
//
//    }
//
//    public void testManyToOne(){
//        SDFSourceDescription manyToOneInputSourceDesc = null;
//        SDFDataschema localSchema = this.createLocalSchema();
//        SDFDataschema globalSchema = this.createGlobalSchema();
//        manyToOneInputSourceDesc = this.createManyToOneInputSourceDesc(localSchema, globalSchema);
//        
//        AnnotatedPlan annotatedPlan2 = null;
//        try {
//            annotatedPlan2 = new AnnotatedPlan(manyToOneInputSourceDesc,0);
//        } catch (NotImplementedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SDFException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        // Jetzt eigentlich Assertions ...
//
//        System.out.println(annotatedPlan2.toString());
//        
//        System.out.println(annotatedPlan2.toString()); 
//
//        // Ist jetzt keine Methode von AnnotatedPlan sondern
//        // vom Zugriffsoperator
//        
//        //        List input = new ArrayList();
////        input.add(new String("A B C D E F"));
////        List output = annotatedPlan2.processInput(input);
////        System.out.println("Output "+output);
////        input = new ArrayList();
////        input = output;
////        output = annotatedPlan2.processOutput(input);
////        System.out.println("Output "+output);        
//
//    }

}