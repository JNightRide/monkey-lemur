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

import com.simsilica.lemur.Panel;

/**
 * Si queremos que nuestros componentes sean redimensionables a travez del
 * diseño dinamico, se tiene que implementar la interfaz <code>Scalable</code>.
 * 
 * @author wil
 * @version 1.0.0
 * 
 * @since 1.0.0
 * @param <E> (non-JavaDoc)
 */
public interface Scalable<E extends Panel> {
    
    /**
     * Metodo encargado de determinar el factor de escala, dicho facotor
     * es la razon entre la resolucion de pantalla y el tamaño del contenedor
     * raiz o padre.
     * 
     * @return Factor de escala del largo para los componentes.
     */
    default float getScaleFactorWidth() {
        return getAppSize().getWidth() / getWidth(); 
    }
    
    /**
     * Metodo encargado de determinar el factor de escala, dicho facotor
     * es la razon entre la resolucion de pantalla y el tamaño del contenedor
     * raiz o padre.
     * 
     * @return Factor de escala del ancho para los componentes.
     */
    default float getScaleFactorHeight() {
        return getAppSize().getHeight() / getHeight();
    }
    
    /**
     * Largo del componente padre.
     * @return Un float como valor.
     */
    public float getWidth();
    
    /**
     * Ancho del componente padre.
     * @return Un float como valor.
     */
    public float getHeight();
    
    /**
     * Devuelve un objeto con informacion de la resolucion de la pantalla, es
     * decir las dimensiones de la ventana en donde se muestra.
     * @return Un AppSize como valor.
     */
    public AppSize getAppSize();
    
    /**
     * Metodo encargado de reiniciar el escalado de todos los componentes que
     * alverga el contenedor padre que implemente esta interfza.
     */
    public void restart();    
}
