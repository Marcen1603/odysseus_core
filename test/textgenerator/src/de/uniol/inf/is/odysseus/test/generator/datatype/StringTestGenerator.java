package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class StringTestGenerator implements IDatatypeTestGenerator{
	private static final String CHARTEST = "chartest";
	private static IDatatypeTestGenerator instance;
	
	private String[] testcases;
	private int rotation;
	private int characterIndex;
	
	public StringTestGenerator() {
		this.testcases = new String[]{"", null, StringTestGenerator.CHARTEST};
	}
	
	@Override
	public Object getValue(){
		String value = this.testcases[this.rotation];
		if(value!=null){
			if(value.equals(StringTestGenerator.CHARTEST)){
				if(this.characterIndex<=Character.MAX_VALUE){
					value = ""+(char)this.characterIndex;
					this.characterIndex++;
				}else{
					this.rotation++;
				}
			}else{
				this.rotation++;
			}
		}else{
			this.rotation++;
		}
		if(this.testcases.length==this.rotation){
			this.rotation = 0;//Start new Rotation
		}
		return value;
	}
	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new StringTestGenerator();
		}
		return instance;
	}
}
