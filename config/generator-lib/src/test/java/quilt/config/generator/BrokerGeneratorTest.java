package quilt.config.generator;

import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IReplicationController;
import org.junit.Test;
import quilt.config.model.Broker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author lulf
 */
public class BrokerGeneratorTest {
    @Test
    public void testGenerator() {
        BrokerGenerator generator = new BrokerGenerator(null);
        IReplicationController controller = generator.generate(new Broker("testaddr", true, false));

        assertThat(controller.getLabels().get("role"), is("broker"));
        assertThat(controller.getContainers().size(), is(2));

        IContainer broker = controller.getContainer("broker");
        assertThat(broker.getPorts().size(), is(1));
        assertThat(broker.getPorts().iterator().next().getContainerPort(), is(5673));
        assertThat(broker.getEnvVars().get("QUEUE_NAME"), is("testaddr"));
        assertThat(broker.getVolumeMounts().size(), is(1));

        IContainer router = controller.getContainer("router");
        assertThat(router.getPorts().size(), is(1));
        assertThat(router.getPorts().iterator().next().getContainerPort(), is(5672));
    }
}