package com.cubebox.tiaoyitiao.service;

/**
 * Created by luozi on 2016/4/15.
 */
public interface serviceInterface {

    /**
     * 初始化类，调用顺序为1
     */
    void initObject();

    /**
     * 初始化数据，调用顺序为2
     */
    void initData();

    /**
     * 得到布局的id，调用顺序为3
     */
    void initListener();
}
