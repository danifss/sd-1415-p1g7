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
public interface MonInfoInterface {

    // Funções destinadas a alterar as variáveis pertencentes aos Craftman
    /**
     * Set state of the Craftman[i]
     *
     * @param craftmanId
     * @param state
     */
    void setCraftmanState(int craftmanId, int state);

    // Funções destinadas a alterar as variáveis pertencentes aos Customer
    /**
     * Set Customer[i] State
     * @param customerId Customer id
     * @param state State of the customer
     */
    void setCustomerState(int customerId, int state);

    // Função destinada a alterar a variável pertencente ao Owner
    /**
     * Set Owner State
     * @param state State of the Owner
     */
    void setOwnerState(int state);

    // Funções destinadas a alterar as variáveis pertencentes ao Shop
    /**
     * Set Shop State
     * @param state State of the shop
     */
    void setShopState(int state);

    /**
     * Set if the craftsman requested the supply of prime materials to the Factory
     * @param supplyMaterialsToFactory Boolean indicating if the Craftman requested
     */
    void setSupplyMaterialsToFactory(boolean supplyMaterialsToFactory);

    /**
     * Set if the craftsman requested the transfer of finished products to the Shop
     * @param tranfsProductsToShop Boolean indicating if the Craftman requested
     */
    void setTranfsProductsToShop(boolean tranfsProductsToShop);

    /**
     * Set number of customers inside
     * @param nCustomersInsideShop Number of customers inside
     */
    void setnCustomersInsideShop(int nCustomersInsideShop);

    /**
     * Change the number of finished products presently in the Factory
     * @param nFinishedProductsInFactory
     */
    void setnFinishedProductsInFactory(int nFinishedProductsInFactory);

    /**
     * Set number of goods (accumulation) bought by the customer
     * @param customerId Customer id
     * @param nGoodsByCustomer Number of goods bought by the customer
     */
    void setnGoodsByCustomer(int customerId, int nGoodsByCustomer);

    /**
     * Change the number of products (accumulation) manufactured by the craftsman[i]
     * @param craftmanId
     * @param nGoodsCraftedByCraftman
     */
    void setnGoodsCraftedByCraftman(int craftmanId, int nGoodsCraftedByCraftman);

    /**
     * Net number of goods in display
     * @param nGoodsInDisplay Number of goods in display
     */
    void setnGoodsInDisplay(int nGoodsInDisplay);

    // Funções destinadas a alterar as variáveis pertencentes à Factory
    /**
     * Change the amount of prime materials presently in the Factory
     * @param nPrimeMaterialsInFactory
     */
    void setnPrimeMaterialsInFactory(int nPrimeMaterialsInFactory);

    /**
     * Change the total amount of prime materials that have already been supplied (accumulation)
     * @param nPrimeMaterialsSupplied
     */
    void setnPrimeMaterialsSupplied(int nPrimeMaterialsSupplied);

    /**
     * Change the total number of products that have already been manufactured (accumulation)
     * @param nProductsManufactured
     */
    void setnProductsManufactured(int nProductsManufactured);

    /**
     * Change the number of times that a supply of prime materials was delivered to the workshop
     * @param nSuppliedTimes
     */
    void setnSuppliedTimes(int nSuppliedTimes);
    
}
