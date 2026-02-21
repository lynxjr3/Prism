/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.catsoftware.engine.prism.draw;

import com.catsoftware.engine.prism.shapes.ShapeType;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.Serializable;

/**
 * A vector shape object that can be drawn on the canvas.
 * Supports multiple shape types, fills, gradients, rotation, and opacity.
 *
 * @author lynxjr
 * @version 1.0.00
 * @since Prism B1
 */
public class ShapeObject implements DrawableObject, Serializable {
    private ShapeType type;
    private Rectangle bounds;
    private Color color;
    private int strokeSize;
    private boolean filled = false;
    private Color fillColor;
    private float alpha = 1.0f;
    private double rotation = 0;
    private boolean useGradient = false;
    public Color gradientColor1 = Color.WHITE;
    public Color gradientColor2 = Color.BLACK;
    public boolean gradientHorizontal = true;

    /**
     * Creates a new shape object.
     *
     * @param t The shape type
     * @param b The bounding rectangle
     * @param c The stroke color
     * @param s The stroke size
     * @param a The opacity level (0.0 - 1.0)
     */
    public ShapeObject(ShapeType t, Rectangle b, Color c, int s, float a) {
        type = t;
        bounds = new Rectangle(b);
        color = c;
        strokeSize = s;
        fillColor = c;
        alpha = a;
    }

    @Override
    public void draw(Graphics2D g) {
        // Save original composite
        Composite originalComposite = g.getComposite();
        
        // Apply alpha if needed
        if (alpha < 1.0f) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        
        // Save transform for rotation
        AffineTransform original = g.getTransform();
        Rectangle bounds = getBounds();
        g.rotate(Math.toRadians(rotation), bounds.getCenterX(), bounds.getCenterY());
        
        // Draw fill
        if (filled) {
            if (useGradient) {
                Paint originalPaint = g.getPaint();
                GradientPaint gradient;
                if (gradientHorizontal) {
                    gradient = new GradientPaint(
                        bounds.x, bounds.y, gradientColor1,
                        bounds.x + bounds.width, bounds.y, gradientColor2
                    );
                } else {
                    gradient = new GradientPaint(
                        bounds.x, bounds.y, gradientColor1,
                        bounds.x, bounds.y + bounds.height, gradientColor2
                    );
                }
                g.setPaint(gradient);
                drawShape(g, true);
                g.setPaint(originalPaint);
            } else {
                g.setColor(fillColor);
                drawShape(g, true);
            }
        }
        
        // Draw stroke only if strokeSize > 0
        if (strokeSize > 0) {
            g.setColor(color);
            g.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            drawShape(g, false);
        }
        
        // Restore transform and composite
        g.setTransform(original);
        g.setComposite(originalComposite);
    }

    /**
     * Draws the actual shape (fill or outline).
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     */
    private void drawShape(Graphics2D g, boolean fill) {
        switch (type) {
            case SQUARE:
                if (fill) g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                else g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                break;
                
            case CIRCLE:
                if (fill) g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
                else g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
                break;
                
            case LINE:
                g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
                break;
                
            case TRIANGLE:
                int[] triX = {
                    bounds.x + bounds.width / 2,
                    bounds.x,
                    bounds.x + bounds.width
                };
                int[] triY = {
                    bounds.y,
                    bounds.y + bounds.height,
                    bounds.y + bounds.height
                };
                if (fill) g.fillPolygon(triX, triY, 3);
                else g.drawPolygon(triX, triY, 3);
                break;
                
            case HEART:
                drawHeart(g, fill);
                break;
                
            case STAR:
                drawStar(g, fill, 5);
                break;
                
            case ARROW:
                drawArrow(g, fill, bounds.x, bounds.y, bounds.width, bounds.height);
                break;
                
            case PENTAGON:
                drawRegularPolygon(g, fill, 5);
                break;
                
            case HEXAGON:
                drawRegularPolygon(g, fill, 6);
                break;
                
            case OCTAGON:
                drawRegularPolygon(g, fill, 8);
                break;
                
            case CROSS:
                drawCross(g, fill);
                break;
                
            case DIAMOND:
                drawDiamond(g, fill);
                break;
        }
    }

