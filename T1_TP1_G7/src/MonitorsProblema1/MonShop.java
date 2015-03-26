package MonitorsProblema1;

import genclass.*;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop {
    
    /**
     * Present State of Shop
     * 
     * @serialField shopState
     */
    private int shopState;
    
    /**
     * General Repository
     * 
     * @serialField sharedInfo
     */
    private final MonInfo sharedInfo;

    /**
     * FIFO with Customers on the Shop
     *
     * @serialField sitCustomer
     */
    private MemFIFO sitCustomer;
    
    private int customerInsideShop;
    
    private int nGoodsInDisplay;
    private int flagBringProductsFromFactory;
    private boolean flagPrimeMaterialsNeeded;
    

    /**
     * Create Monitor of the Shop
     * 
     * @param nInitialProductsInShop
     * @param nCustomer
     */
    public MonShop(int nInitialProductsInShop, int nCustomer, MonInfo sharedInfo) {
        this.sharedInfo = sharedInfo;
        this.shopState = MonInfo.CLOSED;
        this.customerInsideShop = 0;
        sitCustomer = new MemFIFO(nCustomer); // create FIFO for wainting Customers
        
        this.nGoodsInDisplay = nInitialProductsInShop; // Bens a venda inicialmente
        this.flagBringProductsFromFactory = 0;
        this.flagPrimeMaterialsNeeded = false;
    }

    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }

    public synchronized void setnGoodsInDisplay(int goods) {
        this.nGoodsInDisplay = goods;
        sharedInfo.setnGoodsInDisplay(nGoodsInDisplay);
    }

    public boolean customersInTheShop(){
        return !this.sitCustomer.isEmpty(); // True if not empty queue
    }

    /**
     * 
     * @return check if the door is open or not
     */
    public boolean isDoorOpen() {
        return shopState != 0; // 0 => closed
    }

    public synchronized void enterShop() {
        this.customerInsideShop++;
        sharedInfo.setnCustomersInsideShop(customerInsideShop);
    }
    
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
