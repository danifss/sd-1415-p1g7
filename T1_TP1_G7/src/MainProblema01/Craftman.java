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
    
    
    
    // Variáveis que necessitam ser usadas no repositório
    
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
    
    
    
    // Variáveis que não são necessárias no repositório geral
    
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
     * Life cycle of the Craftman
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
                        // Craftman verifica se há materias para produzir um novo produto
                        if(checkForMaterials()){
                            // Craftman coleta os materiais
                            collectMaterials();
                            // Craftman prepara para produzir
                            if(nPrimeMaterials != 0){
                                prepareToProduce();
                                System.out.printf("Artesao %d\t- Obteu %d materias primas.\n",craftmanId,nPrimeMaterials);
                            }
                        }
                    }
                    break;
                case PRODUCING_A_NEW_PIECE:
                    // Produz uma nova peca
                    shapingItUp();
                    // Dirige-se a zona de armazenamento
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
     * Check for materials
     */
    private boolean checkForMaterials(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return factory.checkForMaterials();
    }
    
    /**
     * Collect materials
     */
    private void collectMaterials(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        nPrimeMaterials += factory.collectMaterials();
    }
    
    /**
     * Prepare to produce
     */
    private void prepareToProduce(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCraftmanState(PRODUCING_A_NEW_PIECE);
    }
    
    /**
     * Producing new piece
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
     * Goes to store
     */
    private void goToStore(){
        try{
            sleep((long) (1+40*Math.random()));
        }catch(InterruptedException e){}
        setCraftmanState(STORING_IT_FOR_TRANSFER);
        nProduct -= factory.goToStore(nProduct);
    }
    
    /**
     * Batch ready for transfer
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
     * Back to work
     */
    private void backToWork(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setCraftmanState(FETCHING_PRIME_MATERIALS);
    }
    
    /**
     * Prime materials needed
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
     * Verifies if the Craftman stops
     * @return true if needs to stop
     */
    private boolean endOper() {
        // valida se o craftman deve terminar ou nao
        return factory.endOfPrimeMaterials() && !checkForMaterials() && (stateCraftman==FETCHING_PRIME_MATERIALS) && (nPrimeMaterials==0);
    }
    
    /**
     * Function to change the Craftman state (internal and in the repository)
     * @param state State of the Craftman
     */
    private void setCraftmanState(int state){
        stateCraftman = state;
        info.setCraftmanState(craftmanId, state);
    }
}
