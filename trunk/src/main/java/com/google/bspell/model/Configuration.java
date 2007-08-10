package com.google.bspell.model;

public class Configuration {

    /**
     * Describe dictionary here.
     */
    private String dictionary;

    /**
     * Describe reservedDict here.
     */
    private String reservedDict;

    /**
     * Describe spellCheckConfig here.
     */
    private String spellCheckConfig;

    /**
     * Describe format here.
     */
    private String format;

    /**
     * Describe registry here.
     */
    private String registry;

    /**
     * Get the <code>Dictionary</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getDictionary() {
        return dictionary;
    }

    /**
     * Set the <code>Dictionary</code> value.
     *
     * @param newDictionary The new Dictionary value.
     */
    public final void setDictionary(final String newDictionary) {
        this.dictionary = newDictionary;
    }

    /**
     * Get the <code>ReservedDict</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getReservedDict() {
        return reservedDict;
    }

    /**
     * Set the <code>ReservedDict</code> value.
     *
     * @param newReservedDict The new ReservedDict value.
     */
    public final void setReservedDict(final String newReservedDict) {
        this.reservedDict = newReservedDict;
    }

    /**
     * Get the <code>SpellCheckConfig</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getSpellCheckConfig() {
        return spellCheckConfig;
    }

    /**
     * Set the <code>SpellCheckConfig</code> value.
     *
     * @param newSpellCheckConfig The new SpellCheckConfig value.
     */
    public final void setSpellCheckConfig(final String newSpellCheckConfig) {
        this.spellCheckConfig = newSpellCheckConfig;
    }

    /**
     * Get the <code>Format</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getFormat() {
        return format;
    }

    /**
     * Set the <code>Format</code> value.
     *
     * @param newFormat The new Format value.
     */
    public final void setFormat(final String newFormat) {
        this.format = newFormat;
    }

    /**
     * Get the <code>Registry</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getRegistry() {
        return registry;
    }

    /**
     * Set the <code>Registry</code> value.
     *
     * @param newRegistry The new Registry value.
     */
    public final void setRegistry(final String newRegistry) {
        this.registry = newRegistry;
    }
}