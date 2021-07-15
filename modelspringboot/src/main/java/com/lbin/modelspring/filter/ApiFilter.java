package com.lbin.modelspring.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ʹ��ע���ע������
 *
 * @WebFilter��һ��ʵ����javax.servlet.Filter�ӿڵ��ඨ��Ϊ������ ����filterName����������������, ��ѡ
 * ����urlPatternsָ��Ҫ���� ��URLģʽ,Ҳ��ʹ������value������.(ָ��Ҫ���˵�URLģʽ�Ǳ�ѡ����)
 */
@Slf4j
//@WebFilter(filterName = "apiFilter", urlPatterns = "/api/*")
public class ApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("apiFilter��ʼ��");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("apiFilter-" + request.getLocalAddr() + "-" + request.getServletPath());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("apiFilter����");
    }
}
