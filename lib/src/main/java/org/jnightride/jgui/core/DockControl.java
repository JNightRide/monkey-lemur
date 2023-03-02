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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.core.AbstractNodeControl;
import com.simsilica.lemur.core.GuiControl;

import javax.annotation.Nonnull;
import org.jnightride.jgui.Scalable;

/**
 * Un <code>DockControl</code> es la clase encargado de controlar el escalado
 * de los componentes hijos. Es muy importantes utilizar esta clase para 
 * establecer la posicion y de manera opcional las dimensiones del componente.
 * 
 * @author wil
 * @version 1.0.01
 * 
 * @since 1.0.0
 */
@SuppressWarnings(value = {"unchecked"})
public class DockControl extends AbstractNodeControl<DockControl> {

    /*
        Atributos de la clase control.
    */
    private final Scalable<? extends Panel> scalableGui;
    private DockStyle dockStyle;
    private DockControl parent;    

    /*
        Constructor de la clase.
    */
    public DockControl(final Scalable<? extends Panel> scalableGui,
                        @Nonnull final DockStyle dockStyle) {
        this.scalableGui = scalableGui;        
        this.dockStyle = dockStyle;
    }
    
    /**
     * Metodo encargado de establecer la visibilidad del componete
     * grafico que se ete gestionando sin eliminarlo del nodo padre.
     * 
     * @param visible {@code true} si se desea que el componente sea visible,
     *                  de lo contrario {@code false} para que no se visualize.
     */
    public void setVisible(boolean visible) {
        if (visible && 
                spatial.getCullHint().equals(Spatial.CullHint.Always)) {
            spatial.setCullHint(Spatial.CullHint.Never);
        } else if (!visible 
                    && spatial.getCullHint().equals(Spatial.CullHint.Never)) {
            spatial.setCullHint(Spatial.CullHint.Always);
        }
    }
    
    /**
     * Determina la visibilidad del componente graficos.
     * @return Un valor booleando.
     */
    public boolean isVisible() {
        return spatial.getCullHint()
                      .equals(Spatial.CullHint.Never);
    }
    
    /**
     * Devuelve el componente grafico de la unterfaz del usuario.
     * @param <E> Tipo de dato a retornar.
     * @return Componnete de la interfaz grafica.
     */
    public <E extends Panel> E getGui() {
        return (E) getSpatial();
    }

    /**
     * Devuelve el contenedor raiz principal de la pantalla.
     * @return Contenedor principal de la interfaz grafica.
     */
    public Scalable<? extends Panel> getRootPane() {
        return scalableGui;
    }

    /**
     * Devuelve el padre del componente grafico.
     * @return Padre del componente, de lo contrario
     * <code>null</code>.
     */
    public DockControl getParent() {
        return parent;
    }

    /**
     * Devuelve los datos de estilo del componente.
     * @return Datos de estilo.
     */
    public DockStyle getDockStyle() {
        return dockStyle;
    }
    
    /**
     * Determina si el control tiene un padre.
     * @return {@code true} si el control tiene un padre, de lo
     *          contrario {@code false}.
     */
    public boolean hasParent() {
        return parent != null;
    }
    
    /**
     * Devuelve el largo del componente.
     * @return Dimension del GUI.
     */
    public float getWidth() {
        return spatial.getControl(GuiControl.class).getSize().x;
    }
    
    /**
     * Devuelve el ancho del componente.
     * @return Dimension del GUI.
     */
    public float getHeight() {
        return spatial.getControl(GuiControl.class).getSize().y;
    }
    
    protected float getParentWidth() {
        if (hasParent()) {
            return parent.getWidth();
        } else {
            return scalableGui.getWidth() * scalableGui.getScaleFactorWidth();
        }
    }
    protected float getParentHeight() {
        if (hasParent()) {
            return parent.getHeight();
        } else {
            return scalableGui.getHeight() * scalableGui.getScaleFactorHeight();
        }
    }
    
    /**
     * Metodo encargado de actuliza las dimensiones y posiciones del
     * componente de la interfaz de usuario.
     */
    public void updateGui() {
        Vector3f mySize = new Vector3f();        
        // Obtenemos el control del componente.
        GuiControl control = spatial.getControl(GuiControl.class);
        if (scalableGui != null) {
            Vector3f prefSize = dockStyle.getSize();            
            mySize.setX(isLockScaling() 
                                ? prefSize.getX() * scalableGui.getScaleFactorHeight()
                                : prefSize.getX() * scalableGui.getScaleFactorWidth());
                
            mySize.setY(prefSize.getY() * scalableGui.getScaleFactorHeight());
            mySize.setZ(prefSize.getZ());
        }
        // establecemos las nuevas caracteristicas
        // sobre el control del componente.
        control.setSize(mySize);
        
        // centramos el nodo.
        if (hasParent()) {
            control.getNode().setLocalTranslation((getParentWidth()/ 2.0f),
                                                 -(getParentHeight() / 2.0f), dockStyle.getLocation().z);
        } else {
            control.getNode().setLocalTranslation((scalableGui.getWidth() / 2f) * scalableGui.getScaleFactorWidth(),
                                                  (scalableGui.getHeight() / 2f) * scalableGui.getScaleFactorHeight(), dockStyle.getLocation().z);
        }

        // Calculamos la nueva posicion segun el diseño.
        control.getNode().move(calculatePosition(control));
        
        // Centramos el componete en la nueva posicion.
         control.getNode().move(-mySize.x * 0.5F, mySize.y * 0.5F, 0.0F);
    }

