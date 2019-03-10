package model.util;
import java.lang.Math;
import java.util.Comparator;
import java.util.Random;

import model.data_structures.*;

public class Sort {
	
	/*
	 * **********************************************************************************************
	 * *********************************** SHELL SORT ***********************************************
	 * **********************************************************************************************
	 */
	/**
	 * Ordenar datos aplicando el algoritmo ShellSort
	 * @param datos - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */

	public static <T> void ordenarShellSort(IArregloDinamico<T> datos, Comparator<T> c) {
		// Esta es una implementacion hecha con el proposito de entender shellsort, que permite
		// el uso de diferentes secuencias con facilidad. La unica diferencia en uso de recursos
		// de esta implementacion es la creacion de la secuencia y su guardado en memoria, algo minimo.
		int[] secuencia = giveSequence(datos.darTamano());
		
		for (int h : secuencia) hsort(datos, h, c);
	}
	
	private static <T> void hsort(IArregloDinamico<T> datos, int h, Comparator<T> c) {
		int posOfInserting;
		for (int i = 0; i < h; i += 1) {
			// insertion-sort el h-subarray que empieza en i
			for (int j = i + h; j < datos.darTamano() ; j += h) {
				posOfInserting = j;
				while ((posOfInserting - h)>= 0 && less(datos.darObjeto(posOfInserting), datos.darObjeto(posOfInserting-h), c)) {
					exchange(datos, posOfInserting, posOfInserting-h);
					posOfInserting -= h;
				}
			}
		}
	}
	
	private static int[] giveSequence(int n) {
		// Usamos la secuencia 1, 4, 13, ...
		if (n < 4) return new int[] {1};
		// Esto asegura que para el h maximo, cada subarray que ea h-sorted tenga longitud al menos 3
		int k = (int)(Math.log(2*n + 1)/Math.log(3) - 1);
		int[] lista = new int[k];
		for (int i = 0; i < k; i++) lista[i] = (int)((Math.pow(3, k-i) - 1)/2);
		
		return lista;
	}
	/*
	 * **********************************************************************************************
	 */
	
	
	/*
	 * **********************************************************************************************
	 * *********************************** MERGE SORT ***********************************************
	 * **********************************************************************************************
	 */
	/**
	 * Ordenar datos aplicando el algoritmo MergeSort
	 * @param movingViolationsQueue - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */
	public static <T> void ordenarMergeSort(IArregloDinamico<T> datos, Comparator<T> comparator ) {
		
		//Bottom - Up
		int N = datos.darTamano();
		//Se crea un arreglo auxiliar
		Queue<T> auxiliar = new Queue<T>();
		Queue<T> auxiliar2 = new Queue<T>();
		for (int i = 1; i < N; i = i+i) {
			for (int min = 0; min < N-i; min+=i+i) {
				merge(datos, auxiliar,auxiliar2, min, min+i-1, Math.min(min+i+i-1, N-1), comparator);
			}
		}
	}

	
	private static <T> void merge(IArregloDinamico<T> datos, Queue<T> auxiliar, Queue<T> auxiliar2, int min, int med, int max, Comparator<T> comparator){
		//Se copia el arreglo
		for( int k = min; k<=med; k++){
			auxiliar.enqueue(datos.darObjeto(datos.darTamano()-1));
		}
		for( int k = med+1; k<=max; k++){
			auxiliar2.enqueue(datos.darObjeto(datos.darTamano()-1));
		}
		
		T i = auxiliar.dequeue();
		T j = auxiliar2.dequeue();
		
		for(int k = min; k<= max;k++){
			
			if (i == null) {
				datos.cambiarEnPos(k, j);
				j = auxiliar2.dequeue();
			}
			else if(j == null){
				datos.cambiarEnPos(k, i);
				i = auxiliar.dequeue();
			}
			else if(less(i,j, comparator)){
				datos.cambiarEnPos(k, i);
				i = auxiliar.dequeue();
			}
			else{
				datos.cambiarEnPos(k, j);
				j = auxiliar2.dequeue();
			}
		}
	}
	/*
	 * **********************************************************************************************
	 */
	
	
	/*
	 * **********************************************************************************************
	 * *********************************** QUICK SORT ***********************************************
	 * **********************************************************************************************
	 */
	/**
	 * Ordenar datos aplicando el algoritmo QuickSort
	 * @param datos - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */
	public static <T> void ordenarQuickSort(IArregloDinamico<T> datos, Comparator<T> c) {
		// Desordenar: basado shuffle() de la libreria StdRandom del libro
		shuffle(datos);
		ordenarQuickSort(datos, 0, datos.darTamano()-1, c);
	}
	
	private static <T> void ordenarQuickSort(IArregloDinamico<T> datos, int min, int max, Comparator<T> c) {
		if (min >= max) return;
		
		T ref = datos.darObjeto(min);
		
		// Partir array en dato de referencia, datos menores o iguales, y datos mayores
		int indLastLeq = min;
		int indFirstGr = max + 1;
		while (true) {
			// Encontrar la posicion actual donde termina la particion de numeros menores o iguales
			while (indLastLeq < max && !less(ref, datos.darObjeto(indLastLeq + 1), c)) indLastLeq += 1;
			if (indLastLeq + 1 == indFirstGr) break;
			
			// Encontrar la posicion actual donde empieza la particion de numeros mayores
			while (indFirstGr > (min+1) && less(ref, datos.darObjeto(indFirstGr - 1), c)) indFirstGr -= 1;
			if (indLastLeq + 1 == indFirstGr) break;
			
			// En caso de no cruzarse las dos partes del array, hacer el intercambio de los
			// elementos que interrumpen las 2 partes del array
			exchange(datos, indLastLeq + 1, indFirstGr - 1);
			indLastLeq += 1;
			indFirstGr -= 1;
		}
		// Dejar elemento de referencia en su posicion final
		exchange(datos, min, indLastLeq);
		
		// Ordenar las 2 partes restantes
		ordenarQuickSort(datos, min, indLastLeq - 1, c);
		ordenarQuickSort(datos, indFirstGr, max, c);
	}
	/*
	 * **********************************************************************************************
	 */
	
	/**
	 * Comparar 2 objetos usando la comparacion "natural" de su clase
	 * @param v primer objeto de comparacion
	 * @param w segundo objeto de comparacion
	 * @return true si v es menor que w usando el metodo compareTo. false en caso contrario.
	 */
	private static <T> boolean less(T v, T w, Comparator<T> c)
	{
		return c.compare(v, w) < 0;
	}
	
	/**
	 * Intercambiar los datos de las posicion i y j
	 * @param datos contenedor de datos
	 * @param i posicion del 1er elemento a intercambiar
	 * @param j posicion del 2o elemento a intercambiar
	 */
	private static <T> void exchange(IArregloDinamico<T> datos, int i, int j)
	{
		T temp = datos.darObjeto(i);
		datos.cambiarEnPos(i, datos.darObjeto(j));
		datos.cambiarEnPos(j, temp);
	}
	
	public static <T> boolean isSorted(Comparator<T> c, IArregloDinamico<T> datos) {
		for (int i = 0; i < datos.darTamano()-1; i++)
			if (less(datos.darObjeto(i+1), datos.darObjeto(i), c)) return false;
		return true;
	}
	
	public static <T> void shuffle(IArregloDinamico<T> datos) {
		int n = datos.darTamano();
		int r;
		Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            r = i + random.nextInt(n-i);
            exchange(datos, i, r);
        }
	}
}