package service;

import G3.crud.entities.Vehiculo;
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
@Path("vehiculo")
public class VehiculoFacadeREST extends AbstractFacade<Vehiculo> {

    private Logger LOGGER = Logger.getLogger(VehiculoFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public VehiculoFacadeREST() {
        super(Vehiculo.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Vehiculo entity) throws BadRequestException, InternalServerErrorException {
        try {
            if (entity == null) {
                LOGGER.log(Level.SEVERE, "El objeto Vehiculo recibido es nulo.");
                throw new BadRequestException("El objeto Vehiculo es nulo.");
            }
            super.create(entity);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el vehículo", e);
            throw new InternalServerErrorException("Error interno al crear el vehículo.", e);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Vehiculo entity) throws BadRequestException, NotFoundException, InternalServerErrorException {
        try {
            if (entity == null || id == null) {
                LOGGER.log(Level.SEVERE, "El objeto Vehiculo o el ID recibido es nulo.");
                throw new BadRequestException("El objeto Vehiculo o el ID es nulo.");
            }
            Vehiculo existingVehiculo = super.find(id);
            if (existingVehiculo == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el vehículo con ID " + id);
                throw new NotFoundException("Vehículo no encontrado.");
            }
            super.edit(entity);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al editar el vehículo con ID " + id, e);
            throw new InternalServerErrorException("Error interno al editar el vehículo con ID " + id, e);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) throws NotFoundException, InternalServerErrorException {
        try {
            Vehiculo existingVehiculo = super.find(id);
            if (existingVehiculo == null) {
                LOGGER.log(Level.SEVERE, "No se encuentra el vehículo con ID " + id);
                throw new NotFoundException("Vehículo no encontrado.");
            }
            super.remove(existingVehiculo);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar el vehículo con ID " + id, e);
            throw new InternalServerErrorException("Error interno al eliminar el vehículo con ID " + id, e);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Long id) throws NotFoundException {
        Vehiculo vehiculo = super.find(id);
        if (vehiculo == null) {
            LOGGER.log(Level.SEVERE, "No se encuentra el vehículo con ID " + id);
            throw new NotFoundException("Vehículo no encontrado.");
        }
        return vehiculo;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() {
        try {
            return super.findAll();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        try {
            return String.valueOf(super.count());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    // Filtrado por Marca
    @GET
    @Path("marca/{marca}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPorMarca(@PathParam("marca") String marca) throws NotFoundException, InternalServerErrorException {
        List<Vehiculo> vehiculos = null;
        if (marca == null || marca.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'marca' es nulo o vacío.");
            throw new BadRequestException("El parámetro 'marca' no puede ser nulo o vacío.");
        }
        try {
            LOGGER.log(Level.INFO, "Buscando vehículos por marca", marca);
            vehiculos = em.createNamedQuery("filtradoPorMarcaVehiculo")
                    .setParameter("marca", marca)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No se ha encontrado ningún vehículo con esa marca", e);
            throw new NotFoundException("No se ha encontrado ningún vehículo con esa marca.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al filtrar por marca", e);
            throw new InternalServerErrorException("Error interno al procesar la consulta por marca.", e);
        }
        return vehiculos;
    }

    // Filtrado por Color
    @GET
    @Path("color/{color}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPorColor(@PathParam("color") String color) throws NotFoundException, InternalServerErrorException {
        List<Vehiculo> vehiculos = null;
        if (color == null || color.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'color' es nulo o vacío.");
            throw new BadRequestException("El parámetro 'color' no puede ser nulo o vacío.");
        }
        try {
            LOGGER.log(Level.INFO, "Buscando vehículos por color", color);
            vehiculos = em.createNamedQuery("filtradoPorColorVehiculo")
                    .setParameter("color", color)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No se ha encontrado ningún vehículo con ese color", e);
            throw new NotFoundException("No se ha encontrado ningún vehículo con ese color.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al filtrar por color", e);
            throw new InternalServerErrorException("Error interno al procesar la consulta por color.", e);
        }
        return vehiculos;
    }

    // Filtrado por Precio
    @GET
    @Path("precio/{precio}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPorPrecio(@PathParam("precio") Integer precio) throws NotFoundException, InternalServerErrorException {
        List<Vehiculo> vehiculos = null;
        if (precio == null) {
            LOGGER.log(Level.SEVERE, "El parámetro 'precio' es nulo.");
            throw new BadRequestException("El parámetro 'precio' no puede ser nulo.");
        }
        try {
            LOGGER.log(Level.INFO, "Buscando vehículos por precio", precio);
            vehiculos = em.createNamedQuery("filtradoPorPrecioVehiculo")
                    .setParameter("precio", precio)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No se ha encontrado ningún vehículo con ese precio", e);
            throw new NotFoundException("No se ha encontrado ningún vehículo con ese precio.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al filtrar por precio", e);
            throw new InternalServerErrorException("Error interno al procesar la consulta por precio.", e);
        }
        return vehiculos;
    }

    // Filtrado por Kilómetros
    @GET
    @Path("km/{km}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPorKm(@PathParam("km") Integer km) throws NotFoundException, InternalServerErrorException {
        List<Vehiculo> vehiculos = null;
        if (km == null) {
            LOGGER.log(Level.SEVERE, "El parámetro 'km' es nulo.");
            throw new BadRequestException("El parámetro 'km' no puede ser nulo.");
        }
        try {
            LOGGER.log(Level.INFO, "Buscando vehículos por kilómetros", km);
            vehiculos = em.createNamedQuery("filtradoKmVehiculo")
                    .setParameter("km", km)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "No se ha encontrado ningún vehículo con ese kilometraje", e);
            throw new NotFoundException("No se ha encontrado ningún vehículo con ese kilometraje.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al filtrar por kilómetros", e);
            throw new InternalServerErrorException("Error interno al procesar la consulta por kilómetros.", e);
        }
        return vehiculos;
    }

    // Filtrado por DatePicker para Proveedores
    @GET
    @Path("fechaAlta/{fechaAlta}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoDatePickerVehiculo(@PathParam("fechaAlta") String fechaAlta) throws NotFoundException, InternalServerErrorException {
        List<Vehiculo> vehiculos = null;

        if (fechaAlta == null || fechaAlta.isEmpty()) {
            LOGGER.log(Level.SEVERE, "El parámetro 'ultimaActividad' es nulo o está vacío.");
            throw new BadRequestException("El parámetro 'ultimaActividad' no puede ser nulo o vacío.");
        }

        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(fechaAlta);

            LOGGER.log(Level.INFO, "Buscando proveedores cuya última actividad sea igual a la introducida", parseo);
            vehiculos = em.createNamedQuery("filtradoDatePickerVehiculo")
                    .setParameter("fechaAlta", parseo)
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
        return vehiculos;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
