package de.offis.salsa.obsrec.ui;

import java.awt.Checkbox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.google.common.eventbus.EventBus;

/**
 *
 * @author Alex
 */
public class ObjectRulesPanel extends JPanel {

    private HashMap<String, Checkbox> boxes = new HashMap<String, Checkbox>();
    
    private EventBus events;
    
    private ArrayList<String> active = new ArrayList<String>();
    
    public ObjectRulesPanel(EventBus events) {
        this.events = events;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    public void setObjectRule(String name, Boolean state){
        if(boxes.containsKey(name)){
            boxes.get(name).setState(state);
        } else {
            Checkbox b = createCheckbox(name, state);
            boxes.put(name, b);
            add(b);
        }
    }
    
    private Checkbox createCheckbox(String name, Boolean state){
        Checkbox b = new Checkbox(name, state);
        b.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Checkbox cb = (Checkbox)e.getSource();
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    active.add(cb.getLabel());
                } else {
                    active.remove(cb.getLabel());
                }
                
                events.post(new de.offis.salsa.obsrec.ui.events.ObjectRulesUserChangedEvent(active));
            }
        });
        
        return b;
    }
}
