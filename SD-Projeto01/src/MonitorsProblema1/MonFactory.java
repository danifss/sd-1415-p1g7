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
	 * Factory where Craftsmans will work
	 * 
	 * @param sharedInfo
	 * @param shop 
	 */
	public MonFactory(MonInfo repositorio, MonShop shop) {
		this.repositorio = repositorio;
		this.shop = shop;
	}

	/**
	 * Collecting prime materials
	 * @return true or false
	 */
	public synchronized boolean collectMaterials() {
		return false;
	}
	
	
}
