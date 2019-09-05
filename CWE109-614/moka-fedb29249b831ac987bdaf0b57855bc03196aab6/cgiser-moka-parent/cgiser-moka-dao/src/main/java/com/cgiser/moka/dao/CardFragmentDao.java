package com.cgiser.moka.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class CardFragmentDao {
//	private static final Logger logger = LoggerFactory.getLogger(CardFragmentDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getAllCardFragment(){
        String sql = "select * From t_cgiser_card_fragment where state=1";
        return  mokaJdbcTemplate.queryForList(sql);
		
	}
	public List<Map<String,Object>> getCardFragmentByType(int type){
        String sql = "select * From t_cgiser_card_fragment where type = "+type+" and state=1";
        return  mokaJdbcTemplate.queryForList(sql);
		
	}
	public Map<String,Object> getCardFragmentByCardId(int cardId){
        String sql = "select * From t_cgiser_card_fragment where cardId="+cardId+" and state=1";
        return  mokaJdbcTemplate.queryForMap(sql);
		
	}
	public Map<String,Object> getCardFragmentByfragmentId(int fragmentId){
		String sql = "select * From t_cgiser_card_fragment where id="+fragmentId+" and state=1";
        return  mokaJdbcTemplate.queryForMap(sql);
		
	}
	
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}
