package pl.umcs.workshop.group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
  List<Group> findAllByType(String type);
}
