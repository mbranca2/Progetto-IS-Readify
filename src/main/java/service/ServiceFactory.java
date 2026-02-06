package service;

import model.dao.*;
import service.account.AccountService;
import service.account.impl.AccountServiceImpl;
import service.address.AddressService;
import service.address.impl.AddressServiceImpl;
import service.cart.CartFacade;
import service.cart.impl.CartFacadeImpl;
import service.catalog.AdminCatalogService;
import service.catalog.CatalogService;
import service.catalog.CategoryService;
import service.catalog.impl.AdminCatalogServiceImpl;
import service.catalog.impl.CatalogServiceImpl;
import service.catalog.impl.CategoryServiceImpl;
import service.catalog.observer.CartConsistencyObserver;
import service.order.OrderService;
import service.order.impl.OrderServiceImpl;
import service.review.ReviewService;
import service.review.impl.ReviewServiceImpl;

public class ServiceFactory {

    private ServiceFactory() {
    }

    public static AccountService accountService() {
        return new AccountServiceImpl(new UtenteDAO(), new IndirizzoDAO());
    }

    public static AddressService addressService() {
        return new AddressServiceImpl(new IndirizzoDAO());
    }

    public static CartFacade cartFacade() {
        return new CartFacadeImpl(new CarrelloDAO(), new LibroDAO());
    }

    public static OrderService orderService() {
        return new OrderServiceImpl(new OrdineDAO(), new CarrelloDAO());
    }

    public static AdminCatalogService adminCatalogService() {
        AdminCatalogServiceImpl svc = new AdminCatalogServiceImpl(new LibroDAO());
        svc.registerObserver(new CartConsistencyObserver());
        return svc;
    }

    public static CatalogService catalogService() {
        return new CatalogServiceImpl(new LibroDAO());
    }

    public static CategoryService categoryService() {
        return new CategoryServiceImpl(new CategoriaDAO());
    }

    public static ReviewService reviewService() {
        return new ReviewServiceImpl(new RecensioneDAO(), new OrdineDAO());
    }
}
