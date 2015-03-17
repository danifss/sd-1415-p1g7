/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainProblema01;

import MonitorsProblema1.*;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Customer extends Thread implements CustomerInterface{
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
			
			while(!goShopping(this.customerId)){
				
			}
        }
    }
	
	/**
	 * Customer go shopping, first check if the shop door is open
	 * then enter shop and appraising offer on display and if he wants 
	 * that he buy goods, and finally exit shop
	 * 
	 * @return true if the customer could enter the shop
	 */
	public boolean goShopping(int customerId){
		
		
		if(!isDoorOpen())
			return false;
		
		return true;
	}
        
    /**
     * Living normal life
     */
	@Override
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
