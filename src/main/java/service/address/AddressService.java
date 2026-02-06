package service.address;

import model.bean.Indirizzo;

import java.util.List;

public interface AddressService {

    List<Indirizzo> listByUser(int idUtente);

    Indirizzo getById(int idIndirizzo);

    boolean isOwnedByUser(int idIndirizzo, int idUtente);
}
