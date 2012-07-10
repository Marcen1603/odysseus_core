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
package de.uniol.inf.is.odysseus.rcp.queryview.physical;

import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;

public class PhysicalQueryViewData implements IQueryViewData {

    private final int id;
    private final String status;
    private final int priority;
    private final String parserId;
    private final String userName;
    private final String queryText;

    public PhysicalQueryViewData(int id, String status, int priority, String parserId, String userName, String queryText) {
        super();
        this.id = id;
        this.status = status;
        this.priority = priority;
        this.parserId = parserId;
        this.userName = userName;
        this.queryText = queryText;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getParserId() {
        return parserId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getQueryText() {
        return queryText;
    }

}
