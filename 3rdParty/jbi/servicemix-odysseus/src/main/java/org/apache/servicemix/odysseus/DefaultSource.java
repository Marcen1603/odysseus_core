/*
 *  Copyright 2009 ckuka.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.apache.servicemix.odysseus;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import java.util.Map;

/**
 *
 * @author ckuka
 */
public class DefaultSource extends AbstractSource<RelationalTuple<ITimeInterval>> {

    private SDFAttributeList schema;

    public DefaultSource(SDFAttributeList schema) {
        this.schema = schema;
    }

    @Override
    protected void process_open() throws OpenFailedException {
    }

    public void pushElement(Map<String, Object> values) {
        RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(this.schema.size());
        int i = 0;
        for (SDFAttribute attr : schema) {
            tuple.setAttribute(i++, values.get(attr.getURI().toString()));
        }
        transfer(tuple);
    }
}
