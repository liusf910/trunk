package com.ln.tms.service;

import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mapper.StorageMapper;
import com.ln.tms.pojo.Courier;
import com.ln.tms.pojo.Storage;
import com.ln.tms.pojo.StorageCourier;
import com.ln.tms.pojo.UserStorage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StorageService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class StorageService extends BaseService<Storage> {

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private CourierService courierService;

    /**
     * 查询仓库列表
     *
     * @param storage Storage
     * @return List<Storage>
     */
    @Transactional(readOnly = true)
    public List<Storage> queryStoragePage(Storage storage) {
        return storageMapper.queryStoragePage(storage);
    }

    /**
     * 查询仓库总条数
     *
     * @param storage Storage
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer getStorageTotal(Storage storage) {
        return storageMapper.getStorageTotal(storage);
    }

    /**
     * 根据storageCode查询仓库信息
     *
     * @param storageCode 仓库code
     * @return Storage
     */
    @Transactional(readOnly = true)
    public Storage getStorageBySCode(String storageCode) {
        return storageMapper.getStorageBySCode(storageCode);
    }

    /**
     * 保存仓库
     *
     * @param storage Storage
     */
    @Transactional
    public void saveStorage(Storage storage) {
        storageMapper.saveStorage(storage); // 保存仓库信息
        this.saveStorageCourier(storage);
    }

    /**
     * 编辑仓库
     *
     * @param storage  Storage
     */
    @Transactional
    public void editStorage(Storage storage) {
        storageMapper.updateStorage(storage); // 修改仓库信息
        storageMapper.deleteStorageCourier(storage);//删除已关联的快递信息
        this.saveStorageCourier(storage);
    }


    /**
     * 保存仓库和快递关联信息
     *
     * @param storage  Storage
     */
    @Transactional
    private void saveStorageCourier(Storage storage) {
        String storageCourierStr = storage.getStorageCourier();
        if (StringUtils.isNotBlank(storageCourierStr)) {
            String shipperCodeArr[] = storageCourierStr.split(",");
            for (String shipperCode : shipperCodeArr) {
                StorageCourier storageCourier = new StorageCourier();
                storageCourier.setShipperCode(shipperCode);
                storageCourier.setStorageCode(storage.getStorageCode());
                storageMapper.saveStorageCourier(storageCourier);
            }
        }
    }


    /**
     * 校验仓库编号
     *
     * @param storageCode 仓库code
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkStorageCode(String storageCode) {
        Storage storage = new Storage();
        storage.setStorageCode(storageCode);
        return storageMapper.checkStorageCode(storage) > 0 ;
    }

    /**
     * 校验仓库名称
     *
     * @param storageCode 仓库code
     * @param storageName 仓库名称
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkStorageName(String storageCode, String storageName) {
        Storage storage = new Storage();
        storage.setStorageCode(storageCode);
        storage.setStorageName(storageName);
        return storageMapper.checkStorageName(storage) > 0 ;
    }

    /**
     * 删除仓库
     *
     * @param storageCode 仓库code
     * @return boolean
     */
    @Transactional
    public boolean deleteStorageByCode(String storageCode) {
        //查询用户是否绑定其仓库，有的话不能删除
        if (storageMapper.getBindUserStorage(storageCode) > 0) {
            return false;
        }
        storageMapper.deleteStorageByCode(storageCode);
        return true;
    }

    /**
     * 获取仓库对应的快递公司信息
     *
     * @param storageCode 仓库code
     * @return List<Courier>
     */
    @Transactional(readOnly = true)
    public List<Courier> getStorageCouriers(String storageCode) {
        List<Courier> cList = courierService.queryAll();
        List<StorageCourier> scList = storageMapper.getStorageCouriers(storageCode);
        for (StorageCourier sc : scList) {
            for (Courier c : cList) {
                if (sc.getShipperCode().equals(c.getShipperCode())) {
                    c.setIsSelected(true);
                }
            }
        }
        return cList;
    }

    /**
     * 查询所有的仓库
     *
     * @return List<Storage>
     */
    @Transactional(readOnly = true)
    public List<Storage> queryAllNew() {
        return storageMapper.queryAllNew();
    }

    /**
     * 根据仓库获取对应的快递公司
     *
     * @param storageCode 仓库code
     * @return List<Map<String, Object>>
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCouriers(String storageCode) {
        String storageCodeArr[] = storageCode.split(",");
        List<String> list = new ArrayList<String>();
        CollectionUtils.addAll(list, storageCodeArr);
        List<Courier> cList = storageMapper.getCouriers(list);
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        for (Courier c : cList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", c.getShipperCode());
            map.put("label", c.getCourierName());
            returnList.add(map);
        }
        return returnList;
    }

    /**
     * 根据用户ID查询用户所属仓库
     *
     * @param userId 用户ID
     * @return List<Storage>
     */
    @Transactional(readOnly = true)
    public List<Storage> getStorageByUserId(String userId) {
        return storageMapper.getByUserId(userId);
    }

    /**
     * 根据仓库获取对应的快递公司New
     *
     * @param storageCode 仓库code
     * @return List<Courier>
     */
    public List<Courier> getCouriersNew(String storageCode) {
        return storageMapper.getCouriersNew(storageCode);
    }

    /**
     * 获取全部仓库快递公司
     *
     * @param infoWhere InfoWhere
     * @return List<StorageCourier>
     */
    public List<StorageCourier> getAllStorageCourier(InfoWhere infoWhere) {
        return storageMapper.getAllStorageCourier(infoWhere);
    }

    /**
     * 根据用户查询用户管理的仓库
     *
     * @param userId 用户编号
     * @return List<Storage>
     */
    public List<Storage> queryBindUserStorage(String userId) {
        return storageMapper.queryBindUserStorage(userId);
    }

    /**
     * 查询用户仓库下的快递编号
     *
     * @param userStorage 查询条件
     * @return String 快递编号
     */
    @Transactional(readOnly = true)
    public String queryCourierByUser(UserStorage userStorage) {
        return storageMapper.queryCourierByUser(userStorage);
    }


    /**
     * 获取用户对应的仓库快递公司
     *
     * @param infoWhere InfoWhere
     * @return  List<StorageCourier>
     */
    public List<StorageCourier> getStorageCourierByUserId(InfoWhere infoWhere) {
        return storageMapper.getStorageCourierByUserId(infoWhere);
    }

}
