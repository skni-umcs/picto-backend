package pl.umcs.workshop.symbol;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SymbolService {
  @Autowired private SymbolRepository symbolRepository;

  public List<Symbol> getAllSymbols() {
    return symbolRepository.findAll();
  }
}
