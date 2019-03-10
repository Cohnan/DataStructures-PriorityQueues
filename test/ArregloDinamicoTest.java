/**
 * 
 */


import java.util.Random;

import junit.framework.TestCase;
import model.data_structures.ArregloDinamico;

/**
 * @author cohnan daniya97
 *
 */
public class ArregloDinamicoTest extends TestCase{
	/*
	 * Atributos 
	 */
	private ArregloDinamico<String> arreglo;
	private final int numeroEscenarios = 1000;
	private final int tamanoMax = 100;
	
	int nEscenariosEliminar = numeroEscenarios/10; // Numero de escenarios en los que se probara el metodo
	int nEscenariosObtener = numeroEscenarios/10; // Maximo numero de escenarios enlos que probara el metodo obtener
	int nEscenariosCambiar = numeroEscenarios/10; // Maximo numero de escenarios enlos que probara el metodo cambiar

	/*
	 * Escenarios
	 */
	// Arreglo con n elementos
	private void setUpEscenario(int n, int max) {
		if (max == 0) 
			arreglo = new ArregloDinamico<String>();
		else
			arreglo = new ArregloDinamico<String>(max);
		for (int i = 0; i < n; i++) {
			arreglo.agregar("Elemento " + i);
		}
	}

	/*
	 * Metodos para Pruebas
	 */
	/**
	 * Prueba el constructor
	 */
	public void testArregloDinamico() {
		for (int n = 0; n < numeroEscenarios; n++) {
			setUpEscenario(n, 0);
			assertTrue("El arreglo deberia tener tamano " + n, arreglo.darTamano() == n);
			setUpEscenario(n, tamanoMax);
			assertTrue("El arreglo deberia tener tamano " + n, arreglo.darTamano() == n);
		}
	}
	
	/**
	 * Prueba el metodo agregar
	 */
	public void testAgregar() {
		int nAgregados = 3;
		
		for (int n = 0; n <= numeroEscenarios; n++) {
			setUpEscenario(n, 0);
			// Agrega nAgregados elementos
			for (int i = 0; i < nAgregados; i++) arreglo.agregar("Nuevo elemento " + i);
			assertTrue("Escenario: " + n + ". El arreglo deberia tener " + (n + nAgregados) + " elementos."
					+ " Pero tiene " + arreglo.darTamano(), arreglo.darTamano() == (n + nAgregados));
			
			int i = 0;
			for (String dato : arreglo) {
				// Verifica que los primeros n elementos sean los esperados
				if (i < n) {
					assertTrue("Escenario: " + n + ". El " + i + "-esimo elemento deberia ser: Elemento " + i
							+ ", pero se obtuvo " + dato, arreglo.darObjeto(i).equals("Elemento " + i));
					i++;
				}
				// Verifica que los nAgregados elementos siguientes
				else if (n <= i &&  i < n + nAgregados) {
					assertTrue("Escenario: " + n + ". El dato esperado era: " + "Nuevo elemento " + (i-n)
							+ ", pero se obtuvo " + dato, dato.equals("Nuevo elemento " + (i-n)));
					i++;
				}
			}
		}
	}
	
	/**
	 * Prueba el metodo cambiarEnPos()
	 */
	public void testCambiarEnPos() {
		int nc; // Numero de elementos a cambiar en cada escenario
		int[] posicionesCambiar; // Posiciones a cambiar en cada escenario
		for (int n = 0; n < nEscenariosCambiar; n++) {
			setUpEscenario(n, 0);
			
			// Elegir numero posiciones a cambiar
			nc = 2*n / 3;
			
			// Elegir las posiciones a cambiar
			posicionesCambiar = new int[nc];
			int i;
			for (int k = 0; k < nc; k++) {
				i = (n-1) - k*(n/nc);
				posicionesCambiar[k]= i;
			}
			
			// Cambiar los elementos
			String dato;
			for (int k = 0; k < nc; k++) {
				i = posicionesCambiar[k];
				
				dato = "Nuevo dato " + k;
				arreglo.cambiarEnPos(i, dato);
				// Verificar que se cambio correctamente el dato
				assertTrue("Escenario: " + n + ". El dato esperado era: " + "Nuevo dato " + k
							+ ", pero se obtuvo " + dato, dato.equals("Nuevo dato " + k));
				// Verificar que no ha cambiado el tamano del arreglo
				assertTrue("Escenario: " + n + ". El arreglo deberia tener " + n + " elementos."
							+ " Pero tiene " + arreglo.darTamano(), arreglo.darTamano() == n);
			}
		}
	}
	
