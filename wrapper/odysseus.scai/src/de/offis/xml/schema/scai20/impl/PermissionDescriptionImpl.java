/*
 * XML Type:  PermissionDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.PermissionDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML PermissionDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class PermissionDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.PermissionDescription
{
    private static final long serialVersionUID = 1L;
    
    public PermissionDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PERMISSION$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Permission");
    private static final javax.xml.namespace.QName PROPERTY$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Property");
    private static final javax.xml.namespace.QName INHERITABLE$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "inheritable");
    
    
    /**
     * Gets the "Permission" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Permission getPermission()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Permission target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Permission)get_store().find_element_user(PERMISSION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Permission" element
     */
    public void setPermission(de.offis.xml.schema.scai20.PermissionDescription.Permission permission)
    {
        generatedSetterHelperImpl(permission, PERMISSION$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Permission" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Permission addNewPermission()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Permission target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Permission)get_store().add_element_user(PERMISSION$0);
            return target;
        }
    }
    
    /**
     * Gets array of all "Property" elements
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Property[] getPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROPERTY$2, targetList);
            de.offis.xml.schema.scai20.PermissionDescription.Property[] result = new de.offis.xml.schema.scai20.PermissionDescription.Property[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Property" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Property getPropertyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Property)get_store().find_element_user(PROPERTY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Property" element
     */
    public int sizeOfPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY$2);
        }
    }
    
    /**
     * Sets array of all "Property" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setPropertyArray(de.offis.xml.schema.scai20.PermissionDescription.Property[] propertyArray)
    {
        check_orphaned();
        arraySetterHelper(propertyArray, PROPERTY$2);
    }
    
    /**
     * Sets ith "Property" element
     */
    public void setPropertyArray(int i, de.offis.xml.schema.scai20.PermissionDescription.Property property)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Property)get_store().find_element_user(PROPERTY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(property);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Property" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Property insertNewProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Property)get_store().insert_element_user(PROPERTY$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Property" element
     */
    public de.offis.xml.schema.scai20.PermissionDescription.Property addNewProperty()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.PermissionDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.PermissionDescription.Property)get_store().add_element_user(PROPERTY$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "Property" element
     */
    public void removeProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY$2, i);
        }
    }
    
    /**
     * Gets the "inheritable" element
     */
    public boolean getInheritable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INHERITABLE$4, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "inheritable" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetInheritable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(INHERITABLE$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "inheritable" element
     */
    public boolean isSetInheritable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INHERITABLE$4) != 0;
        }
    }
    
    /**
     * Sets the "inheritable" element
     */
    public void setInheritable(boolean inheritable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INHERITABLE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INHERITABLE$4);
            }
            target.setBooleanValue(inheritable);
        }
    }
    
    /**
     * Sets (as xml) the "inheritable" element
     */
    public void xsetInheritable(org.apache.xmlbeans.XmlBoolean inheritable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(INHERITABLE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(INHERITABLE$4);
            }
            target.set(inheritable);
        }
    }
    
    /**
     * Unsets the "inheritable" element
     */
    public void unsetInheritable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INHERITABLE$4, 0);
        }
    }
    /**
     * An XML Permission(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class PermissionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.PermissionDescription.Permission
    {
        private static final long serialVersionUID = 1L;
        
        public PermissionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GRANT$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "grant");
        private static final javax.xml.namespace.QName WITHDRAW$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "withdraw");
        
        
        /**
         * Gets the "grant" element
         */
        public java.lang.String getGrant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRANT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "grant" element
         */
        public org.apache.xmlbeans.XmlString xgetGrant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRANT$0, 0);
                return target;
            }
        }
        
        /**
         * True if has "grant" element
         */
        public boolean isSetGrant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(GRANT$0) != 0;
            }
        }
        
        /**
         * Sets the "grant" element
         */
        public void setGrant(java.lang.String grant)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRANT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GRANT$0);
                }
                target.setStringValue(grant);
            }
        }
        
        /**
         * Sets (as xml) the "grant" element
         */
        public void xsetGrant(org.apache.xmlbeans.XmlString grant)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GRANT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GRANT$0);
                }
                target.set(grant);
            }
        }
        
        /**
         * Unsets the "grant" element
         */
        public void unsetGrant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(GRANT$0, 0);
            }
        }
        
        /**
         * Gets the "withdraw" element
         */
        public java.lang.String getWithdraw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WITHDRAW$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "withdraw" element
         */
        public org.apache.xmlbeans.XmlString xgetWithdraw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WITHDRAW$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "withdraw" element
         */
        public boolean isSetWithdraw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(WITHDRAW$2) != 0;
            }
        }
        
        /**
         * Sets the "withdraw" element
         */
        public void setWithdraw(java.lang.String withdraw)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WITHDRAW$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WITHDRAW$2);
                }
                target.setStringValue(withdraw);
            }
        }
        
        /**
         * Sets (as xml) the "withdraw" element
         */
        public void xsetWithdraw(org.apache.xmlbeans.XmlString withdraw)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WITHDRAW$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(WITHDRAW$2);
                }
                target.set(withdraw);
            }
        }
        
        /**
         * Unsets the "withdraw" element
         */
        public void unsetWithdraw()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(WITHDRAW$2, 0);
            }
        }
    }
    /**
     * An XML Property(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class PropertyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.PermissionDescription.Property
    {
        private static final long serialVersionUID = 1L;
        
        public PropertyImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName KEY$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "key");
        private static final javax.xml.namespace.QName VALUE$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "value");
        
        
        /**
         * Gets the "key" element
         */
        public java.lang.String getKey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "key" element
         */
        public org.apache.xmlbeans.XmlString xgetKey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KEY$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "key" element
         */
        public void setKey(java.lang.String key)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(KEY$0);
                }
                target.setStringValue(key);
            }
        }
        
        /**
         * Sets (as xml) the "key" element
         */
        public void xsetKey(org.apache.xmlbeans.XmlString key)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(KEY$0);
                }
                target.set(key);
            }
        }
        
        /**
         * Gets the "value" element
         */
        public java.lang.String getValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "value" element
         */
        public org.apache.xmlbeans.XmlString xgetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "value" element
         */
        public void setValue(java.lang.String value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE$2);
                }
                target.setStringValue(value);
            }
        }
        
        /**
         * Sets (as xml) the "value" element
         */
        public void xsetValue(org.apache.xmlbeans.XmlString value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUE$2);
                }
                target.set(value);
            }
        }
    }
}
