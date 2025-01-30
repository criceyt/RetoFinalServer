package service;

import G3.crud.crypto.EmailServicio;
import G3.crud.crypto.Hash;
import G3.crud.crypto.Servidor;
import G3.crud.entities.Persona;
import G3.crud.entities.Persona_;
import G3.crud.entities.Trabajador;
import G3.crud.entities.Usuario;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Security;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("persona")
public class PersonaFacadeREST extends AbstractFacade<Persona> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(PersonaFacadeREST.class.getName());

    public PersonaFacadeREST() {
        super(Persona.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Persona entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Persona entity) {
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
    public Persona find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("reiniciarContrasena/{email}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response resetPassword(@PathParam("email") String email) {
        Query query;
        Persona pers;  // Usamos Persona en lugar de Usuario
        String contrasena;
        String subject, text;
        EmailServicio emailServicio = new EmailServicio();  // Instanciamos el servicio de email

        if (email == null || email.isEmpty()) {
            LOGGER.log(Level.INFO, "UserRESTful service: invalid email {0}.", email);
            return Response.status(Response.Status.BAD_REQUEST).entity("Los parámetros no pueden estar vacíos").build();
        }

        try {
            query = em.createNamedQuery("findEmailPersona");
            query.setParameter("email", email);
            pers = (Persona) query.getSingleResult();  // Casting a Persona

            if (pers == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("El email no está asociado a ninguna persona").build();
            }

            LOGGER.log(Level.INFO, "UserRESTful service: resetting password for {0}.", email);

            // Generamos la nueva contraseña
            contrasena = EmailServicio.generateRandomPassword().toString();  // Usamos el generador de contraseñas
            pers.setContrasenaReset(Hash.hashText(contrasena));  // Establecemos la nueva contraseña en la persona
            super.edit(pers);  // Guardamos la persona actualizada

            // Definimos el asunto y el cuerpo del correo
            subject = "Solicitud de restablecimiento de contraseña";
            text = "Su nueva contraseña es: " + contrasena;

            // Enviamos el correo al usuario con la nueva contraseña
            emailServicio.sendEmail(pers.getEmail(), contrasena, text, subject);

            // Retornamos una respuesta exitosa
            return Response.ok("La contraseña ha sido restablecida y se ha enviado un correo con la nueva contraseña").build();

        } catch (NotFoundException ex) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity("El correo no coincide con ninguna persona").build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception updating password for {0}.", ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }

    @GET
    @Path("inicioSesionPersona/{email}/{contrasena}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response inicioSesionPersona(@PathParam("email") String email, @PathParam("contrasena") String contrasena) {

        Persona persona = null;
        try {
            LOGGER.log(Level.INFO, "UserRESTful service: find user by email and password");
            contrasena = URLDecoder.decode(contrasena, "UTF-8");
            contrasena = contrasena.replace(" ", "+");
            contrasena = Servidor.desencriptarContraseña(contrasena);
            contrasena = Hash.hashText(contrasena);  // Hashear la contraseña

            // Realizar la consulta en la base de datos
            persona = (Persona) em.createNamedQuery("inicioSesionPersona")
                    .setParameter("email", email)
                    .setParameter("contrasena", contrasena)
                    .getSingleResult();

            LOGGER.log(Level.INFO, "Clase devuelta por JPA: " + persona.getClass());

            // Determinar el tipo de persona y devolverlo
            if (persona instanceof Usuario) {
                return Response.ok((Usuario) persona).build();  // Retorna Usuario
            } else if (persona instanceof Trabajador) {
                return Response.ok((Trabajador) persona).build();  // Retorna Trabajador
            }

        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "UserRESTful service: No user found with provided email and password");
            return Response.status(Response.Status.NOT_FOUND).entity("No user found").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception reading user by email and password", e);
            throw new InternalServerErrorException(e);
        }

        // Si no es un Usuario ni un Trabajador, devolver una Persona general
        return Response.ok(persona).build();
    }

    /*
    @GET
    @Path("inicioSesionPersona/{email}/{contrasena}")
    @Produces({"application/xml"})
    public Response inicioSesionPersona(@PathParam("email") String email, @PathParam("contrasena") String contrasena) {
        Persona persona = null;
        try {

            LOGGER.log(Level.INFO, "UserRESTful service: find user by email and password");

            // Realizar la consulta en la base de datos (utilizando Named Query en JPA)
            persona = (Persona) em.createNamedQuery("inicioSesionPersona")
                    .setParameter("email", email)
                    .setParameter("contrasena", contrasena)
                    .getSingleResult();

            LOGGER.log(Level.INFO, "Clase devuelta por JPA: " + persona.getClass());

            // Si la persona es un Usuario o Trabajador, se devuelve el tipo correspondiente
            if (persona instanceof Usuario) {
                return Response.ok((Usuario) persona).build();  // Retorna Usuario
            } else if (persona instanceof Trabajador) {
                return Response.ok((Trabajador) persona).build();  // Retorna Trabajador
            }

        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "UserRESTful service: No user found with provided email and password");
            // Si no se encuentra el usuario, se devuelve un 404
            return Response.status(Response.Status.NOT_FOUND).entity("No user found").build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception reading user by email and password", e);
            // En caso de error, se lanza un 500 (Error del servidor)
            throw new InternalServerErrorException(e);
        }

        // En caso de que no sea ni Usuario ni Trabajador, se devuelve una Persona general
        return Response.ok(persona).build();  // Retorna Persona por defecto
    }*/
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
