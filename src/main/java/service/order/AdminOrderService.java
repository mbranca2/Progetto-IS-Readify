package service.order;

import model.bean.Ordine;
import model.bean.StatoOrdine;
import service.order.dto.AdminOrderDetail;

import java.util.List;

public interface AdminOrderService {

    List<Ordine> listAll();

    AdminOrderDetail getOrderDetail(int idOrdine) throws AdminOrderServiceException;

    boolean updateStatus(int idOrdine, StatoOrdine nuovoStato) throws AdminOrderServiceException;
}
