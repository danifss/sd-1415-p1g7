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
     * @serialField repositorio
     */
    private final MonInfo repositorio;

    /**
     * Shop Object
     * 
     * @serialField  shop
     */
    private final MonShop shop;

    /**
     * Factory where Craftmans will work
     * 
     * @param repositorio
     * @param shop 
     */
    public MonFactory(MonInfo repositorio, MonShop shop) {
        this.repositorio = repositorio;
        this.shop = shop;
    }

    /**
     * Check For Materials
     * @return true or false
     */
    
    public synchronized boolean checkForMaterials(){
        return false;
    }
    
    /**
     * Collecting prime materials
     * @return true or false
     */
    public synchronized boolean collectMaterials() {
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
