package de.uniol.inf.is.odysseus.rest.dto.request;

public class CreateSocketRequestDTO extends SessionRequestDTO {

	private int rootPort;
	private int queryId;
	private String queryName;
	private boolean useQueryName;
	private String operatorName;
	private int outputOperatorPort;
	private boolean useOutputOperatorPort;

	public CreateSocketRequestDTO() {
		this.useQueryName = false;
	}

	public CreateSocketRequestDTO(String token, int queryId, int rootPort) {
		super(token);
		this.queryId = queryId;
		this.rootPort = rootPort;
		this.useQueryName = false;
	}

	public CreateSocketRequestDTO(String token, String queryName, int rootPort) {
		super(token);
		this.queryName = queryName;
		this.rootPort = rootPort;
		this.useQueryName = true;
	}

	public int getRootPort() {
		return rootPort;
	}

	public void setRootPort(int rootPort) {
		this.rootPort = rootPort;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public boolean isUseQueryName() {
		return useQueryName;
	}

	public void setUseQueryName(boolean useName) {
		this.useQueryName = useName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public int getOutputOperatorPort() {
		return outputOperatorPort;
	}

	public void setOutputOperatorPort(int outputOperatorPort) {
		this.outputOperatorPort = outputOperatorPort;
	}

	public boolean isUseOutputOperatorPort() {
		return useOutputOperatorPort;
	}

	public void setUseOutputOperatorPort(boolean useOutputOperatorPort) {
		this.useOutputOperatorPort = useOutputOperatorPort;
	}

}
