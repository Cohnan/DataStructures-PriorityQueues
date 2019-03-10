package model.vo;

public class LocationVO implements Comparable<LocationVO> {
	/*
	 * Atributos
	 */
	
	private int addressID;
	private String location;
	private int numberOfRegisters; //Numero de veces que aparece el addressID en los registros cargados
	
	/*
	 * Constructor
	 */
	public LocationVO(int addressID, String location, int currentNumber) {
		this.addressID = addressID;
		this.location = location;
		this.numberOfRegisters = currentNumber;
	}
	
	/*
	 * Metodos
	 */
	public int compareTo(LocationVO loc2) {
		int comparation = numberOfRegisters - loc2.getNumberOfRegisters(); 
		if(comparation != 0) return comparation;
		return location.compareTo(loc2.getLocation());
	}
	
	public int changeNumberOrRegs(int newNumber) {
		return this.numberOfRegisters = newNumber;
	}
	
	public int getAddressID() {
		return addressID;
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getNumberOfRegisters() {
		return numberOfRegisters;
	}
	
	
	public String toString()
	{
		return "LocationVO "+addressID+" Regs: " + numberOfRegisters;
	}
}