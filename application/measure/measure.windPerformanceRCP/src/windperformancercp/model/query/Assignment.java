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
		
		/*public Assignment(String kind, Attribute.AttributeType dt, ISource rs, int id){
			this.kind = kind;
			this.attType = dt;
			this.respSource = rs;
			this.attributeId = id;
		}*/
		
		public Assignment(Kind kind, Attribute.AttributeType dt, Stream rs, int id){
			this.kind = kind;
			this.attType = dt;
			this.respStream = rs;
			this.attributeId = id;
		}
		
		
		/*public Assignment(String kind, Attribute.AttributeType dt){
			this();
			this.setKind(kind);
			this.setType(dt);
		}*/
		
		public Assignment(Kind kind, Attribute.AttributeType dt){
			this();
			this.setKind(kind);
			this.setType(dt);
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
		
		
		public Attribute.AttributeType getDataType(){
			return attType;
		}
		
		public void setType(Attribute.AttributeType dt){
			this.attType = dt;
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

	}
