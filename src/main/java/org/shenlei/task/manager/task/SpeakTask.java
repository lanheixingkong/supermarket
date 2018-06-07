/**
 * 
 */
package org.shenlei.task.manager.task;

import java.util.Random;

import org.shenlei.task.manager.speak.Speak;

/**
 * @author: 谌磊
 * @date: 2018年6月7日 上午9:54:26
 * 
 */
public class SpeakTask extends BaseTask {

	@Override
	public void doTask() {
		Speak.speak(getDescription());
	}

	public SpeakTask() {
		this.setTaskName("SpeakTask-" + System.currentTimeMillis() + new Random().nextInt());
	}

}
