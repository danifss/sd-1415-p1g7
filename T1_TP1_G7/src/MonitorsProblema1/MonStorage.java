package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonStorage {
    
    /**
     * Number of initial Prime Materials in Storage
     * 
     * @serialField nInitialPrimeMaterialsInStorage
     */
    private int nInitialPrimeMaterialsInStorage = 0;
    
    /**
     * Maximum number of products to be delivered to the factory
     * 
     * @serialField nMaxPrimeMaterialsToDeliver
     */
    private final int nMaxPrimeMaterialsToDeliver;
    
    /**
     * Number of Prime Materials delivered to the factory
     * 
     * @serialField nPrimeMaterialsDelivered
     */
    private int nPrimeMaterialsDelivered = 0;

    public MonStorage(int nInitialPrimeMaterialsInStorage, int nMaxPrimeMaterialsToDeliver) {
        if(nInitialPrimeMaterialsInStorage > 0)
            this.nInitialPrimeMaterialsInStorage = nInitialPrimeMaterialsInStorage;

        this.nMaxPrimeMaterialsToDeliver = nMaxPrimeMaterialsToDeliver;
    }

    
}
