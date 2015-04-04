package MonitorsProblema1;

import genclass.GenericIO;
import genclass.TextFile;

/**
 * This class is responsible to host the Craftmans.
 * 
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonInfo implements MonInfoInterface {

    /**
     * Owner States.
     */
    private final static int
            OPENING_THE_SHOP = 0,
            WAITING_FOR_NEXT_TASK = 1,
            ATTENDING_A_CUSTOMER = 2,
            CLOSING_THE_SHOP = 3,
            DELIVERING_PRIME_MATERIALS = 4,
            COLLECTING_A_BATCH_OF_PRODUCTS = 5,
            BUYING_PRIME_MATERIALS = 6;
    
    /**
     * Customer States.
     */
    private final static int
            CARRYING_OUT_DAILY_CHORES = 0,
            CHECKING_DOOR_OPEN = 1,
            APPRAISING_OFFER_IN_DISPLAY = 2,
            BUYING_SOME_GOODS = 3;
    
    /**
     * Craftman States.
     */
    private final static int
            FETCHING_PRIME_MATERIALS = 0,
            PRODUCING_A_NEW_PIECE = 1,
            STORING_IT_FOR_TRANSFER = 2,
            CONTACTING_THE_ENTREPRENEUR = 3;
    
    /**
     * Shop States.
     */
    private final static int
            CLOSED = 0,
            STILL_OPEN = 1,
            OPEN = 2;
    
    /**
     * Owner needed information.
     * @serialField stateOwner state of the Owner
     */
    private int stateOwner;
    
    /**
     * Customers needed information.
     * @serialField nCustomer Number of Customers
     * @serialField stateCustomer State of the customer
     * @serialField nGoodsByCustomer Number of goods (accumulation) bought by the customer
     */
    private final int nCustomer;
    private int[] stateCustomer;
    private int[] nGoodsByCustomer;
    
    /**
     * Craftman needed information.
     * @serialField nCraftsman Number of Craftsman
     * @serialField stateCraftman State of the Craftman
     * @serialField nGoodsCraftedByCraftman Number of goods produced by each Craftman
     */
    private final int nCraftman;
    private int[] stateCraftman;
    private int[] nGoodsCraftedByCraftman;

    /**
     * Shop needed information.
     * @serialField stateShop State of the shop
     * @serialField nCustomersInsideShop Number of customers inside
     * @serialField nGoodsInDisplay Number of goods in display
     * @serialField transfProductsToShop A phone call was made by a craftsman requesting the transfer of finished products to the shop
     * @serialField supplyMaterialsToFactory A phone call was made by a craftsman requesting the supply of prime materials to the workshop
     */
    private int stateShop;
    private int nCustomersInsideShop;
    private int nGoodsInDisplay;
    private boolean transfProductsToShop;
    private boolean supplyMaterialsToFactory;
    
    /**
     * Workshop needed information.
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
    
    /**
     * Name of the logging file.
     * @serialField fName
     */
    private String fName = "log.txt";


    /**
     * General Repository for manage of all relevant information.
     * 
     * @param nCraftsman Number of Craftmans
     * @param nCustomer	Number of Customers
     * @param fName Log file name
     * @param nPrimeMaterialsInFactory Initial number of prime materials in factory
     */
    public MonInfo(int nCraftsman, int nCustomer, String fName, int nPrimeMaterialsInFactory) {
        // Initialization of the Craftman's variables
        this.nCraftman = nCraftsman;
        stateCraftman = new int[this.nCraftman];
        for(int i=0;i<this.nCraftman; i++)
            stateCraftman[i] = FETCHING_PRIME_MATERIALS;	
        nGoodsCraftedByCraftman = new int[this.nCraftman];
        for(int i=0;i<this.nCraftman; i++) 
            nGoodsCraftedByCraftman[i] = 0;
        
        // Initialization of the Factory's variables
        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
        nFinishedProductsInFactory = 0;
        nSuppliedTimes = 0;
        nPrimeMaterialsSupplied = 0;
        nProductsManufactured = 0;
        
        // Initialization of the Customer's variables
        this.nCustomer = nCustomer;
        this.stateCustomer = new int[this.nCustomer];
        for(int i=0;i<this.nCustomer; i++)
            stateCustomer[i] = CARRYING_OUT_DAILY_CHORES;
        this.nGoodsByCustomer = new int[this.nCustomer];
        for(int i=0;i<this.nCustomer; i++) 
            nGoodsByCustomer[i] = 0;
        
        // Initialization of the Owner's variables
        this.stateOwner = OPENING_THE_SHOP;
        
        // Initialization of the Shop's variables
        this.stateShop = CLOSED;
        this.nCustomersInsideShop = 0;
        this.nGoodsInDisplay = 0;
        this.transfProductsToShop = false;
        this.supplyMaterialsToFactory = false;

        // Initialization of the logging file
        if ((fName != null) && !("".equals(fName))) {
            this.fName = fName;
        }
        
        reportInitialStatus();
    }

    /**
     * Write initial state (internal operation).
     */
    private void reportInitialStatus() {
        TextFile log = new TextFile();                  // instanciacao de uma variavel de tipo ficheiro de texto

        if (!log.openForWriting(".", this.fName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + this.fName + " falhou!");
            System.exit(1);
        }
        log.writelnString("        Aveiro Handicraft SARL - Description of the internal state\n");
        
        String line1 = "ENTREPRE";
        String line2 = "  Stat  ";
        for(int i=0;i<nCustomer;i++){
            line1 += String.format("%10s","CUST_"+i);
            line2 += String.format("%10s", "Stat BP");
        }
        line1 += "  ";
        line2 += "  ";
        for(int i=0;i<nCraftman;i++){
            line1 += String.format("%11s","CRAFT_"+i);
            line2 += String.format("%11s","Stat PP");
        }
        line1 += "          SHOP                WORKSHOP";
        line2 += "  Stat NCI NPI PCR PMR  APMI NPI NSPM TAPM TNP";
        log.writelnString(line1);
        log.writelnString(line2);
        
        //log.writelnString("ENTREPRE  CUST_0  CUST_1  CUST_2   CRAFT_0 CRAFT_1 CRAFT_2          SHOP                WORKSHOP");
        //log.writelnString("  Stat   Stat BP Stat BP Stat BP   Stat PP Stat PP Stat PP  Stat NCI NPI PCR PMR  APMI NPI NSPM TAPM TNP");
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + this.fName + " falhou!");
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Write the actual state (internal operation).
     * One line of text about the system is written in the file.
     */
    private void reportStatus() {
        TextFile log = new TextFile();

        String lineStatus = "";

        if (!log.openForAppending(".", fName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fName + " falhou!");
            System.exit(1);
        }
     
        switch(stateOwner){
            case OPENING_THE_SHOP:
                lineStatus += String.format("%6s", "OTS");
                break;
            case WAITING_FOR_NEXT_TASK:
                lineStatus += String.format("%6s", "WFNT");
                break;
            case ATTENDING_A_CUSTOMER:
                lineStatus += String.format("%6s", "AAC");
                break;
            case CLOSING_THE_SHOP:
                lineStatus += String.format("%6s", "CTS");
                break;
            case DELIVERING_PRIME_MATERIALS:
                lineStatus += String.format("%6s", "DPM");
                break;
            case COLLECTING_A_BATCH_OF_PRODUCTS:
                lineStatus += String.format("%6s", "CBOP");
                break;
            case BUYING_PRIME_MATERIALS:
                lineStatus += String.format("%6s", "BPM");
                break;
        }
        lineStatus += "   ";
        for (int i = 0; i < nCustomer; i++) {
            switch (stateCustomer[i]) {
                case CARRYING_OUT_DAILY_CHORES:
                    lineStatus += String.format("%6s", "CODC");
                    break;
                case CHECKING_DOOR_OPEN:
                    lineStatus += String.format("%6s", "CDO");
                    break;
                case APPRAISING_OFFER_IN_DISPLAY:
                    lineStatus += String.format("%6s", "AOID");
                    break;
                case BUYING_SOME_GOODS:
                    lineStatus += String.format("%6s", "BSG");
                    break;
            }
            lineStatus += " ";
            lineStatus += String.format("%2d", nGoodsByCustomer[i]);
            lineStatus += " ";
        }
        lineStatus += "  ";
        for (int i = 0; i < nCraftman; i++) {
            switch (stateCraftman[i]) {
                case FETCHING_PRIME_MATERIALS:
                    lineStatus += String.format("%7s", "FPM");
                    break;
                case PRODUCING_A_NEW_PIECE:
                    lineStatus += String.format("%7s", "PANP");
                    break;
                case STORING_IT_FOR_TRANSFER:
                    lineStatus += String.format("%7s", "SIFT");
                    break;
                case CONTACTING_THE_ENTREPRENEUR:
                    lineStatus += String.format("%7s", "CTE");
                    break;
            }
            lineStatus += " ";
            lineStatus += String.format("%2d", nGoodsCraftedByCraftman[i]);
            lineStatus += " ";
        }
        lineStatus += " ";
        switch(stateShop){
            case CLOSED:
                lineStatus += String.format("%4s", "CLOS");
                break;
            case STILL_OPEN:
                lineStatus += String.format("%4s", "STIL");
                break;
            case OPEN:
                lineStatus += String.format("%4s", "OPEN");
                break;
        }
        lineStatus += " ";
        lineStatus += String.format("%3d", nCustomersInsideShop);
        lineStatus += " ";
        lineStatus += String.format("%3d", nGoodsInDisplay);
        lineStatus += " ";
        if(transfProductsToShop)
            lineStatus += String.format("%3s", "T");
        else
            lineStatus += String.format("%3s", "F");
        lineStatus += " ";
        if(supplyMaterialsToFactory)
            lineStatus += String.format("%3s", "T");
        else
            lineStatus += String.format("%3s", "F");
        lineStatus += "  ";
        lineStatus += String.format("%4d", nPrimeMaterialsInFactory);
        lineStatus += " ";
        lineStatus += String.format("%3d", nFinishedProductsInFactory);
        lineStatus += " ";
        lineStatus += String.format("%4d", nSuppliedTimes);
        lineStatus += " ";
        lineStatus += String.format("%4d", nPrimeMaterialsSupplied);
        lineStatus += " ";
        lineStatus += String.format("%3d", nProductsManufactured);

        
        log.writelnString(lineStatus);
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fName + " falhou!");
            System.exit(1);
        }
    }
    
    
    
    // Function to change the Owner's variable
    /**
     * Set Owner State.
     * @param state State of the Owner
     */
    @Override
    public synchronized void setOwnerState(int state) {
        this.stateOwner = state;
        reportStatus();
    }
    
    
    
    // Function to change the Customers' variables
    /**
     * Set Customer[i] State.
     * @param customerId Customer id
     * @param state State of the Customer
     */
    @Override
    public synchronized void setCustomerState(int customerId, int state) {
        this.stateCustomer[customerId] = state;
        reportStatus();
    }
    
    /**
     * Set number of goods (accumulation) bought by the customer.
     * @param customerId Customer id
     * @param nGoodsByCustomer Number of goods bought by the customer
     */
    @Override
    public synchronized void setnGoodsByCustomer(int customerId, int nGoodsByCustomer) {
        this.nGoodsByCustomer[customerId] = nGoodsByCustomer;
    }
    
    
    
    // Function to change the Craftmans' variables
    /**
     * Set state of the Craftman[i].
     * @param craftmanId Craftman id
     * @param state State of the Craftman
     */
    @Override
    public synchronized void setCraftmanState(int craftmanId, int state) {
        this.stateCraftman[craftmanId] = state;
        reportStatus();
    }
    
    /**
     * Change the number of products (accumulation) manufactured by the craftsman[i].
     * @param craftmanId Craftman id
     * @param nGoodsCraftedByCraftman total number of the products crafted by the Craftman
     */
    @Override
    public synchronized void setnGoodsCraftedByCraftman(int craftmanId, int nGoodsCraftedByCraftman){
        this.nGoodsCraftedByCraftman[craftmanId] = nGoodsCraftedByCraftman;
    }
    
    
    
    // Function to change the Shop's variables
    /**
     * Set Shop State.
     * @param state State of the shop
     */
    @Override
    public synchronized void setShopState(int state){
        this.stateShop = state;
        reportStatus();
    }
    
    /**
     * Set number of customers inside.
     * @param nCustomersInsideShop Number of customers inside
     */
    @Override
    public synchronized void setnCustomersInsideShop(int nCustomersInsideShop) {
        this.nCustomersInsideShop = nCustomersInsideShop;
    }
    
    /**
     * Set number of goods in display.
     * @param nGoodsInDisplay Number of goods in display
     */
    @Override
    public synchronized void setnGoodsInDisplay(int nGoodsInDisplay) {
        this.nGoodsInDisplay = nGoodsInDisplay;
    }
    
    /**
     * Set if the craftsman requested the transfer of finished products to the Shop.
     * @param tranfsProductsToShop Boolean indicating if the Craftman requested
     */
    @Override
    public synchronized void setTranfsProductsToShop(boolean tranfsProductsToShop) {
        this.transfProductsToShop = tranfsProductsToShop;
    }
    
    /**
     * Set if the craftsman requested the supply of prime materials to the Factory.
     * @param supplyMaterialsToFactory Boolean indicating if the Craftman requested
     */
    @Override
    public synchronized void setSupplyMaterialsToFactory(boolean supplyMaterialsToFactory) {
        this.supplyMaterialsToFactory = supplyMaterialsToFactory;
    }
        

    
    // Function to change the Factory's variables
    /**
     * Change the amount of prime materials presently in the Factory.
     * @param nPrimeMaterialsInFactory Amount of prime materials available in the Factory
     */
    @Override
    public synchronized void setnPrimeMaterialsInFactory(int nPrimeMaterialsInFactory) {
        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
    }
    
    /**
     * Change the number of finished products presently in the Factory.
     * @param nFinishedProductsInFactory Number of finished products in the Factory
     */
    @Override
    public synchronized void setnFinishedProductsInFactory(int nFinishedProductsInFactory){
        this.nFinishedProductsInFactory = nFinishedProductsInFactory;
    }
    
    /**
     * Change the number of times that a supply of prime materials was delivered to the Factory.
     * @param nSuppliedTimes Number of times that the owner delivered prime materials
     */
    @Override
    public synchronized void setnSuppliedTimes(int nSuppliedTimes){
        this.nSuppliedTimes = nSuppliedTimes;
    }
    
    /**
     * Change the total amount of prime materials that have already been supplied (accumulation).
     * @param nPrimeMaterialsSupplied Number of prime materials supplied
     */
    @Override
    public synchronized void setnPrimeMaterialsSupplied(int nPrimeMaterialsSupplied){
        this.nPrimeMaterialsSupplied = nPrimeMaterialsSupplied;
    }
    
    /**
     * Change the total number of products that have already been manufactured (accumulation).
     * @param nProductsManufactured Total number of products produced
     */
    @Override
    public synchronized void setnProductsManufactured(int nProductsManufactured){
        this.nProductsManufactured = nProductsManufactured;
    }
}
