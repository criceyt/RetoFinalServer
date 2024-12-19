package G3.crud.services;

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
        resources.add(G3.crud.services.CompraFacadeREST.class);
        resources.add(G3.crud.services.MantenimientoFacadeREST.class);
        resources.add(G3.crud.services.PersonaFacadeREST.class);
        resources.add(G3.crud.services.ProveedorFacadeREST.class);
        resources.add(G3.crud.services.TrabajadorFacadeREST.class);
        resources.add(G3.crud.services.UsuarioFacadeREST.class);
        resources.add(G3.crud.services.VehiculoFacadeREST.class);
    }
}
