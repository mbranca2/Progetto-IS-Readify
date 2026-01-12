<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrello - Librorama</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    <link rel="stylesheet" href="<c:url value='/css/cart.css' />">
</head>
<body>
<jsp:include page="header.jsp" />

<div class="cart-container">
    <h1>Il tuo carrello</h1>

    <c:choose>
        <c:when test="${empty sessionScope.carrello or sessionScope.carrello.vuoto}">
            <div class="empty-cart">
                <h2>Il tuo carrello è vuoto</h2>
                <p>Non hai ancora aggiunto nessun libro al carrello.</p>
                <a href="${pageContext.request.contextPath}/libri">Sfoglia il catalogo</a>
            </div>
        </c:when>
        <c:otherwise>
            <div id="cart-items">
                <c:forEach items="${sessionScope.carrello.articoli}" var="articolo">
                    <div class="cart-item" data-product-id="${articolo.libro.idLibro}">
                        <img src="${pageContext.request.contextPath}/img/libri/copertine/${not empty articolo.libro.copertina ? articolo.libro.copertina : 'default.jpg'}"
                             alt="${articolo.libro.titolo}">
                        <div class="item-details">
                            <h3 class="item-title">${articolo.libro.titolo}</h3>
                            <p class="item-author">di ${articolo.libro.autore}</p>
                            Prezzo del libro: <p class="item-price"><fmt:formatNumber value="${articolo.libro.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2" /></p>
                            Prezzo totale libri: <p class="item-total subtotal" id="totale-${articolo.libro.idLibro}"><fmt:formatNumber value="${articolo.libro.prezzo * articolo.quantita}" type="currency" currencySymbol="€" minFractionDigits="2" /></p>
                            <div class="quantity-controls">
                                <button type="button" class="quantity-btn minus"
                                        data-product-id="${articolo.libro.idLibro}"
                                        style="padding: 6px 12px; background-color: #0d6efd; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 16px; cursor: pointer; margin-right: 5px;">
                                    -
                                </button>
                                <input type="number" class="quantity-input"
                                       value="${articolo.quantita}"
                                       min="1"
                                       max="${articolo.libro.disponibilita}"
                                       data-product-id="${articolo.libro.idLibro}"
                                       style="width: 50px; text-align: center; border: 1px solid #ccc; border-radius: 4px; padding: 4px;">
                                <button type="button" class="quantity-btn plus"
                                        data-product-id="${articolo.libro.idLibro}"
                                        style="padding: 6px 12px; background-color: #0d6efd; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 16px; cursor: pointer; margin-left: 5px;">
                                    +
                                </button>
                            </div>
                            <button type="button" class="remove-btn"
                                    data-product-id="${articolo.libro.idLibro}"
                                    style="margin-top: 10px; padding: 6px 12px; background-color: #dc3545; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: bold;">
                                Rimuovi
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="cart-summary">
                <div class="cart-total">
                    Totale: <span id="cart-total"><fmt:formatNumber value="${sessionScope.carrello.totale}" type="currency" currencySymbol="€" minFractionDigits="2" /></span>
                </div>
                <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                    Procedi all'acquisto
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp" />

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="<c:url value='/js/carrello.js' />"></script>

</body>
</html>