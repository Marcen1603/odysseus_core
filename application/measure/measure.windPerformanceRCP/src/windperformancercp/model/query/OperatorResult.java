package windperformancercp.model.query;

public class OperatorResult {
	Stream stream;
	String query;
	
	public OperatorResult(Stream stream, String query){
		this.stream = stream;
		this.query = query;
	}
	
	public String getQuery(){
		return query;
	}
	
	public Stream getStream(){
		return stream;
	}
}
