package windperformancercp.model.query;

import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.ISource;

public class Assignment {
	
		public enum Kind{
			TIMESTAMP,
			WINDSPEED,
			POWER,
			PRESSURE,
			TEMPERATURE
		}
		
		//String kind; //TODO
		Kind kind;
		Attribute.AttributeType attType;
		ISource respSource;
		Stream respStream;
		int attributeId;
		
		public Assignment(Attribute.AttributeType dt, ISource rs, int ind){
		
			this.attType = dt;
			this.respStream = null;
			this.respSource = rs;
			this.attributeId = ind;
			
		}
		
		public Assignment(Attribute.AttributeType at, Stream rs, int ind){
			
			this.attType = at;
			this.respStream = rs;
			this.respSource = null;
			this.attributeId = ind;
		}
		
		
		/*public Assignment(String kind, Attribute.AttributeType dt){
			this();
			this.setKind(kind);
			this.setType(dt);
		}*/
		
		public Assignment(Attribute.AttributeType at){
			this();
			this.attType = at;
		}
		
		public Assignment(){
			this.kind = null;
			this.attType = null;
			this.respSource = null;
			this.attributeId = -1;
		}
		
		
		
		public boolean isValid(){
			if(respSource != null){
				if((attributeId>=0)&&(respSource.getNumberOfAtts()>attributeId)){
					if(attType.equals(respSource.getAttributeList().get(attributeId))){
						return true;
					}
				}
			}
			return false;
		}
		
		/*public String getKind(){
			return kind;
		}*/
		
		public Kind getKind(){
			return kind;
		}
		
		/*public void setKind(String w){
			this.kind = w; 
		}*/
		
		public void setKind(Kind w){
			this.kind = w; 
		}
		
		
		public Attribute.AttributeType getAttType(){
			return attType;
		}
		
		public void setType(Attribute.AttributeType at){
			this.attType = at;
		}
		
		public ISource getRespSource(){
			return respSource;
		}
		
		public void setRespSource(ISource src){
			this.respSource = src;
		}

		public Stream getRespStream(){
			return respStream;
		}
		
		public void setRespStream(Stream str){
			this.respStream = str;
		}
		
		public int getAttributeId(){
			return attributeId;
		}
		
		public String toString(){
			String info = "";
			
			if(this.respSource !=null)
				info = info + this.attType.toString()+" "+this.respSource.getName()+" "+this.respSource.getIthAtt(attributeId).getName();
			if(this.respStream !=null)
				info = info + this.attType.toString()+" "+this.respStream.getName()+" "+this.respStream.getIthAttName(this.attributeId);
			return info;
		}

	}
