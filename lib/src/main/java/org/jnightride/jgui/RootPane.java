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

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.core.GuiLayout;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnightride.jgui.core.DynamicLayout;

/**
 * Un objeto de la clase <code>RootPane</code> se utiliza como contenero
 * padre o raiz para todos los componentes de la interfaz grafcia de usuario
 * si estos son redimensionables.
 * 
 * @author wil
 * @version 1.0.1
 * 
 * @since 1.0.0
 */
public class RootPane extends Container implements Scalable<RootPane> {

    /** Logger de la clase. */
    private static final Logger LOG = Logger.getLogger(RootPane.class.getName());
    
    /**
     * Aplicacion principal del juego <code>jme3</code>.
     */
    private final Application app;

    /**
     * Genere un nuevo contenero raiz <code>RootPane</code>
     * @param app (non-JavaDoc).
     */
    public RootPane(Application app) {
        this.app = app;
        
        // Configuramo nuetro contenedor raiz.
        RootPane.this.setBackground(null);
        RootPane.this.setLayout(new DynamicLayout(this));
    }
    
    // [ Setters ] :Establece un nuevo diseño para los componetes
    //              hijos de esta ventana.
    @Override
    public void setLayout(GuiLayout layout) {
        if (layout != null 
                && !(layout instanceof DynamicLayout)) {
            RootPane.LOG.log(Level.WARNING, " [ Layout ] :It is recommended to use FreeLayout as the layout instead of {0}.", layout.getClass());
        }        
        super.setLayout(layout);
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public float getWidth() {
        return this.getPreferredSize().x;
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public float getHeight() {
        return this.getPreferredSize().y;
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public AppSize getAppSize() {
        if (app == null) {
            return new AppSize();
        }
        final AppSettings as = app.getContext().getSettings();
        return new AppSize(as.getWidth(), as.getHeight());
    }

    /**
     * (non-JavaDoc)
     */
    @Override
    public void restart() {
        GuiLayout layout = getLayout();
        if ((layout instanceof DynamicLayout<?>)) {
            ((DynamicLayout<?>) layout).restart();
        }
    }
}
