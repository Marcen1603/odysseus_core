/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement.persistence.impl;

import java.util.HashMap;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ParameterMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 2519422112300681754L;

    private final String query;

    private boolean namedQuery = true;

    private int resultLimit = -1;

    private ParameterMap(final String query, final boolean namedQuery) {
        this.query = query;
        this.namedQuery = namedQuery;
    }

    public static ParameterMap getNamedQuery(final String query) {
        return new ParameterMap(query, true);
    }

    public static ParameterMap getQuery(final String query) {
        return new ParameterMap(query, false);
    }

    public ParameterMap add(final HashMap<String, Object> parameter) {
        this.putAll(parameter);
        return this;
    }

    public ParameterMap add(final String parameter, final Object value) {
        this.put(parameter, value);
        return this;
    }

    public String getQuery() {
        return this.query;
    }

    public int getResultLimit() {
        return this.resultLimit;
    }

    public boolean isNamedQuery() {
        return this.namedQuery;
    }

    public void setResultLimit(final int resultLimit) {
        this.resultLimit = resultLimit;
    }
}
