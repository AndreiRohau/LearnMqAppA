REQUEST-PERLY pattern

Producer waits until request is processed and returned, therefore no Listeners required
one queue for requests.

Replies go to temporary queue, which is destroyed after response
mq config xml is not touched.

required (apache-activemq-5.17.1-bin): https://activemq.apache.org/components/classic/download/
![](requestor-replier(temp-Q).png)
