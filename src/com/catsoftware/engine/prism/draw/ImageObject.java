/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * A drawable image object that can be placed on the canvas.
 * Supports transparency, rotation, and serialization for saving/loading.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class ImageObject implements DrawableObject, Serializable {
    private transient BufferedImage image;
    private int x, y, width, height;
    private double rotation = 0;
    private float alpha = 1.0f;
    private byte[] imageData;

    /**
     * Creates a new image object.
     *
     * @param img The source image
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param w The width
     * @param h The height
     */
    public ImageObject(BufferedImage img, int x, int y, int w, int h) {
        this.image = img;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        setImage(img);
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            // Save original composite
            Composite originalComposite = g.getComposite();
            
            // Apply alpha
            if (alpha < 1.0f) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
            
            // Save transform for rotation
            AffineTransform original = g.getTransform();
            int centerX = x + width/2;
            int centerY = y + height/2;
            g.rotate(Math.toRadians(rotation), centerX, centerY);
            
            // Draw the image
            g.drawImage(image, x, y, width, height, null);
            
            // Restore transform and composite
            g.setTransform(original);
            g.setComposite(originalComposite);
        }
    }
    
    @Override
    public boolean contains(Point p) {
        // For rotated images, we need to transform the point back
        if (rotation != 0) {
            int centerX = x + width/2;
            int centerY = y + height/2;
            
            // Translate point relative to center
            double dx = p.x - centerX;
            double dy = p.y - centerY;
            
            // Rotate point back by -rotation
            double rad = Math.toRadians(-rotation);
            double rotatedX = dx * Math.cos(rad) - dy * Math.sin(rad);
            double rotatedY = dx * Math.sin(rad) + dy * Math.cos(rad);
            
            // Check if rotated point is within unrotated bounds
            int checkX = centerX + (int)rotatedX;
            int checkY = centerY + (int)rotatedY;
            
            return new Rectangle(x, y, width, height).contains(checkX, checkY);
        }
        
        // No rotation - simple bounds check
        return new Rectangle(x, y, width, height).contains(p);
    }
    
    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    @Override
    public Rectangle getBounds() {
        if (rotation == 0) {
            return new Rectangle(x, y, width, height);
        }
        
        // Calculate rotated bounds
        double rad = Math.toRadians(rotation);
        double cos = Math.abs(Math.cos(rad));
        double sin = Math.abs(Math.sin(rad));
        
        int newWidth = (int)(width * cos + height * sin);
        int newHeight = (int)(width * sin + height * cos);
        
        int newX = x + (width - newWidth) / 2;
        int newY = y + (height - newHeight) / 2;
        
        return new Rectangle(newX, newY, newWidth, newHeight);
    }
    
    @Override
    public void resize(double scaleX, double scaleY) {
        width = (int)(width * scaleX);
        height = (int)(height * scaleY);
    }
    
    @Override
    public void resizeAbsolute(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
    }
    
    @Override
    public void fill(Graphics2D g, Color fillColor) {
        // Images can't be filled
    }
    
    /**
     * Returns the image data.
     * Restores from byte array if needed.
     *
     * @return The buffered image
     */
    public BufferedImage getImage() { 
        if (image == null && imageData != null) {
            try {
                image = ImageIO.read(new java.io.ByteArrayInputStream(imageData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return image; 
    }
    
    /** @return The X coordinate */
    public int getX() { return x; }
    
    /** @return The Y coordinate */
    public int getY() { return y; }
    
    /** @return The width */
    public int getWidth() { return width; }
    
    /** @return The height */
    public int getHeight() { return height; }

    /**
     * Sets the opacity level.
     *
     * @param alpha Value between 0.1 and 1.0
     */
    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.1f, Math.min(1.0f, alpha));
    }

    /** @return The current opacity level */
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

    @Override
    public void setGradient(Color color1, Color color2, boolean horizontal) {
        // Images don't support gradients
    }

    @Override
    public boolean hasGradient() {
        return false;
    }

    @Override
    public void removeGradient() {
        // Nothing to remove
    }

    /**
     * Sets the image and prepares it for serialization.
     *
     * @param img The new image
     */
    public void setImage(BufferedImage img) {
        this.image = img;
        // Convert to bytes for serialization
        if (img != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", baos);
                this.imageData = baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                this.imageData = null;
            }
        }
    }

    /**
     * Custom deserialization to restore image from bytes.
     *
     * @param in The object input stream
     * @throws java.io.IOException If reading fails
     * @throws ClassNotFoundException If class not found
     */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Restore image from bytes
        if (imageData != null) {
            try {
                this.image = ImageIO.read(new java.io.ByteArrayInputStream(imageData));
            } catch (Exception e) {
                e.printStackTrace();
                this.image = null;
            }
        }
    }
}
