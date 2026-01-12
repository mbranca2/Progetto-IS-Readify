<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Account - Librorama</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="account-container">
    <c:if test="${not empty requestScope.messaggio}">
        <div class="alert alert-${requestScope.tipoMessaggio}">
            ${requestScope.messaggio}
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${not empty sessionScope.utente}">
            <div class="profile-header">
                <h2>Benvenuto, ${sessionScope.utente.nome}!</h2>
                <p>Gestisci il tuo account e i tuoi ordini</p>

                <c:if test="${not empty requestScope.messaggio}">
                    <div class="alert-success">${requestScope.messaggio}</div>
                </c:if>

                <c:if test="${not empty requestScope.errore}">
                    <div class="alert-error">${requestScope.errore}</div>
                </c:if>
            </div>

            <div class="account-content">
                <!-- Menu laterale -->
                <div class="account-menu">
                    <button class="tab-button active" data-target="profile">Profilo</button>
                    <button class="tab-button" data-target="password">Password</button>
                    <button class="tab-button" data-target="orders">I miei ordini</button>
                </div>

                <!-- Contenuti -->
                <div class="tab-section">
                    <div id="profile" class="tab-panel active">
                        <h3 class="section-title">Informazioni personali</h3>
                        <form method="post" action="${pageContext.request.contextPath}/profilo" class="account-form">
                            <div class="form-group">
                                <label for="nome" class="form-label">Nome</label>
                                <input type="text" id="nome" name="nome" class="form-input"
                                       value="${sessionScope.utente.nome}" required>
                            </div>
                            <div class="form-group">
                                <label for="cognome" class="form-label">Cognome</label>
                                <input type="text" id="cognome" name="cognome" class="form-input"
                                       value="${sessionScope.utente.cognome}" required>
                            </div>
                            <div class="form-group">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" id="email" name="email" class="form-input"
                                       value="${sessionScope.utente.email}" required>
                            </div>
                            <div class="form-group">
                                <label for="telefono" class="form-label">Telefono</label>
                                <input type="tel" id="telefono" name="telefono" class="form-input"
                                       value="${sessionScope.utente.telefono}">
                            </div>
                            <button type="submit" class="btn">Salva modifiche</button>
                        </form>
                    </div>

                    <div id="password" class="tab-panel">
                        <h3 class="section-title">Cambia password</h3>
                        <form method="post" action="${pageContext.request.contextPath}/cambia-password" class="account-form" id="passwordChangeForm" onsubmit="return validatePasswordChange()">
                            <div class="form-group">
                                <label for="vecchiaPassword" class="form-label">Password attuale</label>
                                <input type="password" id="vecchiaPassword" name="vecchiaPassword"
                                       class="form-input" required>
                                <div class="invalid-feedback"></div>
                            </div>
                            <div class="form-group">
                                <label for="nuovaPassword" class="form-label">Nuova password</label>
                                <input type="password" id="nuovaPassword" name="nuovaPassword"
                                       class="form-input" required minlength="8"
                                       pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$">
                                <div class="invalid-feedback"></div>
                            </div>
                            <div class="form-group">
                                <label for="confermaPassword" class="form-label">Conferma nuova password</label>
                                <input type="password" id="confermaPassword" name="confermaPassword"
                                       class="form-input" required>
                                <div class="invalid-feedback"></div>
                            </div>
                            <button type="submit" class="btn">Aggiorna password</button>
                        </form>
                    </div>

                    <div id="orders" class="tab-panel">
                        <h3 class="section-title">I miei ordini</h3>
                        <c:choose>
                            <c:when test="${not empty requestScope.ordini and not empty requestScope.ordini.ordini}">
                                <table class="order-table">
                                    <thead>
                                    <tr>
                                        <th>Ordine #</th>
                                        <th>Data</th>
                                        <th>Totale</th>
                                        <th>Stato</th>
                                        <th>Azioni</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${requestScope.ordini.ordini}" var="ordine">
                                        <tr>
                                            <td>#${ordine.idOrdine}</td>
                                            <td><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy"/></td>
                                            <td><fmt:formatNumber value="${ordine.totale}" type="currency" currencyCode="EUR"/></td>
                                            <td><span class="order-badge">${ordine.stato}</span></td>
                                            <td><a href="${pageContext.request.contextPath}/dettaglio-ordine?id=${ordine.idOrdine}">Dettagli</a></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-orders">
                                    <p>Nessun ordine effettuato</p>
                                    <a href="${pageContext.request.contextPath}/libri" class="btn">Inizia a fare acquisti</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-orders">
                <h3>Accesso richiesto</h3>
                <p>Per accedere a questa pagina, effettua il <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Login</a> o <a href="${pageContext.request.contextPath}/registrazione" class="btn btn-primary">Registrati</a></p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp" />

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const tabButtons = document.querySelectorAll('.tab-button');
        const tabPanels = document.querySelectorAll('.tab-panel');
        const urlParams = new URLSearchParams(window.location.search);
        const activeTab = urlParams.get('tab') || 'profile';

        function setActiveTab(tabId) {
            const newUrl = window.location.pathname + '?tab=' + tabId;
            window.history.pushState({ path: newUrl }, '', newUrl);

            tabButtons.forEach(btn => {
                if (btn.getAttribute('data-target') === tabId) {
                    btn.classList.add('active');
                } else {
                    btn.classList.remove('active');
                }
            });
            
            tabPanels.forEach(panel => {
                if (panel.id === tabId) {
                    panel.classList.add('active');
                    panel.classList.remove('hidden');
                } else {
                    panel.classList.remove('active');
                    panel.classList.add('hidden');
                }
            });
        }

        tabButtons.forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const targetTab = this.getAttribute('data-target');
                setActiveTab(targetTab);
            });
        });

        if (document.getElementById(activeTab)) {
            setActiveTab(activeTab);
        } else if (tabButtons.length > 0) {
            setActiveTab(tabButtons[0].getAttribute('data-target'));
        }

        window.addEventListener('popstate', function() {
            const urlParams = new URLSearchParams(window.location.search);
            const tab = urlParams.get('tab') || 'profile';
            setActiveTab(tab);
        });
    });
</script>

</body>
</html>
