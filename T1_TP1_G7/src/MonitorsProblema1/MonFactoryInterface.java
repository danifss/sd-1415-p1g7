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
public interface MonFactoryInterface {

    /**
     * Indicates that the owner has products to collect
     */
    void batchReadyForTransfer();

    /**
     * Verifies if the Craftman needs to contact the owner
     * @return true if he needs to contact
     */
    boolean checkContactProduct();

    /**
     * Check For Materials
     * @return true if has materials
     */
    boolean checkForMaterials();

    /**
     * Check if the Craftman needs to contact owner to bring prime materials
     * @return true if needs to restock
     */
    boolean checkForRestock();

    /**
     * Collecting prime materials
     * @return number of collected prime materials
     */
    int collectMaterials();

    /**
     * Sees if have more prime materials
     * @return true if no more prime materials
     */
    boolean endOfPrimeMaterials();



    /**
     * The Craftman sees if someone already contacted the owner to restock the prime materials
     * @return true if someone already contacted the owner
     */
    boolean flagPrimeActivated();

    /**
     * Craftman sees how many prime materials needs to produce a new product
     * @return number of prime materials needed per products
     */
    int getnPrimePerProduct();

    /**
     * Craftman store the products produced and increment total produced products
     * @param nProd Number of products the Craftman has
     * @return number of products the Craftman stored
     */
    int goToStore(int nProd);

    /**
     * Owner goes to factory to collect finished products
     * @return number of products collected
     */
    int goToWorkshop();

    /**
     * Turn true the flag that indicates that prime materials is needed
     * @return if it is really needed to call the owner
     */
    boolean primeMaterialsNeeded();

    /**
     * Owner brings prime materials
     * @param nPrimeMaterials Amount of prime materials to restock
     */
    void replenishStock(int nPrimeMaterials);
    
}
