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
     * @param info Repository
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
        while(true){
            switch(info.getStateCraftsman(craftmanId)){
                case MonInfo.FETCHING_PRIME_MATERIALS:
                    if(factory.checkForMaterials()){
                        factory.collectMaterials(craftmanId);
                         
                    //Prepare to produce
                    }
                    else{
                        // primeMaterialsNeeded
                    }
                   
                    
                    //primeMaterialsNeeded
                    
                    break;
                case MonInfo.PRODUCING_A_NEW_PIECE:
                    shapingItUp();
                    goToStore();
                    break;
                case MonInfo.STORING_IT_FOR_TRANSFER:
                    //batchReadyForTransfer
                    
                    //backToWork
                    
                    break;
                case MonInfo.CONTACTING_THE_ENTREPRENEUR:
                    //contact entrepreneur
                    
                    //backToWork
            }
        }
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
