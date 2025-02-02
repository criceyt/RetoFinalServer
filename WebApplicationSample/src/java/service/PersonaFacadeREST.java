package service;

import G3.crud.crypto.EmailServicio;
import G3.crud.crypto.Hash;
import G3.crud.crypto.Servidor;
import G3.crud.entities.Persona;
import G3.crud.entities.Persona_;
import G3.crud.entities.Trabajador;
import G3.crud.entities.UpdatePasswordRequest;
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
        Persona pers;
        String nuevaContrasena;
        EmailServicio emailServicio = new EmailServicio();

        if (email == null || email.isEmpty()) {
            LOGGER.log(Level.INFO, "UserRESTful service: invalid email {0}.", email);
            return Response.status(Response.Status.BAD_REQUEST).entity("Los parámetros no pueden estar vacíos").build();
        }

        try {
            query = em.createNamedQuery("findEmailPersona");
            query.setParameter("email", email);
            pers = (Persona) query.getSingleResult();

            if (pers == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("El email no está asociado a ninguna persona").build();
            }

            LOGGER.log(Level.INFO, "UserRESTful service: resetting password for {0}.", email);

            // Generamos y hasheamos la nueva contraseña
            nuevaContrasena = EmailServicio.generateRandomPassword().toString();

            // Guardamos la nueva contraseña en la base de datos
            pers.setContrasena(nuevaContrasena);
            em.merge(pers);
            em.flush(); // Sincronizamos los cambios con la base de datos

            // 📧 **Enviar email con la nueva contraseña**
            String subject = "Restablecimiento de Contraseña";
            String body = "Hola " + pers.getNombreCompleto() + ",\n\n"
                    + "Tu contraseña ha sido restablecida correctamente.\n"
                    + "Tu nueva contraseña es: " + nuevaContrasena + "\n\n"
                    + "Por seguridad, te recomendamos cambiarla lo antes posible.\n\n"
                    + "Saludos,\nEl equipo de soporte.";

            boolean correoEnviado = emailServicio.sendEmail(email, nuevaContrasena, body, subject);

            if (!correoEnviado) {
                LOGGER.log(Level.SEVERE, "UserRESTful service: Error sending email to {0}.", email);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("La contraseña ha sido restablecida, pero hubo un error al enviar el correo.")
                        .build();
            }

            return Response.ok("La contraseña ha sido restablecida y enviada al correo.").build();

        } catch (NoResultException ex) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: No user found with email {0}.", email);
            return Response.status(Response.Status.NOT_FOUND).entity("El correo no coincide con ninguna persona").build();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Error updating password for {0}.", email);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }

    @PUT
    @Path("updatePassword")
    @Consumes(MediaType.APPLICATION_XML)
    public Response updatePassword(UpdatePasswordRequest request) {
        try {
            // Buscar al usuario por email
            Persona usuario = (Persona) em.createNamedQuery("findEmailPersona")
                    .setParameter("email", request.getEmail())
                    .getSingleResult();

            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("<message>Usuario no encontrado</message>")
                        .build();
            }

            // Encriptar la nueva contraseña
            String hashedPassword = Hash.hashText(request.getNewPassword());

            // Actualizar la contraseña en la base de datos
            em.createNamedQuery("reiniciarContrasena")
                    .setParameter("nuevaContrasena", hashedPassword)
                    .setParameter("email", request.getEmail())
                    .executeUpdate();

            // Enviar correo de notificación
            EmailServicio emailServicio = new EmailServicio();
            String subject = "Cambio de contraseña exitoso";
            String body = "Hola, " + usuario.getNombreCompleto() + ".\n\n"
                    + "Tu contraseña ha sido cambiada exitosamente.\n"
                    + "Si no has realizado este cambio, por favor contacta con soporte.";
            emailServicio.sendEmail(request.getEmail(), null, body, subject);

            return Response.ok("<message>Contraseña actualizada con éxito</message>").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("<message>Error al actualizar la contraseña</message>")
                    .build();
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
