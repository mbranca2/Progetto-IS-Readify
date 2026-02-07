package service.cart;

import model.bean.Carrello;

public interface CartFacade {

    Carrello syncAfterLogin(int idUtente, Carrello carrelloTemporaneo);

    Carrello getCurrentCart(Integer idUtente, Carrello sessionCart);

    boolean addBook(Integer idUtente, Carrello cart, int idLibro, int quantita);

    boolean updateQuantity(Integer idUtente, Carrello cart, int idLibro, int nuovaQuantita);

    boolean removeBook(Integer idUtente, Carrello cart, int idLibro);
}
