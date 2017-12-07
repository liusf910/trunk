package com.ln.tms.mapper;

import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Storage;
import com.ln.tms.pojo.StorageCourier;
import com.ln.tms.pojo.UserStorage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * StorageMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface StorageMapper extends MyMapper<Storage> {

    /**
     * 查询仓库列表
     *
     * @param storage
     * @return
     */
    public List<Storage> queryStoragePage(Storage storage);

    /**
     * 查询仓库总条数
     *
     * @param storage
     * @return
     */
    public Integer getStorageTotal(Storage storage);

    /**
     * 根据storageCode查询仓库信息
     *
     * @param storageCode
     * @return
     */
    public Storage getStorageBySCode(String storageCode);

    /**
     * 保存仓库信息
     *
     * @param storage
     */
    public Integer saveStorage(Storage storage);

    /**
     * 保存仓库和快递公司关联信息
     *
     * @param storageCourier
     * @return
     */
    public Integer saveStorageCourier(StorageCourier storageCourier);

    /**
     * 校验仓库名称
     *
     * @param storage
     * @return
     */
    public Integer checkStorageName(Storage storage);

    /**
     * 校验仓库编号
     *
     * @param storage
     * @return
     */
    public Integer checkStorageCode(Storage storage);

    /**
     * 删除仓库
     *
     * @param storageCode
     */
    public void deleteStorageByCode(String storageCode);

    /**
     * 获取仓库对应的快递公司信息
     *
     * @param storageCode
     * @return
     */
    public List<StorageCourier> getStorageCouriers(String storageCode);

    /**
     * 修改仓库信息
     *
     * @param storage
     */
    public void updateStorage(Storage storage);

    /**
     * 删除已关联的快递信息
     *
     * @param storage
     */
    public void deleteStorageCourier(Storage storage);

    /**
     * 查询所有的仓库
     *
     * @return
     */
    public List<Storage> queryAllNew();

    /**
     * 根据仓库获取对应的快递公司
     *
     * @param list
     * @return
     */
    public List<Courier> getCouriers(List<String> list);

    /**
     * 获取仓库和用户是否关联
     *
     * @param storageCode
     * @return
     */
    public Integer getBindUserStorage(String storageCode);

    /**
     * 根据用户ID查询用户所属仓库
     *
     * @param userId 用户ID
     * @return List
     */
    List<Storage> getByUserId(String userId);

    /**
     * 根据仓库获取对应的快递公司New
     *
     * @param storageCode
     * @return
     */
    public List<Courier> getCouriersNew(String storageCode);

    /**
     * 获取全部仓库快递公司
     *
     * @param infoWhere
     * @return
     */
    public List<StorageCourier> getAllStorageCourier(@Param(value = "where") InfoWhere infoWhere);

    /**
     * 根据用户查询用户管理的仓库
     *
     * @param userId 用户Id
     * @return List
     */
    public List<Storage> queryBindUserStorage(String userId);

    /**
     * 查询用户仓库下的快递编号
     *
     * @param userStorage 查询条件
     * @return String
     */
    String queryCourierByUser(UserStorage userStorage);
    
	/**
	 * 获取用户对应的仓库快递公司
	 * @param infoWhere
	 * @return
	 */
	public List<StorageCourier> getStorageCourierByUserId(@Param(value = "where") InfoWhere infoWhere);
}
