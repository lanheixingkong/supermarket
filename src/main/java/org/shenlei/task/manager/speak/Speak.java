/**
 * 
 */
package org.shenlei.task.manager.speak;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.shenlei.task.manager.task.Version;

import com.iflytek.cloud.speech.ResourceUtil;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizerListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 谌磊
 * @date: 2018年6月7日 下午12:27:13
 * 
 */
@Slf4j
public class Speak {
	// 语音合成对象
	private SpeechSynthesizer mTts;
	private Map<String, String> mParamMap = new HashMap<String, String>();
	private Map<String, String[]> mVoiceMap = new LinkedHashMap<String, String[]>();

	private static Speak instance = new Speak();
	private static final int QUEUE_SIZE = 1000;
	private final BlockingQueue<String> producerQueue = new LinkedBlockingQueue<String>(QUEUE_SIZE);
	private Object lock = new Object();

	public static void speak(String content) {
		try {
			log.info("put speak content:{}", content);
			instance.producerQueue.put(content);
		} catch (InterruptedException e) {
			log.error("队列添加异常", e);
		}
	}

	private class SpeakRunnable implements Runnable {

		@Override
		public void run() {
			String content = null;
			while (true) {
				synchronized (lock) {
					content = producerQueue.poll();
					if (content != null) {
						log.info("开始播放：{}", content);
						instance.mTts.startSpeaking(content, mSynListener);
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private Speak() {
		initSpeech();
		Thread t = new Thread(new SpeakRunnable());
		t.setDaemon(true);
		t.start();
	}

	public void initSpeech() {
		StringBuffer param = new StringBuffer();
		param.append("appid=" + Version.getAppid());
		// param.append( ","+SpeechConstant.LIB_NAME_32+"=myMscName" );
		SpeechUtility.createUtility(param.toString());
		mTts = SpeechSynthesizer.createSynthesizer();
		initParamMap();
		log.info("Speech初始化完成。。。");
	}

	private static class DefaultValue {
		public static final String ENG_TYPE = SpeechConstant.TYPE_CLOUD;
		// public static final String ENG_TYPE = SpeechConstant.TYPE_LOCAL;
		public static final String VOICE = "小燕";
		public static final String BG_SOUND = "0";
		public static final String SPEED = "50";
		public static final String PITCH = "50";
		public static final String VOLUME = "50";
	}

	private void initParamMap() {
		this.mParamMap.put(SpeechConstant.ENGINE_TYPE, DefaultValue.ENG_TYPE);
		this.mParamMap.put(SpeechConstant.VOICE_NAME, DefaultValue.VOICE);
		this.mParamMap.put(SpeechConstant.BACKGROUND_SOUND, DefaultValue.BG_SOUND);
		this.mParamMap.put(SpeechConstant.SPEED, DefaultValue.SPEED);
		this.mParamMap.put(SpeechConstant.PITCH, DefaultValue.PITCH);
		this.mParamMap.put(SpeechConstant.VOLUME, DefaultValue.VOLUME);
		this.mParamMap.put(SpeechConstant.TTS_AUDIO_PATH, null);
	}

	void setting() {
		final String engType = this.mParamMap.get(SpeechConstant.ENGINE_TYPE);
		String voiceName = null;

		for (Entry<String, String> entry : this.mParamMap.entrySet()) {
			String value = entry.getValue();
			if (SpeechConstant.VOICE_NAME.equals(entry.getKey())) {
				String[] names = this.mVoiceMap.get(entry.getValue());
				voiceName = value = SpeechConstant.TYPE_CLOUD.equals(engType) ? names[0] : names[1];
			}

			mTts.setParameter(entry.getKey(), value);
		}

		// 本地合成时设置资源，并启动引擎
		if (SpeechConstant.TYPE_LOCAL.equals(engType)) {
			// 启动合成引擎
			mTts.setParameter(ResourceUtil.ENGINE_START, SpeechConstant.ENG_TTS);
			// 设置资源路径
			String curPath = System.getProperty("user.dir");
			log.info("Current path={}", curPath);
			String resPath = ResourceUtil.generateResourcePath(curPath + "/tts/common.jet") + ";"
					+ ResourceUtil.generateResourcePath(curPath + "/tts/" + voiceName + ".jet");
			System.out.println("resPath=" + resPath);
			mTts.setParameter(ResourceUtil.TTS_RES_PATH, resPath);
		} // end of if is TYPE_LOCAL

		// 启用合成音频流事件，不需要时，不用设置此参数
		mTts.setParameter(SpeechConstant.TTS_BUFFER_EVENT, "1");
	}// end of function setting

	// 合成监听器
	private SynthesizerListener mSynListener = new SynthesizerListener() {
		// 会话结束回调接口，没有错误时，error为null
		public void onCompleted(SpeechError error) {
			synchronized (lock) {
				lock.notify();
			}
		}

		// 缓冲进度回调
		// percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		// 开始播放
		public void onSpeakBegin() {
		}

		// 暂停播放
		public void onSpeakPaused() {
		}

		// 播放进度回调
		// percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		// 恢复播放回调接口
		public void onSpeakResumed() {
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, int arg3, Object arg4, Object arg5) {
		}
	};

}
