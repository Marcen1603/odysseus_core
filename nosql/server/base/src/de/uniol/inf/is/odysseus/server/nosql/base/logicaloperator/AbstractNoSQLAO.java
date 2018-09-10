package de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 *  The AbstractNoSQLAO is the superclass for all logical operators in the NoSQL context.
 *  The parameters host, port, user and password will be set here and are needed in all logical operators in the NoSQL context.
 */
public abstract class AbstractNoSQLAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = -1402629936641429743L;
	
	private String host;
    private int port;
    private String user;
    private String password;
    private String database;


    public AbstractNoSQLAO() {
        super();
    }

    public AbstractNoSQLAO(AbstractNoSQLAO old) {
        super(old);
        this.host = old.host;
        this.database = old.database;
        this.port = old.port;
        this.user = old.user;
        this.password = old.password;
    }

    /**
     * @param host Name of the associated host
     */
    @Parameter(name = "HOST", type = StringParameter.class, optional = false, doc = "Name of the host")
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param host Name of the associated host
     */
    @Parameter(name = "Database", type = StringParameter.class, optional = true, doc = "The name of the target database")
    public void setDatabase(String database) {
        this.database = database;
    }

    
    /**
     * @param port Port of the associated NoSQL database
     */
    @Parameter(name = "PORT", type = IntegerParameter.class, optional = false, doc = "Port of the NoSQL database")
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param user User of the associated NoSQL database
     */
    @Parameter(name = "USER", type = StringParameter.class, optional = true, doc = "User of the NoSQL database")
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param password User of the associated NoSQL database
     */
    @Parameter(name = "PASSWORD", type = StringParameter.class, optional = true, doc = "Users password of the NoSQL database")
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
    
    public String getDatabase() {
		return database;
	}
    
}
