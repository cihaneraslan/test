package com.cgiser.moka.legion.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.GoodManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Color;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionGood;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class BuyLegionGoodAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String goodId = ServletUtil.getDefaultValue(request, "goodId", "");
			Role role = super.getCurrentRole(request);
			if(StringUtils.isEmpty(goodId)){
				returnType.setMsg("请选择要购买的商品！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您还没加入帮派哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("帮派不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionGood legionGood = legionManager.getLegionGoodById(new Long(goodId));
			if(legionGood==null){
				returnType.setMsg("购买的商品不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legion.getLegionLevel()<legionGood.getTechLevel()){
				returnType.setMsg("购买的商品还未解锁！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legioner.getHonor()<legionGood.getGoodPrice()){
				returnType.setMsg("您的功勋不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legionManager.updateLegionerHonor(legioner.getId(), legionGood.getGoodPrice())<1){
				returnType.setMsg("您的功勋不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(legionGood.getGoodType()==13){
				roleManager.addEnergy(role.getRoleName(), legionGood.getGoodNum());
			}
			if(legionGood.getGoodType()==4){
				GoodManager goodManager = (GoodManager)HttpSpringUtils.getBean("goodManager");
				CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
				UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
				List<Card> cards = new ArrayList<Card>();
				boolean hasStar45 = false;
				Card card = new Card();
				Color color = goodManager.getGoodColorById(legionGood.getGoodValue());
				for(int i =0;i<legionGood.getGoodNum();i++){
					double a= Math.random()*100;
					double s1 = color.getStar1();
					double s2 = s1+color.getStar2();
					double s3 = s2+color.getStar3();
					double s4 = s3+color.getStar4();
					double s5 = s4+color.getStar5();
					//如果已经随机出4、5星卡牌则减掉随机出4,5星的概率，避免一个包里面随机出多张4、5星卡牌
					if (hasStar45&&a>s3&&a<s5){
						a = a-color.getStar4()-color.getStar5();
					}else{
						if(a>s3&a<s4){
							hasStar45 = true;
							card = cardManager.randomCard(4);
						}
						if(a>s4&a<s5){
							hasStar45 = true;
							card = cardManager.randomCard(5);
						}
					}
					if(a>0&&a<s1){
						card = cardManager.randomCard(1);
					}
					if(a>s1&&a<s2){
						card = cardManager.randomCard(2);
					}
					if(a>s2&a<s3){
						card = cardManager.randomCard(3);
					}
					if(card!=null){
						cards.add(card);
					}
				}
				if(cards.size()<legionGood.getGoodNum()){
					returnType.setMsg("购买的商品不存在！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
				StringBuffer strCards = new StringBuffer();
				for(int i =0;i<cards.size();i++){
					card = cards.get(i);
					//获取3星以上卡牌全服公告
					if(card.getColor()>3){
						ChannelBuffer buffer = new DynamicChannelBuffer(200);
						buffer.writeInt(MessageType.SYSTEM.getCode());
						buffer.writeInt(MessageType.GETCARD.getCode());
						MessageUtil.writeString(buffer, role.getRoleName(), "UTF-8");
						buffer.writeInt(card.getCardId());
						messageManager.sendMessge(MessageType.SYSTEM, buffer);
					}
					userCardManager.saveUserCard(card.getCardId(), role.getRoleId());
					strCards.append(card.getCardId());
					if(i!=cards.size()-1){
						strCards.append("_");
					}
				}
				returnType.setValue(strCards.toString());
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
