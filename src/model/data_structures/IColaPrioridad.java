package model.data_structures;

public interface IColaPrioridad<T extends Comparable<T>> extends Iterable<T> {
	/**
	 * Retorna true si la Cola esta vacia
	 * @return true si la Cola esta vacia, false de lo contrario
	 */
	public boolean esVacia();
	
	/**
	 * Retorna el numero de elementos contenidos
	 * @return el numero de elemntos contenidos
	 */
	public int darNumElementos();
	
	/**
	 * Inserta un nuevo elemento en la Cola
	 * @param t el nuevo elemento que se va ha agregar
	 */
	public void agregar(T t);
	
	/**
	 * Quita y retorna el elemento de mayor prioridad
	 * @return el elemento eliminado. null en caso de estar vacia
	 */
	public T delMax();
	
	/**
	 * Retorna el elemento de mayor prioridad sin sacarlo
	 * @return el el elemento de mayor prioridad. null en caso de estar vacia
	 */
	public T max();
}
