package org.drools.workflow.instance.impl;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.definition.process.Connection;
import org.drools.process.instance.ProcessInstance;
import org.drools.spi.CompiledInvoker;
import org.drools.spi.ProcessContext;
import org.drools.spi.ReturnValueEvaluator;
import org.drools.spi.Wireable;
import org.drools.workflow.core.Constraint;
import org.drools.workflow.instance.node.SplitInstance;

/**
 * Default implementation of a constraint.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ReturnValueConstraintEvaluator
    implements
    Constraint,
    ConstraintEvaluator,
    Wireable,
    Externalizable {

    private static final long serialVersionUID = 400L;

    private String            name;
    private String            constraint;
    private int               priority;
    private String            dialect;
    private String            type;

    public ReturnValueConstraintEvaluator() {
    }

    private ReturnValueEvaluator evaluator;

    public String getConstraint() {
        return this.constraint;
    }

    public void setConstraint(final String constraint) {
        this.constraint = constraint;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
	public String toString() {
        return this.name;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void wire(Object object) {
        setEvaluator( (ReturnValueEvaluator) object );
    }

    public void setEvaluator(ReturnValueEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public ReturnValueEvaluator getReturnValueEvaluator() {
        return this.evaluator;
    }

    public boolean evaluate(SplitInstance instance,
                            Connection connection,
                            Constraint constraint) {
        Object value;
        try {
            ProcessContext context = new ProcessContext();
            context.setNodeInstance( instance );
            value = this.evaluator.evaluate( ((ProcessInstance) instance.getProcessInstance()).getWorkingMemory(),
                                             context );
        } catch ( Exception e ) {
            throw new RuntimeException( "unable to execute ReturnValueEvaluator",
                                        e );
        }
        if ( !(value instanceof Boolean) ) {
            throw new RuntimeException( "Constraints must return boolean values" );
        }
        return ((Boolean) value).booleanValue();
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        this.evaluator = (ReturnValueEvaluator) in.readObject();
        this.name = in.readUTF();
        this.constraint = (String) in.readObject();
        this.priority = in.readInt();
        this.dialect = in.readUTF();
        this.type = (String) in.readObject();

    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if ( this.evaluator instanceof CompiledInvoker ) {
            out.writeObject( null );
        } else {
            out.writeObject( this.evaluator );
        }
        out.writeUTF( this.name );
        out.writeObject( this.constraint );
        out.writeInt( this.priority );
        out.writeUTF( dialect );
        out.writeObject( type );
    }

    public void setMetaData(String name, Object value) {
    	// Do nothing
    }
    
    public Object getMetaData(String name) {
        return null;
    }
    
}
