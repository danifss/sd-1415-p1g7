package MainProblema01;

import MonitorsProblema1.*;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Customer extends Thread {
    /**
     * Customer identity
     *
     * @serialField customerId
     */
    private int customerId;
    
    /**
     * Present Customer State
     * 
     * @serialField customerState
     */
    private int customerState;
    
    /**
     * Shop
     * 
     * @serialField shop
     */
    private MonShop shop;
	
    /**
     * General Repository
     * 
     * @serialField sharedInfo
     */
    private final MonInfo sharedInfo;

    /**
     * Create customer thread
     * 
     * @param customerId customer identification
     * @param shop Shop
     * @param nIter number of cycle iterations of the client life
     */
    public Customer(MonInfo sharedInfo, int customerId, MonShop shop){
        this.sharedInfo = sharedInfo;
        this.customerId = customerId;
        this.shop = shop;
        this.customerState = MonInfo.CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
     * Life cycle of the customer
     */
    @Override
    public void run(){
        while(!endOper()){
            livingNormalLife();

			goShopping();

			if(!shop.isDoorOpen()) {
				tryAgainLater();
				continue; // passa para a proxima iteracao
			}

            enterShop();
            int goods = perusingAround();
            if(goods != 0){
                    iWantThis(goods);
            }
            shop.exitShop(customerId);
        }
    }

    /**
     * Living normal life
     */
    private void livingNormalLife(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }

    /**
     * Customer go shopping, first check if the shop door is open
     * then enter shop and appraising offer on display and if he wants 
     * that he buy goods, and finally exit shop
     * 
     */
    private void goShopping(){
        setCustomerState(MonInfo.CHECKING_DOOR_OPEN);
        this.sharedInfo.setCustomerState(customerId, MonInfo.CHECKING_DOOR_OPEN);
    }

    /**
     * Try Again Later
     */
    private void tryAgainLater(){
        this.sharedInfo.setCustomerState(customerId, MonInfo.CARRYING_OUT_DAILY_CHORES);
        setCustomerState(MonInfo.CARRYING_OUT_DAILY_CHORES);

        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
    private void enterShop() {
        this.sharedInfo.setCustomerState(customerId, MonInfo.APPRAISING_OFFER_IN_DISPLAY);
        this.sharedInfo.setnCustomersInsideShop(1);
        
        setCustomerState(MonInfo.APPRAISING_OFFER_IN_DISPLAY);
        
        shop.enterShop();
    }
    
    private int perusingAround(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        
        return shop.perusingAround(); // retorna num. de bens a comprar
    }
    
    private void iWantThis(int goods){
        this.sharedInfo.setCustomerState(customerId, MonInfo.BUYING_SOME_GOODS);
        
        setCustomerState(MonInfo.BUYING_SOME_GOODS);
        
        shop.iWantThis(customerId, goods); // acao bloqueante
        this.sharedInfo.setnGoodsInDisplay(-goods); // reduz produtos disponiveis na loja
        this.sharedInfo.incrementnGoodsByCustomer(customerId, goods); // adiciona num. total de produtos comprados pelo cliente
        /**
         * TERMINAR
         */
    }

	private boolean endOper() {
		// valida se o cliente deve termina ou nao
		
		return true;
	}

    public void setCustomerState(int customerState) {
        this.customerState = customerState;
    }
    
}