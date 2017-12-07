package com.ln.tms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * BaiShiTrace 百世汇通物流信息
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaiShiTrace {


    private List<TraceLogsBean> traceLogs;

    public List<TraceLogsBean> getTraceLogs() {
        return traceLogs;
    }

    public void setTraceLogs(List<TraceLogsBean> traceLogs) {
        this.traceLogs = traceLogs;
    }

    public static class TraceLogsBean {
        private String logisticProviderID;//快递承运商编码
        private String mailNo; //运单号
        private List<TracesBean> traces; //流转信息
        private Problems problems;  //备注（问题信息）

        public String getLogisticProviderID() {
            return logisticProviderID;
        }

        public void setLogisticProviderID(String logisticProviderID) {
            this.logisticProviderID = logisticProviderID;
        }

        public String getMailNo() {
            return mailNo;
        }

        public void setMailNo(String mailNo) {
            this.mailNo = mailNo;
        }

        public List<TracesBean> getTraces() {
            return traces;
        }

        public void setTraces(List<TracesBean> traces) {
            this.traces = traces;
        }

        public Problems getProblems() {
            return problems;
        }

        public void setProblems(Problems problems) {
            this.problems = problems;
        }

        public static class TracesBean {
            private String acceptTime;
            private String acceptAddress;
            private String scanType;
            private String remark;

            public String getAcceptTime() {
                return acceptTime;
            }

            public void setAcceptTime(String acceptTime) {
                this.acceptTime = acceptTime;
            }

            public String getAcceptAddress() {
                return acceptAddress;
            }

            public void setAcceptAddress(String acceptAddress) {
                this.acceptAddress = acceptAddress;
            }

            public String getScanType() {
                return scanType;
            }

            public void setScanType(String scanType) {
                this.scanType = scanType;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }

        public static class Problems {


            private List<Problem> problem; //备注（问题信息）

            public List<Problem> getTraces() {
                return problem;
            }

            public void setTraces(List<Problem> problem) {
                this.problem = problem;
            }

            public static class Problem {
                /**
                 * 通知站点
                 */
                private String noticeSite;
                /**
                 * 问题件原因
                 */
                private String problemCause;
                /**
                 * 问题件类型
                 */
                private String problemType;
                /**
                 * 登记时间
                 */
                private String registerDate;
                /**
                 * 登记人
                 */
                private String registerMan;
                /**
                 * 回复内容
                 */
                private String replyContent;
                /**
                 * 回复时间
                 */
                private String replyDate;
                /**
                 * 回复人
                 */
                private String replyMan;
                /**
                 * 序号
                 */
                private Long seqNum;

                public String getNoticeSite() {
                    return noticeSite;
                }

                public void setNoticeSite(String noticeSite) {
                    this.noticeSite = noticeSite;
                }

                public String getProblemCause() {
                    return problemCause;
                }

                public void setProblemCause(String problemCause) {
                    this.problemCause = problemCause;
                }

                public String getProblemType() {
                    return problemType;
                }

                public void setProblemType(String problemType) {
                    this.problemType = problemType;
                }

                public String getRegisterDate() {
                    return registerDate;
                }

                public void setRegisterDate(String registerDate) {
                    this.registerDate = registerDate;
                }

                public String getRegisterMan() {
                    return registerMan;
                }

                public void setRegisterMan(String registerMan) {
                    this.registerMan = registerMan;
                }

                public String getReplyContent() {
                    return replyContent;
                }

                public void setReplyContent(String replyContent) {
                    this.replyContent = replyContent;
                }

                public String getReplyDate() {
                    return replyDate;
                }

                public void setReplyDate(String replyDate) {
                    this.replyDate = replyDate;
                }

                public String getReplyMan() {
                    return replyMan;
                }

                public void setReplyMan(String replyMan) {
                    this.replyMan = replyMan;
                }

                public Long getSeqNum() {
                    return seqNum;
                }

                public void setSeqNum(Long seqNum) {
                    this.seqNum = seqNum;
                }
            }

        }
    }
}