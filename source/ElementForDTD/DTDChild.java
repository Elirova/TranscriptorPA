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

/**
 * Contenedor que almacena un DTDItem e información sobre su cantidad.
 */
public class DTDChild {
    public static enum Amount { UNO, MAS, ASTERISCO, INTERROGACION};
    private final DTDItem element;
    private Amount amount;
    
    public DTDChild(DTDItem element, String amount) {
        this.element = element;
        setAmount(amount);
    }
    
    public DTDChild(DTDItem element, Amount amount) {
        this.element = element;
        this.amount = amount;
    }
    
    /**
     * @return DTDItem almacenado.
     */
    public DTDItem getElement() {
        return element;
    }

    /**
     * @return Cantidad posible del DTDItem asociado.
     */
    public Amount getAmount() {
        return amount;
    }
    
    /**
     * Indica la cantidad que tendrá el DTDItem asociado.
     * @param amount Cadena que contiene la información a asignar.
     */
    public void setAmount(String amount) {
        switch(amount) {
            case "+":
                this.amount = Amount.MAS;
                break;
            case "*":
                this.amount = Amount.ASTERISCO;
                break;
            case "?":
                this.amount = Amount.INTERROGACION;
                break;
            default:
                this.amount = Amount.UNO;
        }
    }
    
    @Override
    public String toString() {
        String ret = "Cantidad: " + amount + " ";
        if(element == null) return ret + "No tiene elemento";
        return ret + element.toString();
    }
}
