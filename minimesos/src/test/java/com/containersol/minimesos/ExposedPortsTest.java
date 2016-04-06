package com.containersol.minimesos;

import com.containersol.minimesos.cluster.MesosCluster;
import com.containersol.minimesos.config.ConsulConfig;
import com.containersol.minimesos.config.MarathonConfig;
import com.containersol.minimesos.config.MesosAgentConfig;
import com.containersol.minimesos.config.MesosMasterConfig;
import com.containersol.minimesos.main.factory.MesosClusterContainersFactory;
import com.containersol.minimesos.marathon.MarathonContainer;
import com.containersol.minimesos.mesos.ClusterArchitecture;
import com.containersol.minimesos.mesos.ConsulContainer;
import com.containersol.minimesos.mesos.MesosAgentContainer;
import com.containersol.minimesos.mesos.MesosMasterContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExposedPortsTest {
    private static final boolean EXPOSED_PORTS = true;

    private MesosCluster cluster;

    @Before
    public void beforeTest() {

        MesosMasterConfig masterConfig = new MesosMasterConfig();
        MesosAgentConfig agentConfig = new MesosAgentConfig();
        ConsulConfig consulConfig = new ConsulConfig();

        ClusterArchitecture architecture = new ClusterArchitecture.Builder()
                .withZooKeeper()
                .withMaster(zooKeeper -> new MesosMasterContainer(zooKeeper, masterConfig))
                .withAgent(zooKeeper -> new MesosAgentContainer(zooKeeper, agentConfig))
                .withMarathon(zooKeeper -> new MarathonContainer(zooKeeper, marathonConfig))
                .withConsul(new ConsulContainer(consulConfig))
                .build();

        cluster = new MesosCluster(architecture.getClusterConfig(), architecture.getClusterContainers().getContainers());

        cluster.setExposedHostPorts(EXPOSED_PORTS);
        cluster.start();

    }

    @After
    public void afterTest() {
        if (cluster != null) {
            cluster.stop();
        }
    }

    @Test
    public void testLoadCluster() {
        String clusterId = cluster.getClusterId();
        MesosCluster cluster = MesosCluster.loadCluster(clusterId, new MesosClusterContainersFactory());

        assertTrue("Deserialize cluster is expected to remember exposed ports setting", cluster.isExposedHostPorts());
    }

}
