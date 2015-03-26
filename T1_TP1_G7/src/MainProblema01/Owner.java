package MainProblema01;

import MonitorsProblema1.*;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Owner extends Thread {
	
	/**
	 * General Repository
	 * 
	 * @serialField sharedInfo
	 */
	private MonInfo sharedInfo;

	/**
	 * Factory
	 *
	 * @serialField factory
	 */
	private MonFactory factory;

	/**
	 * Shop
	 *
	 * @serialField shop
	 */
	private MonShop shop;
    
    /**
     * Storage
     * 
     * @serialField storage
     */
    private MonStorage storage;
    
    /**
     * Preent State of the Owner
     * 
     * @serialField ownerState
     */
    private int ownerState;

	/**
	 * Create owner thread
	 *
	 * @param ownerId Owner identity
	 * @param factory Factory
	 * @param shop Shop
	 */
	public Owner(MonInfo sharedInfo, MonFactory factory, MonShop shop, MonStorage storage) {
		this.sharedInfo = sharedInfo;
		this.factory = factory;
		this.shop = shop;
        this.storage = storage;
        
        this.ownerState = MonInfo.OPENING_THE_SHOP;
	}

	/**
	 * Life cycle of the owner
	 */
	@Override
	public void run() {
        System.out.println("Iniciado o Owner.");
		boolean out;
		int cid, sit = 0;
		do {
			prepareToWork(); // fica a dormir
			out = false;
			while (!out) {
				sit = appraiseSit();
				switch (sit) {
					case 1: // atender cliente
						cid = this.shop.addressACustomer();
						serviceCustomer();
						sayGoodbyeToCustomer(cid);
						break;
					case 2: // buscar materias primas do armazem para a oficina
					case 3: // buscar produtos terminados a oficina
						closeTheDoor();
						out = !shop.customersInTheShop(); // verifica primeiro se ha  clientes para atender
						break;
					case 4: // nada para fazer
						out = true;
						break;
				}
			}
			prepareToLeave(); // sair da loja?
			if (sit == 3) {
				goToWorkshop(); // vai buscar produtos a oficina e levar para a loja
			} else if (sit == 2) {
				int q = visitSuppliers(); // comprar materias primas
				replenishStock(q); // colocar as materias primas compradas na oficina
			}
			returnToShop();
		} while(!endOper());
        System.out.println("Terminado o Owner.");
	}

	private void prepareToWork() {
		this.sharedInfo.setOwnerState(MonInfo.WAITING_FOR_NEXT_TASK);
        setOwnerState(MonInfo.WAITING_FOR_NEXT_TASK);
		
		try {
			sleep((long) (1 + 20 * Math.random()));
		} catch (InterruptedException e) {}
	}

	private int appraiseSit() {
		// verifica se ha clientes para serem atendidos
		if(this.shop.customersInTheShop())
			return 1;
		// verifica se foi notificada por um artesao pedir materias primas
        if(this.shop.isSupplyMaterialsToFactory())
			return 2;
		// verifica se foi notificada por um artesao que ha produtos para ir para a loja
		if(this.shop.isTranfsProductsToShop())
			return 3;
		return 4; // nada para fazer
	}

	private void serviceCustomer() {
		this.sharedInfo.setOwnerState(MonInfo.ATTENDING_A_CUSTOMER);
        setOwnerState(MonInfo.ATTENDING_A_CUSTOMER);
		
		try {
			sleep((long) (1 + 10 * Math.random()));
		} catch (InterruptedException e) {}
	}

	private void sayGoodbyeToCustomer(int cid) {
		this.shop.removeSitCustomer(cid);
		prepareToWork();
	}

	private void closeTheDoor() {
		this.sharedInfo.setShopState(MonInfo.CLOSED);
        this.shop.setShopState(MonInfo.CLOSED);
	}

	private void prepareToLeave() { // nao sei se e assim
		this.sharedInfo.setShopState(MonInfo.STILL_OPEN);
		this.sharedInfo.setOwnerState(MonInfo.CLOSING_THE_SHOP);
        
        this.shop.setShopState(MonInfo.STILL_OPEN);
        setOwnerState(MonInfo.CLOSING_THE_SHOP);
	}

	private void goToWorkshop() {
        this.sharedInfo.setOwnerState(MonInfo.COLLECTING_A_BATCH_OF_PRODUCTS);
        setOwnerState(MonInfo.COLLECTING_A_BATCH_OF_PRODUCTS);

        shop.goToWorkshop();                        // Atualiza a flag
        int products = this.factory.goToWorkshop(); // get dos produtos feitos
        //this.factory.setnFinishedProductsInFactory(0); // por a 0 os produtos feitos na oficina

        this.shop.setnGoodsInDisplay(products); // set dos produtos anteriores para os disponiveis na loja

        this.sharedInfo.setTranfsProductsToShop(false);
	}

	private int visitSuppliers() {
        // visita o fornecedor/armazem para comprar materia prima
        this.sharedInfo.setOwnerState(MonInfo.BUYING_PRIME_MATERIALS);
        setOwnerState(MonInfo.BUYING_PRIME_MATERIALS);
        
        try {
            sleep((long) (1 + 10 * Math.random()));
        } catch (InterruptedException e) {}
        
        if(storage.isPrimeMaterialsAvailabe())
            return storage.getBunchOfPrimeMaterials();
        return 0; // sem materias primas
	}

    /**
     * Owner delivers prime materials to the Factory
     * @param q number of prime materials delivered 
     */
	private void replenishStock(int q) {
        shop.replenishStock();
        factory.replenishStock(q);	
	}

	/**
	 * Return to shop
	 */
	public void returnToShop() {
		try {
			sleep((long) (1 + 10 * Math.random()));
		} catch (InterruptedException e) {}
		
		this.sharedInfo.setOwnerState(MonInfo.OPENING_THE_SHOP);
		this.sharedInfo.setShopState(MonInfo.OPEN);
	}
    
    private void setOwnerState(int ownerState) {
        this.ownerState = ownerState;
    }
    
    private boolean endOper() {
		// valida se o Owner deve terminar ou nao
		//if()
		return true;
	}
}
