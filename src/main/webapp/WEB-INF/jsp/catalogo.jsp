<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--debug -->
<c:if test="${empty libri or libri.size() == 0}">
    <div class="debug-warning">
        ATTENZIONE: La lista dei libri è vuota o non è stata caricata correttamente.
        <c:if test="${not empty requestScope['jakarta.servlet.error.message']}">
            <br/>Errore: ${requestScope['jakarta.servlet.error.message']}
        </c:if>
    </div>
</c:if>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo - Readify</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    <link rel="stylesheet" href="<c:url value='/css/catalogo.css' />">
</head>
<body>
<jsp:include page="header.jsp" />

<main class="container">
    <div class="catalogo-layout">
        <!-- Sidebar Filtri -->
        <aside class="sidebar">
            <!-- Sezione Categorie -->
            <div class="sidebar-section">
                <h3>Categorie</h3>
                <div class="categorie-list" id="categorieGrid">
                    <!-- Categoria "Tutte" -->
                    <div class="categoria-item selezionata" data-categoria-id="0" onclick="filtraPerCategoria(0)">
                        <span class="icon">📚</span>
                        <span>Tutte le categorie</span>
                    </div>

                    <c:forEach items="${categorie}" var="categoria">
                        <div class="categoria-item" data-categoria-id="${categoria.idCategoria}" 
                             onclick="filtraPerCategoria(${categoria.idCategoria})">
                            <c:choose>
                                <c:when test="${categoria.nomeCategoria.toLowerCase().contains('narrativa')}">
                                    <span class="icon">📖</span>
                                </c:when>
                                <c:when test="${categoria.nomeCategoria.toLowerCase().contains('scolastica')}">
                                    <span class="icon">🎓</span>
                                </c:when>
                                <c:when test="${categoria.nomeCategoria.toLowerCase().contains('scientific')}">
                                    <span class="icon">🔬</span>
                                </c:when>
                                <c:when test="${categoria.nomeCategoria.toLowerCase().contains('bambini')}">
                                    <span class="icon">👶</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="icon">📚</span>
                                </c:otherwise>
                            </c:choose>
                            <span>${categoria.nomeCategoria}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
            
            <div class="search-bar">
                <input type="text" placeholder="Cerca nel catalogo..." id="searchInput">
                <button type="button" id="searchButton" aria-label="Cerca">
                    <span class="icon">🔍</span>
                    <span class="sr-only">Cerca</span>
                </button>
            </div>
            
            <div class="sidebar-section">
                <h3>Filtra per</h3>
                <div class="filter-option">
                    <h4>Prezzo</h4>
                    <div>
                        <input type="radio" id="price1" name="price" value="sotto10">
                        <label for="price1">Sotto i 10€</label>
                    </div>
                    <div>
                        <input type="radio" id="price2" name="price" value="10-20">
                        <label for="price2">10€ - 20€</label>
                    </div>
                    <div>
                        <input type="radio" id="price3" name="price" value="oltre20">
                        <label for="price3">Oltre 20€</label>
                    </div>
                    <div>
                        <input type="radio" id="priceAll" name="price" value="" checked>
                        <label for="priceAll">Tutti i prezzi</label>
                    </div>
                </div>
                
                <div class="filter-option">
                    <h4>Disponibilità</h4>
                    <div>
                        <input type="radio" id="avail1" name="availability" value="disponibile">
                        <label for="avail1">Disponibile</label>
                    </div>
                    <div>
                        <input type="radio" id="avail2" name="availability" value="in-arrivo">
                        <label for="avail2">In arrivo</label>
                    </div>
                    <div>
                        <input type="radio" id="availAll" name="availability" value="" checked>
                        <label for="availAll">Tutti</label>
                    </div>
                </div>
            </div>
            
            <div class="sidebar-section">
                <h3>Contatti</h3>
                <p><span class="icon">📞</span> 123 456 7890</p>
                <p><span class="icon">✉️</span> info@readify.it</p>
                <p><span class="icon">📍</span> Via Roma 123, Milano</p>
            </div>
        </aside>

        <!-- Contenuto Principale -->
        <div class="main-content">
            <div class="libri-grid" id="booksGrid">
                <c:forEach items="${libri}" var="libro">
                    <div class="libro-card" 
                         data-categorie="<c:forEach items="${libro.categorie}" var="cat" varStatus="loop">${cat}${!loop.last ? ',' : ''}</c:forEach>"
                         data-prezzo="${libro.prezzo}" 
                         data-disponibile="${libro.disponibilita > 0}">
                        <div class="img-container">
                            <img src="${pageContext.request.contextPath}/img/libri/copertine/${not empty libro.copertina ? libro.copertina : 'default.jpg'}"
                                 alt="${libro.titolo}" 
                                 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/libri/copertine/default.jpg';">
                        </div>
                        <div class="libro-info">
                            <h3>${libro.titolo}</h3>
                            <p class="autore">${libro.autore}</p>
                            <c:if test="${not empty libro.descrizione}">
                                <p class="descrizione">
                                    ${fn:length(libro.descrizione) > 150 ? 
                                        fn:substring(libro.descrizione, 0, 150).concat('...') : 
                                        libro.descrizione}
                                </p>
                            </c:if>
                            <div class="prezzo-disponibilita">
                                <p class="prezzo">
                                    <span class="prezzo-label">Prezzo: </span>
                                    <span class="prezzo-valore">
                                        ${libro.prezzo} €
                                    </span>
                                </p>
                                <p class="disponibilita">
                                    <c:choose>
                                        <c:when test="${libro.disponibilita > 0}">
                                            <span class="disponibile">Disponibile (${libro.disponibilita})</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="non-disponibile">Non disponibile</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                            <div class="libro-azioni">
                                <c:choose>
                                    <c:when test="${libro.disponibilita > 0}">
                                        <a href="libro?id=${libro.idLibro}" class="btn btn-primary">Dettagli</a>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="tooltip-container">
                                            <button class="btn btn-secondary" disabled>Avvisami</button>
                                            <span class="tooltip-text">WIP</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty libri}">
                    <div class="nessun-risultato">
                        <p>Nessun libro disponibile con i filtri selezionati.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp" />

<script src="${pageContext.request.contextPath}/js/catalogo-filtri.js"></script>

</body>
</html>
