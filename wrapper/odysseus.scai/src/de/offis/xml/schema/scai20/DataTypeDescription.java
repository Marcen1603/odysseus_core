/*
 * XML Type:  DataTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.DataTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML DataTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface DataTypeDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(DataTypeDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("datatypedescription006ctype");
    
    /**
     * Gets the "name" element
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" element
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" element
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" element
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets the "StringType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.StringType getStringType();
    
    /**
     * True if has "StringType" element
     */
    boolean isSetStringType();
    
    /**
     * Sets the "StringType" element
     */
    void setStringType(de.offis.xml.schema.scai20.DataTypeDescription.StringType stringType);
    
    /**
     * Appends and returns a new empty "StringType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.StringType addNewStringType();
    
    /**
     * Unsets the "StringType" element
     */
    void unsetStringType();
    
    /**
     * Gets the "DecimalType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.DecimalType getDecimalType();
    
    /**
     * True if has "DecimalType" element
     */
    boolean isSetDecimalType();
    
    /**
     * Sets the "DecimalType" element
     */
    void setDecimalType(de.offis.xml.schema.scai20.DataTypeDescription.DecimalType decimalType);
    
    /**
     * Appends and returns a new empty "DecimalType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.DecimalType addNewDecimalType();
    
    /**
     * Unsets the "DecimalType" element
     */
    void unsetDecimalType();
    
    /**
     * Gets the "BinaryType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.BinaryType getBinaryType();
    
    /**
     * True if has "BinaryType" element
     */
    boolean isSetBinaryType();
    
    /**
     * Sets the "BinaryType" element
     */
    void setBinaryType(de.offis.xml.schema.scai20.DataTypeDescription.BinaryType binaryType);
    
    /**
     * Appends and returns a new empty "BinaryType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.BinaryType addNewBinaryType();
    
    /**
     * Unsets the "BinaryType" element
     */
    void unsetBinaryType();
    
    /**
     * Gets the "ListType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.ListType getListType();
    
    /**
     * True if has "ListType" element
     */
    boolean isSetListType();
    
    /**
     * Sets the "ListType" element
     */
    void setListType(de.offis.xml.schema.scai20.DataTypeDescription.ListType listType);
    
    /**
     * Appends and returns a new empty "ListType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.ListType addNewListType();
    
    /**
     * Unsets the "ListType" element
     */
    void unsetListType();
    
    /**
     * Gets the "EnumType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.EnumType getEnumType();
    
    /**
     * True if has "EnumType" element
     */
    boolean isSetEnumType();
    
    /**
     * Sets the "EnumType" element
     */
    void setEnumType(de.offis.xml.schema.scai20.DataTypeDescription.EnumType enumType);
    
    /**
     * Appends and returns a new empty "EnumType" element
     */
    de.offis.xml.schema.scai20.DataTypeDescription.EnumType addNewEnumType();
    
    /**
     * Unsets the "EnumType" element
     */
    void unsetEnumType();
    
    /**
     * An XML StringType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface StringType extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(StringType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("stringtype4b53elemtype");
        
        /**
         * Gets the "min" element
         */
        long getMin();
        
        /**
         * Gets (as xml) the "min" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMin();
        
        /**
         * True if has "min" element
         */
        boolean isSetMin();
        
        /**
         * Sets the "min" element
         */
        void setMin(long min);
        
        /**
         * Sets (as xml) the "min" element
         */
        void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min);
        
        /**
         * Unsets the "min" element
         */
        void unsetMin();
        
        /**
         * Gets the "max" element
         */
        long getMax();
        
        /**
         * Gets (as xml) the "max" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMax();
        
        /**
         * True if has "max" element
         */
        boolean isSetMax();
        
        /**
         * Sets the "max" element
         */
        void setMax(long max);
        
        /**
         * Sets (as xml) the "max" element
         */
        void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max);
        
        /**
         * Unsets the "max" element
         */
        void unsetMax();
        
        /**
         * Gets the "regex" element
         */
        java.lang.String getRegex();
        
        /**
         * Gets (as xml) the "regex" element
         */
        org.apache.xmlbeans.XmlString xgetRegex();
        
        /**
         * True if has "regex" element
         */
        boolean isSetRegex();
        
        /**
         * Sets the "regex" element
         */
        void setRegex(java.lang.String regex);
        
        /**
         * Sets (as xml) the "regex" element
         */
        void xsetRegex(org.apache.xmlbeans.XmlString regex);
        
        /**
         * Unsets the "regex" element
         */
        void unsetRegex();
        
        /**
         * Gets the "defaultvalue" element
         */
        java.lang.String getDefaultvalue();
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        org.apache.xmlbeans.XmlString xgetDefaultvalue();
        
        /**
         * Sets the "defaultvalue" element
         */
        void setDefaultvalue(java.lang.String defaultvalue);
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        void xsetDefaultvalue(org.apache.xmlbeans.XmlString defaultvalue);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.DataTypeDescription.StringType newInstance() {
              return (de.offis.xml.schema.scai20.DataTypeDescription.StringType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.DataTypeDescription.StringType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.DataTypeDescription.StringType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML DecimalType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface DecimalType extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(DecimalType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("decimaltype8423elemtype");
        
        /**
         * Gets the "min" element
         */
        float getMin();
        
        /**
         * Gets (as xml) the "min" element
         */
        org.apache.xmlbeans.XmlFloat xgetMin();
        
        /**
         * True if has "min" element
         */
        boolean isSetMin();
        
        /**
         * Sets the "min" element
         */
        void setMin(float min);
        
        /**
         * Sets (as xml) the "min" element
         */
        void xsetMin(org.apache.xmlbeans.XmlFloat min);
        
        /**
         * Unsets the "min" element
         */
        void unsetMin();
        
        /**
         * Gets the "max" element
         */
        float getMax();
        
        /**
         * Gets (as xml) the "max" element
         */
        org.apache.xmlbeans.XmlFloat xgetMax();
        
        /**
         * True if has "max" element
         */
        boolean isSetMax();
        
        /**
         * Sets the "max" element
         */
        void setMax(float max);
        
        /**
         * Sets (as xml) the "max" element
         */
        void xsetMax(org.apache.xmlbeans.XmlFloat max);
        
        /**
         * Unsets the "max" element
         */
        void unsetMax();
        
        /**
         * Gets the "scale" element
         */
        float getScale();
        
        /**
         * Gets (as xml) the "scale" element
         */
        org.apache.xmlbeans.XmlFloat xgetScale();
        
        /**
         * True if has "scale" element
         */
        boolean isSetScale();
        
        /**
         * Sets the "scale" element
         */
        void setScale(float scale);
        
        /**
         * Sets (as xml) the "scale" element
         */
        void xsetScale(org.apache.xmlbeans.XmlFloat scale);
        
        /**
         * Unsets the "scale" element
         */
        void unsetScale();
        
        /**
         * Gets the "defaultvalue" element
         */
        float getDefaultvalue();
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        org.apache.xmlbeans.XmlFloat xgetDefaultvalue();
        
        /**
         * Sets the "defaultvalue" element
         */
        void setDefaultvalue(float defaultvalue);
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        void xsetDefaultvalue(org.apache.xmlbeans.XmlFloat defaultvalue);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.DataTypeDescription.DecimalType newInstance() {
              return (de.offis.xml.schema.scai20.DataTypeDescription.DecimalType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.DataTypeDescription.DecimalType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.DataTypeDescription.DecimalType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML BinaryType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface BinaryType extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(BinaryType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("binarytypeb343elemtype");
        
        /**
         * Gets the "min" element
         */
        long getMin();
        
        /**
         * Gets (as xml) the "min" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMin();
        
        /**
         * True if has "min" element
         */
        boolean isSetMin();
        
        /**
         * Sets the "min" element
         */
        void setMin(long min);
        
        /**
         * Sets (as xml) the "min" element
         */
        void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min);
        
        /**
         * Unsets the "min" element
         */
        void unsetMin();
        
        /**
         * Gets the "max" element
         */
        long getMax();
        
        /**
         * Gets (as xml) the "max" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMax();
        
        /**
         * True if has "max" element
         */
        boolean isSetMax();
        
        /**
         * Sets the "max" element
         */
        void setMax(long max);
        
        /**
         * Sets (as xml) the "max" element
         */
        void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max);
        
        /**
         * Unsets the "max" element
         */
        void unsetMax();
        
        /**
         * Gets the "defaultvalue" element
         */
        byte[] getDefaultvalue();
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        org.apache.xmlbeans.XmlBase64Binary xgetDefaultvalue();
        
        /**
         * Sets the "defaultvalue" element
         */
        void setDefaultvalue(byte[] defaultvalue);
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        void xsetDefaultvalue(org.apache.xmlbeans.XmlBase64Binary defaultvalue);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.DataTypeDescription.BinaryType newInstance() {
              return (de.offis.xml.schema.scai20.DataTypeDescription.BinaryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.DataTypeDescription.BinaryType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.DataTypeDescription.BinaryType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML ListType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface ListType extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ListType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("listtype4120elemtype");
        
        /**
         * Gets the "min" element
         */
        long getMin();
        
        /**
         * Gets (as xml) the "min" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMin();
        
        /**
         * True if has "min" element
         */
        boolean isSetMin();
        
        /**
         * Sets the "min" element
         */
        void setMin(long min);
        
        /**
         * Sets (as xml) the "min" element
         */
        void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min);
        
        /**
         * Unsets the "min" element
         */
        void unsetMin();
        
        /**
         * Gets the "max" element
         */
        long getMax();
        
        /**
         * Gets (as xml) the "max" element
         */
        org.apache.xmlbeans.XmlUnsignedInt xgetMax();
        
        /**
         * True if has "max" element
         */
        boolean isSetMax();
        
        /**
         * Sets the "max" element
         */
        void setMax(long max);
        
        /**
         * Sets (as xml) the "max" element
         */
        void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max);
        
        /**
         * Unsets the "max" element
         */
        void unsetMax();
        
        /**
         * Gets array of all "allowedvalue" elements
         */
        java.lang.String[] getAllowedvalueArray();
        
        /**
         * Gets ith "allowedvalue" element
         */
        java.lang.String getAllowedvalueArray(int i);
        
        /**
         * Gets (as xml) array of all "allowedvalue" elements
         */
        org.apache.xmlbeans.XmlString[] xgetAllowedvalueArray();
        
        /**
         * Gets (as xml) ith "allowedvalue" element
         */
        org.apache.xmlbeans.XmlString xgetAllowedvalueArray(int i);
        
        /**
         * Returns number of "allowedvalue" element
         */
        int sizeOfAllowedvalueArray();
        
        /**
         * Sets array of all "allowedvalue" element
         */
        void setAllowedvalueArray(java.lang.String[] allowedvalueArray);
        
        /**
         * Sets ith "allowedvalue" element
         */
        void setAllowedvalueArray(int i, java.lang.String allowedvalue);
        
        /**
         * Sets (as xml) array of all "allowedvalue" element
         */
        void xsetAllowedvalueArray(org.apache.xmlbeans.XmlString[] allowedvalueArray);
        
        /**
         * Sets (as xml) ith "allowedvalue" element
         */
        void xsetAllowedvalueArray(int i, org.apache.xmlbeans.XmlString allowedvalue);
        
        /**
         * Inserts the value as the ith "allowedvalue" element
         */
        void insertAllowedvalue(int i, java.lang.String allowedvalue);
        
        /**
         * Appends the value as the last "allowedvalue" element
         */
        void addAllowedvalue(java.lang.String allowedvalue);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "allowedvalue" element
         */
        org.apache.xmlbeans.XmlString insertNewAllowedvalue(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "allowedvalue" element
         */
        org.apache.xmlbeans.XmlString addNewAllowedvalue();
        
        /**
         * Removes the ith "allowedvalue" element
         */
        void removeAllowedvalue(int i);
        
        /**
         * Gets array of all "defaultvalue" elements
         */
        java.lang.String[] getDefaultvalueArray();
        
        /**
         * Gets ith "defaultvalue" element
         */
        java.lang.String getDefaultvalueArray(int i);
        
        /**
         * Gets (as xml) array of all "defaultvalue" elements
         */
        org.apache.xmlbeans.XmlString[] xgetDefaultvalueArray();
        
        /**
         * Gets (as xml) ith "defaultvalue" element
         */
        org.apache.xmlbeans.XmlString xgetDefaultvalueArray(int i);
        
        /**
         * Returns number of "defaultvalue" element
         */
        int sizeOfDefaultvalueArray();
        
        /**
         * Sets array of all "defaultvalue" element
         */
        void setDefaultvalueArray(java.lang.String[] defaultvalueArray);
        
        /**
         * Sets ith "defaultvalue" element
         */
        void setDefaultvalueArray(int i, java.lang.String defaultvalue);
        
        /**
         * Sets (as xml) array of all "defaultvalue" element
         */
        void xsetDefaultvalueArray(org.apache.xmlbeans.XmlString[] defaultvalueArray);
        
        /**
         * Sets (as xml) ith "defaultvalue" element
         */
        void xsetDefaultvalueArray(int i, org.apache.xmlbeans.XmlString defaultvalue);
        
        /**
         * Inserts the value as the ith "defaultvalue" element
         */
        void insertDefaultvalue(int i, java.lang.String defaultvalue);
        
        /**
         * Appends the value as the last "defaultvalue" element
         */
        void addDefaultvalue(java.lang.String defaultvalue);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "defaultvalue" element
         */
        org.apache.xmlbeans.XmlString insertNewDefaultvalue(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "defaultvalue" element
         */
        org.apache.xmlbeans.XmlString addNewDefaultvalue();
        
        /**
         * Removes the ith "defaultvalue" element
         */
        void removeDefaultvalue(int i);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.DataTypeDescription.ListType newInstance() {
              return (de.offis.xml.schema.scai20.DataTypeDescription.ListType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.DataTypeDescription.ListType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.DataTypeDescription.ListType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML EnumType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface EnumType extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(EnumType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("enumtype4a43elemtype");
        
        /**
         * Gets the "defaultvalue" element
         */
        java.lang.String getDefaultvalue();
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        org.apache.xmlbeans.XmlString xgetDefaultvalue();
        
        /**
         * Sets the "defaultvalue" element
         */
        void setDefaultvalue(java.lang.String defaultvalue);
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        void xsetDefaultvalue(org.apache.xmlbeans.XmlString defaultvalue);
        
        /**
         * Gets array of all "allowedvalue" elements
         */
        de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[] getAllowedvalueArray();
        
        /**
         * Gets ith "allowedvalue" element
         */
        de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue getAllowedvalueArray(int i);
        
        /**
         * Returns number of "allowedvalue" element
         */
        int sizeOfAllowedvalueArray();
        
        /**
         * Sets array of all "allowedvalue" element
         */
        void setAllowedvalueArray(de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[] allowedvalueArray);
        
        /**
         * Sets ith "allowedvalue" element
         */
        void setAllowedvalueArray(int i, de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue allowedvalue);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "allowedvalue" element
         */
        de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue insertNewAllowedvalue(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "allowedvalue" element
         */
        de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue addNewAllowedvalue();
        
        /**
         * Removes the ith "allowedvalue" element
         */
        void removeAllowedvalue(int i);
        
        /**
         * An XML allowedvalue(@http://xml.offis.de/schema/SCAI-2.0).
         *
         * This is a complex type.
         */
        public interface Allowedvalue extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Allowedvalue.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("allowedvalue4bfeelemtype");
            
            /**
             * Gets the "ordinal" element
             */
            long getOrdinal();
            
            /**
             * Gets (as xml) the "ordinal" element
             */
            org.apache.xmlbeans.XmlUnsignedInt xgetOrdinal();
            
            /**
             * Sets the "ordinal" element
             */
            void setOrdinal(long ordinal);
            
            /**
             * Sets (as xml) the "ordinal" element
             */
            void xsetOrdinal(org.apache.xmlbeans.XmlUnsignedInt ordinal);
            
            /**
             * Gets the "name" element
             */
            java.lang.String getName();
            
            /**
             * Gets (as xml) the "name" element
             */
            org.apache.xmlbeans.XmlString xgetName();
            
            /**
             * Sets the "name" element
             */
            void setName(java.lang.String name);
            
            /**
             * Sets (as xml) the "name" element
             */
            void xsetName(org.apache.xmlbeans.XmlString name);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue newInstance() {
                  return (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.DataTypeDescription.EnumType newInstance() {
              return (de.offis.xml.schema.scai20.DataTypeDescription.EnumType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.DataTypeDescription.EnumType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.DataTypeDescription.EnumType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.DataTypeDescription newInstance() {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.DataTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.DataTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
