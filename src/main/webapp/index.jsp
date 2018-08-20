<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>

<form action="/user/login.do" method="post">
    <input type="text" value="admin" name="username">
    <input type="password" value="zhouheng" name="password">
    <input type="submit" value="Login">
</form>


<form action="/user/reset_password.do" method="post">
    <input type="text" value="admin" name="passwordOld">
    <input type="password" value="zhouheng" name="passwordNew">
    <input type="submit" value="RestPassword">
</form>
<br/>
<br/>
<form action="/user/update_information.do" method="post">
    <input type="text" value="email@163.com" name="email">
    <input type="text" value="15893883888" name="phone">
    <input type="text" value="问题" name="question">
    <input type="text" value="答案" name="answer">
    <input type="submit" value="跟新信息">
</form>
<form action="/manage/category/get_category.do" method="post">
    <input type="text" value="0" name="categoryId">
    <input type="submit" value="获取商品">
</form>

<form action="/product/detail.do" method="post">
    <input type="text" value="67" name="productId">
    <input type="submit" value="获取product商品">
</form>

<br/>
<br/>
<form action="/manage/category/add_category.do" method="post">
    <input type="text" value="0" name="categoryId">
    <input type="text" value="Name" name="categoryName">
    <input type="submit" value="增加商品品类" name="submit">
</form>

<br/><br/>
<form action="/manage/category/set_category_name.do" method="post">
    <input type="text" value="0" name="categoryId">
    <input type="text" value="Name" name="categoryName">
    <input type="submit" value="更新商品名称" name="submit">
</form>

<form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="上传">
</form>

<br/>
<br/>
<form action="/cart/list.do" method="post">
    <input type="text" value="0" name="categoryId">
    <input type="text" value="Name" name="categoryName">
    <input type="submit" value="购物车" name="submit">
</form>

<form action="/shipping/delete.do" method="post">
    <input type="text" value="31" name="shippingId">
    <input type="submit" value="删除">
</form>

<br/><br/>
<form action="/order/pay.do" method="post">
    <input type="text" value="1492091141269" name="orderNo">
    <input type="submit" value="付款">
</form>

</body>
</html>
