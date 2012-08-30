package de.offis.salsa.obsrec.ui.events;

import java.util.ArrayList;

public class ObjectRulesUserChangedEvent {

    private ArrayList<String> active;    
    
    public ObjectRulesUserChangedEvent(ArrayList<String> active) {
        this.active = active;
    }

    public ArrayList<String> getActive() {
        return active;
    }    
}