    /**
     * Draws a heart shape using parametric equations.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     */
    private void drawHeart(Graphics2D g, boolean fill) {
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;
        
        int centerX = x + w/2;
        int centerY = y + h/2;
        int size = Math.min(w, h) / 2;
        
        int points = 100;
        int[] xPoints = new int[points];
        int[] yPoints = new int[points];
        
        double scale = size / 18.0;
        
        for (int i = 0; i < points; i++) {
            double t = 2 * Math.PI * i / points;
            
            double xt = 16 * Math.pow(Math.sin(t), 3);
            double yt = 13 * Math.cos(t) - 5 * Math.cos(2*t) - 2 * Math.cos(3*t) - Math.cos(4*t);
            
            xPoints[i] = centerX + (int)(xt * scale);
            yPoints[i] = centerY - (int)(yt * scale);
        }
        
        if (fill) g.fillPolygon(xPoints, yPoints, points);
        else g.drawPolygon(xPoints, yPoints, points);
    }

    /**
     * Draws a star shape.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     * @param points Number of points (5 for classic star)
     */
    private void drawStar(Graphics2D g, boolean fill, int points) {
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        int outerRadius = Math.min(bounds.width, bounds.height) / 2;
        int innerRadius = outerRadius / 2;
        
        double angle = Math.PI / 2;
        double step = Math.PI / points;
        
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        
        for (int i = 0; i < points * 2; i++) {
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int)(radius * Math.cos(angle));
            yPoints[i] = centerY - (int)(radius * Math.sin(angle));
            angle += step;
        }
        
