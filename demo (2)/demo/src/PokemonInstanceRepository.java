import com.example.demo.model.PokemonInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonInstanceRepository extends JpaRepository<PokemonInstance, Long> {
}
