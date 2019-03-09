package model.data_structures;
/**
 * Basado en el codigo dado por el profesor De la Rosa, Universidad de los Andes, 
 * en el curso Estructuras de Datos en el periodo 201910.
 */
public interface IArregloDinamico<T> extends Iterable<T>{

	/**
	 * Retornar el numero de elementos en el arreglo
	 * @return
	 */
	int darTamano( );
	
	/**
	 * Retornar el elemento en la posicion i
	 * @param i posicion de consulta
	 * @return elemento de consulta. null si no hay elemento en posicion.
	 */
	T darObjeto( int i );

	/**
	 * Agregar un dato de forma compacta (en la primera casilla disponible).
	 * Caso Especial: Si el arreglo esta lleno debe aumentarse su capacidad, agregar el nuevo dato y deben quedar multiples casillas disponibles para futuros nuevos datos.
	 * @param dato nuevo elemento
	 */
	public void agregar( T dato );
	
	/**
	 * Cambiar el dato en la posicion dada 
	 * @param i posicion del arreglo a modificar
	 */
	public void cambiarEnPos( int i , T dato);
	
	/**
	 * Eliminar el dato en la posicion i del arreglo.
	 * Los datos restantes deben quedar "compactos" desde la posicion 0.
	 * @param i Posicion del dato
	 * @return dato eliminado
	 */
	T eliminarEnPos( int i );
}