package mg.dynaquest.support;

public class Tools{
	
	public static StringBuffer toStringBuffer(Object[] array){
		StringBuffer ret = new StringBuffer();
		for (Object o:array){
			ret.append(" ").append(o);
		}
		return ret;
	}
	
	public static StringBuffer toStringBuffer(int[] array){
		StringBuffer ret = new StringBuffer();
		for (int o:array){
			ret.append(" ").append(o);
		}
		return ret;
	}
}
	
	
	