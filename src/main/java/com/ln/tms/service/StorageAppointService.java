package com.ln.tms.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.ln.tms.mapper.StorageAppointMapper;
import com.ln.tms.pojo.StorageAppoint;

import tk.mybatis.mapper.entity.Example;

@Service
public class StorageAppointService extends BaseService<StorageAppoint>{

	@Autowired
	StorageAppointMapper storageAppointMapper;
	
	/**
	 * 分页查询仓库预约信息
	 * @param storageAppoint
	 * @param page
	 * @param rows
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageInfo<StorageAppoint> queryStorageAppointPage(
			StorageAppoint storageAppoint,
			Integer page,
			Integer rows){
		Example example = new Example(StorageAppoint.class);
		example.setOrderByClause("create_date DESC");
		Example.Criteria criteria = example.createCriteria();
		if(StringUtils.isNotBlank(storageAppoint.getWarehouseCode())){
			criteria.andEqualTo("warehouseCode",storageAppoint.getWarehouseCode());
		}
		if(StringUtils.isNotBlank(storageAppoint.getName())){
			criteria.andLike("name", "%"+storageAppoint.getName()+"%");
		}
		return super.queryPageListByExample(example, page, rows);
	}
	
	/**
	 * 验证仓库编号
	 * @param warehouseCode
	 * @return
	 */
	@Transactional(readOnly=true)
	public boolean checkStorageAppoint(String warehouseCode){
		StorageAppoint storageAppoint = new StorageAppoint();
		storageAppoint.setWarehouseCode(warehouseCode);
		return storageAppointMapper.checkStorageAppointCode(storageAppoint)>0;
	}
}
