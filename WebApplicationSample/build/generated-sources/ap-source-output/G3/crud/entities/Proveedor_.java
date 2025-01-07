package G3.crud.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Proveedor.class)
public abstract class Proveedor_ {

	public static volatile SingularAttribute<Proveedor, String> nombreProveedor;
	public static volatile SingularAttribute<Proveedor, Date> ultimaActividad;
	public static volatile SingularAttribute<Proveedor, Long> idProveedor;
	public static volatile SingularAttribute<Proveedor, TipoVehiculo> tipoVehiculo;
	public static volatile SingularAttribute<Proveedor, String> especialidad;
	public static volatile SetAttribute<Proveedor, Vehiculo> vehiculos;

}

