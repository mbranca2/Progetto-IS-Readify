<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Libri - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/book-management.css">
    <style>
        .icon {
            display: inline-block;
            width: 1em;
            height: 1em;
            margin-right: .5em;
            vertical-align: middle
        }

        .icon-plus::before {
            content: '➕'
        }

        .icon-search::before {
            content: '🔍'
        }

        .icon-edit::before {
            content: '✏️'
        }

        .icon-delete::before {
            content: '🗑️'
        }

        .icon-eye::before {
            content: '👁️'
        }
    </style>
</head>
<body>

<jsp:include page="../header.jsp"/>

<c:if test="${not empty param.success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${param.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${not empty param.errore}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${param.errore}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="container">
<div class="page-header">
    <h1>Gestione Libri</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/libri?azione=nuovo">
        <span class="icon icon-plus"></span>Nuovo Libro
    </a>
</div>

<div class="search-filters">
    <form method="get" action="${pageContext.request.contextPath}/admin/libri" class="search-form">
        <div class="form-grid">
            <div class="form-group">
                <label for="titolo">Titolo</label>
                <input type="text" id="titolo" name="titolo" value="${fn:escapeXml(param.titolo)}">
            </div>
            <div class="form-group">
                <label for="autore">Autore</label>
                <input type="text" id="autore" name="autore" value="${fn:escapeXml(param.autore)}">
            </div>
            <div class="form-group">
                <label for="categoria">Categoria</label>
                <select id="categoria" name="categoria">
                    <option value="">Tutte le categorie</option>
                    <c:forEach items="${categorie}" var="cat">
                        <c:set var="selectedCat"
                               value="${param.categoria != null && param.categoria == (cat.idCategoria)}"/>
                        <option value="${cat.idCategoria}" ${selectedCat ? 'selected' : ''}>
                                ${fn:escapeXml(cat.nomeCategoria)}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="prezzoMin">Prezzo min</label>
                <input type="text" id="prezzoMin" name="prezzoMin" value="${fn:escapeXml(param.prezzoMin)}">
            </div>
            <div class="form-group">
                <label for="prezzoMax">Prezzo max</label>
                <input type="text" id="prezzoMax" name="prezzoMax" value="${fn:escapeXml(param.prezzoMax)}">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">
                    <span class="icon icon-search"></span>Cerca
                </button>
            </div>
        </div>
    </form>
</div>

<div class="card">
<div class="card-content">
<div class="table-container">
<table class="book-table">
<thead>
<tr>
    <th>Copertina</th>
    <th>Titolo</th>
    <th>Autore</th>
    <th>Prezzo</th>
    <th>Disponibilità</th>
    <th class="actions">Azioni</th>
