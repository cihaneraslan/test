package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Color;
import com.cgiser.moka.model.Good;

public interface GoodManager {
	/**
	 * 获取所有商品
	 * @return
	 */
	public List<Good> getGoods();
	public Good getGoodById(int goodsId);
	/**
	 * 获取商品概率
	 * @param id
	 * @return
	 */
	public Color getGoodColorById(int id);
}
