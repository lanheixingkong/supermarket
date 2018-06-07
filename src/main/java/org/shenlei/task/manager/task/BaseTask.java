package org.shenlei.task.manager.task;

import org.apache.commons.lang3.StringUtils;
import org.shenlei.task.manager.constant.TaskType;
import org.shenlei.task.manager.util.DateUtils;

import lombok.Data;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:57:58
 * 
 */
@Data
public abstract class BaseTask implements BaseTaskInterface {

	/** 初始化延迟 */
	private long initialDelay;
	/** 执行间隔 */
	private long period;
	/** 固定执行时间 */
	private String fixedTime;
	/** 任务执行开始时间 */
	private String taskStartTime;
	/** 任务执行结束时间 */
	private String taskEndTime;
	/** 描述 */
	private String description;
	/** 任务名称 */
	private String taskName;

	@Override
	public void run() {

		String startTime = DateUtils.HHMMSS_MIN;
		String endTime = DateUtils.HHMMSS_MAX;

		if (StringUtils.isNotBlank(getTaskStartTime())) {
			startTime = getTaskStartTime();
		}
		if (StringUtils.isNotBlank(getTaskEndTime())) {
			endTime = getTaskEndTime();
		}

		// HH:mm:ss
		String nowTime = DateUtils.getSpecifiedTimeformat(DateUtils.HH_MM_SS);
		if (startTime.compareTo(nowTime) <= 0 && nowTime.compareTo(endTime) <= 0) {
			doTask();
		}
	}

	public int getTaskType() {
		if (StringUtils.isBlank(fixedTime)) {
			return TaskType.INTERVAL_PERIOD;
		}

		return TaskType.FIXED_TIME;
	}
}
