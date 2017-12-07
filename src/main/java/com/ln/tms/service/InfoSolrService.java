package com.ln.tms.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.ln.tms.enums.StateType;
import com.ln.tms.pojo.Info;

@Service
public class InfoSolrService {
	@Resource
	private SolrClient solrClient;

	public PageInfo<Info> solrFind(Map<String, Object> map, Integer page, Integer rows) {
		SolrQuery query = new SolrQuery();// 查询
		query.setStart((page - 1) * rows + 1);
		query.setRows(rows);
		String queryStr = "";
		if (null != map.get("orderField") && null != map.get("orderDirection")) {
			query.setSort(map.get("orderField").toString(),
					"desc".equals(map.get("orderDirection").toString()) ? ORDER.desc : ORDER.asc);
		} else {
			query.setSort("shipmentsTime", ORDER.desc);
		}

		// 发货日期进行查询
		if (null != map.get("shipmentsTimeStart") && null != map.get("shipmentsTimeEnd")
				&& !map.get("shipmentsTimeStart").equals(map.get("shipmentsTimeEnd"))) {
			String queryKey = "ymdShipmentsTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("shipmentsTimeStart")) + " TO "
					+ getTimeYMD((Date) map.get("shipmentsTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("shipmentsTimeStart") && null != map.get("shipmentsTimeEnd")
				&& map.get("shipmentsTimeStart").equals(map.get("shipmentsTimeEnd"))) {
			String queryKey = "ymdShipmentsTime";
			String queryValue = getTimeYMD((Date) map.get("shipmentsTimeEnd"));
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null == map.get("shipmentsTimeStart") && null != map.get("shipmentsTimeEnd")) {
			String queryKey = "ymdShipmentsTime";
			String queryValue = "[* TO " + getTimeYMD((Date) map.get("shipmentsTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("shipmentsTimeStart") && null == map.get("shipmentsTimeEnd")) {
			String queryKey = "ymdShipmentsTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("shipmentsTimeStart")) + " TO *]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		}
		// 计划签收日期
		if (null != map.get("planTimeStart") && null != map.get("planTimeEnd")
				&& !map.get("planTimeStart").equals(map.get("planTimeEnd"))) {
			String queryKey = "newPlanTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("planTimeStart")) + " TO "
					+ getTimeYMD((Date) map.get("planTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("planTimeStart") && null != map.get("planTimeEnd")
				&& map.get("planTimeStart").equals(map.get("planTimeEnd"))) {
			String queryKey = "newPlanTime";
			String queryValue = getTimeYMD((Date) map.get("planTimeEnd"));
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null == map.get("planTimeStart") && null != map.get("planTimeEnd")) {
			String queryKey = "newPlanTime";
			String queryValue = "[* TO " + getTimeYMD((Date) map.get("planTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("planTimeStart") && null == map.get("planTimeEnd")) {
			String queryKey = "newPlanTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("planTimeStart")) + " TO *]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		}

		// 揽收日期
		if (null != map.get("tookTimeStart") && null != map.get("tookTimeEnd")
				&& !map.get("tookTimeStart").equals(map.get("tookTimeEnd"))) {
			String queryKey = "newTookTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("tookTimeStart")) + " TO "
					+ getTimeYMD((Date) map.get("tookTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("tookTimeStart") && null != map.get("tookTimeEnd")
				&& map.get("tookTimeStart").equals(map.get("tookTimeEnd"))) {
			String queryKey = "newTookTime";
			String queryValue = getTimeYMD((Date) map.get("tookTimeEnd"));
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null == map.get("tookTimeStart") && null != map.get("tookTimeEnd")) {
			String queryKey = "newTookTime";
			String queryValue = "[* TO " + getTimeYMD((Date) map.get("tookTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("tookTimeStart") && null == map.get("tookTimeEnd")) {
			String queryKey = "newTookTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("tookTimeStart")) + " TO *]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		}
		// 签收日期
		if (null != map.get("signTimeStart") && null != map.get("signTimeEnd")
				&& !map.get("signTimeStart").equals(map.get("signTimeEnd"))) {
			String queryKey = "newSignTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("signTimeStart")) + " TO "
					+ getTimeYMD((Date) map.get("signTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("signTimeStart") && null != map.get("signTimeEnd")
				&& map.get("signTimeStart").equals(map.get("signTimeEnd"))) {
			String queryKey = "newSignTime";
			String queryValue = getTimeYMD((Date) map.get("signTimeEnd"));
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null == map.get("signTimeStart") && null != map.get("signTimeEnd")) {
			String queryKey = "newSignTime";
			String queryValue = "[* TO " + getTimeYMD((Date) map.get("signTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("signTimeStart") && null == map.get("signTimeEnd")) {
			String queryKey = "newSignTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("signTimeStart")) + " TO *]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		}
		// 付款日期
		if (null != map.get("payTimeStart") && null != map.get("payTimeEnd")
				&& !map.get("payTimeStart").equals(map.get("payTimeEnd"))) {
			String queryKey = "newPayTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("payTimeStart")) + " TO "
					+ getTimeYMD((Date) map.get("payTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("payTimeStart") && null != map.get("payTimeEnd")
				&& map.get("payTimeStart").equals(map.get("payTimeEnd"))) {
			String queryKey = "newPayTime";
			String queryValue = getTimeYMD((Date) map.get("payTimeEnd"));
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null == map.get("payTimeStart") && null != map.get("payTimeEnd")) {
			String queryKey = "newPayTime";
			String queryValue = "[* TO " + getTimeYMD((Date) map.get("payTimeEnd")) + "]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		} else if (null != map.get("payTimeStart") && null == map.get("payTimeEnd")) {
			String queryKey = "newPayTime";
			String queryValue = "[" + getTimeYMD((Date) map.get("payTimeStart")) + " TO *]";
			queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
		}

		for (String key : map.keySet()) {
			String queryKey = "";
			Object queryValue = "";
			if (null != map.get(key) && !"false".equals(map.get(key).toString()) && !"".equals(map.get(key))) {
				if ("true".equals(map.get(key).toString())) {
					// 今日达
					Date currentTime = new Date();
					if ("signToday".equals(key)) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String dateString = formatter.format(currentTime);
						queryKey = "bzPlanTime";
						queryValue = "[" + dateString + "T00:00:00Z TO " + dateString + "T00:00:00Z]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
					} else if ("fkzlsFlag".equals(key)) {
						// 付款时间早于揽收时间48h
						queryKey = "fkzls";
						queryValue = "{0 TO *]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
					} else if ("psOver".equals(key)) {
						// 配送超时
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String dateString = formatter.format(currentTime);
						queryKey = "bzPlanTime";
						queryValue = "[* TO " + dateString + "T00:00:00Z}";
						query.addFilterQuery("-state:3");
						query.addFilterQuery("-state:4");
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
					} else if ("qsOver".equals(key)) {
						// 签收超时
						queryKey = "overDay";
						queryValue = "{0 TO *]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
					} else if ("tookReasonNull".equals(key)) {
						// 揽收超时原因为空
						query.addFilterQuery("-tookReason:*");
					} else if ("signReasonNull".equals(key)) {
						// 签收超时原因为空
						query.addFilterQuery("-signReason:*");
					} else if ("signFor".equals(key)) {
						// 待签超时几天
						query.addFilterQuery("-state:0");
						query.addFilterQuery("-state:3");
						if ("0".equals(map.get("dqOver"))) {
							queryKey = "newPlanTime";
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							String result = sdf1.format(currentTime);
							queryValue = "[" + result + " TO *]";
							queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
						} else if ("1".equals(map.get("dqOver"))) {
							queryKey = "newPlanTime";
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							String result = sdf1.format(currentTime);
							queryValue = "[* TO " + result + "]";
							queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
						} else if ("4".equals(map.get("dqOver"))) {
							String yesterday3 = getWantTime(3);
							queryKey = "newPlanTime";
							queryValue = "[* TO " + yesterday3 + "]";
							queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
						} else if ("3".equals(map.get("dqOver"))) {
							String yesterday2 = getWantTime(2);
							queryKey = "newPlanTime";
							queryValue = yesterday2;
							queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
						} else if ("2".equals(map.get("dqOver"))) {
							String yesterday1 = getWantTime(1);
							queryKey = "newPlanTime";
							queryValue = yesterday1;
							queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
						}
					} else if ("lsOver".equals(key)) {
						// 揽收超时
						queryStr = queryStr + " AND ((state:0";

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String days = sdf.format(currentTime);
						queryKey = "ymdShipmentsTime";
						queryValue = "[* TO " + days + "}";
						queryStr = queryStr + " AND (((" + queryKey + ":" + queryValue;
						queryValue = "{" + days + " TO *]";
						queryStr = queryStr + " OR " + queryKey + ":" + queryValue + ")";

						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.DATE, -1);
						Date date = calendar.getTime();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String tdays = df.format(date);
						queryKey = "ymdShipmentsTime";
						queryValue = "[* TO " + tdays + "}";
						queryStr = queryStr + ") AND " + queryKey + ":" + queryValue + ")) OR ";

						queryKey = "tookTime";
						queryValue = "*";
						queryStr = queryStr + "((" + queryKey + ":" + queryValue;

						queryKey = "sTimeJxTTime";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;
						queryValue = "{0 TO *]";
						queryStr = queryStr + " OR " + queryKey + ":" + queryValue + ")";

						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "tookTJqShipmentsT";
						queryValue = "{1 TO *]";
						queryStr = queryStr + ")) OR " + queryKey + ":" + queryValue + "))";
					}
				} else if ("dlsOver".equals(key)) {

					Date currentTime = new Date();
					// 待揽超时
					if ("0".equals(map.get("dlsOver"))) {
						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
						String result = sdf1.format(currentTime);
						queryValue = "[" + result + " TO *]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "xjShipmentsTime20";
						queryValue = "{0 TO *]";
						queryStr = queryStr + ") OR (" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = "[" + getWantTime(1) + " TO *]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue + "))";

					} else if ("1".equals(map.get("dlsOver"))) {
						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
						String result = sdf1.format(currentTime);
						queryValue = "[* TO " + result + "}";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "xjShipmentsTime20";
						queryValue = "{0 TO *]";
						queryStr = queryStr + ") OR (" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = "[* TO " + getWantTime(1) + "}";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue + "))";

					} else if ("2".equals(map.get("dlsOver"))) {
						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = getWantTime(1);
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "xjShipmentsTime20";
						queryValue = "{0 TO *]";
						queryStr = queryStr + ") OR (" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = getWantTime(2);
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue + "))";

					} else if ("3".equals(map.get("dlsOver"))) {
						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = getWantTime(2);
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "xjShipmentsTime20";
						queryValue = "{0 TO *]";
						queryStr = queryStr + ") OR (" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = getWantTime(3);
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue + "))";
					} else if ("4".equals(map.get("dlsOver"))) {
						queryKey = "xjShipmentsTime20";
						queryValue = "[* TO 0}";
						queryStr = queryStr + " AND ((" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = "[* TO " + getWantTime(3) + "]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;

						queryKey = "xjShipmentsTime20";
						queryValue = "{0 TO *]";
						queryStr = queryStr + ") OR (" + queryKey + ":" + queryValue;

						queryKey = "ymdShipmentsTime";
						queryValue = "[* TO " + getWantTime(4) + "]";
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue + "))";
					}
				} else if ("userId".equals(key)) {
					String shipperCode = (String) map.get("shipperCode");
					String[] shipperCodeArr = shipperCode.split(",");
					String s = "";
					for (String shipperCodeStora : shipperCodeArr) {
						shipperCodeStora = shipperCodeStora.replaceAll("'", "");
						s = s + " OR scsc:" + shipperCodeStora;
					}
					String substring = s.substring(4);
					substring = "(" + substring + ")";
					query.setQuery(substring);
				} else if ("state".equals(key)) {
					queryKey = "state";
					StateType st = (StateType) map.get(key);
					int code = st.code();
					queryValue = code + "";
					queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
				} else if ("orderState".equals(key)) {
					// 根据配送状态查询
					String orderState = map.get("orderState").toString();
					queryKey = "orderStatus";
					if ("qs".equals(orderState)) {
						queryValue = "0";
					} else if ("thqs".equals(key)) {
						queryValue = "1";
					} else if ("thz".equals(key)) {
						queryValue = "2";
					} else if ("ys".equals(key)) {
						queryValue = "3";
					}
					queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
				} else if ("tookReason".equals(key)) {
					// 揽收原因
					queryKey = "tookReason";
					String queryValue1;
					String took = map.get("tookReason").toString();
					if ("skcs".equals(took)) {
						queryValue1 = "0";
					} else if ("xt".equals(took)) {
						queryValue1 = "1";
					} else if ("lz".equals(took)) {
						queryValue1 = "2";
					} else if ("yc".equals(took)) {
						queryValue1 = "3";
					} else {
						queryValue1 = "4";
					}
					queryStr = queryStr + " AND " + queryKey + ":" + queryValue1;
				} else if ("logisticCode".equals(key)) {
					queryKey = "logisticCode";
					queryValue = "[" + map.get(key) + " TO " + map.get(key) + "]";
					queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
				} else {
					if (!"dqOver".equals(key) && !"shipperCode".equals(key) && !"courierName".equals(key)
							&& !"dlsOver".equals(key) && !"orderField".equals(key) && !"orderDirection".equals(key)
							&& !"shipmentsTimeStart".equals(key) && !"shipmentsTimeEnd".equals(key)
							&& !"planTimeStart".equals(key) && !"planTimeEnd".equals(key)
							&& !"tookTimeStart".equals(key) && !"tookTimeEnd".equals(key)
							&& !"signTimeStart".equals(key) && !"signTimeEnd".equals(key) && !"payTimeStart".equals(key)
							&& !"payTimeEnd".equals(key) && !"orderState".equals(key)) {
						queryKey = key;
						queryValue = map.get(key);
						queryStr = queryStr + " AND " + queryKey + ":" + queryValue;
					}
				}
			}
		}
		if (!"".equals(queryStr)) {
			queryStr = queryStr.substring(5);
			if (null != query.getQuery() || !"".equals(query.getQuery())) {
				String query2 = query.getQuery();
				queryStr = queryStr + " AND " + query2;
			}
			query.setQuery(queryStr);
		} else {
			if (null == query.getQuery() || "".equals(query.getQuery())) {
				query.setQuery("*:*");
			}
		}
		QueryResponse response = null;
		try {
			response = solrClient.query(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SolrDocumentList solrDocumentList = response.getResults();
		long total = solrDocumentList.getNumFound();
		DocumentObjectBinder binder = new DocumentObjectBinder();
		List<Info> infos = binder.getBeans(Info.class, solrDocumentList);
		PageInfo<Info> pageInfo = new PageInfo<Info>();
		pageInfo.setList(infos);
		pageInfo.setPageNum(page);
		pageInfo.setPageSize(rows);
		// 数据库数据总条数
		pageInfo.setTotal(total);
		pageInfo.setPages(10);
		return pageInfo;
	}

	private String getWantTime(Integer day) {
		Date currentTime = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		currentTime = calendar.getTime();
		String yesterday = sdf1.format(currentTime);
		return yesterday;
	}

	public PageInfo<Info> singleSolrFind(Map<String, Object> map, Integer page, Integer rows) {
		SolrQuery query = new SolrQuery();// 查询
		String queryStr = "";
		for (String key : map.keySet()) {
			if ("logisticCode".equals(key)) {
				queryStr = "logisticCode:" + map.get(key);
			}
		}
		query.setQuery(queryStr);
		QueryResponse response = null;
		try {
			response = solrClient.query(query);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		SolrDocumentList solrDocumentList = response.getResults();
		long total = solrDocumentList.getNumFound();
		DocumentObjectBinder binder = new DocumentObjectBinder();
		List<Info> infos = binder.getBeans(Info.class, solrDocumentList);
		PageInfo<Info> pageInfo = new PageInfo<Info>();
		pageInfo.setList(infos);
		// 数据库数据总条数
		pageInfo.setTotal(total);
		return pageInfo;
	}

	/**
	 * @param date
	 * @return
	 */
	public String getBackTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);// date 换成已经已知的Date对象
		cal.add(Calendar.HOUR_OF_DAY, -8);// back 8 hour
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(cal.getTime());
		return time;
	}

	public String getTimeYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
}
