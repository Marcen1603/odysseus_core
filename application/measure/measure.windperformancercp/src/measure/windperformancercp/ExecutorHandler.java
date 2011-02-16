package measure.windperformancercp;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

/**
 * Diese Hilfsklasse verwaltet die vom Declarative Service zur Verfuegung
 * gestellten <code>IExecutor</code>. Der Nutzer kann damit die aktuelle
 * Instanz der Klasse bekommen, falls benoetigt.
 * <p>
 * Der Declative Service ruft die Methoden <code>bindExecutor()</code> und
 * <code>unbindExecutor</code> selbststaendig auf. Der Nutzer sollte sie nicht
 * explizit aufrufen.
 * 
 * @author Timo Michelsen
 * 
 */
public class ExecutorHandler {

	private static IExecutor executor;
	
	public ExecutorHandler(){
	}


	/**
	 * Binds Odysseus  <code>IExecutor</code>. , that handles query execution
	 * Called by declarative Service. User shall not call this method.
	 * @param ex
	 */
	public void bindExecutor(IExecutor ex){
		System.out.println(this.toString()+" bind Executor! ****************************************************");				
		executor = ex;		
	}
	
	/**
	 * Unbinds Odysseus Executor, that handles query execution
	 * Called by declarative Service. User shall not call this method.
	 * @param ex
	 */
	public void unbindExecutor( IExecutor ex ) {
		executor = null;		
	}
	
	/**
	 * Returns the actual 
	 * <code>IExecutor</code>. 
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder
	 *         <code>null</code>.
	 */
	public static IExecutor getExecutor() {
		return executor;
	}
	
}
