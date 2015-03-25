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
     * Number of prime materials available
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterials;
    
    /**
     * Number of prime materials needed to produce a new product
     * @serialField nPrimePerProduct
     */
    private int nPrimePerProduct;
    
    /**
     * Minimum number of prime materials in stock to call owner to restock
     * @serialField nPrimeRestock
     */
    private int nPrimeRestock;
    
    /**
     * Flag to see if the owner was already contacted to bring prime materials
     * @serialField primeCall
     */
    private boolean primeCall;
    
    /**
     * Number of products in Factory to be delivered to the Shop by Owner
     * @serialField nFinishedProductsInFactory
     */
    private int nFinishedProductsInFactory;
    
    /**
     * Maximum number of finished products that the owner can collect
     * @serialField nProductsCollect
     */
    private int nProductsCollect;
    
    /**
     * Maximum number of Products in Factory
     * 
     * @serialField nLimitOfProductsInFactory
     */
    private int nLimitOfProductsInFactory;
    
    /**
     * Flag to see how many times the owner was contacted to collect finished products
     * @serialField nProductsCall
     */
    private int nProductsCall;
    
    /**
     * Factory where Craftmans will work
     * 
     * @param nPrimeMaterials
     * @param nPrimePerProduct
     * @param nPrimeRestock
     * @param nLimitOfProductsInFactory
     */
    public MonFactory(int nPrimeMaterials, int nPrimePerProduct, int nPrimeRestock, int nLimitOfProductsInFactory, int nProductsCollect) {
        this.nPrimeMaterials = nPrimeMaterials;
        this.nPrimePerProduct = nPrimePerProduct;
        this.nPrimeRestock = nPrimeRestock;
        this.nLimitOfProductsInFactory = nLimitOfProductsInFactory;
        this.nProductsCollect = nProductsCollect;
        nFinishedProductsInFactory = 0;
        primeCall = false;
        nProductsCall = 0;
    }

    /**
     * Check For Materials
     * @return true or false
     */
    public synchronized boolean checkForMaterials(){
        return nPrimeMaterials >= nPrimePerProduct;
    }
    
    /**
     * Collecting prime materials
     * @return number of collected prime materials
     */
    public synchronized int collectMaterials() {
        // Falta acabar
        if(nPrimeMaterials>=nPrimePerProduct){
            nPrimeMaterials -= nPrimePerProduct;
            return nPrimeMaterials;
        }
        return 0;
    }
    
    /**
     * Craftman store the products produced
     * @param nProd Number of products the Craftman has
     * @return number of products the Craftman stored
     */
    public synchronized int goToStore(int nProd){
        nFinishedProductsInFactory += nProd;
        return nProd;
    }
    
    /**
     * Prime Materials Needed
     */
    public synchronized void primeMaterialsNeeded(){
        
    }

      
    /**
     * Owner goes to factory to collect finished products
     * @return number of products collected
     */
    public synchronized int getProducts(){
        int res;
        if(nFinishedProductsInFactory <= nProductsCollect){
            res = nFinishedProductsInFactory;
            nFinishedProductsInFactory = 0;
        }else{
            res = nProductsCollect;
            nFinishedProductsInFactory -= nProductsCollect;
        }
        nProductsCall -= 1;
        return res;
    }
    
    /**
     * Returns number of prime materials needed per product
     * @return number 
     */
    public int getnPrimePerProduct() {
        return nPrimePerProduct;
    }
}
