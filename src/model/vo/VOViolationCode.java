package model.vo;

public class VOViolationCode {
	
	/*
	 * Atributos
	 */
	private String VOCode;
	private double fineAmt;
	
	/*
	 * Constructor
	 */
	public VOViolationCode(String VOCode, double fineAmt) {
		this.VOCode = VOCode;
		this.fineAmt = fineAmt;
	}
	
	/*
	 * Metodos
	 */
	public String getViolationCode() {
		return VOCode;
	}
	
	public double getAvgFineAmt() {
		return fineAmt;
	}
}