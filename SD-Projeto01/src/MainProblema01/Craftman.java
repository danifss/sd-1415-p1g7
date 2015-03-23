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
     * CRAFTSMAN STATES
     */
    public final static int
            FETCHING_PRIME_MATERIALS = 0,
            PRODUCING_A_NEW_PIECE = 1,
            STORING_IT_FOR_TRANSFER = 2,
            CONTACTING_THE_ENTREPRENEUR = 3;
    
    /**
     * Craftman internal state
     * 
     * @serialField craftmanState
     */
    private int craftmanState;
    
    /**
     * Craftman thread id
     * 
     * @serialField craftmanId
     */
    private int craftmanId;
    
    /**
     * Number of prime materials collected
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterials;
    
    /**
     * Factory/Workshop
     * 
     * @serialField factory
     */
    private MonFactory factory;

    /**
     * General Repository
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
        craftmanState = MonInfo.FETCHING_PRIME_MATERIALS;
        nPrimeMaterials = 0;
    }
    
    /**
     * Life cycle of the Craftman
     */
    @Override
    public void run(){
        while(true){
            switch(info.getStateCraftsman(craftmanId)){
                case MonInfo.FETCHING_PRIME_MATERIALS:
                    if(checkForMaterials()){
                        collectMaterials();
                         
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
     * Check for materials
     * @return true if has materials
     */
    private boolean checkForMaterials(){
        return factory.checkForMaterials();
    }
    
    /**
     * Collect materials
     */
    private void collectMaterials(){
        nPrimeMaterials += factory.collectMaterials();
    }
    
    /**
     * Prepare to produce
     */
    private void prepareToProduce(){
        
    }
    
    /**
     * Producing new piece
     */
    private void shapingItUp(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
    /**
     * Goes to store
     */
    private void goToStore(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
    }
    
    private void batchReadyForTransfer(){
        
    }
    
    private void backToWork(){
        
    }
    
    private void primeMaterialsNeeded(){
        
    }
}