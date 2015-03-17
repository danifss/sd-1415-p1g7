package MonitorsProblema1;

import MainProblema01.*;
import genclass.GenericIO;
import genclass.TextFile;

/**
 *
 * @author Daniel
 */
public class MonInfo {

	/**
	 * CRAFTSMAN STATES
	 */
	private final static int FETCHING_PRIME_MATERIALS = 0,		// 
			PRODUCING_A_NEW_PIECE = 1,							// 
			STORING_IT_FOR_TRANSFER = 2,						// 
			CONTACTING_THE_ENTREPRENEUR = 3;					// 
	/**
	 * CUSTOMER STATES
	 */
	private final static int CARRYING_OUT_DAILY_CHORES = 0,		// 
			CHECKING_DOOR_OPEN = 1,								// 
			APPRAISING_OFFER_IN_DISPLAY = 2,					// 
			BUYING_SOME_GOODS = 3;								// 

	/**
	 * Number of Craftsman
	 *
	 * @serialField nCraftsman
	 */
	private int nCraftsman;

	/**
	 * Present STATE of Craftsman
	 *
	 * @serialField StateCraftsman
	 */
	private int[] StateCraftsman;

	/**
	 * Number of Customers
	 *
	 * @serialField nCustomer
	 */
	private int nCustomer;

	/**
	 * Present STATE of Customer
	 *
	 * @serialField StateCustomer
	 */
	private int[] StateCustomer;

	/**
	 * Name of the file logging
	 *
	 * @serialField fName
	 */
	private String fName = "log.txt";

	/**
	 * Number of Shops
	 *
	 * @serialField nShops
	 */
	private int nShops;

	/**
	 * Number of Owners
	 *
	 * @serialField nOwners
	 */
	private int nOwners;

	/**
	 * Number of iteration of life cycle of the customers
	 *
	 * @serialField nIter
	 */
	private int nIter = 0;

	public MonInfo(int nCraftsman, int nCustomer, int nShops, int nOwners, String fName, int nIter) {
		if (nCraftsman > 0)
			this.nCraftsman = nCraftsman;
		if (nCustomer > 0)
			this.nCustomer = nCustomer;
		if (nShops > 0)
			this.nShops = nShops;
		if (nOwners > 0)
			this.nOwners = nOwners;

		/* Inicializar os estados internos */
		StateCraftsman = new int[this.nCraftsman]; // create array craftsman state
		for (int i = 0; i < this.nCraftsman; i++) {
			StateCraftsman[i] = FETCHING_PRIME_MATERIALS; // Set initial state
		}
		StateCustomer = new int[this.nCustomer]; // create array customers state
		for (int i = 0; i < this.nCustomer; i++) {
			StateCustomer[i] = CARRYING_OUT_DAILY_CHORES; // Set initial state
		}
		
		/* inicializar o ficheiro de logging */
		if ((fName != null) && !("".equals(fName))) {
			this.fName = fName;
		}
		reportInitialStatus();
	}

	/**
	 * Escrever o estado inicial (operação interna).
	 * <p>
	 * Os barbeiros estão a dormir e os clientes a realizar as tarefas do dia a
	 * dia.
	 */
	private void reportInitialStatus() {
		TextFile log = new TextFile();                  // instanciação de uma variável de tipo ficheiro de texto

		if (!log.openForWriting(".", fName)) {
			GenericIO.writelnString("A operação de criação do ficheiro " + fName + " falhou!");
			System.exit(1);
		}
		log.writelnString("\t\tAveiro Handicraft SARL - Description of the internal state");
		log.writelnString("\nNúmero de iterações = " + nIter + "\n");
		if (!log.close()) {
			GenericIO.writelnString("A operação de fecho do ficheiro " + fName + " falhou!");
			System.exit(1);
		}
		log.writelnString("ENTREPRE CUST_0 CUST_1 CUST_2 CRAFT_0 CRAFT_1 CRAFT_2 SHOP WORKSHOP");
		log.writelnString("Stat Stat BP Stat BP Stat BP Stat PP Stat PP Stat PP Stat NCI NPI PCR PMR APMI NPI NSPM TAPM TNP");

		reportStatus();
	}

	/**
	 * Write the actual state (internal opration)
	 * <p>
	 * One line of text about the system is written in the file.
	 */
	private void reportStatus() {
		TextFile log = new TextFile();                      // instanciação de uma variável de tipo ficheiro de texto

		String lineStatus = "";                              // linha a imprimir

		if (!log.openForAppending(".", fName)) {
			GenericIO.writelnString("A operação de criação do ficheiro " + fName + " falhou!");
			System.exit(1);
		}
		for (int i = 0; i < nCraftsman; i++) {
			switch (StateCraftsman[i]) {
				case FETCHING_PRIME_MATERIALS:
					lineStatus += "  ";
					break;
				case PRODUCING_A_NEW_PIECE:
					lineStatus += "  ";
					break;
				case STORING_IT_FOR_TRANSFER:
					lineStatus += "  ";
					break;
				case CONTACTING_THE_ENTREPRENEUR:
					lineStatus += "  ";
					break;
			}
		}
		for (int i = 0; i < nCustomer; i++) {
			switch (StateCustomer[i]) {
				case CARRYING_OUT_DAILY_CHORES:
					lineStatus += "  ";
					break;
				case CHECKING_DOOR_OPEN:
					lineStatus += "  ";
					break;
				case APPRAISING_OFFER_IN_DISPLAY:
					lineStatus += "  ";
					break;
				case BUYING_SOME_GOODS:
					lineStatus += "  ";
					break;
			}
		}
		log.writelnString(lineStatus);
		if (!log.close()) {
			GenericIO.writelnString("A operação de fecho do ficheiro " + fName + " falhou!");
			System.exit(1);
		}
	}
}
