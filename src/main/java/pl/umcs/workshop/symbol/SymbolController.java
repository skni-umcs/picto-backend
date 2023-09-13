package pl.umcs.workshop.symbol;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("symbol/")
public class SymbolController {
  @Autowired private SymbolService symbolService;

  @GetMapping("all")
  public List<Symbol> getAllSymbols() {
    return symbolService.getAllSymbols();
  }
}
