package MainProblema01;

import MonitorsProblema1.*;

/**
 * This class is responsible to host the Owner
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Owner extends Thread implements OwnerInterface {
    
    /**
     * Owner States
     */
    private final static int
            OPENING_THE_SHOP = 0,
            WAITING_FOR_NEXT_TASK = 1,
            ATTENDING_A_CUSTOMER = 2,
            CLOSING_THE_SHOP = 3,
            DELIVERING_PRIME_MATERIALS = 4,
            COLLECTING_A_BATCH_OF_PRODUCTS = 5,
            BUYING_PRIME_MATERIALS = 6;
    
    /**
     * Decision taken in AppraiseSit
     */
    private final static int
            NEED_TO_CLOSE_SHOP = 0,
            ADDRESS_CUSTOMER = 1;
    
    /**
     * General Repository
     * @serialField sharedInfo
     */
    private MonInfo sharedInfo;

    /**
     * Factory
     * @serialField factory
     */
    private MonFactory factory;

    /**
     * Shop
     * @serialField shop
     */
    private MonShop shop;
    
    /**
     * Storage
     * @serialField storage
     */
    private MonStorage storage;
    
    /**
     * Present State of the Owner
     * @serialField ownerState
     */
    private int ownerState;
    
    /**
     * Number of prime materials that the owner has
     * @serialField nPrimeMaterials
     */
    private int nPrimeMaterials;
    
    /**
     * Id of the customer that the owner is attending
     * @serialField attendingCustomerId
     */
    private int attendingCustomerId;

    /**
     * Create owner thread.
     *
     * @param sharedInfo General repository
     * @param factory Factory
     * @param shop Shop
     * @param storage Storage
     */
    public Owner(MonInfo sharedInfo, MonFactory factory, MonShop shop, MonStorage storage) {
        this.sharedInfo = sharedInfo;
        this.factory = factory;
        this.shop = shop;
        this.storage = storage;
        ownerState = OPENING_THE_SHOP;
        attendingCustomerId = -1;
    }

    /**
     * Life cycle of the Owner.
     */
    @Override
    public void run() {
        System.out.println("Iniciado o Owner.");

        shop.openTheDoor(); // Owner needs to open the shop before he starts working
        
        while(!endOper()){
            switch(ownerState){
                case OPENING_THE_SHOP:
                    prepareToWork();
                    break;
                case WAITING_FOR_NEXT_TASK:
                    int decision = appraiseSit();
                    if(decision == NEED_TO_CLOSE_SHOP){
                        closeTheDoor();
                        if(customersInTheShop()){
                            break;
                        }
                        prepareToLeave();
                    } else if(decision == ADDRESS_CUSTOMER){
                        addressACustomer();
                    }
                    break;
                case ATTENDING_A_CUSTOMER:
                    System.out.printf("Owner\t\t- Atender o cliente %d.\n",attendingCustomerId);
                    serviceCustomer();
                    sayGoodByeToCustomer();
                    break;
                case CLOSING_THE_SHOP:
                    if(shop.isSupplyMaterialsToFactory()){
                        visitSuppliers();
                        System.out.printf("Owner\t\t- Vou ao armazem comprar materia prima.\n");
                    }else if(shop.isTranfsProductsToShop()){
                        goToWorkShop();
                        System.out.printf("Owner\t\t- Vou a oficina.\n");
                    }else{
                        System.out.printf("Owner\t\t- Fim.\n");
                        returnToShop();
                    }
                    break;
                case COLLECTING_A_BATCH_OF_PRODUCTS:
                    returnToShop();
                    break;
                case BUYING_PRIME_MATERIALS:
                    if(nPrimeMaterials > 0) {
                        replenishStock();
                        System.out.printf("Owner\t\t- Repor stock da oficina.\n");
                    }else
                        setOwnerState(DELIVERING_PRIME_MATERIALS);
                    break;
                case DELIVERING_PRIME_MATERIALS:
                    returnToShop();
                    break;
            }
        }
        System.out.println("Terminado o Owner.");
    }

    /**
     * Owner prepares to work, changing his state to waiting for the next task.
     */
    private void prepareToWork() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setOwnerState(WAITING_FOR_NEXT_TASK);
    }

    /**
     * Owner waits to see what he needs to do.
     * @return action he will do
     */
    private int appraiseSit(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return shop.appraiseSit();
    }
    
    /**
     * Owner closes the door of the Shop.
     */
    private void closeTheDoor() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        shop.closeTheDoor();
    }
    
    /**
     * Owner sees if there is customers in the shop.
     */
    private boolean customersInTheShop(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        return shop.customersInTheShop();
    }
    
    /**
     * Owner prepares to go to the Factory.
     */
    private void prepareToLeave(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setOwnerState(CLOSING_THE_SHOP);
    }
    
    /**
     * Owner prepares to address a customer.
     */
    private void addressACustomer(){
        try{
            sleep((long) (100));
        }catch(InterruptedException e){}
        setOwnerState(ATTENDING_A_CUSTOMER);
        attendingCustomerId = shop.addressACustomer(); // atende cliente seguinte
    }
    
    /**
     * Owner services a Customer.
     * He will take more time if the Customer is buying more products.
     */
    private void serviceCustomer() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        int n = shop.serviceCustomer();
        int i = 0;
        while(i<n){
            //Debug
            System.out.println("--------Tratando produto: "+ (i+1) + " do customer: "+ attendingCustomerId);
            try {
                sleep((long) (25 + 10 * Math.random()));
            } catch (InterruptedException e) {}
            i++;
        }
    }
    
    /**
     * Owner finish the purchase and say goodbye to the Customer.
     * Then he wait for the next task.
     */
    private void sayGoodByeToCustomer() {
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        shop.sayGoodByeToCustomer();
        if(shop.isShopStillOpen()){
            closeTheDoor();
        }
        setOwnerState(WAITING_FOR_NEXT_TASK);
    }
    
    /**
     * Owner goes to Factory to collect products.
     * Then he adds the products collected to the display.
     */
    private void goToWorkShop(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setOwnerState(COLLECTING_A_BATCH_OF_PRODUCTS);

        shop.goToWorkshop();
        int products = factory.goToWorkshop();

        shop.addnGoodsInDisplay(products);
    }
    
    /**
     * Owner goes to the storage to collect some prime materials.
     */
    private void visitSuppliers(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setOwnerState(BUYING_PRIME_MATERIALS);

        if(storage.isPrimeMaterialsAvailabe())
            nPrimeMaterials = storage.visitSuppliers();
        else{
            nPrimeMaterials = 0;
        }
    }
    
    /**
     * Owner returns to the shop and opens the door.
     */
    private void returnToShop(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}

        setOwnerState(OPENING_THE_SHOP);
        shop.openTheDoor();
    }
    
    /**
     * Owner delivers prime materials to the Factory.
     */
    private void replenishStock(){
        try{
            sleep((long) (20));
        }catch(InterruptedException e){}
        setOwnerState(DELIVERING_PRIME_MATERIALS);
        shop.replenishStock();
        factory.replenishStock(nPrimeMaterials);
        nPrimeMaterials = 0;
    }
    
    /**
     * Verifies if the Owner can stop working.
     * From the shop, he checks if all products have been transferred to Shop, and all the products
     * are sold. He needs to be in the State CLOSING_THE_SHOP and the shop can't have Customers
     * inside.
     * @return true if needs to stop
     */
    private boolean endOper() {
        return shop.endOper() && (ownerState == CLOSING_THE_SHOP) && !shop.customersInTheShop();
    }
    
    /**
     * Change the owner state.
     * @param ownerState state of the owner
     */
    private void setOwnerState(int ownerState) {
        this.ownerState = ownerState;
        sharedInfo.setOwnerState(ownerState);
    }
}
