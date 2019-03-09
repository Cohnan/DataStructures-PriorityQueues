package controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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

	// Muestra obtenida de los datos cargados 
	IArregloDinamico<VOMovingViolation> muestra;

	// Copia de la muestra de datos a ordenar 
	IArregloDinamico<VOMovingViolation> muestraCopia;

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
			movingVOLista = new ArregloDinamico<VOMovingViolation>();

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
					movingVOLista.agregar(new VOMovingViolation(posiciones, row));
					contadores[fileCounter] += 1;
				}
				fileCounter += 1;
			}

			for (int contador : contadores) suma += contador;

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
	public void generarMuestra( int n )
	{
		if(n > 240000){
			throw new IllegalArgumentException("No se generan muestras de tal tamanio.");
		}
		muestra = new ArregloDinamico<VOMovingViolation>(n);	
		IArregloDinamico<Integer> posiciones  =  new ArregloDinamico<>(n);
		
		// Generar posiciones
		long initTimePosGen = System.currentTimeMillis();
		for (int i = 0; i < n; i++){
			posiciones.agregar((int)(Math.random() * (movingVOLista.darTamano()-1)));
		}
		
		while(!Sort.isSorted(Comparator.<Integer>naturalOrder(), posiciones)) {
			Sort.ordenarShellSort(posiciones, Comparator.<Integer>naturalOrder()); //Rapido para listas parcialmente ordenadas
			for (int i = 0; i < n-1; i++) {
				while (posiciones.darObjeto(i) == posiciones.darObjeto(i+1)) posiciones.cambiarEnPos(i,(int)(Math.random() * movingVOLista.darTamano()-1));
			}
		}
		
		// Cargar muestra
		int contadorInf = 0;
		Iterator<VOMovingViolation> iterador = movingVOLista.iterator();
		VOMovingViolation infraccionAct = iterador.next();
		long initTimeCargando = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			while (contadorInf != posiciones.darObjeto(i)) {
				infraccionAct = iterador.next();
				contadorInf += 1;
			}
			muestra.agregar(infraccionAct);
		}
		
		
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
				this.generarMuestra( nMuestra );
				view.printMensage("Muestra generada");
				break;

			case 3:
				// Mostrar los datos de la muestra actual (original)
				if ( nMuestra > 0 && muestra != null && muestra.darTamano() == nMuestra )
				{    
					view.printDatosMuestra( nMuestra, muestra);
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;
/*
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
*/
			case 7:
				// Mostrar los datos de la muestra ordenada (muestra copia)
				
				
				if ( nMuestra > 0 && muestraCopia != null && muestraCopia.darTamano() == nMuestra )
				{    view.printDatosMuestra( nMuestra, muestraCopia);    }
				else
				{
					view.printMensage("Muestra Ordenada invalida");
				}
				break;

			case 8:	
				// Una muestra ordenada se convierte en la muestra a ordenar
				if ( nMuestra > 0 && muestraCopia != null && muestraCopia.darTamano() == nMuestra )
				{    
					muestra = muestraCopia;
					view.printMensage("La muestra ordenada (copia) es ahora la muestra de datos a ordenar");
				}
				break;
/*
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
*/
			case 10:	
				fin=true;
				sc.close();
				break;
			}
		}
	}

}
