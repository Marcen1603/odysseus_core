/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.builder.types;

/**
 *
 * @author Claas
 */
public class SCAIReference {

    private boolean referenceByID = false;
    private String name = null;
    private String id = null;

    public String getId() {
        if(referenceByID)
            return id;
        else
            return null;
    }

    public String getName() {
        if(!referenceByID)
            return name;
        else return null;
    }

    public boolean isReferenceByID() {
        return referenceByID;
    }

    public SCAIReference(String identifier, boolean refByID) {
        this.referenceByID = refByID;
        if(refByID)
            this.id = identifier;
        else
            this.name = identifier;
    }

    /**
     * Constructor to create SCAIReference from id or name
     * @param id
     * @param name
     */
    public SCAIReference(String id, String name) {
        if (id != null) {
            this.id = id;
            if (name != null) {
                this.name = name;
            }
            else this.referenceByID = true;
        }
        else {
            this.name = name;
            this.referenceByID = false;
        }
    }
}
