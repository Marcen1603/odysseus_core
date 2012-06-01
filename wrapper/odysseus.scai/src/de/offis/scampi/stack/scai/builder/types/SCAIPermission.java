/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.scai.builder.types;

import java.util.Map;


/**
 *
 * @author sbehrensen
 */
public class SCAIPermission {
    private String privilege;
    
    private Map<String, String> Properties;
    
    private Boolean inheritable;
    
    public enum PrivilegeActions { 
        GRANT, WITHDRAW;
    }
    
    private PrivilegeActions privilegeAction;

    public Map<String, String> getProperties() {
        return Properties;
    }

    public void setProperties(Map<String, String> Properties) {
        this.Properties = Properties;
    }

    public Boolean isInheritable() {
        return inheritable;
    }

    public void setInheritable(Boolean inheritable) {
        this.inheritable = inheritable;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public PrivilegeActions getPrivilegeAction() {
        return privilegeAction;
    }

    public void setPrivilegeAction(PrivilegeActions privilegeAction) {
        this.privilegeAction = privilegeAction;
    }
}
