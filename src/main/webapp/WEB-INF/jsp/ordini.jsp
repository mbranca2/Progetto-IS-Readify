<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei ordini</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        h1 {
            margin-bottom: 2rem;
            color: #2c3e50;
        }

        .no-orders {
            text-align: center;
            padding: 3rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .no-orders h2 {
            margin-bottom: 1rem;
        }

        .order-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 1.5rem;
            overflow: hidden;
        }

        .order-header {
            background-color: #f8f9fa;
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .order-id {
            font-weight: 600;
            color: #2c3e50;
        }

        .order-date {
            color: #666;
        }

        .order-status {
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            text-transform: capitalize;
        }

        .order-details {
            padding: 1.5rem;
        }

        .order-items {
            margin-top: 1rem;
        }

        .order-item {
            display: flex;
            padding: 1rem 0;
            border-bottom: 1px solid #eee;
            align-items: center;
        }

        .order-item:last-child {
            border-bottom: none;
        }

        .item-image {
            width: 80px;
            height: 100px;
            object-fit: cover;
            border-radius: 4px;
            margin-right: 1.5rem;
        }

        .item-details h4 {
            margin: 0 0 0.5rem 0;
            color: #2c3e50;
        }

        .item-details p {
            margin: 0.2rem 0;
            color: #666;
        }

        .order-total {
            margin-top: 1.5rem;
            text-align: right;
            font-size: 1.1rem;
            font-weight: 600;
            color: #2c3e50;
        }

        .order-actions {
            margin-top: 1.5rem;
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
        }

        .btn {
            display: inline-block;
            padding: 0.7rem 1.2rem;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 600;
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #2980b9;
        }
    </style>
</head>

<body>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>I miei ordini</h1>

    <c:choose>
        <c:when test="${empty ordini}">
            <div class="no-orders">
                <h2>Nessun ordine effettuato</h2>
                <p>Non hai ancora effettuato nessun ordine nel nostro negozio.</p>
                <a href="${pageContext.request.contextPath}/libri" class="btn">Inizia a fare acquisti</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="orders-list">
                <c:forEach items="${ordini}" var="ordine">
                    <div class="order-card">
                        <div class="order-header">
                            <div>
                                <span class="order-id">Ordine #${ordine.idOrdine}</span>
                                <span class="order-date">
                                    - <fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy HH:mm" />
                                </span>
                            </div>

                            <!-- FIX: usare name() per classi CSS (toString() è descrizione) -->
                            <span class="order-status status-${ordine.stato.name().toLowerCase()}">
                                    ${ordine.stato}
                            </span>
                        </div>

                        <div class="order-details">
                            <div class="order-items">
                                <c:forEach items="${ordine.dettagli}" var="dettaglio">
                                    <div class="order-item">
                                        <img src="${pageContext.request.contextPath}/img/libri/copertine/${dettaglio.immagineCopertina != null ? dettaglio.immagineCopertina : 'default.jpg'}"
                                             alt="${dettaglio.titoloLibro}"
                                             class="item-image"
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/libri/copertine/default.jpg'"
                                             onload="if(this.naturalWidth === 0) this.src='${pageContext.request.contextPath}/img/libri/copertine/default.jpg'">
                                        <div class="item-details">
                                            <h4>${dettaglio.titoloLibro}</h4>
                                            <p>Autore: ${dettaglio.autoreLibro}</p>
                                            <p>ISBN: ${dettaglio.isbnLibro}</p>
                                            <p>Quantità: ${dettaglio.quantita}</p>
                                            <p>Prezzo: <fmt:formatNumber value="${dettaglio.prezzoUnitario}" type="currency" currencySymbol="€"/></p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <div class="order-total">
                                Totale: <fmt:formatNumber value="${ordine.totale}"
                                                          type="currency" currencyCode="EUR" maxFractionDigits="2"/>
                            </div>

                            <div class="order-actions">
                                <!-- FIX: confronto corretto sul nome enum -->
                                <c:if test="${ordine.stato.name() == 'IN_ATTESA'}">
                                    <button class="btn" onclick="annullaOrdine(${ordine.idOrdine})">
                                        Annulla ordine
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function annullaOrdine(idOrdine) {
        if (!confirm("Vuoi annullare questo ordine?")) return;

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}/annulla-ordine", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onload = function() {
            if (xhr.status === 200) {
                location.reload();
            } else {
                alert("Impossibile annullare l'ordine.");
            }
        };

        xhr.send("idOrdine=" + encodeURIComponent(idOrdine));
    }
</script>

</body>
</html>
