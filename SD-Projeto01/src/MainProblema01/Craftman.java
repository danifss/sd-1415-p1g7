/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainProblema01;

import MonitorsProblema1.*;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Craftman extends Thread {
    /**
     * Craftman thread id
     * 
     * @serialField craftmanId
     */
    private int craftmanId;
    
    /**
     * Factory/Workshop
     * 
     * @serialField factory
     */
    private MonFactory factory;
	
    /**
     * Shop
     * 
     * @serialField shop
     */
    private final MonInfo info;
    
    /**
     * Create Craftman thread
     * 
     * @param craftmanId Craftman identity
     * @param factory Factory
     */
    public Craftman(int craftmanId, MonFactory factory, MonInfo info){
        this.craftmanId = craftmanId;
        this.factory = factory;
        this.info = info;
    }
    
    /**
     * Life cycle of the Craftman
     */
    @Override
    public void run(){
//        while(true){
//			if(!factory.collectMaterials()){ // if can not collect materials
//				shop.primeMaterialsNeeded(); // request prime materials
//				backToWork(); // return to work
//			}
//			prepareToProduce(); // preparing to produce
//			backToWork(); // return to work
//			
//		}
    }
    
    /**
     * Producing new piece
     */
    public void shapingItUp(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
    /**
     * Goes to store
     */
    public void goToStore(){
        info.setStateCraftsman(craftmanId, MonInfo.STORING_IT_FOR_TRANSFER);
        
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
}
