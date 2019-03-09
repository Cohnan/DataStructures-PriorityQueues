package controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.opencsv.CSVReader;

import model.data_structures.*;
import model.util.Sort;
import model.vo.VOMovingViolation;
import view.MovingViolationsManagerView;

@SuppressWarnings("unused")
public class Controller {

	public static final String[] movingViolationsFilePaths = new String[] {"data/Moving_Violations_Issued_in_January_2018.csv", "data/Moving_Violations_Issued_in_February_2018.csv", "data/Moving_Violations_Issued_in_March_2018.csv"};

	private MovingViolationsManagerView view;

	private IArregloDinamico<VOMovingViolation> movingVOLista;

	// TODO Definir las estructuras de datos para cargar las infracciones del periodo definido

	// Muestra obtenida de los datos cargados 
	Comparable<VOMovingViolation> [ ] muestra;

	// Copia de la muestra de datos a ordenar 
	Comparable<VOMovingViolation> [ ] muestraCopia;

	public Controller() {
		view = new MovingViolationsManagerView();

		movingVOLista = null;
	}

	/**
	 * Leer los datos de las infracciones de los archivos. Cada infraccion debe ser Comparable para ser usada en los ordenamientos.
	 * Todas infracciones (MovingViolation) deben almacenarse en una Estructura de Datos (en el mismo orden como estan los archivos)
	 * A partir de estos datos se obtendran muestras para evaluar los algoritmos de ordenamiento
	 * @return numero de infracciones leidas 
	 */
	public int loadMovingViolations() {
		// TODO Los datos de los archivos deben guardarse en la Estructura de Datos definida

		CSVReader reader = null;

		// Contadores de infracciones en cada mes de los archivos a cargar
		int[] contadores = new int[movingViolationsFilePaths.length];
		int fileCounter = 0;
		int suma = 0;
		try {
			movingVOLista = new Queue<VOMovingViolation>();

			for (String filePath : movingViolationsFilePaths) {
				reader = new CSVReader(new FileReader(filePath));
				String[] headers = reader.readNext();
				// Array con la posicion de cada header conocido (e.g. LOCATION) dentro del header del archivo
				// Esto permite que el archivo tenga otras columnas y se encuentren en otro orden y aun asi
				// el programa funcione.
				int[] posiciones = new int[VOMovingViolation.EXPECTEDHEADERS.length];
				for (int i = 0; i < VOMovingViolation.EXPECTEDHEADERS.length; i++) {
					posiciones[i] = buscarArray(headers, VOMovingViolation.EXPECTEDHEADERS[i]);
				}
				// Carga de las infracciones a la cola, teniendo en cuenta el formato del archivo
				contadores[fileCounter] = 0;
				for (String[] row : reader) {
					movingVOLista.enqueue(new VOMovingViolation(posiciones, row));
					contadores[fileCounter] += 1;
				}
				fileCounter += 1;
			}

			for (int contador : contadores) suma += contador;
			/*System.out.println("  ----------Informaci�n Sobre la Carga------------------  ");
			for (int i = 0; i < contadores.length; i++) {
				System.out.println("Infracciones Mes " + (i+1)+": " + contadores[i]);
			}
			System.out.println("Total Infracciones Cuatrisemetre: " + suma);*/

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return suma;
	}

	private int buscarArray(String[] array, String string) {
		int i = 0;

		while (i < array.length) {
			if (array[i].equals(string)) return i;
			i += 1;
		}
		return -1;
	}

	/**
	 * Generar una muestra aleatoria de tamaNo n de los datos leidos.
	 * Los datos de la muestra se obtienen de las infracciones guardadas en la Estructura de Datos.
	 * @param n tamaNo de la muestra, n > 0
	 * @return muestra generada
	 */
	public Comparable<VOMovingViolation>[] generarMuestra( int n )
	{

		if(n > 240000){
			throw new IllegalArgumentException("No se generan muestras de tal tamanio.");
		}
		muestra = new Comparable[ n ];	
		Integer[] posiciones  =  new Integer[n];
		
		// Generar posiciones
		long initTimePosGen = System.currentTimeMillis();
		for (int i = 0; i < n; i++){
			posiciones[i] = (int)(Math.random() * (movingVOLista.darTamano()-1));
		}
		
		Sort.ordenarQuickSort(posiciones);
		while(!Sort.isSorted(posiciones)) {
			Sort.ordenarShellSort(posiciones);
			for (int i = 0; i < n-1; i++) {
				while (posiciones[i] == posiciones[i+1]) posiciones[i] = (int)(Math.random() * movingVOLista.darTamano()-1);
			}
		}
		/*
		// Generar posiciones
		contador = 0;
		int random = 0;
		int indDeRandom;
		int temp;
		long initTimePosGen = System.currentTimeMillis();
		while(contador < n){
			random = (int)(Math.random() * movingVOLista.darTamano()-1);
			// Hallar la que seria la posiciones ordenada de random en el arreglo de posiciones
			indDeRandom = contador;
			while (indDeRandom > 0 && posiciones[indDeRandom - 1] > random) {
				indDeRandom -=1;
			}
			// Agrega y aumenta el contador 
			// si no se encuentra ya random en el arreglo
			if (indDeRandom == 0 || posiciones[indDeRandom - 1] < random) {
				posiciones[contador] = random;
				for (int i = contador; i > indDeRandom; i--) {
					temp = posiciones[i];
					posiciones[i] = posiciones[i-1];
					posiciones[i-1] = temp;
				}
				contador ++;
			}
		}
		*/
		///////////////////////////////////////////////////////////////////////////
		long finTimePosGen = System.currentTimeMillis();
		System.out.println("En generarse las posiciones se usaron " + (finTimePosGen - initTimePosGen) + "milis");
		
		// Cargar muestra
		int contadorInf = 0;
		Iterator<VOMovingViolation> iterador = movingVOLista.iterator();
		VOMovingViolation infraccionAct = iterador.next();
		long initTimeCargando = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			while (contadorInf != posiciones[i]) {
				infraccionAct = iterador.next();
				contadorInf += 1;
			}
			muestra[i] = infraccionAct;
		}
		long finTimeCargando = System.currentTimeMillis();

		
		System.out.println("En cargar la muestra se usaron " + (finTimeCargando - initTimeCargando) + "milis");
		System.out.println("Datos de la muestra: primer, segundo, ultimo y tamanio");
		System.out.println(muestra[0] + " " + posiciones[0]);
		System.out.println(muestra[1] + " " +  posiciones[1]);
		System.out.println(muestra[n-1] + " " +  posiciones[n-1]);
		System.out.println(movingVOLista.darTamano());
		return muestra;
	}

