package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public interface MonShopInterface {

    /**
     * Update the number of products that the shop is selling.
     * @param goods Number of products to add to the number of products in display
     */
    void addnGoodsInDisplay(int goods);

    /**
     * The Owner address a Customer on the queue.
     * The owner wakes up the first Customer in the queue.
     * @return id of the Customer that the Owner is attending
     */
    int addressACustomer();

    /**
     * Owner sees the situation of the shop and decide what to do.
     * If the shop is open, he waits until a Customer or a Craftman calls him. 
     * If the shop is still open, he wait until all Customers leave the shop, and 
     * then proceeds to the request from the Factory.
     * FACTORY_NEEDS_SOMETHING can also indicates that the Owner can stop, because
     * this action makes the Owner close the Shop.
     * @return action to do
     */
    int appraiseSit();

    /**
     * The Craftman indicates that the Owner can go to factory to collect products.
     * He changes the flag and wakes up the Owner.
     */
    void batchReadyForTransfer();

    /**
     * Owner closes the door.
     * If the shop has Customers inside, the shop change his state to STILL_OPEN,
     * if the shop doesn't have Customers, the shop change his state to CLOSED.
     */
    void closeTheDoor();

    /**
     * See if the Queue has customers.
     * @return true if the queue has customers
     */
    boolean customerInTheQueue();

    /**
     * See if there is customers inside the shop.
     * @return true if the number of Customers inside the shop is greater than 0
     */
    boolean customersInTheShop();

    /**
     * See if the owner and the customer can stop.
     * Checks if all products have been transferred to Shop, and all the products
     * are sold.
     * @return true if they can stop working
     */
    boolean endOper();

    /**
     * The customer enters the shop.
     */
    void enterShop();

    /**
     * The Customer leaves the Shop.
     * If he his the last one leaving the Shop, wakes up the Owner (important to Owner
     * finish working, if he his waiting for all the Customers to buy products,
     * but the last one don't buy anything).
     */
    void exitShop();

    /**
     * The owner goes to the Factory to collect products.
     */
    void goToWorkshop();

    /**
     * The Customer goes to the queue, and waits till the owner call him.
     * When the owner calls a Customer and he is in the front of the queue, he 
     * makes the purchase.
     * @param customerId Id of the customer that wants to buy something
     * @param nGoods Number of goods that the Customer wants to buy
     */
    void iWantThis(int customerId, int nGoods);

    /**
     * The Customer sees if the door is open
     * @return True if the shop is OPEN
     */
    boolean isDoorOpen();

    /**
     * See if the shop is on state STILL_OPEN.
     * @return true if the shop is STILL_OPEN
     */
    boolean isShopStillOpen();

    /**
     * Check if the factory needs prime materials.
     * @return true if the Factory needs prime materials
     */
    boolean isSupplyMaterialsToFactory();

    /**
     * Check if the owner can collect products.
     * @return true if he needs to go to the factory
     */
    boolean isTranfsProductsToShop();

    /**
     * Owner opens the door.
     * The state of the Shop is changed to OPEN.
     */
    void openTheDoor();

    /**
     * The Customer is perusing around.
     * A random number is generated, then if it is less than 0.5 and 
     * exists goods to buy, that number*100 is divided by the number 
     * of goods for sell and returns his remainder.
     * @return number of goods to buy
     */
    int perusingAround();

    /**
     * The Craftman indicates that prime materials is needed in the Factory.
     * He changes the flag and wakes up the Owner.
     */
    void primeMaterialsNeeded();

    /**
     * Remove Customer from queue.
     * @param customerId 
     */
    void removeSitCustomer(int customerId);

    /**
     * Owner goes to Factory to restock prime materials.
     */
    void replenishStock();
    
    /**
     * The Owner says goodbye to the Customer he is attending.
     * He updates the flag that indicates that the purchase was made and wakes up
     * the Customer. Then removes the Customer from the queue.
     */
    void sayGoodByeToCustomer();

    /**
     * The Owner services a customer.
     * The Owner waits until the Customer updates the number of products he wants 
     * to buy.
     * @return number of goods that the customer is buying
     */
    int serviceCustomer();

    /**
     * Set present Shop state.
     * @param state State of the Shop
     */
    void setShopState(int state);
}
