package org.shenlei.task.manager.task;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:58:36
 * 
 */
public interface BaseTaskInterface extends Runnable {

	/**
	 * 任务执行方法
	 */
	public void doTask();
}
