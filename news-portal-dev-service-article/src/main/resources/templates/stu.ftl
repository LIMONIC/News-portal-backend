<html>
<head>
    <title>Hello Freemarker</title>
</head>
<body>

<div>hello ${there}</div>
<br>
<div>
    用户id：${stu.uid}<br/>
    用户姓名：${stu.username}<br/>
    年龄：${stu.age}<br/>
    生日：${stu.birthday?string('yyyy-MM-dd HH:mm:ss')}<br/>
    余额：${stu.amount}<br/>
    已育：${stu.haveChild?string('yes', 'no')}<br/>
    伴侣：${stu.spouse.username}, ${stu.spouse.age}岁
</div>
</body>
</html>