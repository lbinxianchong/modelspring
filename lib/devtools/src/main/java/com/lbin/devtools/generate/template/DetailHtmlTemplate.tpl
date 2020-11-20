<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="/common/template :: header(~{::title},~{::link},~{::style})">
</head>
<th:block th:replace="/common/detail :: detail(~{::tbody})">
    <th:block th:fragment="tbody">
        <th:block jsoup="detail" th:replace="/common/detail :: ttext2 ('#{text1.title}',#{text1.name},'#{text2.title}',#{text2.name})"></th:block>
        <th:block jsoup="ttext" th:replace="/common/detail :: ttext ('#{text1.title}',#{text1.name})"></th:block>
        <th:block jsoup="remark" th:replace="/common/detail :: ttext ('备注',${model.remark})"></th:block>
    </th:block>
</th:block>
</html>