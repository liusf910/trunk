package com.ln.tms.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Info - 快递信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SolrDocument(solrCoreName="collection1")
@Table(name = "tms_info")
public class Info extends BaseBean {

    private static final long serialVersionUID = 8318866924186606109L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Field
    /**ID*/
    private Long infoId;

    /**
     * 外键ID快递公司编号
     */
    @Field
    private String shipperCode;

    /**
     * 快递单号
     */
    @Field
    private String logisticCode;

    /**
     * 订单号
     */
    @Field
    private String orderCode;

    /**
     * 外部单号
     */
    @Field
    private String extOrderCode;

    /**
     * 店铺代码
     */
    @Field
    private String shopCode;

    /**
     * 店铺名称
     */
    @Field
    private String shopName;

    /**
     * 发货仓库
     */
    @Field
    private String storage;

    /**
     * 发货仓库代码
     */
    @Field
    private String storageCode;

    /**
     * 发货时间
     */
    @Field
    private Date shipmentsTime;

    /**
     * 数量
     */
    @Field
    private String qty;

    /**
     * 重量
     */
    @Field
    private String weight;

    /**
     * 收件联系人
     */
    @Field
    private String linkman;

    /**
     * 收件人联系电话
     */
    @Field
    private String phone;

    /**
     * 收件人省
     */
    @Field
    private String province;

    /**
     * 收件人市
     */
    @Field
    private String city;

    /**
     * 收件人区县
     */
    @Field
    private String district;

    /**
     * 收件人详细地址
     */
    @Field
    private String addr;

    /**
     * 收件人城市分级
     */
    @Field
    private String cityScale;

    /**
     * 订单状态
     */
    @Field
    private String orderState;

    /**
     * 是否货到付款
     */
    @Field
    private String cod;

    /**
     * 代收运费
     */
    @Field
    private String freight;

    /**
     * 快递公司时限
     */
    @Field
    private Integer validityDay;

    /**
     * 计划到货日期
     */
    @Field
    private Date planTime;

    /**
     * 快递当前所在地
     */
    @Field
    private String location;

    /**
     * 物流状态(0-订单生成，1-已揽收，2-在途中，201-到达派件城市，202-派件中，211-已放入快递柜或驿站，3-已签收，311-已取出快递柜或驿站，4-问题件，401-发货无信息，402-超时未签收，403-超时未更新，404-拒收（退件），412-快递柜或驿站超时未取)
     */
    
    private StateType state;
    @Field
    @Transient
    private String enumState;
    
    public String getEnumState() {
		return enumState;
	}

	public void setEnumState(String enum_state) {
		this.enumState = state.toString();
	}

	/**
     * 最后一条轨迹更新时间
     */
    @Field
    private Date lastTime;

    /**
     * 快递员揽件时间(来自跟踪信息)
     */
    @Field
    private Date tookTime;

    /**
     * 收件人签收时间(来自跟踪信息)
     */
    @Field
    private Date signTime;

    /**
     * 签收人
     */
    @Field
    private String signUser;

    /**
     * 超时天数
     */
    @Field
    private Integer overDay;

    /**
     * 时效（实际耗时）
     */
    @Field
    private Long attritTime;

    /**
     * 到货天数（在途天数）
     */
    @Field
    private Integer attritDay;

    /**
     * 付款时间
     */
    @Field
    private Date payTime;

    /**
     * 换货标识 change换货
     */
    @Field
    private String bizType;


    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getExtOrderCode() {
        return extOrderCode;
    }

