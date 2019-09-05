package com.cgiser.moka.manager;

import java.util.Date;
import java.util.List;

import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.LegionEvent;
import com.cgiser.moka.model.LegionGood;
import com.cgiser.moka.model.LegionLevel;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;

public interface LegionManager {
	/**
	 * 获取军团列表
	 * @param legion
	 * @param index
	 * @param count
	 * @return
	 */
	public List<Legion> getLegions(String legion,int index,int count);
	/**
	 * 获取军团申请
	 * @param legionId
	 * @param index
	 * @param count
	 * @return
	 */
	public List<LegionApply> getLegionApply(long legionId,int index,int count);
	/**
	 * 获取指定ID的军团申请
	 * @param legionId
	 * @return
	 */
	public LegionApply getLegionApply(long id);
	/**
	 * 获取玩家的军团申请
	 * @param legionId
	 * @param index
	 * @param count
	 * @return
	 */
	public LegionApply getLegionApplyByRoleId(long legionId,Long roleId);
	/**
	 * 获取军团成员
	 * @param roleId
	 * @return
	 */
	public Legioner getLegioner(long roleId);
	/**
	 * 获取军团成员
	 * @param roleId
	 * @return
	 */
	public Legioner getLegionerBylegionerId(long legionerId);
	/**
	 * 获取军团成员
	 * @param legionId
	 * @param index
	 * @param count
	 * @return
	 */
	public List<Legioner> getLegioner(long legionId,int index,int count);
	/**
	 * 获取军团数目
	 * @return
	 */
	public int getLegionCount();
	/**
	 * 创建军团
	 * @param roleName
	 * @param name
	 * @param createid
	 * @param slogan
	 * @return
	 */
	public Long saveLegion(Role role,String name,long createid,String slogan);
	/**
	 * 修改军团口号
	 * @param legionId
	 * @param slogan
	 * @return
	 */
	public int updateLegionSlogan(long legionId,String slogan);
	/**
	 *更新帮派公告
	 * @param legionId
	 * @param slogan
	 * @return
	 */
	public int updateLegionNotice(long legionId,String notice);
	/**
	 * 获取军团
	 * @param legionId
	 * @return
	 */
	public Legion getLegionById(long legionId);
	/**
	 * 获取军团
	 * @param name
	 * @return
	 */
	public Legion getLegionByName(String name);
	/**
	 * 申请参加军团
	 * @param legionId
	 * @param roleId
	 * @return
	 */
	public Long ApplyAddToLegion(Long legionId,Long roleId);
	/**
	 * 同意军团申请
	 * @param Id
	 * @return
	 */
	public int AgreeLegionApply(Long Id);
	/**
	 * 拒绝军团申请
	 * @param Id
	 * @return
	 */
	public int delLegionApply(Long Id);
	/**
	 * 加入帮派后删除掉之前申请的所有帮派申请
	 * @param Id
	 * @return
	 */
	public int delLegionApplyByRoleId(Long roleId);
	/**
	 * 退出军团
	 * @param legionId
	 * @return
	 */
	public int outLegion(Long legionerId);
	/**
	 * 解散军团
	 * @param legionId
	 * @return
	 */
	public int breakupLegion(Long legionId);
	/**
	 * 获取副团长
	 * @param legionId
	 * @return
	 */
	public Legioner getDeputyHeader(Long legionId);
	/**
	 * 获取指定职位的团员
	 * @param duty
	 * @return
	 */
	public List<Legioner> getLegionersByduty(Long legionId,int duty);
	/**
	 * 修改副团长
	 * @param legionId
	 * @return
	 */
	public int updateDeputyHeader(Long legionId,Long roleId);
	/**
	 * 获取团长
	 * @param legionId
	 * @return
	 */
	public Legioner getHeader(Long legionId);
	/**
	 * 辞去职位
	 * @param legionerId
	 * @return
	 */
	public int resignDuty(Long legionerId);
	/**
	 * 修改团长
	 * @param legionId
	 * @return
	 */
	public int updateHeader(Long legionId,Long roleId);
	/**
	 * 修改团员职位
	 * @param legionId
	 * @return
	 */
	public int updateLegionerDuty(Long legionerId,int duty);
	/**
	 * 获取贡献值最高的团员，贡献值一样看级别
	 * @param legionId
	 * @return
	 */
	public Legioner getMaxContributeLegioner(Long legionId);
	/**
	 * 升级军徽
	 * @param legionId
	 * @return
	 */
	public int upLegionEmblem(Role role,Long legionId);
	/**
	 * 修改军徽
	 * @param emblemId
	 * @param legionId
	 * @return
	 */
	public int updateLegionEmblem(int emblemId,Long legionId);
	/**
	 * 增加角色军团贡献和军团荣誉点
	 * @param legionerId
	 * @param contribute
	 * @param honor
	 * @return
	 */
	public int addRoleContributeHonor(Long legionerId,int contribute,int honor);
	/**
	 * 增加军团贡献值
	 * @param legionId
	 * @param contribute
	 * @return
	 */
	public int addLegionContribute(Long legionId,int contribute,int contributeId);
	/**
	 * 增加军团铜钱
	 * @param legionId
	 * @param resources
	 * @return
	 */
	public int addLegionResources(Long legionId,int resources);
	/**
	 * 减去军团铜钱
	 * @param legionId
	 * @param resources
	 * @return
	 */
	public int delLegionResources(Long legionId,int resources);
	/**
	 * 设置守卫
	 * @param id
	 * @param roleId
	 * @return
	 */
	public int setGuard(int id,Long roleId);
	/**
	 * 是否已经是守卫了
	 * @param roleId
	 * @return
	 */
	public int isGuard(Long roleId);
	/**
	 * 减少帮派战次数
	 * @return
	 */
	public int updateLegionFightAttackTimes(Long legionId);
	/**
	 * 增加当日帮派被掠夺次数
	 * @return
	 */
	public int addLegionRobTimes(Long legionId);
	/**
	 * 增加当日帮派被掠夺次数
	 * @return
	 */
	public int resetLegionRobTimes();
	/**
	 * 增加帮派战次数
	 * @return
	 */
	public int addLegionFightAttackTimes(Long legionId);
	/**
	 * 重置帮派战次数
	 * @return
	 */
	public int resetLegionFightAttackTimes();
	/**
	 * 增加帮派战购买次数
	 * @return
	 */
	public int addLegionFightBuyAttackTimes(Long legionId);
	/**
	 * 增加帮派成员帮派币
	 * @return
	 */
	public int addLegionerHonor(Long legionerId,int num);
	/**
	 * 消耗帮派成员帮派币
	 * @return
	 */
	public int updateLegionerHonor(Long legionerId,int num);
	/**
	 * 减少玩家帮派战次数
	 * @return
	 */
	public int updateRoleLegionFightAttackTimes(Long legionerId);
	/**
	 * 增加玩家帮派战次数
	 * @return
	 */
	public int addRoleLegionFightAttackTimes(Long legionerId);
	/**
	 * 重置玩家帮派战次数
	 * @return
	 */
	public int resetRoleLegionFightAttackTimes();
	/**
	 * 重置玩家敲鼓次数
	 * @return
	 */
	public int resetRoleLegionContribute();
	/**
	 * 减少玩家敲鼓次数
	 * @return
	 */
	public int updateRoleLegionContribute(Long legionerId,int id);
	/**
	 * 减少玩家敲鼓次数
	 * @return
	 */
	public int addRoleLegionContribute(Long legionerId,int id);
	/**
	 * 保存帮派事件
	 * @param legionId 帮派ID
	 * @param roleId 帮派事件发生的主体
	 * @param content 帮派时间内容
	 * @param type 帮派事件类型
	 * @return
	 */
	public Long saveLegionEvent(Long legionId,Long roleId,String content,int type);
	/**
	 * 获取帮派最近三天的事件，倒序排列
	 * @param legionId
	 * @return
	 */
	public List<LegionEvent> getLegionEvent(Long legionId);
	/**
	 * 获取帮派最近三天的指定类型事件，倒序排列
	 * @param legionId
	 * @return
	 */
	public List<LegionEvent> getLegionEventByType(Long legionId,int type);
	/**
	 * 升级帮派
	 * @param legionId
	 * @param roleId
	 * @return
	 */
	public int upLegion(Long legionId,Long roleId);
	/**
	 * 根据等级获取帮派等级信息
	 * @param level
	 * @return
	 */
	public LegionLevel getLegionLevelByLevel(int level);
	
	/**
	 * 根据科技ID获取帮派科技信息
	 * @param level
	 * @return
	 */
	public LegionTech getLegionTechByTechId(String techId);
	/**
	 * 获取所有的帮派科技
	 * @param level
	 * @return
	 */
	public List<LegionTech> getLegionTechs();
	/**
	 * 获取所有的帮派产品
	 * @return
	 */
	public List<LegionGood> getLegionGoods();
	/**
	 * 获取指定ID的帮派产品
	 * @return
	 */
	public LegionGood getLegionGoodById(Long goodId);
	/**
	 * 获取玩家最后退出帮派的时间
	 * @param roleId
	 */
	public Date getOutLegionTime(Long roleId);
}
