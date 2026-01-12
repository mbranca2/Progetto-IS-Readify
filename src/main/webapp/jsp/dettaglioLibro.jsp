<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty libro}">
    <c:redirect url="/libri" />
</c:if>

<!DOCTYPE html>
<html lang="it" class="book-detail-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <c:set var="descrizioneBreve" value="${libro.descrizione != null ? libro.descrizione : ''}" />
    <c:set var="descrizioneLunghezza" value="${not empty libro.descrizione ? libro.descrizione.length() : 0}" />
    <c:set var="descrizioneDaMostrare" value="${descrizioneLunghezza > 160 ? descrizioneBreve.substring(0, 160) : descrizioneBreve}" />
    <meta name="description" content="${not empty libro.descrizione ? descrizioneDaMostrare : 'Dettagli del libro ' += libro.titolo}">
    <title>${libro.titolo} - Librorama</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-detail.css">
</head>
<body>
<jsp:include page="header.jsp" />
<main class="container">
    <div class="book-detail-container">
        <div class="book-cover-container">
            <c:choose>
                <c:when test="${not empty libro.copertina}">
                    <img src="${pageContext.request.contextPath}/img/libri/copertine/${libro.copertina}" alt="${libro.titolo}" class="book-cover">
                </c:when>
                <c:otherwise>
                    <div class="no-cover">
                        <div class="no-cover-icon">📚</div>
                        <p>Nessuna copertina disponibile</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="book-info">
            <h1 class="book-title">${libro.titolo}</h1>
            <p class="book-author">di ${libro.autore}</p>

            <div class="book-meta">
                <span>📋 ISBN: ${libro.isbn}</span>
                <span class="availability">
                        <c:choose>
                            <c:when test="${libro.disponibilita > 0}">
                                ✅ Disponibile (${libro.disponibilita} copie)
                            </c:when>
                            <c:otherwise>
                                ❌ Non disponibile
                            </c:otherwise>
                        </c:choose>
                    </span>
            </div>

            <div class="book-price">
                ${libro.prezzo} €
            </div>

            <c:choose>
                <c:when test="${libro.disponibilita > 0}">
                    <form action="${pageContext.request.contextPath}/carrello" method="post">
                        <input type="hidden" name="azione" value="aggiungi">
                        <input type="hidden" name="idLibro" value="${libro.idLibro}">
                        <input type="hidden" name="quantita" value="1">
                        <button type="submit" class="btn-add-to-cart">
                            🛒 Aggiungi al carrello
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="btn-add-to-cart" disabled>
                        ❌ Non disponibile
                    </button>
                </c:otherwise>
            </c:choose>

            <c:if test="${not empty param.aggiunto}">
                <div class="alert alert-success" role="alert">
                    <span>✅</span>
                    <span>Prodotto aggiunto al carrello!</span>
                </div>
            </c:if>

            <div class="book-details">
                <h3>Descrizione</h3>
                <p>${not empty libro.descrizione ? libro.descrizione : 'Nessuna descrizione disponibile per questo libro.'}</p>
                
                <h3>Dettagli</h3>
                <ul class="details-list">
                    <li><strong>Autore:</strong> ${libro.autore}</li>
                    <li><strong>ISBN:</strong> ${libro.isbn}</li>
                    <li><strong>Prezzo:</strong> ${libro.prezzo} €</li>
                    <li><strong>Disponibilità:</strong>
                        <c:choose>
                            <c:when test="${libro.disponibilita > 0}">
                                <span class="in-stock">In magazzino (${libro.disponibilita} copie)</span>
                            </c:when>
                            <c:otherwise>
                                <span class="out-of-stock">Esaurito</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>

                <h3>Recensioni</h3>
                <c:choose>
                    <c:when test="${not empty valutazioni}">
                        <div class="reviews-container">
                            <c:forEach items="${valutazioni}" var="valutazione">
                                <div class="review-card">
                                    <div class="review-header">
                                        <span class="reviewer-name">${valutazione.nomeUtente}</span>
                                        <div class="rating">
                                            <c:forEach begin="1" end="5" var="i">
                                                <span class="star ${i <= valutazione.voto ? 'filled' : ''}">★</span>
                                            </c:forEach>
                                        </div>
                                        <span class="review-date">
                                            <fmt:formatDate value="${valutazione.dataRecensione}" pattern="dd/MM/yyyy"/>
                                        </span>
                                    </div>
                                    <p class="review-comment">${valutazione.commento}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="no-reviews">Non ci sono ancora recensioni per questo libro.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            </div>
        </div>
</main>

<jsp:include page="footer.jsp" />

<script>

    function addToCart(bookId, quantity) {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('idLibro', bookId);
        formData.append('quantita', quantity);
        
        xhr.open('POST', '${pageContext.request.contextPath}/aggiungi-al-carrello', true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                showAddToCartFeedback();
                const cartCount = document.querySelector('.cart-count');
                if (cartCount) {
                    const currentCount = parseInt(cartCount.textContent) || 0;
                    cartCount.textContent = currentCount + quantity;
                }
            } else {
                alert('Si è verificato un errore durante l\'aggiunta al carrello.');
            }
        };
        xhr.send(formData);
    }

    function showAddToCartFeedback() {
        const feedback = document.createElement('div');
        feedback.className = 'cart-feedback';
        feedback.textContent = 'Prodotto aggiunto al carrello!';
        
        document.body.appendChild(feedback);

        void feedback.offsetWidth;
        feedback.classList.add('show');
        
        setTimeout(() => {
            feedback.classList.remove('show');
            setTimeout(() => feedback.remove(), 300);
        }, 3000);
    }
</script>
</body>
</html>