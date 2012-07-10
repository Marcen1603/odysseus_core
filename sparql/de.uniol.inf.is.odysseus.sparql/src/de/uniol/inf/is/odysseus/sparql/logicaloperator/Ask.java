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
// * Ask.java
// *
// * Created on 9. November 2007, 12:28
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import java.util.ArrayList;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.TimeInterval;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.UnaryLogicalOp;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowType;
//
///**
// * This class represents an ask operator. This operator does not need any fields
// * on the logical level. The out elements cannot be determined, because an
// * ask operator only generates boolean values.
// *
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class Ask extends UnaryLogicalOp{
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
//     * If the ask operator is for start end predicate windows, this
//     * array list stores the time intervalls of such windows.
//     */
//    private ArrayList<TimeInterval> intervals;
//    
//    /** Creates a new instance of Ask */
//    public Ask() {
//    	super();
//    }
//    
//    private Ask(Ask original){
//        super(original);
//    }
//    
//    public Ask clone(){
//        return new Ask(this);
//    }
//
//    public WindowType getType() {
//        return type;
//    }
//
//    public void setType(WindowType type) {
//        if(type == WindowType.START_END_PREDICATE_WINDOW){
//            this.intervals = new ArrayList<TimeInterval>();
//        }
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
//
//    public ArrayList<TimeInterval> getIntervals() {
//        return intervals;
//    }
//
//    public void setIntervals(ArrayList<TimeInterval> intervals) {
//        this.intervals = intervals;
//    }
//    
//    public void addTimeinterval(TimeInterval i){
//        if(this.intervals == null){
//            this.intervals = new ArrayList<TimeInterval>();
//        }
//        this.intervals.add(i);
//    }
//    
//    public TimeInterval removeTimeinterval(int index){
//        if(this.intervals == null){
//            return null;
//        }
//        return this.intervals.remove(index);
//    }
//    
//    public void removeTimeinterval(TimeInterval i){
//        if(this.intervals == null){
//            return;
//        }
//        this.intervals.remove(i);
//    }
//}
