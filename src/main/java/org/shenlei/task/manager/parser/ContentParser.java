/**
 * 
 */
package org.shenlei.task.manager.parser;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.shenlei.task.manager.task.BaseTask;
import org.shenlei.task.manager.task.SpeakTask;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 谌磊
 * @date: 2018年6月7日 上午10:41:38
 * 
 */
@Component
@Slf4j
public class ContentParser {

	public List<BaseTask> parse() {
		List<BaseTask> list = Lists.newArrayList();
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(ContentParser.class.getClassLoader().getResourceAsStream("content.xml"));
			Element root = document.getRootElement();
			SpeakTask task = null;
			for (Element config : root.elements()) {
				task = new SpeakTask();
				for (Element content : config.elements()) {
					if ("内容".equals(content.getName())) {
						task.setDescription(content.getStringValue());
					} else if ("开始时间".equals(content.getName())) {
						task.setTaskStartTime(content.getStringValue() + ":00");
					} else if ("结束时间".equals(content.getName())) {
						task.setTaskEndTime(content.getStringValue() + ":00");
					} else if ("间隔时间".equals(content.getName())) {
						task.setPeriod(Long.parseLong(content.getStringValue()));
					}
				}

				list.add(task);
			}

			log.info("共解析任务数量：{}, 任务详细：{}", list.size(), list);
		} catch (Exception e) {
			log.error("content解析异常", e);
		}
		return list;
	}
}
