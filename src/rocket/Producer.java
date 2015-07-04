package rocket;

import java.util.concurrent.TimeUnit;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

public class Producer {
	public Producer(String name, String topic, String tag, 
			String body) throws MQClientException, InterruptedException {
		/**
		 * һ��Ӧ�ô���һ��Producer����Ӧ����ά���˶��󣬿�������Ϊȫ�ֶ�����ߵ���<br>
		 * ע�⣺ProducerGroupName��Ҫ��Ӧ������֤Ψһ<br>
		 * ProducerGroup����������ͨ����Ϣʱ�����ò��󣬵��Ƿ��ͷֲ�ʽ������Ϣʱ���ȽϹؼ���
		 * ��Ϊ��������ز����Group�µ�����һ��Producer
		 */
		DefaultMQProducer producer = new DefaultMQProducer(name);
		producer.setNamesrvAddr("10.131.252.221:9876");
		// producer.setInstanceName("Producer");

		/**
		 * Producer������ʹ��֮ǰ����Ҫ����start��ʼ������ʼ��һ�μ���<br>
		 * ע�⣺�мǲ�������ÿ�η�����Ϣʱ��������start����
		 */
		producer.start();

		/**
		 * ������δ������һ��Producer������Է��Ͷ��topic�����tag����Ϣ��
		 * ע�⣺send������ͬ�����ã�ֻҪ�����쳣�ͱ�ʶ�ɹ������Ƿ��ͳɹ�Ҳ�ɻ��ж���״̬��<br>
		 * ������Ϣд��Master�ɹ�������Slave���ɹ������������Ϣ���ڳɹ������Ƕ��ڸ���Ӧ���������Ϣ�ɿ���Ҫ�󼫸ߣ�<br>
		 * ��Ҫ������������������⣬��Ϣ���ܻ���ڷ���ʧ�ܵ������ʧ��������Ӧ��������
		 */

		try {
			Message msg = new Message(topic,// topic
					tag,// tag
					"master",// key
					(body).getBytes());// body
			SendResult sendResult = producer.send(msg);
			System.out.println(sendResult);
		} catch (Exception e) {
			e.printStackTrace();
			TimeUnit.MILLISECONDS.sleep(1000);
		}

		/**
		 * Ӧ���˳�ʱ��Ҫ����shutdown��������Դ���ر��������ӣ���MetaQ��������ע���Լ�
		 * ע�⣺���ǽ���Ӧ����JBOSS��Tomcat���������˳����������shutdown����
		 */
		producer.shutdown();
	}
}
