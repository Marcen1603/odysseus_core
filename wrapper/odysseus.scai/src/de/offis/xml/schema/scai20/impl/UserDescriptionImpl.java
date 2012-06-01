/*
 * XML Type:  UserDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.UserDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML UserDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class UserDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.UserDescription
{
    private static final long serialVersionUID = 1L;
    
    public UserDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName USERNAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "username");
    private static final javax.xml.namespace.QName PASSWORD$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "password");
    private static final javax.xml.namespace.QName PERMISSION$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Permission");
    private static final javax.xml.namespace.QName ROLE$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "role");
    
    
    /**
     * Gets the "username" element
     */
    public java.lang.String getUsername()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "username" element
     */
    public org.apache.xmlbeans.XmlString xgetUsername()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "username" element
     */
    public void setUsername(java.lang.String username)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERNAME$0);
            }
            target.setStringValue(username);
        }
    }
    
    /**
     * Sets (as xml) the "username" element
     */
    public void xsetUsername(org.apache.xmlbeans.XmlString username)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERNAME$0);
            }
            target.set(username);
        }
    }
    
    /**
     * Gets the "password" element
     */
    public byte[] getPassword()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASSWORD$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getByteArrayValue();
        }
    }
    
    /**
     * Gets (as xml) the "password" element
     */
    public org.apache.xmlbeans.XmlBase64Binary xgetPassword()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PASSWORD$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "password" element
     */
    public void setPassword(byte[] password)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASSWORD$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PASSWORD$2);
            }
            target.setByteArrayValue(password);
        }
    }
    
    /**
     * Sets (as xml) the "password" element
     */
    public void xsetPassword(org.apache.xmlbeans.XmlBase64Binary password)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PASSWORD$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PASSWORD$2);
            }
            target.set(password);
        }
    }
    
    /**
     * Gets array of all "Permission" elements
     */
    public de.offis.xml.schema.scai20.PermissionDescription[] getPermissionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PERMISSION$4, targetList);
            de.offis.xml.schema.scai20.PermissionDescription[] result = new de.offis.xml.schema.scai20.PermissionDescription[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Permission" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription getPermissionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription)get_store().find_element_user(PERMISSION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Permission" element
     */
    public int sizeOfPermissionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PERMISSION$4);
        }
    }
    
    /**
     * Sets array of all "Permission" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setPermissionArray(de.offis.xml.schema.scai20.PermissionDescription[] permissionArray)
    {
        check_orphaned();
        arraySetterHelper(permissionArray, PERMISSION$4);
    }
    
    /**
     * Sets ith "Permission" element
     */
    public void setPermissionArray(int i, de.offis.xml.schema.scai20.PermissionDescription permission)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription)get_store().find_element_user(PERMISSION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(permission);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Permission" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription insertNewPermission(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription)get_store().insert_element_user(PERMISSION$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Permission" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription addNewPermission()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription)get_store().add_element_user(PERMISSION$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "Permission" element
     */
    public void removePermission(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PERMISSION$4, i);
        }
    }
    
    /**
     * Gets array of all "role" elements
     */
    public java.lang.String[] getRoleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ROLE$6, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "role" element
     */
    public java.lang.String getRoleArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ROLE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "role" elements
     */
    public org.apache.xmlbeans.XmlString[] xgetRoleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ROLE$6, targetList);
            org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "role" element
     */
    public org.apache.xmlbeans.XmlString xgetRoleArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ROLE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "role" element
     */
    public int sizeOfRoleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ROLE$6);
        }
    }
    
    /**
     * Sets array of all "role" element
     */
    public void setRoleArray(java.lang.String[] roleArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(roleArray, ROLE$6);
        }
    }
    
    /**
     * Sets ith "role" element
     */
    public void setRoleArray(int i, java.lang.String role)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ROLE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(role);
        }
    }
    
    /**
     * Sets (as xml) array of all "role" element
     */
    public void xsetRoleArray(org.apache.xmlbeans.XmlString[]roleArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(roleArray, ROLE$6);
        }
    }
    
    /**
     * Sets (as xml) ith "role" element
     */
    public void xsetRoleArray(int i, org.apache.xmlbeans.XmlString role)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ROLE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(role);
        }
    }
    
    /**
     * Inserts the value as the ith "role" element
     */
    public void insertRole(int i, java.lang.String role)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(ROLE$6, i);
            target.setStringValue(role);
        }
    }
    
    /**
     * Appends the value as the last "role" element
     */
    public void addRole(java.lang.String role)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ROLE$6);
            target.setStringValue(role);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "role" element
     */
    public org.apache.xmlbeans.XmlString insertNewRole(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(ROLE$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "role" element
     */
    public org.apache.xmlbeans.XmlString addNewRole()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ROLE$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "role" element
     */
    public void removeRole(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ROLE$6, i);
        }
    }
}
