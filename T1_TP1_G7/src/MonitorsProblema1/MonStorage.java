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

    public MonStorage(int nInitialPrimeMaterialsInStorage) {
        if(nInitialPrimeMaterialsInStorage > 0)
            this.nInitialPrimeMaterialsInStorage = nInitialPrimeMaterialsInStorage;
    }

    
}
