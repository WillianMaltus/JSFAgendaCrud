package com.sales.control;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sales.model.DaoFactory;

@ManagedBean // O papel do MB é intermediar a vinculação entre RN e a camada de visão
@RequestScoped
public class ContatoBean { // Agora esta classe tem o papel de controller (MVC)
	private int id;
	private String nome, email, senha, sexo, endereco;
	// Objeto de listagem dos dados advindos do banco de dados
	public ArrayList<ContatoBean> contatosListDB;

	// fornecer os getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public ArrayList<ContatoBean> getContatosListDB() {
		return contatosListDB;
	}

	public void setContatosListDB(ArrayList<ContatoBean> contatosListDB) {
		this.contatosListDB = contatosListDB;
	}

	@PostConstruct // este método será disparado quando da criação através do método construtor
	public void init() {
		contatosListDB = DaoFactory.getListaContatos();
	}

	// método utilizado pela view (listarcontatos.xhtml) para entrar em modo de
	// edição (apresenta a view de edição de dados)
	public String buscarContato(int id) {
		return DaoFactory.buscarContato(id);
	}

	public String atualizarContato(ContatoBean c) {
		return DaoFactory.atualizarContato(c);
	}
	
	public String deletarContato(int id) {
		return DaoFactory.deletarContato(id);
	}
	
	public String criarContato(ContatoBean a) {
		return DaoFactory.criarContato(a);
	}
	
	public String salvarIdSessao(int id) {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("contatoId", id);
		System.out.println("chegou no salvar id" );
		return "listarRedesSociais.xhtml?faces-redirect=true";
	}

}
