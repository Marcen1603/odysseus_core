package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.tile;

import java.io.Serializable;

public class Stats implements Serializable {
    
	private static final long serialVersionUID = -5201181117048315573L;
	public int tileCount;
    public long dt;
    
    public Stats() {
        reset();
    }
    
    
    public void reset() {
        tileCount = 0;
        dt = 0;
    }
}