	/**
	 * Prueba el metodo darTamano()
	 */
	public void testDarTamano() {
		for (int n = 0; n <= numeroEscenarios; n++) {
			setUpEscenario(n, 0);
			assertTrue("Escenario: " + n + ". El arreglo deberia tener " + n + " elementos."
						+ " Pero tiene " + arreglo.darTamano(), arreglo.darTamano() == n);
		}
	}
	
	/**
	 * Prueba el metodo darObjeto()
	 */
	public void testDarObjeto() {
		int np; // Numero de elementos a cambiar en cada escenario
		int[] posicionesProbar; // Posiciones a probar en cada escenario
		
		for (int n = 0; n <= nEscenariosObtener; n++) {
			setUpEscenario(n, 0);
			
			// Elegir numero posiciones a obtener
			np = 2*n / 3;
			
			// Elegir las posiciones a probar
			Random random = new Random(System.currentTimeMillis());
			posicionesProbar = new int[np];
			int i;
			for (int k = 0; k < np; k++) {
				i = random.nextInt(n);
				posicionesProbar[k]= i;
			}
			
			// Obtener los elementos
			String dato;
			for (int k = 0; k < np; k++) {
				i = posicionesProbar[k];
				
				dato = arreglo.darObjeto(i);
				// Verificar que el objeto es el esperado
				assertTrue("Escenario: " + n + ". El dato esperado era: " + "Elemento " + i
						    + ", pero se obtuvo " + dato, dato.equals("Elemento " + i));
				// Verificar que no ha cambiado el tamano del arreglo
				assertTrue("Escenario: " + n + ". El arreglo deberia tener " + n + " elementos."
							+ " Pero tiene " + arreglo.darTamano(), arreglo.darTamano() == n);
			}
		}
	}
	
	/**
	 * Prueba los metodos de eliminacion de elementos
	 */
	public void testEliminarEnPos() {
		int npe; // Posiciones a eliminar en cada escenario
		
		int[] posicionesEliminar;
		for (int n = 1; n <= nEscenariosEliminar; n++) {
			setUpEscenario(n, 0);
			
			// Elegir numero aleatorio de posiciones a eliminar
			Random random = new Random(System.currentTimeMillis());
			npe = random.nextInt(n+1);
						
			// Elegir las posiciones a eliminar
			posicionesEliminar = new int[npe];
			int i;
			for (int k = 0; k < npe; k++) {
				i = (n-1) - k*(n/npe); // Orden descendente necesario para esta implementacion
				posicionesEliminar[k]= i;
			}
			
			// Eliminar los elementos desde el mas grande
			int eliminados = 0;
			String dato;
			for (int k = 0; k < npe; k++) {
				i = posicionesEliminar[k];
				dato = arreglo.eliminarEnPos(i);
				eliminados += 1;
				// Verificar que se elimino el elemento correcto
				assertTrue("Escenario: " + n + ". El dato esperado era: " + "Elemento " + (i)
						+ ", pero se obtuvo " + dato, dato.equals("Elemento " + (i)));
				assertTrue("Escenario: " + n + ". El arreglo deberia tener " + (n - eliminados) + " elementos."
						+ " Pero tiene " + arreglo.darTamano(), arreglo.darTamano() == (n - eliminados));
				// Verificar que el elemento que ahora se encuentra en la posicon i es el i + 1
				if (i < (arreglo.darTamano()-1) && ((n/npe) != 1)) {
					assertTrue("Escenario: " + n + ". El " + i + "-esimo elemento deberia ser: Elemento " + (i+1)
							+ ", pero se obtuvo " + dato, arreglo.darObjeto(i).equals("Elemento " + (i+1)));
				}
			}
		}
	}
	
	/**
	 * Prueba el metodo iterator()
	 */
	public void testIterator() {
		int n = 1000;
		
		// Probar el iterador para los 2 posibles constructores para un n grande cualquiera
		for (Integer max : new Integer[]{0, tamanoMax}){
			setUpEscenario(n, max);
			
			int i = 0;
			for(String dato: arreglo) {
				assertTrue("Escenario: " + n + ". El " + i + "-esimo elemento deberia ser: Elemento " + i
						+ ", pero se obtuvo " + dato, arreglo.darObjeto(i).equals("Elemento " + i));
				i += 1;
			}
			// Verificar que solo se identifican n elementos
			assertTrue("Escenario: " + n + ". El iterador deberia identificar y devolver " + n + " elementos", i == n);
		}
	}

	
}
