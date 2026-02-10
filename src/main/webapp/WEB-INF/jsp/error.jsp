<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Errore - Server</title>
</head>
<body>
<h1>Si e' verificato un errore</h1>
<p>Messaggio: ${pageContext.exception.message}</p>
<pre>
<%
    Throwable ex = pageContext.getException();
    if (ex == null) {
        ex = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
    }
    if (ex == null) {
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    if (ex != null) {
        java.io.StringWriter sw = new java.io.StringWriter();
        ex.printStackTrace(new java.io.PrintWriter(sw));
        out.println(sw.toString());
    } else {
        out.println("Nessuna eccezione disponibile.");
    }
%>
</pre>
</body>
</html>
