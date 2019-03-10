package controller;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

import com.opencsv.CSVReader;

import model.data_structures.*;
import model.util.Sort;
import model.vo.LocationVO;
import model.vo.VOMovingViolation;
import model.vo.VOViolationCode;
import view.MovingViolationsManagerView;

@SuppressWarnings("unused")
public class Controller {

	public static final String[] movingViolationsFilePaths = new String[] {"data/Moving_Violations_Issued_in_January_2018.csv", "data/Moving_Violations_Issued_in_February_2018.csv", "data/Moving_Violations_Issued_in_March_2018.csv"};

	private MovingViolationsManagerView view;

	private static IArregloDinamico<VOMovingViolation> movingVOLista;
	private static IArregloDinamico<LocationVO> locationVOLista;
	private static IArregloDinamico<LocationVO> muestraLoc;

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
		CSVReader reader = null;

		// Contadores de infracciones en cada mes de los archivos a cargar
		int[] contadores = new int[movingViolationsFilePaths.length]; // No se usa en este taller
		int fileCounter = 0; 
		int suma = 0;
		try {
			movingVOLista = new ArregloDinamico<VOMovingViolation>();

			for (String filePath : movingViolationsFilePaths) {
				// Entender el header
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
			for(int nInfs : contadores) suma += nInfs;
			/*
			 * Generar la lista de LocationVOs
			 */
			Sort.ordenarShellSort(movingVOLista, new VOMovingViolation.AddressIDOrder());
			
			// Inicializar la lista de LocationVOs
			locationVOLista = new ArregloDinamico<LocationVO>();

			// Si no hay datos, entonces deja la cola vacia
			Iterator<VOMovingViolation> iterador = movingVOLista.iterator();
			if (!iterador.hasNext()) {return suma;}

			// Como los datos estan ordenados, tomamos una infraccion de referencia para comparar con
			// los datos inmediatamente siguientes
			VOMovingViolation infrRevisar = iterador.next();
			int addressRef = infrRevisar.getAddressID();
			String locationRef = infrRevisar.getLocation();
			// Contador de infracciones con el mismo AddressID
			int contadorIgs = 1;

			while (iterador.hasNext()) { // Podria hacerse diferente
				infrRevisar = iterador.next();

				if (addressRef == infrRevisar.getAddressID()) {
					// Actualiza contadores
					contadorIgs += 1;
				} else {
					// Agrega el LocationVO que esta revisando a la cola
					locationVOLista.agregar(new LocationVO(addressRef, locationRef, contadorIgs));
					
					// Reestablece referencias
					addressRef = infrRevisar.getAddressID();
					locationRef = infrRevisar.getLocation();
					contadorIgs = 1;
				}
			}
			// Agregar la ultima referencia 
			locationVOLista.agregar(new LocationVO(addressRef, locationRef, contadorIgs));
			System.out.println("Hay " + locationVOLista.darTamano() + " LocationVOs");

		} catch (Exception e) {
			e.printStackTrace();
			suma = -1;
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
	public IArregloDinamico<VOMovingViolation> generarMuestraInfracciones(int n)
	{
		if(n > movingVOLista.darTamano()){
			throw new IllegalArgumentException("No se generan muestras de tal tamanio.");
		}
		
		IArregloDinamico<VOMovingViolation> muestra = new ArregloDinamico<VOMovingViolation>(n);	
		IArregloDinamico<Integer> posiciones  =  new ArregloDinamico<>(n);
		
		// Generar posiciones
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
		//long initTimeCargando = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			while (contadorInf != posiciones.darObjeto(i)) {
				infraccionAct = iterador.next();
				contadorInf += 1;
			}
			muestra.agregar(infraccionAct);
		}
		
		return muestra;
	}

