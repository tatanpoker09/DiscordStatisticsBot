package com.eilers.tatanpoker09;

public abstract class Module {
    private boolean enabled;

    /**
     * Method to be overriden with custom functionality.
     * Called on module being loaded. This happens EVERY TIME on startup
     */
    public void onLoad(){

    }

    /**
     * Method to be overriden with custom functionality.
     * Called on module being enabled. Only happens if @property(enabled) = true.
     */
    public void onEnable(){

    }

    /**
     * Method to be overriden with custom functionality.
     * Called on module being disabled. Called whenever the module passes from being enabled to disabled.
     * Includes bot shutdown.
     */
    public void onDisable(){

    }
}
