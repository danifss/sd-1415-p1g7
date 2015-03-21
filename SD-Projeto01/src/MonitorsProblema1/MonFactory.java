package MonitorsProblema1;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonFactory {
    
    /**
     * General repository Object
     * 
     * @serialField info
     */
    private final MonInfo info;

    /**
     * Shop Object
     * 
     * @serialField  shop
     */
    private final MonShop shop;

    /**
     * Factory where Craftmans will work
     * 
     * @param info
     * @param shop 
     */
    public MonFactory(MonInfo info, MonShop shop) {
        this.info = info;
        this.shop = shop;
    }

    /**
     * Check For Materials
     * @return true or false
     */
    
    public synchronized boolean checkForMaterials(){
        return info.getnPrimeMaterialsInFactory() > 0;
    }
    
    /**
     * Collecting prime materials
     * @param craftmanId
     * @return true or false
     */
    public synchronized boolean collectMaterials(int craftmanId) {
        int numPrime = 1;
        info.setnPrimeMaterialsByCraftman(craftmanId, numPrime);
        info.removePrimeMaterials(numPrime);
        return false;
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
