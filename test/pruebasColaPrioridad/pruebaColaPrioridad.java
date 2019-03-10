package pruebasColaPrioridad;

import junit.framework.TestCase;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.MaxHeapCP;
import model.vo.LocationVO;

public class pruebaColaPrioridad extends TestCase {
	
	private MaxColaPrioridad<LocationVO> cp1;
	private MaxHeapCP<LocationVO> cp2;

	//Colas Vacias
	private void setUpEscenario0() {
		cp1 = new MaxColaPrioridad<LocationVO>();
		cp2 = new MaxHeapCP<LocationVO>();
	}

	private void setUpEscenario1() {
		cp1 = new MaxColaPrioridad<LocationVO>();
		cp2 = new MaxHeapCP<LocationVO>();
		LocationVO auxiliar;

		for (int i = 0; i < 100; i++) {
			auxiliar = new LocationVO(i*i, "NA", i+10);
			cp1.agregar(auxiliar);
			cp2.agregar(auxiliar);
		}


	}


	public void testColaVacia(){
		setUpEscenario0();

		//Las colas de prioridad deberían estar vacías
		assertEquals("No hay elementos debería retornar true",true, cp1.esVacia());
		assertEquals("No hay elementos debería retornar true",true, cp2.esVacia());

		//Eliminar el máximo
		assertEquals("No hay elementos debería retornar null",null, cp1.delMax());
		assertEquals("No hay elementos debería retornar null",null, cp2.delMax());

		//Número de elementos debería ser 0
		assertEquals("No hay elementos debería retornar 0",0, cp1.darNumElementos());
		assertEquals("No hay elementos debería retornar 0",0, cp2.darNumElementos());

	}

	public void testColaVaciaAgregoyEliminoelementos(){
		setUpEscenario0();

		//Agrego elementos a las colas
		LocationVO elemento1 = new LocationVO(815694, "100 BLK MICHIGAN AVE NW E/B", 10);
		LocationVO elemento2 = new LocationVO(814983,"3RD ST TUNNEL NW N/B BY MA AVE",20);
		LocationVO elemento3 = new LocationVO(805065,"2200 BLOCK K ST NW E/B",40);

		cp1.agregar(elemento1);
		cp1.agregar(elemento2);
		cp1.agregar(elemento3);

		cp2.agregar(elemento1);
		cp2.agregar(elemento2);
		cp2.agregar(elemento3);

		//Ahora se verifica que se elimine el máximo correctamente		
		assertEquals("El elemento máximo de MaxCP es el número 3",elemento3, cp1.max());
		assertEquals("El elemento máximo de HeapCP es el número 3",elemento3, cp2.max());
		assertEquals("Debería eliminarse de MaxCP el elemento 3",elemento3, cp1.delMax());
		assertEquals("Debería eliminarse de HepCP el elemento 3",elemento3, cp2.delMax());
		assertEquals("Debería eliminarse de MaxCP el elemento 2",elemento2, cp1.delMax());
		assertEquals("Debería eliminarse de HepCP el elemento 2",elemento2, cp2.delMax());
		assertEquals("Debería eliminarse de MaxCP el elemento 1",elemento1, cp1.delMax());
		assertEquals("Debería eliminarse de HepCP el elemento 1",elemento1, cp2.delMax());


	}

	public void testMuchosElementos(){

		setUpEscenario1();
		//Se verifica que las colas contengan todos los datos
		assertEquals("Debería haber 100 elementos",100, cp1.darNumElementos());
		assertEquals("Debería haber 100 elementos",100, cp2.darNumElementos());


		//Se verifica que estén ordenados las colas
		assertEquals("El elemento máximo no debería ser ese",109, cp1.max().getNumberOfRegisters());
		assertEquals("El elemento máximo no debería ser ese",109, cp2.max().getNumberOfRegisters());

		//Se verifica que se eliminen todos los elementos en orden
		for (int i = 0; i < 100; i++) {
			assertEquals("Los elementos no se están eliminando en orden",(99-i)+10, cp1.delMax().getNumberOfRegisters());
			assertEquals("Los elementos no se están eliminando en orden",(99-i)+10, cp2.delMax().getNumberOfRegisters());
		}

	}


	public void agregarNuevoElementosConMuchosElementos(){

		setUpEscenario1();

		//Insertando un elemento muy grande
		LocationVO auxiliar = new LocationVO(123, "NA", 1000000);
		cp1.agregar(auxiliar);
		cp2.agregar(auxiliar);

		//Debe eliminarse el elemento recién agregado
		assertEquals("El elemento eliminado no debería ser ese",auxiliar, cp1.delMax());
		assertEquals("El elemento eliminado no debería ser ese",auxiliar, cp2.delMax());


		//Insertando un elemento con la misma prioridad
		auxiliar = new LocationVO(123,"NA",109);
		LocationVO auxiliar2 = new LocationVO(123, "NA", 99);
		cp1.agregar(auxiliar);
		cp2.agregar(auxiliar);
		cp1.agregar(auxiliar2);
		cp2.agregar(auxiliar2);

		assertEquals("El elemento eliminado no debería ser ese",109, cp1.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",109, cp1.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",109, cp2.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",109, cp2.delMax().getNumberOfRegisters());


		assertEquals("El elemento eliminado no debería ser ese",99, cp1.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",99, cp1.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",99, cp2.delMax().getNumberOfRegisters());
		assertEquals("El elemento eliminado no debería ser ese",99, cp2.delMax().getNumberOfRegisters());

	}

}
