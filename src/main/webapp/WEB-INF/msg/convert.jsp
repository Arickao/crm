<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript">
        alert("线索转换失败,网页将跳转到线索首页");
        window.open("workbench/clue/index.jsp","workareaFrame");
    </script>
</head>
<body>

</body>
</html>