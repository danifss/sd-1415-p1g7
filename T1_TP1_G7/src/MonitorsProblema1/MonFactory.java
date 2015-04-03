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
     * Check if the Craftman needs to contact owner to bring prime materials
     * The Craftman needs to contact the Owner if the number of prime materials available in the Factory
     * is less than the minimum number of prime materials in stock to call owner to restock, and the
     * number of prime materials supplied is less than the total number of prime materials available
     * in the storage at the beginning
     * @return true if needs to restock
     */
    @Override
    public synchronized boolean checkForRestock(){
        return (nPrimeMaterialsInFactory < nPrimeRestock) && (nPrimeMaterialsSupplied < nTotalPrime);
    }
    
    /**
     * Check For Materials
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
     * Collecting prime materials
     * @return number of collected prime materials
     */
    @Override
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
    @Override
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
    @Override
    public synchronized void batchReadyForTransfer(){
        flagNProductsCall += 1;
    }
    
    /**
     * Verifies if the Craftman needs to contact the owner
     * @return true if he needs to contact
     */
    @Override
    public synchronized boolean checkContactProduct(){
        // Caso o número de produtos acabados a dividir pelo número de produtos que a owner pode levar
        // for diferente da quantidade de vezes que a flag foi ativada, significa que o Craftman necessita
        // que tem mais nProductsCollect para serem levados para a loja (Divisão de inteiros)
        return (nFinishedProductsInFactory / nProductsCollect != flagNProductsCall) || (((nTotalPrime+nInitialPrime) / nPrimePerProduct == nProductsManufactured) && (nFinishedProductsInFactory > 0));
    }
    
    /**
     * Turn true the flag that indicates that prime materials is needed
     * @return if it is really needed to call the owner
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
     * The Craftman sees if someone already contacted the owner to restock the prime materials
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
     * Owner goes to factory to collect finished products
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
        info.setnFinishedProductsInFactory(nFinishedProductsInFactory);
        return res;
    }
    
    /**
     * Owner brings prime materials
     * @param nPrimeMaterials Amount of prime materials to restock
     */
    @Override
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
     * Sees if have more products to collect
     * @return true if no more products to collect
     */
    @Override
    public boolean endProductsToCollect(){
        return nFinishedProductsInFactory == 0;
    }
    
    /**
     * Sees if have more prime materials
     * @return true if no more prime materials
     */
    @Override
    public boolean endOfPrimeMaterials(){
        return (nPrimeMaterialsSupplied == nTotalPrime);
    }
}
