package MonitorsProblema1;

/**
 * This class is responsible to host the Factory/Workshop
 * 
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonFactory {
    
    /**
     * General Repository
     * 
     * @serialField shop
     */
    private final MonInfo info;
    
    
    // Variáveis que necessitam ser usadas no repositório
    
    /**
     * Amount of prime materials presently in the workshop
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterialsInFactory;
    
    /**
     * Number of products in Factory to be delivered to the Shop by Owner
     * @serialField nFinishedProductsInFactory
     */
    private int nFinishedProductsInFactory;
    
    /**
     * Number of times that a supply of prime materials was delivered to the workshop
     * @serialField nSuppliedTimes
     */
    private int nSuppliedTimes;
    
    /**
     * Total number of prime materials delivered
     * @serialField nPrimeMaterialsSupplied
     */
    private int nPrimeMaterialsSupplied;
   
    /**
     * Total number of products that have already been manufactured (accumulation)
     * @serialField nProductsManufactured
     */
    private int nProductsManufactured;
    
    
    // Variáveis que não são necessárias no repositório geral
    
    /**
     * Total number of prime materials in the Storage at the beginning
     * @serialField nTotalPrime
     */
    private final int nTotalPrime;
    
    /**
     * Number of prime materials needed to produce a new product
     * @serialField nPrimePerProduct
     */
    private final int nPrimePerProduct;
    
    /**
     * Minimum number of prime materials in stock to call owner to restock
     * @serialField nPrimeRestock
     */
    private final int nPrimeRestock;
    
    /**
     * Maximum number of finished products that the owner can collect
     * @serialField nProductsCollect
     */
    private final int nProductsCollect;
    
    /**
     * Maximum number of Products in Factory
     * 
     * @serialField nLimitOfProductsInFactory
     */
    private final int nLimitOfProductsInFactory;
    
    /**
     * Flag to see if the owner was already contacted to bring prime materials
     * @serialField primeCall
     */
    private boolean flagPrimeCall;
    
    /**
     * Flag to see how many times the owner was contacted to collect finished products
     * @serialField nProductsCall
     */
    private int flagNProductsCall;
    
    
    /**
     * Factory where Craftmans will work
     * 
     * @param info
     * @param nPrimeMaterials
     * @param nPrimePerProduct
     * @param nPrimeRestock
     * @param nLimitOfProductsInFactory
     * @param nProductsCollect
     * @param nTotalPrime
     */
    public MonFactory(MonInfo info, int nPrimeMaterials, int nPrimePerProduct, int nPrimeRestock, int nLimitOfProductsInFactory, int nProductsCollect, int nTotalPrime) {
        this.info = info;
        this.nPrimeMaterialsInFactory = nPrimeMaterials;
        this.nPrimePerProduct = nPrimePerProduct;
        this.nPrimeRestock = nPrimeRestock;
        this.nLimitOfProductsInFactory = nLimitOfProductsInFactory;
        this.nProductsCollect = nProductsCollect;
        this.nTotalPrime = nTotalPrime;
        nPrimeMaterialsSupplied = 0;
        nFinishedProductsInFactory = 0;
        nSuppliedTimes = 0;
        nProductsManufactured = 0;
        flagPrimeCall = false;
        flagNProductsCall = 0;
    }

    /**
     * Check if the Craftman needs to contact owner to bring prime materials
     * @return true if needs to restock
     */
    public synchronized boolean checkForRestock(){
        return nPrimeMaterialsInFactory < nPrimeRestock;
    }
    
    /**
     * Check For Materials
     * @return true if has materials
     */
    public synchronized boolean checkForMaterials(){
        try{
            while(nPrimeMaterialsInFactory<nPrimePerProduct && !endOper()){
                wait();
                Thread.sleep(1000);
            }
        }catch(Exception e){}
        
        // Return always true if endOper is false
        return nPrimeMaterialsInFactory >= nPrimePerProduct;
    }
    
    /**
     * Collecting prime materials
     * @return number of collected prime materials
     */
    public synchronized int collectMaterials() {
        // Garante que há matérias primas para buscar
        if(checkForMaterials())
        {
            nPrimeMaterialsInFactory -= nPrimePerProduct;
            info.setnPrimeMaterialsInFactory(nPrimeMaterialsInFactory);
            return nPrimePerProduct;
        }
        return 0;
    }
    
    /**
     * Craftman store the products produced and increment total produced products
     * @param nProd Number of products the Craftman has
     * @return number of products the Craftman stored
     */
    public synchronized int goToStore(int nProd){
        nFinishedProductsInFactory += nProd;
        nProductsManufactured += nProd;
        
        //Guarda os valores no repositório geral
        info.setnFinishedProductsInFactory(nFinishedProductsInFactory);
        info.setnProductsManufactured(nProductsManufactured);
        
        return nProd;
    }
    
    /**
     * Indicates that the owner has products to collect
     */
    public synchronized void batchReadyForTransfer(){
        flagNProductsCall += 1;
    }
    
    /**
     * Verifies if the Craftman needs to contact the owner
     * @return true if he needs to contact
     */
    public synchronized boolean checkContactProduct(){
        // Caso o número de produtos acabados a dividir pelo número de produtos que a owner pode levar
        // for diferente da quantidade de vezes que a flag foi ativada, significa que o Craftman necessita
        // que tem mais nProductsCollect para serem levados para a loja (Divisão de inteiros)
        return nFinishedProductsInFactory / nProductsCollect != flagNProductsCall;
    }
    
    /**
     * Prime Materials Needed
     */
    public synchronized void primeMaterialsNeeded(){
        flagPrimeCall = true;
    }
    
    /**
     * The Craftman sees if someone already contacted the owner to restock the prime materials
     * @return 
     */
    public synchronized boolean flagPrimeActivated(){
        return flagPrimeCall;
    }
    
    /**
     * Returns number of prime materials needed per product
     * @return number 
     */
    public int getnPrimePerProduct() {
        return nPrimePerProduct;
    }
    
    /**
     * Owner goes to factory to collect finished products
     * @return number of products collected
     */
    public synchronized int goToWorkshop(){
        int res;
        if(nFinishedProductsInFactory <= nProductsCollect){
            res = nFinishedProductsInFactory;
            nFinishedProductsInFactory = 0;
        }else{
            res = nProductsCollect;
            nFinishedProductsInFactory -= nProductsCollect;
        }
        flagNProductsCall -= 1;
        info.setnFinishedProductsInFactory(nFinishedProductsInFactory);
        return res;
    }
    
    /**
     * Owner brings prime materials
     * @param nPrimeMaterials 
     */
    public synchronized void replenishStock(int nPrimeMaterials){
        this.nPrimeMaterialsInFactory += nPrimeMaterials;
        nPrimeMaterialsSupplied += nPrimeMaterials;
        nSuppliedTimes += 1;
        flagPrimeCall = false;
        
        //Guarda os valores no repositório geral
        info.setnPrimeMaterialsInFactory(this.nPrimeMaterialsInFactory);
        info.setnSuppliedTimes(nSuppliedTimes);
        info.setnPrimeMaterialsSupplied(nPrimeMaterialsSupplied);

        notifyAll();
    }
    
    /**
     * Sees if the Craftman can stop
     * @return true if the Craftman can stop
     */
    public boolean endOper(){
        return nPrimeMaterialsSupplied == nTotalPrime;
    }
}
