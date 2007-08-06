package mg.dynaquest.sourceselection;

import java.util.ArrayList;
import java.util.HashMap;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;


/**
 * @author  Jurij Henne  Ein Prototyp des RepositoryManagers, welches die Zugriffe auf das ConstantRepository kapselt und  Belegungen für ausgewählte Attribute in Form von ConstantenListen zurückliefert.  Der Manager ist zuständig für:  1. Initialisierung des Repository  2. Die vorhandenen Zuordungen (Attribut <-> KonstantenSet) vom Repository abfragen  3. Die Zuordnungen aufbereiten und in Form von KonstantenListe an die aufrufende CompensateAction zurück zu liefern
 */
public class RepositoryManager {

	/**
	 * Legt das Repository fest, welches die Daten liefern soll
	 * @uml.property  name="repository"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ConstantRepository repository;

	public RepositoryManager()
	{
		this.repository = new ConstantRepository();
	}
	
	/**
	 * Liefert für eine gewünschtes Attribut eine Liste von Belegungen. 
	 * Liest die Attributbelegungen als "fertige" Liste aus dem Repository aus. 
	 * @param attribute Das Attribut, welches die Belegungen erfordert
	 * @return
	 */
	public  SDFConstantList getConstantList(SDFAttribute attribute)
	{
		SDFConstantList newSet = new SDFConstantList();
		String attrQualName = attribute.getURI(false);
		HashMap constList = this.repository.getconstantList();
		ArrayList constants = (ArrayList)constList.get(attrQualName);
		if(constants!=null)
			for(int i = 0; i<constants.size(); i++)
			{
				SDFConstant nextConst = new SDFStringConstant("Const:"+System.currentTimeMillis(), (String)constants.get(i));
				newSet.add(nextConst);
			}	
		return newSet;
	}
	
	/**
	 * Liefert für eine gewünschtes Attribut eine Liste von Belegungen. 
	 * Liest die Belegungen als Interval-Angabe aus dem Repository und transformiert sie zu einer Liste von Werten
	 * @param attribute Das Attribut, welches die Belegungen erfordert
	 * @return
	 */
		public  SDFConstantList getIntervalList(SDFAttribute attribute)
		{
			SDFConstantList newSet = new SDFConstantList();
			String attrQualName = attribute.getURI(false);
			HashMap constList = this.repository.getIntervalList();
			ArrayList constants = (ArrayList)constList.get(attrQualName);
			if(constants!=null)
			{
				int start = (Integer)constants.get(0);
				int end = (Integer)constants.get(1);
				int granularity = (Integer)constants.get(2);
				for(int i = 0; i<(end-start); i+=granularity)
				{
					int nextValue = start+i;
					SDFConstant nextConst = new SDFNumberConstant("Const:"+System.currentTimeMillis(), String.valueOf(nextValue));
					newSet.add(nextConst);
				}	
			}

			return newSet;
		}

	/**
	 * Liefert für eine gewünschtes Attribut eine Liste von Belegungen. 
	 * Liest die Belegungen als Query aus dem Repository, spricht die Quellen an (TODO) und ermittelt so 
	 * die gesuchten Belegungen.
	 * Da DERZEIT weder der Anfrageformat spezifiziert ist, noch die Quellen vorliegen, leitet diese Methode die
	 * Aufrufe an die getConstantList() weiter.
	 * Beispiel: 
	 * Die ConstantRepository.sourceList enthält Belgungen für das Attribut Autor in Form von 
	 * fiktiven anfragen{"AutorAnfrage1", "AutorAnfrage2"}. Wenn wir nun die Anfrage "Autor" an die repository.getconstantList() 
	 * umleiten, erhalten wie die verfügbaren Belegungen {"Verne", "Wells", "Defoe", "Shakespeare"}.
	 * Damit kann die Ausführung der Hilfsquelle ausreichend simuliert werden. Selbstverständlich lassen sich die
	 * Belegunegen auch direkt anstatt der Anfragen ablegen, nur dann unterscheiden sich die getSourceList() und 
	 * getConstantList() überhaupt nicht mehr.
	 * @param attribute Das Attribut, welches die Belegungen erfordert
	 * @return
	 */
		public  SDFConstantList getSourceList(SDFAttribute attribute)
		{
			SDFConstantList newSet = new SDFConstantList();
			String attrQualName = attribute.getURI(false);
			HashMap constList = this.repository.getSourceList();
			ArrayList constants = (ArrayList)constList.get(attrQualName);
			if(constants!=null)
			{
				for(int i = 0; i<constants.size(); i++)
				{
					// Hier noch sehr DUMMY-haft. Da wir weder Anfragen auslesen , noch Quellen haben, lesen wir die 
					// Belegungen über die ConstantRepository.getconstantList() aus.
					HashMap _constList = this.repository.getconstantList();
					ArrayList _constants = (ArrayList)_constList.get(constants.get(i));
					if(_constants!=null)
						for(int j = 0; j<_constants.size(); j++)
						{
							SDFConstant nextConst = new SDFStringConstant("Const:"+System.currentTimeMillis(), (String)_constants.get(j));
							newSet.add(nextConst);
						}	
				}	
			}

			return newSet;
		}
		
		
}
