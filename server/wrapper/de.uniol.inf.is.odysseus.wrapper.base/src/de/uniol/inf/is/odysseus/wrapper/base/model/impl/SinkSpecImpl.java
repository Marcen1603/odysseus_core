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
package de.uniol.inf.is.odysseus.wrapper.base.model.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.wrapper.base.model.SinkConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class SinkSpecImpl implements SinkSpec {
    private final String name;
    private final List<String> schema = new ArrayList<String>();
    private SinkConfiguration configuration;

    public SinkSpecImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getSchema() {
        return this.schema;
    }

    @Override
    public void setConfiguration(final SinkConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SinkConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public String toString() {
        return "SinkImpl [name=" + this.name + ", schema=" + this.schema + ", configuration="
                + this.configuration + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
        result = prime * result + ((schema == null) ? 0 : schema.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SinkSpecImpl other = (SinkSpecImpl) obj;
        if (configuration == null) {
            if (other.configuration != null)
                return false;
        }
        else if (!configuration.equals(other.configuration))
            return false;
        if (schema == null) {
            if (other.schema != null)
                return false;
        }
        else if (!schema.equals(other.schema))
            return false;
        return true;
    }
    
    
}
