package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;


public class SymbolTableOperationFactory {

	@SuppressWarnings("unchecked")
	public static AbstractSymbolTableOperation getOperation(String name) {
		if (name == null || name.length() == 0){
			return null;
		}
		if (Write.class.getSimpleName().equalsIgnoreCase(name)){
			return new Write();
		}
		if (Count.class.getSimpleName().equalsIgnoreCase(name)){
			return new Count();
		}
		if (Max.class.getSimpleName().equalsIgnoreCase(name)){
			return new Max();
		}
		if (Min.class.getSimpleName().equalsIgnoreCase(name)){
			return new Min();
		}
		if (Sum.class.getSimpleName().equalsIgnoreCase(name)){
			return new Sum();
		}
		
		throw new IllegalArgumentException("No operation "+ name +" defined!");
	}
	
	

}
