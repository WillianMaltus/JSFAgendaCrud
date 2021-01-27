package com.sales.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.sales.control.ContatoBean;
import com.sales.control.RedeSocialBean;

//Classe utilizada para manutenção dos dados no Banco de dados (CRUD)
//Mysql 5.17

public class DaoFactory {

	// Classes para manipulação dos dados
	public static Statement stm; // fornece um ambiente para a sentença SQL
	public static Connection con; // Gerencia a conexão com BD
	public static ResultSet rs; // Mantém a lista dos dados lidos no BD
	public static PreparedStatement pstm; // prepara o ambiente para SQL

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String db_url = "jdbc:mysql://localhost:3306/teste?useTimezone=true&serverTimezone=UTC";
			String db_usuario = "root";
			String db_senha = "postgres";
			con = DriverManager.getConnection(db_url, db_usuario, db_senha);
			System.out.println("Conectado");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} catch (ClassNotFoundException cex) {
			cex.printStackTrace();
		}
		return con;
	}

	public static ArrayList<ContatoBean> getListaContatos() {
		ArrayList<ContatoBean> contatosList = new ArrayList<ContatoBean>();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("SELECT * FROM contatos");
			while (rs.next()) {
				// objeto para transição de dados
				ContatoBean contatoBean = new ContatoBean();
				contatoBean.setId(rs.getInt("contato_id"));
				contatoBean.setNome(rs.getString("contato_nome"));
				contatoBean.setEmail(rs.getString("contato_email"));
				contatoBean.setSenha(rs.getString("contato_senha"));
				contatoBean.setSexo(rs.getString("contato_sexo"));
				contatoBean.setEndereco(rs.getString("contato_endereco"));
				// compondo a lista que será apresentada na página JSF
				contatosList.add(contatoBean);
			}
			con.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return contatosList;
	}
	
	public static ArrayList<RedeSocialBean> getListaRedesSociais(int contatoId) {
		System.out.println("Chegou na lista de redes socias");
		ArrayList<RedeSocialBean> RedesSociaisList = new ArrayList<RedeSocialBean>();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("SELECT * FROM redes_sociais rd, contatos c where c.contato_id = " + contatoId + " and c.contato_id = rd.contato_id");
			while (rs.next()) {
				// objeto para transição de dados
				RedeSocialBean redeSocialBean = new RedeSocialBean();
				ContatoBean contatoBean = new ContatoBean();
				redeSocialBean.setId(rs.getInt("redes_sociais_id"));
				redeSocialBean.setNome(rs.getString("nome"));
				redeSocialBean.setLink(rs.getString("link"));
				contatoBean.setId(rs.getInt("contato_id"));
				contatoBean.setNome(rs.getString("contato_nome"));
				
				redeSocialBean.setContato(contatoBean);
				// compondo a lista que será apresentada na página JSF
				RedesSociaisList.add(redeSocialBean);
			}
			con.close();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return RedesSociaisList;
	}

	// método para edição das informações dos contatos
	public static String buscarContato(int id) {
		ContatoBean contatoBean = null;
		//configurar objeto do contato na sessão
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("select * from contatos where contato_id = " + id);
			if (rs != null) {
				rs.next();
				contatoBean = new ContatoBean();
				contatoBean.setId(rs.getInt("contato_id"));
				contatoBean.setNome(rs.getString("contato_nome"));
				contatoBean.setEmail(rs.getString("contato_email"));
				contatoBean.setSexo(rs.getString("contato_sexo"));
				contatoBean.setEndereco(rs.getString("contato_endereco"));
				contatoBean.setSenha(rs.getString("contato_senha"));
			}
			sessionMap.put("contatoSessao", contatoBean);
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/editarContato.xhtml?faces-redirect=true";
	}
	
	public static String buscarRedeSocial(int id) {
		RedeSocialBean redeSocialBean = null;
		//configurar objeto do contato na sessão
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery("select * from redes_sociais where redes_sociais_id = " + id);
			if (rs != null) {
				rs.next();
				redeSocialBean = new RedeSocialBean();
				redeSocialBean.setId(rs.getInt("redes_sociais_id"));
				redeSocialBean.setNome(rs.getString("nome"));
				redeSocialBean.setLink(rs.getString("link"));
			}
			sessionMap.put("redeSocialSessao", redeSocialBean);
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/editarRedeSocial.xhtml?faces-redirect=true";
	}


	public static String atualizarContato(ContatoBean c) {
		try {
			pstm = getConnection().prepareStatement("update contatos set contato_nome =?,"
					+ "contato_email =?, contato_senha =?, contato_sexo =?,"
					+ "contato_endereco =? where contato_id=?");
			pstm.setString(1, c.getNome());
			pstm.setString(2, c.getEmail());
			pstm.setString(3, c.getSenha());
			pstm.setString(4, c.getSexo());
			pstm.setString(5, c.getEndereco());
			pstm.setInt(6, c.getId());
			pstm.executeUpdate();
			con.close();
					
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarcontatos.xhtml?faces-redirect=true";
	}
	
	public static String atualizarRedesSociais(RedeSocialBean r) {
		try {
			pstm = getConnection().prepareStatement("update redes_sociais set nome =?,"
					+ "link =?"
					+ "where redes_sociais_id=?");
			pstm.setString(1, r.getNome());
			pstm.setString(2, r.getLink());
			pstm.setInt(3, r.getId());
			pstm.executeUpdate();
			con.close();
					
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarRedesSociais.xhtml?faces-redirect=true";
	}
	
	public static String deletarContato(int id) {
		deletarRedeSocialContato(id); //deleta os filhos da tabela
		try {
			pstm = getConnection().prepareStatement("delete from contatos where contato_id = " + id);
			pstm.executeUpdate();
			con.close();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarcontatos.xhtml?faces-redirect=true";
	}
	
	public static void deletarRedeSocialContato(int id) {
		try {
			pstm = getConnection().prepareStatement("delete from redes_sociais where contato_id = " + id);
			pstm.executeUpdate();
			con.close();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}
	
	public static String deletarRedeSocial(int id) {
		try {
			pstm = getConnection().prepareStatement("delete from redes_sociais where redes_sociais_id = " + id);
			pstm.executeUpdate();
			con.close();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarRedesSociais.xhtml?faces-redirect=true";
	}
	
	public static String criarContato(ContatoBean c) {
		try {
			pstm = getConnection().prepareStatement("INSERT INTO contatos "
					//+ "(contato_nome, contato_email, contato_senha, contato_sexo, contato_endereco) "
					//+ "values (?, ?, ?, ?, ?)");
					+ "(contato_nome, contato_senha, contato_sexo, contato_endereco) "
					+ "values (?, ?, ?, ?)");
			pstm.setString(1, c.getNome());
			//pstm.setString(2, c.getEmail());
			pstm.setString(2, c.getSenha());
			pstm.setString(3, c.getSexo());
			pstm.setString(4, c.getEndereco());
			pstm.executeUpdate();
			con.close();
					
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarcontatos.xhtml?faces-redirect=true";
	}
	
	public static String criarRedeSocial(RedeSocialBean r) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object id = sessionMap.get("contatoId");
		int contatoId = (Integer) id;
		try {
			pstm = getConnection().prepareStatement("INSERT INTO redes_sociais "
												   	+ "(nome, link, contato_id) "
													+ "values (?, ?, ?)");
			pstm.setString(1, r.getNome());
			pstm.setString(2, r.getLink());
			pstm.setInt(3, contatoId);
			pstm.executeUpdate();
			con.close();
					
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return "/listarRedesSociais.xhtml?faces-redirect=true";
	}
}