    /**
     * Metodo encargado de escalar la posicion del componente segun el diseño
     * establecido de ello.
     * 
     * @param control
     *          Control de componente GUI.
     * @return Posicione 3D para el componente.
     */
    private Vector3f calculatePosition(GuiControl control) {
        if (scalableGui == null)
            return new Vector3f(0.0F, 0.0F, 0.0F);
                
        float width  = control.getSize().x,
              height = control.getSize().y;
        
        Vector3f myPos = dockStyle.getLocation();
        float offsetX = myPos.getX();
        float offsetY = myPos.getY();
        
        float xPos, yPos, zPos = myPos.getZ();
        switch (dockStyle.getDock()) {
            case Center:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = (offsetX * scalableGui.getScaleFactorHeight());
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = (offsetX * scalableGui.getScaleFactorWidth());
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case CenterBottom:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = offsetX * scalableGui.getScaleFactorHeight();
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = offsetX * scalableGui.getScaleFactorWidth();
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case CenterTop:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = offsetX * scalableGui.getScaleFactorHeight();
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = offsetX * scalableGui.getScaleFactorWidth();
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case LeftBottom:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorHeight();
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorWidth();
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case LeftCenter:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorHeight();
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorWidth();
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case LeftTop:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorHeight();
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = -(getParentWidth() * 0.5f) + (width * 0.5f) + offsetX * scalableGui.getScaleFactorWidth();
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case RightBottom:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorHeight());
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorWidth());
                    yPos = -(getParentHeight() * 0.5f) + (height * 0.5f) + (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case RightCenter:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorHeight());
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorWidth());
                    yPos = (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            case RightTop:
                if (hasParent() && getParent().isLockScaling()) {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorHeight());
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                } else {
                    xPos = (getParentWidth() * 0.5f) - (width * 0.5f) - (offsetX * scalableGui.getScaleFactorWidth());
                    yPos = (getParentHeight() * 0.5f) - (height * 0.5f) - (offsetY * scalableGui.getScaleFactorHeight());
                }
                return new Vector3f(xPos, yPos, zPos);
            default:
                throw new AssertionError();
        }
    }
    
    /**
     * Devuelve el estado de escalado.
     * @return Un valor bollean.
     */
    public boolean isLockScaling() {
        return this.dockStyle.isLockscaling();
    }
    
    /**
     * Establece una nueva posicione.
     * @param x posicion en {@code x}.
     * @param y posicion en {@code y}.
     */
    public void setPosition(float x, float y) {
        this.dockStyle.setLocation(x, y);
        this.updateGui();
    }
    
    /**
     * Establce la profundidad del objeto.
     * @param z profundidad.
     */
    public void setDepthPosition(float z) {
        Vector3f old = this.dockStyle.getLocation();
        this.dockStyle.setLocation(new Vector3f(old.getX(), old.getY(), z));
        this.updateGui();
    }

    /**
     * Establece un nuevo diseño de la posicion.
     * @param dock Nuevo diseño.
     */
    public void setDock(Dock dock) {
        this.dockStyle.setDock(dock == null 
                                ? Dock.Center : dock);
        this.updateGui();
    }

    public void setDockStyle(@Nonnull DockStyle dockStyle) {
        this.dockStyle = dockStyle;
        this.updateGui();
    }
    
    public Vector3f getPosition() {
        return spatial.getLocalTranslation();
    }
    public Vector3f getScreenPosition() {
        return new Vector3f(getPosition().x + (scalableGui.getWidth() * 0.5f * scalableGui.getScaleFactorWidth()),
                            getPosition().y + (scalableGui.getHeight() * 0.5f * scalableGui.getScaleFactorHeight()), getPosition().z);
    }
    
    /**
     * Si el componente administrado es una extencia de {@code Label}, de ser
     * asi puedes establecer untamaña escalable para la fuente.     * 
     * @param size tamaño de la funete.
     */
    public void setFontSize(float size) {
        Panel gui = getGui();
        if (gui instanceof Label) {
            ((Label) gui).setFontSize(size * scalableGui.getScaleFactorHeight());
        }
    }
    
    @Override
    protected void attach() {
        if (spatial != null && spatial.getControl(GuiControl.class) == null)
            throw new IllegalArgumentException("Child is not GUI element.");
        
        Node parentNode = spatial.getParent();
        if (parentNode != null
                && parentNode.getControl(DockControl.class) != null) {
            parent = parentNode.getControl(DockControl.class);
        }
    }
    @Override
    protected void detach() { 
        parent = null;
    }
}
