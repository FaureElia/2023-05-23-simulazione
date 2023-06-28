package it.polito.tdp.baseball;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


import it.polito.tdp.baseball.model.Model;
import it.polito.tdp.baseball.model.People;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnesse;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnDreamTeam;

    @FXML
    private Button btnGradoMassimo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtYear;

    
    
    @FXML
    void doCalcolaConnesse(ActionEvent event) {
    	int connesse=this.model.calcolaConnesse();
    	if(connesse==0) {
    		this.txtResult.setText("grafico senza archi");
    	}else {
    		this.txtResult.appendText("numero di componenti connesse: "+connesse+"\n");
    	}
    	
    }

    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.btnGradoMassimo.setDisable(true);
		this.btnConnesse.setDisable(true);
		this.btnDreamTeam.setDisable(true);
    	String salarioS=this.txtSalary.getText();
    	String annoS=this.txtYear.getText();
    	if(salarioS=="" || annoS=="") {
    		this.txtResult.setText("inserire un valore!");
    		return;
    	}
    	
    	try {
    		int salario=Integer.parseInt(salarioS);
    		int anno=Integer.parseInt(annoS);
    		if(anno<1871 || anno>2019) {
    			this.txtResult.setText("inserire un anno valido!");
    			return;
    		}
    		
    		List<People> vertici=this.model.creaGrafo(anno,salario);
    		if(vertici!=null && vertici.size()!=0) {
    			this.txtResult.setText("grafico creato\n");
    			this.txtResult.appendText("vertici: "+this.model.getNodi()+"\n");
    			this.txtResult.appendText("archi: "+this.model.getArchi()+"\n");
    			this.btnGradoMassimo.setDisable(false);
    			this.btnConnesse.setDisable(false);
    			this.btnDreamTeam.setDisable(false);
    		}else {
    			this.txtResult.setText("grafo non creato!");
    		}
    		
    		
    		
    	}catch(NumberFormatException e){
    		this.txtResult.setText("inserire valori numerici");
    		
    	}
    	
    }

    
    @FXML
    void doDreamTeam(ActionEvent event) {
    	List<People> dreamTeam=this.model.getDreamTeam();
    	if(dreamTeam==null) {
    		this.txtResult.setText("impossibile trovare dreamTeam");
    	}
    	else{
    		this.txtResult.setText("dream team trovato:\n SALARIO TOTALE: "+this.model.getSalarioMax()+" \n");
    		
    		for(People p: dreamTeam) {
    			this.txtResult.appendText(p+"\n");
    		}
    		
    	}

    }

    
    @FXML
    void doGradoMassimo(ActionEvent event) {
    	String gradoMassimo=this.model.getGradoMassimo();
    	if (gradoMassimo!=null) {
    		this.txtResult.appendText("trovato vertice con grado massimo\n");
    		this.txtResult.appendText(gradoMassimo+"\n");
    	}else {
    		this.txtResult.setText("vertice con grado massimo non trovato");
    		
    	}
    	

    }

    
    @FXML
    void initialize() {
        assert btnConnesse != null : "fx:id=\"btnConnesse\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGradoMassimo != null : "fx:id=\"btnGradoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
