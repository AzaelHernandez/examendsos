package tecnm.ito.examendsos.Estudiantes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteModel,Long> {
    EstudianteModel findByNumeroControl(String numeroControl);
}
