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
     * Create customer thread
     * 
     * @param customerId customer identification
     * @param shop Shop
     * @param nIter number of cycle iterations of the client life
     */
    public Customer(int customerId, MonShop shop, int nIter){
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
			
        }
    }
	
        
    /**
     * Living normal life
     */
    public void livingNormalLife(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }

	/**
	 * 
	 * @return if the door is open or not
	 */
	private boolean isDoorOpen() {
		return false;
	}
    
}
