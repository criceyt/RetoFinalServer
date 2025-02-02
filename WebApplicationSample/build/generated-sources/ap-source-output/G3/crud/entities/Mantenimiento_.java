package G3.crud.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Mantenimiento.class)
public abstract class Mantenimiento_ {

	public static volatile SingularAttribute<Mantenimiento, String> descripcion;
	public static volatile SingularAttribute<Mantenimiento, Boolean> mantenimientoExitoso;
	public static volatile SingularAttribute<Mantenimiento, Long> idMantenimiento;
	public static volatile SingularAttribute<Mantenimiento, Vehiculo> vehiculo;
	public static volatile SingularAttribute<Mantenimiento, Date> fechaFinalizacion;

}

