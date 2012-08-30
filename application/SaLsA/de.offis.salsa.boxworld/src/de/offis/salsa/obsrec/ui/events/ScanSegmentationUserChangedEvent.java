package de.offis.salsa.obsrec.ui.events;

public class ScanSegmentationUserChangedEvent {

    private String scanSegmentation;
    
    public ScanSegmentationUserChangedEvent(String string) {
        this.scanSegmentation = string;
    }

    public String getScanSegmentation() {
        return scanSegmentation;
    }
}
