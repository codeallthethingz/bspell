package com.google.bspell.model;

import java.util.ArrayList;
import java.util.List;

public final class Word {

    List<String> suggestions = new ArrayList<String>();
    List<Location> locations = new ArrayList<Location>();

    public List<Location> getLocations() {
        return locations;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    private StringBuffer value = new StringBuffer();

    public StringBuffer getValue() {
        return value;
    }

    public void setValue(final StringBuffer newValue) {
        this.value = newValue;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Check if this object is equal to another object.
     * 
     * <p>For the definition of the object equivalence relation
     * see {@link java.lang.Object#equals(Object)}.</p>
     * 
     * @param obj another, possibly equal object.
     * 
     * @return true if the objects are equal, false otherwise.
     * 
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }

        if (obj instanceof Word) {
            Word o = (Word) obj;
            return o.getValue().toString().equals(value.toString());
        }

        if (obj instanceof String) {
            return value.toString().equals(obj.toString());
        }
        return false;
    }
    
    public String toString() {
        return value.toString();
    }
}