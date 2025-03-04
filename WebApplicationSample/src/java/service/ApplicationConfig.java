package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author 2dam
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(service.CompraFacadeREST.class);
        resources.add(service.MantenimientoFacadeREST.class);
        resources.add(service.PersonaFacadeREST.class);
        resources.add(service.ProveedorFacadeREST.class);
        resources.add(service.TrabajadorFacadeREST.class);
        resources.add(service.UsuarioFacadeREST.class);
        resources.add(service.VehiculoFacadeREST.class);
    }
   
}

