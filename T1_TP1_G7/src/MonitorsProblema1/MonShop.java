package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop {
    
     /**
      * Shop States
      */
    private final static int
            CLOSED = 0,
            STILL_OPEN = 1,
            OPEN = 2;
    
    /**
     * Decision taken in AppraiseSit
     */
    private final static int
            FACTORY_NEEDS_SOMETHING = 0,
            ADDRESS_CUSTOMER = 1;
    
    /**
     * Present State of Shop
     * @serialField shopState
     */
    private int shopState;
    
    /**
     * General Repository
     * @serialField sharedInfo
     */
    private final MonInfo sharedInfo;

    /**
     * FIFO with Customers on the Shop
     * @serialField sitCustomer
     */
    private MemFIFO sitCustomer;
    
    /**
     * Number of customers inside the shop
     * @serialField customerInsideShop
     */
    private int customerInsideShop;
    
    /**
     * Number of goods that the shop has to sell
     * @serialField nGoodsInDisplay
     */
    private int nGoodsInDisplay;
    
    /**
     * Number of goods that the present customer will buy
     * @serialField nGoodsToBuy
     */
    private int nGoodsToBuy;
    
    /**
     * Flag to see if the owner already finished the purchase
     * @serialField flagPurchaseMade
     */
    private boolean flagPurchaseMade;
    
    /**
     * Flag to see if the Factory has products to bring
     * @serialField flagBringProductsFromFactory
     */
    private int flagBringProductsFromFactory;
    
    /**
     * Flag to see if the Factory needs prime materials
     * @serialField flagPrimeMaterialsNeeded
     */
    private boolean flagPrimeMaterialsNeeded;
    

    /**
     * Create Monitor of the Shop
     * 
     * @param nInitialProductsInShop Initial number of products in the shop at the beginning
     * @param nCustomer Number of customers
     * @param sharedInfo General repository
     */
    public MonShop(int nInitialProductsInShop, int nCustomer, MonInfo sharedInfo) {
        this.sharedInfo = sharedInfo;
        shopState = CLOSED;
        customerInsideShop = 0;
        sitCustomer = new MemFIFO(nCustomer); // create FIFO for wainting Customers
        nGoodsInDisplay = nInitialProductsInShop; // Bens a venda inicialmente
        flagBringProductsFromFactory = 0;
        flagPrimeMaterialsNeeded = false;
        nGoodsToBuy = 0;
        flagPurchaseMade = false;
    }
    
    /**
     * See if there is customers inside the shop
     * @return true if it is customers inside
     */
    public boolean customersInTheShop(){
        return customerInsideShop > 0;
    }

    /**
     * See if the Queue has customers
     * @return true if the queue has customers
     */
    public boolean customerInTheQueue(){
        return !this.sitCustomer.isEmpty();
    }
    
    /**
     * Owner sees the situation of the shop and decide what to do
     * @return action to do
     */
    public synchronized int appraiseSit(){        
        if(shopState == OPEN){
            try{
                while(sitCustomer.isEmpty() && !isSupplyMaterialsToFactory() && !isTranfsProductsToShop()){
                    wait();
                    Thread.sleep(1000);
                }
            }catch(Exception e){}
            if((isSupplyMaterialsToFactory() || isTranfsProductsToShop())){
                return FACTORY_NEEDS_SOMETHING;
            }
            return ADDRESS_CUSTOMER;
        }
        try{
            while(sitCustomer.isEmpty() && customersInTheShop()){
                wait();
                Thread.sleep(1000);
            }
        }catch(Exception e){}
        if(!customersInTheShop()){
            return FACTORY_NEEDS_SOMETHING;
        }
        return ADDRESS_CUSTOMER;
    }
    
    /**
     * Owner closes the door
     */
    public synchronized void closeTheDoor(){
        if(customersInTheShop()){
            setShopState(STILL_OPEN);
        }else{
            setShopState(CLOSED);
        }
    }
    
    /**
     * Owner opens the door
     */
    public synchronized void openTheDoor(){
        setShopState(OPEN);
    }
    
    /**
     * See if the shop is on state STILL_OPEN
     * @return true if the shop is STILL_OPEN
     */
    public synchronized boolean isShopStillOpen(){
        return shopState == STILL_OPEN;
    }
    
    /**
     * The owner goes to the Factory to collect products
     */
    public synchronized void goToWorkshop(){
        flagBringProductsFromFactory -= 1;
        if(flagBringProductsFromFactory > 0)
            sharedInfo.setTranfsProductsToShop(true);
        else
            sharedInfo.setTranfsProductsToShop(false);
    }
    
    /**
     * Update the number of products that the shop is selling
     * @param goods 
     */
    public synchronized void addnGoodsInDisplay(int goods) {
        nGoodsInDisplay += goods;
        sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
    }
    
    /**
     * Owner goes to factory to restock prime materials
     */
    public synchronized void replenishStock(){
        flagPrimeMaterialsNeeded = false;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
    }
    
    /**
     * Prime materials is needed in the Factory
     */
    public synchronized void primeMaterialsNeeded(){
        flagPrimeMaterialsNeeded = true;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
        notifyAll();
    }
    
    /**
     * Owner can go to factory to collect products
     */
    public synchronized void batchReadyForTransfer(){
        flagBringProductsFromFactory += 1;
        sharedInfo.setTranfsProductsToShop(true);
        notifyAll();
    }
    
    /**
     * Address a Customer on the queue
     * @return customer on top of the queue
     */
    public synchronized int addressACustomer(){
        return (int) this.sitCustomer.peek(); // retorna id do cliente
    }
    
    /**
     * Service customer
     * @param customerId
     */
    public synchronized void serviceCustomer(int customerId){
        notifyAll();
    }
    
    public synchronized void sayGoodByeToCustomer(int customerId){
        flagPurchaseMade = true;
        notifyAll();
        removeSitCustomer(customerId); // remove cliente da fila
    }
    
    
    //Funcions of customer
    /**
     * See if the door is open
     * @return True if is open
     */
    public boolean isDoorOpen() {
        return shopState == OPEN;
    }
    
    /**
     * A customer enters the shop
     */
    public synchronized void enterShop() {
        this.customerInsideShop++;
        sharedInfo.setnCustomersInsideShop(customerInsideShop);
    }
    
    /**
     * A random number is generated, then if it is less than 0.5 and 
     * exists goods to buy, that number*100 is divided by the number 
     * of goods for sell and returns his remainder.
     * @return number of goods to buy
     */
    public synchronized int perusingAround(){
        // choose what to buy
        double r = Math.random();
        if((r <= 0.5) && (nGoodsInDisplay > 0)){ // 50% probability to buy
            r = r * 100;
            int goods = (int) r % nGoodsInDisplay;
            if(nGoodsInDisplay == 1) // para quando tem apenas 1 produto a venda
                goods = 1;
            nGoodsInDisplay -= goods; // retirar de exposicao os produtos
            sharedInfo.setnGoodsInDisplay(nGoodsInDisplay); // reduz produtos disponiveis na loja
            return goods;
        }
        return 0;
    }
    
    /**
     * Customer goes to the queue, and waits till the owner call him.
     * When the owner call and he is in the front of the queue 
     * he buys the goods 
     * @param customerId
     * @param nGoods 
     */
    public synchronized void iWantThis(int customerId, int nGoods) {
        
        this.sitCustomer.write(customerId); // ir para a fila de atendimento
        notifyAll(); // acordar dona
        while((int)this.sitCustomer.peek() != customerId){
            try{
                wait(); // espera que a dona chame o proximo cliente
                Thread.sleep(1000);
            }catch(InterruptedException ex){}
        }

        nGoodsToBuy = nGoods;
        flagPurchaseMade = false;
        while(!flagPurchaseMade){
            try{
                wait();
                Thread.sleep(1000);
            }catch(Exception e){}
        }
    }
    
    /**
     * Remove customer from queue
     * @param customerId 
     */
    public synchronized void removeSitCustomer(int customerId){
        //sitCustomer.remove(customerId); // nao sei se funciona bem
        if((int)sitCustomer.peek() == customerId) // remove o cliente correto da fila
            sitCustomer.read();
    }
    
    /**
     * The Customer leaves the Shop
     */
    public synchronized void exitShop() {
        customerInsideShop--; // decrementar clientes na loja
        sharedInfo.setnCustomersInsideShop(customerInsideShop); // decrementar clientes dentro da loja
        if(shopState==STILL_OPEN && !customersInTheShop()){
            notifyAll();
        }
    }  

    /**
     * Set present Shop state
     * @param state
     */
    public synchronized void setShopState(int state) {
        shopState = state;
        sharedInfo.setShopState(state);
    }

    /**
     * Check if the factory needs prime materials
     * @return true if is needed
     */
    public boolean isSupplyMaterialsToFactory() {
        return flagPrimeMaterialsNeeded;
    }
    
    /**
     * Check if the owner can collect products
     * @return true if he needs to go to the factory
     */
    public boolean isTranfsProductsToShop() {
        return flagBringProductsFromFactory > 0;
    }
    
}
