/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.io;

/**
 * Version information for the Prism engine.
 * Used for tracking compatibility between different versions of the engine.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class PrismVersion {
    
    /** Beta 1 release identifier */
    public static final String VERSION = "B1";
    
    /** Numeric version code for comparison (1000 = 1.0.00) */
    public static final int VERSION_CODE = 1000;
    
    /** Human-readable release name */
    public static final String RELEASE_NAME = "Beta 1";
    
    /**
     * Returns the full version string for display.
     *
     * @return Formatted version string (e.g., "Prism B1 (Beta 1)")
     */
    public static String getFullVersion() {
        return "Prism " + VERSION + " (" + RELEASE_NAME + ")";
    }
}

