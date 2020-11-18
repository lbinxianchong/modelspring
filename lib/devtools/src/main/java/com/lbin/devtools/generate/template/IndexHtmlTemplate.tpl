<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:mo="http://www.w3.org/1999/xhtml">
<head th:replace="/common/template :: header(~{::title},~{::link},~{::style})">
</head>
<th:block th:replace="/common/index :: index('#{baseUrl}',~{::header},~{::search},~{::buttonleft},~{::buttonright},~{::table},~{::thead},~{::tbody},~{::tajax})">
    <th:block th:fragment="header">
        <span>#{title}管理</span>
    </th:block>
    <th:block th:fragment="search">

        <th:block th:with="status=${param.status}">
            <th:block th:replace="/common/index :: paramstatus ('状态','status','SEARCH_STATUS',${status})"></th:block>
        </th:block>
        <th:block jsoup="search" th:with="#{search.name}=${param.#{search.name}}">
            <th:block th:replace="/common/index :: paramtext ('#{search.title}','#{search.name}',${#{search.name}})"></th:block>
        </th:block>
    </th:block>
    <th:block th:fragment="buttonleft">

    </th:block>
    <th:block th:fragment="buttonright">

    </th:block>
    <!--<th:block th:fragment="table"></th:block>-->
    <th:block th:fragment="thead">
        #{th}
    </th:block>
    <th:block th:fragment="tbody">
        #{list}
    </th:block>
    <!--<th:block th:fragment="tajax"></th:block>-->
</th:block>
</html>