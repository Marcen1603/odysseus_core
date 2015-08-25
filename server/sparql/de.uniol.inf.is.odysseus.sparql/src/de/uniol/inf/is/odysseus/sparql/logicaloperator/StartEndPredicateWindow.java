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
// * StartEndPredicateWindow.java
// *
// * Created on 9. November 2007, 08:26
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowType;
//
///**
// *
// * This class represents a start-/end-predicate-window.
// *
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class StartEndPredicateWindow extends WindowAO{
//    
//    /**
//     * The start predicate of this window.
//     */
//    private StartEndPredicate startPredicate;
//    
//    /**
//     * The end predicate of this window.
//     */
//    private StartEndPredicate endPredicate;
//    
//    /**
//     * the reducing window operator
//     */
//    private WindowAO reducingWindow;
//    
//    /**
//     * indicates by which window operator this
//     * predicate window is reduced.
//     */
//    private WindowType reducedBy;
//    
//    /**
//     * Possibly there is an ask-operator in the query
//     * plan, that has to be notified about new window intervals.
//     */
//    private AbstractLogicalOperator ask;
//    
//    /**
//     * Possibly there is a slice-operator in the query
//     * plan, that has to be notified about new window intervals.
//     */
//    private AbstractLogicalOperator slice;
//    
//    /** Creates a new instance of StartEndPredicateWindow */
//    public StartEndPredicateWindow() {
//        this.setWindowType(WindowType.START_END_PREDICATE_WINDOW);
//    }
//    
//    public StartEndPredicateWindow(StartEndPredicate start,
//            StartEndPredicate end){
//        this.setStartPredicate(start);
//        this.setEndPredicate(end);
//        this.setWindowType(WindowType.START_END_PREDICATE_WINDOW);
//    }
//    
//    private StartEndPredicateWindow(StartEndPredicateWindow original){
//        super(original);
//        this.setStartPredicate(original.getStartPredicate().clone());
//        this.setEndPredicate(original.getEndPredicate().clone());
//        this.setWindowType(WindowType.START_END_PREDICATE_WINDOW);
//    }
//    
//    @Override
//    public StartEndPredicateWindow clone(){
//        return new StartEndPredicateWindow(this);
//    }
//
//    public StartEndPredicate getStartPredicate() {
//        return startPredicate;
//    }
//
//    public void setStartPredicate(StartEndPredicate startPredicate) {
//        this.startPredicate = startPredicate;
//    }
//
//    public StartEndPredicate getEndPredicate() {
//        return endPredicate;
//    }
//
//    public void setEndPredicate(StartEndPredicate endPredicate) {
//        this.endPredicate = endPredicate;
//    }
//
//    public AbstractLogicalOperator getAsk() {
//        return ask;
//    }
//
//    public void setAsk(AbstractLogicalOperator ask) {
//        this.ask = ask;
//    }
//
//    public AbstractLogicalOperator getSlice() {
//        return slice;
//    }
//
//    public void setSlice(AbstractLogicalOperator slice) {
//        this.slice = slice;
//    }
//
//    public WindowAO getReducingWindow() {
//        return reducingWindow;
//    }
//
//    public void setReducingWindow(WindowAO reducingWindow) {
//    	this.reducingWindow = reducingWindow;
//    	this.reducedBy = reducingWindow.getWindowType();
//    }
//
//	public WindowType getReducedBy() {
//		return reducedBy;
//	}
//    
//    
//}
