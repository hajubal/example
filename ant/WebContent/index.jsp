<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.Date"%><%
    
    Date result = (Date)request.getAttribute("result");
    
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
Hello world.
<form action="/test.do">
	<input type="text" name="name" value="ha">
	<input type="submit" value="send"><br>
	<%=result == null ? "" : result%>
</form>
</body>
</html>