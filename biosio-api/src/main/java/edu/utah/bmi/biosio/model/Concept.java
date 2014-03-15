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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Concept (include standard concept and relationship definitions)
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="CONCEPT")
public class Concept {

	private String cui;
	private String term;
	private Boolean isRelationship;
	private Set<Synonym> synonyms;
	private Set<Description> descriptions;
	private Set<Citation> citations;
	private Set<Relationship> relationships;
	private Set<SemanticType> semanticTypes;
	private Set<Mapping> mappings;

	/**
	 * New empty concept
	 */
	public Concept(){
	}
	
	/**
	 * New concept
	 * @param cui CUI
	 * @param term Term
	 * @param isRelationship Relationship flag
	 */
	public Concept(String cui, String term, boolean isRelationship){
		this.cui = cui;
		this.term = term;
		this.isRelationship = isRelationship;
	}

	@Id @Column(name = "cui")
	public String getCui() {return cui;}
	public void setCui(String cui) {this.cui = cui;}
	
	@Column(name = "str")
	public String getTerm() {return term;}
	public void setTerm(String term) {this.term = term;}
	
	@Column(name = "is_rel")
	public Boolean getIsRelationship() {return isRelationship;}
	public void setIsRelationship(Boolean isRelationship) {this.isRelationship = isRelationship;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="cui")
	public Set<Synonym> getSynonyms() {return synonyms;}
	public void setSynonyms(Set<Synonym> synonyms) {this.synonyms = synonyms;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="cui")
	public Set<Description> getDescriptions() {return descriptions;}
	public void setDescriptions(Set<Description> descriptions) {this.descriptions = descriptions;}

	@ManyToMany
    @JoinTable(
            name="CONCEPT_CITATION",
            joinColumns = @JoinColumn( name="cui"),
            inverseJoinColumns = @JoinColumn(name="citation_id")
    )
	public Set<Citation> getCitations() {return citations;}
	public void setCitations(Set<Citation> citations) {this.citations = citations;}

	@ManyToMany
    @JoinTable(
            name="CONCEPT_SEMANTIC_TYPE",
            joinColumns = @JoinColumn( name="cui"),
            inverseJoinColumns = @JoinColumn(name="tid")
    )
	public Set<SemanticType> getSemanticTypes() {return semanticTypes;}
	public void setSemanticTypes(Set<SemanticType> semanticTypes) {this.semanticTypes = semanticTypes;}
	

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="cui1")
	public Set<Relationship> getRelationships() {return relationships;}
	public void setRelationships(Set<Relationship> relationships) {this.relationships = relationships;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="cui")
	public Set<Mapping> getMappings() {return mappings;}
	public void setMappings(Set<Mapping> mappings) {this.mappings = mappings;}

}
