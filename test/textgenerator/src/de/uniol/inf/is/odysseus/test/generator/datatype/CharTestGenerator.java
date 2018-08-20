package de.uniol.inf.is.odysseus.test.generator.datatype;

/**
 * 
 * @author pBruns
 *
 */
public class CharTestGenerator implements IDatatypeTestGenerator{
	private static IDatatypeTestGenerator instance;
	
	private int characterIndex;
	
	public CharTestGenerator() {
		this.characterIndex = 0;
	}
	
	@Override
	public Object getValue(){
		if(characterIndex<=Character.MAX_VALUE){
			char ch = (char)characterIndex;
			characterIndex++;
			return ch;
		}else{
			this.characterIndex = 0;
		}
		return null;
	}

	
	public static IDatatypeTestGenerator getInstance() {
		if(instance==null){
			instance = new CharTestGenerator();
		}
		return instance;
	}
}
