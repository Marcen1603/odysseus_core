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

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ckuka
 */
public class DefaultSink extends AbstractSink<RelationalTuple<ITimeInterval>> {

    private OdysseusConsumerEndpoint listener;
    private SDFAttributeList schema;

    public DefaultSink(OdysseusConsumerEndpoint listener) {
        this.listener = listener;
    }

    @Override
    protected void process_next(RelationalTuple<ITimeInterval> object, int port, boolean isReadOnly) {
        Map<String, String> result = new HashMap<String, String>();
        int i = 0;
        for (SDFAttribute attr : this.schema) {
            result.put(attr.getURI(), object.getAttribute(i++).toString());
        }
        this.listener.update(result);
    }

    public void setSchema(SDFAttributeList schema) {
        this.schema = schema;
    }
}
