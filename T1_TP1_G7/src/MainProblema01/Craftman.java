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
public class Craftman extends Thread implements CraftmanInterface {
    
    /**
     * Craftman States
     */
    private final static int
            FETCHING_PRIME_MATERIALS = 0,
            PRODUCING_A_NEW_PIECE = 1,
            STORING_IT_FOR_TRANSFER = 2,
            CONTACTING_THE_ENTREPRENEUR = 3;
    
    /**
     * Factory/Workshop
     * @serialField factory
     */
    private final MonFactory factory;
        
    /**
     * Shop
     * @serialField shop
     */
    private final MonShop shop;
    
    /**
     * General Repository
     * @serialField shop
     */
    private final MonInfo info;
    
    
    
    // Variables that need to be used in the repository
    /**
     * Craftman internal state
     * 
     * @serialField stateCraftman
     */
    private int stateCraftman;
    
    /**
     * Craftman thread id
     * 
     * @serialField craftmanId
     */
    private final int craftmanId;
    
    /**
     * Number of total products he produced
     * @serialField totalProduced
     */
    private int totalProduced;
    
    
    
    // Variables that don't need to be used in the repository
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
     * Create Craftman thread
     * 
     * @param craftmanId Craftman identity
     * @param factory Factory
     * @param shop Shop
     * @param info Repository
     */
    public Craftman(int craftmanId, MonFactory factory, MonShop shop, MonInfo info){
        this.craftmanId = craftmanId;
        this.factory = factory;
        this.shop = shop;
        this.info = info;
        stateCraftman = FETCHING_PRIME_MATERIALS;
        nPrimeMaterials = 0;
        nProduct = 0;
        totalProduced = 0;
    }
    
    /**
     * Life cycle of the Craftman.
     */
    @Override
    public void run(){
        System.out.println("Iniciado o Craftman: "+craftmanId);
        while(!endOper()){
            switch(stateCraftman){
                case FETCHING_PRIME_MATERIALS:
                    if(factory.checkForRestock() && !factory.flagPrimeActivated()){
                        primeMaterialsNeeded();
                        System.out.printf("Artesao %d\t- A pedir materia prima.\n",craftmanId);
                    }else{
                        if(checkForMaterials()){
                            collectMaterials();
                            if(nPrimeMaterials != 0){
                                prepareToProduce();
                                System.out.printf("Artesao %d\t- Obteu %d materias primas.\n",craftmanId,nPrimeMaterials);
                            }
                        }
                    }
                    break;
                case PRODUCING_A_NEW_PIECE:
                    shapingItUp();
                    goToStore();
                    System.out.printf("Artesao %d\t- Produziu um produto.\n",craftmanId);
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
        System.out.println("Terminado o Craftman: "+craftmanId);
    }
    
    /**
     * He goes to the factory to see if he can collect prime materials.
     */
    private boolean checkForMaterials(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return factory.checkForMaterials();
    }
    
    /**
     * He goes to the factory to collect prime materials.
     * He adds to the number of prime materials he has the number of prime materials
     * collected.
     */
    private void collectMaterials(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        nPrimeMaterials += factory.collectMaterials();
    }
    
    /**
     * He prepares to produce a new piece.
     */
    private void prepareToProduce(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCraftmanState(PRODUCING_A_NEW_PIECE);
    }
    
    /**
     * He produces a new products.
     * He uses the prime materials he has to produce a new piece. He decrements the number
     * of prime materials he has to the number of prime materials used, and increments
     * the number of products he has at the moments and the total number of products
     * produced.
     */
    private void shapingItUp(){
        try{
            sleep((long) (100+20*Math.random()));
        }catch(InterruptedException e){}
        nPrimeMaterials -= factory.getnPrimePerProduct();
        nProduct += 1;
        totalProduced += 1;
        info.setnGoodsCraftedByCraftman(craftmanId, totalProduced);
    }
    
    /**
     * He stores the product produced, decreasing the number of products he has
     * with him at the moment.
     */
    private void goToStore(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        setCraftmanState(STORING_IT_FOR_TRANSFER);
        nProduct -= factory.goToStore(nProduct);
    }
    
    /**
     * He contacts the Owner to say that he has products in the Factory to collect.
     * He goes to the factory to activate the flag (to prevent other Craftmans from
     * calling the Owner), and then goes to the shop to contact the Owner.
     */
    private void batchReadyForTransfer(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCraftmanState(CONTACTING_THE_ENTREPRENEUR);
        factory.batchReadyForTransfer();
        shop.batchReadyForTransfer();
    }
    
    /**
     * He goes back to work to produce a new product.
     */
    private void backToWork(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCraftmanState(FETCHING_PRIME_MATERIALS);
    }
    
    /**
     * He contacts the Owner to say that prime materials is needed in the Factory.
     * First he goes to the Factory to check if someone already contacted the Owner,
     * if someone already done it, he doesn't do anything, if nobody done it, he goes
     * to the Shop and contacts the Owner.
     */
    private void primeMaterialsNeeded(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        if(factory.primeMaterialsNeeded()){
            setCraftmanState(CONTACTING_THE_ENTREPRENEUR);
            shop.primeMaterialsNeeded();
        }
    }
    
    /**
     * Verifies if the Craftman can stop working.
     * He goes to the factory to check if all prime materials were supplied, he checks
     * if there is no prime materials available in the Factory, his state should
     * be the initial state and he should not have any prime material with him (if he
     * has, he needs to produce the new product).
     * @return true if needs to stop
     */
    private boolean endOper() {
        // valida se o craftman deve terminar ou nao
        return factory.endOfPrimeMaterials() && !checkForMaterials() && (stateCraftman==FETCHING_PRIME_MATERIALS) && (nPrimeMaterials==0);
    }
    
    /**
     * Function to change the Craftman state (internal and in the repository).
     * @param state State of the Craftman
     */
    private void setCraftmanState(int state){
        stateCraftman = state;
        info.setCraftmanState(craftmanId, state);
    }
}
