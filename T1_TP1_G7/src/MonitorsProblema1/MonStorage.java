package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class MonStorage {
    
    /**
     * Present number of Prime Materials in Storage
     * 
     * @serialField nPrimeMaterialsInStorage
     */
    private int nPrimeMaterialsInStorage = 0;
    
    /**
     * Maximum number of Prime Materials available to be delivered to the factory
     * 
     * @serialField nMaxPrimeMaterialsToDeliver
     */
    private final int nMaxPrimeMaterialsToDeliver;
    
    /**
     * Number of Prime Materials delivered to the factory
     * 
     * @serialField nPrimeMaterialsDelivered
     */
    private int nPrimeMaterialsDelivered = 0;

    public MonStorage(int nInitialPrimeMaterialsInStorage) {
        nPrimeMaterialsInStorage = nInitialPrimeMaterialsInStorage;
        nMaxPrimeMaterialsToDeliver = nInitialPrimeMaterialsInStorage;
    }

    public int getnPrimeMaterialsDelivered() {
        return nPrimeMaterialsDelivered;
    }

    public synchronized void setnPrimeMaterialsDelivered(int nPrimeMaterialsDelivered) {
        this.nPrimeMaterialsDelivered = nPrimeMaterialsDelivered;
    }

    public boolean isPrimeMaterialsAvailabe() {
        return nPrimeMaterialsDelivered < nMaxPrimeMaterialsToDeliver;
    }

    public synchronized int getBunchOfPrimeMaterials() {
        int primeMaterialsToSell = (int) nMaxPrimeMaterialsToDeliver/6;
        if(nPrimeMaterialsInStorage >= primeMaterialsToSell) { // ter mat. prima suficiente para uma entrega
            nPrimeMaterialsInStorage -= primeMaterialsToSell; // decrementa materias primas vendidas
            return primeMaterialsToSell; // returna materias primas vendidas
        } else if(nPrimeMaterialsInStorage < primeMaterialsToSell){
            primeMaterialsToSell = nPrimeMaterialsInStorage;
            nPrimeMaterialsInStorage = 0; // reset materias primas no armazem
            return primeMaterialsToSell; // retorna as ultimas materias primas
        }
        return 0; // sem materias primas para serem vendidas
    }

    public int getnMaxPrimeMaterialsToDeliver() {
        return nMaxPrimeMaterialsToDeliver;
    }
    
}
