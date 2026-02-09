package business.service.order;

import business.model.Ordine;
import business.model.StatoOrdine;
import business.service.order.dto.AdminOrderDetail;

import java.util.List;

public interface AdminOrderService {

    List<Ordine> listAll();

    AdminOrderDetail getOrderDetail(int idOrdine) throws AdminOrderServiceException;

    boolean updateStatus(int idOrdine, StatoOrdine nuovoStato) throws AdminOrderServiceException;
}
