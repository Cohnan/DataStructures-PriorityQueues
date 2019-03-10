package model.data_structures;

import java.util.Iterator;

public class MaxColaPrioridad<T extends Comparable<T>> implements IColaPrioridad<T> {
	/*
	 * Atributos
	 */
	private int tamano;
	private IQueue<T> cola;

	/*
	 * Métodos
	 */
	public MaxColaPrioridad() {
		tamano = 0;
		cola = new Queue<T>();
	}
	
	public boolean esVacia() {
		return tamano == 0;
	}

	public int darNumElementos() {
		return tamano;
	}

	public void agregar(T t) {
		// Agregar los elementos a una nueva cola en orden
		boolean agregado = false;
		Queue<T> colaFinal = new Queue<T>();
		for(T elemento : cola) {
			// Agregar el nuevo elemento a la nueva cola en donde corresponda (>: lo agrega al final de los elementos con su misma prioridad)
			if (!agregado && t.compareTo(elemento) > 0) {
				colaFinal.enqueue(t); 
				agregado = true;
			}
			colaFinal.enqueue(cola.dequeue()); // Dequeue de la cola original para no ocupar mas espacio
		}
		// En  caso de no haberse agregado aun, se agrega al final
		if (!agregado) colaFinal.enqueue(t);
		
		// Reemplazar cola por la nueva cola
		this.cola = colaFinal;
		tamano +=1;
	}

	public T delMax() {
		if (tamano == 0) return null;
		tamano-=1;
		return cola.dequeue();
	}

	public T max() {
		if (tamano == 0) return null;
		return cola.iterator().next();
	}
	
	public Iterator<T> iterator() {
		return cola.iterator();
	}
}
