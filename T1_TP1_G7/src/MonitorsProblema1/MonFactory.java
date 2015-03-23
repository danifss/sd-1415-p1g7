package MonitorsProblema1;

/**
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
     * Factory where Craftmans will work
     * 
     * @param nPrimeMaterials
     * @param nPrimePerProduct
     */
    public MonFactory(int nPrimeMaterials, int nPrimePerProduct, int nPrimeRestock) {
        this.nPrimeMaterials = nPrimeMaterials;
        this.nPrimePerProduct = nPrimePerProduct;
        this.nPrimeRestock = nPrimeRestock;
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
     * Prime Materials Needed
     */
    public synchronized void primeMaterialsNeeded(){
        
    }

    /**
     * Back To Work
     */
    public synchronized void backToWork(){
        
    }
}
