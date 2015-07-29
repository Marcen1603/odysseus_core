package de.uniol.inf.is.odysseus.iql.odl.typing.typeoperators;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.ITypeOperators;

public class IPunctuationOperators implements ITypeOperators {

	@Override
	public Class<?> getType() {
		return IPunctuation.class;
	}
	
	public static IPunctuation plus(IPunctuation punctuation, long time) {
		return punctuation.clone(punctuation.getTime().plus(new PointInTime(time)));
	}
	
	public static IPunctuation minus(IPunctuation punctuation, long time) {
		return punctuation.clone(punctuation.getTime().minus(new PointInTime(time)));
	}
	
	public static IPunctuation multiply(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()*time));
	}
	
	public static IPunctuation divide(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()/time));
	}
	
	public static IPunctuation modulo(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()%time));
	}
	
	public static IPunctuation plusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()+1));
	}
	
	public static IPunctuation minusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()-1));
	}
	
	public static IPunctuation plusPostfix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()+1));
	}
	
	public static IPunctuation minusPostfix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()-1));
	}

	@Override
	public boolean plusSupported() {
		return true;
	}

	@Override
	public boolean minusSupported() {
		return true;
	}

	@Override
	public boolean multiplySupported() {
		return true;
	}

	@Override
	public boolean divideSupported() {
		return true;
	}

	@Override
	public boolean moduloSupported() {
		return true;
	}

	@Override
	public boolean plusPrefixSupported() {
		return true;
	}

	@Override
	public boolean minusPrefixSupported() {
		return true;
	}

	@Override
	public boolean plusPostfixSupported() {
		return true;
	}

	@Override
	public boolean minusPostfixSupported() {
		return true;
	}

	@Override
	public boolean getSupported() {
		return false;
	}

	@Override
	public boolean hasExtensionAttribute(String name) {
		return false;
	}

	@Override
	public boolean hasExtensionMethod(String name, int args) {
		return false;
	}
		
	
}
