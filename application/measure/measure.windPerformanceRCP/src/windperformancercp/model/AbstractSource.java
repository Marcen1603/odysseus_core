package windperformancercp.model;

public abstract class AbstractSource implements ISource {
	
	private int type;
	private int port;
	private int id;
	private String streamIdentifier;
	
	public static final int MMId = 0;
	public static final int WTId = 1;
	
	public AbstractSource(){
	}
	
	public AbstractSource(int typeId, String identifier, int portId){
		this.type = typeId;
		this.streamIdentifier = identifier;
		this.port = portId;
	}
	
	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public boolean isWindTurbine() {
		if(this.type == 1) return true;
		return false;
	}

	@Override
	public boolean isMetMast() {
		if(this.type == 0) return true;
		return false;
	}

	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public String getStreamIdentifier(){
		return this.streamIdentifier;
	}
	
	@Override
	public String toString(){
		String info = "Source_ID"+this.id+"Source_Name: "+this.streamIdentifier+"Source_Type: "+this.type+" Port: "+this.port;
		return info;
	}

}
