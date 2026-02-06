DROP
DATABASE IF EXISTS readify;

CREATE
DATABASE readify;
USE
readify;

CREATE TABLE Utente
(
    id_utente        INT AUTO_INCREMENT PRIMARY KEY,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password_cifrata VARCHAR(255) NOT NULL,
    nome             VARCHAR(50)  NOT NULL,
    cognome          VARCHAR(50)  NOT NULL,
    data_nascita     DATE         NOT NULL,
    ruolo            VARCHAR(20)  NOT NULL
);

CREATE TABLE IndirizzoSpedizione
(
    id_indirizzo         INT AUTO_INCREMENT PRIMARY KEY,
    id_utente            INT          NOT NULL,
    nome_destinatario    VARCHAR(50)  NOT NULL,
    cognome_destinatario VARCHAR(50)  NOT NULL,
    telefono             VARCHAR(20)  NOT NULL,
    via                  VARCHAR(100) NOT NULL,
    civico               VARCHAR(10)  NOT NULL,
    cap                  VARCHAR(10)  NOT NULL,
    citta                VARCHAR(50)  NOT NULL,
    provincia            VARCHAR(50)  NOT NULL,
    nazione              VARCHAR(50)  NOT NULL,
    FOREIGN KEY (id_utente) REFERENCES Utente (id_utente)
);

CREATE TABLE Libro
(
    id_libro      INT AUTO_INCREMENT PRIMARY KEY,
    titolo        VARCHAR(100)   NOT NULL,
    autore        VARCHAR(100)   NOT NULL,
    casa_editrice VARCHAR(100)   NOT NULL,
    anno_pubblicazione YEAR NOT NULL,
    lingua        VARCHAR(50)    NOT NULL,
    isbn          VARCHAR(20)    NOT NULL UNIQUE,
    descrizione   TEXT           NOT NULL,
    categoria     VARCHAR(50)    NOT NULL,
    immagine      VARCHAR(255)   NOT NULL,
    prezzo        DECIMAL(10, 2) NOT NULL,
    disponibilita INT            NOT NULL
);

CREATE TABLE Carrello
(
    id_carrello INT AUTO_INCREMENT PRIMARY KEY,
    id_utente   INT NOT NULL,
    FOREIGN KEY (id_utente) REFERENCES Utente (id_utente)
);

CREATE TABLE ElementoCarrello
(
    id_elemento INT AUTO_INCREMENT PRIMARY KEY,
    id_carrello INT NOT NULL,
    id_libro    INT NOT NULL,
    quantita    INT NOT NULL,
    FOREIGN KEY (id_carrello) REFERENCES Carrello (id_carrello),
    FOREIGN KEY (id_libro) REFERENCES Libro (id_libro)
);

CREATE TABLE Ordine
(
    id_ordine    INT AUTO_INCREMENT PRIMARY KEY,
    id_utente    INT            NOT NULL,
    id_indirizzo INT            NOT NULL,
    data_ordine  DATETIME       NOT NULL,
    totale       DECIMAL(10, 2) NOT NULL,
    stato        VARCHAR(50)    NOT NULL,
    FOREIGN KEY (id_utente) REFERENCES Utente (id_utente),
    FOREIGN KEY (id_indirizzo) REFERENCES IndirizzoSpedizione (id_indirizzo)
);

CREATE TABLE DettaglioOrdine
(
    id_dettaglio    INT AUTO_INCREMENT PRIMARY KEY,
    id_ordine       INT            NOT NULL,
    id_libro        INT            NOT NULL,
    titolo_libro    VARCHAR(100)   NOT NULL,
    autore_libro    VARCHAR(100)   NOT NULL,
    prezzo_unitario DECIMAL(10, 2) NOT NULL,
    quantita        INT            NOT NULL,
    FOREIGN KEY (id_ordine) REFERENCES Ordine (id_ordine),
    FOREIGN KEY (id_libro) REFERENCES Libro (id_libro)
);

INSERT INTO Utente (email, password_cifrata, nome, cognome, data_nascita, ruolo)
VALUES ('admin@readify.it', '$2a$10$C3Y9aWcYb9dV8mXh7G6S5e4l2r2TjvP6wq4V0m0uF7x3n8fK9aV1O', 'Admin', 'Readify',
        '1990-01-01', 'ADMIN');

INSERT INTO Libro (titolo, autore, casa_editrice, anno_pubblicazione, lingua, isbn, descrizione, categoria, immagine,
                   prezzo, disponibilita)
VALUES ('Il Nome della Rosa', 'Umberto Eco', 'Bompiani', 1980, 'Italiano', '9788845247752',
        'Un giallo storico ambientato in un monastero medievale.', 'Romanzo', 'nome_della_rosa.jpg', 15.99, 10),
       ('1984', 'George Orwell', 'Secker & Warburg', 1949, 'Inglese', '9780451524935',
        'Un romanzo distopico che descrive una società totalitaria.', 'Distopia', '1984.jpg', 12.50, 8),
       ('Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 'Inglese', '9780132350884',
        'Una guida alla scrittura di codice pulito e manutenibile.', 'Informatica', 'clean_code.jpg', 32.00, 5);

