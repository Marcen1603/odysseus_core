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