    public void setExtOrderCode(String extOrderCode) {
        this.extOrderCode = extOrderCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public Date getShipmentsTime() {
        return shipmentsTime;
    }

    public void setShipmentsTime(Date shipmentsTime) {
        this.shipmentsTime = shipmentsTime;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCityScale() {
        return cityScale;
    }

    public void setCityScale(String cityScale) {
        this.cityScale = cityScale;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public Integer getValidityDay() {
        return validityDay;
    }

    public void setValidityDay(Integer validityDay) {
        this.validityDay = validityDay;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StateType getState() {
    	if(null!=this.getEnumState() && !"".equals(this.getEnumState()))
        return StateType.codeOf(Integer.parseInt(this.getEnumState()));
    	return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getTookTime() {
        return tookTime;
    }

    public void setTookTime(Date tookTime) {
        this.tookTime = tookTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignUser() {
        return signUser;
    }

    public void setSignUser(String signUser) {
        this.signUser = signUser;
    }

    public Integer getOverDay() {
        return overDay;
    }

    public void setOverDay(Integer overDay) {
        this.overDay = overDay;
    }

    public Long getAttritTime() {
        return attritTime;
    }

    public void setAttritTime(Long attritTime) {
        this.attritTime = attritTime;
    }

    public Integer getAttritDay() {
        return attritDay;
    }

    public void setAttritDay(Integer attritDay) {
        this.attritDay = attritDay;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /****************************
     *         扩展字段          *
     ****************************/
    /**
     * 派件城市
     */
    @Field
    @Transient
    private String pjCity;
    /**
     * 轨迹信息json格式
     */
    @Field
    @JsonIgnore
    @Transient
    private String infoTrace;
    /**
     * 揽件时间(快递公司文件)
     */
    @Transient
    private Date fileTookTime;
    /**
     * 签收时间(快递公司文件)
     */
    @Field
    @Transient
    private Date fileSignTime;
    /**
     * 签收超时原因
     */
    @Field
    @Transient
    private String signReason;

    /**
     * 签收超时原因List
     */
    @Field
    @Transient
    private List<SignOverVo> signReasonList;

    /**
     * 历史签收超时原因
     */
    @Field
    @Transient
    private String signStr;

    /**
     * 揽收超时原因
     */
    @Field
    @Transient
    private String tookReason;

    /**
     * 配送状态
     */
    @Transient
    private String orderStatus;
    /**
     * 是否超时
     */
    @Field
    @Transient
    private String timeOut;

    /**
     * 轨迹信息json格式反序列化成list
     */
    @Field
    @Transient
    private List<Trace> traces;

    /**
     * 系统当前时间
     */
    @Field
    @Transient
    private Date systemTime;

    /**
     * 判别列
     */
    @Field
    @Transient
    private String distColumn;

    /**
     * 快递公司名
     */
    @Field
    @Transient
    private String courierName;


    /**
     * 待揽超时天数
     */
    @Field
    @Transient
    private int dlsOver;

    /**
     * 待签超时天数
     */
    @Field
    @Transient
    private int dqOver;


    public int getDlsOver() throws ParseException {
    	
     String hour = new SimpleDateFormat("HH").format(shipmentsTime);
     String ss = new SimpleDateFormat("ss").format(shipmentsTime);
     if (Integer.parseInt(hour) < 20 || (Integer.parseInt(hour)==20 && Integer.parseInt(ss)==0)) {
            return DateUtils.getDateDifference(shipmentsTime, new Date());
        } else {
            return DateUtils.getDateDifference(shipmentsTime, new Date()) - 1;
        }
    }

    public void setDlsOver(int dlsOver) {
        this.dlsOver = dlsOver;
    }

    public int getDqOver() {
        if (null != DateUtils.getDateDifference(planTime, new Date())) {
            return DateUtils.getDateDifference(planTime, new Date());
        }
        return dqOver;
    }

    public void setDqOver(int dqOver) {
        this.dqOver = dqOver;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getPjCity() {
        return pjCity;
    }

    public void setPjCity(String pjCity) {
        this.pjCity = pjCity;
    }

    public String getInfoTrace() {
        return infoTrace;
    }

    public void setInfoTrace(String infoTrace) {
        this.infoTrace = infoTrace;
    }

    public Date getFileTookTime() {
        return fileTookTime;
    }

    public void setFileTookTime(Date fileTookTime) {
        this.fileTookTime = fileTookTime;
    }

    public Date getFileSignTime() {
        return fileSignTime;
    }

    public void setFileSignTime(Date fileSignTime) {
        this.fileSignTime = fileSignTime;
    }

    public String getSignReason() {
        if (StringUtils.isNotBlank(signReason)) {
            return (SignType.codeOf(Integer.parseInt(signReason))).getDesc();
        }
        return signReason;
    }

    public void setSignReason(String signReason) {
        this.signReason = signReason;
    }

    public String getTookReason() {
        if (StringUtils.isNotBlank(tookReason)) {
            return (TookType.codeOf(Integer.parseInt(tookReason))).getDesc();
        }
        return tookReason;
    }

    public void setTookReason(String tookReason) {
        this.tookReason = tookReason;
    }

    public String getOrderStatus() {
        if (StringUtils.isNotBlank(orderStatus)) {
            return OrderType.codeOf(Integer.parseInt(orderStatus)).getDesc();
        }
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @SuppressWarnings("unchecked")
    public List<Trace> getTraces() {
        return StringUtils.isNotBlank(this.getInfoTrace()) ? (List<Trace>) JsonUtils.toObject(this.getInfoTrace(), new TypeReference<List<Trace>>() {
        }) : null;
    }

    public Date getSystemTime() {
        return new Date();
    }

    public String getDistColumn() {
        if (signTime != null && fileSignTime != null) {
            if (DateUtils.getDateHours(signTime, fileSignTime) < 24) {
                return "fileSignTime";
            }
        }
        if (signTime == null) {
            return "fileSignTime";
        } else {
            return "signTime";
        }
    }


    public String getSignStr() {
        return signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }

    public List<SignOverVo> getSignReasonList() {
        List<SignOverVo> signReasonList = new ArrayList<SignOverVo>();
        if (StringUtils.isNotBlank(signStr)) {
            String[] signReasonArr = signStr.split("#");
            for (int i = signReasonArr.length - 1; i >= 0; i--) {
                String[] signOverVoArr = signReasonArr[i].split("&");
                SignOverVo soVo = new SignOverVo();
                soVo.setOrderStatus((OrderType.codeOf(Integer.parseInt(signOverVoArr[0]))).getDesc());
                soVo.setFileSignTime(signOverVoArr[1]);
                soVo.setSignReason((SignType.codeOf(Integer.parseInt(signOverVoArr[2]))).getDesc());
                signReasonList.add(soVo);
            }
        }
        return signReasonList;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }


}