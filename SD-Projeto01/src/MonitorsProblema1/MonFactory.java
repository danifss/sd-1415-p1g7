package MonitorsProblema1;


import genclass.*;
/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonFactory {
    /**
     * Constants that explains the state of the threads craftman and owner
     */
    
    private final static int WAITINGTASK = 0,           // Owner is waiting for the next task
                             COLLECTINGPRODS = 1,       // Owner is collecting a batch of products
                             BUYINGMATERIALS = 2,       // Owner is buying prime materials
                             DELIVERINGMATERIALS = 3,   // Owner is delivering prime materials
                             FETCHINGMATERIALS = 0,     // Craftman is fetching prime materials
                             CONTACTINGOWNER = 1;       // Craftman is contacting the owner
    
    /**
     * Number of craftmans in the factory 
     * 
     * @serialField nCraftman
     */
    private int nCraftman = 0;

	public MonFactory(MonInfo sharedInfo, MonShop shop) {
		
	}
	
	
}
