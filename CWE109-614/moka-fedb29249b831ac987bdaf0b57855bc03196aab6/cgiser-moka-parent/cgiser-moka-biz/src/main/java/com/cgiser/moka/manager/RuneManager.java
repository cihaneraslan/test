package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Rune;

public interface RuneManager { 
	/**
	 * 获取所有的星辰
	 * @return
	 */
	public List<Rune> getRunes();
	/**
	 * 根据星辰ID获取星辰
	 * @param runeId
	 * @return
	 */
	public Rune getRuneById(int runeId);
	/**
	 * 根据指定星级星辰的数量
	 * @param star
	 * @return
	 */
	public Integer RandomRune(int star);
}
