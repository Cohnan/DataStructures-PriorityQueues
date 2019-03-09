package model.data_structures;

import java.util.Iterator;

/**
 * Implementacion de IQueue, interfaz para una cola como estructura de datos abstracta
 * @author cohnan
 */
public class Queue<T> implements IQueue<T> {
	/*
	 * Variables
	 */
	private Nodo<T> first;	// Primer nodo en ser agregado
	private Nodo<T> ultimo; // Ultimo nodo en ser agregado
	private int size;		// Tamano de la cola

	/*
	 * Constructor
	 */
	public Queue() {
		size = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private Nodo<T> current = first;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				T dato = current.darObjeto();
				current = current.darSiguiente();
				return dato;
			}
		};
	}


	/**
	 * @return true si esta vac�a, false de lo contrario
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * @return el tamano de la cola
	 */
	public int size() {
		return size;
	}

	/**
	 * agrega un elemento a la cola
	 */
	public void enqueue(T t) {
		Nodo<T> nuevo = new Nodo<>(t);
		if(size == 0) {
			first = nuevo;
			ultimo = nuevo;
		}
		else {
			ultimo.cambiarSiguiente(nuevo);
			ultimo = nuevo;
		}
		size += 1;

	}

	/**
	 * quita un elemento de la cola
	 */
	public T dequeue() {
		if (size == 0) return null;
		
		Nodo<T> primeroEnFila = first;
		first = primeroEnFila.darSiguiente();

		size--;
		return primeroEnFila.darObjeto();
	}

}