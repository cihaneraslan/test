
package com.cgiser.moka.dao;

import javax.sql.DataSource;

import org.springframework.core.Ordered;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * 动态绑定测试类
 * 
 * @author yangh
 * @version $Id$
 */
public class DataSourceMock implements Ordered {

    private DataSource mokaDataSource;
    public DataSource getMokaDataSource() {
		return mokaDataSource;
	}

	public void setMokaDataSource(DataSource mokaDataSource) {
		this.mokaDataSource = mokaDataSource;
	}

    public void init() {
        try {
            SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
            builder.bind("java:comp/env/mokaDataSource", mokaDataSource);
            builder.activate();
        } catch (Exception e) {

        }
    }

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		 return Ordered.HIGHEST_PRECEDENCE + 1;
	}
}
