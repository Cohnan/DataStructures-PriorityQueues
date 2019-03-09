package model.util;

import java.util.Comparator;

import junit.framework.TestCase;
import model.data_structures.ArregloDinamico;

public class SortTest extends TestCase{
	/*
	 * Constantes
	 */
	// Tamano de la muestra
	private final int N = 1000000;
	// Numero de escenarios de prueba
	private final int totalEscenarios = 3;
	
	/*
	 * Atributos
	 */
	// Muestra de datos a ordenar
	private ArregloDinamico<Double> datos;
	private Comparator<Double> comparador = Comparator.<Double>naturalOrder();
	
	/**
	 * Establece los escenarios de prueba
	 * @param n Numero de escenario de prueba a configurar
	 */
	private void setUpEscenario(int n) {
		if (n >= totalEscenarios) throw new IllegalArgumentException("No hay tantos escenarios");
		datos = new ArregloDinamico<Double>(N);
		switch(n) {
		// Escenario: muestra ya ordenada
		case 0:
			for(int i = 0; i < N; i++) datos.agregar((double) i);
			break;
		// Escenarios: muestra totalmente desordenada
		case 1:
			for(int i = 0; i < N; i++) datos.agregar(N-1.-i);
		// Escenario: muestra aleatoria
		case 2:
			for(int i = 0; i < N; i++) datos.agregar(Math.random());
		}
	}
	
	/**
	 * Prueba de ShellSort
	 */
	public void testShellSort() {
		for (int n = 0; n < totalEscenarios; n++) {
			setUpEscenario(n);
				
			Sort.ordenarShellSort(datos, comparador);
			assertTrue(Sort.isSorted(comparador, datos));
		}
	}
	
	/**
	 * Prueba de MergeSort
	 */
	public void testMergeSort() {
		for (int n = 0; n < totalEscenarios; n++) {
			setUpEscenario(n);
				
			Sort.ordenarMergeSort(datos, comparador);
			assertTrue(Sort.isSorted(comparador, datos));
		}
	}
	
	/**
	 * Prueba de QuickSort
	 */
	public void testQuickSort() {
		for (int n = 0; n < totalEscenarios; n++) {
			setUpEscenario(n);
				
			Sort.ordenarQuickSort(datos, comparador);
			assertTrue(Sort.isSorted(comparador, datos));
		}
	}
}
