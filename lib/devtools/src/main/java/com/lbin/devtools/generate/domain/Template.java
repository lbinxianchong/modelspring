package com.lbin.devtools.generate.domain;

import lombok.Data;

/**
 * @author
 * @date 2018/10/21
 */
@Data
public class Template {
    private boolean entity;
    private boolean model;
    private boolean controller;
    private boolean server;
    private boolean service;
    private boolean repository;
    private boolean validator;
    private boolean index;
    private boolean add;
    private boolean detail;
}