	/**
	 * Generar una copia de una muestra. Se genera un nuevo arreglo con los mismos elementos.
	 * @param muestra - datos de la muestra original
	 * @return copia de la muestra
	 */
	public Comparable<VOMovingViolation> [ ] obtenerCopia( Comparable<VOMovingViolation> [ ] muestra)
	{
		Comparable<VOMovingViolation> [ ] copia = new Comparable[ muestra.length ]; 
		for ( int i = 0; i < muestra.length; i++)
		{    copia[i] = muestra[i];    }
		return copia;
	}

	/**
	 * Ordenar datos aplicando el algoritmo ShellSort
	 * @param datos - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */
	public void ordenarShellSort( Comparable<VOMovingViolation>[ ] datos ) {

		Sort.ordenarShellSort(datos);
	}

	/**
	 * Ordenar datos aplicando el algoritmo MergeSort
	 * @param datos - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */
	public void ordenarMergeSort( Comparable<VOMovingViolation>[ ] datos ) {

		Sort.ordenarMergeSort(datos);
	}

	/**
	 * Ordenar datos aplicando el algoritmo QuickSort
	 * @param datos - conjunto de datos a ordenar (inicio) y conjunto de datos ordenados (final)
	 */
	public void ordenarQuickSort( Comparable<VOMovingViolation>[ ] datos ) {

		Sort.ordenarQuickSort(datos);
	}

