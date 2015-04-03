package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop implements MonShopInterface {
    
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
     * @serialField queueCustomer
     */
    private MemFIFO queueCustomer;
    
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
     * Id of the customer that the owner is attending
     * @serialField attendingCustomerId;
     */
    private int attendingCustomerId;
    
    /**
     * Number of goods that the customer is buying
     * @serialField nGoodsCustomerHave
     */
    private int nGoodsCustomerHave;
    
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
     * Total amount of products delivered
     * @serialField nProductsDelivered
     */
    private int nProductsDelivered;
    
    /**
     * Total number of products that the shop can have in this experience
     * @serialField totalProducts
     */
    private final int totalProducts;
    
    /**
     * Create Monitor of the Shop
     * 
     * @param nInitialProductsInShop Initial number of products in the shop at the beginning
     * @param nCustomer Number of customers
     * @param sharedInfo General repository
     */
    public MonShop(int nInitialProductsInShop, int nCustomer, MonInfo sharedInfo, int totalProducts) {
        this.sharedInfo = sharedInfo;
        shopState = CLOSED;
        customerInsideShop = 0;
        queueCustomer = new MemFIFO(nCustomer); // create FIFO for wainting Customers
        nGoodsInDisplay = nInitialProductsInShop; // Bens a venda inicialmente
        nGoodsCustomerHave = 0;
        attendingCustomerId = -1;
        flagBringProductsFromFactory = 0;
        flagPrimeMaterialsNeeded = false;
        flagPurchaseMade = false;
        nProductsDelivered = nInitialProductsInShop;
        this.totalProducts = totalProducts;
    }
    
    /**
     * See if there is customers inside the shop
     * @return true if it is customers inside
     */
    @Override
    public boolean customersInTheShop(){
        return customerInsideShop > 0;
    }

    /**
     * See if the Queue has customers
     * @return true if the queue has customers
     */
    @Override
    public boolean customerInTheQueue(){
        return !this.queueCustomer.isEmpty();
    }
    
    /**
     * Owner sees the situation of the shop and decide what to do
     * @return action to do
     */
    @Override
    public synchronized int appraiseSit(){        
        if(!endOper() || customersInTheShop()){
            if(shopState == OPEN){
                try{
                    while(queueCustomer.isEmpty() && !isSupplyMaterialsToFactory() && !isTranfsProductsToShop() && !endOper()){
                        wait();
                        Thread.sleep(1000);
                    }
                }catch(Exception e){}
                if(isSupplyMaterialsToFactory() || isTranfsProductsToShop() || endOper()){
                    return FACTORY_NEEDS_SOMETHING;
                }
                return ADDRESS_CUSTOMER;
            }
            try{
                while(queueCustomer.isEmpty() && customersInTheShop()){
                    wait();
                    Thread.sleep(1000);
                }
            }catch(Exception e){}
            if(!customersInTheShop()){
                return FACTORY_NEEDS_SOMETHING;
            }
            return ADDRESS_CUSTOMER;
        }
        return FACTORY_NEEDS_SOMETHING;
    }
    
    /**
     * Owner closes the door
     */
    @Override
    public synchronized void closeTheDoor(){
        if(customersInTheShop()){
            setShopState(STILL_OPEN);
            System.out.println("Shop\t\t- Vai Fechar.");
        }else{
            setShopState(CLOSED);
            System.out.println("Shop\t\t- Porta Fechada.");
        }
    }
    
    /**
     * Owner opens the door
     */
    @Override
    public synchronized void openTheDoor(){
        setShopState(OPEN);
        System.out.println("Shop\t\t- Porta Aberta.");
    }
    
    /**
     * See if the shop is on state STILL_OPEN
     * @return true if the shop is STILL_OPEN
     */
    @Override
    public synchronized boolean isShopStillOpen(){
        return shopState == STILL_OPEN;
    }
    
    /**
     * The owner goes to the Factory to collect products
     */
    @Override
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
    @Override
    public synchronized void addnGoodsInDisplay(int goods) {
        nGoodsInDisplay += goods;
        nProductsDelivered += goods;
        sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
    }
    
    /**
     * Owner goes to factory to restock prime materials
     */
    @Override
    public synchronized void replenishStock(){
        flagPrimeMaterialsNeeded = false;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
    }
    
    /**
     * Prime materials is needed in the Factory
     */
    @Override
    public synchronized void primeMaterialsNeeded(){
        flagPrimeMaterialsNeeded = true;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
        notifyAll();
    }
    
    /**
     * Owner can go to factory to collect products
     */
    @Override
    public synchronized void batchReadyForTransfer(){
        flagBringProductsFromFactory += 1;
        sharedInfo.setTranfsProductsToShop(true);
        notifyAll();
    }
    
    /**
     * Address a Customer on the queue
     */
    @Override
    public synchronized int addressACustomer(){
        attendingCustomerId = (int) this.queueCustomer.peek(); // retorna id do cliente
        notifyAll();
        return attendingCustomerId;
    }
    
    /**
     * Service customer
     * @param customerId
     * @return number of goods that the customer is buying
     */
    @Override
    public synchronized int serviceCustomer(int customerId){
        while(nGoodsCustomerHave < 0){
            try{
                wait();
            }catch(Exception e){}
        }
        return nGoodsCustomerHave;
    }
    
    /**
     * Say goodbye to attending Customer
     */
    @Override
    public synchronized void sayGoodByeToCustomer(){
        flagPurchaseMade = true;
        notifyAll();
        removeSitCustomer(attendingCustomerId); // remove cliente da fila
        attendingCustomerId = -1;
    }
    
    
    //Funcions of customer
    /**
     * See if the door is open
     * @return True if is open
     */
    @Override
    public synchronized boolean isDoorOpen() {
        return shopState == OPEN;
    }
    
    /**
     * A customer enters the shop
     */
    @Override
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
    @Override
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
    @Override
    public synchronized void iWantThis(int customerId, int nGoods) {
        
        this.queueCustomer.write(customerId); // ir para a fila de atendimento
        notifyAll(); // acordar dona
        while(attendingCustomerId != customerId){
            try{
                wait(); // espera que a dona chame o proximo cliente
                Thread.sleep(1000);
            }catch(InterruptedException ex){}
        }
        
        flagPurchaseMade = false;
        nGoodsCustomerHave = nGoods;
        notifyAll();
        while(!flagPurchaseMade){
            try{
                wait();
                Thread.sleep(1000);
            }catch(Exception e){}
        }
        nGoodsCustomerHave = -1;
    }
    
    /**
     * Remove customer from queue
     * @param customerId 
     */
    @Override
    public synchronized void removeSitCustomer(int customerId){
        //sitCustomer.remove(customerId); // nao sei se funciona bem
        if((int)queueCustomer.peek() == customerId) // remove o cliente correto da fila
            queueCustomer.read();
    }
    
    /**
     * The Customer leaves the Shop
     */
    @Override
    public synchronized void exitShop() {
        customerInsideShop--; // decrementar clientes na loja
        sharedInfo.setnCustomersInsideShop(customerInsideShop); // decrementar clientes dentro da loja
        if(!customersInTheShop()){
            notifyAll();
        }
    }  

    /**
     * Set present Shop state
     * @param state
     */
    @Override
    public synchronized void setShopState(int state) {
        shopState = state;
        sharedInfo.setShopState(state);
    }

    /**
     * Check if the factory needs prime materials
     * @return true if is needed
     */
    @Override
    public boolean isSupplyMaterialsToFactory() {
        return flagPrimeMaterialsNeeded;
    }
    
    /**
     * Check if the owner can collect products
     * @return true if he needs to go to the factory
     */
    @Override
    public boolean isTranfsProductsToShop() {
        return flagBringProductsFromFactory > 0;
    }
    
    /**
     * See if the owner and the customer can stop.
     * Checks if all products have been transferred to Shop, and it have
     * all been sold.
     * @return true if they can
     */
    @Override
    public boolean endOper(){
        return nProductsDelivered == totalProducts && nGoodsInDisplay == 0;
    }
    
}
