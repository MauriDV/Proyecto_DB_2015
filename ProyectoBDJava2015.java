import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class ProyectoBDJava2015 extends JFrame implements ActionListener{




	private JFrame mainFrame;
	private JButton boton1,boton2,boton3,boton4,boton5,boton6;
	private int ancho = 300;
	public ProyectoBDJava2015() {
		setLayout(null);
		boton1 = new JButton("INSERTAR");
		boton2 = new JButton("ELIMINAR");
		boton3 = new JButton("LISTAR");
		boton4 = new JButton("LISTAR POR JUGADOR");
		boton5 = new JButton("PARTIDAS GANADAS POR JUGADOR");
		boton6 = new JButton("PARTIDAS MAS LARGAS POR JUGADOR");
		boton1.setBounds(10,10,ancho,40);
		boton2.setBounds(10,60,ancho,40);
		boton3.setBounds(10,110,ancho,40);
		boton4.setBounds(10,160,ancho,40);
		boton5.setBounds(10,210,ancho,40);
		boton6.setBounds(10,260,ancho,40);
		add(boton1);
		add(boton2);
		add(boton3);
		add(boton4);
		add(boton5);
		add(boton6);
		boton1.addActionListener(this);
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ProyectoBDJava2015 ventana=new ProyectoBDJava2015();
		ventana.setBounds(500,250,325,340);
		ventana.setVisible(true);
		ventana.setResizable(false);
		ventana.setDefaultCloseOperation(ProyectoBDJava2015.EXIT_ON_CLOSE);
		ventana.setTitle("PROYECTO BASE DE DATOS 2015");

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // Buffer usado para leer lo ingresado por teclado.
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost/Proyecto";
			System.out.print("Nombre de Usuario: ");
			String username = br.readLine();
			System.out.print("Clave: ");
			String password = br.readLine();
			Class.forName(driver);	// Load database driver if not already loaded.
			Connection connection = DriverManager.getConnection(url, username, password); // Establish network connection to database.
			Statement statement = connection.createStatement();
			char opcion;
			do {
				opcion = menu();
				switch (opcion) {
					case 'i' :
						insertar(connection);
						break;
					case 'e' :
						eliminar(statement);
						break;
					case 'l' :
						listar(statement);
						break;
					case 'o' :
						listar_partida_de_jugador(statement);
						break;
					case 'a' :
						listar_partidas_mas_largas(connection);
						break;
					case 'g' :
						listar_partidas_ganadas(connection);
						break;
					case 's' :
						break;
					default :
						System.out.println("\n*** Opcion incorrecta!!!. Intente nuevamente. ***");
						break;
				}
			} while (opcion != 's');
		}
		catch(ClassNotFoundException cnfe) {
			System.err.println("Error loading driver: " + cnfe);
		}
		catch(SQLException sqle) {
			System.err.println("Error connecting: " + sqle);
		}
		catch (IOException ioe) {
			System.err.println("Error I/O");
		}
	}

	public void actionPerformed(ActionEvent e, Statement statement) {
        if (e.getSource()==boton1) {
            listar(statement);
        }
    }

	private void jbInit() throws Exception {
	}

	private static void eliminar(Statement statement) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("\n- Ingrese el nick: ");
			String nick = br.readLine();
			String query = "delete from jugador where nick='"+nick+"'";
			statement.executeUpdate(query);
		}
		catch (IOException ioe) {
			System.err.println("Error I/O");
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void insertar(Connection connection) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("\n- Ingrese el Nick: ");
			String dniI = br.readLine();
			System.out.print("\n- Ingrese el email: ");
			String firstName = br.readLine();
			System.out.print("\n- Ingrese el Nombre y apellido: ");
			String lastName = br.readLine();
			System.out.print("\n- Ingrese la Fecha de Nacimiento (aaaa-mm-dd): ");
			String bornDate = br.readLine();
			System.out.print("\n- Ingrese la edad: ");
			String phoneNumber = br.readLine();
			String query = "insert into jugador (nick,email,nombreAPellido,fechaNac,edad) values (?,?,?,?,?)";
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString (1, dniI);
			preparedStmt.setString (2, firstName);
			preparedStmt.setString (3, lastName);
			preparedStmt.setString (4, bornDate);
			preparedStmt.setString (5, phoneNumber);

			preparedStmt.execute();
		}
		catch (java.lang.NumberFormatException NFE) {
			System.err.println("\n*** El DNI solo puede contener caracteres numericos. ***");
		}
		catch (IOException ioe) {
			System.err.println("Error I/O");
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void listar(Statement statement) {
		try {
			String query = "SELECT * FROM jugador";
			ResultSet resultSet1 = statement.executeQuery(query);
			while(resultSet1.next()) {
				System.out.print("\nid: " + resultSet1.getString(1));
				System.out.print("; nick: " + resultSet1.getString(2));
				System.out.print("; email: " + resultSet1.getString(3));
				System.out.print("; Nombre y Apellido: " + resultSet1.getString(4));
				System.out.print("; Fecha de Nacimiento: " + resultSet1.getString(5));
				System.out.print("; edad: " + resultSet1.getString(6));
				System.out.print("\n");
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void listar_partida_de_jugador(Statement statement) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("\n- Ingrese el Nick: ");
			String id_jugador = br.readLine();
			String query = "SELECT idpartida,fecha,id2,id,id1,nick FROM partida NATURAL JOIN jugador WHERE (id=id1 OR id=id2) AND (nick='"+id_jugador+"')";
			ResultSet resultSet1 = statement.executeQuery(query);
			while(resultSet1.next()) {
				System.out.print("\ncodigo de partida: " + resultSet1.getString(1));
				System.out.print("; fecha: " + resultSet1.getString(2));
				System.out.print("; contrincante: " + resultSet1.getString(3));
				System.out.print("\n");
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void listar_partidas_ganadas(Connection connection) {
		try {
			Statement stat1 = connection.createStatement();
			Statement stat2 = connection.createStatement();
			String query = "SELECT nick FROM jugador";
			ResultSet resultSet1 = stat1.executeQuery(query);
			while(resultSet1.next()) {
				String nick = resultSet1.getString(1);
				String query1 = "select count(nick) from partida natural join jugador where (id=id1) and resultado='Ganador' and nick='"+nick+"'";
				ResultSet resultSet2 = stat2.executeQuery(query1);
				while(resultSet2.next()){
					System.out.print("\n- "+nick+" tiene "+resultSet2.getString(1)+" partidas ganadas");
				}
				resultSet2.close();
			}
			resultSet1.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void listar_partidas_mas_largas(Connection connection) {
		try {
			Statement stat1 = connection.createStatement();
			Statement stat2 = connection.createStatement();
			String query = "SELECT DISTINCT nick FROM partida natural join jugador where id=id1";
			ResultSet resultSet1 = stat1.executeQuery(query);
			while(resultSet1.next()) {
				String nick = resultSet1.getString(1);
				String query1 = "select MAX(TIMEDIFF(horaFin,HoraInicio)) from partida natural join jugador where (id=id1) and nick='"+nick+"'";
				ResultSet resultSet2 = stat2.executeQuery(query1);
				while(resultSet2.next()){
					System.out.println("- "+nick+" su partida mas larga fue de "+resultSet2.getString(1)+"");
				}
				resultSet2.close();
			}
			resultSet1.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Muestra por pantalla el menu y lee por teclado la opcion ingresada por el usuario.
	 * @throws IOException cuando el metodo readLine() falla.
	 * @pre. true.
	 * @post. Muestra por pantalla el menu y lee por teclado la opcion ingresada por el usuario.
	 */
	private static char menu() throws IOException {
		char op = ' ';
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n \t --------------------------------------");
		System.out.println("\t|*************** Menu **************** |");
		System.out.println("\t --------------------------------------");
		System.out.println("\t| - i: Insertar                        |");
		System.out.println("\t| - e: Eliminar                        |");
		System.out.println("\t| - g: Partidas ganadas por jugador    |");
		System.out.println("\t| - a: Partidas mas largas por jugador |");
		System.out.println("\t| - o: Listar por jugador              |");
		System.out.println("\t| - l: Listar                          |");
		System.out.println("\t| - s: Salir                           |");
		System.out.println("\t -------------------------------------- ");
		System.out.print("\tSeleccione la opcion deseada: ");
		try {
			op = Character.toLowerCase((b.readLine()).charAt(0)); // Toma el 1er caracter del string ingresado
		}                                                         // por el usuario (como minuscula).
		catch (IOException ioe) {
			throw new IOException("Error I/O");
		}
		return op;
	}
	

}