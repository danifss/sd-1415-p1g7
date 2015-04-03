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
    private final int customerId;
    
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
     * Total number of goods bought buy the customer
     * 
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
     * Life cycle of the customer
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
                    // ver se tem produtos
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
     * Living normal life
     */
    private void livingNormalLife(){
        try{
            sleep((long) (1000+100*Math.random()));
        }catch(InterruptedException e){}
    }

    /**
     * Customer go shopping, first check if the shop door is open
     * then enter shop and appraising offer on display and if he wants 
     * that he buy goods, and finally exit shop
     */
    private void goShopping(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(CHECKING_DOOR_OPEN);
    }

    /**
     * Customer checks if the shop is open
     */
    private boolean isDoorOpen(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return shop.isDoorOpen();
    }
    
    /**
     * Try Again Later
     */
    private void tryAgainLater(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(CARRYING_OUT_DAILY_CHORES);

        try{
            sleep((long) (500+100*Math.random()));
        }catch(InterruptedException e){}
    }

    /**
     * Customer enter in the shop
     */
    private void enterShop() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        shop.enterShop();
        setCustomerState(APPRAISING_OFFER_IN_DISPLAY);
    }
    
    /**
     * Customer chooses what to buy
     * @return number of goods to buy
     */
    private void perusingAround(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        
        nProductsCustomer = shop.perusingAround(); // retorna num. de bens a comprar
    }
    
    /**
     * Customer goes to the queue to buy the goods
     * @param goods 
     */
    private void iWantThis(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCustomerState(BUYING_SOME_GOODS);
        
        shop.iWantThis(customerId, nProductsCustomer); // acao bloqueante
        nGoodsBought += nProductsCustomer; // adiciona bens comprados ao total
        sharedInfo.setnGoodsByCustomer(customerId, nGoodsBought);
        //nProductsCustomer = 0; // nao precisa de limpar e assim facilita o debug
    }

    /**
     * The Customer leaves the Shop
     */
    private void exitShop() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        shop.exitShop();
        setCustomerState(CARRYING_OUT_DAILY_CHORES);
    }
    
    /**
     * Check if Customer can die or not
     * @return if customer dies or not
     */
    private boolean endOper() {
        // valida se o cliente deve terminar ou nao
        return shop.endOper() && customerState == CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
     * Change the Present Customer State
     * @param customerState 
     */
    private void setCustomerState(int customerState) {
        this.customerState = customerState;
        sharedInfo.setCustomerState(customerId,customerState);
    }
}
