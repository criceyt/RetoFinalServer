package service;

import G3.crud.entities.Mantenimiento;
import static G3.crud.entities.Mantenimiento_.vehiculo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;

@Stateless
@Path("mantenimiento")
public class MantenimientoFacadeREST extends AbstractFacade<Mantenimiento> {

    private Logger LOGGER = Logger.getLogger(VehiculoFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public MantenimientoFacadeREST() {
        super(Mantenimiento.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Mantenimiento entity) throws BadRequestException, InternalServerErrorException {
        try {
            if (entity == null) {
                LOGGER.log(Level.SEVERE, "El objeto Mantenimiento recibido es nulo.");
                throw new BadRequestException("El objeto Mantenimiento es nulo.");
            }
            super.create(entity);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el mantenimiento", e);
            throw new InternalServerErrorException(e);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Mantenimiento entity) throws BadRequestException, NotFoundException, InternalServerErrorException {
        try {
            if (entity == null || id == null) {
                LOGGER.log(Level.SEVERE, "El objeto Mantenimiento o el ID recibido es nulo.");
                throw new BadRequestException("El objeto Mantenimiento o el ID es nulo.");
            }
            Mantenimiento existingMantenimiento = super.find(id);
            if (existingMantenimiento == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el mantenimiento con ID " + id);
                throw new NotFoundException("Mantenimiento no encontrado.");
            }
            super.edit(entity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al editar el mantenimiento con ID " + id, e);
            throw new InternalServerErrorException(e);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) throws NotFoundException, InternalServerErrorException {
        try {
            Mantenimiento existingMantenimiento = super.find(id);
            if (existingMantenimiento == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el mantenimiento con ID " + id);
                throw new NotFoundException("Mantenimiento no encontrado.");
            }
            super.remove(existingMantenimiento);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar el mantenimiento con ID " + id, e);
            throw new InternalServerErrorException(e);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Mantenimiento find(@PathParam("id") Long id) throws NotFoundException {
        Mantenimiento mantenimiento = super.find(id);
        if (mantenimiento == null) {
            LOGGER.log(Level.SEVERE, "No se encuentra el mantenimiento con ID " + id);
            throw new NotFoundException("Mantenimiento no encontrado.");
        }
        return mantenimiento;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mantenimiento> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mantenimiento> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    // Filtrado por DatePicker para Mantenimientos
    @GET
    @Path("fechaFinalizacion/{fechaFinalizacion}")
    @Produces({"application/xml"})
    public List<Mantenimiento> filtradoPorDatePickerMantenimiento(@PathParam("fechaFinalizacion") String fechaFinalizacion) throws NotFoundException ,InternalServerErrorException {
        List<Mantenimiento> mantenimientos = null;

        if (fechaFinalizacion == null) {
            LOGGER.log(Level.SEVERE, "La fechaFinalizacion es nula");
            throw new BadRequestException();
        }

        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(fechaFinalizacion);

            LOGGER.log(Level.INFO, "Buscando mantenimientos en los que la fecha de finalizacion sea igual a la introducida", parseo);
            mantenimientos = em.createNamedQuery("filtradoPorDatePickerMantenimiento")
                    .setParameter("fechaFinalizacion", parseo)
                    .getResultList();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Error al intentar parsear la fechaFinalizacion");
            throw new InternalServerErrorException(e);
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun mantenimiento con esa fecha de finalizacion");
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return mantenimientos;
    }

    // Filtrar por MantenimientoExitoso
    @GET
    @Path("mantenimientoExitoso/{mantenimientoExitoso}")
    @Produces({"application/xml"})
    public List<Mantenimiento> filtrarPorMantenimientoExitoso(@PathParam("mantenimientoExitoso") Boolean mantenimientoExitoso) throws NotFoundException ,InternalServerErrorException {
        List<Mantenimiento> mantenimientos = null;
        try {
            LOGGER.log(Level.INFO, "Buscando mantenimientos en los que el mantenimiento haya sido exitoso", mantenimientoExitoso);
            mantenimientos = em.createNamedQuery("filtrarPorMantenimientoExitoso")
                    .setParameter("mantenimientoExitoso", mantenimientoExitoso)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun mantenimiento con un mantenimiento exitoso");
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return mantenimientos;
    }

    // Filtrar por FechaFinalizacion para Abajo
    @GET
    @Path("fechaFin/{fechaFin}")
    @Produces({"application/xml"})
    public List<Mantenimiento> filtrarPorFechaFinParaAbajo(@PathParam("fechaFin") String fechaFin) throws NotFoundException ,InternalServerErrorException {
        List<Mantenimiento> mantenimientos = null;

        if (fechaFin == null) {
            LOGGER.log(Level.SEVERE, "La fechaFin es nula");
            throw new BadRequestException();
        }
        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(fechaFin);

            LOGGER.log(Level.INFO, "Buscando mantenimientos anteriores a la fecha introducida", parseo);
            mantenimientos = em.createNamedQuery("filtrarPorFechaFin")
                    .setParameter("fechaFin", parseo)
                    .getResultList();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Error al intentar parsear la fechaFin");
            throw new InternalServerErrorException(e);
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun mantenimiento por debajo de la fecha introducida");
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return mantenimientos;
    }

    // Filtrar por IdVehiculo
    @GET
    @Path("idVehiculo/{idVehiculo}")
    @Produces({"application/xml"})
    public List<Mantenimiento> filtrarPorIdVehiculo(@PathParam("idVehiculo") Long idVehiculo) throws NotFoundException ,InternalServerErrorException {
        List<Mantenimiento> mantenimientos = null;

        if (idVehiculo == null) {
            LOGGER.log(Level.SEVERE, "El id del Vehiculo es nulo");
            throw new BadRequestException();
        }

        try {
            LOGGER.log(Level.INFO, "Buscando mantenimientos por id de un vehiculo", idVehiculo);
            mantenimientos = em.createNamedQuery("buscarMantenimientoMatricula")
                    .setParameter("idVehiculo", idVehiculo)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "ERROR 404, El id de vehiculo introducido no tiene ningun mantenimiento asociado");
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return mantenimientos;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}

