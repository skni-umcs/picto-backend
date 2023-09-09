package pl.umcs.workshop.symbol;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, Long> {
  List<Symbol> findAllByRoundsId(Long roundId);
}
