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