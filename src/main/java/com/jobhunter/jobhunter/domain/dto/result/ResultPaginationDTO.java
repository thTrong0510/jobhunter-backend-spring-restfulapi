package com.jobhunter.jobhunter.domain.dto.result;

import com.jobhunter.jobhunter.domain.dto.attach.Meta;

public class ResultPaginationDTO {
	private Meta meta;
	private Object result;

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Meta getMeta() {
		return meta;
	}

	public Object getResult() {
		return result;
	}

	public ResultPaginationDTO(Meta meta, Object result) {
		this.meta = meta;
		this.result = result;
	}

	public ResultPaginationDTO() {
	}

}
