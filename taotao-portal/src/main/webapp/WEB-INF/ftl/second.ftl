<html>
<head>
	<title>${title}</title>
</head>
<body>
	<label>ѧ�ţ�</label>${student.id}<br>
	<label>������</label>${student.name}<br>
	<label>סַ��</label>${student.address}<br>
	ѧ���б�
	<table border="1">
	<#list students as s>
		<tr>
			<td>${s.id}</td>
			<td>${s.name}</td>
			<td>${s.address}</td>
		</tr>
	</#list>
	</table>
</body>
</html>