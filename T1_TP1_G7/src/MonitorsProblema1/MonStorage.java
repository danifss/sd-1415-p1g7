package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonStorage implements MonStorageInterface {
    
    /**
     * Present number of Prime Materials in Storage
     * @serialField nPrimeMaterialsInStorage
     */
    private int nPrimeMaterialsInStorage;
    
    /**
     * Maximum number of Prime Materials available to be delivered to the factory
     * @serialField nMaxPrimeMaterialsToDeliver
     */
    private final int nMaxPrimeMaterialsToDeliver;
    
    /**
     * Number of Prime Materials delivered to the factory
     * @serialField nPrimeMaterialsDelivered
     */
    private int nPrimeMaterialsDelivered;
    
    /**
     * Number of prime materials that the owner can carry
     * @serialField nPrimeOwnerCarry
     */
    private final int nPrimeOwnerCarry;

    /**
     * Create monitor of the storage
     * @param nInitialPrimeMaterialsInStorage Number of prime materials at the beginning
     * @param nPrimeOwnerCarry Number of prime materials that the owner can carry
     */
    public MonStorage(int nInitialPrimeMaterialsInStorage, int nPrimeOwnerCarry) {
        nPrimeMaterialsInStorage = nInitialPrimeMaterialsInStorage;
        nMaxPrimeMaterialsToDeliver = nInitialPrimeMaterialsInStorage;
        this.nPrimeOwnerCarry = nPrimeOwnerCarry;
        nPrimeMaterialsDelivered = 0;
    }

    /**
     * Get the number of prime materials already delivered
     * @return number of prime materials delivered
     */
    @Override
    public int getnPrimeMaterialsDelivered() {
        return nPrimeMaterialsDelivered;
    }
    
    /**
     * See if the storage has prime materials
     * @return true if it has
     */
    @Override
    public boolean isPrimeMaterialsAvailabe() {
        return nPrimeMaterialsInStorage > 0;
    }

    /**
     * Owner visit suppliers and get some prime materials to be delivered in the factory
     * @return Number of prime materials collected
     */
    @Override
    public synchronized int visitSuppliers() {
        int primeMaterials;
        if(nPrimeMaterialsInStorage >= nPrimeOwnerCarry){
            primeMaterials = nPrimeOwnerCarry;
        }else{
            primeMaterials = nPrimeMaterialsInStorage;
        }
        nPrimeMaterialsInStorage -= primeMaterials;
        nPrimeMaterialsDelivered += primeMaterials;
        
        return primeMaterials;
    }

    /**
     * Get the amount of prime materials that the owner should collect during the experience
     * @return number of prime materials to deliver
     */
    @Override
    public int getnMaxPrimeMaterialsToDeliver() {
        return nMaxPrimeMaterialsToDeliver;
    }
    
}
