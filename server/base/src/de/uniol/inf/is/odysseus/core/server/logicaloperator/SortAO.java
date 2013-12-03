/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
/*
 * Created on 10.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Marco Grawunder
 *
 */
public class SortAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 3251466434501011289L;

	/**
	 * @uml.property  name="sortAttrib"
	 */
    SDFSchema sortAttribs = null; 

    /**
     * tells you if the attribute at the
     * specified index has to be in ascending
     * (true) or descending (false) order
     */
    private boolean[] ascending;


    /**
     * 
     */
    public SortAO(SDFSchema set) {
        super();
        this.sortAttribs = set;
        setName("SortPO");
    }

    /**
     * @param sortPO
     */
    public SortAO(SortAO sortPO) {
        super(sortPO);
        this.sortAttribs = sortPO.sortAttribs;
        setName("SortAO");
    }

    /**
     * 
     */
    public SortAO() {
        super();
        setName("SortAO");
    }


    /**
     * @return Returns the sortAttrib.
     * 
     * @uml.property name="sortAttrib"
     */
    public SDFSchema getSortAttrib() {
        return sortAttribs;
    }

    /**
     * @param sortAttrib The sortAttrib to set.
     * 
     * @uml.property name="sortAttrib"
     */
    public void setSortAttrib(SDFSchema sortAttribs) {
        this.sortAttribs = sortAttribs;
    }

	public @Override
	SortAO clone() {
		return new SortAO(this);
	}
	
	public boolean[] getAscending() {
		return ascending;
	}

	public void setAscending(boolean[] ascending) {
		this.ascending = ascending;
	}
	
}
