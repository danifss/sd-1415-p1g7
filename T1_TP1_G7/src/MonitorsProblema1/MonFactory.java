package MonitorsProblema1;

/**
 * This class is responsable to host the Factory/Workshop
 * 
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonFactory {
    
    /**
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterials;
    
    /**
     * Number of prime materials needed to produce a new product
     * @serialField nPrimePerProduct
     */
    private int nPrimePerProduct;
    
    /**
     * Maximum number of prime materials in stock to call owner to restock
     * @serialField nPrimeRestock
     */
    private int nPrimeRestock;
    
    /**
     * Maximum number of Products in Factory
     * 
     * @serialField nLimitOfProductsInFactory
     */
    private final int nLimitOfProductsInFactory;
    private int nFinishedProductsInFactory;
    
    /**
     * Factory where Craftmans will work
     * 
     * @param nPrimeMaterials
     * @param nPrimePerProduct
     * @param nPrimeRestock
     * @param nLimitOfProductsInFactory
     */
    public MonFactory(int nPrimeMaterials, int nPrimePerProduct, int nPrimeRestock, int nLimitOfProductsInFactory) {
        this.nPrimeMaterials = nPrimeMaterials;
        this.nPrimePerProduct = nPrimePerProduct;
        this.nPrimeRestock = nPrimeRestock;
        this.nLimitOfProductsInFactory = nLimitOfProductsInFactory;
        
        this.nFinishedProductsInFactory = 0;
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
     * Back To Work
     */
    public synchronized void backToWork(){
        
    }

    public int getnFinishedProductsInFactory() {
        return nFinishedProductsInFactory;
    }

    public synchronized void setnFinishedProductsInFactory(int nFinishedProductsInFactory) {
        this.nFinishedProductsInFactory = nFinishedProductsInFactory;
    }
    
    
    /**
     * Returns number of prime materials needed per product
     * @return number 
     */
    public int getnPrimePerProduct() {
        return nPrimePerProduct;
    }
    
}
