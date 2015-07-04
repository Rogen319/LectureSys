package rocket;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

public class PushConsumer {

   /**
    * ��ǰ������PushConsumer�÷���ʹ�÷�ʽ���û��о�����Ϣ��RocketMQ�������Ƶ���Ӧ�ÿͻ��ˡ�<br>
    * ����ʵ��PushConsumer�ڲ���ʹ�ó���ѯPull��ʽ��MetaQ����������Ϣ��Ȼ���ٻص��û�Listener����<br>
    */
   public static void main(String[] args) throws InterruptedException,
           MQClientException {
       /**
        * һ��Ӧ�ô���һ��Consumer����Ӧ����ά���˶��󣬿�������Ϊȫ�ֶ�����ߵ���<br>
        * ע�⣺ConsumerGroupName��Ҫ��Ӧ������֤Ψһ
        */
       DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
               "ConsumerGroupName");
       consumer.setNamesrvAddr("10.131.252.221:9876");
       //consumer.setInstanceName("Consumber");

       /**
        * ����ָ��topic��tags�ֱ����TagA��TagC��TagD
        */
       consumer.subscribe("G4-seleteCourse", "TagA || TagC || TagD");
       /**
        * ����ָ��topic��������Ϣ<br>
        * ע�⣺һ��consumer������Զ��Ķ��topic
        */
       consumer.subscribe("G4-addStudengtInfo", "*");

       consumer.registerMessageListener(new MessageListenerConcurrently() {

           /**
            * Ĭ��msgs��ֻ��һ����Ϣ������ͨ������consumeMessageBatchMaxSize����������������Ϣ
            */
           public ConsumeConcurrentlyStatus consumeMessage(
                   List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
               System.out.println(Thread.currentThread().getName()
                       + " Receive New Messages: " + msgs.size());

               MessageExt msg = msgs.get(0);
               if (msg.getTopic().equals("G4-seleteCourse")) {
                   // ִ��TopicTest1�������߼�
                   if (msg.getTags() != null && msg.getTags().equals("TagA")) {
                       // ִ��TagA������
                       System.out.println(new String(msg.getBody())+"tagA");
                   } else if (msg.getTags() != null
                           && msg.getTags().equals("TagC")) {
                       // ִ��TagC������
                	   System.out.println(new String(msg.getBody())+"tagC");
                   } else if (msg.getTags() != null
                           && msg.getTags().equals("TagD")) {
                       // ִ��TagD������
                	   System.out.println(new String(msg.getBody())+"tagD");
                   }
               } else if (msg.getTopic().equals("G4-addStudengtInfo")) {
                   System.out.println(new String(msg.getBody()));
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