        if (fill) g.fillPolygon(xPoints, yPoints, points * 2);
        else g.drawPolygon(xPoints, yPoints, points * 2);
    }

    /**
     * Draws an arrow shape.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     * @param x Top-left X coordinate
     * @param y Top-left Y coordinate
     * @param w Width
     * @param h Height
     */
    private void drawArrow(Graphics2D g, boolean fill, int x, int y, int w, int h) {
        boolean horizontal = w > h;
        
        if (horizontal) {
            int shaftLength = w - Math.min(40, w/3);
            int headSize = Math.min(30, w/4);
            int shaftWidth = Math.max(4, h/4);
            int arrowY = y + h/2;
            
            Path2D.Float arrow = new Path2D.Float();
            
            arrow.moveTo(x + w, arrowY);
            arrow.lineTo(x + w - headSize, arrowY - headSize/2);
            arrow.lineTo(x + shaftLength, arrowY - shaftWidth/2);
            arrow.lineTo(x, arrowY - shaftWidth/2);
            arrow.lineTo(x, arrowY + shaftWidth/2);
            arrow.lineTo(x + shaftLength, arrowY + shaftWidth/2);
            arrow.lineTo(x + w - headSize, arrowY + headSize/2);
            arrow.closePath();
            
            if (fill) g.fill(arrow);
            else g.draw(arrow);
            
        } else {
            int shaftHeight = h - Math.min(40, h/3);
            int headSize = Math.min(30, h/4);
            int shaftWidth = Math.max(4, w/4);
            int arrowX = x + w/2;
            
            Path2D.Float arrow = new Path2D.Float();
            
            arrow.moveTo(arrowX, y + h);
            arrow.lineTo(arrowX + headSize/2, y + h - headSize);
            arrow.lineTo(arrowX + shaftWidth/2, y + shaftHeight);
            arrow.lineTo(arrowX + shaftWidth/2, y);
            arrow.lineTo(arrowX - shaftWidth/2, y);
            arrow.lineTo(arrowX - shaftWidth/2, y + shaftHeight);
            arrow.lineTo(arrowX - headSize/2, y + h - headSize);
            arrow.closePath();
            
            if (fill) g.fill(arrow);
            else g.draw(arrow);
        }
    }

    /**
     * Draws a regular polygon.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     * @param sides Number of sides
     */
    private void drawRegularPolygon(Graphics2D g, boolean fill, int sides) {
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        int radius = Math.min(bounds.width, bounds.height) / 2;
        
        double angle = Math.PI / 2;
        double step = 2 * Math.PI / sides;
        
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];
        
        for (int i = 0; i < sides; i++) {
            xPoints[i] = centerX + (int)(radius * Math.cos(angle));
            yPoints[i] = centerY - (int)(radius * Math.sin(angle));
            angle += step;
        }
        
        if (fill) g.fillPolygon(xPoints, yPoints, sides);
        else g.drawPolygon(xPoints, yPoints, sides);
    }

    /**
     * Draws a cross/plus sign shape.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     */
    private void drawCross(Graphics2D g, boolean fill) {
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;
        
        int armWidth = Math.max(2, w / 4);
        int armHeight = Math.max(2, h / 4);
        int centerX = x + w/2;
        int centerY = y + h/2;
        
        if (fill) {
            g.fillRect(x, centerY - armHeight/2, w, armHeight);
            g.fillRect(centerX - armWidth/2, y, armWidth, h);
        } else {
            g.drawRect(x, centerY - armHeight/2, w, armHeight);
            g.drawRect(centerX - armWidth/2, y, armWidth, h);
        }
    }

    /**
     * Draws a diamond shape.
     *
     * @param g The graphics context
     * @param fill true for fill, false for outline
     */
    private void drawDiamond(Graphics2D g, boolean fill) {
        int[] xPoints = {
            bounds.x + bounds.width / 2,
            bounds.x + bounds.width,
            bounds.x + bounds.width / 2,
            bounds.x
        };
        int[] yPoints = {
            bounds.y,
            bounds.y + bounds.height / 2,
            bounds.y + bounds.height,
            bounds.y + bounds.height / 2
        };
        
        if (fill) g.fillPolygon(xPoints, yPoints, 4);
        else g.drawPolygon(xPoints, yPoints, 4);
    }

    @Override
    public boolean contains(Point p) {
        Rectangle hitBounds = new Rectangle(
            bounds.x - strokeSize,
            bounds.y - strokeSize,
            bounds.width + strokeSize * 2,
            bounds.height + strokeSize * 2
        );
        return hitBounds.contains(p);
    }

    @Override
    public void moveBy(int dx, int dy) {
        bounds.translate(dx, dy);
    }
    
    @Override
    public Rectangle getBounds() {
        if (rotation == 0) {
            return new Rectangle(bounds);
        }
        
        double rad = Math.toRadians(rotation);
        double cos = Math.abs(Math.cos(rad));
        double sin = Math.abs(Math.sin(rad));
        
        int newWidth = (int)(bounds.width * cos + bounds.height * sin);
        int newHeight = (int)(bounds.width * sin + bounds.height * cos);
        
        int newX = bounds.x + (bounds.width - newWidth) / 2;
        int newY = bounds.y + (bounds.height - newHeight) / 2;
        
        return new Rectangle(newX, newY, newWidth, newHeight);
    }
    
    @Override
    public void resize(double scaleX, double scaleY) {
        int newWidth = (int)(bounds.width * scaleX);
        int newHeight = (int)(bounds.height * scaleY);
        resizeAbsolute(newWidth, newHeight);
    }
    
    @Override
    public void resizeAbsolute(int newWidth, int newHeight) {
        bounds.width = Math.max(5, newWidth);
        bounds.height = Math.max(5, newHeight);
    }
    
    @Override
    public void fill(Graphics2D g, Color fillColor) {
        this.filled = true;
        this.fillColor = fillColor;
    }
    
    /**
     * Sets whether this shape is filled.
     *
     * @param filled true for filled, false for outline only
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    /**
     * Checks if this shape is filled.
     *
     * @return true if filled
     */
    public boolean isFilled() {
        return filled;
    }
    
    /**
     * Sets the fill color.
     *
     * @param color The new fill color
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
    }
    
    /**
     * Returns the fill color.
     *
     * @return The fill color
     */
    public Color getFillColor() {
        return fillColor;
    }
    
    /**
     * Returns the shape type.
     *
     * @return The shape type
     */
    public ShapeType getType() {
        return type;
    }
    
    /**
     * Sets the stroke size.
     *
     * @param size The new stroke size
     */
    public void setStrokeSize(int size) {
        this.strokeSize = size;
    }
    
    /**
     * Returns the stroke size.
     *
     * @return The stroke size
     */
    public int getStrokeSize() {
        return strokeSize;
    }
    
    /**
     * Returns the stroke color.
     *
     * @return The stroke color
     */
    public Color getColor() { return color; }

    /**
     * Sets the stroke color.
     *
     * @param color The new stroke color
     */
    public void setColor(Color color) {
        this.color = color;
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
     * Applies a gradient fill to this shape.
     *
     * @param color1 Start color
     * @param color2 End color
     * @param horizontal true for horizontal gradient, false for vertical
     */
    public void setGradient(Color color1, Color color2, boolean horizontal) {
        this.useGradient = true;
        this.gradientColor1 = color1;
        this.gradientColor2 = color2;
        this.gradientHorizontal = horizontal;
    }

    /**
     * Checks if this shape has a gradient fill.
     *
     * @return true if gradient is enabled
     */
    public boolean hasGradient() {
        return useGradient;
    }

    /**
     * Removes gradient fill from this shape.
     */
    public void removeGradient() {
        this.useGradient = false;
    }
}

