package MainProblema01;

import MonitorsProblema1.*;

/**
 * This class is responsible to host the Customers
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Customer extends Thread implements CustomerInterface {
    
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
     * @serialField customerId
     */
    private final int customerId;
    
    /**
     * Present Customer State
     * @serialField customerState
     */
    private int customerState;
    
    /**
     * Shop
     * @serialField shop
     */
    private MonShop shop;
	
    /**
     * General Repository
     * @serialField sharedInfo
     */
    private final MonInfo sharedInfo;
    
    /**
     * Total number of goods bought buy the customer
     * @serialField nGoodsBought
     */
    private int nGoodsBought;

    /**
     * Number of products that the customer has with him
     * @serialField nProductsCustomerHas
     */
    private int nProductsCustomer;
    
    /**
     * Create customer thread
     * 
     * @param sharedInfo General repository
     * @param customerId customer identification
     * @param shop Shop
     */
    public Customer(MonInfo sharedInfo, int customerId, MonShop shop){
        this.sharedInfo = sharedInfo;
        this.customerId = customerId;
        this.shop = shop;
        this.customerState = CARRYING_OUT_DAILY_CHORES;
        this.nGoodsBought = 0;
    }
    
    /**
     * Life cycle of the customer.
     */
    @Override
    public void run(){
        System.out.println("Iniciado o Customer: "+customerId);
        while(!endOper()){
            switch(customerState){
                case CARRYING_OUT_DAILY_CHORES:
                    livingNormalLife();
                    goShopping();
                    break;
                case CHECKING_DOOR_OPEN:
                    if(isDoorOpen()) {
                        enterShop();
                        System.out.printf("Cliente %d\t- Entrou na loja.\n",customerId);
                    }else{
                        tryAgainLater();
                    }
                    break;
                case APPRAISING_OFFER_IN_DISPLAY:
                    perusingAround();
                    System.out.printf("Cliente %d\t- A escolher produtos.\n",customerId);
                    if(nProductsCustomer > 0){
                        iWantThis();
                        System.out.printf("Cliente %d\t- Comprou %d produtos.\n",customerId,nProductsCustomer);
                    }else{
                        exitShop();
                        System.out.printf("Cliente %d\t- Saiu sem comprar produtos.\n",customerId);
                    }
                    break;
                case BUYING_SOME_GOODS:
                        exitShop();
                        System.out.printf("Cliente %d\t- Saiu da loja.\n",customerId);
                    break;
            }
        }
        System.out.println("Terminado o Customer: "+customerId);
    }

    /**
     * Living normal life.
     */
    private void livingNormalLife(){
        try{
            sleep((long) (1000+200*Math.random()));
        }catch(InterruptedException e){}
    }

    /**
     * Customer goes shopping.
     * First, he will need to check if the door is open.
     */
    private void goShopping(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(CHECKING_DOOR_OPEN);
    }

    /**
     * Customer checks if the shop is open.
     */
    private boolean isDoorOpen(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return shop.isDoorOpen();
    }
    
    /**
     * If the shop is not open, he will try again later.
     */
    private void tryAgainLater(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(CARRYING_OUT_DAILY_CHORES);

        try{
            sleep((long) (750+200*Math.random()));
        }catch(InterruptedException e){}
    }

    /**
     * Customer enters the shop.
     */
    private void enterShop() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        shop.enterShop();
        setCustomerState(APPRAISING_OFFER_IN_DISPLAY);
    }
    
    /**
     * Customer chooses if he wants to buy something.
     * If he wants, he collects some products.
     * @return Number of goods to buy
     */
    private void perusingAround(){
        try{
            sleep((long) (100+20*Math.random()));
        }catch(InterruptedException e){}
        
        nProductsCustomer = shop.perusingAround();
    }
    
    /**
     * Customer goes to the queue to buy the products.
     * He will wait until is his turn in the queue.
     * @param goods 
     */
    private void iWantThis(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(BUYING_SOME_GOODS);
        
        shop.iWantThis(customerId, nProductsCustomer); 
        nGoodsBought += nProductsCustomer;
        sharedInfo.setnGoodsByCustomer(customerId, nGoodsBought);
    }

    /**
     * Customer leaves the Shop.
     */
    private void exitShop() {
        try{
            sleep((long) (100+25*Math.random()));
        }catch(InterruptedException e){}
        shop.exitShop();
        setCustomerState(CARRYING_OUT_DAILY_CHORES);
    }
    
    /**
     * Verifies if the Owner can stop working.
     * From the shop, he checks if all products have been transferred to Shop, and all the products
     * are sold. He needs to be in the state CARRYING_OUT_DAILY_CHORES
     * @return true if needs to stop
     */
    private boolean endOper() {
        return shop.endOper() && customerState == CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
     * Change the present Customer state.
     * @param customerState 
     */
    private void setCustomerState(int customerState) {
        this.customerState = customerState;
        sharedInfo.setCustomerState(customerId,customerState);
    }
}
