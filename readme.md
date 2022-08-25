REQUEST-PERLY pattern
Producer waits until request is processed and returned, therefore no Listeners required
one queue for requests
replies goes to temporary queue, which is destroyed after response
mq config xml is not touched
![](requestor-replier(temp-Q).png)
