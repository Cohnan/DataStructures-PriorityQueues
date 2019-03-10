package model.data_structures;

import java.util.Iterator;

public class MaxHeapCP <T extends Comparable<T>> implements IColaPrioridad<T>{

	
private ArregloDinamico<T> cp;
	
	
	public MaxHeapCP(){
		cp = new ArregloDinamico<T>();
		cp.agregar(null);
	}
	
	
	
	@Override
	public Iterator<T> iterator() {
		return cp.iterator();
	}

	@Override
	public boolean esVacia() {
		if(cp.darTamano() == 1) return true;
		return false;
	}

	@Override
	public int darNumElementos() {
		return cp.darTamano()-1;
	}

	public void agregar(T t){
		cp.agregar(t);
		swim(darNumElementos());
	}

	public T delMax() {
	

		if(darNumElementos() ==0) return null;
		T max = cp.darObjeto(1);
		exch(1,darNumElementos());
		cp.eliminarEnPos(darNumElementos());
		sink(1);
		
		return max;
	}

	@Override
	public T max() {
		// TODO Auto-generated method stub
		return cp.darObjeto(1);
	}
	
	private void swim (int k){
		
		while(k>1 && less(k/2,k)){
			exch(k,k/2);
			k = k/2;
		}
		
	}
	
	private void sink(int k){
		
		int N = darNumElementos();
		while(2*k<=N){
			int j = 2*k;
			if(j<N && less(j,j+1)) j++;
			if(!less(k,j)) break;
			exch(k,j);
			k = j;
		}
	}
	
	private boolean less (int i, int j){
		return cp.darObjeto(i).compareTo(cp.darObjeto(j))<0;
	}
	
	private void exch(int i, int j){
	T auxiliar = cp.darObjeto(i);
	cp.cambiarEnPos(i, cp.darObjeto(j));	
	cp.cambiarEnPos(j, auxiliar);
	}


	
	
}
