package service.order;

import model.bean.Carrello;
import model.bean.Ordine;

import java.util.List;

public interface OrderService {

    Ordine placeOrder(int idUtente, int idIndirizzo, Carrello carrello) throws OrderServiceException;

    List<Ordine> listByUser(int idUtente);

    boolean cancelOrder(int idOrdine, int idUtente) throws OrderServiceException;
}
