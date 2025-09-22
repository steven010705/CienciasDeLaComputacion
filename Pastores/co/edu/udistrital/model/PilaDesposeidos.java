package co.edu.udistrital.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack; // Se usa la clase Stack de Java

public class PilaDesposeidos {
    Stack<Pastor> pila; // La pila de pastores desposeídos

    public PilaDesposeidos() {
        pila = new Stack<>();
    }

    // Agrega un pastor a la cima de la pila
    public void push(Pastor pastor) {
        pila.push(pastor);
    }

    // Remueve y devuelve el pastor de la cima de la pila
    public Pastor pop() {
        if (!pila.isEmpty()) {
            return pila.pop();
        }
        return null; // Retorna null si la pila está vacía
    }

    // Nuevo método para rescatar solo pastores de la misma religión
    public Pastor popPastorDeMismaReligion(String religionBuscada) {
        Stack<Pastor> pilaTemporal = new Stack<>(); // Pila auxiliar para mantener el orden
        Pastor pastorEncontrado = null;

        // Recorre la pila original
        while (!pila.isEmpty()) {
            Pastor pastorActual = pila.pop();
            if (pastorActual.getReligion().equals(religionBuscada)) {
                pastorEncontrado = pastorActual;
                break; // Se encontró el pastor, se sale del bucle
            } else {
                pilaTemporal.push(pastorActual); // Si no coincide, se guarda temporalmente
            }
        }

        // Regresa los pastores de la pila temporal a la pila original
        while (!pilaTemporal.isEmpty()) {
            pila.push(pilaTemporal.pop());
        }

        return pastorEncontrado; // Devuelve el pastor encontrado o null
    }

    // Devuelve el pastor de la cima de la pila sin removerlo
    public Pastor peek() {
        if (!pila.isEmpty()) {
            return pila.peek();
        }
        return null; // Retorna null si la pila está vacía
    }

    // Verifica si la pila está vacía
    public boolean estaVacia() {
        return pila.isEmpty();
    }

    // Convierte la pila a una lista para visualización (el último en entrar es el primero en la lista)
    public List<Pastor> toList() {
        List<Pastor> lista = new ArrayList<>(pila);
        Collections.reverse(lista); // Invierte la lista para que el "tope" de la pila esté al principio visualmente
        return lista;
    }
}