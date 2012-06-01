/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.builder.types;

/**
 *
 * @author Claas
 */
public class SCAISensorReference {

    private boolean referenceByID = false;
    private String name = null;
    private String domainName = null;
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

    public String getDomainName() {
        if(!referenceByID)
            return domainName;
        else return null;
    }

    public boolean isReferenceByID() {
        return referenceByID;
    }

    /**
     * Constructor to create SCAIReference from id or name
     * @param id
     * @param name
     */
    public SCAISensorReference(String id) {
        this.id = id;
        this.referenceByID = true;
    }

    public SCAISensorReference(String name, String domainName) {
            this.name = name;
            this.domainName = domainName;
            this.referenceByID = false;
    }

    public SCAISensorReference(String id, String name, String domainName) {
        if (name != null && domainName != null) {
            this.name = name;
            this.domainName = domainName;
            this.referenceByID = false;
        }
        else if (id != null) {
            this.id = id;
            this.referenceByID = true;
        }
    }
}
