package tecnm.ito.examendsos.Estudiantes;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {
    private EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public EstudianteModel obtenerEstudiante(String numero){
        return repository.findByNumeroControl(numero);
    }

    public EstudianteModel guardar(EstudianteModel model){
        return repository.save(model);
    }

    public List<EstudianteModel> traer(){
        return repository.findAll();
    }
}
