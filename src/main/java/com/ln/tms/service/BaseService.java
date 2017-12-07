package com.ln.tms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ln.tms.pojo.BaseBean;
import com.ln.tms.mymapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * BaseService - 基类
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public abstract class BaseService<T extends BaseBean> {

    @Autowired
    private MyMapper<T> mapper;

    /**
     * 主键id查询
     *
     * @param id ID
     * @return T
     */
    public T queryById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     *
     * @return List<T>
     */
    public List<T> queryAll() {
        return mapper.selectAll();
    }

    /**
     * 条件查询所有
     *
     * @param t T
     * @return List<T>
     */
    public List<T> queryByWhere(T t) {
        return mapper.select(t);
    }

    /**
     * 条件查询一条
     *
     * @param t T
     * @return T
     */
    public T queryOne(T t) {
        return mapper.selectOne(t);
    }

    /**
     * 自定义example查询
     *
     * @param example Example
     * @return List<T>
     */
    public List<T> queryByExample(Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param t    T
     * @param page page
     * @param rows rows
     * @return PageInfo<T>
     */
    public PageInfo<T> queryPageListByWhere(T t, Integer page, Integer rows) {
        PageHelper.startPage(page, rows, true);
        List<T> list = this.queryByWhere(t);
        return new PageInfo<T>(list);
    }

    /**
     * 自定义example分页查询
     *
     * @param example Example
     * @param page    page
     * @param rows    rows
     * @return PageInfo<T>
     */
    public PageInfo<T> queryPageListByExample(Example example, Integer page, Integer rows) {
        PageHelper.startPage(page, rows, true);
        List<T> list = this.mapper.selectByExample(example);
        return new PageInfo<T>(list);
    }

    /**
     * 新增
     *
     * @param t T
     * @return Integer
     */
    public Integer save(T t) {
        t.setCreateDate(new Date());
        t.setModifyDate(t.getCreateDate());
        return this.mapper.insert(t);
    }

    /**
     * 新增数据返回主键
     *
     * @param t T
     * @return Integer
     */
    public Integer saveUseGeneratedKeys(T t) {
        return mapper.insertUseGeneratedKeys(t);
    }

    /**
     * 新增数据（null的属性不会保存持久化对象的某个属性不赋值,即不操作数据库对应的属性字段）
     *
     * @param t T
     * @return Integer
     */
    public Integer saveSelective(T t) {
        t.setCreateDate(new Date());
        t.setModifyDate(t.getCreateDate());
        return this.mapper.insertSelective(t);
    }

    /**
     * 新增List
     *
     * @param list List<T>
     * @return Integer
     */
    /*public Integer saveList(List<T> list) {
        return mapper.insertList(list);
    }*/

    /**
     * 主键id更新
     *
     * @param t T
     * @return Integer
     */
    public Integer update(T t) {
        t.setModifyDate(new Date());
        return mapper.updateByPrimaryKey(t);
    }

    /**
     * 主键更新（null的属性不会更新,更新的对象某个属性不赋值,即不操作数据库对应的属性字段）
     *
     * @param t T
     * @return Integer
     */
    public Integer updateSelective(T t) {
        t.setModifyDate(new Date());
        return mapper.updateByPrimaryKeySelective(t);

    }

    /**
     * 自定义example 更新
     *
     * @param t       T
     * @param example Example
     * @return Integer
     */
    public Integer updateByExampleSelective(T t, Example example) {
        t.setModifyDate(new Date());
        return mapper.updateByExampleSelective(t, example);
    }

    /**
     * 实体删除
     *
     * @param t T
     * @return Integer
     */
    public Integer delete(T t) {
        return mapper.delete(t);
    }

    /**
     * 主键id删除
     *
     * @param id ID
     * @return Integer
     */
    public Integer deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     *
     * @param ids      List<Object>
     * @param property String
     * @param clazz    Class<T>
     * @return Integer
     */
    public Integer deleteByIds(List<Object> ids, String property, Class<T> clazz) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }
}