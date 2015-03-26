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
            CLOSED = 0,                           // 
            STILL_OPEN = 1,                       // 
            OPEN = 2;                             // 

    /**
     * Number of Craftsman
     *
     * @serialField nCraftsman
     */
    private int nCraftsman;

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
     * Present STATE of Shop
     * 
     * @serialField stateShop
     */
    private int stateShop;

    /**
     * Name of the file logging
     *
     * @serialField fName
     */
    private String fName = "log.txt";

    /**
     * Craftman needed information
     * @serialField stateCraftman State of the Craftman
     * @serialField nGoodsCraftedByCraftman Number of goods produced by each Craftman
     */
    private int[] stateCraftman;
    private int[] nGoodsCraftedByCraftman;

    /**
     * Workshop needed information
     * @serialField nPrimeMaterialsInFactory Amount of prime materials presently in the workshop
     * @serialField nFinishedProductsInFactory Number of finished products presently in the workshop
     * @serialField nSuppliedTimes Number of times that a supply of prime materials was delivered to the workshop
     * @serialField nPrimeMaterialsSupplied total amount of prime materials that have already been supplied (accumulation)
     * @serialField nProductsManufactured total number of products that have already been manufactured (accumulation)
     */
    private int nPrimeMaterialsInFactory;
    private int nFinishedProductsInFactory;
    private int nSuppliedTimes;
    private int nPrimeMaterialsSupplied;
    private int nProductsManufactured;
    
    
    
    // Tratamento de bens e materias primas
    private int[] nGoodsByCustomer; // Num. bens comprados por cada cliente.


    
    // Variáveis da Loja
    private int nCustomersInsideShop; // Num. de clientes na loja
    private int nGoodsInDisplay; // Num. de bens a venda.
    
    private boolean tranfsProductsToShop; // Artesao avisa para tranferirem produtos acabados para a loja.
    private boolean supplyMaterialsToFactory; // Artesao avisa que precisa de materiais no oficina.

    /**
     * General Repository for manage all relevant information
     * 
     * @param nCraftsman Number of Craftmans
     * @param nCustomer	Number of Customers
     * @param fName Log file name
     * @param nPrimeMaterialsInFactory
     */
    public MonInfo(int nCraftsman, int nCustomer, String fName, int nPrimeMaterialsInFactory) {
        this.nCraftsman = nCraftsman;
        this.nCustomer = nCustomer;

		
        this.nGoodsByCustomer = new int[this.nCustomer];
        for(int elem: this.nGoodsByCustomer) elem = 0;
        this.nGoodsCraftedByCraftman = new int[this.nCraftsman];
        for(int elem: this.nGoodsCraftedByCraftman) elem = 0;

        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
        this.nCustomersInsideShop = 0;
        this.nGoodsInDisplay = 0;
        this.tranfsProductsToShop = false;
        this.supplyMaterialsToFactory = false;
        this.nFinishedProductsInFactory = 0;
        this.nSuppliedTimes = 0;
        this.nPrimeMaterialsSupplied = 0;
        this.nProductsManufactured = 0;


        /* Inicializar os estados internos */
        this.stateCraftman = new int[this.nCraftsman];			// create array craftsman state
        for(int craftman: this.stateCraftman)
            craftman = FETCHING_PRIME_MATERIALS;		// Set initial state of Craftsman
        
        this.stateCustomer = new int[this.nCustomer];			// create array customers state
        for(int customer: this.stateCustomer)
            customer = CARRYING_OUT_DAILY_CHORES;		// Set initial state of Customer
        
        this.stateShop = CLOSED;							// Set initial state of Shop
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
        TextFile log = new TextFile();                  // instanciacao de uma variavel de tipo ficheiro de texto

        if (!log.openForWriting(".", this.fName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + this.fName + " falhou!");
            System.exit(1);
        }
        log.writelnString("        Aveiro Handicraft SARL - Description of the internal state\n");
        log.writelnString("ENTREPRE  CUST_0  CUST_1  CUST_2   CRAFT_0 CRAFT_1 CRAFT_2          SHOP                WORKSHOP");
        log.writelnString("  Stat   Stat BP Stat BP Stat BP   Stat PP Stat PP Stat PP  Stat NCI NPI PCR PMR APMI NPI NSPM TAPM TNP");
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + this.fName + " falhou!");
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Write the actual state (internal operation)
     *
     * One line of text about the system is written in the file.
     */
    private void reportStatus() {
        TextFile log = new TextFile();  // instanciação de uma variavel de tipo ficheiro de texto

        String lineStatus = "";     // linha a imprimir

        if (!log.openForAppending(".", fName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fName + " falhou!");
            System.exit(1);
        }
        for (int i = 0; i < nCustomer; i++) {
            switch (stateCustomer[i]) {
                case CARRYING_OUT_DAILY_CHORES:
                    lineStatus += " CODC ";
                    break;
                case CHECKING_DOOR_OPEN:
                    lineStatus += " CDO  ";
                    break;
                case APPRAISING_OFFER_IN_DISPLAY:
                    lineStatus += " AOID ";
                    break;
                case BUYING_SOME_GOODS:
                    lineStatus += " BSG  ";
                    break;
            }
            lineStatus += " " + nGoodsByCustomer[i] + " ";
        }
        for (int i = 0; i < nCraftsman; i++) {
            switch (stateCraftman[i]) {
                case FETCHING_PRIME_MATERIALS:
                    lineStatus += " FPM  ";
                    break;
                case PRODUCING_A_NEW_PIECE:
                    lineStatus += " PANP ";
                    break;
                case STORING_IT_FOR_TRANSFER:
                    lineStatus += " SIFT ";
                    break;
                case CONTACTING_THE_ENTREPRENEUR:
                    lineStatus += " CTE  ";
                    break;
            }
            lineStatus += " " + nGoodsCraftedByCraftman[i] + " ";
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
    public synchronized void setStateCraftsman(int craftsmanId, int state) {
        this.stateCraftman[craftsmanId] = state;
        reportStatus();
    }
    
    /**
     * Set Customer[i] State
     * 
     * @param customerId
     * @param state 
     */
    public synchronized void setCustomerState(int customerId, int state) {
        this.stateCustomer[customerId] = state;
        reportStatus();
    }
    
    /**
     * Set Owner State
     * @param state 
     */
    public synchronized void setOwnerState(int state) {
        this.stateOwner = state;
        reportStatus();
    }
    
    /**
     * Set Shop State
     * @param state 
     */
    public synchronized void setShopState(int state){
        this.stateShop = state;
        reportStatus();
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
        return stateCraftman[i];
    }
    
    /**
     * @return Number of Customers
     */
    public int getnCustomer() {
        return nCustomer;
    }
    
    /**
     * @param i Number of the Customer
     * @return State of Customer[i]
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
    
    /**
     * @return State of Shop
     */
    public int getStateShop(){
        return stateShop;
    }

    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }
    
    // Num. bens comprados por cada cliente.
    public synchronized void incrementnGoodsByCustomer(int customerId, int nGoods) {
        this.nGoodsByCustomer[customerId] += nGoods;
        reportStatus();
    }

    // Num. bens produzidos por cada artesao.
    public synchronized void incrementnGoodsCraftedByCraftman(int craftsmanId) {
        this.nGoodsCraftedByCraftman[craftsmanId]++;
    }
    
    // Num. de clientes na loja
    public synchronized void setnCustomersInsideShop(int nCustomersInsideShop) {
        this.nCustomersInsideShop += nCustomersInsideShop;
        reportStatus();
    }
    
    // Num. de bens a venda.
    public synchronized void setnGoodsInDisplay(int nGoodsInDisplay) {
        this.nGoodsInDisplay += nGoodsInDisplay;
        reportStatus();
    }
    
    // Artesao avisa para tranferirem produtos acabados para a loja.
    public synchronized void setTranfsProductsToShop(boolean tranfsProductsToShop) {
        this.tranfsProductsToShop = tranfsProductsToShop;
        reportStatus();
    }
    
    // Artesao avisa que precisa de materiais no oficina.
    public synchronized void setSupplyMaterialsToFactory(boolean supplyMaterialsToFactory) {
        this.supplyMaterialsToFactory = supplyMaterialsToFactory;
        reportStatus();
    }
    
    // Num. de bens produzidos que estao na oficina
    public synchronized void setnFinishedProductsInFactory(int nFinishedProductsInFactory) {
        this.nFinishedProductsInFactory += nFinishedProductsInFactory;
        reportStatus();
    }
    
    // Num. vezes que foram fornecidas materias primas para a oficina
    public synchronized void setnSuppliedTimes(int nSuppliedTimes) {
        this.nSuppliedTimes += nSuppliedTimes;
        reportStatus();
    }
    
    // Num. total de materias primas ja fornecidas
    public synchronized void setnPrimeMaterialsSupplied(int nPrimeMaterialsSupplied) {
        this.nPrimeMaterialsSupplied += nPrimeMaterialsSupplied;
        reportStatus();
    }
    
    // Num. total de bens produzidos pela oficina.
    public synchronized void setnProductsManufactured(int nProductsManufactured) {
        this.nProductsManufactured += nProductsManufactured;
        reportStatus();
    }

	public boolean isToTranfsProductsToShop() {
		return tranfsProductsToShop;
	}

	public boolean isToSupplyMaterialsToFactory() {
		return supplyMaterialsToFactory;
        }
        
    /**
     * Change the number of prime materials available in the factory
     * @param nPrimeMaterialsInFactory 
     */
    public synchronized void setnPrimeMaterialsInFactory(int nPrimeMaterialsInFactory) {
        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
    }
}