</tr>
</thead>
<tbody>
<c:forEach items="${libri}" var="libro">
    <tr>
    <td class="align-middle">
    <c:choose>
        <c:when test="${not empty libro.copertina}">
            <img src="${pageContext.request.contextPath}/img/libri/copertine/${fn:escapeXml(libro.copertina)}"
                 alt="${fn:escapeXml(libro.titolo)}" class="book-cover">
        </c:when>
        <c:otherwise>
            <img src="${pageContext.request.contextPath}/img/libri/copertine/default.jpg"
                 alt="${fn:escapeXml(libro.titolo)}" class="book-cover">
        </c:otherwise>
    </c:choose>
    </td>
    <td class="align-middle">${fn:escapeXml(libro.titolo)}</td>
    <td class="align-middle">${fn:escapeXml(libro.autore)}</td>
    <td class="align-middle">
    <fmt:formatNumber value="${libro.prezzo}" type="currency" currencySymbol="€" minFractionDigits="2"/>
    </td>
    <td class="align-middle">
    <span class="availability-badge ${libro.disponibilita > 0 ? 'available' : 'unavailable'}">
    ${libro.disponibilita > 0 ? 'Disponibile' : 'Esaurito'}
    </span>
    </td>
    <td class="actions">
    <div class="action-buttons">
    <a href="${pageContext.request.contextPath}/libro?id=${libro.idLibro}" class="btn btn-icon view" title="Visualizza" target="_blank">
    <span class="icon icon-eye"></span>
    </a>

    <a href="${pageContext.request.contextPath}/admin/libri/modifica?id=${libro.idLibro}" class="btn btn-icon edit" title="Modifica">
    <span class="icon icon-edit"></span>
    </a>

    <form id="deleteForm-${libro.idLibro}" action="${pageContext.request.contextPath}/admin/libri/elimina" method="post" style="display:inline;">
    <input type="hidden" name="azione" value="elimina">
    <input type="hidden" name="id" value="${libro.idLibro}">
    <button type="button" class="btn btn-icon delete"
    onclick="confermaEliminazione(${libro.idLibro}, '${fn:replace(fn:replace(fn:escapeXml(libro.titolo), "'", "\\'"), "\"", "\\'")}')"
        title="Elimina">
                <span class="icon icon-delete"></span>
                </button>
                </form>
                </div>
                </td>
                </tr>
                        </c:forEach>

                <c:if test="${empty libri}">
                        <tr>
                <td colspan="6" class="no-results">
                <div>Nessun libro trovato</div>
                </td>
                        </tr>
                        </c:if>
                </tbody>
                </table>
                </div>

                <c:if test="${totalePagine > 1}">
        <div class="d-flex justify-content-center mt-4">
        <nav aria-label="Paginazione">
                <ul class="pagination">

        <c:url var="prevUrl" value="${pageContext.request.contextPath}/admin/libri">
        <c:param name="pagina" value="${paginaCorrente - 1}" />
        <c:if test="${not empty param.titolo}"><c:param name="titolo" value="${param.titolo}" /></c:if>
        <c:if test="${not empty param.autore}"><c:param name="autore" value="${param.autore}" /></c:if>
        <c:if test="${not empty param.categoria}"><c:param name="categoria" value="${param.categoria}" /></c:if>
        <c:if test="${not empty param.prezzoMin}"><c:param name="prezzoMin" value="${param.prezzoMin}" /></c:if>
        <c:if test="${not empty param.prezzoMax}"><c:param name="prezzoMax" value="${param.prezzoMax}" /></c:if>
        </c:url>

        <li class="page-item ${paginaCorrente == 1 ? 'disabled' : ''}">
        <a class="page-link" href="${prevUrl}" aria-label="Precedente">
        <span aria-hidden="true">&laquo;</span>
        </a>
        </li>

        <c:forEach begin="1" end="${totalePagine}" var="i">
        <c:url var="pageUrl" value="${pageContext.request.contextPath}/admin/libri">
        <c:param name="pagina" value="${i}" />
        <c:if test="${not empty param.titolo}"><c:param name="titolo" value="${param.titolo}" /></c:if>
        <c:if test="${not empty param.autore}"><c:param name="autore" value="${param.autore}" /></c:if>
        <c:if test="${not empty param.categoria}"><c:param name="categoria" value="${param.categoria}" /></c:if>
        <c:if test="${not empty param.prezzoMin}"><c:param name="prezzoMin" value="${param.prezzoMin}" /></c:if>
        <c:if test="${not empty param.prezzoMax}"><c:param name="prezzoMax" value="${param.prezzoMax}" /></c:if>
        </c:url>

        <li class="page-item ${i == paginaCorrente ? 'active' : ''}">
        <a class="page-link" href="${pageUrl}">${i}</a>
        </li>
        </c:forEach>

        <c:url var="nextUrl" value="${pageContext.request.contextPath}/admin/libri">
        <c:param name="pagina" value="${paginaCorrente + 1}" />
        <c:if test="${not empty param.titolo}"><c:param name="titolo" value="${param.titolo}" /></c:if>
        <c:if test="${not empty param.autore}"><c:param name="autore" value="${param.autore}" /></c:if>
        <c:if test="${not empty param.categoria}"><c:param name="categoria" value="${param.categoria}" /></c:if>
        <c:if test="${not empty param.prezzoMin}"><c:param name="prezzoMin" value="${param.prezzoMin}" /></c:if>
        <c:if test="${not empty param.prezzoMax}"><c:param name="prezzoMax" value="${param.prezzoMax}" /></c:if>
        </c:url>

        <li class="page-item ${paginaCorrente == totalePagine ? 'disabled' : ''}">
                <a class="page-link" href="${nextUrl}" aria-label="Successivo">
        <span aria-hidden="true">&raquo;</span>
        </a>
        </li>

        </ul>
        </nav>
        </div>
        </c:if>

        </div>
        </div>
        </div>

        <script>
        function confermaEliminazione(idLibro, titolo) {
        if (confirm('Sei sicuro di voler eliminare il libro "' + titolo + '"?')) {
        document.getElementById('deleteForm-' + idLibro).submit();
        }
        }
                </script>

                <jsp:include page="../footer.jsp" />

        </body>
        </html>