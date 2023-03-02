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
package org.jnightride.jgui.core;

import com.jme3.math.Vector3f;

import javax.annotation.Nonnull;

/**
 * Un <code>DockStyle</code> es el encargado de gestionar los parametros
 * que se pasan por parametro al agregar un nuevo nodo hijo al nodo
 * padre con el layout <code>DynamicLayout</code>.
 * 
 * @author wil
 * @version 1.0.0
 * 
 * @since 1.0.0
 */
public final 
class DockStyle implements Cloneable {
    
    /**
     * Objeto {@code Boolean} encargado de determinar el tipo de escalado
     * que se aplicara al nodo hijo.
     */
    private Boolean lockscaling;
    
    /**
     * Encargado de determinar la referencia en donde se calculara su posicion
     * en el espacio de la GUI.
     */
    private Dock dock;

    /**
     * Encargado de almacenar la posicion {@code original} del
     * 'Gui', es utilizada para determinar la escala de de las posiciones
     * del los componentes en la pantalla.
     */
    private Vector3f pos = new Vector3f(0.0F, 0.0F, 1.0F);
    
    /**
     * Se encarga de almacenar las dimensiones {@code original} del
     * 'Gui', estos datos se utilizan para escalar/redimensionar un
     * componente en pantalla.
     */
    private Vector3f size = new Vector3f(0.0F, 0.0F, 0.0F);
    
    /**
     * Constructor predeterminado de la clase <code>DockStyle</code> en donde
     * se puede pasar los {@code Object... constraints} del nodo hijo.     * 
     * @param constraints Lista de parametros.
     */
    public DockStyle(@Nonnull Object... constraints) {
        for (final Object element : constraints) {
            if (element == null)
                continue;
            
            if ((element instanceof Boolean) 
                    && (this.lockscaling == null)) {
                this.lockscaling = (Boolean) element;
            } else if ((element instanceof Dock) 
                            && (this.dock == null)) {
                this.dock = (Dock) element;
            }
            
            if (this.dock != null 
                    && this.lockscaling != null)
                break;
        }
        
        if (this.lockscaling == null)
            this.lockscaling = Boolean.FALSE;
            
        if (this.dock == null)
            this.dock = Dock.Center;
    }
    
    /**
     * Metodo encargado de clonar {@code DockStyle}.
     * @return Clon de este objeto.
     */
    @Override
    public DockStyle clone() {
        try {
            DockStyle clon = (DockStyle) 
                            super.clone();
            clon.lockscaling = lockscaling;
            clon.dock = dock;
            clon.pos  = pos.clone();
            clon.size = size.clone();
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
    
    /**
     * Establece nuevas dimensiones para esta {@code Rect}. Solo
     * se puede redimensionar en 2 ejes.
     * 
     * @param with
     *          Largo de la recta.
     * @param height 
     *          Ancho de la recta.
     */
    public void setSize(float with, float height) {
        this.setSize(new Vector3f(with, height, this.size.getZ()));
    }
    
    /**
     * Metodo encargado de establecer una nueva dimension. Si el parametro
     * es <code>null</code>, las dimensiones seran <code>0</code>.
     * @param size Vector 3D para la nueva dimension.
     */
    public void setSize(Vector3f size) {
        if (size == null) {
            this.size.zero();
        } else {
            this.size = size;
        }
    }
    
    /**
     * Establece una nueva posicion para la recta en 2D.
     * @param x nueva posicion en {@code x}.
     * @param y nueva posicion en {@code y}.
     */
    public void setLocation(float x, float y) {
        this.setLocation(new Vector3f(x, y, this.pos.getZ()));
    }
    
    /**
     * Metodo encargado de establecer una nueva posiciones. Si la posiciones
     * es <code>null</code>, las tres posiciones {@code x, y, z} seran <code>0</code>.
     * @param pos Vector 3D para la nueva posicion.
     */
    public void setLocation(Vector3f pos) {
        if (pos == null) {
            this.pos.zero();
        } else {
            this.pos = pos;
        }
    }

    /*
        Otros Setters.
    */
    public void setLockscaling(@Nonnull Boolean lockscaling) {
        this.lockscaling = lockscaling;
    }
    public void setDock(@Nonnull Dock dock) {
        this.dock = dock;
    }
    
    /**
     * Devuelve un clon de las dimensiones de  esta {@code Rect}.
     * @return Dimension de la recta.
     */
    public Vector3f getSize() {
        return this.size.clone();
    }
    
    /**
     * Devuelve un clon de las posiciones de esta {@code Rect}.
     * @return Posicicion de la recta.
     */
    public Vector3f getLocation() {
        return this.pos.clone();
    }

    /*
        Otros Getters.
    */
    @Nonnull
    public Boolean isLockscaling() {
        return lockscaling;
    }
    @Nonnull
    public Dock getDock() {
        return dock;
    }
}