	/**
	 * Generar una muestra aleatoria de tamaNo n de LocationVOs
	 * Los datos de la muestra se obtienen de las infracciones guardadas en la Estructura de Datos.
	 * @param n tamaNo de la muestra, n > 0
	 * @return muestra generada
	 */
	public IArregloDinamico<LocationVO> generarMuestraLocM2(int n)
	{	
		if(n > locationVOLista.darTamano()){
			throw new IllegalArgumentException("El tamanio maximo de la muestra es " + locationVOLista.darTamano());
		}
		IArregloDinamico<LocationVO> muestra = new ArregloDinamico<LocationVO>(n);	
		IArregloDinamico<Integer> posiciones  =  new ArregloDinamico<>(n);
		
		// Generar posiciones
		for (int i = 0; i < n; i++){
			posiciones.agregar((int)(Math.random() * (locationVOLista.darTamano()-1)));
		}
		
		while(!Sort.isSorted(Comparator.<Integer>naturalOrder(), posiciones)) {
			Sort.ordenarShellSort(posiciones, Comparator.<Integer>naturalOrder()); //Rapido para listas parcialmente ordenadas
			for (int i = 0; i < n-1; i++) {
				while (posiciones.darObjeto(i) == posiciones.darObjeto(i+1)) posiciones.cambiarEnPos(i,(int)(Math.random() * locationVOLista.darTamano()-1));
			}
		}
		
		// Cargar muestra
		int contadorInf = 0;
		Iterator<LocationVO> iterador = locationVOLista.iterator();
		LocationVO locAct = iterador.next();
		//long initTimeCargando = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			while (contadorInf != posiciones.darObjeto(i)) {
				locAct = iterador.next();
				contadorInf += 1;
			}
			muestra.agregar(locAct);
		}
		// 
		//Sort.shuffle(muestra);
		this.muestraLoc = muestra;
		return muestra;
	}
	
	/**
	 * Generar una copia de una muestra. Se genera un nuevo arreglo con los mismos elementos.
	 * @return copia de la muestra
	 */
	public IArregloDinamico<LocationVO> generarCopiaLoc( )
	{
		IArregloDinamico<LocationVO> copia = new ArregloDinamico<LocationVO>(); 
		for ( LocationVO loc : locationVOLista)
		{    copia.agregar(loc);    }
		return copia;
	}
	
	/**
	 * Devuelve los tiempos promedios para agregar y eliminar elementos en la cola
	 * de prioridad dada por parametro
	 * @param colaP Cola de Prioridad sobre la cual se adicionaran los elementos de la muestra
	 * @return tiempos[0] el tiempo promedio de adicion; tiempos[1] el tiempo promedio de eliminacion (nanosegundos)
	 */
	public long[] medirTiemposPromedio(IColaPrioridad<LocationVO> colaP) {
		long startTime;
		long endTime;
		long[] duraciones = new long[2];
		int totalDatos = colaP.darNumElementos();
		
		// Medicion de agregar()
		startTime = System.nanoTime();//.truncatedTo(ChronoUnit.MICROS);
		for (LocationVO loc : muestraLoc) {
			colaP.agregar(loc);
		}
		endTime = System.nanoTime();//.truncatedTo(ChronoUnit.MICROS);
		duraciones[0] = (endTime - startTime)/muestraLoc.darTamano();
		
		// Medicion de delMax()
		startTime = System.nanoTime();
		for (int i = 0; i < totalDatos; i++) {
			colaP.delMax();
		}
		endTime = System.nanoTime();
		duraciones[1] = (endTime - startTime)/muestraLoc.darTamano();
		return duraciones;
	}

	private MaxColaPrioridad <LocationVO> crearMaxColaP (LocalDateTime fInicial, LocalDateTime 
			fFinal){
		MaxColaPrioridad<LocationVO> respuesta = new MaxColaPrioridad<LocationVO>();
		creacionDeColas(respuesta, fInicial, fFinal);
		return respuesta;
	}
	
	private MaxHeapCP <LocationVO> crearMaxHeapCP (LocalDateTime fInicial, LocalDateTime fFinal) {
		MaxHeapCP<LocationVO> respuesta = new MaxHeapCP<LocationVO>();
		creacionDeColas(respuesta, fInicial, fFinal);
		return respuesta;
	}
	
	private void creacionDeColas(IColaPrioridad<LocationVO> respuesta, LocalDateTime fInicial, LocalDateTime fFinal) {
		if (!Sort.isSorted(new VOMovingViolation.AddressIDOrder(), movingVOLista)) {
			Sort.ordenarShellSort(movingVOLista, new VOMovingViolation.AddressIDOrder());
		}
		
		// Inicializar la lista de LocationVOs
		//MaxHeapCP<LocationVO> respuesta = new MaxHeapCP<LocationVO>();

		// Si no hay datos, entonces deja la cola vacia
		Iterator<VOMovingViolation> iterador = movingVOLista.iterator();
		if (!iterador.hasNext()) {return;}

		// Como los datos estan ordenados, tomamos una infraccion de referencia para comparar con
		// los datos inmediatamente siguientes
		VOMovingViolation infrRevisar = iterador.next();
		int addressRef = infrRevisar.getAddressID();
		String locationRef = infrRevisar.getLocation();
		// Contador de infracciones con el mismo AddressID en el rango de fechas indicado
		int contadorIgs = 0;
		if (infrRevisar.getTicketIssueDate().compareTo(fInicial) >= 0 && 
				infrRevisar.getTicketIssueDate().compareTo(fFinal) <= 0) {
			contadorIgs += 1;
		}
		while (iterador.hasNext()) { // Podria hacerse diferente
			infrRevisar = iterador.next();
	
			if (addressRef == infrRevisar.getAddressID()) {
				// Actualiza contadores
				if (infrRevisar.getTicketIssueDate().compareTo(fInicial) >= 0 && 
						infrRevisar.getTicketIssueDate().compareTo(fFinal) <= 0) {
					contadorIgs += 1;
				}
			} else {
				// Agrega el LocationVO que esta revisando a la cola
				respuesta.agregar(new LocationVO(addressRef, locationRef, contadorIgs));
				
			// 	Reestablece referencias
				addressRef = infrRevisar.getAddressID();
				locationRef = infrRevisar.getLocation();
				contadorIgs = 0;
				if (infrRevisar.getTicketIssueDate().compareTo(fInicial) >= 0 && 
						infrRevisar.getTicketIssueDate().compareTo(fFinal) <= 0) {
					contadorIgs += 1;
				}
			}
		}
		// Agregar la ultima referencia 
		respuesta.agregar(new LocationVO(addressRef, locationRef, contadorIgs));
	}
	
	
	
	public void run() {
		int nDatos = 0;
		int nMuestra = 0;

		Scanner sc = new Scanner(System.in);
		boolean fin = false;

		while(!fin)
		{
			view.printMenu();

			int option = sc.nextInt();
			long[] tiempos;
			
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
				this.generarMuestraLocM2( nMuestra );
				view.printMensage("Muestra generada");
				break;

			case 3:
				// Mostrar los datos de la muestra actual (original)
				if ( nMuestra > 0 && muestraLoc != null && muestraLoc.darTamano() == nMuestra )
				{    
					view.printDatosMuestra( nMuestra, muestraLoc);
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 4:
				// MaxColaPrioridad: Calcular tiempo promedio para agregar y eliminar datos segun la muestra actual
				if ( nMuestra > 0 && muestraLoc != null && muestraLoc.darTamano() == nMuestra )
				{
					tiempos = this.medirTiemposPromedio(new MaxColaPrioridad<LocationVO>());
					view.printMensage("Tiempo promedio de adicion: " + tiempos[0] + " nanosegundos.");
					view.printMensage("Tiempo promedio de eliminacion: " + tiempos[1] + " nanosegundos.");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;

			case 5:
				// MaxHeapCP: Calcular tiempo promedio para agregar y eliminar datos segun la muestra actual
				if ( nMuestra > 0 && muestraLoc != null && muestraLoc.darTamano() == nMuestra )
				{
					tiempos = this.medirTiemposPromedio(new MaxHeapCP<LocationVO>());
					view.printMensage("Tiempo promedio de adicion: " + tiempos[0] + " nanosegundos.");
					view.printMensage("Tiempo promedio de eliminacion: " + tiempos[1] + " nanosegundos.");
				}
				else
				{
					view.printMensage("Muestra invalida");
				}
				break;
/*
			case 6:
				// Aplicar QuickSort a una copia de la muestra
				
	
				
				if ( nMuestra > 0 && muestra != null && muestra.length == nMuestra )
				{
					muestraLocCopia = this.obtenerCopia(muestra);
					startTime = System.currentTimeMillis();
					this.ordenarQuickSort(muestraLocCopia);
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
				
				
				if ( nMuestra > 0 && muestraLocCopia != null && muestraLocCopia.darTamano() == nMuestra )
				{    view.printDatosMuestra( nMuestra, muestraLocCopia);    }
				else
				{
					view.printMensage("Muestra Ordenada invalida");
				}
				break;

			case 8:	
				// Una muestra ordenada se convierte en la muestra a ordenar
				if ( nMuestra > 0 && muestraLocCopia != null && muestraLocCopia.darTamano() == nMuestra )
				{    
					muestraLoc = muestraLocCopia;
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
*/
			case 10:	
				fin=true;
				sc.close();
				break;
			}
		}
	}

}
