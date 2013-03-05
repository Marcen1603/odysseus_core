package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;

public final class TileServer {
	
    private final String url;
    private final int maxZoom;
    private boolean broken;
    private TileCache cache;

    public TileServer(String url, int maxZoom, int format, ScreenManager manager) {
        this.url = url;
        this.maxZoom = maxZoom;
        this.cache = new TileCache(manager);;
    }

    public String toString() {
        return url;
    }

    public int getMaxZoom() {
        return maxZoom;
    }
    public String getURL() {
        return url;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

	public TileCache getCache() {
		return cache;
	}

}