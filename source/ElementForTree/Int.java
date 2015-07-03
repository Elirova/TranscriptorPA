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

package ElementForTree;

/**
 * Entero mutable.
 */
public class Int {
    int number;

    public Int(int n) {
        number = n;
    }

    /**
     * @return Entero.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Establece el valor del entero.
     * @param number Nuevo valor del entero.
     */
    public void setNumber(int number) {
        this.number = number;
    }
    
    /**
     * Suma un entero al existente.
     * @param number Entero a añadir.
     */
    public void addNumber(int number) {
        this.number += number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
