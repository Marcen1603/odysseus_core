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
// * Construct.java
// *
// * Created on 9. November 2007, 12:22
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.hp.hpl.jena.graph.Triple;
//
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.UnaryLogicalOp;
//
///**
// * This class represents a construct operator. It owns a list of triples
// * from which to create statements. So it is not possible to determine the
// * output elements, because these are rdf statements.
// *
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class Construct extends UnaryLogicalOp{
//    
//    /**
//     * This list of triples from which to create rdf statements.
//     */
//    private List<Triple> triples;
//    
//    /** Creates a new instance of Construct */
//    public Construct() {
//        this.triples = new ArrayList<Triple>();
//    }
//    
//    public Construct(List<Triple> triples){
//        this.setTriples(triples);
//    }
//    
//    public Construct(Construct original){
//        super(original);
//        this.setTriples(original.getTriples());
//    }
//    
//    @Override
//    public Construct clone(){
//        return new Construct(this);
//    }
//    
//    public void addTriple(Triple t){
//        this.triples.add(t);
//    }
//
//    public List<Triple> getTriples() {
//        return triples;
//    }
//    
//    public Triple removeTriple(int i){
//        return this.triples.remove(i);
//    }
//
//    public void setTriples(List<Triple> triples) {
//        this.triples = triples;
//    }
//    
//    
//}
