package com.ln.tms.service;

import com.ln.tms.bean.CountData;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mapper.CountRateMapper;
import com.ln.tms.pojo.*;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.POIExcelUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * TimeRateService - 快递时效Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class CountRateService extends BaseService<CountRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountRateService.class);

    /**
     * 文件后缀
     */
    private static final String CSV_SF = ".xlsx";

    /**
     * 文件存放磁盘
     */
    @Value("${file.disk.path}")
    private String fileDiskPath;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Autowired
    private CountRateMapper countRateMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private TimeLimitService timeLimitService;

    @Autowired
    private LogService logService;

    @Autowired
    private FileupService fileupService;

    /**
     * 统计快递的揽收送达率
     */
    @Transactional
    public void getTimeRate() {
        String beforDays = DateUtils.getBeforDays();
        //根据日期获取待揽集合
        List<Info> infoTookList = countRateMapper.queryInfoTookList(beforDays);

        List<CountRate> result = new ArrayList<CountRate>();
        //获取所有的省份
        List<String> provinces = timeLimitService.queryProvince();
        //获取所有仓库下快递公司
        List<StorageCourier> storageCouriers = storageService.getAllStorageCourier(new InfoWhere());
        for (StorageCourier sc : storageCouriers) {
            final String storageCode = sc.getStorageCode();
            final String shipperCode = sc.getShipperCode();
            List<Info> infos = selectTotal(infoTookList, shipperCode, storageCode);
            int tookTotal = CollectionUtils.isNotEmpty(infos) ? infos.size() : 0;
            if (tookTotal > 0) {
                int tookReal = selectTookReal(infos, shipperCode, storageCode, beforDays);
                result.add(getCountRate(beforDays, storageCode, shipperCode, null, tookTotal, tookReal, "0", null));
                LOGGER.warn("日期={},统计{}仓库下{}待揽总数={},实际揽收数量={}", beforDays, sc.getStorageName(), sc.getCourierName(), tookTotal, tookReal);
            }
        }

        if (CollectionUtils.isNotEmpty(result)) {
            Integer saveCount = countRateMapper.saveCountRateBatch(result);
            LOGGER.warn("日期={},批量统计出数据={}条", beforDays, saveCount);
        }
    }

    /**
     * 手动触发统计快递的揽收及时率
     *
     * @param date 统计的日期
     */
    @Transactional
    public void getTimeRate(String date) {
        try {
            //根据日期获取待揽集合
            List<Info> infoTookList = countRateMapper.queryInfoTookList(date);

            List<CountRate> result = new ArrayList<CountRate>();
            //获取所有的省份
            List<String> provinces = timeLimitService.queryProvince();
            //获取所有仓库下快递公司
            List<StorageCourier> storageCouriers = storageService.getAllStorageCourier(new InfoWhere());
            for (StorageCourier sc : storageCouriers) {
                final String storageCode = sc.getStorageCode();
                final String shipperCode = sc.getShipperCode();
                List<Info> infos = selectTotal(infoTookList, shipperCode, storageCode);
                int tookTotal = CollectionUtils.isNotEmpty(infos) ? infos.size() : 0;
                if (tookTotal > 0) {
                    int tookReal = selectTookReal(infos, shipperCode, storageCode, date);
                    result.add(getCountRate(date, storageCode, shipperCode, null, tookTotal, tookReal, "0", null));
                }
            }

            if (CollectionUtils.isNotEmpty(result)) {
                Integer delCount = countRateMapper.clearCountRateByDate(date);
                LOGGER.warn("日期={},删除这天统计数据={}条", date, delCount);
                Integer saveCount = countRateMapper.saveCountRateBatch(result);
                LOGGER.warn("日期={},批量统计出数据={}条", date, saveCount);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 集合中挑选出仓库，快递公司的总集合
     *
     * @param lists       总集合
     * @param shipperCode 快递编号
     * @param storageCode 仓库编号
     * @return int
     */
    private List<Info> selectTotal(List<Info> lists, final String shipperCode, final String storageCode) {
        List<Info> selects = null;
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        selects = (List<Info>) CollectionUtils.select(lists, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                Info info = (Info) o;
                return StringUtils.equals(info.getStorageCode(), storageCode) && StringUtils.equals(info.getShipperCode(), shipperCode);
            }
        });
        return selects;
    }

    /**
     * 集合中查询实际揽件的数量
     *
     * @param lists       总集合
     * @param shipperCode 快递编号
     * @param storageCode 仓库编号
     * @return int
     */
    private int selectTookReal(List<Info> lists, final String shipperCode, final String storageCode, String beforDays) {
        if (CollectionUtils.isEmpty(lists)) {
            return 0;
        }
        Collection selects = null;
        final Date endDays = DateUtils.stringToDate(beforDays + " 23:59:59");
        selects = CollectionUtils.select(lists, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                Info info = (Info) o;
                return StringUtils.equals(info.getStorageCode(), storageCode) && StringUtils.equals(info.getShipperCode(), shipperCode)
                        && info.getState().code() != 0 && DateUtils.compareToDate(endDays, info.getTookTime());
            }
        });
        return CollectionUtils.isNotEmpty(selects) ? selects.size() : 0;
    }

    /**
     * 汇率表设置值
     *
     * @param beforDays   统计日期
     * @param storageCode 仓库编码
     * @param shipperCode 快递编码
     * @param shouldNum   总数量
     * @param realNum     实际数量
     * @param countType   统计类型
     * @return CountRate
     */
    private CountRate getCountRate(String beforDays, String storageCode, String shipperCode, String province, int shouldNum, int realNum, String countType, Integer attritDay) {
        CountRate countRate = new CountRate();
        countRate.setCountDate(DateUtils.strToDay(beforDays));
        countRate.setShipperCode(shipperCode);
        countRate.setStorageCode(storageCode);
        countRate.setProvince(province);
        countRate.setShouldNum(shouldNum);
        countRate.setRealNum(realNum);
        DecimalFormat df = new DecimalFormat("#.00");
        double rate = (double) realNum / shouldNum * 100;
        countRate.setSentRate(Double.parseDouble(df.format(rate)));
        countRate.setAttritDay(attritDay);
        countRate.setCountType(countType);
        return countRate;
    }

    /**
     * 根据用户仓库权限查询快递的揽收及时率
     *
     * @param countRate 查询条件
     * @return List
     */
    @Transactional(readOnly = true)
    public List<CountRate> queryList(CountRate countRate) {
        return countRateMapper.queryList(countRate);
    }

    /**
     * 开始计算平均天数
     *
     * @param countData 查询条件
     * @return boolean
     */
    @Transactional
    public boolean startAvgDay(final CountData countData, final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer suf = new StringBuffer();
                try {
                    List<Info> infoList = countRateMapper.queryInfoAll(countData);
                    if (CollectionUtils.isNotEmpty(infoList)) {
                        //获取导出的集合
                        List<CountData> lists = new ArrayList<CountData>();
                        List<String> provinces = timeLimitService.queryProvince();
                        if (CollectionUtils.isEmpty(provinces)) {
                            throw new NullPointerException("查询出的省份为空");
                        }
                        for (String province : provinces) {
                            List<Info> infos = selectSignTotal(infoList, province);
                            if (CollectionUtils.isNotEmpty(infos)) {
                                Integer totalDay = 0;
                                Integer realNum = 0;
                                Integer oneDay = 0;
                                Integer twoDay = 0;
                                Integer threeDay = 0;
                                Integer gtThreeDay = 0;
                                for (Info info : infos) {
                                    if (info.getAttritDay() != null) {
                                        totalDay += info.getAttritDay();
                                        if (info.getState().code() == 3) {
                                            realNum++;
                                            if (info.getAttritDay() == 1) {
                                                oneDay++;
                                            } else if (info.getAttritDay() == 2) {
                                                twoDay++;
                                            } else if (info.getAttritDay() == 3) {
                                                threeDay++;
                                            } else if (info.getAttritDay() > 3) {
                                                gtThreeDay++;
                                            }
                                        }
                                    }
                                }
                                CountData cd = new CountData();
                                cd.setProvince(province);
                                cd.setOneDaySignNum(oneDay);
                                cd.setTwoDaySignNum(twoDay);
                                cd.setThreeDaySignNum(threeDay);
                                cd.setGtThreeDaySignNum(gtThreeDay);
                                cd.setTotal(realNum);
                                cd.setTotalDay(totalDay);
                                cd.setAvgDay(calAvg(totalDay, realNum));
                                lists.add(cd);
                            }
                        }
                        String fileName = exportAvg(lists, user, countData);
                        suf.append("导出成功，fileUrl=");
                        suf.append(fileName);
                    } else {
                        suf.append("发货时间内没有查询到数据");
                    }
                } catch (Exception e) {
                    suf.append("导出失败，失败原因:");
                    suf.append(e.getMessage());
                    e.printStackTrace();
                } finally {
                    //导入成功后记录操作日志
                    Log log = new Log();
                    log.setOperation("平均天数导出");
                    log.setUserId(user.getUserId());
                    log.setIp(user.getIp());
                    log.setParameter(StringUtils.join("仓库编号：", countData.getStorageCode(), ",快递编号：", countData.getShipperCode(), "发货时间：", countData.getShipmentsStartDate(), "到", countData.getShipmentsEndDate()));
                    log.setContent(suf.toString());
                    logService.save(log);
                }
            }
        }).start();
        return true;
    }

    /**
     * 集合中挑选出到省的集合
     *
     * @param lists    总集合
     * @param province 到达的省
     * @return List
     */
    private List<Info> selectSignTotal(List<Info> lists, final String province) {
        List<Info> selects = null;
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        selects = (List<Info>) CollectionUtils.select(lists, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                Info info = (Info) o;
                return StringUtils.contains(info.getProvince(), province);
            }
        });
        return selects;
    }

    /**
     * 计算比率(平均数，精确到1位小数)
     *
     * @param total 总数
     * @param real  实际数
     * @return double
     */
    private double calAvg(Integer total, Integer real) {
        if (total != null && total > 0 && real != null && real > 0) {
            DecimalFormat df = new DecimalFormat("#.0");
            double rate = (double) total / real;
            return Double.parseDouble(df.format(rate));
        } else {
            return 0;
        }
    }

    /**
     * 导出统计的数据(平均送货天数)
     *
     * @param countDatas 导出的集合
     * @param countData  查询条件
     * @param user       操作用户
     * @return String 文件的名称
     */
    @Transactional
    public String exportAvg(List<CountData> countDatas, User user, CountData countData) {
        String fileName = "";
        try {
            String shettName = "";
            StringBuffer suf = new StringBuffer();
            //获取导出的名称
            InfoWhere infoWhere = new InfoWhere();
            infoWhere.setStorageCode(countData.getStorageCode());
            infoWhere.setShipperCode(countData.getShipperCode());
            List<StorageCourier> storageCourier = storageService.getAllStorageCourier(infoWhere);
            if (CollectionUtils.isNotEmpty(storageCourier)) {
                shettName = storageCourier.get(0).getStorageName();
                suf.append(shettName);
                for (StorageCourier courier : storageCourier) {
                    suf.append(courier.getCourierName());
                    suf.append("、");
                }
                suf.append("送货天数统计");
            } else {
                throw new RuntimeException("没有查询出对应的仓库快递");
            }

            String title = StringUtils.join(countData.getShipmentsStartDate(), "到", countData.getShipmentsEndDate(), suf.toString());
            HSSFWorkbook workbook1 = POIExcelUtils.makeExcelHead(title, 7, 20);
            String[] secondTitles = {"省份", "一天", "两天", "三天", "大于三天", "合计(总笔数)", "总天数", "平均送货天数"};
            HSSFWorkbook workbook2 = POIExcelUtils.makeSecondHead(workbook1, secondTitles, StringUtils.join(shettName, "送货天数统计"));

            String[] beanProperty = {"province", "oneDaySignNum", "twoDaySignNum", "threeDaySignNum", "gtThreeDaySignNum", "total", "totalDay", "avgDay"};
            HSSFWorkbook workbook = POIExcelUtils.exportExcelData(workbook2, countDatas, beanProperty);

            fileName = StringUtils.join(fileDiskPath, shettName, DateUtils.getDateformat(new Date(), "yyyyMMddHHmmss"), CSV_SF);
            FileOutputStream os = new FileOutputStream(fileName);
            workbook.write(os);
            os.close();

            fileName = StringUtils.isNotBlank(fileName) ? StringUtils.replace(StringUtils.substringAfter(fileName, fileDiskPath), "\\", "/") : null;
            //导入成功后记录操作日志
            Fileup fileup = new Fileup();
            fileup.setFileName(fileName);
            fileup.setFileUrl(fileServerUrl + fileName);
            fileup.setBelongTo("3-0");
            fileup.setIp(user.getIp());
            fileup.setUserId(user.getUserId());
            fileupService.saveFileup(fileup);
            LOGGER.info("export End fileUrl={}", fileName);
        } catch (Exception e) {
            //删除文件
            File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new NullPointerException("Folder Delete failed");

                }
            }
            throw new RuntimeException(e.getMessage(), e);
        }
        return fileName;
    }

    /**
     * 计算比率(百分比，精确到2位小数)
     *
     * @param total 总数
     * @param real  实际数
     * @return double
     */
    private double calRate(Integer total, Integer real) {
        if (total != null && total > 0) {
            DecimalFormat df = new DecimalFormat("#.00");
            double rate = (double) real / total * 100;
            return Double.parseDouble(df.format(rate));
        } else {
            return 0;
        }
    }

    /**
     * 开始综合分析
     *
     * @param countData 查询条件
     * @return boolean
     */
    @Transactional
    public boolean startAllCal(final CountData countData, final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer suf = new StringBuffer();
                try {
                    List<Info> infoList = countRateMapper.queryInfoAll(countData);
                    if (CollectionUtils.isNotEmpty(infoList)) {
                        //获取导出的集合
                        List<CountData> lists = new ArrayList<CountData>();

                        InfoWhere infoWhere = new InfoWhere();
                        infoWhere.setStorageCode(countData.getStorageCode());
                        List<StorageCourier> storageCouriers = storageService.getAllStorageCourier(infoWhere);
                        if (CollectionUtils.isEmpty(storageCouriers)) {
                            throw new NullPointerException("查询出的仓库快递为空");
                        }
                        for (StorageCourier sc : storageCouriers) {
                            List<Info> infos = selectTotal(infoList, sc.getShipperCode(), sc.getStorageCode());
                            if (CollectionUtils.isNotEmpty(infos)) {
                                Integer deliverNum = 0;
                                Integer threeDayNum = 0;
                                Integer inTimeNum = 0;
                                for (Info info : infos) {
                                    if (info.getState().code() == 3) {
                                        deliverNum++;
                                        if (info.getAttritDay() != null) {
                                            if (info.getAttritDay() <= 3) {
                                                threeDayNum++;
                                            }
                                            if (DateUtils.compareToDate(info.getPlanTime(), info.getSignTime())) {
                                                inTimeNum++;
                                            }
                                        }
                                    }
                                }
                                CountData cd = new CountData();
                                cd.setStorageCode(sc.getStorageCode());
                                cd.setShipperCode(sc.getShipperCode());
                                cd.setStorageName(sc.getStorageName());
                                cd.setCourierName(sc.getCourierName());
                                cd.setTotal(infos.size());
                                cd.setDeliverNum(deliverNum);
                                cd.setThreeDayNum(threeDayNum);
                                cd.setInTimeNum(inTimeNum);
                                cd.setDeliverRate(calRate(cd.getTotal(), deliverNum));
                                cd.setThreeDayRate(calRate(cd.getTotal(), threeDayNum));
                                cd.setInTimeRate(calRate(cd.getTotal(), inTimeNum));
                                lists.add(cd);
                            }
                        }
                        List<CountData> tookRates = countRateMapper.queryTookRate(countData);
                        if (CollectionUtils.isNotEmpty(tookRates)) {
                            for (CountData cdata : lists) {
                                for (CountData tookRate : tookRates) {
                                    if (cdata.getStorageCode().equals(tookRate.getStorageCode()) &&
                                            cdata.getShipperCode().equals(tookRate.getShipperCode())) {
                                        cdata.setTookNum(tookRate.getTookNum());
                                        cdata.setTookTotal(tookRate.getTookTotal());
                                        cdata.setTookRate(calRate(cdata.getTookTotal(), cdata.getTookNum()));
                                    }
                                }
                            }
                        }
                        String fileName = exportAll(lists, user, countData);
                        suf.append("导出成功，fileUrl=");
                        suf.append(fileName);
                    } else {
                        suf.append("发货时间内没有查询到数据");
                    }
                } catch (Exception e) {
                    suf.append("导出失败，失败原因:");
                    suf.append(e.getMessage());
                    e.printStackTrace();
                } finally {
                    //导入成功后记录操作日志
                    Log log = new Log();
                    log.setOperation("快递综合分析导出");
                    log.setUserId(user.getUserId());
                    log.setIp(user.getIp());
                    log.setParameter(StringUtils.join("仓库编号：", countData.getStorageCode(), ",快递编号：", countData.getShipperCode(), "发货时间：", countData.getShipmentsStartDate(), "到", countData.getShipmentsEndDate()));
                    log.setContent(suf.toString());
                    logService.save(log);
                }
            }
        }).start();
        return true;
    }

    /**
     * 导出统计的数据(揽收率，妥投率，三天到达率，及时率)
     *
     * @param countDatas 导出的集合
     * @param countData  查询条件
     * @param user       操作用户
     * @return String 文件的名称
     */
    @Transactional
    public String exportAll(List<CountData> countDatas, User user, CountData countData) {

        String fileName = "";
        String sheetName = "快递综合分析";
        try {
            StringBuffer suf = new StringBuffer();
            String title = StringUtils.join(countData.getShipmentsStartDate(), "到", countData.getShipmentsEndDate(), sheetName);
            suf.append(title.toString());

            HSSFWorkbook workbook1 = POIExcelUtils.makeExcelHead(title, 11, 15);
            String[] secondTitles = {"仓库", "快递公司", "总笔数", "应揽收总笔数", "及时揽件笔数", "妥投笔数", "三天送达笔数", "及时送达笔数", "揽件及时率", "妥投率", "三天送达率", "及时率"};
            HSSFWorkbook workbook2 = POIExcelUtils.makeSecondHead(workbook1, secondTitles, sheetName);

            String[] beanProperty = {"storageName", "courierName", "total", "tookTotal", "tookNum", "deliverNum", "threeDayNum", "inTimeNum", "tookRate", "deliverRate", "threeDayRate", "inTimeRate"};
            HSSFWorkbook workbook = POIExcelUtils.exportExcelData(workbook2, countDatas, beanProperty);

            fileName = StringUtils.join(fileDiskPath, sheetName, DateUtils.getDateformat(new Date(), "yyyyMMddHHmmss"), CSV_SF);
            FileOutputStream os = new FileOutputStream(fileName);
            workbook.write(os);
            os.close();

            fileName = StringUtils.isNotBlank(fileName) ? StringUtils.replace(StringUtils.substringAfter(fileName, fileDiskPath), "\\", "/") : null;
            //导入成功后记录操作日志
            Fileup fileup = new Fileup();
            fileup.setFileName(fileName);
            fileup.setFileUrl(fileServerUrl + fileName);
            fileup.setBelongTo("4-0");
            fileup.setIp(user.getIp());
            fileup.setUserId(user.getUserId());
            fileupService.saveFileup(fileup);
            LOGGER.info("export End fileUrl={}", fileName);
        } catch (Exception e) {
            //删除文件
            File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new NullPointerException("Folder Delete failed");

                }
            }
            throw new RuntimeException(e.getMessage(), e);
        }
        return fileName;
    }

}