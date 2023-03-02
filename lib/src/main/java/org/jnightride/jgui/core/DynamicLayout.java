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

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.AbstractGuiComponent;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.GuiLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import org.jnightride.jgui.Scalable;

/**
 * Clase encargado de gestionar un layout dinamico.
 * @param <E> (non-JavaDoc).
 */
public class DynamicLayout<E extends Panel> extends AbstractGuiComponent 
                                implements GuiLayout, Cloneable {

    /**
     * Mapa o diccionario donde agregaremos los diferentes nodos
     * hijos del componente padre.
     */
    private final Map<Node, DockControl> children = new IdentityHashMap<>();
    
    /**
     * Ventana principal donde se vizualizan todos los componentes
     * de la intrefaz de usuario <code>GUI</code>.
     */
    private final Scalable<E> rootPane;
    
    /*
        Constructor de la clase.
    */
    public DynamicLayout(@Nonnull Scalable<E> scalable) {
        this.rootPane = scalable;
    }
        
    @Override
    public void calculatePreferredSize(Vector3f size) {
        DockControl control = getNode().getControl(DockControl.class);
        if (control == null) {
            size.set(new Vector3f(1.0F, 1.0F, 0.0F));
        } else {
            Vector3f myPref = control.getDockStyle().getSize();
            size.set(myPref);
        }
    }
    
    public void restart() {
        for (Map.Entry<Node, DockControl> entry : this.children.entrySet()) {
            DockControl dc = entry.getValue();
            if (dc == null)
                continue;
            
            dc.updateGui();
            
            final Node node = entry.getKey();
            if (node instanceof Container) {
                GuiLayout layout = ((Container) node).getLayout();
                if (layout instanceof DynamicLayout) {
                    ((DynamicLayout) layout).restart();
                }
            }
        }
    }

    @Override
    public void reshape(Vector3f pos, Vector3f size) { 
        for (final Map.Entry<?, ?> entry : this.children.entrySet()) {
            if (entry.getValue() == null)
                continue;
            
            DockControl control = (DockControl) entry.getValue();
            GuiControl guiControl  = ((Node) entry.getKey()).getControl(GuiControl.class);
            
            control.getDockStyle().setSize(guiControl.getPreferredSize().clone());
            control.updateGui();
        }
    }

    @Override
    public <T extends Node> T addChild(T t, Object... constraints) {
        if(t != null && t.getControl(GuiControl.class) == null)
            throw new IllegalArgumentException( "Child is not GUI element." );
        
        if (t == null)
            return null;
        
        if (children.containsKey(t))
            removeChild(t);
        
        DockStyle args = null;
        DockControl newFreeControl;
        
        if (t.getControl(DockControl.class) != null)  {
            newFreeControl = t.getControl(DockControl.class);
            args = newFreeControl.getDockStyle();
        } else {
            newFreeControl = new DockControl(rootPane, new DockStyle(constraints));
        }
        
        t.addControl(newFreeControl);
        children.put(t, newFreeControl);
        
        if (getGuiControl() != null) {
            getGuiControl().getNode().attachChild(t);
            newFreeControl.attach();
            
            if ( args != null ) {
                DockStyle newConstraints = new DockStyle(constraints);
                if ( !args.equals(newConstraints) ) {
                    newFreeControl.setDockStyle(newConstraints);
                }
            }
        }
        
        invalidate();
        return t;
    }

    @Override
    public void detach(GuiControl parent) {
        super.detach(parent);
        for (final Node child : getChildren()) {
            if (child == null)
                continue;            
            child.removeFromParent();
        }
    }

    @Override
    public void attach(GuiControl parent) {
        super.attach(parent);
        for (final Node child : getChildren()) {
            if (child == null)
                continue;
            getNode().attachChild(child);
            child.getControl(DockControl.class).attach();
        }
    }

    @Override
    public void removeChild(Node n) {
        if (children.remove(n) == null)
            return;
        
        /*n.removeControl(DynamicControl.class);*/
        n.removeFromParent();
        invalidate();
    }

    @Override
    public Collection<Node> getChildren() {
        return Collections.unmodifiableSet(this.children.keySet());
    }

    @Override
    public void clearChildren() {
        if (this.children == null)
            return;
        
        for (final Map.Entry<?, ?> entry : this.children.entrySet()) {
            if (entry.getKey() == null)
                continue;
            
            ((Node) entry.getKey()).removeFromParent();
            /*((Node) entry.getKey()).removeControl(DynamicControl.class);*/
        }
        this.children.clear();
    }

    @Override
    public GuiLayout clone() {
        return new DynamicLayout(rootPane);
    }
}
