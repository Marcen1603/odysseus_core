/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
///*
// * Slice.java
// *
// * Created on 9. November 2007, 12:16
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import com.hp.hpl.jenaUpdated.query.Query;
//
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.UnaryLogicalOp;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowType;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
///**
// * This class represents a slice operator, that will be used for evaluating
// * limit and offset clauses in a sparql query.
// *
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class Slice extends UnaryLogicalOp{
//    
//    public static final long NOLIMIT = Query.NOLIMIT;
//    public static final long NOOFFSET = Query.NOLIMIT;
//    
//    /**
//     * An ask operator can have different semantics depending on
//     * the window operators in the query plan.
//     * The type field defines the semantics for the ask operator.
//     */
//    private WindowType type;
//    
//    /**
//     * If the ask operator is for fixed time windows
//     * this field stores the width of such windows.
//     */
//    private long fixedWidth;
//    
//    /**
//     * The value for the limit clause. Will be -1, if not used.
//     */
//    private long limit;
//    
//    /**
//     * the value for the offset clause. Will -1, if not used.
//     */
//    private long offset;
//    
//    /** Creates a new instance of Slice */
//    public Slice() {
//        this.setLimit(-1);
//        this.setOffset(-1);
//    }
//    
//    public Slice(long limit, long offset){
//        this.setLimit(limit);
//        this.setOffset(offset);
//    }
//    
//    private Slice(Slice original){
//        super(original);
//        this.setLimit(original.getLimit());
//        this.setOffset(original.getOffset());
//    }
//    
//    public void calcOutElements() {
//        AbstractLogicalOperator po = getInputAO();
//        this.setInputSchema(po.getOutputSchema());
//
//        if (po != null){
//            SDFSchema l1 = po.getOutputSchema();		
//            SDFSchema jList = new SDFSchema();
//            jList.addAttributes(l1);
//            this.setOutputSchema(jList);
//        }
//        
//        // also calc the output id size
//        this.calcOutIDSize();
//    }
//    
//    public void calcOutIDSize(){
//    	this.setInputIDSize(this.getInputAO().getOutputIDSize());
//    	this.setOutputIDSize(this.getInputIDSize());
//    }
//    
//    @Override
//    public Slice clone(){
//        return new Slice(this);
//    }
//
//    public long getLimit() {
//        return limit;
//    }
//
//    public void setLimit(long limit) {
//        this.limit = limit;
//    }
//
//    public long getOffset() {
//        return offset;
//    }
//
//    public void setOffset(long offset) {
//        this.offset = offset;
//    }
//
//    public WindowType getType() {
//        return type;
//    }
//
//    public void setType(WindowType type) {
//        this.type = type;
//    }
//
//    public long getFixedWidth() {
//        return fixedWidth;
//    }
//
//    public void setFixedWidth(long fixedWidth) {
//        this.fixedWidth = fixedWidth;
//    }
//}
