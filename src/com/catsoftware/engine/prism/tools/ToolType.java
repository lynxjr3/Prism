/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.tools;

/**
 * Enumeration of all available measurement tools in Prism's toolbox.
 * These tools are used for measuring distances, angles, and other properties
 * on the canvas.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public enum ToolType {
    /** Measures straight line distance between two points */
    RULER,
    
    /** Measures angles between two lines */
    PROTRACTOR,
    
    /** Measures area of a triangle defined by three points */
    TRIANGLE,
    
    /** Draws circles and arcs with a specified radius */
    COMPASS,
    
    /** Measures width and height of objects (like real calipers) */
    CALIPER,
    
    /** Checks if a line is horizontal or vertical */
    LEVEL
}
