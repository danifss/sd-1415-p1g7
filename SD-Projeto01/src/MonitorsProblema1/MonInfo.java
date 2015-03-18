package MonitorsProblema1;

import genclass.GenericIO;
import genclass.TextFile;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonInfo {

    /**
     * CRAFTSMAN STATES
     */
    public final static int
            FETCHING_PRIME_MATERIALS = 0,           // 
            PRODUCING_A_NEW_PIECE = 1,              // 
            STORING_IT_FOR_TRANSFER = 2,            // 
            CONTACTING_THE_ENTREPRENEUR = 3;        // 
    /**
     * CUSTOMER STATES
     */
    public final static int
            CARRYING_OUT_DAILY_CHORES = 0,          // 
            CHECKING_DOOR_OPEN = 1,                 // 
            APPRAISING_OFFER_IN_DISPLAY = 2,        // 
            BUYING_SOME_GOODS = 3;                  // 
    /**
     * OWNER STATES
     */
    public final static int
            OPENING_THE_SHOP = 0,                   // 
            WAITING_FOR_NEXT_TASK = 1,              // 
            ATTENDING_A_CUSTOMER = 2,               // 
            CLOSING_THE_SHOP = 3,                   //
            DELIVERING_PRIME_MATERIALS = 4,         // 
            COLLECTING_A_BATCH_OF_PRODUCTS = 5,     // 
            BUYING_PRIME_MATERIALS = 6;             // 
    /**
     * SHOP STATES
     */
    public final static int
            OPEN = 0,                               // 
            STILL_OPEN = 1,                         // 
            CLOSED = 2;                             // 

    /**
     * Number of Craftsman
     *
     * @serialField nCraftsman
     */
    private int nCraftsman = 0;

    /**
     * Present STATE of Craftsman
     *
     * @serialField stateCraftsman
     */
    private int[] stateCraftsman;

    /**
     * Number of Customers
     *
     * @serialField nCustomer
     */
    private int nCustomer = 0;

    /**
     * Present STATE of Customer
     *
     * @serialField stateCustomer
     */
    private int[] stateCustomer;

    /**
     * Present STATE of Owner
     * 
     * @serialField stateOwner
     */
    private int stateOwner;

    /**
     * Name of the file logging
     *
     * @serialField fName
     */
    private String fName = "log.txt";

    /**
     * Number of iteration of life cycle of the customers
     *
     * @serialField nIter
     */
    private int nIter = 0;

    private int[] nGoods; // Num. bens comprados por cada cliente.
    private int[] nGoodsCrafted; // Num. bens produzidos por cada artesao.
    private int nCustomersInsideShop; // Num. de clientes na loja
    private int nGoodsInDisplay; // Num. de bens a venda.
    private boolean tranfsProductsToShop; // Artesao avisa para tranferirem produtos acabados para a loja.
    private boolean supplyMaterialsToFactory; // Artesao avisa que precisa de materiais no oficina.
    private int nPrimeMaterialsInFactory; // Num. de materias primas na oficina
    private int nFinishedProductsInFactory; // Num. de bens produzidos que estao na oficina
    private int nSuppliedTimes; // Num. vezes que foram fornecidas materias primas para a oficina
	private int nPrimeMaterialsSupplied; // Num. total de materias primas ja fornecidas
	private int nProductsManufactured; // Num. total de bens produzidos pela oficina.
    
    private final int nInitialPrimeMaterialsInStorage;
    private final int nProductsInShop;
    private final int nPrimeMaterialsByProduct;
    private final int nPrimeMaterialsForRestock;
    private final int nLimitOfProductsInFactory;

    /**
     * General Repository for manage all relevant information
     * 
     * @param nCraftsman Number of Craftmans
     * @param nCustomer	Number of Customers
     * @param fName Log file name
     * @param nIter Number of Customers iterations
     * @param nInitialPrimeMaterialsInStorage	
     * @param nPrimeMaterialsInFactory
     * @param nProductsInShop
     * @param nPrimeMaterialsByProduct
     * @param nPrimeMaterialsForRestock
     * @param nLimitOfProductsInFactory 
     */
    public MonInfo(
                int nCraftsman,
                int nCustomer,
                String fName,
                int nIter,
                int nInitialPrimeMaterialsInStorage,
                int nPrimeMaterialsInFactory,
                int nProductsInShop,
                int nPrimeMaterialsByProduct,
                int nPrimeMaterialsForRestock,
                int nLimitOfProductsInFactory   ) {
        if (nCraftsman > 0) 
                this.nCraftsman = nCraftsman;
        if (nCustomer > 0)
                this.nCustomer = nCustomer;
        if (nIter > 0)
                this.nIter = nIter;
        this.nInitialPrimeMaterialsInStorage = nInitialPrimeMaterialsInStorage;
        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
        this.nProductsInShop = nProductsInShop;
        this.nPrimeMaterialsByProduct = nPrimeMaterialsByProduct;
        this.nPrimeMaterialsForRestock = nPrimeMaterialsForRestock;
        this.nLimitOfProductsInFactory = nLimitOfProductsInFactory;
		
		this.nGoods = new int[this.nCustomer];
		this.nGoodsCrafted = new int[this.nCraftsman];
		this.nCustomersInsideShop = 0;
		this.nGoodsInDisplay = 0;
		this.tranfsProductsToShop = false;
		this.supplyMaterialsToFactory = false;
		this.nFinishedProductsInFactory = 0;
		this.nSuppliedTimes = 0;
		this.nPrimeMaterialsSupplied = 0;
		this.nProductsManufactured = 0;
		

        /* Inicializar os estados internos */
        stateCraftsman = new int[this.nCraftsman];			// create array craftsman state
        for (int i = 0; i < this.nCraftsman; i++) {
                stateCraftsman[i] = FETCHING_PRIME_MATERIALS;	// Set initial state of Craftsman
        }
        stateCustomer = new int[this.nCustomer];			// create array customers state
        for (int i = 0; i < this.nCustomer; i++) {
                stateCustomer[i] = CARRYING_OUT_DAILY_CHORES;	// Set initial state of Customer
        }
        this.stateOwner = OPENING_THE_SHOP;					// Set initial state of Owner

        /* inicializar o ficheiro de logging */
        if ((fName != null) && !("".equals(fName))) {
                this.fName = fName;
        }
        reportInitialStatus();
    }

    /**
     * Write initial state (internal operation)
     * 
     */
    private void reportInitialStatus() {
        TextFile log = new TextFile();                  // instanciação de uma variável de tipo ficheiro de texto

        if (!log.openForWriting(".", this.fName)) {
                GenericIO.writelnString("A operação de criação do ficheiro " + this.fName + " falhou!");
                System.exit(1);
        }
        log.writelnString("\t\tAveiro Handicraft SARL - Description of the internal state");
        log.writelnString("\nNúmero de iterações = " + this.nIter + "\n");
        if (!log.close()) {
                GenericIO.writelnString("A operação de fecho do ficheiro " + this.fName + " falhou!");
                System.exit(1);
        }
        log.writelnString("ENTREPRE CUST_0 CUST_1 CUST_2 CRAFT_0 CRAFT_1 CRAFT_2 SHOP WORKSHOP");
        log.writelnString("Stat Stat BP Stat BP Stat BP Stat PP Stat PP Stat PP Stat NCI NPI PCR PMR APMI NPI NSPM TAPM TNP");

        reportStatus();
    }

    /**
     * Write the actual state (internal operation)
     *
     * One line of text about the system is written in the file.
     */
    private void reportStatus() {
        TextFile log = new TextFile();  // instanciação de uma variável de tipo ficheiro de texto

        String lineStatus = "";     // linha a imprimir

        if (!log.openForAppending(".", fName)) {
                GenericIO.writelnString("A operação de criação do ficheiro " + fName + " falhou!");
                System.exit(1);
        }
        for (int i = 0; i < nCraftsman; i++) {
                switch (stateCraftsman[i]) {
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
            switch (stateCustomer[i]) {
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

    /**
     * Set Craftsman[i] state
     * 
     * @param craftsmanId
     * @param state 
     */
    public void setStateCraftsman(int craftsmanId, int state) {
        this.stateCraftsman[craftsmanId] = state;
    }
    /**
     * Set Customer[i] State
     * 
     * @param customerId
     * @param state 
     */
    public void setStateCustomer(int customerId, int state) {
        this.stateCustomer[customerId] = state;
    }
    /**
     * Set Owner State
     * @param state 
     */
    public void setStateOwner(int state) {
        this.stateOwner = state;
    }
    /**
     * @return Number of Craftmans
     */
    public int getnCraftsman() {
        return nCraftsman;
    }
    /**
     * @return State of Craftman[i]
     * 
     * @param i Number of the Craftman
     */
    public int getStateCraftsman(int i) {
        return stateCraftsman[i];
    }
    /**
     * @return Number of Customers
     */
    public int getnCustomer() {
        return nCustomer;
    }
    /**
     * @return State of Customer[i]
     * 
     * @param i Number of the Customer
     */
    public int getStateCustomer(int i) {
        return stateCustomer[i];
    }
    /**
     * @return State of Owner
     */
    public int getStateOwner() {
        return stateOwner;
    }
}
