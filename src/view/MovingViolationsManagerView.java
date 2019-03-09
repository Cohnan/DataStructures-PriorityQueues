package view;

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
		System.out.println("4. Ordenar datos de la muestra por Shellsort (ordenamiento de una copia de la muestra)");
		System.out.println("5. Ordenar datos de la muestra por Mergesort (ordenamiento de una copia de la muestra)");
		System.out.println("6. Ordenar datos de la muestra por Quicksort (ordenamiento de una copia de la muestra)");
		System.out.println("7. Mostrar la muestra ordenada (muestra copia)");
		System.out.println("8. Volver la muestra ordenada (copia) como la muestra actual a ordenar");
		System.out.println("9. Invertir la muestra de datos");
		System.out.println("10. Salir");
		System.out.println("Digite el numero de opcion para ejecutar la tarea, luego presione enter: (Ej., 1):");
		
	}
	
	public void printDatosMuestra( int nMuestra, Comparable [ ] muestra)
	{
		for ( Comparable dato : muestra)
		{	System.out.println(  dato.toString() );    }
	}
	
	public void printMensage(String mensaje) {
		System.out.println(mensaje);
	}
}
