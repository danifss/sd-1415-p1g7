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
    private final int nPrimePerProduct;
    
    /**
     * Minimum number of prime materials in stock to call owner to restock
     * @serialField nPrimeRestock
     */
    private final int nPrimeRestock;
    
    /**
     * Flag to see if the owner was already contacted to bring prime materials
     * @serialField primeCall
     */
    private boolean flagPrimeCall;
    
    /**
     * Number of products in Factory to be delivered to the Shop by Owner
     * @serialField nFinishedProductsInFactory
     */
    private int nFinishedProductsInFactory;
    
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
     * Flag to see how many times the owner was contacted to collect finished products
     * @serialField nProductsCall
     */
    private int flagNProductsCall;
    
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
        flagPrimeCall = false;
        flagNProductsCall = 0;
    }

    /**
     * Check if the Craftman needs to contact owner to bring prime materials
     */
    public synchronized boolean checkForRestock(){
        return nPrimeMaterials < nPrimeRestock;
    }
    
    /**
     * Check For Materials
     */
    public synchronized void checkForMaterials(){
        try{
            while(nPrimeMaterials<nPrimePerProduct){
                wait();
                Thread.sleep(1000);
            }
        }catch(Exception e){}
    }
    
    /**
     * Collecting prime materials
     * @return number of collected prime materials
     */
    public synchronized int collectMaterials() {
        // Garante que há matérias primas para buscar
        checkForMaterials();
        nPrimeMaterials -= nPrimePerProduct;
        return nPrimeMaterials;
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
        return res;
    }
    
    /**
     * Owner brings prime materials
     * @param nPrimeMaterials 
     */
    public synchronized void replenishStock(int nPrimeMaterials){
        this.nPrimeMaterials += nPrimeMaterials;
        flagPrimeCall = false;
    }
}
