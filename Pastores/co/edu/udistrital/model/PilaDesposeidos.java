package co.edu.udistrital.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PilaDesposeidos {
    Stack<Pastor> pila;

    public PilaDesposeidos() {
        pila = new Stack<>();
    }

    public void push(Pastor pastor) {
        pila.push(pastor);
    }

    public Pastor pop() {
        if (!pila.isEmpty()) {
            return pila.pop();
        }
        return null;
    }

    // Nuevo método para rescatar solo pastores de la misma religión
    public Pastor popPastorDeMismaReligion(String religionBuscada) {
        // Se crea una pila temporal para mantener el orden de los pastores no rescatados
        Stack<Pastor> pilaTemporal = new Stack<>();
        Pastor pastorEncontrado = null;

        while (!pila.isEmpty()) {
            Pastor pastorActual = pila.pop();
            if (pastorActual.getReligion().equals(religionBuscada)) {
                pastorEncontrado = pastorActual;
                break; // Se encontró el pastor, se sale del bucle
            } else {
                pilaTemporal.push(pastorActual); // Se guarda en la pila temporal si no coincide la religión
            }
        }

        // Se regresan los pastores de la pila temporal a la pila original
        while (!pilaTemporal.isEmpty()) {
            pila.push(pilaTemporal.pop());
        }

        return pastorEncontrado;
    }

    public Pastor peek() {
        if (!pila.isEmpty()) {
            return pila.peek();
        }
        return null;
    }

    public boolean estaVacia() {
        return pila.isEmpty();
    }

    public List<Pastor> toList() {
        List<Pastor> lista = new ArrayList<>(pila);
        Collections.reverse(lista);
        return lista;
    }
}