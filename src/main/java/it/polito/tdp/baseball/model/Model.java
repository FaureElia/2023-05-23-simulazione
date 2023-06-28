package it.polito.tdp.baseball.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.baseball.db.BaseballDAO;

public class Model {
	private BaseballDAO dao;
	private Graph <People,DefaultEdge> grafo;
	private Map <String, People> idMap;
	//ricorsione
	private List<People> dreamTeam;
	private int salarioMax;
	
	public Model() {
		this.dao=new BaseballDAO();
	}

	public List<People> creaGrafo(int anno, int salario) {
		this.grafo=new SimpleGraph<People, DefaultEdge>(DefaultEdge.class);
		this.idMap=new HashMap<>();
		List<People> vertici=this.dao.getPeople(anno,salario);
		for(People p: vertici) {
			this.idMap.put(p.getPlayerID(), p);
		}
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		//archi
		List<Coppia> coppie=this.dao.getCoppie(this.idMap,anno, salario);
		
		for(Coppia c: coppie) {
			this.grafo.addEdge(c.getP1(), c.getP2());
		}
		System.out.println("grafico con vertici  : "+this.grafo.vertexSet().size());
		System.out.println("grafo con archi: "+ this.grafo.edgeSet().size());
		return vertici;
		
		
		
	}
	
	public int getNodi() {
		return this.grafo.vertexSet().size();
	}
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}

	public String getGradoMassimo() {
		if(this.grafo==null || this.grafo.vertexSet().isEmpty()) {
			return null;
		}
		People massimo=null;
		int gradoMassimo=0;
		for(People p: this.grafo.vertexSet()) {
			if(this.grafo.degreeOf(p)>gradoMassimo) {
				massimo=p;
				gradoMassimo=this.grafo.degreeOf(p);
			}
		}
		if(massimo!=null) {
			return "vertice con grado massimo: "+massimo+", con grado"+gradoMassimo;
		}else {
			return null;
		}
	}

	public int calcolaConnesse() {
		if(this.grafo.vertexSet().isEmpty() || this.grafo==null) {
			return 0;
		}
		ConnectivityInspector<People, DefaultEdge> inspector=new ConnectivityInspector<>(this.grafo);
		return inspector.connectedSets().size();
	}

	public List<People> getDreamTeam() {
		if(this.grafo.vertexSet().isEmpty() || this.grafo==null) {
			return null;
		}
		
		this.dreamTeam=new ArrayList<>();
		this.salarioMax=0;
		List <People> parziale =new ArrayList<>();
		Set<People> rimanenti=new HashSet<>(this.grafo.vertexSet());
		
		for(People p: this.grafo.vertexSet()) {
			parziale.add(p);
			rimanenti.remove(p);
			rimanenti.removeAll(Graphs.neighborSetOf(this.grafo, p));
			cerca(parziale, p.getSalario(),rimanenti);
			rimanenti.add(p);
			rimanenti.addAll(Graphs.neighborSetOf(this.grafo, p));
			parziale.remove(parziale.size()-1);
		}
		
		return this.dreamTeam;
		
		
		
		
		
	}

	private void cerca(List<People> parziale, int salarioCumulativo,Set<People> rimanenti) {
		if(salarioCumulativo>this.salarioMax) {
			System.out.println("soluzione migliore trovata");
			this.salarioMax=salarioCumulativo;
			this.dreamTeam=new ArrayList<>(parziale);
		}
		
		if(rimanenti.size()==0) {
			return;
		}
		
		
		for(People p: this.grafo.vertexSet()) {
			if(rimanenti.contains(p)) {
				parziale.add(p);
				rimanenti.remove(p);
				rimanenti.removeAll(Graphs.neighborSetOf(this.grafo, p));
				cerca(parziale, salarioCumulativo+p.getSalario(),rimanenti);
				rimanenti.add(p);
				rimanenti.addAll(Graphs.neighborSetOf(this.grafo, p));
				parziale.remove(parziale.size()-1);
				
			}
		}
		
	}

	public int getSalarioMax() {
		return salarioMax;
	}

	
	
}