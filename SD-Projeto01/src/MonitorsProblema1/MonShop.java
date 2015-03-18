package MonitorsProblema1;

import genclass.*;

/**
 *
 * @author Daniel
 */
public class MonShop {

	/**
	 * General repository
	 * 
	 * @serialField sharedInfo
	 */
	private MonInfo sharedInfo;
	
	/**
	 * FIFO with Customers on the Shop
	 *
	 * @serialField sitCustomer
	 */
	private MemFIFO sitCustomer;

	/**
	 * Create Monitor of the Shop
	 * 
	 * @param sharedInfo
	 */
	public MonShop(MonInfo sharedInfo) {
		this.sharedInfo = sharedInfo;
		sitCustomer = new MemFIFO(this.sharedInfo.getnCraftsman()); // create FIFO for wainting Customers
		
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
