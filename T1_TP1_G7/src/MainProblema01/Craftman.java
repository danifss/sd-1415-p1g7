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
    private final int craftmanId;
    
    /**
     * Number of prime materials collected
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterials;
    
    /**
     * Number of products he has after producing
     * @serialField nProduct
     */
    private int nProduct;
    
    /**
     * Number of total products he produced
     * @serialField totalProduced
     */
    private int totalProduced;
    
    /**
     * Factory/Workshop
     * 
     * @serialField factory
     */
    private final MonFactory factory;
        
    /**
     * Shop
     * 
     * @serialField shop
     */
    private final MonShop shop;

    /**
     * Storage
     * 
     * @serialField storage
     */
    private final MonStorage storage;
    
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
    public Craftman(int craftmanId, MonFactory factory, MonShop shop, MonStorage storage, MonInfo info){
        this.craftmanId = craftmanId;
        this.factory = factory;
        this.shop = shop;
        this.storage = storage;
        this.info = info;
        craftmanState = MonInfo.FETCHING_PRIME_MATERIALS;
        nPrimeMaterials = 0;
        nProduct = 0;
        totalProduced = 0;
    }
    
    /**
     * Life cycle of the Craftman
     */
    @Override
    public void run(){
        while(!endOper()){
            switch(craftmanState){
                case FETCHING_PRIME_MATERIALS:
                    if(factory.checkForRestock() && !factory.flagPrimeActivated()){
                        primeMaterialsNeeded();
                    }else{
                        // Craftman verifica se há materias para produzir um novo produto
                        checkForMaterials();
                        // Craftman coleta os materiais
                        collectMaterials();
                        // Craftman prepara para produzir
                        prepareToProduce();
                    }
                    break;
                case PRODUCING_A_NEW_PIECE:
                    // Produz uma nova peça
                    shapingItUp();
                    // Dirige-se a zona de armazenamento
                    goToStore();
                    break;
                case STORING_IT_FOR_TRANSFER:
                    if(factory.checkContactProduct()){
                        batchReadyForTransfer();
                    }else{
                        backToWork();
                    }
                    break;
                case CONTACTING_THE_ENTREPRENEUR:                
                    backToWork();
                    break;
            }
        }
        System.out.println("Terminou o Craftman "+craftmanId);
    }
    
    /**
     * Check for materials
     */
    private void checkForMaterials(){
        factory.checkForMaterials();
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
        craftmanState = PRODUCING_A_NEW_PIECE;
    }
    
    /**
     * Producing new piece
     */
    private void shapingItUp(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        nPrimeMaterials -= factory.getnPrimePerProduct();
        nProduct += 1;
        totalProduced += 1;
    }
    
    /**
     * Goes to store
     */
    private void goToStore(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        craftmanState = STORING_IT_FOR_TRANSFER;
        nProduct -= factory.goToStore(nProduct);
    }
    
    private void batchReadyForTransfer(){
        craftmanState = CONTACTING_THE_ENTREPRENEUR;
        factory.batchReadyForTransfer();
        shop.batchReadyForTransfer();
    }
    
    private void backToWork(){
        craftmanState = FETCHING_PRIME_MATERIALS;
    }
    
    private void primeMaterialsNeeded(){
        craftmanState = CONTACTING_THE_ENTREPRENEUR;
        factory.primeMaterialsNeeded();
        shop.primeMaterialsNeeded();
    }
    
	private boolean endOper() {
		// valida se o craftman deve terminar ou nao
		if(!storage.isPrimeMaterialsAvailabe() && (factory.getnTotalProductsMade() < nMaxProductsToDo()))
            return true; // continue alive
        return false; // die!
	}
    
    /**
     * 
     * @return Number of products that still will be produced
     */
    private int nMaxProductsToDo(){
        return storage.getnMaxPrimeMaterialsToDeliver()/factory.getnTotalProductsMade();
    }
}
