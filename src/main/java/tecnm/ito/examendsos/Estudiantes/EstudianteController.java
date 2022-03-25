package tecnm.ito.examendsos.Estudiantes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1")
public class EstudianteController {

    private EstudianteService service;

    @Autowired
    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    //OPCIONAL: solo es para ver cuales están
    @GetMapping("/ver")
    public List<EstudianteModel> ver(){
        return service.traer();
    }

    @PostMapping("/ica")
    public ResponseEntity<Map<String,Object>> getICA(@RequestBody EstudianteModel model){
        //Se intenta obtener el estudiante con el numero de control si existe
        EstudianteModel estudiante = service.obtenerEstudiante(model.getNumeroControl());
        //Map donde se guardan los resultados o los errores
        HashMap<String, Object> mapJSON = new HashMap<>();
        //Si no encuentra el estudiante por numero de control, se guardan los datos que se mandan
        if (estudiante == null){
            estudiante = service.guardar(model);
        }else{
            //Si se encuentra, se actualizan los datos
            //si el actual campo está vacío o si el que se acaba de mandar es != null
            if(estudiante.getAltura() == null || model.getAltura() != null){
                estudiante.setAltura(model.getAltura());
            }
            if(estudiante.getCintura() == null || model.getCintura() != null){
                estudiante.setCintura(model.getCintura());
            }
            //Solo se cambia el genero si este está vacío previamente
            if(estudiante.getGenero() == null){
                estudiante.setGenero(model.getGenero());
            }
            //Se guardan los cambios del estudiante
            estudiante = service.guardar(estudiante);
        }
        //Se valida si la altura es válida y si no se manda un error
        Integer altura = estudiante.getAltura();
        if(altura == null || altura == 0){
            mapJSON.put("error", "Falta el valor de altura");
           return new ResponseEntity<>(mapJSON,HttpStatus.BAD_REQUEST);
        }
        //Se valida si la cintura es válida y si no se manda un error
        Integer cintura = estudiante.getCintura();
        if(cintura == null || cintura == 0){
            mapJSON.put("error", "Falta el valor de la cintura");
            return new ResponseEntity<>(mapJSON,HttpStatus.BAD_REQUEST);
        }
        //Se valida si el genero es válido y si no se manda un error
        String genero = estudiante.getGenero();
        if (genero == null || genero == ""){
            mapJSON.put("error", "El genero no existe o no es válido");
            return new ResponseEntity<>(mapJSON,HttpStatus.BAD_REQUEST);
        }
        //Se hace la operacion, es importante el casting ya que ambos son int
        double ica = (double) cintura / altura;

        //Se guardan las opciones que vienen en la tabla de la pagina del ejercicio
        String opciones[] = {"Delgadez severa", "Delgadez leve", "Peso normal", "Sobrepeso",
                "Sobrepeso elevado", "Obesidad mórbida"};
        double hombres[] = {0, 0.35, 0.43, 0.53, 0.58, 0.63};
        double mujeres[] = {0, 0.35, 0.42, 0.49, 0.54, 0.58};

        //La opcion que se va a mandar segun el ICA
        String opcion = "";
        //Dependiendo del genero, se utiliza uno de los arreglos, por default se usará el de hombre
        double [] tipoGenero;
        if (genero == "M"){
            tipoGenero = mujeres;
        } else {
            tipoGenero = hombres;
        }

        //Se recorre el arreglo, y si es mayor o igual el ica al valor obtenido,
        //Se asigna el valor de opciones[], por lo que se queda con el ultimo valor
        //cuando este sea mayor o igual
        for(int i=0; i< opciones.length; i++){
            if(ica >= tipoGenero[i]){
                opcion = opciones[i];
            }
        }
        
        //Se colocan los valores en el map y se devuelven
        mapJSON.put("ica", ica);
        mapJSON.put("resultado", opcion);
        return new ResponseEntity<>(mapJSON, HttpStatus.OK);
    }
}
