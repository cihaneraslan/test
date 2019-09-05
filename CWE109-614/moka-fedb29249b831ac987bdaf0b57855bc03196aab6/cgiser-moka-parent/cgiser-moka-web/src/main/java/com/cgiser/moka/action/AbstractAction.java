package com.cgiser.moka.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.PropertyNameProcessor;

import org.apache.struts.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.VersionManager;
import com.cgiser.moka.model.AwardItem;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.ExtData;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.LoginAward;
import com.cgiser.moka.model.MMap;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.model.UserRune;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.model.Version;
import com.cgiser.moka.result.AwardResult;
import com.cgiser.moka.result.EmblemResult;
import com.cgiser.moka.result.ExploreResult;
import com.cgiser.moka.result.FaneResult;
import com.cgiser.moka.result.ReturnType;

public class AbstractAction extends Action {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<String, Cookie> cookieMap;
	public static String cookieId = "_cid";
	private JsonConfig jsonConfig;
	public static Version version;

	/**
	 * 打印返回值给客户端
	 * 
	 * @param response
	 * @param returnType
	 * @throws IOException
	 */
	protected void printReturnType2Response(HttpServletResponse response,
			ReturnType<?> returnType) {
		
		response.setCharacterEncoding("UTF-8");
		if (jsonConfig == null) {
			jsonConfig = new JsonConfig();
			PropertyNameProcessor processor = new PropertyNameProcessor() {
				@Override
				public String processPropertyName(Class beanClass, String name) {
					name = name.replaceFirst(name.substring(0, 1), name
							.substring(0, 1).toUpperCase());
					return name;
				}
			};
			JsonValueProcessor dateprocessor = new JsonValueProcessor() {
				@Override
				public Object processObjectValue(String key, Object value,
						JsonConfig jsonConfig) {
					if (value instanceof Date) {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							return dateFormat.format(value);
						} catch (Exception e) {
							return value;
						}
					}
					return value;
				}

				@Override
				public Object processArrayValue(Object value,
						JsonConfig jsonConfig) {
					// TODO Auto-generated method stub
					return null;
				}
			};
			jsonConfig.registerJsonValueProcessor(Date.class, dateprocessor);
			jsonConfig.registerJsonPropertyNameProcessor(Card.class, processor);
			jsonConfig
					.registerJsonPropertyNameProcessor(Skill.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(UserCard.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(UserCardGroup.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Rune.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(UserRune.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(FightResult.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Player.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Battle.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(FightCard.class,
					processor);
			jsonConfig
					.registerJsonPropertyNameProcessor(Round.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(Opp.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(UserStage.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(MMap.class,processor);
			jsonConfig.registerJsonPropertyNameProcessor(Stage.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(StageLevel.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(ExtData.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(FightRune.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(LegionApply.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(EmblemResult.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(AwardItem.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(AwardResult.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(FaneResult.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(LoginAward.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Soul.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(ExploreResult.class,
					processor);
			if(version==null){
				VersionManager versionManager = (VersionManager)HttpSpringUtils.getBean("versionManager");
				version = versionManager.getLastVersion();
			}
		}
		returnType.setVersion(version);
		try {
			response.getWriter().print(
					JSONObject.fromObject(returnType, jsonConfig));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Role getCurrentRole(HttpServletRequest request) {
		cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			int length = cookies.length;
			for (int i = 0; i < length; i++) {
				cookieMap.put(cookies[i].getName(), cookies[i]);
			}
		}
		Cookie cookie = cookieMap.get(cookieId);
		if (null != cookie) {
			RoleManager roleManager = (RoleManager) HttpSpringUtils
					.getBean("roleManager");
//			List<Role> list = roleManager.getRolesByUserIden(cookie.getValue(),
//					null);
//			if (CollectionUtils.isEmpty(list)) {
//				return null;
//			}
			return roleManager.getRoleByDigestRoleName(cookie.getValue());
		} else {
			return null;
		}
	}
}
