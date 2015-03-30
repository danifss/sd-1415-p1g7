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
     * Customer States
     */
    private final static int
            CARRYING_OUT_DAILY_CHORES = 0,
            CHECKING_DOOR_OPEN = 1,
            APPRAISING_OFFER_IN_DISPLAY = 2,
            BUYING_SOME_GOODS = 3;
    
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
     * Factory/Workshop
     * 
     * @serialField factory
     */
    private final MonFactory factory;

    /**
     * Create customer thread
     * 
     * @param customerId customer identification
     * @param shop Shop
     * @param nIter number of cycle iterations of the client life
     */
    public Customer(MonInfo sharedInfo, int customerId, MonShop shop, MonFactory factory){
        this.sharedInfo = sharedInfo;
        this.customerId = customerId;
        this.shop = shop;
        this.factory = factory;
        this.customerState = CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
     * Life cycle of the customer
     */
    @Override
    public void run(){
        System.out.println("Iniciado o Customer: "+customerId);
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
            exitShop();
        }
        System.out.println("Terminado o Customer: "+customerId);
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
     */
    private void goShopping(){
        setCustomerState(CHECKING_DOOR_OPEN);
    }

    /**
     * Try Again Later
     */
    private void tryAgainLater(){
        setCustomerState(CARRYING_OUT_DAILY_CHORES);

        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
    private void enterShop() {
        setCustomerState(APPRAISING_OFFER_IN_DISPLAY);
        
        shop.enterShop();
    }
    
    private int perusingAround(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        
        return shop.perusingAround(); // retorna num. de bens a comprar
    }
    
    private void iWantThis(int goods){
        setCustomerState(BUYING_SOME_GOODS);
        
        shop.iWantThis(customerId, goods); // acao bloqueante
    }

    private void exitShop() {
        shop.exitShop(customerId);
        setCustomerState(CARRYING_OUT_DAILY_CHORES);
    }

    private void setCustomerState(int customerState) {
        this.customerState = customerState;
        sharedInfo.setCustomerState(customerId,customerState);
    }
    
    private boolean endOper() {
        // valida se o cliente deve terminar ou nao
        //return factory.endOper() && !factory.checkForMaterials() && noPrimeMaterialsAvailable() && allProductsSold();
	return false;
    }
    
}
