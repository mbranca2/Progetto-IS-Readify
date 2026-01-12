<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei ordini - Librorama</title>
    <style>
        /* Stili di base */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        
        body {
            background-color: #f5f5f5;
            color: #333;
            line-height: 1.6;
        }
        
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        h1 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
            text-align: center;
        }
        
        .no-orders {
            text-align: center;
            padding: 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-top: 2rem;
        }
        
        .no-orders p {
            margin-bottom: 1rem;
            color: #666;
        }
        
        .btn {
            display: inline-block;
            padding: 0.7rem 1.5rem;
            background-color: #2e7d32;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 600;
            transition: background-color 0.2s;
        }
        
        .btn:hover {
            background-color: #1b5e20;
        }
        
        /* Stili per la lista ordini */
        .orders-list {
            margin-top: 2rem;
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
        
        .item-details {
            flex: 1;
        }
        
        .order-total {
            margin-top: 1.5rem;
            padding-top: 1.5rem;
            border-top: 1px solid #eee;
            text-align: right;
            font-size: 1.2rem;
            font-weight: 600;
        }
        
        .order-actions {
            margin-top: 1.5rem;
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
        }

        
        /* Responsive */
        @media (max-width: 768px) {
            .order-header {
                flex-direction: column;
            }
            
            .order-item {
                flex-direction: column;
                text-align: center;
            }
            
            .item-image {
                margin: 0 0 1rem 0;
            }
            
            .order-actions {
                flex-direction: column;
            }
            
            .btn {
                width: 100%;
                text-align: center;
            }
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
                                <span class="order-status status-${ordine.stato.toString().toLowerCase()}">
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
                                    <c:if test="${ordine.stato == 'IN_ATTESA'}">
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

    <jsp:include page="footer.jsp" />

    <script>
        function annullaOrdine(ordineId) {
            if (confirm('Sei sicuro di voler annullare questo ordine?')) {
                fetch('${pageContext.request.contextPath}/annulla-ordine', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'id=' + ordineId
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('Si è verificato un errore durante l\'annullamento dell\'ordine.');
                    }
                })
                .catch(error => {
                    console.error('Errore:', error);
                    alert('Si è verificato un errore durante l\'annullamento dell\'ordine.');
                });
            }
        }
    </script>
</body>
</html>
