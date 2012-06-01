/*
 * XML Type:  OperatorGroupStatus
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorGroupStatus
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML OperatorGroupStatus(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class OperatorGroupStatusImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorGroupStatus
{
    private static final long serialVersionUID = 1L;
    
    public OperatorGroupStatusImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DEPLOYED$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "deployed");
    
    
    /**
     * Gets the "deployed" element
     */
    public boolean getDeployed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEPLOYED$0, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "deployed" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetDeployed()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(DEPLOYED$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "deployed" element
     */
    public void setDeployed(boolean deployed)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEPLOYED$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEPLOYED$0);
            }
            target.setBooleanValue(deployed);
        }
    }
    
    /**
     * Sets (as xml) the "deployed" element
     */
    public void xsetDeployed(org.apache.xmlbeans.XmlBoolean deployed)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(DEPLOYED$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(DEPLOYED$0);
            }
            target.set(deployed);
        }
    }
}
