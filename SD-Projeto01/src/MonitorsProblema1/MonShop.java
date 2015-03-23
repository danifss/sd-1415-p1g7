package MonitorsProblema1;

import genclass.*;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class MonShop {

    /**
     * General repository
     * 
     * @serialField sharedInfo
     */
    private MonInfo sharedInfo;

    /**
     * FIFO with Customers on the Shop
     *
     * @serialField sitCustomer
     */
    private MemFIFO sitCustomer;

    /**
     * Create Monitor of the Shop
     * 
     * @param sharedInfo
     */
    public MonShop(MonInfo sharedInfo) {
        this.sharedInfo = sharedInfo;
        sitCustomer = new MemFIFO(this.sharedInfo.getnCustomer()); // create FIFO for wainting Customers
    }

	public boolean isSitCustomerEmpty(){
		return this.sitCustomer.isEmpty();
	}

    /**
     * Customer go shopping, first check if the shop door is open
     * then enter shop and appraising offer on display and if he wants 
     * that he buy goods, and finally exit shop
     * 
     */
    public synchronized void goShopping(int customerId){
        this.sharedInfo.setStateCustomer(customerId, MonInfo.CHECKING_DOOR_OPEN);
    }

    /**
     * 
     * @return check if the door is open or not
     */
    public synchronized boolean isDoorOpen(int customerId) {
        return sharedInfo.getStateShop() != 0;
    }

    public synchronized void enterShop(int customerId) {
        // mudar estado do cliente
        this.sharedInfo.setStateCustomer(customerId, MonInfo.APPRAISING_OFFER_IN_DISPLAY);
        // incrementar clientes dentro da loja
        this.sharedInfo.setnCustomersInsideShop(1);
    }

    public synchronized void iWantThis(int customerId, int nGoods) {
        this.sharedInfo.setStateCustomer(customerId, MonInfo.BUYING_SOME_GOODS);
        this.sitCustomer.write(customerId); // ir para a fila de atendimento
        notifyAll(); // acordar dona
        while(this.sitCustomer.peek() != null){
            try{
                wait(); // espera que a dona chame o proximo cliente
            }catch(InterruptedException ex){}

            if((int)this.sitCustomer.peek() == customerId){ // verifica se e ele a ser chamado
                this.sharedInfo.setnGoodsInDisplay(-nGoods); // reduz produtos disponiveis na loja
                this.sharedInfo.incrementnGoodsByCustomer(customerId, nGoods); // adiciona num. total de produtos comprados pelo cliente
            }
        }
    }

    public synchronized void exitShop(int customerId) {
        // mudar estado do cliente
        this.sharedInfo.setStateCustomer(customerId, MonInfo.APPRAISING_OFFER_IN_DISPLAY);
        // decrementar clientes dentro da loja
        this.sharedInfo.setnCustomersInsideShop(-1);
    }
	
	public synchronized int addressACustomer(){
		notifyAll(); // chamar cliente
		
		return (int) this.sitCustomer.peek();
		
	}

	public void removeSitCustomer(int cid) {
		this.sitCustomer.read();
		//this.sitCustomer.remove(cid); // nao sei se funciona
	}
}
