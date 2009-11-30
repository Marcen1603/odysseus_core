package de.uniol.inf.is.odysseus.cep.metamodel;

public class SymbolTableOperationFactory {

	@SuppressWarnings("unchecked")
	public static <T> SymbolTableOperation getOperation(String name) {
		if (name == null || name.length() == 0){
			return null;
		}
		if (Write.class.getSimpleName().equals(name)){
			return new Write<T>();
		}
		if (Count.class.getSimpleName().equals(name)){
			return new Count();
		}
		if (Max.class.getSimpleName().equals(name)){
			return new Max();
		}
		if (Min.class.getSimpleName().equals(name)){
			return new Min();
		}
		if (Sum.class.getSimpleName().equals(name)){
			return new Sum();
		}

		throw new IllegalArgumentException("No such operation "+ name +" defined!");
	}
	
	

}
