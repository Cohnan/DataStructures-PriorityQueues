package model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Representation of a Trip object
 */
public class VOMovingViolation {
	
	public static final String[] EXPECTEDHEADERS = new String[] {"OBJECTID", "ROW_", "LOCATION", "ADDRESS_ID", "STREETSEGID", "XCOORD", "YCOORD", "TICKETTYPE", "FINEAMT", "TOTALPAID", "PENALTY1", "PENALTY2", "ACCIDENTINDICATOR", "AGENCYID", "TICKETISSUEDATE", "VIOLATIONCODE", "VIOLATIONDESC", "ROW_ID"};
	// Estos son los indice de los textos en EXPECTEDHEADERS
		public static final int OBJECTID = 0;
		public static final int ROW_= 1;
		public static final int LOCATION = 2;
		public static final int ADDRESS_ID = 3;
		public static final int STREETSEGID = 4;
		public static final int XCOORD = 5;
		public static final int YCOORD = 6;
		public static final int TICKETTYPE = 7;
		public static final int FINEAMT = 8;
		public static final int TOTALPAID = 9;
		public static final int PENALTY1 = 10;
		public static final int PENALTY2 = 11;
		public static final int ACCIDENTINDICATOR = 12;
		public static final int AGENCYID = 13;
		public static final int TICKETISSUEDATE = 14;
		public static final int VIOLATIONCODE = 15;
		public static final int VIOLATIONDESC = 16;	
		public static final int ROW_ID = 17;
	
	/**
	 * Atributos de la infracci�n
	 */
	private String iD; 
	private String location;
	private int addressID;
	private int streetsegID;
	private double totalPaid;
	private double penalty1;
	private double penalty2;
	private boolean accidentIndicator;
	private LocalDateTime ticketIssueDate;
	private String violationCode;
	private String violationDesc;
	private int fineAmount;


	/**
	 * Constructor. Recibe los argumentos de la infracci�n a trav�s del archivo CSV
	 */
	public VOMovingViolation(int[] headerPositions, String[] linea){
		String campo;
		
		iD = linea[0];  //linea[headerPositions[OBJECTID]];
		
		location = linea[headerPositions[LOCATION]];
		
		
		// Si se considera ADDRESS_ID como int:
		campo = linea[headerPositions[ADDRESS_ID]];
		if (!campo.equals("")) addressID = Integer.parseInt(campo);
		else addressID = -1;
		// Si se considera ADDRESS_ID como String
		//addressID = linea[headerPositions[ADDRESS_ID]];

		campo = linea[headerPositions[STREETSEGID]];
		if (!campo.equals("")) streetsegID = Integer.parseInt(campo);
		else streetsegID = -1;
		
		totalPaid = Double.parseDouble(linea[headerPositions[TOTALPAID]]);
		
		campo = linea[headerPositions[PENALTY1]];
		if (!campo.equals("")) penalty1 = Double.parseDouble(campo);
		else penalty1 = 0;
		
		campo = linea[headerPositions[PENALTY2]];
		if (!campo.equals("")) penalty2 = Double.parseDouble(campo);
		else penalty2 = 0;
		
		campo = linea[headerPositions[ACCIDENTINDICATOR]];
		if 		(campo.equalsIgnoreCase("Yes")) accidentIndicator = true;
		else if (campo.equalsIgnoreCase("No"))	accidentIndicator = false;
		else throw new IllegalArgumentException("El indicador de accidente no tiene un valor reconocible.");
		
		campo = linea[headerPositions[TICKETISSUEDATE]];
		ticketIssueDate = LocalDateTime.parse(campo, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'"));
		
		violationCode = linea[headerPositions[VIOLATIONCODE]];
		
		violationDesc = linea[headerPositions[VIOLATIONDESC]];
		
		fineAmount = Integer.parseInt(linea[headerPositions[FINEAMT]]);
	}

	/**
	 * @return id - Identificador único de la infracción
	 */
	public String objectId() {
		return iD;
	}	

	/**
	 * @return location - Direcci�n en formato de texto.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * @return addressID
	 */
	public int getAddressID() {
		return addressID;
	}
	
	/**
	 * @return streetsgeID
	 */
	public int getStreetsegID() {
		return streetsegID;
	}

	/**
	 * @return date - Fecha cuando se puso la infracción .
	 */
	public LocalDateTime getTicketIssueDate() {
		return ticketIssueDate;
	}

	/**
	 * @return totalPaid - Cuanto dinero efectivamente pag� el que recibi� la infracci�n en USD.
	 */
	public double getTotalPaid() {
		return totalPaid;
	}
	
	/**
	 * @return penalty1 - 
	 */
	public double getPenalty1() {
		return penalty1;
	}
	
	/**
	 * @return penalty2 - 
	 */
	public double getPenalty2() {
		return penalty2;
	}

	/**
	 * @return accidentIndicator - Si hubo un accidente o no.
	 */
	public boolean getAccidentIndicator() {
		return accidentIndicator;
	}

	/**
	 * @return description - Descripci�n textual de la infracción.
	 */
	public String  getViolationDescription() {
		return violationDesc;
	}

	public String  getViolationCode() {
		return violationCode;
	}

	public int getFineAmount() {
		return fineAmount;
	}
	
	/**
	 * For comparations
	 */
	
	public static class ObjectIDOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation arg0, VOMovingViolation arg1) {
			
			return arg0.objectId().compareTo(arg1.objectId());
		}
		
	}
	
	public static class TicketIssueOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation inf1, VOMovingViolation inf2) {
			
			return inf1.getTicketIssueDate().compareTo(inf2.getTicketIssueDate());
		}
		
	}
	
	public static class TimeOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation arg0, VOMovingViolation arg1) {
			
		if(arg0.getTicketIssueDate().getHour()>arg1.getTicketIssueDate().getHour()) return 1;
		else if(arg0.getTicketIssueDate().getHour()>arg1.getTicketIssueDate().getHour()) return -1;	
		else return 0;
		}
		
	}
	
	public static class ViolationCodeOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation inf1, VOMovingViolation inf2) {
			return inf1.getViolationCode().compareTo(inf2.getViolationCode());
		}
		
	}
	
	public static class StreetsgeIDDateOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation arg0, VOMovingViolation arg1) {
			
			if(arg0.getStreetsegID()>arg1.getStreetsegID()) return 1;
			else if(arg0.getStreetsegID()<arg1.getStreetsegID()) return -1;
			else if(arg0.getTicketIssueDate().compareTo(arg1.getTicketIssueDate())>0) return 1;
			else if(arg0.getTicketIssueDate().compareTo(arg1.getTicketIssueDate())<0) return -1;
			else return 0;
		}
		
	}

	public static class ViolationDescOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation inf1, VOMovingViolation inf2) {
			return inf1.getViolationDescription().compareTo(inf2.getViolationDescription());
		}
		
	}
	public static class AddressIDOrder implements Comparator<VOMovingViolation> {

		@Override
		public int compare(VOMovingViolation arg0, VOMovingViolation arg1) {
			return arg0.getAddressID() - arg1.getAddressID();
		}
	}
}