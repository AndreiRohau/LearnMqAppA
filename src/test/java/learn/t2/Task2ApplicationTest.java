package learn.t2;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.zookeeper.Environment;
import org.junit.Before;
import org.junit.Test;
import org.learn.t2.Task2Application;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.mockito.Mockito.when;

public class Task2ApplicationTest {
    private Properties props;

    @Spy
    @InjectMocks
    private Task2Application myClass;

    @Mock
    private Environment environment;

    @Before
    public void init() {
        myClass = new Task2Application();

        props = new Properties();
        props.put(APPLICATION_ID_CONFIG, "my-test-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");

        Environment.list().add(new Environment.Entry("kafka.t20.name", "task1-1"));
    }

    @Test
    public void test() {
//        StreamsBuilder builder = new StreamsBuilder();
//
//        MockApiProcessorSupplier<String, String, Void, Void> supplier = new MockfApiProcessorSupplier<>();
//        myClass.branches(builder).process(supplier);
//
//        try (TopologyTestDriver driver = new TopologyTestDriver(builder.build(), props)) {
//            TestInputTopic<String, String> inputTopic = driver.createInputTopic("myTopic", Serdes.String().serializer(), Serdes.String().serializer());
//            inputTopic.pipeInput("testKey", "testValue");
//        } catch (Exception e) {
//         throw new RuntimeException("hey my runtime exception !");
//        }
//
//        ArrayList<KeyValueTimestamp<String, String>> resultValues = supplier.capturedProcessors(3).get(2).processed();
//        assertEquals(1, resultValues.size());
//        assertEquals("testValue", (resultValues.get(0).value()));
    }

}
