package org.shenlei.task.manager.dubbo.service;

import java.util.List;

import org.shenlei.task.manager.task.TaskInfo;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:59:02
 * 
 */
public interface BaseTaskService {

	/**
	 * 获取任务信息
	 * 
	 * @return
	 */
	List<TaskInfo> getTaskInfo();

	/**
	 * 启动任务
	 * 
	 * @param taskName 任务名
	 * @return
	 */
	boolean start(String taskName);

	/**
	 * 停止任务
	 * 
	 * @param taskName 任务名
	 * @return
	 */
	boolean stop(String taskName);

	/**
	 * 停止所有任务
	 * 
	 */
	void stopAll();

}
