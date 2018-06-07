package org.shenlei.task.manager.dubbo.service;

import java.util.List;

import org.shenlei.task.manager.task.TaskInfo;
import org.shenlei.task.manager.task.TaskProcessor;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:59:08
 * 
 */
public class BaseTaskServiceImpl implements BaseTaskService {
	@Override
	public List<TaskInfo> getTaskInfo() {
		return TaskProcessor.getInstance().getTaskInfo();
	}

	@Override
	public boolean start(String taskName) {
		return TaskProcessor.getInstance().start(taskName);
	}

	@Override
	public boolean stop(String taskName) {
		return TaskProcessor.getInstance().stop(taskName);
	}

	@Override
	public void stopAll() {
		TaskProcessor.getInstance().stopAll();
	}
}
