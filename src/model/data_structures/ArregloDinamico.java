package model.data_structures;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ArregloDinamico<T> implements IArregloDinamico<T> {
	
	/*
	 * Atributos
	 */
	/**
	 * Capacidad maxima del arreglo
	 */
	private int tamanoMax;
	/**
	 * Numero de elementos en el arreglo (de forma compacta desde la posicion 0)
	 */
	private int tamanoAct;
    /**
     * Arreglo de elementos de tamaNo maximo
     */
    private T[] elementos;
    
    /*
     * Constructores
     */
    /**
     * Construir un arreglo con la capacidad maxima inicial.
     * @param max Capacidad maxima inicial
     */
    public ArregloDinamico( int max )
	{
    	elementos = (T[]) new Object[max];
        tamanoMax = max;
        tamanoAct = 0;
    }
	
	/**
      * Construir un arreglo con una capacidad maxima temporal de 2
      */
	public ArregloDinamico() {
		elementos = (T[]) new Object[2];
        tamanoMax = 2;
        tamanoAct = 0;
    }
    
	/*
	 * Metodos
	 */
	public void agregar( T dato ) {
		//if (dato == null) throw new IllegalArgumentException("No se puede agregar un elemento nulo al arreglo");
		// caso de arreglo lleno (aumentar tamaNo)
        if ( tamanoAct == tamanoMax ) {
        	tamanoMax = 2 * tamanoMax;
            T[] copia = elementos;
            elementos = (T[]) new Object[tamanoMax];
            for ( int i = 0; i < tamanoAct; i++) {
            	elementos[i] = copia[i];
            }       
        }
           
        elementos[tamanoAct] = dato;
        tamanoAct++;
    }
	
	public void cambiarEnPos(int i, T dato) {
		if (i < 0 || tamanoAct <= i ) throw new IllegalArgumentException("No existe tal posicion en el arreglo.");
		elementos[i] = dato;
	}
	
	public int darTamano() {
		return tamanoAct;
	}
	
	public T darObjeto(int i) {
		if (i >= tamanoAct) return null;
		return elementos[i];
	}
	
	public T eliminarEnPos(int n) {
		if (n < 0 || n >= tamanoAct) return null; //throw new IllegalArgumentException("No existe tal posicion en el arreglo.");
		T dato = elementos[n];
		
		// Contraer datos
		for (int k = n + 1; k < tamanoAct; k++) {
			elementos[k-1] = elementos[k];
		} elementos[tamanoAct - 1] = null;
		
		// Escrito asi por posible generalizacion
		int nEliminados = 1;
		tamanoAct = tamanoAct - nEliminados;
		
		// Caso especial: se eliminaron todos los elementos
		if (tamanoAct == 0) {
			elementos = (T[]) new Object[2];
			tamanoMax = 2;
			return dato;
		}
					
		// Revalua el tamanoMax necesario para elementos
		while (tamanoAct <= tamanoMax/4) tamanoMax = tamanoMax/4;
		
		return dato;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int iActual = 0;
			@Override
			public boolean hasNext() {
				if (tamanoAct <= iActual) return false;
				return true;
			}
			@Override
			public T next() {
				return elementos[iActual++];
			}
		};
	}
}