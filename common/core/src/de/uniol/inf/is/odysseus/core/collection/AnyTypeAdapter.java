package de.uniol.inf.is.odysseus.core.collection;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AnyTypeAdapter extends XmlAdapter<Object,Object> {
	  public Object unmarshal(Object v) { return v; }
	  public Object marshal(Object v) { return v; }
	}