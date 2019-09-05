package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class CardDao {
	private static final Logger logger = LoggerFactory.getLogger(CardDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>>getCards(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_card";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public List<Map<String,Object>>getMMCards(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_card_mm";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public List<Map<String,Object>> getCardByCardIndex(int star,int cardIndex){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_card where color = ? and masterpacket = 1 limit "+cardIndex+",1";
        String[] para = new String[1];
        para[0] = String.valueOf(star);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public List<Map<String,Object>> getCardByCardIndex(int star,int cardIndex,int cardId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_card where color = ? and cardid <> ? limit "+cardIndex+",1";
        String[] para = new String[2];
        para[0] = String.valueOf(star);
        para[1] = String.valueOf(cardId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public Map<String,Object> getCardById(Long cardId){
        String sql = "select * From t_cgiser_card where cardid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(cardId);
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public List<Map<String,Object>> getCardBySkill(int skillId){
        String sql = "select * From t_cgiser_card where skill = ? or lockskill1 = ? or lockskill2 = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(skillId);
        para[1] = String.valueOf(skillId);
        para[2] = String.valueOf(skillId);
        return mokaJdbcTemplate.queryForList(sql,para);
		
	}
	public int getCardCountByStar(int star){
        String sql = "select count(*) From t_cgiser_card where color = ? and masterpacket = 1";
        String[] para = new String[1];
        para[0] = String.valueOf(star);
        return mokaJdbcTemplate.queryForInt(sql,para);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}
