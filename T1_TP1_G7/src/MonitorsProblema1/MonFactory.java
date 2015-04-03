package MonitorsProblema1;

/**
 * This class is responsible to host the Factory/Workshop
 * 
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonFactory implements MonFactoryInterface {
    
    /**
     * General Repository
     * 
     * @serialField info
     */
    private final MonInfo info;
    
    
    
    // Variables that need to be used in the repository
    /**
     * Amount of prime materials presently in the Factory
     * @serialField nPrimeMaterialsInFactory
     */
    private int nPrimeMaterialsInFactory;
    
    /**
     * Amount of products in Factory to be delivered to the Shop by the Owner
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
    
    
    
    // Variables that don't need to be used in the repository
    /**
     * Total number of prime materials in the Storage at the beginning
     * @serialField nTotalPrime
     */
    private final int nTotalPrime;
    
    /**
     * Amount of prime materials at the beginning in the factory
     * @serialField nInitialPrime
     */
    private final int nInitialPrime;
    
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
     * Constructor of the Factory where Craftmans will work
     * 
     * @param info General Repository
     * @param nPrimeMaterialsInFactory Amount of prime materials available in the Factory at the beginning
     * @param nTotalPrime Total number of prime materials available in the storage what will be delivered
     * @param nPrimePerProduct Number of prime materials needed to produce a new product
     * @param nPrimeRestock Minimum number of prime materials in stock to call owner to restock
     * @param nProductsCollect Maximum number of finished products that the owner can collect
     */
    public MonFactory(MonInfo info, int nPrimeMaterialsInFactory, int nTotalPrime, int nPrimePerProduct, int nPrimeRestock, int nProductsCollect) {
        // Repository
        this.info = info;
        
        // Variables that need to be used in the repository
        this.nPrimeMaterialsInFactory = nPrimeMaterialsInFactory;
        nFinishedProductsInFactory = 0;
        nSuppliedTimes = 0;
        nPrimeMaterialsSupplied = 0;
        nProductsManufactured = 0;
        
        // Variables that don't need to be used in the repository
        this.nTotalPrime = nTotalPrime;
        nInitialPrime = nPrimeMaterialsInFactory;
        this.nPrimePerProduct = nPrimePerProduct;
        this.nPrimeRestock = nPrimeRestock;
        this.nProductsCollect = nProductsCollect;
        flagPrimeCall = false;
        flagNProductsCall = 0;
    }

    /**
     * Check if the Craftman needs to contact owner to bring prime materials.
     * The Craftman needs to contact the Owner if the number of prime materials available in the Factory
     * is less than the minimum number of prime materials in stock to call owner to restock, and the
     * number of prime materials supplied is less than the total number of prime materials available
     * in the storage at the beginning.
     * @return true if needs to restock
     */
    @Override
    public synchronized boolean checkForRestock(){
        return (nPrimeMaterialsInFactory < nPrimeRestock) && (nPrimeMaterialsSupplied < nTotalPrime);
    }
    
    /**
     * The Craftman checks if the Factory has prime materials to collect.
     * If the number of prime materials in the Factory is less than the number of prime
     * materials needed per product, the Craftman will wait until the Owner brings new
     * prime materials and wakes up the Craftman.
     * If all prime materials were already supplied, the Craftman will not wait and
     * he will stop working
     * @return true if has materials
     */
    @Override
    public synchronized boolean checkForMaterials(){
        try{
            while(nPrimeMaterialsInFactory<nPrimePerProduct && !endOfPrimeMaterials()){
                wait();
                Thread.sleep(1000);
            }
        }catch(Exception e){}
        
        // Return always true if endOfPrimeMaterials is false
        return nPrimeMaterialsInFactory >= nPrimePerProduct;
    }
    
    /**
     * The Craftman collects prime materials.
     * He checks again if there is prime materials (to avoid that someone before him
     * already took his prime materials), and then he collects, decreasing the number of
     * pieces collected in the number of prime materials available in the Factory.
     * @return number of collected prime materials
     */
    @Override
    public synchronized int collectMaterials() {
        // Ensure that there is prime materials to collect
        if(checkForMaterials())
        {
            nPrimeMaterialsInFactory -= nPrimePerProduct;
            // Store the values in the general repository
            info.setnPrimeMaterialsInFactory(nPrimeMaterialsInFactory);
            return nPrimePerProduct;
        }
        return 0;
    }
    
    /**
     * The Craftman stores the products produced.
     * The Craftman adds the number of products produced that he has at the moment
     * to the number of finished products in the Factory and to the number of the total
     * products produced.
     * @param nProd Number of products that the Craftman has
     * @return number of products the Craftman stored
     */
    @Override
    public synchronized int goToStore(int nProd){
        nFinishedProductsInFactory += nProd;
        nProductsManufactured += nProd;
        
        // Store the values in the general repository
        info.setnFinishedProductsInFactory(nFinishedProductsInFactory);
        info.setnProductsManufactured(nProductsManufactured);
        
        return nProd;
    }
    
    /**
     * The Craftman indicates that the owner has products to collect.
     * He increments the number of flagNProductsCall to tell that the Owner needs to
     * come to the Factory flagNProductsCall times to collect products.
     */
    @Override
    public synchronized void batchReadyForTransfer(){
        flagNProductsCall += 1;
    }
    
    /**
     * The Craftman verifies if he needs to contact the owner to collect products.
     * Checks if the number of finished products in factory divided by the number of
     * products that the owner can collect is different of flagNProductsCall (integer division).
     * He also checks if he made the last product. If one of the conditions are true,
     * he needs to contact the Owner.
     * @return true if he needs to contact
     */
    @Override
    public synchronized boolean checkContactProduct(){
        return (nFinishedProductsInFactory / nProductsCollect != flagNProductsCall) || ((nTotalPrime+nInitialPrime) / nPrimePerProduct == nProductsManufactured);
    }
    
    /**
     * The Craftman turns true the flag that indicates that prime materials are needed.
     * He also ensure that none of the Craftmans already contacted the Owner.
     * @return true if he contacted the owner
     */
    @Override
    public synchronized boolean primeMaterialsNeeded(){
        if(flagPrimeCall){
            return false;
        }
        flagPrimeCall = true;
        return true;
    }
    
    /**
     * The Craftman sees if someone already contacted the owner to restock prime materials.
     * @return true if someone already contacted the owner
     */
    @Override
    public synchronized boolean flagPrimeActivated(){
        return flagPrimeCall;
    }
    
    /**
     * Craftman sees how many prime materials needs to produce a new product
     * @return number of prime materials needed per products
     */
    @Override
    public int getnPrimePerProduct() {
        return nPrimePerProduct;
    }
    
    /**
     * Owner goes to factory to collect finished products.
     * If he can collect all the products (depends of the number of products that the
     * owner can carry), he collect all products setting the number of finished products
     * in Factory to zero. If he can't collect all the products, he collect the number
     * of products he can, decreasing from the number of finished products the number 
     * of products collected. He also decreases the flagNProductsCall.
     * @return number of products collected
     */
    @Override
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
        // Store the values in the general repository
        info.setnFinishedProductsInFactory(nFinishedProductsInFactory);
        return res;
    }
    
    /**
     * Owner brings prime materials.
     * He increases the number of prime materials in Factory, and also the total number
     * of prime materials supplied and the number of times he came to the Factory.
     * @param nPrimeMaterials Amount of prime materials to restock
     */
    @Override
    public synchronized void replenishStock(int nPrimeMaterials){
        nPrimeMaterialsInFactory += nPrimeMaterials;
        nPrimeMaterialsSupplied += nPrimeMaterials;
        nSuppliedTimes += 1;
        flagPrimeCall = false;
        
        // Store the values in the general repository
        info.setnPrimeMaterialsInFactory(this.nPrimeMaterialsInFactory);
        info.setnSuppliedTimes(nSuppliedTimes);
        info.setnPrimeMaterialsSupplied(nPrimeMaterialsSupplied);

        notifyAll();
    }
    
    /**
     * Checks if the all the prime materials from the storage were supplied.
     * This function helps the Craftman to know if he can stop working.
     * @return true if there is no more prime materials in the storage
     */
    @Override
    public boolean endOfPrimeMaterials(){
        return (nPrimeMaterialsSupplied == nTotalPrime);
    }
}
