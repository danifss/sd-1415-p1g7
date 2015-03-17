package MonitorsProblema1;

import genclass.*;

/**
 *
 * @author Daniel
 */
public class MonShop {

	/**
	 * Number of Craftsman
	 *
	 * @serialField nCraftsman
	 */
	private int nCraftsman;
	
	/**
	 * Number of Customers
	 *
	 * @serialField nCustomer
	 */
	private int nCustomer;
	
	/**
	 * Number of Shops
	 *
	 * @serialField nShops
	 */
	private int nShops;

	/**
	 * Number of Owners
	 *
	 * @serialField nOwners
	 */
	private int nOwners;

	/**
	 * Number of iteration of life cycle of the customers
	 *
	 * @serialField nIter
	 */
	private int nIter = 0;
	
	/**
	 * FIFO with Customers on the Shop
	 *
	 * @serialField sitCustomer
	 */
	private MemFIFO sitCustomer;

	/**
	 * Create Monitor of the Shop
	 *
	 * @param nCraftsman
	 * @param nCustomer
	 * @param nShops
	 * @param nOwners
	 * @param fName
	 * @param nIter
	 */
	public MonShop(MonInfo sharedInfo, int nCraftsman, int nCustomer, int nShops, int nOwners, String fName, int nIter) {
		if (nCraftsman > 0)
			this.nCraftsman = nCraftsman;
		if (nCustomer > 0) 
			this.nCustomer = nCustomer;
		if (nShops > 0)
			this.nShops = nShops;
		if (nOwners > 0)
			this.nOwners = nOwners;

		sitCustomer = new MemFIFO(nCustomer); // create FIFO for wainting Customers
		
	}

	/**
	 * Customer go shopping, first check if the shop door is open
	 * then enter shop and appraising offer on display and if he wants 
	 * that he buy goods, and finally exit shop
	 * 
	 * @return true if the customer could enter the shop
	 */
	public synchronized void goShopping(int customerId){
		
	}
}
