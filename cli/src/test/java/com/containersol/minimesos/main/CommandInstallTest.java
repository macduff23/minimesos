package com.containersol.minimesos.main;

import com.containersol.minimesos.cluster.ClusterRepository;
import com.containersol.minimesos.cluster.Marathon;
import com.containersol.minimesos.cluster.MesosCluster;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandInstallTest {

    private Marathon marathon;

    private MesosCluster mesosCluster;

    private ClusterRepository repository;

    private CommandInstall command;

    @Before
    public void before() {
        marathon = mock(Marathon.class);

        mesosCluster = mock(MesosCluster.class);
        when(mesosCluster.getMarathon()).thenReturn(marathon);

        repository = mock(ClusterRepository.class);
        when(repository.loadCluster(any())).thenReturn(mesosCluster);

        command = new CommandInstall();
        command.repository = repository;
    }

    @Test
    public void testInstallMarathonFile() throws IOException {
        // Given
        command.marathonFile = "src/test/resources/app.json";

        // When
        command.execute();

        // Then
        verify(marathon).deployApp(IOUtils.toString(new FileReader(command.marathonFile)));
    }

    @Test
    public void testInstallMarathonApp() throws IOException {
        // Given
        command.app = "src/test/resources/app.json";

        // When
        command.execute();

        // Then
        verify(marathon).deployApp(IOUtils.toString(new FileReader(command.app)));
    }

    @Test
    public void testInstallMarathonGroup() throws IOException {
        // Given
        command.group = "src/test/resources/group.json";

        // When
        command.execute();

        // Then
        verify(marathon).deployGroup(IOUtils.toString(new FileReader(command.group)));
    }

}
