package view;

import model.data_structures.IArregloDinamico;
import model.vo.LocationVO;
import model.vo.VOMovingViolation;

public class MovingViolationsManagerView 
{
	public MovingViolationsManagerView() {
		
	}
	
	public void printMenu() {
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Taller 4----------------------");
		System.out.println("1. Cargar datos de infracciones en movimiento");
		System.out.println("2. Obtener una muestra de datos a ordenar");
		System.out.println("3. Mostrar la muestra de datos a ordenar");
		System.out.println("4. MaxColaPrioridad: Calcular tiempo promedio para agregar y eliminar datos segun la muestra actual");
		System.out.println("5. MaxHeapCP: Calcular tiempo promedio para agregar y eliminar datos segun la muestra actual");
		System.out.println("6. Comparación de tiempos");
		System.out.println("Digite el numero de opcion para ejecutar la tarea, luego presione enter: (Ej., 1):");
		
	}
	
	public void printDatosMuestra( int nMuestra, IArregloDinamico<LocationVO> muestra)
	{
		for ( LocationVO dato : muestra)
		{	System.out.println(  dato.toString() );    }
	}
	
	public void printMensage(String mensaje) {
		System.out.println(mensaje);
	}
}
