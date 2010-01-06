package de.uniol.inf.is.odysseus.viewer.ctrl;

import de.uniol.inf.is.odysseus.viewer.model.create.IModelManager;

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

	public IModelManager<C> getModelManager();
	

}
