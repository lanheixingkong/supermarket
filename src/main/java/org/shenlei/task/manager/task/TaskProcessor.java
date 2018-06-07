package org.shenlei.task.manager.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.shenlei.task.manager.util.DateUtils;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:58:50
 * 
 */
@Slf4j
public class TaskProcessor {

	private Map<String, BaseTask> taskMap = new ConcurrentHashMap<>();
	private Map<String, ScheduledFuture<?>> scheduledMap = new ConcurrentHashMap<>();

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private static final int CORE_POOL_SIZE = 18;

	private static final long ONE_DAY = 60 * 60 * 24;

	private final ScheduledExecutorService scheduExec = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE,
			new BasicThreadFactory.Builder().namingPattern("task-schedule-pool-%d").daemon(false).build());

	private TaskProcessor() {
	}

	private static class TaskProcessorHolder {

		private static TaskProcessor instance = new TaskProcessor();
	}

	public static TaskProcessor getInstance() {
		return TaskProcessorHolder.instance;
	}

	public static ScheduledExecutorService getScheduExec() {
		return TaskProcessorHolder.instance.scheduExec;
	}

	/**
	 * 启动任务
	 * 
	 * @param baseTask
	 *            任务
	 * @param isDelay
	 *            是否延迟启动
	 * @return 任务信息
	 */
	public boolean start(BaseTask baseTask, boolean isDelay) {
		if (baseTask == null || StringUtils.isBlank(baseTask.getTaskName())) {
			log.error("BaseTask或TaskName不能为空");
			return false;
		}

		try {
			readWriteLock.writeLock().lock();

			String taskName = baseTask.getTaskName();
			if (isRunning(taskName)) {
				log.warn("不能重复启动任务{}", taskName);
				return false;
			}

			log.info("开始启动任务：{}", taskName);

			long initDelay = 13;
			ScheduledFuture<?> sche;
			long period = baseTask.getPeriod() == 0 ? ONE_DAY : baseTask.getPeriod();
			// 间隔周期执行
			if (StringUtils.isEmpty(baseTask.getFixedTime())) {
				initDelay = isDelay ? baseTask.getInitialDelay() : 0L;
				// 从现在开始 initDelay 秒钟之后，每隔 Period 毫秒钟执行一次
				sche = getInstance().scheduExec.scheduleAtFixedRate(baseTask, initDelay, period, TimeUnit.SECONDS);
			} else {
				// 固定时间点执行
				String strFixedTime = baseTask.getFixedTime();
				long currentTime = System.currentTimeMillis();
				long fixedTime = DateUtils.getTimeMillis(strFixedTime);
				initDelay = isDelay ? fixedTime - currentTime : 0L;

				initDelay = initDelay > 0 ? initDelay : period + initDelay;

				// 从现在开始 initDelay 毫秒之后，每隔 Period 毫秒执行一次
				sche = getInstance().scheduExec.scheduleAtFixedRate(baseTask, initDelay, period, TimeUnit.SECONDS);
			}

			taskMap.put(taskName, baseTask);
			scheduledMap.put(taskName, sche);

			log.info("结束启动任务:{}", taskName);
			return true;
		} finally {
			readWriteLock.writeLock().unlock();
		}

	}

	private boolean isRunning(String taskName) {
		return scheduledMap.containsKey(taskName) && !scheduledMap.get(taskName).isDone();
	}

	/**
	 * 启动指定任务
	 * 
	 * @param taskName
	 *            任务名称
	 */
	public boolean start(String taskName) {
		BaseTask task = taskMap.get(taskName);
		return start(task, true);
	}

	public void start(List<BaseTask> list) {
		list.forEach(baseTask -> start(baseTask, false));
	}

	public boolean stop(String taskName) {
		try {
			readWriteLock.writeLock().lock();

			log.info("开始停止任务:{} ", taskName);
			boolean succ = false;
			if (scheduledMap.containsKey(taskName)) {
				ScheduledFuture<?> sche = scheduledMap.get(taskName);
				succ = sche.cancel(false);
			} else {
				log.warn("任务{}不存在", taskName);
			}

			log.info("结束停止任务:{}", taskName);
			return succ;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	public void stopAll() {
		try {
			readWriteLock.writeLock().lock();

			scheduledMap.forEach((k, v) -> v.cancel(false));
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	public List<TaskInfo> getTaskInfo() {
		try {
			readWriteLock.readLock().lock();

			List<TaskInfo> list = Lists.newArrayList();
			taskMap.forEach((k, v) -> {
				TaskInfo.TaskInfoBuilder builder = TaskInfo.builder().taskName(k).description(v.getDescription())
						.period(v.getPeriod()).initDelay(v.getInitialDelay()).type(v.getTaskType());
				ScheduledFuture<?> sche = scheduledMap.get(k);
				if (sche.isDone()) {
					builder.note("任务已结束").running(false);
				} else {
					builder.note("任务运行中").running(true);
				}

				list.add(builder.build());
			});

			return list;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

}
