integerArray(restrictList) ::= <%
new int[]{
$restrictList:{sub | $sub$}; separator=","$
}


%>