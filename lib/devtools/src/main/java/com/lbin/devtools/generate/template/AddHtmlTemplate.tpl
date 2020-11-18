<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="/common/template :: header(~{::title},~{::link},~{::style})">
</head>
<th:block th:replace="/common/add :: add('#{baseUrl}',~{::tform})">
    <th:block th:fragment="tform">
        <th:block jsoup="field" th:replace="/common/add :: inputtext ('#{field.title}','#{field.name}',${model?.#{field.name}})"></th:block>
        <th:block jsoup="remark" th:replace="/common/add :: inputtextarea ('备注','remark',${model?.remark})"></th:block>
    </th:block>
</th:block>
</html>