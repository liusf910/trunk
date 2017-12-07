package com.ln.tms.erpsync;

import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.ErpSyncLog;
import com.ln.tms.pojo.Formula;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.TimeLimit;
import com.ln.tms.service.ErpSyncLogService;
import com.ln.tms.service.FormulaService;
import com.ln.tms.service.InfoService;
import com.ln.tms.service.TimeLimitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ErpSyncService - 获取ERP中间表数据Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "SqlNoDataSourceInspection"})
@Service
public class ErpSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpSyncService.class);
    private static final String XGS = "新公司";
    private static final String SHC = "上海仓";
    private static final String O2O = "O2O仓库";
    private static final String BEFORE = "计划到货日期（20点前）";
    private static final String AFTER = "计划到货日期（20点后）";
    private static final String ALL_CITY = "所有";
    private static final String OTHER_CITY = "其他";

    @Autowired
    @Qualifier(value = "erpDataSource")
    private DataSource dataSource;

    @Autowired
    private InfoService infoService;

    @Autowired
    private TimeLimitService timeLimitService;

    @Autowired
    private FormulaService formulaService;

    @Autowired
    private ErpSyncLogService erpSyncLogService;

    /**
     * 获取ERP中间表数据
     */
    public void execute() {
        long start = System.currentTimeMillis();
        final Formula before = formulaService.queryOne(new Formula(BEFORE));
        final Formula after = formulaService.queryOne(new Formula(AFTER));
        final Map<String, TimeLimit> map = new HashMap<>();
        List<TimeLimit> timeLimits = timeLimitService.queryAll();
        for (TimeLimit timeLimit : timeLimits) {
            String warehouse = timeLimit.getWarehouse();
            String province = timeLimit.getProvince();
            String city = timeLimit.getCity();
            map.put(StringUtils.join(warehouse, province, city), timeLimit);
        }
        //总数
        int count = 0;
        Connection connection = null;
        PreparedStatement countStatement = null;
        ResultSet rsc = null;
        try {
            connection = dataSource.getConnection();
            String countSql = "SELECT COUNT(*) FROM H5LNDS.TSMDATA";
            countStatement = connection.prepareStatement(countSql);
            rsc = countStatement.executeQuery();
            while (rsc.next()) {
                count = rsc.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("查询ERP中间表数据总数异常");
            throw new RuntimeException("查询ERP中间表数据总数异常", e);
        } finally {
            close(connection, countStatement, rsc);
        }
        LOGGER.info("查询ERP中间表总条数={}", count);
        //分页
        if (count > 0) {
            final String queryByPageSql = "SELECT * FROM (SELECT a.*,ROWNUM RN FROM (SELECT * FROM H5LNDS.TSMDATA) a  WHERE ROWNUM <=?)WHERE RN >=?";
            final int size = 500;
            int page = count % size == 0 ? count / size : count / size + 1;
            ExecutorService executorService = Executors.newFixedThreadPool(40);
            List<Future<List<Info>>> futures = new ArrayList<>();
            for (int i = 0; i < page; i++) {
                LOGGER.warn("第{}页加入Future", i);
                final int finalI = i;
                futures.add(executorService.submit(new Callable<List<Info>>() {
                    @Override
                    public List<Info> call() {
                        List<Info> list = new ArrayList<>();
                        Connection con = null;
                        PreparedStatement psp = null;
                        ResultSet resultSet = null;
                        try {
                            con = dataSource.getConnection();
                            psp = con.prepareStatement(queryByPageSql);
                            psp.setObject(1, size * (finalI + 1));
                            psp.setObject(2, size * finalI + 1);
                            resultSet = psp.executeQuery();
                            Info info;
                            while (resultSet.next()) {
                                info = new Info();
                                info.setShipperCode(resultSet.getString("shipper_code"));
                                info.setLogisticCode(resultSet.getString("logistic_code"));
                                info.setOrderCode(resultSet.getString("order_code"));
                                info.setExtOrderCode(resultSet.getString("ext_order_code"));
                                info.setShopCode(resultSet.getString("shop_code"));
                                info.setShopName(resultSet.getString("shop_name"));
                                info.setStorage(resultSet.getString("storage"));
                                info.setStorageCode(resultSet.getString("storage_code"));
                                info.setShipmentsTime(new Date(resultSet.getTimestamp("shipments_time").getTime()));
                                info.setPayTime(resultSet.getTimestamp("pay_time") != null ? new Date(resultSet.getTimestamp("pay_time").getTime()) : null);
                                info.setQty(resultSet.getString("qty"));
                                info.setWeight(resultSet.getString("weight"));
                                info.setLinkman(resultSet.getString("linkman"));
                                info.setPhone(resultSet.getString("phone"));
                                info.setProvince(resultSet.getString("province"));
                                info.setCity(resultSet.getString("city"));
                                info.setDistrict(resultSet.getString("district"));
                                info.setAddr(resultSet.getString("addr"));
                                info.setCityScale(resultSet.getString("city_scale"));
                                info.setOrderState(resultSet.getString("order_state"));
                                info.setCod(resultSet.getString("cod"));
                                info.setFreight(resultSet.getString("freight"));
                                info.setBizType(resultSet.getString("biztype"));
                                String storage = info.getStorage();
                                String province = StringUtils.substring(info.getProvince(), 0, 2);
                                String city = StringUtils.substring(info.getCity(), 0, 2);
                                if (StringUtils.isNotBlank(storage) && StringUtils.isNotBlank(province) && !Objects.equals(storage, O2O)) {
                                    if (storage.length() > 3) {
                                        storage = StringUtils.substring(storage, 0, 3);
                                    }
                                    if (storage.equals(XGS)) {
                                        storage = SHC;
                                    }
                                    Integer vday = getValidityDay(info.getShipperCode(), map, storage, province, city);
                                    info.setValidityDay(vday);
                                    if (vday != null) {
                                        info.setPlanTime(getPlanTimeByFormula(info.getShipmentsTime(), vday, before, after));
                                    }
                                }
                                info.setState(StateType.DSC);
                                list.add(info);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            close(con, psp, resultSet);
                        }
                        return list;
                    }
                }));
            }
            //保存
            int saveCount = 0;
            //重复
            int repetitionCount = 0;
            List<Object> ids = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(futures)) {
                for (Future<List<Info>> future : futures) {
                    List<Info> result;
                    try {
                        result = future.get();
                    } catch (Exception e) {
                        if (CollectionUtils.isNotEmpty(ids)) {
                            infoService.deleteTryByIds(ids);
                        }
                        LOGGER.error("查询ERP中间表数据异常", e);
                        throw new RuntimeException("查询ERP中间表数据异常", e);
                    }
                    if (CollectionUtils.isNotEmpty(result)) {
                        int aSize = result.size();
                        try {
                            Iterator<Info> iterator = result.iterator();
                            while (iterator.hasNext()) {
                                Info next = iterator.next();
                                String shipperCode = next.getShipperCode();
                                String logisticCode = next.getLogisticCode();
                                String orderCode = next.getOrderCode();
                                if (infoService.queryExist(shipperCode, logisticCode, orderCode)) {
                                    LOGGER.warn("本地库已存在ShipperCode={},LogisticCode={},OrderCode={}", shipperCode, logisticCode, orderCode);
                                    iterator.remove();
                                    repetitionCount++;
                                }
                            }
                            infoService.saveBaseInfo(result);
                            saveCount += result.size();
                            for (Info info : result) {
                                ids.add(info.getInfoId());
                            }
                        } catch (Exception e) {
                            if (CollectionUtils.isNotEmpty(ids)) {
                                infoService.deleteTryByIds(ids);
                            }
                            throw new RuntimeException("ERP数据保存到本地数据库异常", e);
                        }
                        LOGGER.warn("{}ERP数据保存到本地数据库成功,ERP获取数={},其中与本地重复数={},实际保存数={}", Thread.currentThread().getName(), aSize, (aSize - result.size()), result.size());
                    }
                    result = null;
                }

            }

            executorService.shutdown();

            Connection conn = null;
            PreparedStatement statement = null;
            int deleteCount = 0;
            try {
                String deleteSql = "DELETE FROM H5LNDS.TSMDATA";
                conn = dataSource.getConnection();
                statement = conn.prepareStatement(deleteSql);
                deleteCount = statement.executeUpdate(deleteSql);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(conn, statement, null);
            }

            ErpSyncLog erpSyncLog = new ErpSyncLog();
            erpSyncLog.setdSize(deleteCount);
            erpSyncLog.setrSize(repetitionCount);
            erpSyncLog.setSize(saveCount);
            long end = System.currentTimeMillis();
            erpSyncLog.setTimeConsum(end - start);
            erpSyncLogService.save(erpSyncLog);
            LOGGER.warn("从ERP中间表获取数据耗时{}", (end - start));
        } else {
            ErpSyncLog erpSyncLog = new ErpSyncLog();
            erpSyncLog.setdSize(0);
            erpSyncLog.setrSize(0);
            erpSyncLog.setSize(0);
            long end = System.currentTimeMillis();
            erpSyncLog.setTimeConsum(end - start);
            erpSyncLogService.save(erpSyncLog);
            LOGGER.warn("从ERP中间表获取数据耗时{}", (end - start));
        }
    }

    /**
     * fel根据公式获取计划日期
     *
     * @param shipmentsTime 发货日期
     * @param vday          时效
     * @param before        当天发货表达式
     * @param after         之后发货表达式
     * @return 计划日期
     */
    private Date getPlanTimeByFormula(Date shipmentsTime, Integer vday, Formula before, Formula after) {
        DateTime dateTime = new DateTime(shipmentsTime);
        if (dateTime.getHourOfDay() < 20) {
            return formulaService.analysisDayExpression(shipmentsTime, vday, before.getExpression());
        }
        if (dateTime.getHourOfDay() >= 20) {
            return formulaService.analysisDayExpression(shipmentsTime, vday, after.getExpression());
        }
        return null;
    }

    /**
     * 获取时限
     *
     * @param shipperCode  快递公司编号
     * @param timeLimitMap 时限map
     * @param storage      发货仓库
     * @param province     目的省
     * @param city         目的市
     * @return 时限
     */
    private Integer getValidityDay(String shipperCode, Map<String, TimeLimit> timeLimitMap, String storage, String province, String city) {
        TimeLimit tc = timeLimitMap.get(StringUtils.join(storage, province, city));
        TimeLimit tla = timeLimitMap.get(StringUtils.join(storage, province, ALL_CITY));
        TimeLimit tlo = timeLimitMap.get(StringUtils.join(storage, province, OTHER_CITY));
        Integer vday = null;
        if (tc != null) {
            vday = choice(shipperCode, tc);
        }
        if (tla != null) {
            vday = choice(shipperCode, tla);
        }
        if (tlo != null) {
            vday = choice(shipperCode, tlo);
        }
        return vday;
    }

    /**
     * 根据快递公司编号选择时效
     *
     * @param shipperCode 快递编号
     * @param timeLimit   时限
     * @return Integer
     */
    private Integer choice(String shipperCode, TimeLimit timeLimit) {
        Integer day = null;
        switch (shipperCode) {
            case "10097":
                day = timeLimit.getYt();
                break;
            case "10090":
                day = timeLimit.getSte();
                break;
            case "12402":
                day = timeLimit.getHt();
                break;
            case "10092":
                day = timeLimit.getZt();
                break;
            case "12397":
                day = timeLimit.getYd();
                break;
            case "10091":
                day = timeLimit.getSf();
                break;
            case "10093":
                day = timeLimit.getEms();
                break;
            case "13344":
                day = timeLimit.getJd();
                break;
            case "13343":
                day = timeLimit.getJdcod();
                break;
        }
        return day;
    }

    /**
     * 主动关闭
     *
     * @param connection     Connection
     * @param countStatement countStatement
     * @param rsc            ResultSet
     */
    private void close(Connection connection, PreparedStatement countStatement, ResultSet rsc) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (countStatement != null) {
                countStatement.close();
            }
            if (rsc != null) {
                rsc.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
