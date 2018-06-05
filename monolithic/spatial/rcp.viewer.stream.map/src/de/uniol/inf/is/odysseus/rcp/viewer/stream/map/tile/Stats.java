package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

public class Stats {
    
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