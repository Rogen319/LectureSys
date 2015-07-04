package rocket;

import java.util.List;

import servlet.AddSchoolServlet;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import function.ConflictionCheck;

public class Consumer {
	/**
	 * ��ǰ������PushConsumer�÷���ʹ�÷�ʽ���û��о�����Ϣ��RocketMQ�������Ƶ���Ӧ�ÿͻ��ˡ�<br>
	 * ����ʵ��PushConsumer�ڲ���ʹ�ó���ѯPull��ʽ��MetaQ����������Ϣ��Ȼ���ٻص��û�Listener����<br>
	 */
	public static void main(String[] args) throws InterruptedException,
			MQClientException {

		String tag = "tagA";
		/**
		 * һ��Ӧ�ô���һ��Consumer����Ӧ����ά���˶��󣬿�������Ϊȫ�ֶ�����ߵ���<br>
		 * ע�⣺ConsumerGroupName��Ҫ��Ӧ������֤Ψһ
		 */
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				"ConsumerGroupName");
		consumer.setNamesrvAddr("10.131.252.221:9876");
		// consumer.setInstanceName("Consumber");

		/**
		 * ����ָ��topic��tags�ֱ����TagA��TagC��TagD
		 */
		consumer.subscribe("G4-addSchool", tag);
		consumer.subscribe("G4-addSchoolConflictionResult", tag);
		/**
		 * ����ָ��topic��������Ϣ<br>
		 * ע�⣺һ��consumer������Զ��Ķ��topic
		 */
		consumer.subscribe("G4-addSchoolConfliction", "*");

		consumer.registerMessageListener(new MessageListenerConcurrently() {

			/**
			 * Ĭ��msgs��ֻ��һ����Ϣ������ͨ������consumeMessageBatchMaxSize����������������Ϣ
			 */
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getName()
						+ " Receive New Messages: " + msgs.size());

				MessageExt msg = msgs.get(0);
				if (msg.getTopic().equals("G4-addSchool")) {
					System.out.println("sucess add School");
				} else if (msg.getTopic().equals(
						"G4-addSchoolConflictionResult")) {
					AddSchoolServlet.addSchoolWait = new String(msg.getBody());
				} else if (msg.getTopic().equals("G4-addSchoolConfliction")) {
					String tmp = new String(msg.getBody());
					String result = ConflictionCheck.addSchoolConfliction(tmp);
					try {
						Producer p = new Producer("ResultProducer",
								"G4-addSchoolConflictionResult", "TagM", result);
					} catch (MQClientException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		/**
		 * Consumer������ʹ��֮ǰ����Ҫ����start��ʼ������ʼ��һ�μ���<br>
		 */
		consumer.start();

		System.out.println("Consumer Started.");
	}
}
