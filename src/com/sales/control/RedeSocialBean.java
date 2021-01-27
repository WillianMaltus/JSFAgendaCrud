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
public class RedeSocialBean {
	private int id;
	private ContatoBean contato;
	private String nome, link;
	public ArrayList<RedeSocialBean> redesSociaisListDB;
	
	public RedeSocialBean() {
		
	}

	public RedeSocialBean(int id, String nome, String link) {
		super();
		this.id = id;
		this.nome = nome;
		this.link = link;
	}

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public ContatoBean getContato() {
		return contato;
	}

	public void setContato(ContatoBean contato) {
		this.contato = contato;
	}
	
	public ArrayList<RedeSocialBean> getRedesSociaisListDB() {
		return redesSociaisListDB;
	}

	public void setRedesSociaisListDB(ArrayList<RedeSocialBean> redesSociaisListDB) {
		this.redesSociaisListDB = redesSociaisListDB;
	}

	@PostConstruct // este método será disparado quando da criação através do método construtor
	public void init() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Object id = sessionMap.get("contatoId");
		int contatoId = (Integer) id;
		redesSociaisListDB = DaoFactory.getListaRedesSociais(contatoId);
	}
	
	public String criarRedeSocial(RedeSocialBean redeSocialBean) {
		return DaoFactory.criarRedeSocial(redeSocialBean);
	}
	
	public String deletarRedeSocial(int id) {
		return DaoFactory.deletarRedeSocial(id);
	}
	
	public String atualizarRedeSocial(RedeSocialBean redeSocial) {
		return DaoFactory.atualizarRedesSociais(redeSocial);
	}
	
	public String buscarRedeSocial(int id) {
		return DaoFactory.buscarRedeSocial(id);
	}
}
