package com.pi;

public interface Filter<T> {
	/**
	 * 
	 * @param t
	 * @return the weight value 0 = false, >=1 = true
	 */
	public int accept(T t);
}
