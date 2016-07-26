package de.uniol.inf.is.odysseus.net.recovery.backupinformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public final class BackupInformation implements Serializable {

	private static final long serialVersionUID = 6663741792600349135L;

	private String pqlStatement;

	private String queryState;

	private String sharedQueryId;

	private boolean master;

	private String masterId;

	private List<ConnectionInformation> incomingConnections = new ArrayList<>();

	private List<ConnectionInformation> outgoingConnections = new ArrayList<>();

	public String getPqlStatement() {
		return pqlStatement;
	}

	public void setPqlStatement(String pqlStatement) {
		this.pqlStatement = pqlStatement;
	}

	public String getQueryState() {
		return queryState;
	}

	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}

	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public List<ConnectionInformation> getIncomingConnections() {
		return incomingConnections;
	}

	public void setIncomingConnections(List<ConnectionInformation> incomingConnections) {
		this.incomingConnections = incomingConnections;
	}

	public List<ConnectionInformation> getOutgoingConnections() {
		return outgoingConnections;
	}

	public void setOutgoingConnections(List<ConnectionInformation> outgoingConnections) {
		this.outgoingConnections = outgoingConnections;
	}

	public static JSONObject toJSON(BackupInformation info) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("pqlStatement", info.pqlStatement);
		json.put("queryState", info.queryState);
		json.put("sharedQueryId", info.sharedQueryId);
		json.put("master", info.master);
		json.put("masterId", info.masterId);
		json.put("numIncomingConnections", info.getIncomingConnections().size());
		for (int i = 0; i < info.getIncomingConnections().size(); i++) {
			json.put("incomingConnection" + i, ConnectionInformation.toJSON(info.getIncomingConnections().get(i)));
		}
		json.put("numOutgoingConnections", info.getOutgoingConnections().size());
		for (int i = 0; i < info.getOutgoingConnections().size(); i++) {
			json.put("outgoingConnection" + i, ConnectionInformation.toJSON(info.getOutgoingConnections().get(i)));
		}
		return json;
	}

	public static BackupInformation fromJSON(JSONObject json) throws JSONException {
		BackupInformation info = new BackupInformation();
		info.setPqlStatement(json.getString("pqlStatement"));
		info.setQueryState(json.getString("queryState"));
		info.setSharedQueryId(json.getString("sharedQueryId"));
		info.setMaster(json.getBoolean("master"));
		info.setMasterId(json.getString("masterId"));
		int numIncomingConnections = json.getInt("numIncomingConnections");
		List<ConnectionInformation> incomingConnections = new ArrayList<>();
		for (int i = 0; i < numIncomingConnections; i++) {
			incomingConnections.add(ConnectionInformation.fromJSON(json.getJSONObject("incomingConnection" + i)));
		}
		info.setIncomingConnections(incomingConnections);
		int numOutgoingConnections = json.getInt("numOutgoingConnections");
		List<ConnectionInformation> outgoingConnections = new ArrayList<>();
		for (int i = 0; i < numOutgoingConnections; i++) {
			outgoingConnections.add(ConnectionInformation.fromJSON(json.getJSONObject("outgoingConnection" + i)));
		}
		info.setOutgoingConnections(outgoingConnections);
		return info;
	}

}