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
    }

    /**
     * Get number of products that the shop is selling
     * @return Number of products in display
     */
    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }

    /**
     * Update the number of products that the shop is selling
     * @param goods 
     */
    public synchronized void addnGoodsInDisplay(int goods) {
        nGoodsInDisplay += goods;
        sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
    }

    // DUVIDA
    // customers in the shop ou customers in the queue?????????????????????????????????????????
    public boolean customersInTheShop(){
        return !this.sitCustomer.isEmpty(); // True if not empty queue
    }

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
    
    
    // Não percebi (Raphael)
    public synchronized int perusingAround(){
        // choose what to buy
        double r = Math.random();
        if(r < 0.5 && nGoodsInDisplay > 0){ // 50% probability to buy
            r = r * 100;
            return (int) r % nGoodsInDisplay;
        }
        return 0;
    }

    
    public synchronized void iWantThis(int customerId, int nGoods) {
        
        this.sitCustomer.write(customerId); // ir para a fila de atendimento
        notifyAll(); // acordar dona
        while((int)this.sitCustomer.peek() != customerId){
            try{
                wait(); // espera que a dona chame o proximo cliente
            }catch(InterruptedException ex){}
        }

        if((int)this.sitCustomer.peek() == customerId){ // verifica se e ele a ser chamado
            nGoodsInDisplay -= nGoods; // diminuir bens da loja
            sharedInfo.setnGoodsInDisplay(nGoodsInDisplay); // reduz produtos disponiveis na loja
            
            // FALTA VERIFICAR A FUNÇAO DE BAIXO, POIS NAO HA VARIAVEL INDICANDO O NUMERO
            // DE PRODUTOS QUE O CUSTOMER COMPROU (Variavel no proprio customer)
            
            sharedInfo.setnGoodsByCustomer(customerId, nGoodsInDisplay); // adiciona num. total de produtos comprados pelo cliente
        }
    }

    public synchronized void exitShop(int customerId) {
        if((int)sitCustomer.peek() == customerId){
            customerInsideShop--; // decrementar clientes na loja
            sharedInfo.setnCustomersInsideShop(customerInsideShop); // decrementar clientes dentro da loja
        }
    }
	
    public synchronized int addressACustomer(){
            notifyAll(); // chamar cliente

            return (int) this.sitCustomer.peek(); // retorna id do cliente
    }
    
    public synchronized void removeSitCustomer(int customerId){
        //sitCustomer.remove(customerId); // nao se funciona bem
        if((int)sitCustomer.peek() == customerId) // remove o cliente correto da fila
            sitCustomer.read();
    }

    public synchronized void setShopState(int state) {
        shopState = state;
        sharedInfo.setShopState(state);
    }

    /**
     * Prime materials is needed in the Factory
     */
    public synchronized void primeMaterialsNeeded(){
        flagPrimeMaterialsNeeded = true;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
    }
    
    /**
     * Check if the factory needs prime materials
     * @return true if is needed
     */
    public boolean isSupplyMaterialsToFactory() {
        return flagPrimeMaterialsNeeded;
    }
    
    /**
     * Owner goes to factory to restock prime materials
     */
    public synchronized void replenishStock(){
        flagPrimeMaterialsNeeded = false;
        sharedInfo.setSupplyMaterialsToFactory(flagPrimeMaterialsNeeded);
    }
    
    /**
     * Owner can go to factory to collect products
     */
    public synchronized void batchReadyForTransfer(){
        flagBringProductsFromFactory += 1;
        sharedInfo.setTranfsProductsToShop(true);
    }
    
    /**
     * Check if the owner can collect products
     * @return true if he needs to go to the factory
     */
    public boolean isTranfsProductsToShop() {
        return flagBringProductsFromFactory > 0;
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

}
