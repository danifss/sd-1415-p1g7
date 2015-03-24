package MonitorsProblema1;

import genclass.*;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop {
    
    /**
     * Presente State of Shop
     * 
     * @serialField shopState
     */
    private int shopState;

    /**
     * FIFO with Customers on the Shop
     *
     * @serialField sitCustomer
     */
    private MemFIFO sitCustomer;
    
    private int customerInsideShop;
    
    private int nGoodsInDisplay;

    /**
     * Create Monitor of the Shop
     * 
     * @param sharedInfo
     */
    public MonShop(int nInitialProductsInShop) {
        this.shopState = MonInfo.CLOSED;
        this.customerInsideShop = 0;
        sitCustomer = new MemFIFO(MonInfo.getnCustomer()); // create FIFO for wainting Customers
        
        this.nGoodsInDisplay = nInitialProductsInShop; // Bens a venda inicialmente
    }

    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }

    public synchronized void setnGoodsInDisplay(int goods) {
        this.nGoodsInDisplay = goods;
    }

	public boolean isSitCustomerEmpty(){
		return this.sitCustomer.isEmpty();
	}

    /**
     * 
     * @return check if the door is open or not
     */
    public synchronized boolean isDoorOpen() {
        return shopState != 0; // 0 => closed
    }

    public synchronized void enterShop() {
        this.customerInsideShop++;
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
        }
    }

    public synchronized void exitShop(int customerId) {
        //sitCustomer.remove(customerId); // nao se funciona bem
        if((int)sitCustomer.peek() == customerId)
            customerInsideShop--; // decrementar clientes na loja
    }
	
	public synchronized int addressACustomer(){
		notifyAll(); // chamar cliente
		
		return (int) this.sitCustomer.peek(); // retorna id do cliente
	}
    
    public synchronized void removeSitCustomer(int customerId){
        if((int)sitCustomer.peek() == customerId) // remove o cliente correto da fila
            sitCustomer.read();
    }

}
