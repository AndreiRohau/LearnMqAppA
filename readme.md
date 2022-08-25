REQUEST-REPLY pattern

Producer waits until request is processed and returned, therefore no Listeners required

Need only one queue for requests (request channel).

Replies go to temporary queue, which is destroyed after response
mq config xml is not touched.

required (apache-activemq-5.17.1-bin): https://activemq.apache.org/components/classic/download/
![](requestor-replier(temp-Q).png)
