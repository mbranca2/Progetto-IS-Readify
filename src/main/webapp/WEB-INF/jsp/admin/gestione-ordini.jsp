<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Ordini - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <style>
        .icon {
            display: inline-block;
            width: 1em;
            height: 1em;
            margin-right: .5em;
            vertical-align: middle;
        }

        .icon-orders::before {
            content: '🧾';
        }

        .table-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background: #f7f7f7;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 10px;
            background: #eee;
            font-size: 0.9em;
        }

        .muted {
            color: #666;
        }
    </style>
</head>
<body>
<jsp:include page="../header.jsp"/>

<div class="container">
    <div class="page-header" style="display:flex; align-items:center; justify-content:space-between; margin:20px 0;">
        <h1><span class="icon icon-orders"></span>Gestione Ordini</h1>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="muted">Torna alla dashboard</a>
    </div>

    <div class="card">
        <div class="card-content">
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>ID Ordine</th>
                        <th>ID Utente</th>
                        <th>Data</th>
                        <th>Stato</th>
                        <th>Totale</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty ordini}">
                            <tr>
                                <td colspan="5" class="muted">Nessun ordine presente.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${ordini}" var="o">
                                <tr>
                                    <td>#${o.idOrdine}</td>
                                    <td>${o.idUtente}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.dataOrdine}">
                                                <fmt:formatDate value="${o.dataOrdine}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge">${o.stato}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.totale}">
                                                € <fmt:formatNumber value="${o.totale}" minFractionDigits="2"
                                                                    maxFractionDigits="2"/>
                                            </c:when>
                                            <c:otherwise>€ 0,00</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
</body>
</html>
