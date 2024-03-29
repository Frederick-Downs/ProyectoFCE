package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class DT_user
{
	PoolConexion pc = PoolConexion.getInstance(); 
	Connection c = PoolConexion.getConnection();
	private ResultSet rsUsuario;
	
	// Metodo para visualizar todos los usuarios activos
	public ArrayList<Tbl_usuario> listUser()
	{
		ArrayList<Tbl_usuario> listaUsuario = new ArrayList<Tbl_usuario>();
		
		try
		{
			PreparedStatement ps = c.prepareStatement("SELECT * from tbl_usuario where estado<>3", 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
			rsUsuario = ps.executeQuery();
			while(rsUsuario.next())
			{
				Tbl_usuario tus  = new Tbl_usuario();
				tus.setId(rsUsuario.getInt("id"));
				tus.setNombres(rsUsuario.getString("nombres"));
				tus.setApellidos(rsUsuario.getString("apellidos"));
				tus.setCarne(rsUsuario.getString("carne"));
				tus.setContrasena(rsUsuario.getString("contrasena"));
				tus.setCorreo(rsUsuario.getString("correo"));
				tus.setEstado(rsUsuario.getInt("estado"));
				listaUsuario.add(tus);
			}
		}
		catch (Exception e)
		{
			System.out.println("DATOS: ERROR en listUser() "+ e.getMessage());
			e.printStackTrace();
		}
		
		return listaUsuario;
	}
	
	
	public boolean guardarUser(Tbl_usuario tus)
	{
		boolean guardado = false;
		
		try
		{
			this.listUser();
			rsUsuario.moveToInsertRow();
			rsUsuario.updateString("nombres", tus.getNombres());
			rsUsuario.updateString("apellidos", tus.getApellidos());
			rsUsuario.updateString("carne", tus.getCarne());
			rsUsuario.updateString("contrasena", getMd5( tus.getContrasena()));
			rsUsuario.updateString("correo", tus.getCorreo());
			rsUsuario.updateInt("estado", 1);
			rsUsuario.insertRow();
			rsUsuario.moveToCurrentRow();
			guardado = true;
			
		}
		catch (Exception e) 
		{
			System.err.println("ERROR guardarUser(): "+e.getMessage());
			e.printStackTrace();
		}
		
		return guardado;
	}
	
	public boolean modificarUser(Tbl_usuario tus)
	{
		boolean modificado=false;	
		try
		{

			this.listUser();
			rsUsuario.beforeFirst();
			while (rsUsuario.next())
			{
				if(rsUsuario.getInt(1)==tus.getId())
				{
					rsUsuario.updateString("nombres", tus.getNombres());
					rsUsuario.updateString("apellidos", tus.getApellidos());
					rsUsuario.updateString("contrasena", tus.getContrasena());
					rsUsuario.updateString("correo", tus.getCorreo());
					rsUsuario.updateInt("estado",2);
					rsUsuario.updateRow();
					modificado=true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("ERROR modificarUser() "+e.getMessage());
			e.printStackTrace();
		}
		return modificado;
		
	}
	
	public boolean eliminarUser(Tbl_usuario tus)
	{
		boolean eliminado=false;	
		try
		{
			this.listUser();
			rsUsuario.beforeFirst();
			while (rsUsuario.next())
			{
				if(rsUsuario.getInt(1)==tus.getId())
				{
					rsUsuario.updateInt("estado",3);
					rsUsuario.updateRow();
					eliminado=true;
					break;
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("ERROR eliminarUser() "+e.getMessage());
			e.printStackTrace();
		}
		return eliminado;
	}

	
	// Metodo para obtenero un usuario
		public int obtenerIDUser(String carne)
		{
			int id_user = 0;
			try
			{
				PreparedStatement ps = c.prepareStatement("SELECT id from tbl_usuario where carne = ? and estado<>3", 
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
						ResultSet.HOLD_CURSORS_OVER_COMMIT);
				ps.setString(1, carne);
				rsUsuario = ps.executeQuery();
				if(rsUsuario.next())
				{
					id_user = rsUsuario.getInt("id");
					
				}
			}
			catch (Exception e)
			{
				System.out.println("DATOS: ERROR en obtenerIDUser() "+ e.getMessage());
				e.printStackTrace();
			}
			
			return id_user;
		}
		
		
		
		// Metodo para obtenero un usuario
		public Tbl_usuario obtenerNombreEstudiante(String carne_correo)
		{
			Tbl_usuario usuario = new Tbl_usuario();
			try
			{
				PreparedStatement ps = c.prepareStatement("SELECT * from tbl_usuario where carne = ? OR correo = ? and estado<>3", 
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
						ResultSet.HOLD_CURSORS_OVER_COMMIT);
				ps.setString(1, carne_correo);
				ps.setString(2, carne_correo);
				
				rsUsuario = ps.executeQuery();
				
				if(rsUsuario.next())
				{
					usuario.setNombres(rsUsuario.getString("nombres"));;
					usuario.setApellidos(rsUsuario.getString("apellidos"));
					usuario.setId(rsUsuario.getInt("id"));
				}
			}
			catch (Exception e)
			{
				System.out.println("DATOS: ERROR en obtenerNombreEstudiante() "+ e.getMessage());
				e.printStackTrace();
			}
			
			return usuario;
		}
		
		public Tbl_usuario obtenerUser(int id)
		{
			Tbl_usuario tus  = new Tbl_usuario();
			try
			{
				PreparedStatement ps = c.prepareStatement("SELECT * from tbl_usuario where id = ? and estado<>3", 
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
						ResultSet.HOLD_CURSORS_OVER_COMMIT);
				ps.setInt(1, id);
				rsUsuario = ps.executeQuery();
				if(rsUsuario.next())
				{
					
					tus.setId(rsUsuario.getInt("id"));
					tus.setNombres(rsUsuario.getString("nombres"));
					tus.setApellidos(rsUsuario.getString("apellidos"));
					tus.setCarne(rsUsuario.getString("carne"));
					tus.setContrasena(rsUsuario.getString("contrasena"));
					tus.setCorreo(rsUsuario.getString("correo"));
					tus.setEstado(rsUsuario.getInt("estado"));
				}
			}
			catch (Exception e)
			{
				System.out.println("DATOS: ERROR en obtenerIDUser() "+ e.getMessage());
				e.printStackTrace();
			}
			
			return tus;
		}

		// Metodo para obtenero un usuario
	public Tbl_usuario obtenerNombreTutor(int idTutor)
	{
		Tbl_usuario tutor = new Tbl_usuario();
		try	{
			PreparedStatement ps = c.prepareStatement("SELECT * from tbl_usuario where id = ? and estado <>3", 
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
			ResultSet.HOLD_CURSORS_OVER_COMMIT);
			ps.setInt(1, idTutor);
	
			rsUsuario = ps.executeQuery();
			
			if(rsUsuario.next()) {
				tutor.setNombres(rsUsuario.getString("nombres"));;
				tutor.setApellidos(rsUsuario.getString("apellidos"));
				tutor.setId(rsUsuario.getInt("id"));
			}
		}catch (Exception e) {
			
			System.out.println("DATOS: ERROR en obtenerNombreTutor() "+ e.getMessage());
			e.printStackTrace();
		}
				
		return tutor;
	}

		
	public boolean existeCarne(String carne)
	{
		boolean existe = false;
		try
		{
			PreparedStatement ps = c.prepareStatement("SELECT * from tbl_usuario where carne = ?", 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, 
					ResultSet.HOLD_CURSORS_OVER_COMMIT);
			ps.setString(1, carne);
			rsUsuario = ps.executeQuery();
			if(rsUsuario.next())
			{
				existe = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("DATOS: ERROR en existeCarne() "+ e.getMessage());
			e.printStackTrace();
		}
		
		return existe;
	}
	
}


}