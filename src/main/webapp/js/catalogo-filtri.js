(function () {
    function $(sel) { return document.querySelector(sel); }
    function $all(sel) { return Array.from(document.querySelectorAll(sel)); }

    function getSelectedCategoriaId() {
        const sel = document.querySelector(".categoria-item.selezionata");
        if (!sel) return 0;
        const v = sel.getAttribute("data-categoria-id");
        const n = parseInt(v, 10);
        return isNaN(n) ? 0 : n;
    }

    function getSearchTerm() {
        const input = $("#searchInput");
        if (!input) return "";
        return (input.value || "").trim().toLowerCase();
    }

    function getSelectedPrice() {
        const checked = document.querySelector("input[name='price']:checked");
        return checked ? checked.value : "";
    }

    function getSelectedAvailability() {
        const checked = document.querySelector("input[name='availability']:checked");
        return checked ? checked.value : "";
    }

    function parsePrice(v) {
        if (v == null) return NaN;
        const s = String(v).replace(",", ".").trim();
        const n = parseFloat(s);
        return isNaN(n) ? NaN : n;
    }

    function matchPriceRange(priceValue, selected) {
        if (!selected) return true;
        if (isNaN(priceValue)) return false;

        if (selected === "sotto10") return priceValue < 10;
        if (selected === "10-20") return priceValue >= 10 && priceValue <= 20;
        if (selected === "oltre20") return priceValue > 20;

        return true;
    }

    function matchAvailability(isAvailable, selected) {
        if (!selected) return true;
        if (selected === "disponibile") return isAvailable === true;
        if (selected === "in-arrivo") return isAvailable === false;
        return true;
    }

    function matchCategoria(card, selectedCatId) {
        if (!selectedCatId || selectedCatId === 0) return true;
        const cats = (card.getAttribute("data-categorie") || "").split(",").map(x => x.trim()).filter(Boolean);
        return cats.includes(String(selectedCatId));
    }

    function matchSearch(card, term) {
        if (!term) return true;
        const titolo = (card.querySelector("h3")?.textContent || "").toLowerCase();
        const autore = (card.querySelector(".autore")?.textContent || "").toLowerCase();
        return titolo.includes(term) || autore.includes(term);
    }

    function applyFilters() {
        const selectedCatId = getSelectedCategoriaId();
        const term = getSearchTerm();
        const priceSel = getSelectedPrice();
        const availSel = getSelectedAvailability();

        const cards = $all(".libro-card");
        let visibleCount = 0;

        cards.forEach(card => {
            const prezzo = parsePrice(card.getAttribute("data-prezzo"));
            const disponibile = String(card.getAttribute("data-disponibile")) === "true";

            const ok =
                matchCategoria(card, selectedCatId) &&
                matchSearch(card, term) &&
                matchPriceRange(prezzo, priceSel) &&
                matchAvailability(disponibile, availSel);

            card.style.display = ok ? "" : "none";
            if (ok) visibleCount++;
        });

        const emptyBox = document.querySelector(".nessun-risultato");
        if (emptyBox) {
            emptyBox.style.display = visibleCount === 0 ? "" : "none";
        }
    }

    window.filtraPerCategoria = function (idCategoria) {
        const items = $all(".categoria-item");
        items.forEach(it => it.classList.remove("selezionata"));
        const el = document.querySelector(".categoria-item[data-categoria-id='" + idCategoria + "']");
        if (el) el.classList.add("selezionata");
        applyFilters();
    };

    function bind() {
        const searchInput = $("#searchInput");
        if (searchInput) {
            searchInput.addEventListener("input", applyFilters);
            searchInput.addEventListener("keydown", function (e) {
                if (e.key === "Enter") {
                    e.preventDefault();
                    applyFilters();
                }
            });
        }

        const searchBtn = $("#searchButton");
        if (searchBtn) searchBtn.addEventListener("click", applyFilters);

        $all("input[name='price']").forEach(r => r.addEventListener("change", applyFilters));
        $all("input[name='availability']").forEach(r => r.addEventListener("change", applyFilters));
    }

    document.addEventListener("DOMContentLoaded", function () {
        bind();
        applyFilters();
    });
})();
