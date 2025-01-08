/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Proveedor;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
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
@Path("g3.crud.entities.proveedor")
public class ProveedorFacadeREST extends AbstractFacade<Proveedor> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public ProveedorFacadeREST() {
        super(Proveedor.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Proveedor entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Proveedor entity) {
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
    public Proveedor find(@PathParam("id") Long id) {
        return super.find(id);
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    // Filtrado por DatePicker para Proveedores
    @GET
    @Path("ultimaActividad/{ultimaActividad}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorDatePickerProveedores(@PathParam("ultimaActividad") String ultimaActividad) {
        List<Proveedor> proveedores = null;
        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(ultimaActividad);

            LOGGER.log(Level.INFO, "UserRESTful service: find users by profile {0}.", parseo);
            proveedores = em.createNamedQuery("filtradoPorDatePickerProveedores")
                    .setParameter("ultimaActividad", parseo)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return proveedores;
    }
    
    
}
