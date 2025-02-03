package service;

import G3.crud.entities.Proveedor;
import G3.crud.entities.TipoVehiculo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Clase que proporciona la lógica para interactuar con la entidad Proveedor.
 * Permite realizar operaciones CRUD sobre los proveedores, así como consultas 
 * avanzadas utilizando filtros.
 * 
 * @author 2dam
 */
@Stateless
@Path("proveedor")
public class ProveedorFacadeREST extends AbstractFacade<Proveedor> {

    private Logger LOGGER = Logger.getLogger(VehiculoFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public ProveedorFacadeREST() {
        super(Proveedor.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Proveedor entity) throws BadRequestException, InternalServerErrorException {
        try {
            if (entity == null) {
                LOGGER.log(Level.SEVERE, "El objeto Proveedor recibido es nulo.");
                throw new BadRequestException("El objeto Proveedor es nulo.");
            }
            super.create(entity);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el proveedor", e);
            throw new InternalServerErrorException(e);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Proveedor entity) throws BadRequestException, NotFoundException, InternalServerErrorException {
        try {
            if (entity == null || id == null) {
                LOGGER.log(Level.SEVERE, "El objeto Proveedor o el ID recibido es nulo.");
                throw new BadRequestException("El objeto Proveedor o el ID es nulo.");
            }
            Proveedor existingProveedor = super.find(id);
            if (existingProveedor == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el proveedor con ID " + id);
                throw new NotFoundException("Proveedor no encontrado.");
            }
            super.edit(entity);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al editar el proveedor con ID " + id, e);
            throw new InternalServerErrorException(e);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) throws NotFoundException, InternalServerErrorException {
        try {
            Proveedor existingProveedor = super.find(id);
            if (existingProveedor == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el proveedor con ID " + id);
                throw new NotFoundException("Proveedor no encontrado.");
            }
            super.remove(existingProveedor);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar el proveedor con ID " + id, e);
            throw new InternalServerErrorException(e);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Proveedor find(@PathParam("id") Long id) throws NotFoundException {
        Proveedor proveedor = super.find(id);
        if (proveedor == null) {
            LOGGER.log(Level.SEVERE, "No se encuentra el proveedor con ID " + id);
            throw new NotFoundException("Proveedor no encontrado.");
        }
        return proveedor;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Proveedor> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Proveedor> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    // Filtrado por DatePicker para Proveedores
    @GET
    @Path("ultimaActividad/{ultimaActividad}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorDatePickerProveedores(@PathParam("ultimaActividad") String ultimaActividad) throws NotFoundException, InternalServerErrorException {
        List<Proveedor> proveedores = null;

        if (ultimaActividad == null || ultimaActividad.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'ultimaActividad' es nulo o está vacío.");
            throw new BadRequestException("El parámetro 'ultimaActividad' no puede ser nulo o vacío.");
        }

        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(ultimaActividad);

            LOGGER.log(Level.INFO, "Buscando proveedores cuya última actividad sea igual a la introducida", parseo);
            proveedores = em.createNamedQuery("filtradoPorDatePickerProveedores")
                    .setParameter("ultimaActividad", parseo)
                    .getResultList();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Error al intentar parsear la ultimaActividad", e);
            throw new InternalServerErrorException("Error al parsear la fecha.", e);
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun proveedor con esa fecha de ultimaActividad", e);
            throw new NotFoundException("Proveedor no encontrado con esa fecha.");
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO, "ERROR 500, Error interno en el servidor", e.getMessage());
            throw new InternalServerErrorException("Error interno en el servidor.", e);
        }
        return proveedores;
    }

    // Filtrado por nombre
    @GET
    @Path("nombreProveedor/{nombreProveedor}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorNombre(@PathParam("nombreProveedor") String nombreProveedor) throws NotFoundException, InternalServerErrorException {
        List<Proveedor> proveedores = null;

        if (nombreProveedor == null || nombreProveedor.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'nombreProveedor' es nulo o está vacío.");
            throw new BadRequestException("El parámetro 'nombreProveedor' no puede ser nulo o vacío.");
        }

        try {
            LOGGER.log(Level.INFO, "Buscando proveedores por nombre", nombreProveedor);
            proveedores = em.createNamedQuery("filtradoPorNombre")
                    .setParameter("nombreProveedor", nombreProveedor)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, El nombre del proveedor introducido no existe", e);
            throw new NotFoundException("Proveedor no encontrado con ese nombre.");
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO, "ERROR 500, Error interno en el servidor", e.getMessage());
            throw new InternalServerErrorException("Error interno en el servidor.", e);
        }
        return proveedores;
    }

    // Filtrado por TipoVehiculo
    @GET
    @Path("tipoVehiculo/{tipoVehiculo}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorTipoVehiculo(@PathParam("tipoVehiculo") String tipoVehiculo) throws NotFoundException, InternalServerErrorException {
        List<Proveedor> proveedores = null;

        if (tipoVehiculo == null || tipoVehiculo.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'tipoVehiculo' es nulo o está vacío.");
            throw new BadRequestException("El parámetro 'tipoVehiculo' no puede ser nulo o vacío.");
        }

        try {
            // Convertir el String recibido a un valor del enum TipoVehiculo
            TipoVehiculo tipo = TipoVehiculo.fromString(tipoVehiculo);

            LOGGER.log(Level.INFO, "Buscando proveedores por tipo de vehículo", tipo);
            proveedores = em.createNamedQuery("filtradoPorTipoVehiculo")
                    .setParameter("tipoVehiculo", tipo)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Tipo de vehículo no válido", tipoVehiculo);
            throw new WebApplicationException("Tipo de vehículo no válido.", Response.Status.BAD_REQUEST);
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, El tipo de vehículo introducido no existe", e);
            throw new NotFoundException("Proveedor no encontrado con ese tipo de vehículo.");
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO, "ERROR 500, Error interno en el servidor", e.getMessage());
            throw new InternalServerErrorException("Error interno en el servidor.", e);
        }
        return proveedores;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
