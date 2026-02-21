/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.shapes;

/**
 * Enumeration of all available shape types in Prism.
 * Used by ShapeObject to determine which shape to draw.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public enum ShapeType {
    /** A four-sided polygon with equal angles */
    SQUARE,
    
    /** A three-sided polygon */
    TRIANGLE,
    
    /** A round shape defined by a center point and radius */
    CIRCLE,
    
    /** A heart-shaped curve (parametric equation) */
    HEART,
    
    /** A straight line between two points */
    LINE,
    
    /** A multi-pointed star shape */
    STAR,
    
    /** An arrow shape pointing in a direction */
    ARROW,
    
    /** A five-sided polygon */
    PENTAGON,
    
    /** A six-sided polygon */
    HEXAGON,
    
    /** An eight-sided polygon */
    OCTAGON,
    
    /** A plus/cross shape */
    CROSS,
    
    /** A diamond/rhombus shape */
    DIAMOND
}
