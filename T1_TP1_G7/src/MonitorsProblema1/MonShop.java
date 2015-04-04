package MonitorsProblema1;

/**
 * This class is responsible to host the Shop
 * 
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop implements MonShopInterface {
    
     /**
      * Shop States.
      */
    private final static int
            CLOSED = 0,
            STILL_OPEN = 1,
            OPEN = 2;
    
    /**
     * Decision taken in AppraiseSit.
     */
    private final static int
            FACTORY_NEEDS_SOMETHING = 0,
            ADDRESS_CUSTOMER = 1;
    
    /**
     * Present State of Shop.
     * @serialField shopState
     */
    private int shopState;
    
    /**
     * General Repository.
     * @serialField sharedInfo
     */
    private final MonInfo sharedInfo;

    /**
     * FIFO with Customers waiting for their turn to buy something.
     * @serialField queueCustomer
     */
    private MemFIFO queueCustomer;
    
    /**
     * Number of customers inside the shop.
     * @serialField customerInsideShop
     */
    private int customerInsideShop;
    
    /**
     * Number of goods that the shop has to sell.
     * @serialField nGoodsInDisplay
     */
    private int nGoodsInDisplay;
    
    /**
     * Id of the customer that the owner is attending.
     * @serialField attendingCustomerId;
     */
    private int attendingCustomerId;
    
    /**
     * Number of goods that the customer is buying.
     * @serialField nGoodsCustomerHave
     */
    private int nGoodsCustomerHave;
    
    /**
     * Flag to see if the owner already finished the purchase.
     * @serialField flagPurchaseMade
     */
    private boolean flagPurchaseMade;
    
    /**
     * Flag to see if the Factory has products to bring.
     * @serialField flagBringProductsFromFactory
     */
    private int flagBringProductsFromFactory;
    
    /**
     * Flag to see if the Factory needs prime materials.
     * @serialField flagPrimeMaterialsNeeded
     */
    private boolean flagPrimeMaterialsNeeded;
    
    /**
     * Total amount of products delivered.
     * @serialField nProductsDelivered
     */
    private int nProductsDelivered;
    
    /**
     * Total number of products that the shop can have in this experience.
     * @serialField totalProducts
     */
    private final int totalProducts;
    
    /**
     * Create Monitor of the Shop.
     * 
     * @param nInitialProductsInShop Initial number of products in the shop at the beginning
     * @param nCustomer Number of customers
     * @param sharedInfo General repository
     * @param totalProducts Total number of products that the shop can have in this experience
     */
    public MonShop(int nInitialProductsInShop, int nCustomer, MonInfo sharedInfo, int totalProducts) {
        this.sharedInfo = sharedInfo;
        shopState = CLOSED;
        customerInsideShop = 0;
        queueCustomer = new MemFIFO(nCustomer);
        nGoodsInDisplay = nInitialProductsInShop;
        nGoodsCustomerHave = 0;
        attendingCustomerId = -1;
        flagBringProductsFromFactory = 0;
        flagPrimeMaterialsNeeded = false;
        flagPurchaseMade = false;
        nProductsDelivered = nInitialProductsInShop;
        this.totalProducts = totalProducts;
    }
    
    /**
     * See if there is customers inside the shop.
     * @return true if the number of Customers inside the shop is greater than 0
     */
    @Override
    public boolean customersInTheShop(){
        return customerInsideShop > 0;
    }

    /**
     * See if the Queue has customers.
     * @return true if the queue has customers
     */
    @Override
    public boolean customerInTheQueue(){
        return !this.queueCustomer.isEmpty();
    }
    
    /**
     * Owner sees the situation of the shop and decide what to do.
     * If the shop is open, he waits until a Customer or a Craftman calls him. 
     * If the shop is still open, he wait until all Customers leave the shop, and 
     * then proceeds to the request from the Factory.
     * FACTORY_NEEDS_SOMETHING can also indicates that the Owner can stop, because
     * this action makes the Owner close the Shop.
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
     * Owner closes the door.
     * If the shop has Customers inside, the shop change his state to STILL_OPEN,
     * if the shop doesn't have Customers, the shop change his state to CLOSED.
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
     * Owner opens the door.
     * The state of the Shop is changed to OPEN.
     */
    @Override
    public synchronized void openTheDoor(){
        setShopState(OPEN);
        System.out.println("Shop\t\t- Porta Aberta.");
    }
    
    /**
     * See if the shop is on state STILL_OPEN.
     * @return true if the shop is STILL_OPEN
     */
    @Override
    public synchronized boolean isShopStillOpen(){
        return shopState == STILL_OPEN;
    }
    
    /**
     * The owner goes to the Factory to collect products.
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
     * Update the number of products that the shop is selling.
     * @param goods Number of products to add to the number of products in display
     */
    @Override
    public synchronized void addnGoodsInDisplay(int goods) {
        nGoodsInDisplay += goods;
        nProductsDelivered += goods;
        sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
    }
    
    /**
     * Owner goes to Factory to restock prime materials.
     */
    @Override
    public synchronized void replenishStock(){
        flagPrimeMaterialsNeeded = false;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
    }
    
    /**
     * The Craftman indicates that prime materials is needed in the Factory.
     * He changes the flag and wakes up the Owner.
     */
    @Override
    public synchronized void primeMaterialsNeeded(){
        flagPrimeMaterialsNeeded = true;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
        notifyAll();
    }
    
    /**
     * The Craftman indicates that the Owner can go to factory to collect products.
     * He changes the flag and wakes up the Owner.
     */
    @Override
    public synchronized void batchReadyForTransfer(){
        flagBringProductsFromFactory += 1;
        sharedInfo.setTranfsProductsToShop(true);
        notifyAll();
    }
    
    /**
     * The Owner address a Customer on the queue.
     * The owner wakes up the first Customer in the queue.
     * @return id of the Customer that the Owner is attending
     */
    @Override
    public synchronized int addressACustomer(){
        attendingCustomerId = (int) this.queueCustomer.peek(); // retorna id do cliente
        notifyAll();
        return attendingCustomerId;
    }
    
    /**
     * The Owner services a customer.
     * The Owner waits until the Customer updates the number of products he wants 
     * to buy.
     * @return number of goods that the customer is buying
     */
    @Override
    public synchronized int serviceCustomer(){
        while(nGoodsCustomerHave < 0){
            try{
                wait();
            }catch(Exception e){}
        }
        return nGoodsCustomerHave;
    }
    
    /**
     * The Owner says goodbye to the Customer he is attending.
     * He updates the flag that indicates that the purchase was made and wakes up
     * the Customer. Then removes the Customer from the queue.
     */
    @Override
    public synchronized void sayGoodByeToCustomer(){
        flagPurchaseMade = true;
        notifyAll();
        removeSitCustomer(attendingCustomerId);
        attendingCustomerId = -1;
    }
    
    
    // Funcions of customer
    /**
     * The Customer sees if the door is open
     * @return True if the shop is OPEN
     */
    @Override
    public synchronized boolean isDoorOpen() {
        return shopState == OPEN;
    }
    
    /**
     * The customer enters the shop.
     */
    @Override
    public synchronized void enterShop() {
        this.customerInsideShop++;
        sharedInfo.setnCustomersInsideShop(customerInsideShop);
    }
    
    /**
     * The Customer is perusing around.
     * A random number is generated, then if it is less than 0.5 and 
     * exists goods to buy, that number*100 is divided by the number 
     * of goods for sell and returns his remainder.
     * @return number of goods to buy
     */
    @Override
    public synchronized int perusingAround(){
        // choose what to buy
        double r = Math.random();
        if((r < 0.5) && (nGoodsInDisplay > 0)){ // 50% probability to buy
            r = r * 10;
            int goods = (int) (r+1) % nGoodsInDisplay;
            if(nGoodsInDisplay == 1) // When there is only one product to sell
                goods = 1;
            //System.out.printf("\t\tR : %f - Goods : %d - Produtos Disponiveis: %d\n",r,goods,nGoodsInDisplay);
            nGoodsInDisplay -= goods;
            sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
            return goods;
        }
        return 0;
    }
    
    /**
     * The Customer goes to the queue, and waits till the owner call him.
     * When the owner calls a Customer and he is in the front of the queue, he 
     * makes the purchase.
     * @param customerId Id of the customer that wants to buy something
     * @param nGoods Number of goods that the Customer wants to buy
     */
    @Override
    public synchronized void iWantThis(int customerId, int nGoods) {
        
        this.queueCustomer.write(customerId); // Goes to the queue
        notifyAll(); // wakes up the Owner
        while(attendingCustomerId != customerId){
            try{
                wait(); // Waits until the Owner calls the next Customer
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
     * Remove Customer from queue.
     * @param customerId 
     */
    @Override
    public synchronized void removeSitCustomer(int customerId){
        if((int)queueCustomer.peek() == customerId) // Removes the correct Customer Id
            queueCustomer.read();
    }
    
    /**
     * The Customer leaves the Shop.
     * If he his the last one leaving the Shop, wakes up the Owner (important to Owner
     * finish working, if he his waiting for all the Customers to buy products,
     * but the last one don't buy anything).
     */
    @Override
    public synchronized void exitShop() {
        customerInsideShop--;
        sharedInfo.setnCustomersInsideShop(customerInsideShop);
        if(!customersInTheShop()){
            notifyAll();
        }
    }  

    /**
     * Set present Shop state.
     * @param state State of the Shop
     */
    @Override
    public synchronized void setShopState(int state) {
        shopState = state;
        sharedInfo.setShopState(state);
    }

    /**
     * Check if the factory needs prime materials.
     * @return true if the Factory needs prime materials
     */
    @Override
    public boolean isSupplyMaterialsToFactory() {
        return flagPrimeMaterialsNeeded;
    }
    
    /**
     * Check if the owner can collect products.
     * @return true if he needs to go to the factory
     */
    @Override
    public boolean isTranfsProductsToShop() {
        return flagBringProductsFromFactory > 0;
    }
    
    /**
     * See if the owner and the customer can stop.
     * Checks if all products have been transferred to Shop, and all the products
     * are sold.
     * @return true if they can stop working
     */
    @Override
    public boolean endOper(){
        return nProductsDelivered == totalProducts && nGoodsInDisplay == 0;
    }
}
