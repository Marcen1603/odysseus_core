package de.offis.scaiconnector;

import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScaiDTO {

    private HashMap<String, Object> data;

    public ScaiDTO() {
        data = new HashMap<String, Object>();
    }

    /**
     * DataType, DataElement usw
     * 
     * @param id
     */
    public void setScaiId(String id) {
        set("scaiId", id);
    }

    public String getScaiId() {
        return (String) get("scaiId");
    }

    /**
     * Atomic, Complex usw
     *
     * @param type
     */
    public void setScaiType(String type) {
        set("scaiType", type);
    }

    public String getScaiType() {
        return (String) get("scaiType");
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public Boolean getBoolean(String key) {
        if (!data.containsKey(key)) {
            return false;
        }
        return (Boolean) data.get(key);
    }

    public Float getFloat(String key) {
        try {
            return Float.parseFloat(getString(key));
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    public Integer getInt(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	public SCAIReference[] getSCAIRefArray(String key) {
        if (!data.containsKey(key)) {
            return new SCAIReference[0];
        }

        List<String> list = (ArrayList<String>) data.get(key);
        SCAIReference[] refs = new SCAIReference[list.size()];
        int i = 0;
        for (String e : list) {
            refs[i] = new SCAIReference(e, false);
            i++;
        }
        return refs;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }
}
