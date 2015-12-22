package com.example.htp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 操作结果实体类
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("htp")
public class ResultBean extends Base {

    @XStreamAlias("result")
    private Result result;

    @XStreamAlias("notice")
    private Notice notice;


    @XStreamAlias("relation")
    private int relation;

    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
    }

    public int getRelation() {
	return relation;
    }

    public void setRelation(int relation) {
	this.relation = relation;
    }

    public Notice getNotice() {
	return notice;
    }

    public void setNotice(Notice notice) {
	this.notice = notice;
    }

}
