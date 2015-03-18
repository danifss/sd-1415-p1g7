package MainProblema01;

import MonitorsProblema1.*;


/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Owner extends Thread {
    /**
     * Owner identity
     * 
     * @serialField ownerId
     */
    private int ownerId;
    
    /**
     * Factory
     * 
     * @serialField factory
     */
    private MonFactory factory;
    
    /**
     * Shop
     * 
     * @serialField shop
     */
    private MonShop shop;
    
    /**
     * Create owner thread
     * 
     * @param ownerId Owner identity
     * @param factory Factory
     * @param shop Shop
     */
    
    public Owner(int ownerId, MonFactory factory, MonShop shop){
        this.ownerId = ownerId;
        this.factory = factory;
        this.shop = shop;
    }
    
    /**
     * Life cycle of the owner
     */
    @Override
    public void run(){
        
    }
    
    /**
     * Return to shop
     */
    public void returnToShop(){
		try {
			sleep ((long) (1 + 10 * Math.random ()));
		}
		catch (InterruptedException e) {}
    }
}

