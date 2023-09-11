package pl.umcs.workshop.symbol;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, Long> {
  List<Symbol> findAllByRoundsId(Long roundId);

  List<Symbol> findAllByUsersId(Long userId);

  List<Symbol> findAllByRoundsIdAndGroupId(Long roundId, Long id);

  List<Symbol> findAllByGameId(Long gameId);
}
