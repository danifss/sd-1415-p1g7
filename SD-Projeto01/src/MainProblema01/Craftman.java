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
public class Craftman extends Thread implements CraftmanInterface{
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
     * Create craftman thread
     * 
     * @param craftmanId Craftman identity
     * @param factory Factory
     */
    public Craftman(int craftmanId, MonFactory factory){
        this.craftmanId = craftmanId;
        this.factory = factory;
    }
    
    /**
     * Life cycle of the craftman
     */
    @Override
    public void run(){
        while(true){
			if(!factory.collectMaterials()){ // if can not collect materials
				primeMaterialsNeeded(); // request prime materials
				backToWork(); // return to work
			} else { // Carftman could collect the materials
				prepareToProduce(); // preparing to produce
				backToWork(); // return to work
			}
		}
    }
    
    /**
     * Producing new piece
     */
    public void shapingItUp(){
        
    }
    
    /**
     * Goes to store
     */
    public void goToStore(){
        
    }
    
}
