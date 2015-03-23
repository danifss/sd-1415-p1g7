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
     * Number of cycle iterations of the client life
     * 
     * @serialField nIter
     */
    private int nIter = 0;
    
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
    public Customer(MonInfo sharedInfo, int customerId, MonShop shop, int nIter){
        this.sharedInfo = sharedInfo;
        this.customerId = customerId;
        this.shop = shop;
        if(nIter>0) this.nIter = nIter;
    }
    
    /**
     * Life cycle of the customer
     */
    @Override
    public void run(){
        for(int i=0; i<nIter; i++){
            
                livingNormalLife();

                shop.goShopping(this.customerId);

                if(!shop.isDoorOpen(this.customerId))
                {
                    tryAgainLater();
                    continue;
                }
            

            shop.enterShop(this.customerId);
            int goods = perusingAround();
            if(goods != 0){
                    this.shop.iWantThis(this.customerId, goods);
            }
            this.shop.exitShop(this.customerId);
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
     * Try Again Later
     */
    private void tryAgainLater(){
        this.sharedInfo.setStateCustomer(customerId, MonInfo.CARRYING_OUT_DAILY_CHORES);

        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
	
    private int perusingAround(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        // choose what to buy
        double r = Math.random();
        if(r < 0.5 && this.sharedInfo.getnGoodsInDisplay() > 0){ // 50% probability to buy
            r = r * 100;
            return (int) r % this.sharedInfo.getnGoodsInDisplay();
        }
        return 0;
    }
}
