package G3.crud.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vehiculo.class)
public abstract class Vehiculo_ {

	public static volatile SingularAttribute<Vehiculo, String> marca;
	public static volatile SingularAttribute<Vehiculo, Integer> potencia;
	public static volatile SetAttribute<Vehiculo, Proveedor> proveedores;
	public static volatile SingularAttribute<Vehiculo, Integer> km;
	public static volatile SingularAttribute<Vehiculo, Integer> precio;
	public static volatile SetAttribute<Vehiculo, Compra> compras;
	public static volatile SingularAttribute<Vehiculo, Long> idVehiculo;
	public static volatile SetAttribute<Vehiculo, Mantenimiento> mantenimientos;
	public static volatile SingularAttribute<Vehiculo, String> color;
	public static volatile SingularAttribute<Vehiculo, Date> fechaAlta;
	public static volatile SingularAttribute<Vehiculo, String> tipoVehiculo;
	public static volatile SingularAttribute<Vehiculo, String> modelo;

}

