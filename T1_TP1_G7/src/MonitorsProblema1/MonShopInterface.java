/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonitorsProblema1;

/**
 *
 * @author Daniel
 */
public interface MonShopInterface {

    /**
     * Update the number of products that the shop is selling
     * @param goods
     */
    void addnGoodsInDisplay(int goods);

    /**
     * Address a Customer on the queue
     */
    int addressACustomer();

    /**
     * Owner sees the situation of the shop and decide what to do
     * @return action to do
     */
    int appraiseSit();

    /**
     * Owner can go to factory to collect products
     */
    void batchReadyForTransfer();

    /**
     * Owner closes the door
     */
    void closeTheDoor();

    /**
     * See if the Queue has customers
     * @return true if the queue has customers
     */
    boolean customerInTheQueue();

    /**
     * See if there is customers inside the shop
     * @return true if it is customers inside
     */
    boolean customersInTheShop();

    /**
     * See if the owner and the customer can stop.
     * Checks if all products have been transferred to Shop, and it have
     * all been sold.
     * @return true if they can
     */
    boolean endOper();

    /**
     * A customer enters the shop
     */
    void enterShop();

    /**
     * The Customer leaves the Shop
     */
    void exitShop();

    /**
     * The owner goes to the Factory to collect products
     */
    void goToWorkshop();

    /**
     * Customer goes to the queue, and waits till the owner call him.
     * When the owner call and he is in the front of the queue
     * he buys the goods
     * @param customerId
     * @param nGoods
     */
    void iWantThis(int customerId, int nGoods);

    //Funcions of customer
    /**
     * See if the door is open
     * @return True if is open
     */
    boolean isDoorOpen();

    /**
     * See if the shop is on state STILL_OPEN
     * @return true if the shop is STILL_OPEN
     */
    boolean isShopStillOpen();

    /**
     * Check if the factory needs prime materials
     * @return true if is needed
     */
    boolean isSupplyMaterialsToFactory();

    /**
     * Check if the owner can collect products
     * @return true if he needs to go to the factory
     */
    boolean isTranfsProductsToShop();

    /**
     * Owner opens the door
     */
    void openTheDoor();

    /**
     * A random number is generated, then if it is less than 0.5 and
     * exists goods to buy, that number*100 is divided by the number
     * of goods for sell and returns his remainder.
     * @return number of goods to buy
     */
    int perusingAround();

    /**
     * Prime materials is needed in the Factory
     */
    void primeMaterialsNeeded();

    /**
     * Remove customer from queue
     * @param customerId
     */
    void removeSitCustomer(int customerId);

    /**
     * Owner goes to factory to restock prime materials
     */
    void replenishStock();
    
    /**
     * Say goodbye to attending Customer
     */
    void sayGoodByeToCustomer();

    /**
     * Service customer
     * @param customerId
     * @return number of goods that the customer is buying
     */
    int serviceCustomer(int customerId);

    /**
     * Set present Shop state
     * @param state
     */
    void setShopState(int state);
    
}
