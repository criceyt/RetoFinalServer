/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Mantenimiento;
import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("g3.crud.entities.mantenimiento")
public class MantenimientoFacadeREST extends AbstractFacade<Mantenimiento> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public MantenimientoFacadeREST() {
        super(Mantenimiento.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Mantenimiento entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Mantenimiento entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Mantenimiento find(@PathParam("id") Long id) {
        return super.find(id);
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    /*
     // Filtrado por DatePicker para Mantenimiento
    @GET
    @Path("fechaFinalizacion/{fechaFinalizacion}")
    @Produces({"application/xml"})
    public List<Mantenimiento> filtradoPorDatePickerMantenimiento(@PathParam("fechaFinalizacion") String fechaFinalizacion) {
        List<Mantenimiento> mantenimientos = null;
        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(fechaFinalizacion);

            LOGGER.log(Level.INFO, "UserRESTful service: find users by profile {0}.", parseo);
            mantenimientos = em.createNamedQuery("filtradoPorDatePickerMantenimiento")
                    .setParameter("fechaFinalizacion", parseo)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return mantenimientos;
    }
    */
}
