
document.addEventListener('DOMContentLoaded', function() {
    // Elementi UI
    const booksGrid = document.getElementById('booksGrid');
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const categoryItems = document.querySelectorAll('.categoria-item');
    const priceFilters = document.getElementsByName('price');
    const availabilityFilters = document.getElementsByName('availability');
    
    // Stato filtri
    let activeFilters = {
        searchTerm: '',
        categoryId: 0,
        priceRange: '',
        availability: ''
    };

    // Inizializzo gli event listener
    function initEventListeners() {
        console.log('Inizializzazione filtri...');

        if (searchButton) {
            searchButton.addEventListener('click', applySearch);
            console.log('Pulsante di ricerca inizializzato');
        }

        if (searchInput) {
            searchInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    applySearch();
                }
            });
            console.log('Input di ricerca inizializzato');
        }
        
        // Filtri prezzo
        Array.from(priceFilters).forEach(radio => {
            radio.addEventListener('change', function() {
                console.log('Filtro prezzo cambiato:', this.value);
                activeFilters.priceRange = this.value;
                filterBooks();
            });
        });
        
        // Filtri disponibilità
        Array.from(availabilityFilters).forEach(radio => {
            radio.addEventListener('change', function() {
                console.log('Filtro disponibilità cambiato:', this.value);
                activeFilters.availability = this.value;
                filterBooks();
            });
        });
        
        console.log('Filtri inizializzati');
    }

    function applySearch() {
        console.log('Applica ricerca:', searchInput.value);
        activeFilters.searchTerm = searchInput.value.toLowerCase().trim();
        filterBooks();
    }
    
    // Funzione per filtrare i libri in base ai filtri attivi
    function filterBooks() {
        console.log('Filtro libri attivato con filtri:', activeFilters);
        
        if (!booksGrid) {
            console.error('Elemento booksGrid non trovato!');
            return;
        }
        
        const bookCards = booksGrid.querySelectorAll('.libro-card');
        console.log('Trovate', bookCards.length, 'carte libro');
        
        if (bookCards.length === 0) {
            console.error('Nessuna carta libro trovata!');
            return;
        }
        
        let visibleCount = 0;
        
        bookCards.forEach(card => {
            const title = card.querySelector('h3')?.textContent?.toLowerCase() || '';
            const author = card.querySelector('.autore')?.textContent?.toLowerCase() || '';
            const price = parseFloat(card.getAttribute('data-prezzo')) || 0;
            const available = card.getAttribute('data-disponibile') === 'true';
            const categories = (card.getAttribute('data-categorie') || '').split(',').map(Number).filter(n => !isNaN(n));
            const searchTerm = activeFilters.searchTerm;
            
            console.log('Controllo libro:', {title, author, price, available, categories});
            
            //applico i filtri
            const matchesSearch = searchTerm === '' || 
                                 title.includes(searchTerm) || 
                                 author.includes(searchTerm);
            
            const matchesCategory = activeFilters.categoryId === 0 || 
                                  categories.includes(activeFilters.categoryId);
            
            let matchesPrice = true;
            if (activeFilters.priceRange) {
                switch(activeFilters.priceRange) {
                    case 'sotto10':
                        matchesPrice = price < 10;
                        break;
                    case '10-20':
                        matchesPrice = price >= 10 && price <= 20;
                        break;
                    case 'oltre20':
                        matchesPrice = price > 20;
                        break;
                }
            }
            
            let matchesAvailability = true;
            if (activeFilters.availability) {
                matchesAvailability = (activeFilters.availability === 'disponibile') ? available : !available;
            }
            
            console.log('Risultati filtri:', {matchesSearch, matchesCategory, matchesPrice, matchesAvailability});
            
            //mostro o nascondo la card in base ai filtri
            if (matchesSearch && matchesCategory && matchesPrice && matchesAvailability) {
                card.style.display = '';
                visibleCount++;
            } else {
                card.style.display = 'none';
            }
        });
        
        console.log('Visibili', visibleCount, 'libri su', bookCards.length);

        const noResults = document.querySelector('.nessun-risultato');
        if (visibleCount === 0) {
            console.log('Nessun risultato trovato');
            if (noResults) {
                noResults.style.display = 'block';
            } else {
                const noResultsMsg = document.createElement('div');
                noResultsMsg.className = 'nessun-risultato';
                noResultsMsg.innerHTML = '<p>Nessun libro disponibile con i filtri selezionati.</p>';
                booksGrid.parentNode.insertBefore(noResultsMsg, booksGrid.nextSibling);
            }
        } else {
            if (noResults) {
                noResults.style.display = 'none';
            }
        }
    }
    
    // Funzione per filtrare per categoria
    window.filtraPerCategoria = function(categoriaId) {
        console.log('Filtra per categoria:', categoriaId);

        categoryItems.forEach(item => {
            const itemId = parseInt(item.getAttribute('data-categoria-id'));
            if (itemId === categoriaId) {
                item.classList.add('selezionata');
            } else {
                item.classList.remove('selezionata');
            }
        });

        activeFilters.categoryId = categoriaId;
        filterBooks();
    };
    
    // Inizializzo gli event listener al caricamento della pagina
    console.log('Inizializzazione filtri in corso...');
    initEventListeners();
    
    // Aggiungi un listener per il caricamento completo della pagina
    window.addEventListener('load', function() {
        console.log('Pagina completamente caricata');
        filterBooks();
    });
});
