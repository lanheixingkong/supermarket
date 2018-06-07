/**
* Copyright (c) 2013-2016 zhaoshangdai Ltd. All Rights Reserved.   
*   
* This software is the confidential and proprietary information of   
* zhaoshangdai. You shall not disclose such Confidential Information   
* and shall use it only in accordance with the terms of the agreements   
* you entered into with zhaoshangdai.   
*/
package org.shenlei.task.manager.task;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * <p>
 * @ClassName: TaskInfo
 * </p>
 * <p>
 * @version: V 4.0
 * </p>
 * <p>
 * @Description: (task任务信息)
 * </p>
 * 
 * @author: 杨君红
 * @date: 2016年11月29日 下午4:32:02
 */
@Data
@Builder
public class TaskInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 任务名称 */
	private String taskName;
	/** 任务状态  */
	private boolean running;
	/** 描述  */
	private String description;
	/** 间隔时间  */
	private Long period;
	/** 初始启动延迟时间 */
	private Long initDelay;
	/** 任务类型（0间隔周期执行,1固定时间点执行）*/
	private Integer type;
	/** 只想状态描述  */
	private String note;
	/** 固定时间点执执行的时间  */
	private String strFixedTime;

}
