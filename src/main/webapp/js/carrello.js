
let eventiInizializzati = false;

//inizializzo gli eventi del carrello
function inizializzaEventiCarrello() {
    console.log("Inizializzazione eventi carrello...");
    if (eventiInizializzati) return;
    eventiInizializzati = true;

    // Gestisco pulsanti di modifica quantità
    document.querySelectorAll(".quantity-btn").forEach(btn => {
        btn.addEventListener("click", async event => {
            const isPlus = btn.classList.contains("plus");
            const isMinus = btn.classList.contains("minus");
            const productId = btn.dataset.productId;
            const input = document.querySelector(`input.quantity-input[data-product-id="${productId}"]`);

            if (!input) return;

            let quantity = parseInt(input.value);

            if (isPlus) {
                quantity++;
            } else if (isMinus && quantity > 1) {
                quantity--;
            }

            input.value = quantity;

            try {
                const res = await fetch(`${contextPath}/carrello`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "X-Requested-With": "XMLHttpRequest"
                    },
                    body: `azione=aggiorna&idLibro=${productId}&quantita=${quantity}`
                });

                const data = await res.json();

                if (data.success) {
                    const item = data.articoli.find(i => i.idLibro == productId);
                    if (item) {
                        const prezzoTotale = document.querySelector(`#totale-${productId}`);
                        if (prezzoTotale) {
                            prezzoTotale.textContent = `€${item.totale.toFixed(2)}`;
                        }
                    }

                    document.getElementById("cart-total").textContent = `€${data.totale.toFixed(2)}`;
                }
            } catch (err) {
                console.error("Errore nell'aggiornamento:", err);
            }
        });
    });
    
    // Gestisco l'input
    document.querySelectorAll('.quantity-input').forEach(input => {
        input.addEventListener('change', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const productId = this.dataset.productId;
            let newQuantity = parseInt(this.value) || 1;
            
            if (newQuantity < 1) {
                newQuantity = 1;
                this.value = 1;
            }
            
            aggiornaQuantitaProdotto(productId, newQuantity);
        });
    });

    // Gestisco rimozione di un libro
    document.querySelectorAll('.remove-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const productId = this.dataset.productId;
            rimuoviProdotto(productId);
        });
    });
}

// Inizializzo gli eventi quando il DOM è caricato
document.addEventListener('DOMContentLoaded', inizializzaEventiCarrello);

function aggiornaQuantitaProdotto(idProdotto, nuovaQuantita) {
    //disabilito i controlli durante l'aggiornamento
    const controls = document.querySelectorAll(`.quantity-input[data-product-id="${idProdotto}"],.quantity-btn[data-product-id="${idProdotto}"]`);
    controls.forEach(control => control.disabled = true);

    const formData = new URLSearchParams();
    formData.append('azione', 'aggiorna');
    formData.append('idLibro', idProdotto);
    formData.append('quantita', nuovaQuantita);

    fetch(`${contextPath}/carrello`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest' },
        body: formData.toString()
    })

        .then(response => {
            if (!response.ok) {
                return response.json()
                    .then(err => { throw new Error(err.message || 'Errore durante l\'aggiornamento del carrello'); })
                    .catch(() => { throw new Error('Errore durante l\'aggiornamento del carrello'); });
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            aggiornaUIcarrello(data);
        })
        .catch(error => {
            console.error('Errore:', error);
            mostraMessaggio(error.message || 'Si è verificato un errore durante l\'aggiornamento del carrello', 'danger');
            window.location.reload();
        })
        .finally(() => {
            controls.forEach(control => control.disabled = false);
        });
}

function rimuoviProdotto(idProdotto) {
    const conferma = window.confirm('Sei sicuro di voler rimuovere questo prodotto dal carrello?');
    if (!conferma) return;

    const removeButtons = document.querySelectorAll(`.remove-btn[data-product-id="${idProdotto}"]`);
    removeButtons.forEach(btn => btn.disabled = true);

    fetch(`${contextPath}/carrello`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: `azione=rimuovi&idLibro=${idProdotto}`
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Errore durante la rimozione del prodotto');
                });
            }
            return response.json();
        })
        .then(data => {
            const item = document.querySelector(`.cart-item[data-product-id="${idProdotto}"]`);
            if (item) {
                item.style.transition = 'opacity 0.3s ease';
                item.style.opacity = '0';
                setTimeout(() => {
                    item.remove();
                    aggiornaUIcarrello(data);
                    mostraMessaggio('Prodotto rimosso dal carrello', 'success');
                }, 300);
            } else {
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            mostraMessaggio(error.message || 'Si è verificato un errore durante la rimozione del prodotto', 'danger');
        })
        .finally(() => {
            removeButtons.forEach(btn => btn.disabled = false);
        });
}

function aggiornaUIcarrello(data) {
    const cartBadge = document.querySelector('.cart-badge');
    if (cartBadge) {
        cartBadge.textContent = data.totaleArticoli || '0';
    }
    
    // Aggiorno il totale nella pagina del carrello
    const cartTotalSpan = document.querySelector('#cart-total');
    if (cartTotalSpan) {
        cartTotalSpan.textContent = (data.totalePrezzo || 0).toFixed(2);
    }
    
    // Aggiorno i totali parziali per ogni riga del carrello
    document.querySelectorAll('.cart-item').forEach(row => {
        const productId = row.dataset.productId;
        const item = data.articoli.find(item => item.idLibro == productId);
        if (item) {
            document.querySelector(`#totale-${productId}`).textContent = item.totale.toFixed(2);
        }
        document.getElementById("totale-articoli").textContent = data.totaleArticoli;
        document.getElementById("totale-prezzo").textContent = data.totale.toFixed(2);
    });
    
    //se il carrello è vuoto
    const cartContainer = document.querySelector('.cart-container');
    const emptyCartMessage = document.querySelector('.empty-cart-message');
    const cartTable = document.querySelector('.cart-table');

    if (data.totaleArticoli === 0 && cartContainer) {
        if (!emptyCartMessage) {
            const message = document.createElement('div');
            message.className = 'alert alert-info empty-cart-message';
            message.textContent = 'Il tuo carrello è vuoto.';
            if (cartTable) cartTable.style.display = 'none';
            cartContainer.prepend(message);
        }

        const cartSummary = document.querySelector('.cart-summary');
        if (cartSummary) {
            cartSummary.style.display = 'none';
        }

    } else if (emptyCartMessage) {
        emptyCartMessage.remove();
        if (cartTable) cartTable.style.display = 'table';

        const cartSummary = document.querySelector('.cart-summary');
        if (cartSummary) {
            cartSummary.style.display = '';
        }
    }
}

function mostraMessaggio(testo, tipo = 'info') {
    const vecchiMessaggi = document.querySelectorAll('.alert-message');
    vecchiMessaggi.forEach(msg => msg.remove());

    const message = document.createElement('div');
    message.className = `alert-message ${tipo}`;
    message.textContent = testo;

    const container = document.querySelector('.container') || document.body;
    container.prepend(message);

    setTimeout(() => {
        message.remove();
    }, 5000);
}
