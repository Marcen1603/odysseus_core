package de.uniol.inf.is.odysseus.iec61970.library.serializable;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;
/**
 * Repräsentation eines TSDA Objektes. Dieser enthält alle wichtigen Daten eines Item Elements und seinen ItemValue Elementen aus dem TSDA Datenmodell
 * @author Mart Köhler
 *
 */
public class TSDAObject implements Serializable, IClone{
	private IResourceID id;
	//value, timestamp, quality
	private ArrayList<ArrayList> data;
	public TSDAObject(IResourceID identifier, ArrayList<ArrayList> data) {
		this.id = identifier;
		this.data = data;
	}
	public IResourceID getId() {
		return id;
	}
	public ArrayList<ArrayList> getData() {
		return data;
	}
	public IClone clone() {
		return new TSDAObject(id,data);
	}
}
