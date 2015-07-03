/*
 * Copyright (c) 2015 Elisabet Romero <eliromeva at gmail.com>. 
 * 
 * This file is part of TranscriptorPA.
 * 
 * TranscriptorPA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TranscriptorPA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TranscriptorPA.  If not, see <http ://www.gnu.org/licenses/>.
 */

package ElementForDTD;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contenedor que almacena una lista de DTDChilds e información sobre su cantidad.
 */
public class DTDOr extends DTDChild {
    private ArrayList<DTDChild> childs;
    
    public DTDOr(DTDChild[] childs, String amount) {
        super(null, amount);
        if(childs != null) this.childs.addAll(Arrays.asList(childs));
        else this.childs = new ArrayList<>();
    }

    /**
     * @return Lista de hijos del elemento.
     */
    public ArrayList<DTDChild> getChilds() {
        return childs;
    }

    /**
     * Añade un hijo a la lista existente.
     * @param child Hijo a añadir.
     */
    public void addChild(DTDChild child) {
        childs.add(child);
    }
    
    @Override
    public String toString() {
        // Cuidado con los elementos recursivos!!!!
        String ret = "OR. Cantidad: " + getAmount() + "\n";
        if(childs.isEmpty()) ret = ret + "NINGUNO\n";
        else for(DTDChild child : childs) {
            if(child instanceof DTDOr) ret = ret.concat((DTDOr)child + "\n");
            else ret = ret.concat(child.toString());
        }
        return ret.concat("\nFIN OR");
    }
}
