/*
 * Copyright 2023 wil.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jnightride.jgui;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import java.io.IOException;

/**
 * Gestiona la resolucion de la pantalla del juego.
 */
public final 
class AppSize implements Cloneable, Savable {
    
    /**
     * Valor 'minimo' que puede tener el largo de la
     * pantalla de juego.
     */
    public static final int MIN_WIDTH  = 1024;
    
    /**
     * Valor 'minimo' que puede tener el nacho de la
     * pantalla de juego.
     */
    public static final int MIN_HEIGHT = 576;
    
    /** Largo de la pantalla. */
    private float width;
    
    /** Ancho de la pantalla. */
    private float height;

    /**
     * Constructor de la clase para uso interno.
     */
    protected AppSize() {
        this(MIN_WIDTH, MIN_HEIGHT);
    }

    /**
     * Genera un nuevo objeto <code>AppSize</code> para establecer una
     * nueva resolucion de pantalla.
     * 
     * @param width Valor del largo de la pantalla de juego.
     * @param height Valor del ancho de la pantalla.
     */
    public AppSize(float width, float height) {
        this.width  = width;
        this.height = height;
    }

    @Override
    public AppSize clone() {
        try {
            AppSize clon = (AppSize)
                        super.clone();
            clon.width  = width;
            clon.height = height;
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
    
    @Override
    public String toString() {
        return "AppSize{" + "width=" + width + ", height=" + height + '}';
    }
    
    // Getters.
    public float getWidth()  { return width; }
    public float getHeight() { return height; }
    
    // Setters.
    public void setWidth(int width) {
        if (Float.isNaN(width) 
                || Float.isInfinite(width))
            throw new IllegalArgumentException("Width=[" + width);
        
        this.width = width;
    }
    public void setHeight(int height) {
        if (Float.isNaN(height) 
                || Float.isInfinite(height))
            throw new IllegalArgumentException("Height=[" + height);
        
        this.height = height;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);        
        out.write(width, "width", MIN_WIDTH);
        out.write(height, "height", MIN_HEIGHT);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        width  = in.readInt("width", MIN_WIDTH);
        height = in.readInt("height", MIN_HEIGHT);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Float.floatToIntBits(this.width);
        hash = 59 * hash + Float.floatToIntBits(this.height);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AppSize other = (AppSize) obj;
        if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width)) {
            return false;
        }
        return Float.floatToIntBits(this.height) == Float.floatToIntBits(other.height);
    }
}
