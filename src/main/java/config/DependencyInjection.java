package config;

import service.*;
import service.impl.*;
import repository.*;
import repository.impl.*;

public class DependencyInjection {
    private static LibroService libroService;
    private static LibroRepository libroRepository;
    private static UtenteService utenteService;
    private static UtenteRepository utenteRepository;
    private static OrdineService ordineService;
    private static OrdineRepository ordineRepository;
    private static DettaglioOrdineService dettaglioOrdineService;
    private static DettaglioOrdineRepository dettaglioOrdineRepository;
    private static CategoriaService categoriaService;
    private static CategoriaRepository categoriaRepository;
    private static IndirizzoService indirizzoService;
    private static IndirizzoRepository indirizzoRepository;

    public static synchronized OrdineService getOrdineService() {
        if (ordineService == null) {
            ordineService = new OrdineServiceImpl();
        }
        return ordineService;
    }

    public static synchronized OrdineRepository getOrdineRepository() {
        if (ordineRepository == null) {
            ordineRepository = new OrdineRepositoryImpl();
        }
        return ordineRepository;
    }

    public static synchronized DettaglioOrdineService getDettaglioOrdineService() {
        if (dettaglioOrdineService == null) {
            dettaglioOrdineService = new DettaglioOrdineServiceImpl();
        }
        return dettaglioOrdineService;
    }

    public static synchronized DettaglioOrdineRepository getDettaglioOrdineRepository() {
        if (dettaglioOrdineRepository == null) {
            dettaglioOrdineRepository = new DettaglioOrdineRepositoryImpl();
        }
        return dettaglioOrdineRepository;
    }

    public static synchronized LibroService getLibroService() {
        if (libroService == null) {
            libroService = new LibroServiceImpl();
        }
        return libroService;
    }

    public static synchronized LibroRepository getLibroRepository() {
        if (libroRepository == null) {
            libroRepository = new LibroRepositoryImpl();
        }
        return libroRepository;
    }

    public static synchronized UtenteService getUtenteService() {
        if (utenteService == null) {
            utenteService = new UtenteServiceImpl();
        }
        return utenteService;
    }

    public static synchronized UtenteRepository getUtenteRepository() {
        if (utenteRepository == null) {
            utenteRepository = new UtenteRepositoryImpl();
        }
        return utenteRepository;
    }

    public static synchronized CategoriaService getCategoriaService() {
        if (categoriaService == null) {
            categoriaService = new CategoriaServiceImpl();
        }
        return categoriaService;
    }

    public static synchronized CategoriaRepository getCategoriaRepository() {
        if (categoriaRepository == null) {
            categoriaRepository = new CategoriaRepositoryImpl();
        }
        return categoriaRepository;
    }

    public static synchronized IndirizzoService getIndirizzoService() {
        if (indirizzoService == null) {
            indirizzoService = new IndirizzoServiceImpl();
        }
        return indirizzoService;
    }

    public static synchronized IndirizzoRepository getIndirizzoRepository() {
        if (indirizzoRepository == null) {
            indirizzoRepository = new IndirizzoRepositoryImpl();
        }
        return indirizzoRepository;
    }
}