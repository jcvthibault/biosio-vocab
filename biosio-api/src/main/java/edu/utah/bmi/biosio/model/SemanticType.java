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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Semantic type (group of concepts with semantic similarity)
 * @author Julien Thibault, University of Utah
 *
 */
@Entity
@Table(name="SEMANTIC_TYPE")
public class SemanticType {

	private String tid;
	private String term;
	private Description description;
	private Set<Concept> concepts;
	
	public SemanticType(){
	}
	
	public SemanticType(String tid, String term){
		this.tid = tid;
		this.term = term;
	}

	@Id @Column(name = "tid")
	public String getTid() {return tid;}
	public void setTid(String tid) {this.tid = tid;}
	
	@Column(name = "str")
	public String getTerm() {return term;}
	public void setTerm(String term) {this.term = term;}

	@ManyToOne
    @JoinTable(
            name="SEMANTIC_TYPE_DESCRIPTION",
            joinColumns = @JoinColumn( name="cui"),
            inverseJoinColumns = @JoinColumn(name="desc_id")
    )
	public Description getDescription() {return description;}
	public void setDescription(Description description) {this.description = description;}
	
	@ManyToMany
    @JoinTable(
            name="CONCEPT_SEMANTIC_TYPE",
            joinColumns = @JoinColumn( name="tid"),
            inverseJoinColumns = @JoinColumn(name="cui")
    )
	public Set<Concept> getConcepts() {return concepts;}
	public void setConcepts(Set<Concept> concepts) {this.concepts = concepts;}
}
