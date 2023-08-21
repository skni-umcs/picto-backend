package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.topology.TopologyRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TopologyRepositoryTests {
    @Autowired
    private TopologyRepository topologyRepository;

    @Test
    @Order(value = 1)
    public void saveTopologyTest() {
        Topology topology = Topology.builder()
                .name("Test topology")
                .numberOfSets(3)
                .usersInSet(5)
                .build();

        topologyRepository.save(topology);

        Assertions.assertThat(topology.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getTopologyTest() {
        Topology topology = topologyRepository.findById(1).orElse(null);

        Assertions.assertThat(topology).isNotNull();
        Assertions.assertThat(topology.getId()).isEqualTo(1);
    }

    @Test
    @Order(value = 3)
    public void getListOfAllTopologiesTest() {
        Topology topology = Topology.builder()
                .name("Test topology 2")
                .numberOfSets(2)
                .usersInSet(8)
                .build();

        topologyRepository.save(topology);
        List<Topology> topologies = topologyRepository.findAll();

        Assertions.assertThat(topologies.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void updateTopologyTest() {
        Topology topology = topologyRepository.findById(1).orElse(null);

        Assertions.assertThat(topology).isNotNull();
        Assertions.assertThat(topology.getNumberOfSets()).isEqualTo(3);

        topology.setNumberOfSets(4);

        Topology savedTopology = topologyRepository.save(topology);

        Assertions.assertThat(topology.getNumberOfSets()).isEqualTo(4);
        Assertions.assertThat(savedTopology.getNumberOfSets()).isEqualTo(4);
        Assertions.assertThat(topology.getId()).isEqualTo(savedTopology.getId());
    }

    @Test
    @Order(value = 5)
    public void deleteTopologyTest() {
        Topology topology = topologyRepository.findById(1).orElse(null);

        Assertions.assertThat(topology).isNotNull();
        topologyRepository.delete(topology);

        Topology topologyCheck = null;
        Optional<Topology> topologyOptional = topologyRepository.findById(1);

        if (topologyOptional.isPresent()) {
            topologyCheck = topologyOptional.get();
        }

        Assertions.assertThat(topologyCheck).isNull();
    }
}
