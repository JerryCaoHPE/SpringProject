package com.hpe.base.entity;

public class Entity {
	/**
	 * 用于json序列化
	 */
	private final static ThreadLocal<Boolean> lazyLoad = new ThreadLocal<Boolean>();
	
	private Long EId;

	public Long getEId() {
		return EId;
	}

	public void setEId(Long eId) {
		EId = eId;
	}

	public static ThreadLocal<Boolean> getLazyload() {
		return lazyLoad;
	}
}
