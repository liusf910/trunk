package com.ln.tms.mapper;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.StorageAppoint;

public interface StorageAppointMapper extends MyMapper<StorageAppoint>{

	/**根据仓库名称查询仓库代码
	 * @param warehouse
	 * @return
	 */
	StorageAppoint queryStorageAppointByName(String warehouse);

	/**
	 * 校检仓库代码
	 * @param storageAppoint
	 * @return
	 */
	Integer checkStorageAppointCode(StorageAppoint storageAppoint);
}
