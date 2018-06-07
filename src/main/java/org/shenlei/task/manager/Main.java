/**
 * 
 */
package org.shenlei.task.manager;

import java.util.List;

import org.shenlei.task.manager.parser.ContentParser;
import org.shenlei.task.manager.task.BaseTask;
import org.shenlei.task.manager.task.TaskInfo;
import org.shenlei.task.manager.task.TaskProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 谌磊
 * @date: 2018年6月7日 上午10:06:48
 * 
 */
@Slf4j
public class Main {

	public static void main(String[] args) {
		// 初始化spring容器
		String path = "classpath:applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(path);
		ContentParser parser = context.getBean(ContentParser.class);
		List<BaseTask> taskList = parser.parse();
		TaskProcessor.getInstance().start(taskList);
		log.info("任务启动完成....");
		printTaskInfo();
		
	}

	private static void printTaskInfo() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					log.info("---------start print task info------------");
					List<TaskInfo> taskInfos = TaskProcessor.getInstance().getTaskInfo();
					log.info("总任务数量为：{}", taskInfos.size());
					for (TaskInfo info : taskInfos) {
						log.info("TaskInfo:{}", info);
					}
					try {
						Thread.sleep(1000*60*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

}
