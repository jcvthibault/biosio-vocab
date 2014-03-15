/*
 * iBIOMES - Integrated Biomolecular Simulations
 * Copyright (C) 2014  Julien Thibault, University of Utah
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.utah.bmi.biosio.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Concept (include standard concept and relationship definitions)
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="CONCEPT_MAPPING")
public class Mapping {

	private Long id;
	private String cui;
	private ExternalOntology ontologyDefinition;
	private String ontologyTerm;
	
	/**
	 * New empty mapping
	 */
	public Mapping(){}
	
	/**
	 * New concept mapping
	 * @param cui CUI
	 * @param ontologyName
	 * @param ontologyTerm
	 * 
	 */
	public Mapping(String cui, String ontologyTerm){
		this.cui = cui;
		this.setOntologyTerm(ontologyTerm);
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@Column(name = "cui")
	public String getCui() {return cui;}
	public void setCui(String cui) {this.cui = cui;}
	
	@Column(name = "concept")
	public String getOntologyTerm() {return ontologyTerm;}
	public void setOntologyTerm(String ontologyTerm) {this.ontologyTerm = ontologyTerm;}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ontology", referencedColumnName="id")
	public ExternalOntology getOntologyDefinition() {return ontologyDefinition;}
	public void setOntologyDefinition(ExternalOntology ontologyDefinition) {this.ontologyDefinition = ontologyDefinition;}

}
