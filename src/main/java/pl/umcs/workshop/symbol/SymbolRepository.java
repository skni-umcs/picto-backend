package pl.umcs.workshop.symbol;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.umcs.workshop.group.Group;

public interface SymbolRepository extends JpaRepository<Symbol, Long> {
  List<Symbol> findAllByRoundsId(Long roundId);

  List<Symbol> findAllByUsersId(Long userId);

  List<Symbol> findAllByRoundsIdAndGroupId(Long roundId, Long id);

  List<Symbol> findAllByGamesId(Long gameId);
}
