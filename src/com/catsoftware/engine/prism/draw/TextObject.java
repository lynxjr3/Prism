/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 * A text object that can be drawn on the canvas.
 * Supports custom fonts, colors, opacity, rotation, and resizing.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class TextObject implements DrawableObject, Serializable {
    private String text;
    private int x, y;
    private Font font;
    private Color color;
    private double rotation = 0;
    private float alpha = 1.0f;

    /**
     * Creates a new text object.
     *
     * @param t The text content
     * @param x The X coordinate (baseline position)
     * @param y The Y coordinate (baseline position)
     * @param f The font to use
     * @param c The text color
     * @param a The opacity level (0.0 - 1.0)
     */
    public TextObject(String t, int x, int y, Font f, Color c, float a) {
        text = t;
        this.x = x;
        this.y = y;
        font = f;
        color = c;
        alpha = a;
    }

    @Override
    public void draw(Graphics2D g) {
        // Save original composite
        Composite originalComposite = g.getComposite();
        
        // Apply alpha
        if (alpha < 1.0f) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        
        // Save transform for rotation
        AffineTransform original = g.getTransform();
        Rectangle bounds = getBounds();
        g.rotate(Math.toRadians(rotation), bounds.getCenterX(), bounds.getCenterY());
        
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
        
        // Restore transform and composite
        g.setTransform(original);
        g.setComposite(originalComposite);
    }

    @Override
    public boolean contains(Point p) {
        Rectangle r = new Rectangle(
            x - 4, 
            y - font.getSize(), 
            text.length() * font.getSize(), 
            font.getSize()
        );
        return r.contains(p);
    }

    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    @Override
    public Rectangle getBounds() {
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
        int w = fm.stringWidth(text);
        int h = fm.getHeight();
        Rectangle unrotated = new Rectangle(x, y - h, w, h);
        
        if (rotation == 0) {
            return unrotated;
        }
        
        // Calculate rotated bounds
        double rad = Math.toRadians(rotation);
        double cos = Math.abs(Math.cos(rad));
        double sin = Math.abs(Math.sin(rad));
        
        int newWidth = (int)(unrotated.width * cos + unrotated.height * sin);
        int newHeight = (int)(unrotated.width * sin + unrotated.height * cos);
        
        int centerX = unrotated.x + unrotated.width/2;
        int centerY = unrotated.y + unrotated.height/2;
        
        int newX = centerX - newWidth/2;
        int newY = centerY - newHeight/2;
        
        return new Rectangle(newX, newY, newWidth, newHeight);
    }
    
    @Override
    public void resize(double scaleX, double scaleY) {
        // Scale the font size
        int newSize = (int)(font.getSize() * Math.min(scaleX, scaleY));
        font = font.deriveFont((float)newSize);
    }

    @Override
    public void resizeAbsolute(int newWidth, int newHeight) {
        Rectangle bounds = getBounds();
        if (bounds.width == 0 || bounds.height == 0) return;
        
        double scale = Math.min(
            (double)newWidth / bounds.width,
            (double)newHeight / bounds.height
        );
        
        int newSize = (int)(font.getSize() * scale);
        font = font.deriveFont((float)newSize);
    }

    @Override
    public void fill(Graphics2D g, Color fillColor) {
        System.out.println("  TextObject.fill() called");
        System.out.println("  Changing color from " + color + " to " + fillColor);
        this.color = fillColor;
        draw(g);
        System.out.println("  Text fill complete");
    }

    /**
     * Sets the text color.
     *
     * @param color The new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the text content.
     *
     * @return The text
     */
    public String getText() { return text; }
    
    /**
     * Returns the X coordinate.
     *
     * @return The X coordinate
     */
    public int getX() { return x; }
    
    /**
     * Returns the Y coordinate.
     *
     * @return The Y coordinate
     */
    public int getY() { return y; }
    
    /**
     * Returns the font.
     *
     * @return The current font
     */
    public Font getFont() { return font; }
    
    /**
     * Returns the text color.
     *
     * @return The current color
     */
    public Color getColor() { return color; }

    /**
     * Sets the text content.
     *
     * @param text The new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets the font.
     *
     * @param font The new font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Sets the opacity level.
     *
     * @param alpha Value between 0.1 and 1.0
     */
    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.1f, Math.min(1.0f, alpha));
    }

    /**
     * Returns the opacity level.
     *
     * @return The current alpha value
     */
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void setRotation(double degrees) {
        this.rotation = degrees;
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void rotate(double delta) {
        rotation += delta;
        rotation = rotation % 360;
    }

    /**
     * Sets a gradient (not supported for text - falls back to solid color).
     *
     * @param color1 Start color (ignored)
     * @param color2 End color (ignored)
     * @param horizontal Direction (ignored)
     */
    @Override
    public void setGradient(Color color1, Color color2, boolean horizontal) {
        // Text doesn't support gradients - just set color
        this.color = color1;
    }

    @Override
    public boolean hasGradient() {
        return false;
    }

    @Override
    public void removeGradient() {
        // Nothing to remove
    }
}
