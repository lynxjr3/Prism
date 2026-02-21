/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.model;

/**
 * Configuration settings for creating a new Prism document.
 * Used by the new file wizard to pass user preferences to the canvas.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class NewDocumentConfig {
    
    /** Whether the canvas should be infinite (expands as you draw) */
    public boolean infinite;
    
    /** Whether the grid should be visible by default */
    public boolean grid;
    
    /** Initial canvas width in pixels */
    public int width;
    
    /** Initial canvas height in pixels */
    public int height;
}
