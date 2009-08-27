package de.uniol.inf.is.odysseus.viewer.ctrl;

import de.uniol.inf.is.odysseus.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;

/**
 * Diese Schnittstelle stellt die Verbindung zwischen Model und View dar. Es beinhaltet
 * das Model und ist der Ansatzpunkt für größere Aktualisierungen bei Bedarf. Um die
 * Operationen bezüglich des Models durchführen zu können, wird eine Instanz der 
 * IModelProvider-Schnittstelle benötigt. Diese muss explizit über setModelProvider()
 * angegeben werden.
 *
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IController<C> {

	/**
	 * Liefert das Model des MVC-Architekturmodells zurück. Es kann nur dann
	 * ein GraphModel zurückgeliefert werden, wenn ein ModelProvider
	 * über setModelProvider() festgelegt wurde.
	 * 
	 * @return Model des MVC-Architekturmodells
	 */
	public IGraphModel<C> getModel();
	
	/**
	 * Liefert den dem Controller zugewiesenen ModelProvider zurück.
	 * 
	 * @return Der ModelProvider
	 */
	public IModelProvider<C> getModelProvider();
	
	/**
	 * Setzt den ModelProvider für diesen Controller. Dieser wird in der 
	 * Folgezeit verwendet, um das Model zu erstellen und zu aktualisieren.
	 * 
	 * @param modelProvider Der ModelProvider, welcher zur Generierung und 
	 * Aktualisierung des Models verwendet werden soll.
	 */
	public void setModelProvider( IModelProvider<C> modelProvider );
	
	/**
	 * Aktualisiert das Model, welches vom Controller verwaltet wird. Das setzt 
	 * voraus, dass ein ModelProvider über setModelProvider() gesetzt wurde.
	 */
	public void refreshModel();
}