	/**
	 * Invertir una muestra de datos (in place).
	 * datos[0] y datos[N-1] se intercambian, datos[1] y datos[N-2] se intercambian, datos[2] y datos[N-3] se intercambian, ...
	 * @param datos - conjunto de datos af invertir (inicio) y conjunto de datos invertidos (final)
	 */
	public void invertirMuestra( Comparable[ ] datos ) {
		int n = datos.length;
		Comparable[] auxiliar = new Comparable[n];
		// Hacer copia invertida
		for (int i = 0; i < datos.length; i++) auxiliar[i] = datos[n-i-1];
		// Pegar datos al arreglo original
		for (int i = 0; i < datos.length; i++) datos[i] = auxiliar[i];
		
	}

	public void run() {
		long startTime;
		long endTime;
		long duration;

		int nDatos = 0;
		int nMuestra = 0;

		Scanner sc = new Scanner(System.in);
		boolean fin = false;

		while(!fin)
		{
			view.printMenu();

			int option = sc.nextInt();

			switch(option)
			{
			case 1:
				// Cargar infracciones
				nDatos = this.loadMovingViolations();
				view.printMensage("Numero infracciones cargadas:" + nDatos);
				break;

			case 2:
				// Generar muestra de infracciones a ordenar
				view.printMensage("Dar tamaNo de la muestra: ");
				nMuestra = sc.nextInt();
				muestra = this.generarMuestra( nMuestra );
				view.printMensage("Muestra generada");
				break;

			case 3:
				// Mostrar los datos de la muestra actual (original)
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{    
					view.printDatosMuestra( nMuestra, muestra);
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 4:
				// Aplicar ShellSort a una copia de la muestra
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{
					muestraCopia = this.obtenerCopia(muestra);
					startTime = System.currentTimeMillis();
					this.ordenarShellSort(muestraCopia);
					endTime = System.currentTimeMillis();
					duration = endTime - startTime;
					view.printMensage("Ordenamiento generado en una copia de la muestra");
					view.printMensage("Tiempo de ordenamiento ShellSort: " + duration + " milisegundos");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 5:
				// Aplicar MergeSort a una copia de la muestra
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{
					muestraCopia = this.obtenerCopia(muestra);
					startTime = System.currentTimeMillis();
					this.ordenarMergeSort(muestraCopia);
					endTime = System.currentTimeMillis();
					duration = endTime - startTime;
					view.printMensage("Ordenamiento generado en una copia de la muestra");
					view.printMensage("Tiempo de ordenamiento MergeSort: " + duration + " milisegundos");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 6:
				// Aplicar QuickSort a una copia de la muestra
				
	
				
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{
					muestraCopia = this.obtenerCopia(muestra);
					startTime = System.currentTimeMillis();
					this.ordenarQuickSort(muestraCopia);
					endTime = System.currentTimeMillis();
					duration = endTime - startTime;
					view.printMensage("Ordenamiento generado en una copia de la muestra");
					view.printMensage("Tiempo de ordenamiento QuickSort: " + duration + " milisegundos");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 7:
				// Mostrar los datos de la muestra ordenada (muestra copia)
				
				
				if ( nMuestra > 0 && muestraCopia != null && muestraCopia.length == nMuestra )
				{    view.printDatosMuestra( nMuestra, muestraCopia);    }
				else
				{
					view.printMensage("Muestra Ordenada invalida");
				}
				break;

			case 8:	
				// Una muestra ordenada se convierte en la muestra a ordenar
				if ( nMuestra > 0 && muestraCopia != null && muestraCopia.length == nMuestra )
				{    
					muestra = muestraCopia;
					view.printMensage("La muestra ordenada (copia) es ahora la muestra de datos a ordenar");
				}
				break;

			case 9:
				// Invertir la muestra a ordenar
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{    
					this.invertirMuestra(muestra);
					view.printMensage("La muestra de datos a ordenar fue invertida");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}

				break;

			case 10:	
				fin=true;
				sc.close();
				break;
			}
		}
	}

}
