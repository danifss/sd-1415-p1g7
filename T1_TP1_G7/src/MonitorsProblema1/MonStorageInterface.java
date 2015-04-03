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
public interface MonStorageInterface {

    /**
     * Get the amount of prime materials that the owner should collect during the experience
     * @return number of prime materials to deliver
     */
    int getnMaxPrimeMaterialsToDeliver();

    /**
     * Get the number of prime materials already delivered
     * @return number of prime materials delivered
     */
    int getnPrimeMaterialsDelivered();

    /**
     * See if the storage has prime materials
     * @return true if it has
     */
    boolean isPrimeMaterialsAvailabe();

    /**
     * Owner visit suppliers and get some prime materials to be delivered in the factory
     * @return Number of prime materials collected
     */
    int visitSuppliers();
    
}
