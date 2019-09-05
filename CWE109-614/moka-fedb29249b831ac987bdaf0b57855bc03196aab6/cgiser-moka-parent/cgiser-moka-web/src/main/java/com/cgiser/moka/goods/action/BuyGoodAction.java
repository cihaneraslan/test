package com.cgiser.moka.goods.action;

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
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Color;
import com.cgiser.moka.model.Good;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class BuyGoodAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String goodsId = ServletUtil.getDefaultValue(request, "goodsId",
		"");
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(goodsId)&&!StringUtils.isNumeric(goodsId)){
				logger.info("购买商品失败,参数有误");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("购买商品失败,参数有误");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			GoodManager goodManager = (GoodManager)HttpSpringUtils.getBean("goodManager");
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			Good good = goodManager.getGoodById(Integer.parseInt(goodsId));
			if(good==null){
				returnType.setMsg("购买的商品不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(good.getCash()>0){
				if(role.getCash()<good.getCash()){
					returnType.setMsg("您的元宝不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(good.getCoins()>0){
				if(role.getCoins()<good.getCoins()){
					returnType.setMsg("您的铜钱不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(good.getTicket()>0){
				if(role.getTicket()<good.getTicket()){
					returnType.setMsg("您的神龙券不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			Color color = good.getColors().get(String.valueOf(good.getColor()));
			List<Card> cards = new ArrayList<Card>();
			boolean hasStar45 = false;
			Card card = new Card();
			int times = roleManager.getRoleBuyGoodsTime(role.getRoleId());
			if(good.getCash()>0){
				if(times==0){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 40);
				}else if(times<=10){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), times-1);
				}
				if(times==20){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 100);
				}else if(times<=40){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), times-1);
				}
				if(times==50){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 100);
				}else if(times<=100){
					roleManager.updateRoleBuyGoodsTime(role.getRoleId(), times-1);
				}
			}
			
			for(int i =0;i<good.getNum();i++){
				double a= Math.random()*100;
				double s1 = color.getStar1();
				double s2 = s1+color.getStar2();
				double s3 = s2+color.getStar3();
				double s4 = s3+color.getStar4();
				double s5 = s4+color.getStar5();
				if(times==0||times==20||times==50){
					a = s4-0.00001;
					times = 100;
				}
				//如果已经随机出4、5星卡牌则减掉随机出4,5星的概率，避免一个包里面随机出多张4、5星卡牌
				if (hasStar45&&a>s3&&a<s5){
					a = a-color.getStar4()-color.getStar5();
				}else{
					if(a>s3&a<s4){
						hasStar45 = true;
						card = cardManager.randomCard(4);
						if(times<=10){
							roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 40);
						}else{
							roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 100);
						}
					}
					if(a>s4&a<s5){
						hasStar45 = true;
						card = cardManager.randomCard(5);
						if(times<=10){
							roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 40);
						}else{
							roleManager.updateRoleBuyGoodsTime(role.getRoleId(), 100);
						}
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
			if(cards.size()<good.getNum()){
				returnType.setMsg("购买的商品不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			//随机成功后扣掉花费的元宝，铜钱
			if(good.getCash()>0){
				if(!roleManager.updateCash(role.getRoleName(), good.getCash())){
					returnType.setMsg("购买失败,您的元宝不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(good.getCoins()>0){
				if(!roleManager.updateCoin(role.getRoleName(), good.getCoins())){
					returnType.setMsg("购买失败,您的铜钱不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(good.getTicket()>0){
				if(!roleManager.updateTicket(role.getRoleName(), good.getTicket())){
					returnType.setMsg("购买失败,您的神龙券不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
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
			returnType.setStatus(1);
			returnType.setValue(strCards.toString());
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
	public static void main(String[] args) {
		System.out.println(Math.random()*100);
	}
}
